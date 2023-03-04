package com.msb.user.core.controller;


import com.msb.framework.common.context.UserContext;
import com.msb.user.auth.NoAuth;
import com.msb.user.core.manager.LoginManager;
import com.msb.user.core.model.dto.PasswordLoginTobDTO;
import com.msb.user.core.model.dto.UserInfoUpdateDTO;
import com.msb.user.core.model.dto.VerificationCodeLoginDTO;
import com.msb.user.core.model.vo.EmployeeVO;
import com.msb.user.core.model.vo.LoginSuccessVO;
import com.msb.user.core.model.vo.UserVO;
import com.msb.user.core.service.EmployeeService;
import com.msb.user.core.service.UserService;
import com.msb.user.core.service.convert.UserConvert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;

/**
 * (User)表控制层
 *
 * @author makejava
 * @since 2022-03-23 20:13:02
 */
@Api(tags = "登录相关接口")
@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private LoginManager loginManager;

    @Resource
    private EmployeeService employeeService;

    @Resource
    private UserConvert userConvert;

    @ApiOperation("验证码登录，注册")
    @PostMapping("login")
    public LoginSuccessVO login(@Validated @RequestBody VerificationCodeLoginDTO loginDTO) {
        return loginManager.checkVerificationCodeLogin(loginDTO);
    }

    @ApiOperation("B端（内部员工）账号密码登录")
    @PostMapping("loginByPasswordToB")
    public LoginSuccessVO loginByPasswordToB(@Validated @RequestBody PasswordLoginTobDTO passwordLoginTobDTO) {
        return loginManager.checkPasswordLoginToB(passwordLoginTobDTO);
    }

    @ApiOperation("登录发送验证码")
    @GetMapping("login/verificationCode")
    public Boolean loginSendVerificationCode(@Validated @NotBlank String phone) throws Exception {
        loginManager.loginSendSms(phone);
        return Boolean.TRUE;
    }

    @NoAuth
    @ApiOperation("退出登录")
    @GetMapping("logout")
    public Boolean logout() {
        loginManager.logout();
        return Boolean.TRUE;
    }

    @ApiOperation("获取当前登录用户信息")
    @GetMapping("current")
    public UserVO getCurrentUserInfo() {
        return userConvert.toVo(this.userService.getUser(UserContext.getUserId()));
    }

    @ApiOperation("获取当前登录的员工信息")
    @GetMapping("current/employee")
    public EmployeeVO getCurrentEmployee() {
        return employeeService.getEmployeeVoByUserId(UserContext.getUserId());
    }

    @ApiOperation("修改当前用户信息")
    @PutMapping
    public Boolean update(@RequestBody UserInfoUpdateDTO userDTO) {
        this.userService.updateUserToC(UserContext.getUserId(), userDTO);
        return Boolean.TRUE;
    }
}

