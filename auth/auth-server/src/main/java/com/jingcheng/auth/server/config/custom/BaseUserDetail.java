package com.jingcheng.auth.server.config.custom;

import com.jingcheng.user.common.UserVo;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;



/**
 * Create by yuandaijin  on 2019-04-18 16:47
 * token 用户信息设置
 * version 1.0
 */
public class BaseUserDetail implements UserDetails, CredentialsContainer {

    private final UserVo baseUser;
    private final org.springframework.security.core.userdetails.User user;

    public BaseUserDetail(UserVo baseUser, User user) {
        this.baseUser = baseUser;
        this.user = user;
    }


    @Override
    public void eraseCredentials() {
        user.eraseCredentials();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    public UserVo getBaseUser() {
        return baseUser;
    }
}