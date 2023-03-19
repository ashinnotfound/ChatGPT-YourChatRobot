package com.ashin.config;

import com.ashin.handler.QqMessageHandler;
import com.ashin.handler.WechatMessageHandler;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import cn.zhouyafeng.itchat4j.Wechat;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.stereotype.Component;
import xyz.cssxsh.mirai.tool.FixProtocolVersion;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * bot配置
 *
 * @author ashinnotfound
 * @date 2023/02/13
 */
@Slf4j
@Data
@Component
public class BotConfig {
    @Resource
    ProxyConfig proxyConfig;
    @Resource
    QqConfig qqConfig;
    @Resource
    WechatConfig wechatConfig;
    @Resource
    ChatgptConfig chatgptConfig;

    private Bot qqBot;
    @Resource
    private QqMessageHandler qqMessageHandler;

    @Resource
    private WechatMessageHandler wechatMessageHandler;

    private List<OpenAiService> openAiServiceList;
    private ChatMessage basicPrompt;
    private Integer maxToken;
    private Double temperature;
    private String model;

    @PostConstruct
    public void init() {
        //配置代理
        if (null != proxyConfig.getHost() && !"".equals(proxyConfig.getHost())) {
            System.setProperty("http.proxyHost", proxyConfig.getHost());
            System.setProperty("https.proxyHost", proxyConfig.getHost());
        }
        if (null != proxyConfig.getPort() && !"".equals(proxyConfig.getPort())) {
            System.setProperty("http.proxyPort", proxyConfig.getPort());
            System.setProperty("https.proxyPort", proxyConfig.getPort());
        }

        //ChatGPT
        model = "gpt-3.5-turbo";
//        model = "gpt-3.5-turbo-0301" 这是快照版本
        maxToken = 2048;
        temperature = 0.8;
//        你可以通过设定basicPrompt来指定人格
//        basicPrompt = new ChatMessage("system", "You are a helpful assistant");
        openAiServiceList = new ArrayList<>();
        for (String apiKey : chatgptConfig.getApiKey()) {
            apiKey = apiKey.trim();
            if (!"".equals(apiKey)) {
                openAiServiceList.add(new OpenAiService(apiKey, Duration.ofSeconds(1000)));
                log.info("apiKey为 {} 的账号初始化成功", apiKey);
            }
        }

        //微信
        if (wechatConfig.getEnable()){
            Wechat wechatBot = new Wechat(wechatMessageHandler, wechatConfig.getQrPath());
            wechatBot.start();
        }

        //qq
        if (qqConfig.getEnable()){
            Long qq = qqConfig.getAccount();
            String password = qqConfig.getPassword();
            //登录 登陆协议有ANDROID_PHONE, ANDROID_PAD, ANDROID_WATCH, IPAD, MACOS 其中MACOS较为稳定
            BotConfiguration.MiraiProtocol protocol = BotConfiguration.MiraiProtocol.MACOS;
            try {
                qqBot = BotFactory.INSTANCE.newBot(qq, password.trim(), new BotConfiguration() {{
                    setProtocol(protocol);
                }});

                //使用临时修复插件
                FixProtocolVersion.update();

                qqBot.login();
                log.info("成功登录账号为 {} 的qq, 登陆方式为 {}", qq, protocol);
                //订阅监听事件
                qqBot.getEventChannel().registerListenerHost(qqMessageHandler);
            } catch (Exception e) {
                log.error("登陆失败，qq账号为 {}, 登陆方式为 {} ，原因：{}", qq, protocol, e.getMessage());
            }
        }
    }
}
