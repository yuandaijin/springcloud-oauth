package com.jingcheng.auth.server.config.custom;

import com.google.common.collect.Maps;
import com.jingcheng.user.common.UserVo;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.Map;
/**
 * Create by yuandaijin  on 2019-04-18 16:47
 * 自定义token携带内容
 * version 1.0
 */
public class CustomTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        // 设置额外用户信息
        UserVo baseUser = ((BaseUserDetail) authentication.getPrincipal()).getBaseUser();
        baseUser.setPassword(null);
        Map<String, Object> additionalInfo = Maps.newHashMap();
        //自定义token内容，加入用户基本信息
        additionalInfo.put("userInfo",baseUser);
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}