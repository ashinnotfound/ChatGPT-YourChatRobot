package com.ashin.service.impl;

import com.ashin.entity.bo.ChatBO;
import com.ashin.exception.ChatException;
import com.ashin.service.InteractService;
import com.ashin.util.BotUtil;
import com.theokanning.openai.OpenAiHttpException;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    @Resource
    private BotUtil botUtil;

    @Override
    public String chat(ChatBO chatBO) throws ChatException {

        List<ChatMessage> prompt = botUtil.buildPrompt(chatBO.getSessionId(), chatBO.getPrompt());
        if (prompt == null){
            throw new ChatException("提问失败: 提问内容过长");
        }

        //向gpt提问
        ChatCompletionRequest completionRequest = botUtil.getCompletionRequestBuilder().messages(prompt).build();
        ChatMessage answer = null;
        try {
            answer = botUtil.getOpenAiService().createChatCompletion(completionRequest).getChoices().get(0).getMessage();
        }catch (OpenAiHttpException e){
            log.error("向gpt提问失败，提问内容：{}，\n原因：{}\n", chatBO.getPrompt(), e.getMessage(), e);
            // https://platform.openai.com/docs/guides/error-codes/api-errors
            if (401 == e.statusCode){
                throw new ChatException("提问失败: 无效的apikey, 请确保apikey正确且你拥有权限");
            }else if(429 == e.statusCode){
                throw new ChatException("提问过于频繁 或者 apikey余额不足");
            }else if(500 == e.statusCode) {
                throw new ChatException("提问失败: openai服务异常, 请稍后重试");
            }else if(503 == e.statusCode) {
                throw new ChatException("提问失败: openai服务过载, 请稍后重试");
            }
        }
        if (null == answer){
            throw new ChatException("提问失败: 未知错误, 若频繁出现请提issue");
        }

        prompt.add(answer);

        return answer.getContent().trim();
    }
}
