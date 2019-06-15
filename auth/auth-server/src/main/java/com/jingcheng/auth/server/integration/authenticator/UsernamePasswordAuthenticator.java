package com.jingcheng.auth.server.integration.authenticator;

import com.jingcheng.auth.server.integration.AbstractPreparableIntegrationAuthenticator;
import com.jingcheng.auth.server.integration.IntegrationAuthentication;
import com.jingcheng.user.client.UserClient;
import com.jingcheng.user.common.UserVo;
import com.jinghceng.util.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Component;

/**
 * 默认登录处理
 *
 * @author yuandiajin
 * @date 2018-3-31
 **/
@Component
@Primary
public class UsernamePasswordAuthenticator extends AbstractPreparableIntegrationAuthenticator {

    @Autowired
    private UserClient userClient;

    @Override
    public UserVo authenticate(IntegrationAuthentication integrationAuthentication) {
        String pwd = integrationAuthentication.getAuthParameter("password");
        Result<UserVo> result = userClient.findByUsername(integrationAuthentication.getUsername());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (result.isSuccess()) {
            if(integrationAuthentication.getAuthParameter("grant_type").equals("refresh_token")){
                return result.getData();//刷新token不做验证密码操作
            }else{
                if(encoder != null && encoder.matches(pwd,result.getData().getPassword())){
                    return result.getData();//这里密码单独做验证
                }
                throw new OAuth2Exception("密码错误");
            }
        }
        return null;
    }

    @Override
    public void prepare(IntegrationAuthentication integrationAuthentication) {
    }

    @Override
    public boolean support(IntegrationAuthentication integrationAuthentication) {
        return StringUtils.isEmpty(integrationAuthentication.getAuthType());
    }
}
