package com.ashin.service;

import com.ashin.bo.ChatBO;
import com.ashin.exception.ChatException;

/**
 * 交互服务
 *
 * @author ashinnotfound
 * @date 2022/12/10
 */
public interface InteractService {
    /**
     * 聊天
     *
     * @param chatBO 聊天BO
     * @return {@link String}
     * @throws ChatException 聊天异常
     */
    String chat(ChatBO chatBO) throws ChatException;
}
