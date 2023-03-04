package com.msb.user.core.controller.admin;


import com.msb.user.auth.AuthAdmin;
import com.msb.user.core.model.vo.SystemVO;
import com.msb.user.core.service.SystemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "（后管）系统相关接口")
@AuthAdmin
@RestController
@RequestMapping("system")
public class SystemController {


    @Resource
    private SystemService systemService;

    @ApiOperation("系统列表")
    @GetMapping
    public List<SystemVO> listSystem() {
        return systemService.listSystem();
    }
}
