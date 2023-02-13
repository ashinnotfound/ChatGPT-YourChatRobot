package com.ashin.service;

import com.ashin.bo.ChatBO;
import com.ashin.exception.ChatException;
import com.ashin.util.BotUtil;
import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import retrofit2.HttpException;

/**
 * 交互服务impl
 *
 * @author ashinnotfound
 * @date 2022/12/10
 */
@Service
@Slf4j
public class InteractServiceImpl implements InteractService{
    @Override
    public String chat(ChatBO chatBO) throws ChatException {

        String prompt = BotUtil.getPrompt(chatBO.getSessionId(), chatBO.getPrompt());

        //向gpt提问
        OpenAiService openAiService = BotUtil.getOpenAiService();
        CompletionRequest.CompletionRequestBuilder completionRequestBuilder = BotUtil.getCompletionRequestBuilder();

        CompletionRequest completionRequest = completionRequestBuilder.prompt(prompt).build();
        String answer = null;
        try {
            answer = openAiService.createCompletion(completionRequest).getChoices().get(0).getText();
        }catch (HttpException e){
            log.error("向gpt提问失败，提问内容：{}，原因：{}", chatBO.getPrompt(), e.getMessage(), e);
            if (500 == e.code() || 503 == e.code() || 429 == e.code()){
                log.info("尝试重新发送");
                try {
                    //可能是同时请求过多，尝试重新发送
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    log.error("进程休眠失败，原因：{}", ex.getMessage(), ex);
                    throw new RuntimeException(ex);
                }
                return chat(chatBO);
            }
        }
        if (null == answer){
            throw new ChatException("GPT可能暂时不想理你");
        }

        //去除gpt假设的用户提问
        int userIndex = answer.indexOf("User:");
        if (-1 != userIndex){
            answer = answer.substring(0, userIndex - 1) + "<|im_end|>";
        }

        BotUtil.updatePrompt(chatBO.getSessionId(), chatBO.getPrompt(), answer);
        answer = answer.replace("<|im_end|>", "").trim();
        return answer;
    }
}
