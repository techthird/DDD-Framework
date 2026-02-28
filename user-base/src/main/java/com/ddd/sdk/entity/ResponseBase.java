package com.ddd.sdk.entity;

import com.ddd.sdk.enums.ErrorCodeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.MDC;

/**
 * Response基类
 */
@Getter
@Setter
@ToString(callSuper = true)
public class ResponseBase<T> {
    @ApiModelProperty(value = "请求返回码，0-成功，其他失败")
    private String code;

    @ApiModelProperty(value = "请求返回信息")
    private String msg;

    @ApiModelProperty(value = "链路id")
    private String traceId;

    @ApiModelProperty(value = "请求返回实体对象")
    private T data;

    private Long timestamp = System.currentTimeMillis();

    public boolean checkSuccess() {
        return ErrorCodeEnum.SUCCESS.getCode().equals(code);
    }

    public static <T> ResponseBase<T> success() {
        ResponseBase<T> base = new ResponseBase<>();
        base.setCode(ErrorCodeEnum.SUCCESS.getCode());
        base.setMsg(ErrorCodeEnum.SUCCESS.getMsg());
        base.setTraceId(MDC.get("traceId"));
        return base;
    }

    public static <T> ResponseBase<T> success(T data) {
        ResponseBase<T> base = new ResponseBase<>();
        base.setCode(ErrorCodeEnum.SUCCESS.getCode());
        base.setMsg(ErrorCodeEnum.SUCCESS.getMsg());
        base.setData(data);
        base.setTraceId(MDC.get("traceId"));
        return base;
    }

    public static <T> ResponseBase<T> fail(ErrorCodeEnum type) {
        ResponseBase<T> base = new ResponseBase<>();
        base.setCode(type.getCode());
        base.setMsg(type.getMsg());
        base.setTraceId(MDC.get("traceId"));
        return base;
    }

    public static <T> ResponseBase<T> fail(String code, String msg) {
        ResponseBase<T> base = new ResponseBase<>();
        base.setCode(code);
        base.setMsg(msg);
        base.setTraceId(MDC.get("traceId"));
        return base;
    }

    public static <T> ResponseBase<T> fail(ErrorCodeEnum ErrorCodeEnum, String msg) {
        ResponseBase<T> base = new ResponseBase<>();
        base.setCode(ErrorCodeEnum.getCode());
        base.setMsg(msg);
        base.setTraceId(MDC.get("traceId"));
        return base;
    }

}
