package com.jingcheng.user.controller;

import com.jingcheng.user.entity.User;
import com.jingcheng.user.service.UserService;
import com.jinghceng.util.utils.SystemErrorType;
import com.jinghceng.util.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Mr.kim on 2019/03/11.
 * Time:10:08
 * ProjectName:oauth
 */
@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("findByUsername/{username}")
    public Result findByUsername(@PathVariable("username") String username) {
        User user = userService.findByUsername(username);
        if (user == null) {
            return Result.fail(SystemErrorType.USER_NOT_EXIST);
        }
        return Result.success(user);
    }


    @GetMapping("findByUnionId")
    public Result findByUnionId(String unionId) {
        User user = userService.findByUnionId(unionId);
        if (user == null) {
            return Result.fail(SystemErrorType.UNIONID_NOT_EXIST);
        }
        return Result.success(user);
    }


    @GetMapping("findByPhone")
    public Result findByPhone(String phone) {
        User user = userService.findByPhone(phone);
        if (user == null) {
            return Result.fail(SystemErrorType.PHONE_NOT_EXIST);
        }
        return Result.success(user);
    }


}
