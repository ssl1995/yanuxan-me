package com.msb.im.controller.api;

import com.msb.im.api.dto.UpdateStoreConfigDTO;
import com.msb.im.context.ApiContext;
import com.msb.im.module.waiter.service.StoreConfigService;
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
@Api(tags = "(api) 店铺配置接口")
@RestController
@RequestMapping("/api/store")
@Slf4j
@NoAuth
public class ApiStoreConfigController {
    @Resource
    private StoreConfigService storeConfigService;

    @NoAuth
    @ApiOperation("修改店铺头像和昵称")
    @PutMapping
    public void updateStoreData(@RequestBody UpdateStoreConfigDTO updateStoreConfigDTO) {
        Integer systemId = ApiContext.getSystemId();
        updateStoreConfigDTO.setSystemId(systemId);
        storeConfigService.updateStoreData(updateStoreConfigDTO);
    }

}
