package com.msb.mall.base.controller;

import com.msb.mall.base.model.vo.AppVersionVO;
import com.msb.user.auth.NoAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "app版本相关接口")
@NoAuth
@RequestMapping("app/")
@RestController
public class AppVersionController {

    @Value("${app.version}")
    private String version;

    @Value("${app.download}")
    private String download;

    @ApiOperation("app最新版本")
    @GetMapping("version")
    public AppVersionVO getVersion() {
        AppVersionVO appVersionVO = new AppVersionVO();
        appVersionVO.setVersion(version);
        appVersionVO.setDownload(download);
        return appVersionVO;
    }
}
