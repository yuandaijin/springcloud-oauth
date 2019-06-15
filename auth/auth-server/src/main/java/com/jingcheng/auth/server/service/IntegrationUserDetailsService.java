package com.jingcheng.auth.server.service;

import com.alibaba.fastjson.JSONObject;
import com.jingcheng.auth.server.config.custom.BaseUserDetail;
import com.jingcheng.auth.server.integration.IntegrationAuthentication;
import com.jingcheng.auth.server.integration.IntegrationAuthenticationContext;
import com.jingcheng.auth.server.integration.IntegrationAuthenticator;
import com.jingcheng.user.client.UserClient;
import com.jingcheng.user.common.RoleVo;
import com.jingcheng.user.common.UserVo;
import com.jinghceng.util.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 集成认证用户服务
 *
 * @author yuandiajin
 * @date 2018-3-7
 **/
@Service
public class IntegrationUserDetailsService implements UserDetailsService {

    @Autowired
    private UserClient userClient;

    private List<IntegrationAuthenticator> authenticators;

    @Autowired(required = false)
    public void setIntegrationAuthenticators(List<IntegrationAuthenticator> authenticators) {
        this.authenticators = authenticators;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        IntegrationAuthentication integrationAuthentication = IntegrationAuthenticationContext.get();
        //判断是否是集成登录
        if (integrationAuthentication == null) {
            integrationAuthentication = new IntegrationAuthentication();
        }

        //解决refresh_token获取不到username问题
        if(integrationAuthentication.getAuthParameter("grant_type").equals("refresh_token")){
            String refreshToken = integrationAuthentication.getAuthParameter("refresh_token");
            Jwt jwt = JwtHelper.decode(refreshToken);
            Map map = JSONObject.parseObject(jwt.getClaims(), HashMap.class);
            Map user = JSONObject.parseObject(JSONObject.toJSONString(map.get("userInfo")), HashMap.class);
            username = (String)user.get("username");
        }
        integrationAuthentication.setUsername(username);

        UserVo userVo = this.authenticate(integrationAuthentication);//这里根据不同的登录类型来处理不同的用户获取逻辑
        if (userVo == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        org.springframework.security.core.userdetails.User   user = new org.springframework.security.core.userdetails.User(
                username,
                userVo.getPassword(),
                userVo.getEnabled(),
                userVo.getAccountNonExpired(),
                userVo.getCredentialsNonExpired(),
                userVo.getAccountNonLocked(),
                this.obtainGrantedAuthorities(userVo));

        return new BaseUserDetail(userVo, user);
    }




    /**
     * 获得登录者所有角色的权限集合.
     *
     * @param user
     * @return
     */
    private Set<GrantedAuthority> obtainGrantedAuthorities(UserVo user) {
        Result<List<RoleVo>> roles = userClient.getRoleByUserId(user.getId());
        return  roles.getData().stream()
                .map(role -> new SimpleGrantedAuthority(role.getCode()))
                .collect(Collectors.toSet());
    }


    private UserVo authenticate(IntegrationAuthentication integrationAuthentication) {
        if (this.authenticators != null) {
            for (IntegrationAuthenticator authenticator : authenticators) {
                if (authenticator.support(integrationAuthentication)) {
                    return authenticator.authenticate(integrationAuthentication);
                }
            }
        }
        return null;
    }
}
