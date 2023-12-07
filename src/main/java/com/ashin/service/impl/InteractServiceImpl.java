package com.ashin.service.impl;

import com.ashin.config.GptConfig;
import com.ashin.entity.bo.ChatBO;
import com.ashin.entity.dto.ChatResultDTO;
import com.ashin.service.InteractService;
import com.ashin.util.BotUtil;
import com.theokanning.openai.OpenAiHttpException;
import com.theokanning.openai.audio.CreateSpeechRequest;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.image.CreateImageRequest;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URL;
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
    @Resource
    private GptConfig gptConfig;

    @Override
    public ChatResultDTO chat(ChatBO chatBO) {
        List<ChatMessage> prompt = botUtil.buildPrompt(chatBO.getSessionId(), chatBO.getPrompt());
        ChatMessage answer = null;
        ChatResultDTO res = new ChatResultDTO();
        if (prompt == null) {
            res.setStringResult("提问失败: 提问内容过长");
            return res;
        }

        try {
            //向gpt提问
            switch (chatBO.getChatType()) {
                case TEXT:
                    ChatCompletionRequest completionRequest = botUtil.getCompletionRequestBuilder().messages(prompt).build();
                    answer = botUtil.getOpenAiService().createChatCompletion(completionRequest).getChoices().get(0).getMessage();
                    res.setStringResult(answer.getContent().trim());
                    break;
                case IMAGE:
                    CreateImageRequest createImageRequest = CreateImageRequest.builder().n(1).size("1024x1024").prompt(chatBO.getPrompt()).model("dall-e-3").quality(gptConfig.getImageQuality()).style(gptConfig.getImageStyle()).build();
                    answer = new ChatMessage();
                    answer.setRole("assistant");
                    String url = botUtil.getOpenAiService().createImage(createImageRequest).getData().get(0).getUrl();
                    answer.setContent(url);
                    res.setStringResult(url);
                    res.setInputStreamResult(new URL(url).openConnection().getInputStream());
                    break;
                case AUDIO:
                    String input = botUtil.getOpenAiService().createChatCompletion(botUtil.getCompletionRequestBuilder().messages(prompt).build()).getChoices().get(0).getMessage().getContent();
                    answer = new ChatMessage();
                    answer.setRole("assistant");
                    answer.setContent(input);
                    CreateSpeechRequest createSpeechRequest = CreateSpeechRequest.builder().input(input).model(gptConfig.getAudioModel()).voice(gptConfig.getAudioVoice()).speed(gptConfig.getAudioSpeed()).build();
                    ResponseBody speech = botUtil.getOpenAiService().createSpeech(createSpeechRequest);
                    res.setStringResult(input);
                    res.setInputStreamResult(speech.byteStream());
                    break;
            }
        } catch (OpenAiHttpException e) {
            log.error("向gpt提问失败，提问内容：{}，\n原因：{}\n", chatBO.getPrompt(), e.getMessage(), e);
            // https://platform.openai.com/docs/guides/error-codes/api-errors
            if (400 == e.statusCode) {
                res.setStringResult("提问失败: 你的提问被OPENAI安全系统拒绝，请检查是否有敏感词等");
            } else if (401 == e.statusCode) {
                res.setStringResult("提问失败: 无效的apikey, 请确保apikey正确且你拥有权限");
            } else if (429 == e.statusCode) {
                res.setStringResult("提问失败: 提问过于频繁 或者 apikey余额不足");
            } else if (500 == e.statusCode) {
                res.setStringResult("提问失败: openai服务异常, 请稍后重试");
            } else if (503 == e.statusCode) {
                res.setStringResult("提问失败: openai服务过载, 请稍后重试");
            } else {
                res.setStringResult("提问失败: " + e.getMessage());
            }
        } catch (IOException e) {
            res.setStringResult("提问失败: " + e.getMessage());
        }

        prompt.add(answer);

        return res;
    }
}
