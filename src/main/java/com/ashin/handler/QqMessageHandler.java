package com.ashin.handler;

import com.ashin.config.KeywordConfig;
import com.ashin.config.QqConfig;
import com.ashin.constant.ChatType;
import com.ashin.entity.bo.ChatBO;
import com.ashin.entity.dto.ChatResultDTO;
import com.ashin.service.InteractService;
import com.ashin.service.TriggerService;
import com.ashin.util.BotUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListenerHost;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.ExternalResource;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

/**
 * QQ消息处理程序
 *
 * @author ashinnotfound
 * @date 2023/2/1
 */
@Slf4j
public class QqMessageHandler implements ListenerHost {
    private final InteractService interactService;
    private final TriggerService triggerService;
    private final QqConfig qqConfig;
    private final KeywordConfig keywordConfig;
    private final BotUtil botUtil;

    public QqMessageHandler(InteractService interactService, QqConfig qqConfig, KeywordConfig keywordConfig, BotUtil botUtil, TriggerService triggerService) {
        this.interactService = interactService;
        this.qqConfig = qqConfig;
        this.keywordConfig = keywordConfig;
        this.botUtil = botUtil;
        this.triggerService = triggerService;
    }

    /**
     * 好友消息事件
     *
     * @param event 事件
     */
    @EventHandler
    public void onFriendMessageEvent(FriendMessageEvent event) {
        ChatBO chatBO = new ChatBO();
        chatBO.setSessionId(String.valueOf(event.getSubject().getId()));
        String prompt = event.getMessage().contentToString().trim();
        String triggerResponse = triggerService.getResponse(prompt);
        // 检测用户发送的内容是否触发关键词
        if (triggerResponse != null) {
            event.getSubject().sendMessage(
                    new MessageChainBuilder()
                            .append(new QuoteReply(event.getMessage()))
                            .append(triggerResponse)
                            .build()
            );
            return;
        }
        response(event, chatBO, prompt);
    }

    /**
     * 群聊消息事件
     *
     * @param event 事件
     */
    @EventHandler
    public void onGroupMessageEvent(GroupMessageEvent event) {
        ChatBO chatBO = new ChatBO();
        chatBO.setSessionId(String.valueOf(event.getSubject().getId()));

        if (event.getMessage().contains(new At(event.getBot().getId()))) {
            //存在@机器人的消息就向ChatGPT提问
            //去除@再提问
            String prompt = event.getMessage().contentToString().replace("@" + event.getBot().getId(), "").trim();
            // 检测用户发送的内容是否触发关键词
            String triggerResponse = triggerService.getResponse(prompt);
            if (triggerResponse != null) {
                event.getSubject().sendMessage(
                        new MessageChainBuilder()
                                .append(new QuoteReply(event.getMessage()))
                                .append(triggerResponse)
                                .build()
                );
                return;
            }
            response(event, chatBO, prompt);
        }
    }

    private void response(@NotNull MessageEvent event, ChatBO chatBO, String prompt) {
        if (keywordConfig.getReset().equals(prompt)) {
            //检测到重置会话指令
            botUtil.resetPrompt(chatBO.getSessionId());
            event.getSubject().sendMessage("重置会话成功");
        } else {
            if (prompt.startsWith(keywordConfig.getImage())) {
                chatBO.setPrompt(prompt.replaceFirst(keywordConfig.getImage() + " ", ""));
                chatBO.setChatType(ChatType.IMAGE);
            } else if (prompt.startsWith(keywordConfig.getAudio())) {
                chatBO.setPrompt(prompt.replaceFirst(keywordConfig.getAudio() + " ", ""));
                chatBO.setChatType(ChatType.AUDIO);
            } else {
                chatBO.setPrompt(prompt);
                chatBO.setChatType(ChatType.TEXT);
            }
            ChatResultDTO response = interactService.chat(chatBO);
            switch (chatBO.getChatType()) {
                case TEXT:
                    event.getSubject().sendMessage(
                            new MessageChainBuilder()
                                    .append(new QuoteReply(event.getMessage()))
                                    .append(response.getStringResult())
                                    .build()
                    );
                    break;
                case IMAGE:
                    if (response.getInputStreamResult() == null) {
                        event.getSubject().sendMessage(
                                new MessageChainBuilder()
                                        .append(new QuoteReply(event.getMessage()))
                                        .append("ai画图失败, 以下图片url: ").append(response.getStringResult())
                                        .build()
                        );
                    }
                    try (InputStream inputStream = response.getInputStreamResult()) {
                        event.getSubject().sendMessage(
                                new MessageChainBuilder()
                                        .append(new QuoteReply(event.getMessage()))
                                        .append(Contact.uploadImage(event.getSubject(), inputStream))
                                        .build()
                        );
                    } catch (IOException e) {
                        event.getSubject().sendMessage(
                                new MessageChainBuilder()
                                        .append(new QuoteReply(event.getMessage()))
                                        .append("ai画图失败, 以下图片url: ").append(response.getStringResult())
                                        .build()
                        );
                    }
                    break;
                case AUDIO:
                    if (response.getBytesResult() == null) {
                        event.getSubject().sendMessage("语音回复失败, 以下为文本回复: " + response.getStringResult());
                    }
                    OfflineAudio audio = null;
                    byte[] audioBytes = response.getBytesResult();
                    try (ExternalResource externalResource = ExternalResource.create(audioBytes)) {
                        if (event.getSubject() instanceof Group) {
                            Group group = (Group) event.getSubject();
                            audio = group.uploadAudio(externalResource);
                        } else if (event.getSubject() instanceof Friend) {
                            Friend user = (Friend) event.getSubject();
                            audio = user.uploadAudio(externalResource);
                        }
                        if (audio != null) {
                            event.getSubject().sendMessage(audio);
                        } else {
                            event.getSubject().sendMessage("语音回复失败, 以下为文本回复: " + response.getStringResult());
                        }
                    } catch (IOException e) {
                        event.getSubject().sendMessage("语音回复失败, 以下为文本回复: " + response.getStringResult());
                    }
            }
        }
    }

    /**
     * 好友申请事件
     *
     * @param event 事件
     */
    @EventHandler
    public void onNewFriendRequestEvent(NewFriendRequestEvent event) {
        if (qqConfig.getAcceptNewFriend()) {
            event.accept();
        }
    }

    /**
     * 群聊邀请事件
     *
     * @param event 事件
     */
    @EventHandler
    public void onNewGroupRequestEvent(BotInvitedJoinGroupRequestEvent event) {
        if (qqConfig.getAcceptNewGroup()) {
            event.accept();
        }
    }
}
