package com.ashin.service;

import com.ashin.bo.ChatBO;

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
     * @return {@link String ChatGPT的回复}
     */
    String chat(ChatBO chatBO);
}
