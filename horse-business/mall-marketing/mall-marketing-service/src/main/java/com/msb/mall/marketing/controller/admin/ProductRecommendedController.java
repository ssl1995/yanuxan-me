package com.msb.mall.marketing.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.msb.mall.marketing.model.dto.ProductRecommendedEnableDTO;
import com.msb.mall.marketing.model.dto.ProductRecommendedQueryDTO;
import com.msb.mall.marketing.model.vo.ProductRecommendedVO;
import com.msb.mall.marketing.model.dto.ProductRecommendedDTO;
import com.msb.mall.marketing.service.ProductRecommendedService;
import com.msb.user.auth.AuthAdmin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.msb.framework.common.model.PageDTO;
import org.mapstruct.Mapper;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * 商品推荐表(ProductRecommended)表控制层
 *
 * @author makejava
 * @date 2022-04-13 15:44:17
 */
@Api(tags = "（后管）商品推荐相关接口")
@AuthAdmin
@RestController
@RequestMapping("productRecommended")
public class ProductRecommendedController {
    /**
     * 服务对象
     */
    @Resource
    private ProductRecommendedService productRecommendedService;

    @ApiOperation("商品推荐分页查询")
    @GetMapping
    public IPage<ProductRecommendedVO> page(ProductRecommendedQueryDTO productRecommendedQueryDTO) {
        return productRecommendedService.page(productRecommendedQueryDTO);
    }

    @ApiOperation("增加推荐商品")
    @PostMapping
    public Boolean save(@Validated @RequestBody ProductRecommendedDTO productRecommendedDTO) {
        return this.productRecommendedService.save(productRecommendedDTO);
    }

    @ApiOperation("开启 或 关闭是否推荐")
    @PutMapping
    public Boolean enable(@Validated @RequestBody ProductRecommendedEnableDTO productRecommendedDTO) {
        return this.productRecommendedService.enable(productRecommendedDTO);
    }

    @ApiOperation("移除商品推荐")
    @DeleteMapping
    public Boolean delete(@Validated @RequestParam("idList") @NotNull Long [] idList) {
        return this.productRecommendedService.delete(Arrays.asList(idList));
    }
}

