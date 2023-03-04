package com.msb.mall.product.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.common.exception.BizException;
import com.msb.framework.common.model.PageDTO;
import com.msb.framework.common.utils.ListUtil;
import com.msb.framework.web.result.BizAssert;
import com.msb.mall.product.api.model.ProductSkuOccupyDTO;
import com.msb.mall.product.api.model.ProductSkuReturnStockDTO;
import com.msb.mall.product.mapper.ProductSkuMapper;
import com.msb.mall.product.model.dto.admin.ProductSkuDTO;
import com.msb.mall.product.model.entity.Product;
import com.msb.mall.product.model.entity.ProductSku;
import com.msb.mall.product.model.vo.admin.ProductSkuVO;
import com.msb.mall.product.model.vo.app.ProductSkuSimpleVO;
import com.msb.mall.product.service.convert.ProductSkuConvert;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 商品sku(ProductSku)表服务实现类
 *
 * @author luozhan
 * @date 2022-03-29 14:37:40
 */
@Service("productSkuService")
public class ProductSkuService extends ServiceImpl<ProductSkuMapper, ProductSku> {
    @Resource
    private ProductService productService;
    @Resource
    private ProductSkuConvert productSkuConvert;
    @Resource
    private ProductSkuMapper productSkuMapper;

    public IPage<ProductSkuVO> page(PageDTO pageDTO, ProductSkuDTO productSkuDTO) {
        return productSkuConvert.toVo(this.page(pageDTO.page(), new QueryWrapper<ProductSku>().setEntity(productSkuConvert.toEntity(productSkuDTO))));
    }

    public ProductSkuVO getOne(Serializable id) {
        return productSkuConvert.toVo(this.getById(id));
    }

    /**
     * 新增或修改sku数据
     *
     * @param productId         商品id
     * @param productSkuDTOList sku数据
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean save(Long productId, List<ProductSkuDTO> productSkuDTOList) {
        if (CollectionUtils.isEmpty(productSkuDTOList)) {
            return false;
        }
        // 1.更新商品起售价，总库存
        Product product = new Product();
        product.setId(productId);
        // 起售价
        productSkuDTOList.stream().map(ProductSkuDTO::getSellPrice)
                .min(Comparator.naturalOrder())
                .ifPresent(product::setStartingPrice);
        productService.updateById(product);

        // 2.新增或修改sku数据
        List<ProductSku> productSkuList = productSkuDTOList.stream().map(productSkuDTO -> {
            ProductSku productSku = productSkuConvert.toEntity(productSkuDTO);
            productSku.setProductId(productId);
            // 更新库存数量时，忽略stock参数，通过第三步的stockChange来设置
            if (productSku.getId() != null) {
                productSku.setStock(null);
            }
            return productSku;
        }).collect(Collectors.toList());
        saveOrUpdateBatch(productSkuList);

        // 3.更新sku库存数量
        // 有id代表是修改，有stockChange且不等于0代表是更新库存数量
        Predicate<ProductSkuDTO> needUpdateStockPredicate = productSkuDTO -> (productSkuDTO.getId() != null
                && productSkuDTO.getStockChange() != null && !productSkuDTO.getStockChange().equals(0));
        productSkuDTOList.stream().filter(needUpdateStockPredicate).forEach(productSkuDTO -> {
            Integer stockChange = productSkuDTO.getStockChange();
            // 判断是增加库存还是扣减库存
            boolean result = stockChange > 0 ? getBaseMapper().addStock(productSkuDTO.getId(), stockChange)
                    : getBaseMapper().reduceStock(productSkuDTO.getId(), -stockChange);
            BizAssert.isTrue(result, "规格[" + productSkuDTO.getName() + "]库存不足，仅剩" + getById(productSkuDTO.getId()).getStock() + "，调整库存数量：" + stockChange);
        });
        return true;
    }

    public Boolean update(ProductSkuDTO productSkuDTO) {
        return this.updateById(productSkuConvert.toEntity(productSkuDTO));
    }

    public Boolean delete(List<Long> idList) {
        return this.removeByIds(idList);
    }

    /**
     * 根据sku id查询sku简要信息
     */
    public ProductSkuSimpleVO getSimpleSku(Long skuId) {
        return productSkuConvert.toSimpleVo(this.getById(skuId));
    }

    /**
     * 根据商品id查询sku信息
     *
     * @param productId 商品id
     * @return 商品sku列表
     */
    public List<ProductSku> list(Long productId) {
        return appLambdaQuery().eq(ProductSku::getProductId, productId).list();
    }

    /**
     * 根据商品id查询sku信息（app）
     *
     * @param productId 商品id
     * @return 商品sku列表
     */
    public List<ProductSkuSimpleVO> listSimpleVo(Long productId) {
        return productSkuConvert.toSimpleVo(list(productId));
    }

    /**
     * 根据商品id查询sku信息(后管)
     *
     * @param productId 商品id
     * @return 商品sku列表
     */
    public List<ProductSkuVO> listVo(Long productId) {
        List<ProductSku> productSkuList = lambdaQuery().eq(ProductSku::getProductId, productId).list();
        return productSkuConvert.toVo(productSkuList);
    }

    /**
     * 根据skuId列表查询sku信息
     */
    public List<ProductSku> list(List<Long> skuIdList) {
        return lambdaQuery().in(ProductSku::getId, skuIdList).list();
    }


    /**
     * 检查并扣减库存
     *
     * @param productSkuOccupyList skuId和数量
     */
    @Transactional(rollbackFor = Exception.class)
    public void checkAndReduceStock(List<ProductSkuOccupyDTO> productSkuOccupyList) {
        for (ProductSkuOccupyDTO productSkuOccupy : productSkuOccupyList) {
            Long skuId = productSkuOccupy.getSkuId();
            Integer quantity = productSkuOccupy.getQuantity();
            boolean result = productSkuMapper.reduceStock(skuId, quantity);
            BizAssert.isTrue(result, "商品" + "[" + productSkuOccupy.getProductName() + "]" + "库存不足");
        }
    }

    /**
     * 批量返还商品库存
     *
     * @param returnStockDTOList：返还库存DTO列表
     * @return void
     * @author peng.xy
     * @date 2022/5/16
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchReturnStock(List<ProductSkuReturnStockDTO> returnStockDTOList) {
        for (ProductSkuReturnStockDTO returnStockDTO : returnStockDTOList) {
            this.returnStock(returnStockDTO);
        }
    }

    /**
     * 返还商品库存
     *
     * @param returnStockDTO：返还库存DTO
     * @return void
     * @author peng.xy
     * @date 2022/5/16
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean returnStock(ProductSkuReturnStockDTO returnStockDTO) {
        Integer quantity = returnStockDTO.getQuantity();
        if (quantity == null || quantity.intValue() <= 0) {
            throw new BizException("数量必须大于0");
        }
        return productSkuMapper.addStock(returnStockDTO.getSkuId(), returnStockDTO.getQuantity());
    }

    /**
     * 删除商品下的所有sku
     *
     * @param productId 商品id
     */
    public void deleteAllByProductId(Long productId) {
        List<ProductSku> productSkuList = lambdaQuery().select(ProductSku::getId).eq(ProductSku::getProductId, productId).list();
        List<Long> idList = ListUtil.convert(productSkuList, ProductSku::getId);
        delete(idList);
    }


    /**
     * app查询默认设置：
     * 查询id、name、sellPrice、stock、attributeSymbolList
     * isEnable=1
     */
    private LambdaQueryChainWrapper<ProductSku> appLambdaQuery() {
        return super.lambdaQuery()
                .select(
                        ProductSku::getId,
                        ProductSku::getName,
                        ProductSku::getSellPrice,
                        ProductSku::getStock,
                        ProductSku::getAttributeSymbolList)
                .eq(ProductSku::getIsEnable, true);
    }

    /**
     * 统计商品总库存
     *
     * @param productId 商品id
     * @return 总库存
     */
    public BigDecimal getTotalStock(Long productId) {
        return baseMapper.getTotalStock(productId);
    }

    /**
     * 查询库存紧张的商品数量
     * @return 库存紧张的商品数量
     */
    public Integer getStockWarnCount() {
        return  productSkuMapper.getStockWarnCount();
    }
}

