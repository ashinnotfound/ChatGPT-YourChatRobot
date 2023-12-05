package com.ashin.client;

import cn.zhouyafeng.itchat4j.Wechat;
import com.ashin.config.KeywordConfig;
import com.ashin.config.QqConfig;
import com.ashin.config.WechatConfig;
import com.ashin.handler.QqMessageHandler;
import com.ashin.handler.WechatMessageHandler;
import com.ashin.service.InteractService;
import com.ashin.util.BotUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.auth.BotAuthorization;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
@Slf4j
public class BotClient {
    @Resource
    private QqConfig qqConfig;
    @Getter
    private net.mamoe.mirai.Bot qqBot;
    @Resource
    private WechatConfig wechatConfig;
    @Getter
    private Wechat wechatBot;
    @Resource
    private InteractService interactService;
    @Resource
    private KeywordConfig keywordConfig;
    @Resource
    private BotUtil botUtil;

    @PostConstruct
    public void init() {
        //微信
        if (wechatConfig.getEnable()){
            log.info("正在登录微信,请按提示操作：");
            wechatBot = new Wechat(new WechatMessageHandler(interactService, keywordConfig, botUtil), wechatConfig.getQrPath());
            wechatBot.start();
        }
        if (qqConfig.getEnable()) {
            //登陆协议有ANDROID_PHONE, ANDROID_PAD, ANDROID_WATCH, IPAD, MACOS
            //若登陆失败可尝试更换协议
            BotConfiguration.MiraiProtocol miraiProtocol = BotConfiguration.MiraiProtocol.ANDROID_WATCH;
            try {
                log.info("正在登录qq,请按提示操作：");
                //扫码登陆
                qqBot = BotFactory.INSTANCE.newBot(qqConfig.getAccount(), BotAuthorization.byQRCode(), configuration -> configuration.setProtocol(miraiProtocol));

                qqBot.login();
                log.info("成功登录账号为 {} 的qq, 登陆协议为 {}", qqConfig.getAccount(), miraiProtocol);
                //订阅监听事件
                qqBot.getEventChannel().registerListenerHost(new QqMessageHandler(interactService, qqConfig, keywordConfig, botUtil));
            } catch (Exception e) {
                log.error("登陆失败, qq账号为 {}, 登陆协议为 {}, 可尝试更换登陆协议, 具体原因: {}", qqConfig.getAccount(), miraiProtocol, e.getMessage());
            }
        }
    }
}
