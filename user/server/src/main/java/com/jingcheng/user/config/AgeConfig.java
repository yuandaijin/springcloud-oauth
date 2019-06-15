package com.jingcheng.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * Create by yuandaijin  on 2019-03-12 18:09
 * version 1.0
 */
@Data
@Component
@ConfigurationProperties("test")
@RefreshScope
public class AgeConfig {
    private Integer age;
}
