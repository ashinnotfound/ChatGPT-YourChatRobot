package com.ashin.service;

import com.ashin.config.TriggerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 关键词回复服务
 * @author InwardFlow
 * @date 2023/12/13
 */

@Slf4j
@Service
public class TriggerService {
    private final TriggerConfig triggerConfig;

    @Autowired
    public TriggerService(TriggerConfig triggerConfig) {
        this.triggerConfig = triggerConfig;
        log.debug("TriggerConfig: {}", triggerConfig); // 添加日志
    }

    /**
     * 关键词回复
     * @param input 输入
     * @return 输出，如果不触发关键词返回null，触发了就直接返回输出内容
     */
    public String getResponse(String input) {
        Map<String, String> triggers = triggerConfig.getTriggers();

        return triggers.entrySet()
                .stream()
                .filter(entry -> input.contains(entry.getKey()))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(null);
    }

}
