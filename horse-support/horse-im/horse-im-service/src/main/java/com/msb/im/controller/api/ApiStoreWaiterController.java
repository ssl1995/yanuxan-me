
package com.msb.im.controller.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.msb.im.api.dto.*;
import com.msb.im.context.ApiContext;
import com.msb.im.api.vo.StoreWaiterVO;
import com.msb.im.module.waiter.service.StoreWaiterService;
import com.msb.user.auth.NoAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author zhou miao
 * @date 2022/05/31
 */
@RestController
@Api(tags = "(api) 店铺客服管理接口")
@RequestMapping("/api/waiter")
@NoAuth
public class ApiStoreWaiterController {
    @Resource
    private StoreWaiterService storeWaiterService;

    @NoAuth
    @ApiOperation(value = "新增店铺客服")
    @PostMapping
    public void add(@RequestBody AddStoreWaiterDTO addStoreWaiterDTO) {
        Integer systemId = ApiContext.getSystemId();
        addStoreWaiterDTO.setSystemId(systemId);
        storeWaiterService.add(addStoreWaiterDTO);
    }

    @NoAuth
    @ApiOperation(value = "删除店铺客服")
    @DeleteMapping
    public void delete(@RequestBody DeleteStoreWaiterDTO deleteStoreWaiterDTO) {
        Integer systemId = ApiContext.getSystemId();
        deleteStoreWaiterDTO.setSystemId(systemId);
        storeWaiterService.delete(deleteStoreWaiterDTO);
    }

    @NoAuth
    @ApiOperation(value = "更新店铺客服")
    @PutMapping
    public void update(@RequestBody UpdateStoreWaiterDTO updateStoreWaiterDTO) {
        Integer systemId = ApiContext.getSystemId();
        updateStoreWaiterDTO.setSystemId(systemId);
        storeWaiterService.update(updateStoreWaiterDTO);
    }

    @ApiOperation(value = "查询店铺客服")
    @GetMapping
    @NoAuth
    public IPage<StoreWaiterVO> page(StoreWaiterDTO storeWaiterDTO) {
        Integer systemId = ApiContext.getSystemId();
        storeWaiterDTO.setSystemId(systemId);
        return storeWaiterService.page(storeWaiterDTO, storeWaiterDTO);
    }

    @NoAuth
    @ApiOperation(value = "转移会话")
    @PostMapping("/transfer")
    public void transfer(@RequestBody TransferWaiterDTO transferWaiterDTO) {
        Integer systemId = ApiContext.getSystemId();
        transferWaiterDTO.setSystemId(systemId);
        storeWaiterService.transfer(transferWaiterDTO);
    }
}
