package com.msb.framework.web.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author liao
 */
@UtilityClass
public class RequestUtil {

    public HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return Objects.requireNonNull(requestAttributes).getRequest();
    }


    public String getRequestUri() {
        return getRequest().getRequestURI();
    }

    public String getHeader(String header) {
        return getRequest().getHeader(header);
    }

    /**
     * 请求信息，含uri和请求参数
     * 示例：GET:/api/getUser?id=1
     */
    public String getRequestInfo() {
        HttpServletRequest request = getRequest();
        String queryString = request.getQueryString();
        queryString = (StringUtils.isEmpty(queryString) || "null".equals(queryString)) ? "" : "?" + queryString;
        return request.getMethod() + ":" + request.getRequestURI() + queryString;
    }
}
