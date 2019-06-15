package com.jingcheng.auth.server.controller;

import com.jinghceng.util.utils.SystemErrorType;
import com.jinghceng.util.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Mr.kim on 2019/03/11.
 * Time:10:08
 * ProjectName:oauth
 */
@RestController
public class LogoutController {
    @Autowired
    private ConsumerTokenServices consumerTokenServices;

    @DeleteMapping(value = "/loginOut")
    public @ResponseBody
    Result revokeToken(String access_token) {
        if (consumerTokenServices.revokeToken(access_token)) {
            return  Result.success();
        } else {
            return  Result.fail(SystemErrorType.LOGIN_OUT_ERROR);
        }
    }
}
