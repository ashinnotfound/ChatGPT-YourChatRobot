package com.ashin.client;

import com.ashin.config.QqConfig;
import com.ashin.handler.QqMessageHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.auth.BotAuthorization;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.stereotype.Component;
import xyz.cssxsh.mirai.tool.FixProtocolVersion;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
@Slf4j
public class QqBotClient {

    @Resource
    private QqMessageHandler qqMessageHandler;
    @Resource
    private QqConfig qqConfig;

    // qq机器人实例
    @Getter
    private net.mamoe.mirai.Bot qqBot;

    @PostConstruct
    public void init() {
        if (qqConfig.getEnable()) {
            //登录 登陆协议有ANDROID_PHONE, ANDROID_PAD, ANDROID_WATCH, IPAD, MACOS
            try {
                log.info("正在登录qq,请按提示操作：");
                if (qqConfig.getMethod() == 1) {
                    //密码登录
                    qqBot = BotFactory.INSTANCE.newBot(qqConfig.getAccount(), qqConfig.getPassword().trim(), new BotConfiguration() {{
                        setProtocol(BotConfiguration.MiraiProtocol.ANDROID_PAD);
                    }});
                    //使用临时修复插件
                    FixProtocolVersion.update();
                } else {
                    //扫码登陆
                    qqBot = BotFactory.INSTANCE.newBot(qqConfig.getAccount(), BotAuthorization.byQRCode(), configuration -> configuration.setProtocol(BotConfiguration.MiraiProtocol.ANDROID_WATCH));
                }

                qqBot.login();
                log.info("成功登录账号为 {} 的qq, 登陆方式为 {}", qqConfig.getAccount(), qqConfig.getMethod() == 1 ? "密码登录" : "扫码登陆");
                //订阅监听事件
                qqBot.getEventChannel().registerListenerHost(qqMessageHandler);
            } catch (Exception e) {
                log.error("登陆失败，qq账号为 {}, 登陆方式为 {} ，原因：{}", qqConfig.getAccount(), qqConfig.getMethod() == 1 ? "密码登录" : "扫码登陆", e.getMessage());
            }
        }
    }
}
