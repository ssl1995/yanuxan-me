package com.msb.user.auth.exception;

import com.msb.framework.common.exception.BaseResultCodeEnum;
import com.msb.framework.common.result.ResultWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author liao
 */
@Slf4j
@ControllerAdvice
@Order(40)
public class AuthExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    @ExceptionHandler(AuthException.class)
    public ResultWrapper<Object> exception(AuthException exception) {
        log.error("系统异常", exception);
        return ResultWrapper.fail(BaseResultCodeEnum.TOKEN_FAIL);
    }
}
