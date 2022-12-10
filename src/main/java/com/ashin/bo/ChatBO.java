package com.ashin.bo;

import lombok.Data;

/**
 * 聊天BO
 *
 * @author ashinnotfound
 * @date 2022/12/10
 */
@Data
public class ChatBO {
    /**
     * 会话id
     */
    private String sessionId;
    /**
     * 问题
     */
    private String question;
}
