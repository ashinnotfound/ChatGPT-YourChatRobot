package com.ashin.service;

import com.ashin.bo.ChatBO;
import com.ashin.util.ChatbotUtil;
import com.github.plexpt.chatgpt.Chatbot;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 交互服务impl
 *
 * @author ashinnotfound
 * @date 2022/12/10
 */
@Service
public class InteractServiceImpl implements InteractService{
    @Override
    public String chat(ChatBO chatBO) {
        //根据sessionId获取对应的Chatbot实体类，实现会话隔离
        Chatbot chatbot = ChatbotUtil.getChatbot(chatBO.getSessionId());
        Map<String, Object> chatResponse = chatbot.getChatResponse(chatBO.getQuestion());

        System.out.println("question: "+chatBO.getQuestion());
        System.out.println("answer: "+chatResponse.get("message"));

        return (String) chatResponse.get("message");
    }
}