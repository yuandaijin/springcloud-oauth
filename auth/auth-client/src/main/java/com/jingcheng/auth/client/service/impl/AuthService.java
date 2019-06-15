package com.jingcheng.auth.client.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jingcheng.auth.client.config.IgnoreConfig;
import com.jingcheng.auth.client.config.JwtKeyConfig;
import com.jingcheng.auth.client.provider.AuthProvider;
import com.jingcheng.auth.client.service.IAuthService;
import com.jinghceng.util.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.InvalidSignatureException;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Create by yuandaijin  on 2019-04-17 16:45
 * version 1.0
 */
@Service
@Slf4j
@RefreshScope
public class AuthService implements IAuthService {

    @Autowired
    private AuthProvider authProvider;

    /**
     * Authorization认证开头是"bearer "
     */
    private static final int BEARER_BEGIN_INDEX = 7;

    /**
     * jwt token 密钥，主要用于token解析，签名验证
     */
    @Autowired
    private JwtKeyConfig jwtKeyConfig;
    /**
     * 不需要网关签权的url配置(/oauth,/open)
     * 默认/oauth开头是不需要的
     */
    @Autowired
    private IgnoreConfig ignoreConfig;

    /**
     * jwt验签
     */
    private MacSigner verifier;

    @Override
    public Result authenticate(String authentication, String url, String method) {
        return authProvider.auth(authentication, url, method);
    }

    @Override
    public boolean ignoreAuthentication(String url) {
        return Stream.of(ignoreConfig.getStartWith().split(",")).anyMatch(ignoreUrl -> url.startsWith(StringUtils.trim(ignoreUrl)));
    }

    @Override
    public boolean hasPermission(Result authResult) {
        return authResult.isSuccess() && (boolean) authResult.getData();
    }

    @Override
    public boolean hasPermission(String authentication, String url, String method) {
        //从认证服务获取是否有权限
        return hasPermission(authenticate(authentication, url, method));
    }


    @Override
    public boolean tokenIsExpired(String authentication){
        //token是否有效
        if (invalidJwtAccessToken(authentication)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public boolean invalidJwtAccessToken(String authentication) {
        verifier = Optional.ofNullable(verifier).orElse(new MacSigner(jwtKeyConfig.getSigningKey()));
        //是否有效
        boolean invalid = Boolean.FALSE;

        try {
            Jwt jwt = getJwt(authentication);
            jwt.verifySignature(verifier);
            Map map = JSONObject.parseObject(jwt.getClaims(), HashMap.class);
            int exp = (int)map.get("exp");
            if(exp < ((int) (System.currentTimeMillis() / 1000)) ){
                return Boolean.FALSE;
            }
            invalid = Boolean.TRUE;
        } catch (InvalidSignatureException | IllegalArgumentException ex) {
            log.warn("user token has expired or signature error ");
        }
        return invalid;
    }

    @Override
    public Jwt getJwt(String authentication) {
        return JwtHelper.decode(StringUtils.substring(authentication, BEARER_BEGIN_INDEX));
    }

//    /**
//     * jwt过期时间获取
//     * @param args
//     */
//    public static void main(String[] args) {
//        Jwt jwt =JwtHelper.decode("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySW5mbyI6eyJuYW1lIjoi6KKB5Luj6YeRIiwibW9iaWxlIjoiMTUyODE4NzY4MTYiLCJ1c2VybmFtZSI6ImtpbSIsInBhc3N3b3JkIjpudWxsLCJlbmFibGVkIjp0cnVlLCJhY2NvdW50Tm9uRXhwaXJlZCI6dHJ1ZSwiY3JlZGVudGlhbHNOb25FeHBpcmVkIjp0cnVlLCJhY2NvdW50Tm9uTG9ja2VkIjp0cnVlLCJpZCI6MTAyLCJjcmVhdGVkQnkiOiJzeXN0ZW0iLCJ1cGRhdGVkQnkiOiJzeXN0ZW0iLCJjcmVhdGVkVGltZSI6MTU1NTY1NTM4MTAwMCwidXBkYXRlZFRpbWUiOjE1NTU2NTUzODEwMDB9LCJ1c2VyX25hbWUiOiJraW0iLCJzY29wZSI6WyJyZWFkIl0sImV4cCI6MTU1NTg2MTM4MCwiYXV0aG9yaXRpZXMiOlsiQURNSU4iLCJJVCJdLCJqdGkiOiI3YjIyYzE3Ny0wNGJmLTQ1ZmQtODcwOC1kMWE2YmM3MTBhNTUiLCJjbGllbnRfaWQiOiJ3ZWJBcHAifQ.T_PTk9Yxk7_TThkYqF2iYD4LiPgOZzn34COjnT7wOEk");
//        Map map = JSONObject.parseObject(jwt.getClaims(), HashMap.class);
//        int exp = (int)map.get("exp");
//        System.out.println(exp);
//        if(exp < ((int) (System.currentTimeMillis() / 1000)) ){
//
//        }
//
//    }

}
