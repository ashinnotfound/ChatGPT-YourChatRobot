package com.ashin.service.impl;

import com.ashin.entity.bo.ChatBO;
import com.ashin.exception.ChatException;
import com.ashin.service.InteractService;
import com.ashin.util.BotUtil;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import retrofit2.HttpException;

import java.util.List;

/**
 * 交互服务impl
 *
 * @author ashinnotfound
 * @date 2022/12/10
 */
@Service
@Slf4j
public class InteractServiceImpl implements InteractService {
    @Override
    public String chat(ChatBO chatBO) throws ChatException {

        List<ChatMessage> prompt = BotUtil.getPrompt(chatBO.getSessionId(), chatBO.getPrompt());

        //向gpt提问
        OpenAiService openAiService = BotUtil.getOpenAiService();
        ChatCompletionRequest.ChatCompletionRequestBuilder completionRequestBuilder = BotUtil.getCompletionRequestBuilder();

        ChatCompletionRequest completionRequest = completionRequestBuilder.messages(prompt).build();
        ChatMessage answer = null;
        try {
            answer = openAiService.createChatCompletion(completionRequest).getChoices().get(0).getMessage();
        }catch (HttpException e){
            log.error("向gpt提问失败，提问内容：{}，原因：{}", chatBO.getPrompt(), e.getMessage(), e);
            if (500 == e.code() || 503 == e.code() || 429 == e.code()){
                log.warn("尝试重新提问");
                try {
                    //可能是同时请求过多，尝试重新发送
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    log.error("进程休眠失败，原因：{}", ex.getMessage(), ex);
                    throw new RuntimeException(ex);
                }
                return chat(chatBO);
            }else if(400 == e.code()){
                log.warn("尝试重新提问");
                //http400错误，大概率是历史会话太多导致token超出限制
                BotUtil.deleteFirstPrompt(chatBO.getSessionId());
                return chat(chatBO);
            }
        }
        if (null == answer){
            throw new ChatException("GPT可能暂时不想理你");
        }

        prompt.add(answer);
        BotUtil.updatePrompt(chatBO.getSessionId(), prompt);

        return answer.getContent().trim();
    }
}
