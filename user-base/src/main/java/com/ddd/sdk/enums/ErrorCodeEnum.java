package com.ddd.sdk.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.tools.Diagnostic;

@Getter
@AllArgsConstructor
public enum ErrorCodeEnum {
    SUCCESS("0","操作成功"),

    BIZ_ERROR("10001","业务异常"),

    PARA_ERROR("10002","参数错误"),

    PARAM_VALID_CONVERTER_ERROR("10003", "参数转换异常"),

    SYS_EXCEPTION("50000","开小差了，请稍后再试～"),

    BIZ_EXCEPTION("50001","业务异常"),

    NO_PERMISSION("50002", "无访问权限"),

    NOT_LOGIN("50003", "用户未登录");

    private String code;
    private String msg;

 }
