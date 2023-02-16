package com.ashin.util;

import com.ashin.config.AccountConfig;
import com.ashin.exception.ChatException;
import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

/**
 * chatbot工具类
 *
 * @author ashinnotfound
 * @date 2023/2/1
 */
@Component
@Slf4j
public class BotUtil {
    @Resource
    public void setAccountConfig(AccountConfig accountConfig){
        BotUtil.accountConfig = accountConfig;
    }
    private static AccountConfig accountConfig;

    private static final Map<String, Queue<String>> PROMPT_MAP = new HashMap<>();
    private static final Map<OpenAiService, Integer> COUNT_FOR_OPEN_AI_SERVICE = new HashMap<>();
    private static CompletionRequest.CompletionRequestBuilder completionRequestBuilder;

    @PostConstruct
    public void init(){
        completionRequestBuilder = CompletionRequest.builder().model(accountConfig.getModel()).temperature(accountConfig.getTemperature()).maxTokens(accountConfig.getMaxToken());
        for (OpenAiService openAiService : accountConfig.getOpenAiServiceList()){
            COUNT_FOR_OPEN_AI_SERVICE.put(openAiService, 0);
        }
    }

    public static OpenAiService getOpenAiService(){
        //获取使用次数最小的openAiService 否则获取map中的第一个
        Optional<OpenAiService> openAiServiceToUse = COUNT_FOR_OPEN_AI_SERVICE.entrySet().stream()
        .min(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey);
        if (openAiServiceToUse.isPresent()){
            COUNT_FOR_OPEN_AI_SERVICE.put(openAiServiceToUse.get(), COUNT_FOR_OPEN_AI_SERVICE.get(openAiServiceToUse.get()) + 1);
            return  openAiServiceToUse.get();
        }else {
            COUNT_FOR_OPEN_AI_SERVICE.put(COUNT_FOR_OPEN_AI_SERVICE.keySet().iterator().next(), COUNT_FOR_OPEN_AI_SERVICE.get(COUNT_FOR_OPEN_AI_SERVICE.keySet().iterator().next()) + 1);
            return COUNT_FOR_OPEN_AI_SERVICE.keySet().iterator().next();
        }
    }
    public static CompletionRequest.CompletionRequestBuilder getCompletionRequestBuilder(){
        return completionRequestBuilder;
    }

    public static String getPrompt(String sessionId, String newPrompt) throws ChatException {
        StringBuilder prompt = new StringBuilder(accountConfig.getBasicPrompt());
        if (PROMPT_MAP.containsKey(sessionId)){
            for (String s : PROMPT_MAP.get(sessionId)){
                prompt.append(s);
            }
        }
        prompt.append("User: ").append(newPrompt).append("\nChatGPT: ");

        //一个汉字大概两个token
        //预设回答的文字是提问文字数量的两倍
        if (accountConfig.getMaxToken() < (prompt.toString().length() + newPrompt.length() * 2) * 2){
            if (null == PROMPT_MAP.get(sessionId) || null == PROMPT_MAP.get(sessionId).poll()){
                throw new ChatException("问题太长了");
            }
            return getPrompt(sessionId, newPrompt);
        }

        return prompt.toString();
    }

    public static void updatePrompt(String sessionId, String prompt, String answer){
        if (PROMPT_MAP.containsKey(sessionId)){
            PROMPT_MAP.get(sessionId).offer("User: " + prompt + "\nChatGPT: " + answer);
        }else {
            Queue<String> queue = new LinkedList<>();
            queue.offer("User: " + prompt + "\nChatGPT: " + answer);
            PROMPT_MAP.put(sessionId, queue);
        }
    }

    public static void resetPrompt(String sessionId){
        PROMPT_MAP.remove(sessionId);
    }

    public static Map<OpenAiService, Integer> getCountForOpenAiService(){
        return COUNT_FOR_OPEN_AI_SERVICE;
    }
}
