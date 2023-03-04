package com.msb.mall.im.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.msb.framework.common.context.UserContext;
import com.msb.mall.im.model.dto.AddWaiterUserDTO;
import com.msb.mall.im.model.dto.DeleteWaiterUserDTO;
import com.msb.mall.im.model.dto.UpdateWaiterUserDTO;
import com.msb.mall.im.model.dto.WaiterUserDTO;
import com.msb.mall.im.model.vo.WaiterUserVO;
import com.msb.mall.im.service.WaiterUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 获取登录凭证的
 *
 * @author zhou miao
 * @date 2022/05/25
 */
@RestController
@Api(tags = "（后管） 客服用户管理接口")
@RequestMapping("/admin/waiterUser")
public class AdminWaiterUserController {
    @Resource
    private WaiterUserService waiterUserService;

    @ApiOperation("通过登录用户查询客服信息")
    @GetMapping("/getWaiterByUserId")
    public WaiterUserVO getWaiterByUserId() {
        return waiterUserService.getWaiterByUserId(UserContext.getUserId());
    }

    @ApiOperation("新增客服")
    @PostMapping
    public Boolean add(@RequestBody AddWaiterUserDTO addWaiterUserDTO) {
        waiterUserService.add(addWaiterUserDTO);
        return true;
    }

    @ApiOperation("删除客服")
    @DeleteMapping
    public Boolean add(@RequestBody DeleteWaiterUserDTO deleteWaiterUserDTO) {
        waiterUserService.delete(deleteWaiterUserDTO);
        return true;
    }

    @ApiOperation("修改客服")
    @PutMapping
    public Boolean add(@RequestBody UpdateWaiterUserDTO updateWaiterUserDTO) {
        waiterUserService.update(updateWaiterUserDTO);
        return true;
    }

    @ApiOperation("分页查询客服")
    @GetMapping
    public IPage<WaiterUserVO> page(WaiterUserDTO waiterUserDTO) {
        return waiterUserService.page(waiterUserDTO, waiterUserDTO);
    }

}
