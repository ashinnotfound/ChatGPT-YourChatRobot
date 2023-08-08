package com.ashin.client;

import com.ashin.config.GptConfig;
import com.ashin.config.ProxyConfig;
import com.theokanning.openai.service.OpenAiService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class GptClient {
    @Resource
    private ProxyConfig proxyConfig;
    @Resource
    private GptConfig gptConfig;

    // openai服务list
    @Getter
    private List<OpenAiService> openAiServiceList;

    @PostConstruct
    public void init() {
        //配置代理
        if (!proxyConfig.getHost().isEmpty() && !proxyConfig.getPort().isEmpty()) {
            System.setProperty("http.proxyHost", proxyConfig.getHost());
            System.setProperty("https.proxyHost", proxyConfig.getHost());
            System.setProperty("http.proxyPort", proxyConfig.getPort());
            System.setProperty("https.proxyPort", proxyConfig.getPort());
            log.info("代理设置成功!!! {}:{}", proxyConfig.getHost(), proxyConfig.getPort());
        }
        openAiServiceList = new ArrayList<>();
        for (String apiKey : gptConfig.getApiKey()) {
            apiKey = apiKey.trim();
            if (!apiKey.isEmpty()) {
                openAiServiceList.add(new OpenAiService(apiKey, Duration.ofSeconds(1000)));
                log.info("apiKey为 {} 的账号初始化成功", apiKey);
            }
        }
    }
}
