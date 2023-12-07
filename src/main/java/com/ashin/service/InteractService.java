package com.ashin.service;

import com.ashin.entity.bo.ChatBO;
import com.ashin.entity.dto.ChatResultDTO;

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
     * @return {@code Object}
     */
    ChatResultDTO chat(ChatBO chatBO);
}
