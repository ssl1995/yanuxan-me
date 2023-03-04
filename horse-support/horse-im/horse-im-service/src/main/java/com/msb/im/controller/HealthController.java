package com.msb.im.controller;

import com.msb.framework.common.exception.BizException;
import com.msb.im.netty.server.IServer;
import com.msb.user.auth.NoAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.msb.framework.common.exception.BaseResultCodeEnum.SYSTEM_ERROR;

/**
 * 容器健康检查接口
 *
 * @author zhou miao
 * @date 2022/05/14
 */
@Api(tags = "健康检查")
@RestController
public class HealthController {

    @Resource
    private IServer iServer;

    @NoAuth
    @GetMapping("/health")
    @ApiOperation("检查检查")
    public String health() {
        if (iServer.isStart()) {
            return "ok";
        }
        throw new BizException(SYSTEM_ERROR);
    }
}
