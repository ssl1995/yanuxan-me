package com.msb.user.auth;


import com.msb.framework.common.context.UserContext;
import com.msb.framework.common.exception.BaseResultCodeEnum;
import com.msb.framework.common.exception.BizException;
import com.msb.framework.common.model.UserLoginInfo;
import com.msb.framework.web.result.Assert;
import com.msb.user.api.EmployeeDubboService;
import com.msb.user.api.UserDubboService;
import com.msb.user.api.vo.UserDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Optional;

/**
 * @author liao
 */
@Slf4j
@Component
public class DevAuthHandlerInterceptor implements HandlerInterceptor {

    @Resource
    private JwtProperties jwtProperties;

    @DubboReference
    private EmployeeDubboService employeeDubboService;

    @DubboReference
    private UserDubboService userDubboService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HandlerMethod handlerMethod;
        AuthAdmin authAdmin = null;
        if (handler instanceof HandlerMethod) {
            handlerMethod = (HandlerMethod) handler;
            if (handlerMethod.getMethod().isAnnotationPresent(NoAuth.class)
                    || handlerMethod.getBeanType().isAnnotationPresent(NoAuth.class)) {
                return true;
            }
            authAdmin = Optional.ofNullable(handlerMethod.getBeanType().getAnnotation(AuthAdmin.class))
                    .orElse(handlerMethod.getMethod().getAnnotation(AuthAdmin.class));
        }
        String jwtToken = request.getHeader(JwtConstants.JWT_HEADER_KEY);

        if (Objects.isNull(jwtToken)) {
            UserDO user = userDubboService.getUserByPhone("18874639213");
            UserLoginInfo userLoginInfo = new UserLoginInfo();
            BeanUtils.copyProperties(user, userLoginInfo);
            UserContext.set(userLoginInfo);
            return true;
        }

        UserLoginInfo userLoginInfo = userDubboService.checkUserLoginInfo(jwtToken);
        Optional.ofNullable(userLoginInfo).orElseThrow(() -> new BizException(BaseResultCodeEnum.TOKEN_FAIL));

        setUserContext(userLoginInfo);
//        checkEmployee(authAdmin, userLoginInfo);
        return false;
    }

    private void checkEmployee(AuthAdmin authAdmin, UserLoginInfo userLoginInfo) {
        Optional.ofNullable(authAdmin).ifPresent(authAdmin1 -> Assert.notNull(userLoginInfo.getEmployeeId(), BaseResultCodeEnum.NO_OPERATE_PERMISSION));
    }

    private UserLoginInfo setUserContext(UserLoginInfo userLoginInfo) {
        UserContext.set(userLoginInfo);
        return userLoginInfo;
    }
}
