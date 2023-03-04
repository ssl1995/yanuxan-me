package com.msb.framework.web.safety.sign;

/**
 * 签名异常
 *
 * @author R
 */
public class SignException extends RuntimeException {

    public SignException(String message) {
        super(message);
    }

    public SignException() {
        super("验签失败");
    }
}
