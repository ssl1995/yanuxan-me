
package com.msb.im.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.msb.im.api.dto.UpdateStoreConfigDTO;
import com.msb.im.context.ApiContext;
import com.msb.im.model.dto.AddThirdSystemConfigDTO;
import com.msb.im.model.dto.DeleteThirdSystemConfigDTO;
import com.msb.im.model.dto.ThirdSystemConfigDTO;
import com.msb.im.model.vo.ThirdSystemConfigVO;
import com.msb.im.module.waiter.model.dto.AddStoreConfigDTO;
import com.msb.im.module.waiter.model.dto.DeleteStoreConfigDTO;
import com.msb.im.module.waiter.model.dto.StoreConfigDTO;
import com.msb.im.module.waiter.model.vo.StoreConfigVO;
import com.msb.im.module.waiter.service.StoreConfigService;
import com.msb.im.service.ThirdSystemConfigService;
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
@Api(tags = "(后管) 店铺管理接口")
@RequestMapping("/admin/storeConfig")
public class AdminStoreConfigController {
    @Resource
    private StoreConfigService storeConfigService;

    @ApiOperation(value = "新增店铺")
    @PostMapping
    public Boolean add(@Validated @RequestBody AddStoreConfigDTO addStoreConfigDTO) {
        storeConfigService.add(addStoreConfigDTO);
        return true;
    }

    @ApiOperation(value = "删除店铺")
    @DeleteMapping
    public Boolean delete(@Validated @RequestBody DeleteStoreConfigDTO deleteThirdSystemConfigDTO) {
        storeConfigService.delete(deleteThirdSystemConfigDTO);
        return true;
    }

    @ApiOperation(value = "查询店铺")
    @GetMapping
    public IPage<StoreConfigVO> page(StoreConfigDTO storeConfigDTO) {
        return storeConfigService.page(storeConfigDTO, storeConfigDTO);
    }

    @ApiOperation("修改店铺头像和昵称")
    @PutMapping
    public Boolean updateStoreData(@Validated @RequestBody UpdateStoreConfigDTO updateStoreConfigDTO) {
        storeConfigService.updateStoreData(updateStoreConfigDTO);
        return true;
    }
}
