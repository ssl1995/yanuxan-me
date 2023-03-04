package com.msb.mall.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.redis.RedisClient;
import com.msb.mall.marketing.mapper.ProductRecommendedMapper;
import com.msb.mall.marketing.model.dto.ProductRecommendedDTO;
import com.msb.mall.marketing.model.dto.ProductRecommendedEnableDTO;
import com.msb.mall.marketing.model.dto.ProductRecommendedQueryDTO;
import com.msb.mall.marketing.model.entity.ProductRecommended;
import com.msb.mall.marketing.model.vo.ProductRecommendedVO;
import com.msb.mall.marketing.service.convert.ProductRecommendedConvert;
import com.msb.mall.product.api.dubbo.ProductDubboService;
import com.msb.mall.product.api.model.ProductDO;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.msb.mall.marketing.model.RedisKeysConstants.PRODUCT_RECOMMENDED;

/**
 * 商品推荐表(ProductRecommended)表服务实现类
 *
 * @author makejava
 * @date 2022-04-13 15:44:17
 */
@Service("productRecommendedService")
public class ProductRecommendedService extends ServiceImpl<ProductRecommendedMapper, ProductRecommended> {

    @DubboReference
    private ProductDubboService productDubboService;

    @Resource
    private ProductRecommendedConvert productRecommendedConvert;

    @Resource
    private RedisClient redisClient;

    private void deleteProductRecommendedCache(Long productId) {
        redisClient.delete(PRODUCT_RECOMMENDED.concat(String.valueOf(productId)));
    }

    private void putProductRecommendedCache(Long productId, Boolean flag) {
        redisClient.set(PRODUCT_RECOMMENDED.concat(String.valueOf(productId)), flag, 10, TimeUnit.DAYS);
    }

    private Boolean getProductRecommendedCache(Long productId) {
        return redisClient.get(PRODUCT_RECOMMENDED.concat(String.valueOf(productId)));
    }

    public IPage<ProductRecommendedVO> page(ProductRecommendedQueryDTO productRecommendedQueryDTO) {
        Page<ProductRecommended> page = this.lambdaQuery()
                .like(StringUtils.isNotBlank(productRecommendedQueryDTO.getProductName()), ProductRecommended::getProductName, productRecommendedQueryDTO.getProductName())
                .eq(Objects.nonNull(productRecommendedQueryDTO.getIsEnable()), ProductRecommended::getIsEnable, productRecommendedQueryDTO.getIsEnable())
                .page(productRecommendedQueryDTO.page());
        return productRecommendedConvert.toVo(page);
    }

    public Boolean enable(ProductRecommendedEnableDTO productRecommendedEnableDTO) {
        Long id = productRecommendedEnableDTO.getId();
        Optional<ProductRecommended> productRecommended = this.lambdaQuery().eq(ProductRecommended::getId, id).oneOpt();
        productRecommended.ifPresent(p -> {
            if (!productRecommendedEnableDTO.getIsEnable()) {
                deleteProductRecommendedCache(p.getProductId());
            }
            updateById(new ProductRecommended()
                    .setId(productRecommendedEnableDTO.getId())
                    .setIsEnable(productRecommendedEnableDTO.getIsEnable()));
        });
        return true;
    }

    private Stream<ProductRecommended> productMapToProductRecommended(List<Long> productIdList) {
        return productIdList.stream().map(productId ->
        {
            Optional<ProductDO> productOptional = Optional.ofNullable(productDubboService.getProductById(productId));
            return productOptional.map(productDO -> {
                ProductDO product = productOptional.get();
                ProductRecommended productRecommendedNew = new ProductRecommended()
                        .setProductId(productId)
                        .setProductMainPicture(product.getMainPicture())
                        .setProductName(product.getName())
                        .setIsEnable(Boolean.TRUE);
                Optional<ProductRecommended> productRecommendedOptional = lambdaQuery()
                        .eq(ProductRecommended::getProductId, productRecommendedNew.getProductId()).oneOpt();
                productRecommendedOptional.ifPresent(productRecommended -> productRecommendedNew.setId(productRecommended.getId()));
                return productRecommendedNew;
            }).orElse(null);
        }).filter(Objects::nonNull);
    }

    public Boolean save(ProductRecommendedDTO productRecommendedDTO) {
        List<Long> productIdList = productRecommendedDTO.getProductIdList();
        List<ProductRecommended> productRecommendedList = productMapToProductRecommended(productIdList).collect(Collectors.toList());
        this.saveOrUpdateBatch(productRecommendedList);
        productIdList.forEach(this::deleteProductRecommendedCache);
        return true;
    }

    public Boolean delete(Long productId) {
        return this.lambdaUpdate().eq(ProductRecommended::getProductId, productId).remove();
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(List<Long> idList) {
        idList.forEach(id -> {
            ProductRecommended productRecommended = this.getById(id);
            if (productRecommended != null) {
                deleteProductRecommendedCache(productRecommended.getProductId());
            }
        });
        return this.removeByIds(idList);
    }

    public Boolean getProductIsRecommended(Long productId) {
        return Optional.ofNullable(getProductRecommendedCache(productId)).orElseGet(() -> {
            Boolean flag = lambdaQuery()
                    .eq(ProductRecommended::getProductId, productId)
                    .eq(ProductRecommended::getIsEnable, Boolean.TRUE)
                    .oneOpt()
                    .map(productRecommended -> Boolean.TRUE)
                    .orElse(Boolean.FALSE);
            putProductRecommendedCache(productId, flag);
            return flag;
        });
    }
}

