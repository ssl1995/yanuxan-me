package com.msb.third.controller;

import com.msb.framework.common.context.UserContext;
import com.msb.framework.common.model.IDict;
import com.msb.third.enums.PlatformEnum;
import com.msb.third.model.dto.SaveThirdInfoDTO;
import com.msb.third.service.ThirdInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@Api(tags = "第三方授权")
@RestController
@RequestMapping("/third")
public class ThirdInfoController {

    @Resource
    private ThirdInfoService thirdInfoService;

    @ApiOperation(value = "保存第三方授权信息")
    @PostMapping("/saveThirdInfo")
    public Boolean saveThirdInfo(@Validated @RequestBody SaveThirdInfoDTO saveThirdInfoDTO) {
        PlatformEnum platformEnum = IDict.getByCode(PlatformEnum.class, saveThirdInfoDTO.getPlatformType());
        return thirdInfoService.bindThirdInfo(platformEnum, saveThirdInfoDTO.getAppId(), saveThirdInfoDTO.getAppUserId(), UserContext.getUserId());
    }

}
