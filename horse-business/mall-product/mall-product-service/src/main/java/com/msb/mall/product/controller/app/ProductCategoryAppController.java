package com.msb.mall.product.controller.app;


import com.msb.mall.product.model.vo.app.ProductCategoryAppVO;
import com.msb.mall.product.service.ProductCategoryService;
import com.msb.user.auth.NoAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品分类(ProductCategory)表控制层
 *
 * @author luozhan
 * @date 2022-03-29 14:37:39
 */
@Api(tags = "APP-商品分类接口")
@NoAuth
@RestController
@RequestMapping("app/productCategory")
public class ProductCategoryAppController {
    /**
     * 服务对象
     */
    @Resource
    private ProductCategoryService productCategoryService;

//    @GetMapping
//    public List<ProductCategoryVO> listLevelOne() {
//        return productCategoryService.listLevelOne();
//    }
//
//    @GetMapping("{id}")
//    public ProductCategoryVO getOne(@PathVariable Serializable id) {
//        return this.productCategoryService.getOne(id);
//    }
//
//    @PostMapping
//    public Boolean save(@RequestBody ProductCategoryDTO productCategoryDTO) {
//        return this.productCategoryService.save(productCategoryDTO);
//    }
//
//    @PutMapping
//    public Boolean update(@RequestBody ProductCategoryDTO productCategoryDTO) {
//        return this.productCategoryService.update(productCategoryDTO);
//    }
//
//    @DeleteMapping
//    public Boolean delete(@RequestParam("idList") List<Long> idList) {
//        return this.productCategoryService.delete(idList);
//    }

    /**
     * 一级分类列表
     */
    @ApiOperation(value = "一级分类列表", notes = "一级分类列表", httpMethod = "GET")
    @GetMapping("levelOne")
    public List<ProductCategoryAppVO> listLevelOne() {
        return productCategoryService.listLevelOne();
    }

    /**
     * 查询二级分类及商品
     */
    @ApiOperation(value = "查询二级分类及商品", notes = "查询二级分类及商品", httpMethod = "GET")
    @GetMapping("listCategoryAndProduct/{categoryId}")
    public List<ProductCategoryAppVO> listChildCategoryAndProduct(@PathVariable @ApiParam("一级分类id") long categoryId) {
        return productCategoryService.listChildCategoryAndProduct(categoryId);
    }
}

