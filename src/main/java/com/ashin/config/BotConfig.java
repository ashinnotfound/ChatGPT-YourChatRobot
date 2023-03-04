package com.ashin.config;

import com.ashin.handler.MessageEventHandler;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
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
import java.util.ArrayList;
import java.util.List;

/**
 * qq配置
 *
 * @author ashinnotfound
 * @date 2023/03/04
 */
@Data
@Component
@ConfigurationProperties("qq")
class QqConfig{
    private Long account;
    private String password;
}
/**
 * chatgpt配置
 *
 * @author ashinnotfound
 * @date 2023/03/04
 */
@Data
@Component
@ConfigurationProperties("chatgpt")
class ChatgptConfig{
    private List<String> apiKey;
}
/**
 * 代理配置
 *
 * @author ashinnotfound
 * @date 2023/03/04
 */
@Data
@Component
@ConfigurationProperties("proxy")
class ProxyConfig{
    private String host;
    private String port;
}

/**
 * 帐户配置
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
    ChatgptConfig chatgptConfig;

    private Bot qqBot;
    @Resource
    private MessageEventHandler messageEventHandler;

    private List<OpenAiService> openAiServiceList;
    private ChatMessage basicPrompt;
    private Integer maxToken;
    private Double temperature;
    private String model;

    @PostConstruct
    public void init() {
        //配置代理
        System.setProperty("http.proxyHost", proxyConfig.getHost());
        System.setProperty("http.proxyPort", proxyConfig.getPort());
        System.setProperty("https.proxyHost", proxyConfig.getHost());
        System.setProperty("https.proxyPort", proxyConfig.getPort());

        //ChatGPT
        model = "gpt-3.5-turbo";
//        model = "gpt-3.5-turbo-0301" 这是快照版本
        maxToken = 2048;
        temperature = 0.8;
//        你可以通过设定basicPrompt来指定人格
//        basicPrompt = new ChatMessage("system", "You are a helpful assistant");
        openAiServiceList = new ArrayList<>();
        for (String apiKey : chatgptConfig.getApiKey()){
            apiKey = apiKey.trim();
            if (!"".equals(apiKey)){
                openAiServiceList.add(new OpenAiService(apiKey, Duration.ofSeconds(1000)));
                log.info("apiKey为 {} 的账号初始化成功", apiKey);
            }
        }

        //qq
        Long qq = qqConfig.getAccount();
        String password = qqConfig.getPassword();
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
