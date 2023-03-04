package com.msb.mall.im.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.msb.framework.common.context.UserContext;
import com.msb.im.api.dto.StoreWaiterDTO;
import com.msb.im.api.dto.TransferWaiterDTO;
import com.msb.im.api.vo.StoreWaiterVO;
import com.msb.mall.im.service.MallImWaiterService;
import com.msb.mall.im.service.WaiterUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (User)表控制层
 *
 * @author makejava
 * @since 2022-03-23 20:13:02
 */
@Api(tags = "（后管） 客服接口")
@RestController
@RequestMapping("/admin/waiter")
@Slf4j
public class MallImAdminWaiterController {

    @Resource
    private MallImWaiterService mallImWaiterService;

    @ApiOperation("分页查询客服")
    @GetMapping
    public IPage<StoreWaiterVO> page(StoreWaiterDTO storeWaiterDTO) {
        return mallImWaiterService.page(storeWaiterDTO);
    }

    @ApiOperation("转移会话")
    @PostMapping("/transfer")
    public void transfer(@RequestBody TransferWaiterDTO transferWaiterDTO) {
        mallImWaiterService.transfer(transferWaiterDTO);
    }

}

