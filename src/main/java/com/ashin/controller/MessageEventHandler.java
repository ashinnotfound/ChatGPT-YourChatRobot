package com.ashin.controller;

import com.ashin.bo.ChatBO;
import com.ashin.service.InteractService;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListenerHost;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.SingleMessage;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

/**
 * 事件处理
 *
 * @author ashinnotfound
 * @date 2023/2/1
 */
@Component
public class MessageEventHandler implements ListenerHost {
    @Resource
    private InteractService interactService;

    /**
     * 监听消息并把ChatGPT的回答发送到对应qq/群
     * 注：如果是在群聊则需@
     *
     * @param event 事件 ps:此处是MessageEvent 故所有的消息事件都会被监听
     */
    @EventHandler
    public void onMessage(@NotNull MessageEvent event) {
        ChatBO chatBO = new ChatBO();
        chatBO.setSessionId(String.valueOf(event.getSubject().getId()));
        if(event.getBot().getGroups().contains(event.getSubject().getId())) {
            //如果是在群聊
            //遍历收到的消息元素
            for (SingleMessage singleMessage : event.getMessage()) {
                if (singleMessage.equals(new At(event.getBot().getId()))) {
                    //存在@机器人的消息就向ChatGPT提问
                    //去除@再提问
                    chatBO.setPrompt(event.getMessage().contentToString().replace("@" + event.getBot().getId(), "").trim());
                    event.getSubject().sendMessage(interactService.chat(chatBO));
                    break;
                }
            }
        }else {
            //不是在群聊 则直接回复
            chatBO.setPrompt(event.getMessage().contentToString());
            event.getSubject().sendMessage(interactService.chat(chatBO));
        }
    }
}