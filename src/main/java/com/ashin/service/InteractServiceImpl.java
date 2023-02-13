package com.ashin.service;

import com.ashin.bo.ChatBO;
import com.ashin.exception.ChatException;
import com.ashin.util.BotUtil;
import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import org.springframework.stereotype.Service;

/**
 * 交互服务impl
 *
 * @author ashinnotfound
 * @date 2022/12/10
 */
@Service
public class InteractServiceImpl implements InteractService{
    @Override
    public String chat(ChatBO chatBO) throws ChatException {

        String prompt = BotUtil.getPrompt(chatBO.getSessionId(), chatBO.getPrompt());

        //向gpt提问
        OpenAiService openAiService = BotUtil.getOpenAiService();
        CompletionRequest.CompletionRequestBuilder completionRequestBuilder = BotUtil.getCompletionRequestBuilder();

        CompletionRequest completionRequest = completionRequestBuilder.prompt(prompt).build();
        String answer = openAiService.createCompletion(completionRequest).getChoices().get(0).getText();

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