package com.jingcheng.user.controller;

import com.jingcheng.user.entity.Role;
import com.jingcheng.user.service.RoleService;
import com.jinghceng.util.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Mr.kim on 2019/03/11.
 * Time:10:08
 * ProjectName:oauth
 */
@RestController
@RequestMapping("role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("getRoleByUserId/{userId}")
    public Result getRoleByUserId(@PathVariable("userId") Long userId) {
        List<Role> roleList = roleService.getRoleByUserId(userId);
        return Result.success(roleList);
    }

}
