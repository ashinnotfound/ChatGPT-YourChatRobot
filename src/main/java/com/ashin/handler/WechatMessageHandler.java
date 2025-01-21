package com.ashin.handler;

import com.ashin.config.KeywordConfig;
import com.ashin.constant.ChatType;
import com.ashin.entity.bo.ChatBO;
import com.ashin.service.InteractService;
import com.ashin.service.TriggerService;
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
    private final TriggerService triggerService;
    private final KeywordConfig keywordConfig;
    private final BotUtil botUtil;

    public WechatMessageHandler(InteractService interactService, KeywordConfig keywordConfig, TriggerService triggerService, BotUtil botUtil) {
        this.interactService = interactService;
        this.keywordConfig = keywordConfig;
        this.triggerService = triggerService;
        this.botUtil = botUtil;
    }

    @Override
    public String textMsgHandle(BaseMsg baseMsg) {
        //如果是在群聊
        if (baseMsg.isGroupMsg()){
            //去除@再提问
            String prompt = baseMsg.getText().replace("@"+ Core.getInstance().getNickName() + " ", "").trim();
            //存在@机器人的消息就向ChatGPT提问
            if (baseMsg.getText().contains("@"+ Core.getInstance().getNickName())){
                String triggerResponse = triggerService.getResponse(prompt);
                return (triggerResponse == null) ? textResponse(baseMsg.getFromUserName(), prompt) : triggerResponse;
            } else {
                return triggerService.getResponse(baseMsg.getText());
            }
        } else {
            // 不是在群聊 则直接回复
            String triggerResponse = triggerService.getResponse(baseMsg.getText());
            return (triggerResponse == null) ? textResponse(baseMsg.getFromUserName(), baseMsg.getText()) : triggerResponse;
        }
    }

    private String textResponse(String userName, String content) {
        if (keywordConfig.getReset().equals(content)){
            botUtil.resetPrompt(userName);
            return "重置会话成功";
        }else {
            ChatBO chatBO = new ChatBO();
            chatBO.setPrompt(content);
            chatBO.setSessionId(userName);
            if (content.startsWith(keywordConfig.getImage())) {
                chatBO.setPrompt(content.replaceFirst(keywordConfig.getImage() + " ", ""));
                chatBO.setChatType(ChatType.IMAGE);
            } else if (content.startsWith(keywordConfig.getAudio())) {
                return "微信暂不支持语音回复";
            } else {
                chatBO.setPrompt(content);
                chatBO.setChatType(ChatType.TEXT);
            }
            return interactService.chat(chatBO).getStringResult();
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
