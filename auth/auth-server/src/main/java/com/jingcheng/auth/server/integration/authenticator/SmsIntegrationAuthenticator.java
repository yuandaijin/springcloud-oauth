package com.jingcheng.auth.server.integration.authenticator;

import com.jingcheng.auth.server.integration.AbstractPreparableIntegrationAuthenticator;
import com.jingcheng.auth.server.integration.IntegrationAuthentication;
import com.jingcheng.auth.server.utils.VerificationCodeUtil;
import com.jingcheng.user.client.UserClient;
import com.jingcheng.user.common.UserVo;
import com.jinghceng.util.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Component;

/**
 * 短信验证码集成认证
 *
 * @author yuandiajin
 * @date 2018-3-31
 **/
@Component
public class SmsIntegrationAuthenticator extends AbstractPreparableIntegrationAuthenticator {

    @Autowired
    private UserClient userClient;

    @Autowired
    private PasswordEncoder passwordEncoder;


    private final static String SMS_AUTH_TYPE = "sms";

    /**
     * 手机验证码登录包含两个参数 (smsCode   phone)
     *
     * @param integrationAuthentication
     * @return
     */
    @Override
    public UserVo authenticate(IntegrationAuthentication integrationAuthentication) {

        //这里password是验证码
        String password = integrationAuthentication.getAuthParameter("code");
        //这里username是手机号
        String phone = integrationAuthentication.getAuthParameter("phone");
        //发布事件，可以监听事件进行自动注册用户

        //通过手机号码查询用户
        Result<UserVo> result = this.userClient.findByPhone(phone);
        if (result.isSuccess()) {
            //将密码设置为验证码
            result.getData().setPassword(passwordEncoder.encode(password));
            integrationAuthentication.setUsername(result.getData().getUsername());
            //发布事件，可以监听事件进行消息通知
            return result.getData();
        }
        return null;
    }

    @Override
    public void prepare(IntegrationAuthentication integrationAuthentication) {
//        String smsToken = integrationAuthentication.getAuthParameter("sms_token");
        String smsCode = integrationAuthentication.getAuthParameter("code");
        String phone = integrationAuthentication.getAuthParameter("phone");
        Result<Boolean> result = VerificationCodeUtil.validate(smsCode, phone);
        if (!result.isSuccess()) {
            throw new OAuth2Exception("验证码错误或已过期");
        }
    }


    @Override
    public boolean support(IntegrationAuthentication integrationAuthentication) {
        return SMS_AUTH_TYPE.equals(integrationAuthentication.getAuthType());
    }


}
