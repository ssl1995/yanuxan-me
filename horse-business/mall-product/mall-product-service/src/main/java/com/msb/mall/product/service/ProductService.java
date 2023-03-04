package com.msb.mall.product.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.common.model.PageDTO;
import com.msb.framework.web.result.BizAssert;
import com.msb.mall.marketing.api.ProductRecommendDubboService;
import com.msb.mall.product.api.model.ProductDO;
import com.msb.mall.product.config.ProductProperties;
import com.msb.mall.product.enums.ProductTypeEnums;
import com.msb.mall.product.mapper.ProductMapper;
import com.msb.mall.product.model.dto.admin.ProductModifyDTO;
import com.msb.mall.product.model.dto.admin.ProductQueryDTO;
import com.msb.mall.product.model.entity.Product;
import com.msb.mall.product.model.entity.ProductCategory;
import com.msb.mall.product.model.entity.ProductDetail;
import com.msb.mall.product.model.vo.admin.ProductModifyVO;
import com.msb.mall.product.model.vo.admin.ProductVO;
import com.msb.mall.product.model.vo.admin.VirtualProductVO;
import com.msb.mall.product.model.vo.app.*;
import com.msb.mall.product.service.convert.ProductConvert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 商品(Product)表服务实现类
 *
 * @author luozhan
 * @date 2022-03-29 14:37:39
 */
@Slf4j
@Service("productService")
public class ProductService extends ServiceImpl<ProductMapper, Product> {

    @Resource
    private ProductConvert productConvert;
    @Resource
    private ProductProperties productProperties;
    @Resource
    private ProductAttributeGroupService productAttributeGroupService;
    @Resource
    private ProductDetailService productDetailService;
    @Resource
    private ProductSkuService productSkuService;
    @Resource
    private ProductPictureService productPictureService;
    @Resource
    private ProductAttributeService productAttributeService;
    @Resource
    private ProductCategoryService productCategoryService;
    @Resource
    private VirtualProductService virtualProductService;

    @DubboReference
    private ProductRecommendDubboService productRecommendDubboService;

    /**
     * 根据id查询商品简要信息
     */
    public ProductSimpleVO getSimpleProduct(Long productId) {
        return productConvert.toSimpleVo(this.getById(productId));
    }

    public ProductDO getProductDO(Long productId) {
        return productConvert.toDo(this.getById(productId));
    }

    public List<ProductDO> listProductDO(List<Long> productIdList) {
        return productConvert.toDo(this.listByIds(productIdList));
    }

    /**
     * 根据条件分页查询商品简要信息
     */
    public IPage<ProductSimpleVO> pageSimpleProduct(ProductQueryDTO productQueryDTO) {
        List<Long> targetCategoryIdList = new ArrayList<>();
        if (productQueryDTO.getCategoryId() != null) {
            targetCategoryIdList.add(productQueryDTO.getCategoryId());

            List<ProductCategory> productCategoryList = productCategoryService.listByParentId(productQueryDTO.getCategoryId());
            // 循环搜寻子集
            while (CollectionUtils.isNotEmpty(productCategoryList)) {
                productCategoryList.forEach(productCategory -> targetCategoryIdList.add(productCategory.getId()));
                productCategoryList = productCategoryList.stream()
                        .map(ProductCategory::getId)
                        .flatMap(id -> productCategoryService.listByParentId(id).stream())
                        .collect(Collectors.toList());
            }


        }
        Page<Product> pageResult = appLambdaQuery()
                .like(StringUtils.isNotEmpty(productQueryDTO.getName()), Product::getName, productQueryDTO.getName())
                .in(CollectionUtils.isNotEmpty(targetCategoryIdList), Product::getCategoryId, targetCategoryIdList)
                .page(productQueryDTO.page());
        return productConvert.toSimpleVo(pageResult);
    }

    public ProductModifyVO getOne(Long productId) {
        Product product = getById(productId);
        ProductDetail productDetail = productDetailService.getById(productId);
        BizAssert.notNull(productDetail, "商品详情数据异常，找不到商品数据：" + productId);
        List<String> productPictureList = productPictureService.listProductPictureByProductId(productId);
        ProductModifyVO productModifyVO = productConvert.toModifyVO(product);
        productModifyVO.setDetail(productDetail.getDetail());
        productModifyVO.setPictureList(productPictureList);
        productModifyVO.setIsRecommend(productRecommendDubboService.getProductRecommend(productId));
        // 如果商品类型是虚拟商品 需要查询虚拟发货信息进行返回
        if (ProductTypeEnums.VIRTUAL.getCode().equals(product.getProductType())) {
            List<VirtualProductVO> virtualProductVOS = virtualProductService.listVirtualProduct(productId);
            productModifyVO.setVirtualProductVOList(virtualProductVOS);
        }
        return productModifyVO;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long save(ProductModifyDTO productModifyDTO) {
        List<String> pictureList = productModifyDTO.getPictureList();
        String detail = productModifyDTO.getDetail();
        Product product = productConvert.toEntity(productModifyDTO);
        // 1.写入商品表
        // 设置主图
        product.setMainPicture(productModifyDTO.getPictureList().get(0));
        save(product);
        Long productId = product.getId();
        // 2.写入商品详情表
        productDetailService.save(new ProductDetail().setProductId(productId).setDetail(detail));
        // 3.写入商品图片表
        productPictureService.savePictureList(productId, pictureList);
        // 设置商品推荐
        if (Boolean.TRUE.equals(productModifyDTO.getIsRecommend())) {
            productRecommendDubboService.setProductRecommend(productId);
        }
        // 如果是虚拟商品 更新虚拟商品信息
        if (ProductTypeEnums.VIRTUAL.getCode().equals(product.getProductType())) {
            BizAssert.notEmpty(productModifyDTO.getVirtualProductModifyDTOList(), "虚拟商品的文件、内容必填一项");
            virtualProductService.deleteAllAndAdd(productId, productModifyDTO.getVirtualProductModifyDTOList());
        }
        return productId;
    }

    public Boolean update(Long productId, ProductModifyDTO productModifyDTO) {
        // 根据商品id查询商品表 如果修改了商品类型 抛出异常
        Product oldProduct = getById(productId);
        BizAssert.isTrue(oldProduct.getProductType().equals(productModifyDTO.getProductType()), "不能修改商品类型,productId:" + productId);
        Product product = productConvert.toEntity(productModifyDTO);
        // 更新商品表
        product.setId(productId);
        product.setMainPicture(productModifyDTO.getPictureList().get(0));
        updateById(product);
        // 商品详情表
        String detail = productModifyDTO.getDetail();
        productDetailService.update(productId, detail);
        // 商品图片表
        productPictureService.savePictureList(productId, productModifyDTO.getPictureList());
        // 设置商品推荐
        if (Boolean.TRUE.equals(productModifyDTO.getIsRecommend())) {
            productRecommendDubboService.setProductRecommend(productId);
        } else {
            productRecommendDubboService.delProductRecommend(productId);
        }
        // 如果是虚拟商品 保存虚拟商品信息
        if (ProductTypeEnums.VIRTUAL.getCode().equals(product.getProductType())) {
            BizAssert.notEmpty(productModifyDTO.getPictureList(), "虚拟商品的文件、内容必填一项");
            virtualProductService.deleteAllAndAdd(productId, productModifyDTO.getVirtualProductModifyDTOList());
        }
        return true;
    }

    /**
     * 上下架
     *
     * @param productId 商品id
     * @param isEnable  上架状态
     * @return boolean
     */
    public Boolean enableOrDisable(Long productId, Boolean isEnable) {
        Product product = new Product();
        product.setId(productId);
        product.setIsEnable(isEnable);
        return updateById(product);
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(List<Long> idList) {
        for (Long id : idList) {
            productRecommendDubboService.delProductRecommend(id);
        }
        return removeByIds(idList);
    }

    /**
     * 查询甄选推荐列表
     * 根据配置的商品id，查询出数据库中的相关信息，组合成商品推荐信息后返回
     *
     * @return page
     */
    public List<ProductRecommendVO> pageProductRecommend() {
        List<ProductRecommendVO> recommendList = productProperties.getRecommend();
        log.info("配置的推荐商品{}", recommendList);
        return recommendList.stream().map(recommendProduct -> {
            Product product = appLambdaQuery().eq(Product::getId, recommendProduct.getId()).oneOpt().orElse(new Product());

            ProductRecommendVO productRecommendVO = productConvert.toRecommendVo(product);
            productRecommendVO.setTitle(recommendProduct.getTitle());
            productRecommendVO.setSubtitle(recommendProduct.getSubtitle());
            productRecommendVO.setRecommendPicture(recommendProduct.getRecommendPicture());
            return productRecommendVO;
        }).collect(Collectors.toList());
    }

    /**
     * 根据分类id查询商品
     *
     * @param categoryId 分类id
     * @param pageDTO    分页
     * @return page
     */
    public IPage<ProductSimpleVO> pageByCategory(Long categoryId, PageDTO pageDTO) {
        Page<Product> result = appLambdaQuery().eq(Product::getCategoryId, categoryId).page(pageDTO.page());
        return productConvert.toSimpleVo(result);
    }

    /**
     * 查询商品详情
     *
     * @param productId 商品id
     * @return 商品详情
     */
    public ProductDetailVO getProductDetail(Long productId) {
        Product product = lambdaQuery().eq(Product::getId, productId).one();
        ProductDetailVO productDetailVO = productConvert.toDetailVo(product);
        // 查询商品对应的属性信息
        List<ProductAttributeGroupSimpleVO> productAttributeGroupList = productAttributeGroupService.getSimpleVOByProductId(productDetailVO.getId());
        productAttributeGroupList.forEach(productAttributeGroup -> {
            List<ProductAttributeSimpleVO> productAttributeList = productAttributeService.listSimpleVo(productAttributeGroup.getId());
            productAttributeGroup.setAttributes(productAttributeList);
        });
        productDetailVO.setAttributeGroupList(productAttributeGroupList);
        // 查询商品图集
        List<String> productPictureList = productPictureService.listProductPictureByProductId(productId);
        productDetailVO.setPictureList(productPictureList);
        // 查询商品详情内容
        Optional.ofNullable(productDetailService.getById(productId))
                .ifPresent(productDetail -> productDetailVO.setDetail(productDetail.getDetail()));
        return productDetailVO;
    }

    /**
     * 商品列表（分页）
     *
     * @param productQueryDTO 查询条件
     * @return page
     */
    public IPage<ProductVO> page(ProductQueryDTO productQueryDTO) {
        String name = productQueryDTO.getName();
        Long categoryId = productQueryDTO.getCategoryId();
        Page<Product> productPage = lambdaQuery().like(StringUtils.isNotEmpty(name), Product::getName, name)
                .eq(categoryId != null, Product::getCategoryId, categoryId)
                .page(productQueryDTO.page());
        Page<ProductVO> productVoPage = productConvert.toVo(productPage);
        // 统计商品总库存
        productVoPage.getRecords().forEach(productVO -> productVO.setTotalStock(productSkuService.getTotalStock(productVO.getId())));
        return productVoPage;
    }

    /**
     * 根据分类id查询商品信息
     *
     * @param categoryId 分类id
     * @return list
     */
    public List<ProductSimpleVO> listSimpleProductByCategoryId(Long categoryId) {
        List<Product> result = appLambdaQuery().eq(Product::getCategoryId, categoryId).list();
        return productConvert.toSimpleVo(result);
    }

    /**
     * 根据商品id列表查询商品
     */
    public List<Product> listProduct(List<Long> productIdList) {
        return lambdaQuery().in(Product::getId, productIdList).list();
    }

    /**
     * app查询默认设置：
     * 查询id、name、picture
     * isEnable=1 上架
     * 按sort字段排序
     */
    public LambdaQueryChainWrapper<Product> appLambdaQuery() {
        return super.lambdaQuery().select(Product::getId, Product::getName, Product::getStartingPrice, Product::getMainPicture, Product::getProductType)
                .eq(Product::getIsEnable, true)
                .orderByDesc(Product::getCreateTime);
    }


    /**
     * 更新商品分类
     *
     * @param oldCategoryId 老分类id
     * @param newCategoryId 新分类id
     * @return boolean
     */
    public int updateCategory(Long oldCategoryId, Long newCategoryId) {
        List<Product> productList = lambdaQuery().eq(Product::getCategoryId, oldCategoryId).list();
        productList.forEach(product -> product.setCategoryId(newCategoryId));
        updateBatchById(productList, 1000);
        return productList.size();
    }
}

