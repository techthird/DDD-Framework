package com.ddd.common.configuration;


import com.ddd.sdk.entity.ResponseBase;
import com.ddd.sdk.enums.ErrorCodeEnum;
import com.ddd.sdk.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 全局异常处理
 */
@Slf4j
@Configuration
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 一般的参数绑定时候抛出的异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public ResponseBase handleBindException(BindException ex) {
        log.info("参数校验异常", ex);
        List<String> defaultMsg = ex.getBindingResult().getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());
        return ResponseBase.fail(ErrorCodeEnum.PARA_ERROR.getCode(),
                ErrorCodeEnum.PARA_ERROR.getMsg());
    }

    /**
     * 参数转换异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseBase handleConverterException(HttpMessageNotReadableException ex) {
        log.info("前端-后端转换异常", ex);
        return ResponseBase.fail(ErrorCodeEnum.PARAM_VALID_CONVERTER_ERROR.getCode(),
                ErrorCodeEnum.PARAM_VALID_CONVERTER_ERROR.getMsg());
    }

    /**
     * 单个参数校验
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    public ResponseBase handleBindGetException(ConstraintViolationException ex) {
        log.info("单个参数校验异常", ex);
        List<String> defaultMsg = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        return ResponseBase.fail(ErrorCodeEnum.PARA_ERROR.getCode(),
                ErrorCodeEnum.PARA_ERROR.getMsg());
    }

    /**
     * param参数校验异常
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseBase handleException(MissingServletRequestParameterException ex) {
        log.info("方法参数异常", ex);
        String errorMsg = String.format("缺少参数，字段名:%s", ex.getParameterName());
        return ResponseBase.fail(ErrorCodeEnum.PARA_ERROR.getCode(), errorMsg);
    }

    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseBase handleException(MethodArgumentNotValidException ex) {
        log.info("方法参数异常", ex);
        StringBuilder errors = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.append(fieldName + ":" + errorMessage+" ");
        });
        return ResponseBase.fail(ErrorCodeEnum.PARA_ERROR.getCode(),
                "参数错误：" + errors.toString());
    }

    /**
     * 业务异常
     *
     * @param e the e
     * @return http response
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ResponseBase<?> businessException(BusinessException e) {
        log.info("业务异常={}", e.getMessage(), e);
        return ResponseBase.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseBase<?> exception(Exception e) {
        log.error("系统异常 exception={}", e.getMessage(), e);
        return ResponseBase.fail(ErrorCodeEnum.SYS_EXCEPTION.getCode(),
                ErrorCodeEnum.SYS_EXCEPTION.getMsg());
    }

}
