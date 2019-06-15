package com.jingcheng.auth.server.integration.authenticator;

import com.jingcheng.auth.server.integration.IntegrationAuthentication;
import com.jingcheng.auth.server.utils.VerificationCodeUtil;
import com.jinghceng.util.vo.Result;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Component;

/**
 * 集成验证码认证
 *
 * @author yuandiajin
 * @date 2018-3-31
 **/
@Component
public class VerificationCodeIntegrationAuthenticator extends UsernamePasswordAuthenticator {

    private final static String VERIFICATION_CODE_AUTH_TYPE = "vc";


    @Override
    public void prepare(IntegrationAuthentication integrationAuthentication) {
        String vcToken = integrationAuthentication.getAuthParameter("vc_token");
        String vcCode = integrationAuthentication.getAuthParameter("vc_code");
        //验证验证码
        Result<Boolean> result = VerificationCodeUtil.validate(vcToken, vcCode);
        if (!result.isSuccess()) {
            throw new OAuth2Exception("验证码错误");
        }
    }

    @Override
    public boolean support(IntegrationAuthentication integrationAuthentication) {
        return VERIFICATION_CODE_AUTH_TYPE.equals(integrationAuthentication.getAuthType());
    }


}
