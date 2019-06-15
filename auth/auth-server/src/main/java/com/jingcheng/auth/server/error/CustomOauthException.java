package com.jingcheng.auth.server.error;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jinghceng.util.vo.Result;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * 自定义auth认证异常返回
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonSerialize(using = CustomOauthExceptionSerializer.class)
class CustomOauthException extends OAuth2Exception {

    private final  String  refresh_token="Invalid refresh token (expired)";//刷新token过期单独处理

    private final Result result;

    CustomOauthException(OAuth2Exception oAuth2Exception) {
        super(oAuth2Exception.getSummary(), oAuth2Exception);
        if(oAuth2Exception.getMessage().startsWith(refresh_token)){
            this.result = Result.authFail("999",refresh_token);
        }else{
            this.result = Result.authFail("100",oAuth2Exception.getMessage());
        }
    }
}