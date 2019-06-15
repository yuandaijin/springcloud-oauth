package com.jingcheng.user.service.impl;

import com.jingcheng.user.entity.User;
import com.jingcheng.user.mapper.SysUserMapper;
import com.jingcheng.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

/**
 * Created by Mr.kim on 2019/03/11.
 * Time:10:08
 * ProjectName:oauth
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper userMapper;

    @Override
    public User findByUsername(String username) {
        Example example = Example.builder(User.class)
                .where(Sqls.custom().andEqualTo("username", username))
                .build();
        return userMapper.selectOneByExample(example);
    }


    @Override
    public User findByUnionId(String unionId) {
        Example example = Example.builder(User.class)
                .where(Sqls.custom().andEqualTo("union_id", unionId))
                .build();
        return userMapper.selectOneByExample(example);
    }

    @Override
    public User findByPhone(String phone) {
        Example example = Example.builder(User.class)
                .where(Sqls.custom().andEqualTo("mobile", phone))
                .build();
        return userMapper.selectOneByExample(example);
    }
}
