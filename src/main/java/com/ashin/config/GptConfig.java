package com.ashin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * gpt配置
 *
 * @author ashinnotfound
 * @date 2023/03/04
 */
@Data
@Component
@ConfigurationProperties("gpt")
public class GptConfig {
    private String model;
    private Integer maxToken;
    private Double temperature;
    private List<String> basicPrompt;
    private List<String> apiKey;
    private Long ofSeconds;
}
