package com.msb.im.controller.api;

import com.msb.im.api.dto.UpdateSessionUserDTO;
import com.msb.im.context.ApiContext;
import com.msb.im.service.SessionUserService;
import com.msb.user.auth.NoAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zhou miao
 * @date 2022/05/27
 */
@Api(tags = "(api) 用户数据接口")
@RestController
@RequestMapping("/api/user")
@Slf4j
@NoAuth
public class ApiUserController {
    @Resource
    private SessionUserService sessionUserService;

    @NoAuth
    @ApiOperation("修改会话中用户数据")
    @PutMapping
    public void updateUserData(@RequestBody UpdateSessionUserDTO updateSessionUserDTO) {
        Integer sysId = ApiContext.getSystemId();
        updateSessionUserDTO.setSystemId(sysId);
        sessionUserService.updateUserData(updateSessionUserDTO);
    }

}
