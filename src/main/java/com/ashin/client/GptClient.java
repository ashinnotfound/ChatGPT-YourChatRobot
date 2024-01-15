package com.ashin.client;

import com.ashin.config.GptConfig;
import com.ashin.config.ProxyConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.client.OpenAiApi;
import com.theokanning.openai.service.OpenAiService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Component;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

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
                openAiServiceList.add(customOpenAiService(apiKey, gptConfig.getBaseUrl()));
                log.info("apiKey为 {} 的账号初始化成功", apiKey);
            }
        }
    }

    private OpenAiService customOpenAiService(String token, String baseUrl) {
        ObjectMapper mapper = OpenAiService.defaultObjectMapper();
        OkHttpClient client = OpenAiService.defaultClient(token, Duration.ofSeconds(gptConfig.getOfSeconds()));
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return new OpenAiService(retrofit.create(OpenAiApi.class), client.dispatcher().executorService());
    }
}
