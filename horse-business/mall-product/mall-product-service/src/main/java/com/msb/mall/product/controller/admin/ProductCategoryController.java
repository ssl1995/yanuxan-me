package com.msb.mall.product.controller.admin;


import com.msb.mall.product.model.dto.admin.ProductCategoryModifyDTO;
import com.msb.mall.product.model.dto.admin.ProductCategoryQueryDTO;
import com.msb.mall.product.model.dto.admin.SortModifyDTO;
import com.msb.mall.product.model.vo.admin.ProductCategoryVO;
import com.msb.mall.product.service.ProductCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 后管-商品分类接口
 *
 * @author luozhan
 * @date 2022-03-29 14:37:39
 */
@Api(tags = "后管-商品分类")
@RestController
@RequestMapping("admin/productCategory")
public class ProductCategoryController {

    @Resource
    private ProductCategoryService productCategoryService;

    @ApiOperation("查询分类列表（树状结构）")
    @GetMapping
    public List<ProductCategoryVO> list(ProductCategoryQueryDTO productCategoryQueryDTO) {
        return productCategoryService.listUseTree();
    }


    @ApiOperation("添加分类")
    @PostMapping
    public Boolean save(@RequestBody @Validated ProductCategoryModifyDTO productCategoryModifyDTO) {
        return this.productCategoryService.save(productCategoryModifyDTO);
    }

    @ApiOperation("转移商品")
    @PutMapping("transfer/{oldId}/{newId}")
    public int transfer(@PathVariable Long oldId, @PathVariable Long newId) {
        return this.productCategoryService.transfer(oldId, newId);
    }

    @ApiOperation("更新排序")
    @PutMapping("updateSort")
    public Boolean updateSort(@RequestBody @Validated SortModifyDTO sortModifyDTO) {
        return this.productCategoryService.updateSort(sortModifyDTO);
    }

    @ApiOperation("更新分类")
    @PutMapping("{id}")
    public Boolean update(@PathVariable Long id, @RequestBody @Valid ProductCategoryModifyDTO productCategoryModifyDTO) {
        return this.productCategoryService.update(id, productCategoryModifyDTO);
    }

    @ApiOperation("删除分类")
    @DeleteMapping
    public Boolean delete(@ApiParam("分类id（多个用逗号分隔）") @RequestParam("id") List<Long> idList) {
        return this.productCategoryService.delete(idList);
    }
}

