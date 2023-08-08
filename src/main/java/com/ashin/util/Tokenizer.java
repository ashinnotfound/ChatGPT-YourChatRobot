package com.ashin.util;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.ModelType;
import com.theokanning.openai.completion.chat.ChatMessage;
import lombok.var;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 分词器
 *
 * @author ashinnotfound
 * @date 2023/08/06
 */
@Component
public class Tokenizer {

    private final EncodingRegistry registry = Encodings.newDefaultEncodingRegistry();

    /**
     * 计算消息token
     * via https://jtokkit.knuddels.de/docs/getting-started/recipes/chatml
     *
     * @param model    模型
     * @param messages 消息
     * @return int
     */
    public int countMessageTokens(ModelType model, List<ChatMessage> messages) {
        Encoding encoding = registry.getEncodingForModel(model);
        int tokensPerMessage = 0;
        if (model.getName().startsWith("gpt-4")) {
            tokensPerMessage = 3;
        } else if (model.getName().startsWith("gpt-3.5-turbo")) {
            tokensPerMessage = 4; // every message follows <|start|>{role/name}\n{content}<|end|>\n
        }

        int sum = 0;
        for (final var message : messages) {
            sum += tokensPerMessage;
            sum += encoding.countTokens(message.getContent());
            sum += encoding.countTokens(message.getRole());
        }

        sum += 3; // every reply is primed with <|start|>assistant<|message|>

        return sum;
    }

    public int countMessageTokens(String modelName, List<ChatMessage> messages) {
        return countMessageTokens(getModelTypeByName(modelName), messages);
    }

    /**
     * 根据名字获取模型类型
     *
     * @param modelName 模型名称
     * @return {@code ModelType}
     */
    public ModelType getModelTypeByName(String modelName){
        if (ModelType.GPT_4.getName().equals(modelName)){
            return ModelType.GPT_4;
        } else if (ModelType.GPT_4_32K.getName().equals(modelName)){
            return ModelType.GPT_4_32K;
        } else if (ModelType.GPT_3_5_TURBO_16K.getName().equals(modelName)){
            return ModelType.GPT_3_5_TURBO_16K;
        } else {
            return ModelType.GPT_3_5_TURBO;
        }
    }
}
