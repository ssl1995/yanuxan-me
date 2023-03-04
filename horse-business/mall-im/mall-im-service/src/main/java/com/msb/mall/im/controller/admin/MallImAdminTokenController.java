package com.msb.mall.im.controller.admin;

import com.msb.mall.im.conifg.MallImConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 获取登录凭证的
 *
 * @author zhou miao
 * @date 2022/05/25
 */
@RestController
@Api(tags = "（后管）token相关的接口")
@RequestMapping("/admin/token")
public class MallImAdminTokenController {
   @Resource
   private MallImConfig mallImConfig;

    @ApiOperation("续期token")
    @GetMapping("/renewal")
    public void renewal() {

    }

}
