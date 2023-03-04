package com.msb.mall.product.manager;

import com.msb.mall.comment.api.dubbo.CommentDubboService;
import com.msb.mall.comment.api.model.CommentDO;
import com.msb.mall.marketing.api.ActivityDubboService;
import com.msb.mall.marketing.api.model.ActivityProductDO;
import com.msb.mall.marketing.api.model.ProductSkuDO;
import com.msb.mall.product.model.entity.ProductSku;
import com.msb.mall.product.model.vo.app.ProductActivityVO;
import com.msb.mall.product.model.vo.app.ProductDetailVO;
import com.msb.mall.product.model.vo.app.ProductSkuCommentVO;
import com.msb.mall.product.model.vo.app.ProductSkuSimpleVO;
import com.msb.mall.product.service.ProductService;
import com.msb.mall.product.service.ProductSkuService;
import com.msb.mall.trade.api.dubbo.TradeOrderProductDubboService;
import com.msb.mall.trade.api.model.TradeOrderProductDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author liao
 */
@Slf4j
@Component
public class ProductManager {

    @Resource
    private ProductService productService;
    @Resource
    private ProductSkuService productSkuService;

    @DubboReference
    private ActivityDubboService activityDubboService;
    @DubboReference
    private TradeOrderProductDubboService tradeOrderProductDubboService;
    @DubboReference
    private CommentDubboService commentDubboService;

    /**
     * 获取商品详情页数据（带入秒杀信息）
     */
    public ProductDetailVO getProductDetailPage(Long productId) {
        ProductDetailVO productDetail = productService.getProductDetail(productId);
        ActivityProductDO activityProductDO = activityDubboService.getActivityProductDO(productDetail.getId());
        log.info("获取活动商品信息 activityProductDO {}", activityProductDO);
        if (!activityProductDO.getIsActivityProduct()) {
            return productDetail.setProductActivityVO(new ProductActivityVO().setIsStartActivity(Boolean.FALSE));
        }
        ProductActivityVO productActivityVO = new ProductActivityVO()
                .setIsStartActivity(activityProductDO.getIsInProgress())
                .setActivityStartTime(activityProductDO.getActivityStartTime())
                .setActivityEndTime(activityProductDO.getActivityEndTime())
                .setIsActivity(activityProductDO.getIsActivityProduct())
                .setCurrentTime(LocalDateTime.now())
                .setActivityTimeId(activityProductDO.getActivityTimeId())
                .setActivityId(activityProductDO.getActivityId());
        Optional<ProductSkuDO> activityProductSkuDO = activityProductDO.getActivityProductSkuListDO().stream().min(Comparator.comparing(ProductSkuDO::getPrice));
        activityProductSkuDO.ifPresent(activityProductSku -> productActivityVO.setActivityPrice(activityProductSku.getPrice()));

        return productDetail.setProductActivityVO(productActivityVO);
    }

    public List<ProductSkuSimpleVO> listProductSku(Long productId) {
        List<ProductSkuSimpleVO> productSkuSimpleListVO = productSkuService.listSimpleVo(productId);
        ActivityProductDO activityProductDO = activityDubboService.getActivityProductDO(productId);
        log.info("获取活动商品信息sku信息 activityProductDO {}", activityProductDO);
        if (activityProductDO.getIsActivityProduct() && activityProductDO.getIsInProgress()) {
            List<ProductSkuDO> activityProductSkuListDO = activityProductDO.getActivityProductSkuListDO();
            Map<Long, ProductSkuDO> activityProductSkuMap = activityProductSkuListDO.stream()
                    .collect(Collectors.toMap(ProductSkuDO::getProductSkuId, Function.identity()));

            return productSkuSimpleListVO.stream().map(productSkuSimpleVO ->
                    Optional.ofNullable(activityProductSkuMap.get(productSkuSimpleVO.getSkuId()))
                            .map(activityProductSkuDO -> productSkuSimpleVO
                                    .setStock(activityProductSkuDO.getStock())
                                    .setSellPrice(activityProductSkuDO.getPrice()))
                            .orElse(null)).filter(Objects::nonNull).collect(Collectors.toList());
        }
        return productSkuSimpleListVO;
    }

    /**
     * 根据商品id查询sku信息及评论数
     * @param productId 商品id
     * @return sku信息、评论数
     * 1、查询商品的所有sku
     * 2、查询商品的所有订单详情
     * 3、查询商品的所有评价
     * 4、筛选有评价的订单详情，并根据skuid进行分组计算count
     */
    public List<ProductSkuCommentVO> listSkuAndCommentCount(Long productId) {
        // 查询商品id的所有sku信息 sku_id
        List<ProductSku> skuList = productSkuService.list(productId);

        // 查询商品id的所有订单详情 order_product_id  skuId
        List<TradeOrderProductDO> orderProductList = tradeOrderProductDubboService.listTradeOrderByProductId(productId);
        // 查询商品的所有评价 order_product_id
        List<CommentDO> commentList = commentDubboService.listCommentByProductId(productId);
        Map<Long, CommentDO> commentMap = commentList
                .stream()
                .collect(Collectors.toMap(CommentDO::getOrderProductId, item -> item));

        // 遍历订单详情，提取有评论的订单详情并按照skuid分组
        Map<Long, List<TradeOrderProductDO>> skuMap = orderProductList
                .stream()
                .filter(item -> commentMap.containsKey(item.getId()))
                .collect(Collectors.groupingBy(TradeOrderProductDO::getProductSkuId));

        // 初始化一个List<ProductSkuCommentVO> count为0
        List<ProductSkuCommentVO> productSkuCommentVOList = new ArrayList<>();
        skuList.forEach(item -> {
            ProductSkuCommentVO productSkuCommentVO = new ProductSkuCommentVO()
                    .setSkuId(item.getId())
                    .setSkuName(item.getName())
                    .setAttributeSymbolList(item.getAttributeSymbolList())
                    .setCommentCount(0);
            if (skuMap.containsKey(item.getId())) {
                productSkuCommentVO.setCommentCount(skuMap.get(item.getId()).size());
            }
            productSkuCommentVOList.add(productSkuCommentVO);
        });

        return productSkuCommentVOList;
    }



}
