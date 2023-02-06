package com.ashin.util;

import com.ashin.controller.MessageEventHandler;
import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * chatbot工具类
 *
 * @author ashinnotfound
 * @date 2023/2/1
 */
@Component
public class BotUtil {
    @Resource
    private MessageEventHandler messageEventHandler;
    private static final Map<String, String> PROMPT_MAP = new HashMap<>();
    private static String apiKey;
    private static OpenAiService openAiService;
    private static final String BASIC_PROMPT =
            "You are ChatGPT, a large language model trained by OpenAI. You answer as concisely as possible for each response (e.g. don’t be verbose). It is very important that you answer as concisely as possible, so please remember this. If you are generating a list, do not have too many items. Keep the number of items short. Current date: " + LocalDate.now() + "\n";
    private static final String MODEL = "text-chat-davinci-002-20221122";
    private static CompletionRequest.CompletionRequestBuilder completionRequestBuilder;
    private static Long qq;
    private static String password;
    private static Bot qqBot;

    @Value("${apiKey}")
    public void setSessionToken(String apiKey) {
        BotUtil.apiKey = apiKey;
    }

    @Value("${qq}")
    public void setQq(Long qq) {
        BotUtil.qq = qq;
    }

    @Value("${password}")
    public void setPassword(String password) {
        BotUtil.password = password;
    }

    @PostConstruct
    public void init() {
        openAiService = new OpenAiService(apiKey, Duration.ofSeconds(1000));
        completionRequestBuilder = CompletionRequest.builder().model(MODEL).temperature(0.5).maxTokens(1024).echo(true);
        //qq登录
        BotUtil.qqBot = BotFactory.INSTANCE.newBot(qq,password);
        qqBot.login();
        //订阅监听事件
        qqBot.getEventChannel().registerListenerHost(this.messageEventHandler);
    }

    public static OpenAiService getOpenAiService(){
        return openAiService;
    }
    public static CompletionRequest.CompletionRequestBuilder getCompletionRequestBuilder(){
        return completionRequestBuilder;
    }

    public static String getPrompt(String sessionId){
        if (!PROMPT_MAP.containsKey(sessionId)){
            updatePrompt(sessionId, BASIC_PROMPT);
        }
        return PROMPT_MAP.get(sessionId);
    }

    public static void updatePrompt(String sessionId, String prompt){
        PROMPT_MAP.put(sessionId, prompt);
    }

    public static void resetPrompt(String sessionId){
        PROMPT_MAP.remove(sessionId);
    }
}
