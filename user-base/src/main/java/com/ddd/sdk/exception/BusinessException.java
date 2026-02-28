package com.ddd.sdk.exception;

import com.ddd.sdk.enums.ErrorCodeEnum;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 2943470682335539492L;
    private String code;
    private String message;
    private Object data;

    public BusinessException(String code,
                      String message,
                      Throwable cause) {
        this(code, message, cause, null);
    }

    public BusinessException(String code, String message, Throwable cause, Object responseDTO) {
        super( message, cause);
        this.code = code;
        this.message = message;
        this.data = responseDTO;
    }

    public BusinessException(String code, String message) {
        super( message, null);
        this.code = code;
        this.message = message;
    }

    public BusinessException(String message) {
        super( message, null);
        this.code = ErrorCodeEnum.BIZ_ERROR.getCode();
        this.message = message;
    }

}
