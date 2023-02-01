package com.ashin.service;

import com.ashin.bo.ChatBO;
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
    public String chat(ChatBO chatBO) {

        String prompt = BotUtil.getPrompt(chatBO.getSessionId()) + "User: " + chatBO.getPrompt() + "\nChatGPT: ";

        //向gpt提问
        OpenAiService openAiService = BotUtil.getOpenAiService();
        CompletionRequest.CompletionRequestBuilder completionRequestBuilder = BotUtil.getCompletionRequestBuilder();
        CompletionRequest completionRequest = completionRequestBuilder.prompt(prompt).build();
        String text = openAiService.createCompletion(completionRequest).getChoices().get(0).getText();
        String[] textSpilled = text.split(chatBO.getPrompt() + "\nChatGPT: ");
        String answer = textSpilled[textSpilled.length - 1];

        BotUtil.updatePrompt(chatBO.getSessionId(), prompt + answer);
        answer = textSpilled[textSpilled.length - 1].replace("<|im_end|>", "").trim();

        return answer;
    }
}