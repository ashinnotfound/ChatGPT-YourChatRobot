package com.ashin.handler;

import com.ashin.entity.bo.ChatBO;
import com.ashin.exception.ChatException;
import com.ashin.service.InteractService;
import com.ashin.util.BotUtil;
import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import cn.zhouyafeng.itchat4j.core.Core;
import cn.zhouyafeng.itchat4j.face.IMsgHandlerFace;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 微信消息处理程序
 *
 * @author ashinnotfound
 * @date 2023/03/19
 */
@Component
public class WechatMessageHandler implements IMsgHandlerFace {

    @Resource
    private InteractService interactService;

    private static final String RESET_WORD = "重置会话";

    @Override
    public String textMsgHandle(BaseMsg baseMsg) {
        ChatBO chatBO = new ChatBO();
        //如果是在群聊
        if (baseMsg.isGroupMsg()){
            //存在@机器人的消息就向ChatGPT提问
            if (baseMsg.getText().contains("@"+ Core.getInstance().getNickName())){
                //去除@再提问
                String prompt = baseMsg.getText().replace("@"+ Core.getInstance().getNickName(), "").trim();
                if (RESET_WORD.equals(prompt)){
                    BotUtil.resetPrompt(baseMsg.getFromUserName());
                    return "重置会话成功";
                }else {
                    chatBO.setPrompt(prompt);
                    chatBO.setSessionId(baseMsg.getFromUserName());
                    String response;
                    try {
                        response = interactService.chat(chatBO);
                    } catch (ChatException e) {
                        response = e.getMessage();
                    }
                    return response;
                }
            }
        }else {
            //不是在群聊 则直接回复
            if (RESET_WORD.equals(baseMsg.getText())){
                BotUtil.resetPrompt(baseMsg.getFromUserName());
                return "重置会话成功";
            }else {
                chatBO.setPrompt(baseMsg.getText());
                chatBO.setSessionId(baseMsg.getFromUserName());
                String response;
                try {
                    response = interactService.chat(chatBO);
                } catch (ChatException e) {
                    response = e.getMessage();
                }
                return response;
            }
        }
        return null;
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
