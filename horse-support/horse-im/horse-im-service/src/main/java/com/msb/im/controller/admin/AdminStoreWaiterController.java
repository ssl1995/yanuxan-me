
package com.msb.im.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.msb.im.api.dto.AddStoreWaiterDTO;
import com.msb.im.api.dto.DeleteStoreWaiterDTO;
import com.msb.im.api.dto.StoreWaiterDTO;
import com.msb.im.api.dto.UpdateStoreWaiterDTO;
import com.msb.im.api.vo.StoreWaiterVO;
import com.msb.im.module.waiter.service.StoreWaiterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author zhou miao
 * @date 2022/05/31
 */
@RestController
@Api(tags = "(后管) 店铺客服管理接口")
@RequestMapping("/admin/waiter")
public class AdminStoreWaiterController {
    @Resource
    private StoreWaiterService storeWaiterService;

    @ApiOperation(value = "查询店铺客服")
    @GetMapping
    public IPage<StoreWaiterVO> page(StoreWaiterDTO storeWaiterDTO) {
        return storeWaiterService.page(storeWaiterDTO, storeWaiterDTO);
    }

}
