package com.ashin.controller;

import com.ashin.bo.ChatBO;
import com.ashin.service.InteractService;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListenerHost;
import net.mamoe.mirai.event.events.MessageEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 事件处理
 *
 * @author ashinnotfound
 * @date 2022/12/10
 */
@Component
public class MessageEventHandler implements ListenerHost {
    @Resource
    private InteractService interactService;

    /**
     * 监听消息并把ChatGPT的回答发送到对应qq/群
     *
     * @param event 事件 ps:此处是MessageEvent 故所有的消息事件都会被监听
     */
    @EventHandler
    public void onMessage(@NotNull MessageEvent event) {
        ChatBO chatBO = new ChatBO();
        chatBO.setSessionId(String.valueOf(event.getSender().getId()));
        chatBO.setQuestion(event.getMessage().contentToString());
        event.getSubject().sendMessage(interactService.chat(chatBO));
    }
}