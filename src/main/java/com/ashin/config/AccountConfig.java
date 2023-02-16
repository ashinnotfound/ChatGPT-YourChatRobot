package com.ashin.config;

import com.ashin.controller.MessageEventHandler;
import com.theokanning.openai.OpenAiService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 帐户配置
 *
 * @author ashinnotfound
 * @date 2023/02/13
 */
@Slf4j
@Data
@Component
@ConfigurationProperties("account")
public class AccountConfig {
    private Long qq;
    private String password;
    private Bot qqBot;
    @Resource
    private MessageEventHandler messageEventHandler;

    private List<String> apiKey;
    private List<OpenAiService> openAiServiceList;
    private String basicPrompt;
    private Integer maxToken;
    private Double temperature;
    private String model;

    @PostConstruct
    public void init() {
        //ChatGPT
        model = "text-davinci-003";
        maxToken = 2048;
        temperature = 0.8;
        basicPrompt =
                "You are ChatGPT, a large language model trained by OpenAI. You answer as concisely as possible for each response (e.g. don’t be verbose). It is very important that you answer as concisely as possible, so please remember this. If you are generating a list, do not have too many items. Keep the number of items short. Current date: " + LocalDate.now() + "\n";
        openAiServiceList = new ArrayList<>();
        for (String apiKey : apiKey){
            apiKey = apiKey.trim();
            if (!"".equals(apiKey)){
                openAiServiceList.add(new OpenAiService(apiKey, Duration.ofSeconds(1000)));
                log.info("apiKey为 {} 的账号初始化成功", apiKey);
            }
        }

        //qq
        //登录
        BotConfiguration.MiraiProtocol[] protocolArray = BotConfiguration.MiraiProtocol.values();
        int loginCounts = 1;
        for (BotConfiguration.MiraiProtocol protocol : protocolArray){
            try {
                log.warn("正在尝试第 {} 次， 使用 {} 的方式进行登录", loginCounts++, protocol);
                qqBot = BotFactory.INSTANCE.newBot(qq, password.trim(), new BotConfiguration(){{setProtocol(protocol);}});
                qqBot.login();
                log.info("成功登录账号为 {} 的qq, 登陆方式为 {}",qq, protocol);
                //订阅监听事件
                qqBot.getEventChannel().registerListenerHost(this.messageEventHandler);
                break;
            }catch (Exception e){
                log.error("登陆失败，qq账号为 {}, 登陆方式为 {} ，原因：{}", qq, protocol, e.getMessage());
                if (loginCounts > protocolArray.length){
                    log.error("经过多种登录方式仍然登陆失败，可能是密码错误或者受风控影响，请尝试修改密码、绑定手机号等方式提高qq安全系数或者待会再试试");
                    System.exit(-1);
                }
            }
        }
    }
}
