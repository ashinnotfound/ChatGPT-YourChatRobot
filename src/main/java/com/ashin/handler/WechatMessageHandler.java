package com.ashin.handler;

import com.ashin.config.KeywordConfig;
import com.ashin.entity.bo.ChatBO;
import com.ashin.exception.ChatException;
import com.ashin.service.InteractService;
import com.ashin.util.BotUtil;
import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import cn.zhouyafeng.itchat4j.core.Core;
import cn.zhouyafeng.itchat4j.face.IMsgHandlerFace;

/**
 * 微信消息处理程序
 *
 * @author ashinnotfound
 * @date 2023/03/19
 */
public class WechatMessageHandler implements IMsgHandlerFace {
    private final InteractService interactService;
    private final KeywordConfig keywordConfig;
    private final BotUtil botUtil;

    public WechatMessageHandler(InteractService interactService, KeywordConfig keywordConfig, BotUtil botUtil) {
        this.interactService = interactService;
        this.keywordConfig = keywordConfig;
        this.botUtil = botUtil;
    }

    @Override
    public String textMsgHandle(BaseMsg baseMsg) {
        //如果是在群聊
        if (baseMsg.isGroupMsg()){
            //存在@机器人的消息就向ChatGPT提问
            if (baseMsg.getText().contains("@"+ Core.getInstance().getNickName())){
                //去除@再提问
                String prompt = baseMsg.getText().replace("@"+ Core.getInstance().getNickName() + " ", "").trim();
                return textResponse(baseMsg.getFromUserName(), prompt);
            }
        }else {
            //不是在群聊 则直接回复
            return textResponse(baseMsg.getFromUserName(), baseMsg.getText());
        }
        return null;
    }

    private String textResponse(String userName, String content) {
        if (keywordConfig.getReset().equals(content)){
            botUtil.resetPrompt(userName);
            return "重置会话成功";
        }else {
            ChatBO chatBO = new ChatBO();
            chatBO.setPrompt(content);
            chatBO.setSessionId(userName);
            chatBO.setAiDraw(content.startsWith(keywordConfig.getDraw()));
            String response;
            try {
                response = interactService.chat(chatBO);
            } catch (ChatException e) {
                response = e.getMessage();
            }
            return response;
        }
    }

    @Override
    public String picMsgHandle(BaseMsg baseMsg) {
        return null;
    }

    @Override
    public String voiceMsgHandle(BaseMsg baseMsg) {
        return null;
    }

    @Override
    public String viedoMsgHandle(BaseMsg baseMsg) {
        return null;
    }

    @Override
    public String nameCardMsgHandle(BaseMsg baseMsg) {
        return null;
    }

    @Override
    public void sysMsgHandle(BaseMsg baseMsg) {
    }

    @Override
    public String verifyAddFriendMsgHandle(BaseMsg baseMsg) {
        return null;
    }

    @Override
    public String mediaMsgHandle(BaseMsg baseMsg) {
        return null;
    }
}
