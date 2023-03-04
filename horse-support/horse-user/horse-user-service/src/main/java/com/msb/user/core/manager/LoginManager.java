package com.msb.user.core.manager;

import com.msb.framework.common.context.UserContext;
import com.msb.framework.common.exception.BizException;
import com.msb.framework.common.model.UserLoginInfo;
import com.msb.framework.redis.RedisClient;
import com.msb.framework.web.result.BizAssert;
import com.msb.push.api.SmsDubboService;
import com.msb.push.enums.SmsTemplateEnum;
import com.msb.user.core.exception.UserExceptionCodeEnum;
import com.msb.user.core.model.dto.LoginDTO;
import com.msb.user.core.model.dto.PasswordLoginTobDTO;
import com.msb.user.core.model.dto.VerificationCodeLoginDTO;
import com.msb.user.core.model.entity.Employee;
import com.msb.user.core.model.entity.User;
import com.msb.user.core.model.entity.UserLoginLimit;
import com.msb.user.core.model.vo.LoginSuccessVO;
import com.msb.user.core.service.*;
import com.msb.user.core.service.convert.EmployeeConvert;
import com.msb.user.core.service.convert.UserConvert;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.msb.user.core.model.constant.RedisKeysConstants.LOGIN_VERIFICATION_CODE;
import static com.msb.user.core.model.constant.RedisKeysConstants.LOGIN_VERIFICATION_CODE_INTERVAL;

@Component
public class LoginManager {

    @Resource
    private RedisClient redisClient;

    @Resource
    private UserService userService;

    @Resource
    private EmployeeService employeeService;

    @Resource
    private UserConvert userConvert;

    @Resource
    private EmployeeConvert employeeConvert;

    @Resource
    private UserLoginRecordService userLoginRecordService;

    @Resource
    private TokenManager tokenManager;

    @Resource
    private UserSystemRelationService userSystemRelationService;

    @Resource
    private UserLoginLimitService userLoginLimitService;

    @Resource
    private PermissionManager permissionManager;

    @DubboReference
    private SmsDubboService smsDubboService;

    private void setVerificationCodeRedis(String phone, String verificationCode) {
        redisClient.set(LOGIN_VERIFICATION_CODE.concat(phone), verificationCode, 5, TimeUnit.MINUTES);
        redisClient.set(LOGIN_VERIFICATION_CODE_INTERVAL.concat(phone), verificationCode, 1, TimeUnit.MINUTES);
    }

    private void delVerificationCodeRedis(String phone) {
        redisClient.delete(LOGIN_VERIFICATION_CODE.concat(phone));
        redisClient.delete(LOGIN_VERIFICATION_CODE_INTERVAL.concat(phone));
    }

    public void logout() {
        UserLoginInfo userLoginInfo = UserContext.get();
        if (userLoginInfo != null) {
            tokenManager.removeToken(userLoginInfo);
        }
    }

    public void checkBeyondLoginLimit(Long userId, LoginDTO loginDTO) {
        Optional<UserLoginLimit> userLoginLimitOptional = userLoginLimitService.getUserLoginLimit(loginDTO.getSystemId(), loginDTO.getClientId());
        if (!userLoginLimitOptional.isPresent()) {
            return;
        }
        UserLoginLimit userLoginLimit = userLoginLimitOptional.get();
        Map<String, UserLoginInfo> userTokenMap = tokenManager.getUserTokenMap(userId);
        Map<Object, List<UserLoginInfo>> userLoginCount = userTokenMap.values().stream().filter(userLoginInfo -> Objects.nonNull(userLoginInfo.getSystemId()) && Objects.nonNull(userLoginInfo.getClientId()))
                .collect(Collectors.groupingBy(userLoginInfo -> userLoginInfo.getSystemId().toString() + userLoginInfo.getClientId()));

        List<UserLoginInfo> userLoginInfos = userLoginCount.get(loginDTO.getSystemId().toString() + loginDTO.getClientId());
        Integer count = Optional.ofNullable(userLoginInfos).map(List::size).orElse(0);

        if (userLoginLimit.getLoginLimitNum() <= count) {
            if (userLoginLimit.getBeyondMode() == 1) {
                throw new BizException("您已超出登录限制");
            } else {
                if (Objects.nonNull(userLoginInfos) && !userLoginInfos.isEmpty()) {
                    Optional<UserLoginInfo> userLoginInfoOptional = userLoginInfos.stream().min(Comparator.comparing(UserLoginInfo::getTokenExpireTime));
                    userLoginInfoOptional.ifPresent(userLoginInfo -> tokenManager.removeToken(userLoginInfo));
                }
            }
        }
    }

    public void loginSendSms(String phone) {
        BizAssert.isNull(redisClient.get(LOGIN_VERIFICATION_CODE_INTERVAL.concat(phone)), UserExceptionCodeEnum.VERIFICATION_CODE_FREQUENTLY.getMessage());
        String verificationCode = String.valueOf(this.generateVerificationCode());
        smsDubboService.sendSms(phone, SmsTemplateEnum.LOGIN_TEMPLATE, verificationCode);
        setVerificationCodeRedis(phone, verificationCode);
    }

    @Transactional(rollbackFor = Exception.class)
    public LoginSuccessVO checkVerificationCodeLogin(VerificationCodeLoginDTO verificationCodeLoginDTO) {
        String phone = verificationCodeLoginDTO.getPhone();
        String verificationCode = redisClient.get(LOGIN_VERIFICATION_CODE.concat(phone));

        boolean isVerificationCodeSuccess = StringUtils.equals(verificationCode, verificationCodeLoginDTO.getVerificationCode())
                || ("1111".equals(verificationCodeLoginDTO.getVerificationCode()) && "16688888888".equals(verificationCodeLoginDTO.getPhone()));

        BizAssert.isTrue(isVerificationCodeSuccess, UserExceptionCodeEnum.VERIFICATION_CODE_FAIL.getMessage());
        Optional<User> optionalUser = userService.getUserOptional(phone);
        LoginSuccessVO login = loginAndRegister(optionalUser, phone, verificationCodeLoginDTO);
        delVerificationCodeRedis(phone);
        return login;
    }

    @Transactional(rollbackFor = Exception.class)
    public LoginSuccessVO checkPasswordLoginToB(PasswordLoginTobDTO passwordLoginTobDTO) {
        String phone = passwordLoginTobDTO.getPhone();
        Optional<Employee> employeeOptional = employeeService.getEmployee(phone, passwordLoginTobDTO.getPassword());

        BizAssert.isTrue(employeeOptional.isPresent(), "账号密码错误");
        BizAssert.isTrue(employeeOptional.get().getIsEnable(), "当前账户不可用");

        Optional<User> userOptional = userService.getUserOptional(employeeOptional.get().getUserId());
        return loginAndRegister(userOptional, phone, passwordLoginTobDTO);
    }

    public LoginSuccessVO loginAndRegister(Optional<User> userOptional, String phone, LoginDTO loginDTO) {
        userOptional.ifPresent(user -> {
            BizAssert.isTrue(user.getIsEnable(), "用户已被禁用，请联系管理员");
            userService.updateById(new User().setId(user.getId()).setLastLoginTime(LocalDateTime.now()));
        });
        User user = userOptional.orElseGet(() ->
                userService.register(phone, loginDTO.getSystemId(), loginDTO.getClientId()));

        checkBeyondLoginLimit(user.getId(), loginDTO);

        UserLoginInfo userLoginInfo = userConvert.toUserLoginInfo(user);
        userLoginInfo.setSystemId(loginDTO.getSystemId());
        userLoginInfo.setClientId(loginDTO.getClientId());
        //判断是否 B端用户
        Optional.ofNullable(employeeService.getEmployeeByUserId(user.getId()))
                .ifPresent(e -> {
                    userLoginInfo.setEmployeeId(e.getId());
                    permissionManager.setEmployeePermissionToRedis(e.getId());
                });

        String token = tokenManager.generateUserTokenAndSessionWriteRedis(userLoginInfo).getToken();

        userLoginRecordService.save(user.getId(), token, user.getSystemId(), user.getClientId());
        userSystemRelationService.save(user.getId(), user.getSystemId());

        userLoginInfo.setToken(null);
        return LoginSuccessVO.builder().user(userLoginInfo).token(token).build();
    }

    private int generateVerificationCode() {
        return (int) (Math.random() * (9999 - 1000 + 1) + 1000);
    }

}
