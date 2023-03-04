package com.msb.mall.product.controller.admin;


import com.msb.mall.product.model.dto.admin.ProductAttributeGroupDTO;
import com.msb.mall.product.model.dto.admin.SortModifyDTO;
import com.msb.mall.product.model.vo.admin.ProductAttributeGroupVO;
import com.msb.mall.product.service.ProductAttributeGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 商品属性组(ProductAttributeGroup)表控制层
 *
 * @author luozhan
 * @date 2022-03-29 14:37:39
 */
@Api(tags = "后管-商品属性组")
@RestController
@RequestMapping("admin/productAttributeGroup")
public class ProductAttributeGroupController {

    @Resource
    private ProductAttributeGroupService productAttributeGroupService;

    @ApiOperation("根据商品id查询商品属性组列表")
    @GetMapping("{productId}")
    public List<ProductAttributeGroupVO> list(@PathVariable Long productId) {
        return this.productAttributeGroupService.getVOByProductId(productId);
    }

    @ApiOperation("新增商品属性组，返回id")
    @PostMapping
    public Long save(@RequestBody @Valid ProductAttributeGroupDTO productAttributeGroupDTO) {
        return this.productAttributeGroupService.save(productAttributeGroupDTO);
    }

    @ApiOperation("更新商品属性组")
    @PutMapping("{id}")
    public Boolean update(@PathVariable Long id, @RequestBody @Valid ProductAttributeGroupDTO productAttributeGroupDTO) {
        return this.productAttributeGroupService.update(id, productAttributeGroupDTO);
    }

    @ApiOperation("更新商品属性组排序")
    @PutMapping("updateSort")
    public Boolean updateSort(@RequestBody @Valid SortModifyDTO sortModifyDTO) {
        return this.productAttributeGroupService.updateSort(sortModifyDTO);
    }

    @ApiOperation("删除商品属性组（在保存sku信息的时候才一起请求）")
    @DeleteMapping
    public Boolean delete(@ApiParam("属性组id（多个用逗号分隔）") @RequestParam("id") List<Long> idList) {
        return this.productAttributeGroupService.delete(idList);
    }
}

