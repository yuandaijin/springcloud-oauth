package com.jingcheng.user.controller;

import com.jingcheng.user.config.AgeConfig;
import com.jinghceng.util.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Create by yuandaijin  on 2019-03-12 18:06
 * version 1.0
 */
@RestController
@RequestMapping("/config")
@RefreshScope
public class ConfigController {

    @Autowired
    private AgeConfig ageConfig;

    @RequestMapping("/get")
    public Result get() {
        return Result.success(ageConfig.getAge());
    }
}
