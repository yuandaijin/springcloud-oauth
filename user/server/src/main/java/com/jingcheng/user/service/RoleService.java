package com.jingcheng.user.service;

import com.jingcheng.user.entity.Role;

import java.util.List;

/**
 * Created by Mr.kim on 2019/03/11.
 * Time:10:08
 * ProjectName:oauth
 */
public interface RoleService {
    List<Role> getRoleByUserId(Long userId);
}
