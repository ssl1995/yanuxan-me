package com.msb.mall.product.controller.app;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.msb.framework.common.model.PageDTO;
import com.msb.framework.common.utils.ListUtil;
import com.msb.mall.marketing.api.LabelDubboService;
import com.msb.mall.marketing.api.model.ProductLabelDO;
import com.msb.mall.product.manager.ProductManager;
import com.msb.mall.product.model.dto.admin.ProductQueryDTO;
import com.msb.mall.product.model.vo.app.*;
import com.msb.mall.product.service.ProductCategoryService;
import com.msb.mall.product.service.ProductService;
import com.msb.user.auth.NoAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品(Product)表控制层
 *
 * @author luozhan
 * @date 2022-03-29 14:37:39
 */
@Api(tags = "APP-商品接口")
@RestController
@NoAuth
@Slf4j
@RequestMapping("app/product")
public class ProductAppController {
    /**
     * 服务对象
     */
    @Resource
    private ProductService productService;
    @Resource
    private ProductCategoryService productCategoryService;
    @Resource
    private ProductManager productManager;
    @DubboReference(timeout = 10000)
    private LabelDubboService labelDubboService;

    /**
     * 商品瀑布流
     *
     * @param productQueryDTO 查询条件
     * @return page
     */
    @ApiOperation(value = "商品瀑布流", notes = "商品瀑布流", httpMethod = "GET")
    @GetMapping("page")
    public IPage<ProductSimpleVO> page(ProductQueryDTO productQueryDTO) {
        IPage<ProductSimpleVO> productSimplePage = productService.pageSimpleProduct(productQueryDTO);
        handleProductLabel(productSimplePage.getRecords());
        return productSimplePage;
    }

    private void handleProductLabel(List<? extends ProductSimpleVO> productSimpleVOList) {
        try {
            List<Long> productIdList = ListUtil.convert(productSimpleVOList, ProductSimpleVO::getId);
            List<ProductLabelDO> productLabelDOList = labelDubboService.listProductLabel(productIdList);
            ListUtil.match(productSimpleVOList, productLabelDOList, ProductSimpleVO::getId, ProductLabelDO::getProductId, (productSimpleVO, productLabelDO) -> {
                productSimpleVO.setLabelList(productLabelDO.getLabelList());
            });
        } catch (Exception e) {
            log.error("查询商品标签失败", e);
        }
    }


    /**
     * 分类导航（banner下方区域）
     *
     * @return list
     */
    @ApiOperation(value = "分类导航（banner下方区域）", notes = "分类导航（banner下方区域）", httpMethod = "GET")
    @GetMapping("categoryNavigation")
    public List<ProductCategoryAppVO> listCategoryNavigation() {
        return productCategoryService.listCategoryNavigation();
    }

    /**
     * 根据分类id查询商品
     *
     * @param pageDTO    分页
     * @param categoryId 一级分类id
     * @return page
     */
    @ApiOperation(value = "按分类查询相关商品", notes = "按分类查询相关商品", httpMethod = "GET")
    @GetMapping("byCategory")
    public IPage<ProductSimpleVO> pageByCategory(@ApiParam("商品分类id") @RequestParam Long categoryId, PageDTO pageDTO) {
        IPage<ProductSimpleVO> productSimplePage = productService.pageByCategory(categoryId, pageDTO);
        handleProductLabel(productSimplePage.getRecords());
        return productSimplePage;
    }

    /**
     * 商品推荐
     *
     * @return page
     */
    @ApiOperation(value = "商品推荐", notes = "商品推荐", httpMethod = "GET")
    @GetMapping("recommended")
    public List<ProductRecommendVO> pageProductRecommend() {
        List<ProductRecommendVO> productRecommendList = productService.pageProductRecommend();
        handleProductLabel(productRecommendList);
        return productRecommendList;
    }


    /**
     * 查询商品详情
     *
     * @param productId 商品id
     * @return 商品详情
     */
    @ApiOperation("查询商品详情")
    @GetMapping("{productId}")
    public ProductDetailVO getProductDetail(@PathVariable @ApiParam("商品id") Long productId) {
        return productManager.getProductDetailPage(productId);
    }


    /**
     * 根据商品id查询sku信息
     *
     * @param productId 商品id
     * @return 商品sku列表
     */
    @ApiOperation("根据商品id查询sku信息")
    @GetMapping("sku")
    public List<ProductSkuSimpleVO> listSkuByProductId(@ApiParam("商品id") Long productId) {
        return productManager.listProductSku(productId);
    }

    @ApiOperation("根据商品id查询sku信息及评论数")
    @GetMapping("listSkuAndCommentCount/{productId}")
    public List<ProductSkuCommentVO> listSkuAndCommentByProductId(@PathVariable @ApiParam("商品id") Long productId) {
        return productManager.listSkuAndCommentCount(productId);
    }

}

