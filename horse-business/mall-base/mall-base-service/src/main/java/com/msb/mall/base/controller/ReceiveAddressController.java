package com.msb.mall.base.controller;


import com.msb.mall.base.model.dto.ReceiveAddressDTO;
import com.msb.mall.base.model.vo.ReceiveAddressVO;
import com.msb.mall.base.service.ReceiveAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * (ReceiveAddress)表控制层
 *
 * @author makejava
 * @date 2022-03-31 13:57:14
 */
@Api(tags = "收货地址相关接口")
@RestController
@RequestMapping("receiveAddress")
public class ReceiveAddressController {
    /**
     * 服务对象
     */
    @Resource
    private ReceiveAddressService receiveAddressService;

    @ApiOperation("获取当前用户的收货地址")
    @GetMapping
    public List<ReceiveAddressVO> listCurrentUserReceiveAddress() {
        return this.receiveAddressService.listByCurrentUserReceiveAddress();
    }

    @ApiOperation("新增收货地址")
    @PostMapping
    public ReceiveAddressVO save(@Validated @RequestBody ReceiveAddressDTO receiveAddressDTO) {
        return this.receiveAddressService.save(receiveAddressDTO);
    }

    @ApiOperation("修改收货地址")
    @PutMapping
    public ReceiveAddressVO update(@Validated @RequestBody ReceiveAddressDTO receiveAddressDTO) {
        return this.receiveAddressService.update(receiveAddressDTO);
    }

    @ApiOperation("删除收货地址")
    @DeleteMapping
    public Boolean delete(@RequestParam("idList") Long [] idList) {
        return this.receiveAddressService.delete(Arrays.asList(idList));
    }
}

