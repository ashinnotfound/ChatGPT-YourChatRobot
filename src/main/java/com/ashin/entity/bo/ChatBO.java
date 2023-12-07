package com.ashin.entity.bo;

import com.ashin.constant.ChatType;
import lombok.Data;

/**
 * 聊天BO
 *
 * @author ashinnotfound
 * @date 2023/02/01
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
    private String prompt;
    /**
     * 是否ai画图功能
     */
    private ChatType chatType;
}
