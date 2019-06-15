package com.jingcheng.auth.server.utils;

import com.jinghceng.util.utils.SystemErrorType;
import com.jinghceng.util.vo.Result;

/**
 * Create by yuandaijin  on 2019-03-25 17:06
 * version 1.0
 */
public class VerificationCodeUtil {


    //TODO 这里模拟验证码验证是否正确
    public static Result<Boolean> validate(String code, String phone) {
        if (code.equals("123456")) { //测试验证码为110
            return Result.success();
        } else {
            return Result.fail(SystemErrorType.VC_CODE_ERROR);
        }
    }


}
