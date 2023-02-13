package com.ashin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * 帐户配置
 *
 * @author ashinnotfound
 * @date 2023/02/13
 */
@Data
@Component
@ConfigurationProperties
public class AccountConfig {
    private List<String> apiKey;

    private Long qq;
    private String password;
}
