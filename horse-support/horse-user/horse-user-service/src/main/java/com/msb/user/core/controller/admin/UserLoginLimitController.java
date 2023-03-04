package com.msb.user.core.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.user.api.vo.UserDO;
import com.msb.user.auth.AuthAdmin;
import com.msb.user.core.model.dto.UserAdminQueryDTO;
import com.msb.user.core.model.dto.UserLoginLimitDTO;
import com.msb.user.core.model.dto.UserLoginLimitQueryDTO;
import com.msb.user.core.model.entity.User;
import com.msb.user.core.model.vo.UserLoginLimitVO;
import com.msb.user.core.model.vo.UserVO;
import com.msb.user.core.service.UserLoginLimitService;
import com.msb.user.core.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Api(tags = "（B端后管访问）系统级用户登录限制设置")
@AuthAdmin
@RestController
@RequestMapping("admin/user/login/limit")
public class UserLoginLimitController {

    @Resource
    private UserService userService;

    @Resource
    private UserLoginLimitService userLoginLimitService;

    @ApiOperation("查询登录限制")
    @GetMapping
    public IPage<UserLoginLimitVO> page(UserLoginLimitQueryDTO userLoginLimitQueryDTO) {
        return userLoginLimitService.page(userLoginLimitQueryDTO);
    }

    @ApiOperation("新增登录限制")
    @PostMapping
    public Boolean save(UserLoginLimitDTO userLoginLimitDTO) {
        return userLoginLimitService.save(userLoginLimitDTO);
    }

    @ApiOperation("修改登录限制")
    @PostMapping("{id}")
    public Boolean update(@PathVariable Long id, UserLoginLimitDTO userLoginLimitDTO) {
        return userLoginLimitService.update(id, userLoginLimitDTO);
    }

    @ApiOperation("删除登录限制")
    @DeleteMapping
    public Boolean delete(Long[] idList) {
        return userLoginLimitService.delete(Arrays.asList(idList));
    }

}


