package com.msb.framework.web.safety.sign;

import com.msb.framework.common.exception.BaseResultCodeEnum;
import com.msb.framework.common.result.ResultWrapper;
import com.msb.framework.web.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 接口验签异常处理
 *
 * @author R
 */
@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SignExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(SignException.class)
    public ResultWrapper<Void> handleException(SignException signException) {
        log.error("接口验签异常，可能遭遇攻击，uri：{}，异常信息：{}", RequestUtil.getRequestInfo(), signException.getMessage());
        return ResultWrapper.fail(BaseResultCodeEnum.ILLEGAL_REQUEST);
    }
}
