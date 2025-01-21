package com.ashin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * 关键词回复配置
 * @author InwardfFlow
 * @date 2023/12/13
 */

@Data
@Component
@ConfigurationProperties("trigger")
public class TriggerConfig {
    private Map<String, String> triggers;
    @PostConstruct
    public void init() {
        System.out.println("TriggerConfig initialized. Triggers: " + triggers);
    }
}
