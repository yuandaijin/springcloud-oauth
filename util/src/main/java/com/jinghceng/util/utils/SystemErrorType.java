package com.jinghceng.util.utils;

import lombok.Getter;

@Getter
public enum SystemErrorType implements ErrorType {

    SYSTEM_ERROR("-1", "系统异常"),
    SYSTEM_BUSY("000001", "系统繁忙,请稍候再试"),

    GATEWAY_NOT_FOUND_SERVICE("010404", "服务未找到"),
    GATEWAY_ERROR("010500", "网关异常"),
    GATEWAY_CONNECT_TIME_OUT("010002", "网关超时"),
    UPLOAD_FILE_SIZE_LIMIT("020001", "上传文件大小超过限制"),
    ARGUMENT_NOT_VALID("020000", "请求参数校验不通过"),

    UNAUTHORIZED("401", "Unauthorized"),
    USER_NOT_EXIST("100", "用户不存在"),
    UNIONID_NOT_EXIST("100", "unionId不存在"),
    PHONE_NOT_EXIST("100", "手机号不存在"),
    LOGIN_OUT_SUCCESS("200", "注销成功"),
    LOGIN_OUT_ERROR("100", "注销失败"),
    VC_CODE_ERROR("100", "验证码错误"),
    TOKEN_NOT_EXIST("401", "Authorization不存在"),
    CLIENT_NOT_EXIST("401", "客户端信息不存在"),
    TOKEN_IS_EXP("999", "token失效或过期");

    /**
     * 错误类型码
     */
    private String code;
    /**
     * 错误类型描述信息
     */
    private String mesg;

    SystemErrorType(String code, String mesg) {
        this.code = code;
        this.mesg = mesg;
    }
}
