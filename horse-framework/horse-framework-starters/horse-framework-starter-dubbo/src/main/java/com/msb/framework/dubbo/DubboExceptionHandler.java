package com.msb.framework.dubbo;

import com.msb.framework.common.exception.BaseResultCodeEnum;
import com.msb.framework.common.result.ResultWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.RpcException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Dubbo异常处理
 *
 * @author liao
 */
@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DubboExceptionHandler {

    /**
     * 处理dubbo 调用异常
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(RpcException.class)
    public ResultWrapper<Void> rpcException(RpcException rpcException) {
        log.error("dubbo调用异常", rpcException);
        return ResultWrapper.fail(BaseResultCodeEnum.SYSTEM_ERROR, "dubbo调用异常:" + rpcException.getMessage());
    }
}
