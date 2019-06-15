package com.jingcheng.auth.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * Create by yuandaijin  on 2019-04-18 16:47
 * version 1.0
 */
@Data
@Component
@ConfigurationProperties("gate.ignore.authentication")
@RefreshScope
public class IgnoreConfig {

    private String startWith;
}
