package com.ashin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 代理配置
 *
 * @author ashinnotfound
 * @date 2023/03/04
 */
@Data
@Component
@ConfigurationProperties("proxy")
public class ProxyConfig {
    private String host;
    private String port;
}
