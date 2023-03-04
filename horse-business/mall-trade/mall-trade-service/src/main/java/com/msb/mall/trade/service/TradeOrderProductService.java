package com.msb.mall.trade.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.common.context.UserContext;
import com.msb.framework.common.model.PageDTO;
import com.msb.framework.common.utils.ListUtil;
import com.msb.framework.web.result.Assert;
import com.msb.framework.web.result.BizAssert;
import com.msb.mall.marketing.api.ActivityDubboService;
import com.msb.mall.marketing.api.model.ActivityProductSkuReturnStockDTO;
import com.msb.mall.product.api.dubbo.ProductDubboService;
import com.msb.mall.product.api.model.ProductSkuReturnStockDTO;
import com.msb.mall.trade.api.model.TradeOrderProductDO;
import com.msb.mall.trade.enums.ActivityTypeEnum;
import com.msb.mall.trade.api.enums.CommentStatusEnum;
import com.msb.mall.trade.enums.OrderProductDetailEnum;
import com.msb.mall.trade.exception.TradeExceptionCodeEnum;
import com.msb.mall.trade.mapper.TradeOrderProductMapper;
import com.msb.mall.trade.model.dto.app.OrderCommentStatusDTO;
import com.msb.mall.trade.model.entity.TradeOrderProduct;
import com.msb.mall.trade.model.vo.app.OrderProductVO;
import com.msb.mall.trade.service.convert.TradeOrderProductConvert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 订单详情(TradeOrderProduct)表服务实现类
 *
 * @author makejava
 * @since 2022-03-24 18:30:17
 */
@Slf4j
@Service("tradeOrderProductService")
public class TradeOrderProductService extends ServiceImpl<TradeOrderProductMapper, TradeOrderProduct> {

    @Resource
    private TradeOrderProductConvert tradeOrderProductConvert;
    @DubboReference
    private ProductDubboService productDubboService;
    @DubboReference
    private ActivityDubboService activityDubboService;

    /**
     * 根据ID获取订单商品，获取不到则抛出异常
     *
     * @param orderProductId：订单商品ID
     * @return com.msb.mall.trade.model.entity.TradeOrderProduct
     * @author peng.xy
     * @date 2022/4/9
     */
    public TradeOrderProduct getByIdOrThrow(Long orderProductId) {
        TradeOrderProduct orderProduct = super.getById(orderProductId);
        Assert.notNull(orderProduct, TradeExceptionCodeEnum.ORDER_PRODUCT_ERROR);
        return orderProduct;
    }

    /**
     * 根据ID集合，获取商品详情列表
     *
     * @param orderProductIds：ID集合
     * @return java.util.List<com.msb.mall.trade.model.entity.TradeOrderProduct>
     * @author peng.xy
     * @date 2022/4/11
     */
    public List<TradeOrderProduct> listByOrderProductIds(Collection<Long> orderProductIds) {
        return super.lambdaQuery().in(TradeOrderProduct::getId, orderProductIds).list();
    }

    /**
     * 根据ID集合，获取商品详情Map
     *
     * @param orderProductIds：ID集合
     * @return java.util.List<com.msb.mall.trade.model.entity.TradeOrderProduct>
     * @author peng.xy
     * @date 2022/4/11
     */
    public Map<Long, TradeOrderProduct> mapByOrderProductIds(Collection<Long> orderProductIds) {
        List<TradeOrderProduct> orderProductList = this.listByOrderProductIds(orderProductIds);
        if (CollectionUtils.isEmpty(orderProductList)) {
            return Collections.emptyMap();
        }
        return orderProductList.stream().collect(Collectors.toMap(TradeOrderProduct::getId, Function.identity()));
    }

    /**
     * 根据一个或多个订单ID，查询商品详情列表
     *
     * @param orderIds：订单ID列表
     * @return java.util.List<com.msb.mall.trade.model.entity.TradeOrderProduct>
     * @author peng.xy
     * @date 2022/3/29
     */
    public List<TradeOrderProduct> listByOrderIds(Long... orderIds) {
        return this.listByOrderIds(Arrays.asList(orderIds));
    }

    /**
     * 根据订单ID集合，查询商品详情列表
     *
     * @param orderIds：订单ID列表
     * @return java.util.List<com.msb.mall.trade.model.entity.TradeOrderProduct>
     * @author peng.xy
     * @date 2022/3/29
     */
    public List<TradeOrderProduct> listByOrderIds(Collection<Long> orderIds) {
        return super.lambdaQuery().in(TradeOrderProduct::getOrderId, orderIds).list();
    }

    /**
     * 根据一个或多个订单ID，分组查询商品详情列表
     *
     * @param orderIds：订单ID列表
     * @return java.util.Map<java.lang.Long, java.util.List < com.msb.mall.trade.model.entity.TradeOrderProduct>>
     * @author peng.xy
     * @date 2022/4/7
     */
    public Map<Long, List<TradeOrderProduct>> listMapByOrderIds(Long... orderIds) {
        return this.listMapByOrderIds(Arrays.asList(orderIds));
    }

    /**
     * 根据订单ID集合，分组查询商品详情列表Map
     *
     * @param orderIds：订单ID列表
     * @return java.util.List<com.msb.mall.trade.model.entity.TradeOrderProduct>
     * @author peng.xy
     * @date 2022/3/29
     */
    public Map<Long, List<TradeOrderProduct>> listMapByOrderIds(Collection<Long> orderIds) {
        List<TradeOrderProduct> orderProductList = this.listByOrderIds(orderIds);
        if (CollectionUtils.isEmpty(orderProductList)) {
            return Collections.emptyMap();
        }
        return orderProductList.stream().collect(Collectors.groupingBy(TradeOrderProduct::getOrderId));
    }

    /**
     * 比较当前订单商品详情状态
     *
     * @param orderProductId：订单商品ID
     * @param detailStatus：进行比较的状态
     * @return boolean
     * @author peng.xy
     * @date 2022/4/1
     */
    public boolean compareOrderDetailStatus(@Nonnull Long orderProductId, @Nonnull OrderProductDetailEnum... detailStatus) {
        Integer count = super.lambdaQuery().eq(TradeOrderProduct::getId, orderProductId)
                .in(TradeOrderProduct::getDetailStatus, ListUtil.convert(Arrays.asList(detailStatus), OrderProductDetailEnum::getCode))
                .count();
        return Objects.nonNull(count) && count > 0;
    }

    /**
     * 比较当前订单商品详情状态，数据有误则抛出异常
     *
     * @param orderProductId：订单商品ID
     * @param detailStatus：进行比较的状态
     * @return boolean
     * @author peng.xy
     * @date 2022/4/1
     */
    public void compareOrderDetailStatusOrThrow(@Nonnull Long orderProductId, @Nonnull OrderProductDetailEnum... detailStatus) {
        boolean compare = this.compareOrderDetailStatus(orderProductId, detailStatus);
        Assert.isTrue(compare, TradeExceptionCodeEnum.ORDER_PRODUCT_STATUS_EXCEPTION);
    }

    /**
     * 修改当前商品详情状态，与之前的状态进行比较
     *
     * @param orderProductId：订单商品ID
     * @param targetStatus：修改后的状态
     * @param currentStatus：支持修改的当前状态数组，
     * @return boolean
     * @author peng.xy
     * @date 2022/4/1
     */
    public boolean compareAndUpdateDetailStatus(@Nonnull Long orderProductId, @Nonnull OrderProductDetailEnum targetStatus, OrderProductDetailEnum... currentStatus) {
        LambdaUpdateChainWrapper<TradeOrderProduct> wrapper = super.lambdaUpdate().eq(TradeOrderProduct::getId, orderProductId);
        if (ArrayUtils.isNotEmpty(currentStatus)) {
            wrapper.in(TradeOrderProduct::getDetailStatus, ListUtil.convert(Arrays.asList(currentStatus), OrderProductDetailEnum::getCode));
        }
        return wrapper.set(TradeOrderProduct::getDetailStatus, targetStatus.getCode())
                .set(TradeOrderProduct::getUpdateTime, LocalDateTime.now())
                .set(UserContext.isLogin(), TradeOrderProduct::getUpdateUser, UserContext.getUserIdOrDefault())
                .update();
    }

    /**
     * 修改当前商品详情状态，与之前的状态进行比较，若详情状态有误则抛出异常
     *
     * @param orderProductId：订单商品ID
     * @param targetStatus：修改后的状态
     * @param currentStatus：支持修改的当前状态数组
     * @return boolean
     * @author peng.xy
     * @date 2022/4/1
     */
    public void compareAndUpdateDetailStatusOrThrow(@Nonnull Long orderProductId, @Nonnull OrderProductDetailEnum targetStatus, OrderProductDetailEnum... currentStatus) {
        boolean compare = this.compareAndUpdateDetailStatus(orderProductId, targetStatus, currentStatus);
        Assert.isTrue(compare, TradeExceptionCodeEnum.ORDER_PRODUCT_STATUS_EXCEPTION);
    }

    /**
     * 根据订单ID修改商品详情状态，若有误则抛出异常
     *
     * @param orderId：订单ID
     * @param targetStatus：修改后的状态
     * @return boolean
     * @author peng.xy
     * @date 2022/4/1
     */
    public void updateDetailStatusByOrderIdOrThrow(@Nonnull Long orderId, @Nonnull OrderProductDetailEnum targetStatus) {
        boolean update = super.lambdaUpdate().eq(TradeOrderProduct::getOrderId, orderId).set(TradeOrderProduct::getDetailStatus, targetStatus.getCode()).update();
        Assert.isTrue(update, TradeExceptionCodeEnum.ORDER_PRODUCT_STATUS_EXCEPTION);
    }

    /**
     * 根据订单ID和商品详情状态进行比较
     *
     * @param orderId：订单ID
     * @param currentStatus：比较的当前状态数组
     * @return boolean
     * @author peng.xy
     * @date 2022/5/7
     */
    public boolean compareDetailStatusByOrderId(@Nonnull Long orderId, @Nonnull OrderProductDetailEnum... currentStatus) {
        Integer count = super.lambdaQuery().eq(TradeOrderProduct::getOrderId, orderId)
                .in(TradeOrderProduct::getDetailStatus, ListUtil.convert(Arrays.asList(currentStatus), OrderProductDetailEnum::getCode))
                .count();
        return Objects.nonNull(count) && count > 0;
    }

    /**
     * 获取订单商品详情，并开启售后
     *
     * @param orderProductId：订单商品ID
     * @return com.msb.mall.trade.model.entity.TradeOrderProduct
     * @author peng.xy
     * @date 2022/4/11
     */
    @Transactional(readOnly = true)
    public TradeOrderProduct getOrderProductAndOpenAfterSale(@Nonnull Long orderProductId) {
        TradeOrderProduct tradeOrderProduct = this.getByIdOrThrow(orderProductId);
        this.compareAndUpdateDetailStatusOrThrow(orderProductId, OrderProductDetailEnum.AFTER_SALE, OrderProductDetailEnum.NORMAL, OrderProductDetailEnum.REFUND_FAIL);
        return tradeOrderProduct;
    }

    /**
     * 根据订单ID返还库存
     *
     * @param orderId：订单ID
     * @return boolean
     * @author peng.xy
     * @date 2022/5/16
     */
    public boolean returnStockByOrderId(Long orderId) {
        return this.returnStock(this.listByOrderIds(orderId));
    }

    /**
     * 根据订单商品ID数组返还库存
     *
     * @param orderProductIds：订单商品ID
     * @return boolean
     * @author peng.xy
     * @date 2022/5/16
     */
    public boolean returnStockByOrderProductIds(Long... orderProductIds) {
        return this.returnStock(super.lambdaQuery().in(TradeOrderProduct::getId, orderProductIds).list());
    }

    /**
     * 返还商品库存
     *
     * @param tradeOrderProducts：订单商品信息
     * @return boolean
     * @author peng.xy
     * @date 2022/5/16
     */
    private boolean returnStock(List<TradeOrderProduct> tradeOrderProducts) {
        try {
            // 返还商品库存DTO
            List<ProductSkuReturnStockDTO> returnStockDTOList = new LinkedList<>();
            // 缓存秒杀库存DTO
            List<ActivityProductSkuReturnStockDTO> seckillReturnStockDTOList = new LinkedList<>();
            for (TradeOrderProduct tradeOrderProduct : tradeOrderProducts) {
                returnStockDTOList.add(new ProductSkuReturnStockDTO().setSkuId(tradeOrderProduct.getProductSkuId()).setQuantity(tradeOrderProduct.getQuantity()));
                // 秒杀活动，则秒杀库存
                if (Objects.equals(tradeOrderProduct.getActivityType(), ActivityTypeEnum.SECKILL.getCode()) && Objects.nonNull(tradeOrderProduct.getActivityId())) {
                    ActivityProductSkuReturnStockDTO seckillReturnStockDTO = new ActivityProductSkuReturnStockDTO()
                            .setActivityProductSkuId(tradeOrderProduct.getActivityId())
                            .setQuantity(tradeOrderProduct.getQuantity());
                    seckillReturnStockDTOList.add(seckillReturnStockDTO);
                }
            }
            if (CollectionUtils.isNotEmpty(returnStockDTOList)) {
                log.info("返还商品库存：{}", returnStockDTOList);
                productDubboService.batchReturnStock(returnStockDTOList);
            }
            if (CollectionUtils.isNotEmpty(seckillReturnStockDTOList)) {
                log.info("返还秒杀库存：{}", seckillReturnStockDTOList);
                activityDubboService.batchReturnStock(seckillReturnStockDTOList);
            }
        } catch (Exception e) {
            log.error("返还商品库存失败，商品信息：{}", tradeOrderProducts, e);
            return false;
        }
        return true;
    }


    /**
     * 获取订单商品名称
     *
     * @param orderId：订单ID
     * @return java.lang.String
     * @author peng.xy
     * @date 2022/5/18
     */
    public String getOrderProductNames(Long orderId) {
        List<TradeOrderProduct> orderProductList = this.listByOrderIds(orderId);
        return orderProductList.stream().map(TradeOrderProduct::getProductName).collect(Collectors.joining("-"));
    }

    /**
     * 根据商品id查询订单详情信息
     * @param productId 商品id
     * @return 订单详情
     */
    public List<TradeOrderProductDO> listTradeOrderByProductId(Long productId) {
        List<TradeOrderProduct> list = this.lambdaQuery().eq(TradeOrderProduct::getProductId, productId).list();
        return tradeOrderProductConvert.toTradeOrderProductDO(list);
    }

    /**
     * 根据商品id集合查询订单详情信息
     * @param productIdList 商品id
     * @return 订单详情
     */
    public List<TradeOrderProductDO> listTradeOrderByProductId(List<Long> productIdList) {
        List<TradeOrderProduct> list = this.lambdaQuery().in(TradeOrderProduct::getProductId, productIdList).list();
        return tradeOrderProductConvert.toTradeOrderProductDO(list);
    }

    /**
     * 根据评论状态查询订单详情
     * @param orderCommentStatusDTO 评论状态
     * @return 订单详情
     */
    public IPage<OrderProductVO> listTradeOrderByCommentStatusByPage(OrderCommentStatusDTO orderCommentStatusDTO) {
        Long userId = UserContext.getUserId();
        Page<TradeOrderProduct> page = this.lambdaQuery()
                .eq(TradeOrderProduct::getUserId, userId)
                .eq(TradeOrderProduct::getCommentStatus, orderCommentStatusDTO.getCommentStatus())
                .page(orderCommentStatusDTO.page());
        return tradeOrderProductConvert.toOrderProductVOPage(page);
    }

    /**
     * 根据评论状态查询订单详情
     * @param commentStatusEnums 评论状态
     * @return 订单详情
     */
    public IPage<OrderProductVO> listTradeOrderByCommentStatus(PageDTO pageDTO, CommentStatusEnum... commentStatusEnums) {
        Long userId = UserContext.getUserId();
        Page<TradeOrderProduct> list = this.lambdaQuery()
                .eq(TradeOrderProduct::getUserId, userId)
                .in(TradeOrderProduct::getCommentStatus, ListUtil.convert(Arrays.asList(commentStatusEnums), CommentStatusEnum::getCode))
                .page(pageDTO.page());
        return tradeOrderProductConvert.toOrderProductVOPage(list);
    }

    /**
     * 更新订单详情的评价状态
     * @param orderId  订单id
     * @param commentStatus  评价状态
     * @return 更新结果
     */
    public Boolean updateCommentStatus(Long orderId, Integer commentStatus) {
        List<TradeOrderProduct> list = listByOrderIds(orderId);
        BizAssert.notNull(list, TradeExceptionCodeEnum.ORDER_STATUS_EXCEPTION);
        List<Long> orderProductIdList = list.stream().map(TradeOrderProduct::getId).collect(Collectors.toList());
        return this.lambdaUpdate()
                .in(TradeOrderProduct::getId, orderProductIdList)
                .set(TradeOrderProduct::getCommentStatus, commentStatus)
                .update();
    }

    /**
     * 更新订单详情状态
     * @param orderProductId 订单详情id
     * @param commentStatus 评论状态
     * @return 更新结果
     */
    public Boolean updateCommentStatusByOrderProductId(Long orderProductId, Integer commentStatus) {
        return this.lambdaUpdate().eq(TradeOrderProduct::getId, orderProductId).set(TradeOrderProduct::getCommentStatus, commentStatus).update();
    }
}

