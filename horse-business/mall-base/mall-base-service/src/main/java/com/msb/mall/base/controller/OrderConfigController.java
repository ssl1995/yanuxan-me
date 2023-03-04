package com.msb.mall.base.controller;





import com.baomidou.mybatisplus.core.metadata.IPage;
import com.msb.mall.base.model.vo.OrderConfigVO;
import com.msb.mall.base.model.dto.OrderConfigDTO;
import com.msb.mall.base.service.OrderConfigService;
import com.msb.user.auth.AuthAdmin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.msb.framework.common.model.PageDTO;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * 订单配置表(OrderConfig)表控制层
 *
 * @author shumengjiao
 * @since 2022-06-09 16:21:51
 */
@Api(tags = "后管-订单配置")
@AuthAdmin
@RestController
@RequestMapping("orderConfig")
public class OrderConfigController {
    /**
     * 服务对象
     */
    @Resource
    private OrderConfigService orderConfigService;

    @PostMapping("/saveOrUpdate")
    @ApiOperation("新增或更新订单配置")
    public Boolean saveOrUpdate(@Validated @RequestBody OrderConfigDTO orderConfigDTO) {
        return this.orderConfigService.saveOrUpdate(orderConfigDTO);
    }

    @GetMapping("/getOrderConfig")
    @ApiOperation("查询订单配置")
    public OrderConfigVO getOrderConfig() {
        return orderConfigService.getOrderConfig();
    }

}

