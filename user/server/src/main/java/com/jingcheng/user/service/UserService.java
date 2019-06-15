package com.jingcheng.user.service;

import com.jingcheng.user.entity.User;

/**
 * Created by Mr.kim on 2019/03/11.
 * Time:10:08
 * ProjectName:oauth
 */
public interface UserService {
    User findByUsername(String username);

    User findByUnionId(String unionId);

    User findByPhone(String phone);
}
