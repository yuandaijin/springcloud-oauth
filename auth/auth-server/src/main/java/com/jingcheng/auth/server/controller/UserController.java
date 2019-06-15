package com.jingcheng.auth.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


/**
 *
 * Created by Mr.kim on 2019/04/12.
 * Time:10:08
 * ProjectName:oauth
 */
@RestController
@RequestMapping("/oauth")
public class UserController {
    @GetMapping("/user")
    public Principal user(Principal user) {
        return user;
    }
}
