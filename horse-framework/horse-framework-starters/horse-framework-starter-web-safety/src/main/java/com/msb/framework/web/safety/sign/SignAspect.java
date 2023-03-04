package com.msb.framework.web.safety.sign;


import com.msb.framework.web.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 接口验签AOP
 *
 * @author R
 */
@Aspect
@Slf4j
public class SignAspect {

    @Around("@annotation(signAnnotation)")
    public Object around(ProceedingJoinPoint joinPoint, Sign signAnnotation) throws Throwable {
        String signKey = signAnnotation.signKey();
        int timeout = signAnnotation.timeout();
        String timeStampKey = signAnnotation.timeStampKey();
        HttpServletRequest request = RequestUtil.getRequest();
        String sign = request.getHeader(signKey);
        String timeStamp = request.getHeader(timeStampKey);

        if (StringUtils.isEmpty(sign) || StringUtils.isEmpty(timeStamp)) {
            throw new SignException("验签失败，缺少必要header");
        }
        if ((System.currentTimeMillis() - Long.parseLong(timeStamp)) / 1000 > timeout) {
            throw new SignException("请求已过期");
        }
        // 所有参数和时间戳一起逆序相加(使用+号连接)，并做md5
        Object[] requestArgs = joinPoint.getArgs();
        for (Object requestArg : requestArgs) {
            // 入参非简单类型的暂时不支持使用接口签名
            Class<?> argClass = requestArg.getClass();
            if (!ClassUtils.isPrimitiveOrWrapper(argClass) && argClass != String.class) {
                throw new UnsupportedOperationException("暂不支持入参为非简单类型的接口使用@Sign");
            }
        }
        Object[] signArgs = ArrayUtils.add(requestArgs, timeStamp);
        String mergeStr = Stream.of(signArgs).map(String::valueOf).map(StringUtils::reverse).collect(Collectors.joining("+"));
        String signForCompare = DigestUtils.md5DigestAsHex(mergeStr.getBytes(StandardCharsets.UTF_8));
        if (!signForCompare.equals(sign)) {
            // 签名比对失败
            throw new SignException("验签失败，参数:" + mergeStr + ",服务端签名:" + signForCompare);
        }
        return joinPoint.proceed(requestArgs);
    }


}
