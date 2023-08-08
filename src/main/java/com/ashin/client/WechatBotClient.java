package com.ashin.client;

import cn.zhouyafeng.itchat4j.Wechat;
import com.ashin.config.WechatConfig;
import com.ashin.handler.WechatMessageHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
@Slf4j
public class WechatBotClient {
    @Resource
    private WechatMessageHandler wechatMessageHandler;
    @Resource
    private WechatConfig wechatConfig;

    @Getter
    private Wechat wechatBot;
    @PostConstruct
    public void init() {
        //微信
        if (wechatConfig.getEnable()){
            log.info("正在登录微信,请按提示操作：");
            wechatBot = new Wechat(wechatMessageHandler, wechatConfig.getQrPath());
            wechatBot.start();
        }
    }
}
