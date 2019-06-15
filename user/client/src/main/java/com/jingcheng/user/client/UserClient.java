package com.jingcheng.user.client;

import com.jingcheng.user.common.RoleVo;
import com.jingcheng.user.common.UserVo;
import com.jinghceng.util.vo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Create by yuandaijin  on 2019-04-10 15:53
 * version 1.0
 */
@FeignClient(name = "user")
public interface UserClient {

    @GetMapping("user/findByUsername/{username}")
    Result<UserVo> findByUsername(@PathVariable("username") String username);

    @GetMapping("role/getRoleByUserId/{userId}")
    Result<List<RoleVo>> getRoleByUserId(@PathVariable("userId") Long userId);

    @GetMapping("user/findByUnionId")
    Result<UserVo> findByUnionId(@RequestParam("unionId") String unionId);

    @GetMapping("user/findByPhone")
    Result<UserVo> findByPhone(@RequestParam("phone") String phone);

}
