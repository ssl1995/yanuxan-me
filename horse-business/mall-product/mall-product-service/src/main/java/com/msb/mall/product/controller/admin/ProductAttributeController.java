package com.msb.mall.product.controller.admin;


import com.msb.mall.product.model.dto.admin.ProductAttributeDTO;
import com.msb.mall.product.model.dto.admin.SortModifyDTO;
import com.msb.mall.product.model.vo.admin.ProductAttributeVO;
import com.msb.mall.product.service.ProductAttributeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 商品属性(ProductAttribute)表控制层
 *
 * @author luozhan
 * @date 2022-03-29 14:37:39
 */
@Api(tags = "后管-商品属性")
@RestController
@RequestMapping("admin/productAttribute")
public class ProductAttributeController {

    @Resource
    private ProductAttributeService productAttributeService;

    @ApiOperation("根据属性组id查询属性列表")
    @GetMapping("list/{groupId}")
    public List<ProductAttributeVO> listByGroupId(@PathVariable Long groupId) {
        return productAttributeService.listVo(groupId);
    }

    @ApiOperation("新增商品属性，返回id")
    @PostMapping
    public Long save(@RequestBody ProductAttributeDTO productAttributeDTO) {
        return productAttributeService.save(productAttributeDTO);
    }

    @ApiOperation("更新商品属性")
    @PutMapping({"{id}"})
    public Boolean update(@PathVariable Long id, @RequestBody ProductAttributeDTO productAttributeDTO) {
        return productAttributeService.update(id, productAttributeDTO);
    }

    @ApiOperation("更新商品属性排序")
    @PutMapping("updateSort")
    public Boolean updateSort(@RequestBody @Valid SortModifyDTO sortModifyDTO) {
        return productAttributeService.updateSort(sortModifyDTO);
    }

    @ApiOperation("删除商品属性")
    @DeleteMapping
    public Boolean delete(@ApiParam("id（多个用逗号分隔）") @RequestParam("id") List<Long> idList) {
        return productAttributeService.delete(idList);
    }
}

