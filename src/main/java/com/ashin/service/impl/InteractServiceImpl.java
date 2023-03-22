package com.ashin.service.impl;

import com.ashin.entity.bo.ChatBO;
import com.ashin.exception.ChatException;
import com.ashin.service.InteractService;
import com.ashin.util.BotUtil;
import com.theokanning.openai.OpenAiHttpException;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

        List<ChatMessage> prompt = BotUtil.buildPrompt(chatBO.getSessionId(), chatBO.getPrompt());

        //向gpt提问
        OpenAiService openAiService = BotUtil.getOpenAiService();
        ChatCompletionRequest.ChatCompletionRequestBuilder completionRequestBuilder = BotUtil.getCompletionRequestBuilder();

        ChatCompletionRequest completionRequest = completionRequestBuilder.messages(prompt).build();
        ChatMessage answer = null;
        try {
            answer = openAiService.createChatCompletion(completionRequest).getChoices().get(0).getMessage();
        }catch (OpenAiHttpException e){
            log.error("向gpt提问失败，提问内容：{}，\n原因：{}\n", chatBO.getPrompt(), e.getMessage(), e);
            if (429 == e.statusCode){
                throw new ChatException("提问过于频繁(openai限制接口为20prompt/60s)，过一会再来问我吧");
            }else if(400 == e.statusCode){
                log.warn("尝试删除较前会话记录并重新提问");
                //http400错误，大概率是历史会话太多导致token超出限制
                if (BotUtil.deleteFirstPrompt(chatBO.getSessionId())){
                    return chat(chatBO);
                }else {
                    log.warn("删除失败");
                    throw new ChatException("提问太长啦，尝试调高token或者减少提问字数");
                }
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
