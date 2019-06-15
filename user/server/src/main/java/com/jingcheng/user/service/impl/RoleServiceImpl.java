package com.jingcheng.user.service.impl;

import com.jingcheng.user.entity.Role;
import com.jingcheng.user.mapper.SysRoleMapper;
import com.jingcheng.user.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Mr.kim on 2019/03/11.
 * Time:10:08
 * ProjectName:oauth
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private SysRoleMapper roleMapper;

    @Override
    public List<Role> getRoleByUserId(Long userId) {
        return roleMapper.getRoleByUserId(userId);
    }
}
