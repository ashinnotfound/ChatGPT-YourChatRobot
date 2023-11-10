package com.ashin.handler;

import com.ashin.config.KeywordConfig;
import com.ashin.config.QqConfig;
import com.ashin.entity.bo.ChatBO;
import com.ashin.exception.ChatException;
import com.ashin.service.InteractService;
import com.ashin.util.BotUtil;
import com.ashin.util.ImageUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.MessageTooLargeException;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListenerHost;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.message.data.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.io.File;

/**
 * QQ消息处理程序
 *
 * @author ashinnotfound
 * @date 2023/2/1
 */
@Component
@Slf4j
public class QqMessageHandler implements ListenerHost {
    @Resource
    private InteractService interactService;
    @Resource
    private QqConfig qqConfig;
    @Resource
    private KeywordConfig keywordConfig;
    @Resource
    private BotUtil botUtil;

    /**
     * 好友消息事件
     *
     * @param event 事件
     */
    @EventHandler
    public void onFriendMessageEvent(FriendMessageEvent event){
        ChatBO chatBO = new ChatBO();
        chatBO.setSessionId(String.valueOf(event.getSubject().getId()));
        String prompt = event.getMessage().contentToString().trim();
        response(event, chatBO, prompt);
    }

    /**
     * 群聊消息事件
     *
     * @param event 事件
     */
    @EventHandler
    public void onGroupMessageEvent(GroupMessageEvent event){
        ChatBO chatBO = new ChatBO();
        chatBO.setSessionId(String.valueOf(event.getSubject().getId()));
        if (event.getMessage().contains(new At(event.getBot().getId()))) {
            //存在@机器人的消息就向ChatGPT提问
            //去除@再提问
            String prompt = event.getMessage().contentToString().replace("@" + event.getBot().getId(), "").trim();
            response(event, chatBO, prompt);
        }
    }
    private void response(@NotNull MessageEvent event, ChatBO chatBO, String prompt) {
        if (keywordConfig.getReset().equals(prompt)) {
            //检测到重置会话指令
            botUtil.resetPrompt(chatBO.getSessionId());
            event.getSubject().sendMessage("重置会话成功");
        } else {
            String response;
            try {
                chatBO.setPrompt(prompt);
                chatBO.setAiDraw(prompt.startsWith(keywordConfig.getDraw()));
                response = interactService.chat(chatBO);
            }catch (ChatException e){
                response = e.getMessage();
            }
            try {
                if (chatBO.isAiDraw() && !qqConfig.getReturnDrawByURL()){
                    File file = ImageUtil.download(response);
                    Contact.sendImage(event.getSubject(), file);
                    if (!file.delete()){
                        log.warn("图片({})删除失败, 请注意存储空间", file.getAbsolutePath());
                    }
                }else {
                    MessageChain messages = new MessageChainBuilder()
                            .append(new QuoteReply(event.getMessage()))
                            .append(response)
                            .build();
                    event.getSubject().sendMessage(messages);
                }
            }catch (MessageTooLargeException e){
                //信息太大，无法引用，采用直接回复
                event.getSubject().sendMessage(response);
            }
        }
    }

    /**
     * 好友申请事件
     *
     * @param event 事件
     */
    @EventHandler
    public void onNewFriendRequestEvent(NewFriendRequestEvent event){
        if (qqConfig.getAcceptNewFriend()){
            event.accept();
        }
    }

    /**
     * 群聊邀请事件
     *
     * @param event 事件
     */
    @EventHandler
    public void onNewGroupRequestEvent(BotInvitedJoinGroupRequestEvent event){
        if (qqConfig.getAcceptNewGroup()){
            event.accept();
        }
    }
}