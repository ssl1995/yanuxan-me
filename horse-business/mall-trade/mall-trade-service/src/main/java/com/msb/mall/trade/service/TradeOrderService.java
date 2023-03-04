package com.msb.mall.trade.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.cosid.generator.IdGenerator;
import com.msb.framework.common.constant.CommonConst;
import com.msb.framework.common.context.UserContext;
import com.msb.framework.common.exception.BizException;
import com.msb.framework.common.model.IDict;
import com.msb.framework.common.model.PageDTO;
import com.msb.framework.common.utils.EqualsUtil;
import com.msb.framework.common.utils.ListUtil;
import com.msb.framework.mysql.shard.ShardTableContext;
import com.msb.framework.web.result.Assert;
import com.msb.framework.web.result.BizAssert;
import com.msb.framework.web.util.ServletUtil;
import com.msb.mall.base.api.OrderConfigDubboService;
import com.msb.mall.base.api.ReceiveAddressDubboService;
import com.msb.mall.base.api.model.OrderConfigDO;
import com.msb.mall.base.api.model.ReceiveAddressDO;
import com.msb.mall.comment.api.dubbo.CommentDubboService;
import com.msb.mall.comment.api.model.CommentDO;
import com.msb.mall.comment.api.model.CommentDefaultDTO;
import com.msb.mall.product.api.dubbo.ProductDubboService;
import com.msb.mall.product.api.dubbo.ShoppingCartDubboService;
import com.msb.mall.product.api.model.ProductDO;
import com.msb.mall.product.api.model.ProductSkuDO;
import com.msb.mall.product.api.model.ShoppingCartDO;
import com.msb.mall.trade.api.enums.CommentStatusEnum;
import com.msb.mall.trade.api.model.OrderStatisticsByHourDO;
import com.msb.mall.trade.api.model.SalesStatisticsByHourDO;
import com.msb.mall.trade.config.PayCenterConfig;
import com.msb.mall.trade.enums.*;
import com.msb.mall.trade.exception.TradeExceptionCodeEnum;
import com.msb.mall.trade.mapper.TradeOrderMapper;
import com.msb.mall.trade.model.bo.CreateOrderProductBO;
import com.msb.mall.trade.model.dto.admin.*;
import com.msb.mall.trade.model.dto.app.*;
import com.msb.mall.trade.model.dto.pay.AuthPayDTO;
import com.msb.mall.trade.model.dto.pay.BasePayDTO;
import com.msb.mall.trade.model.dto.pay.ReturnPayDTO;
import com.msb.mall.trade.model.entity.*;
import com.msb.mall.trade.model.vo.admin.OrderDeliveryResultVO;
import com.msb.mall.trade.model.vo.admin.OrderDeliveryVO;
import com.msb.mall.trade.model.vo.admin.OrderInfoAdminVO;
import com.msb.mall.trade.model.vo.admin.OrderListAdminVO;
import com.msb.mall.trade.model.vo.app.*;
import com.msb.mall.trade.service.activity.OrderProductActivityHandle;
import com.msb.mall.trade.service.convert.TradeOrderConvert;
import com.msb.mall.trade.service.convert.TradeOrderLogConvert;
import com.msb.mall.trade.service.convert.TradeOrderLogisticsConvert;
import com.msb.mall.trade.service.convert.TradeOrderProductConvert;
import com.msb.pay.api.PayCenterDubboService;
import com.msb.pay.enums.PayCodeEnum;
import com.msb.pay.kit.SignKit;
import com.msb.pay.model.dto.ApplyRefundDTO;
import com.msb.pay.model.dto.PayNotifyDTO;
import com.msb.pay.model.dto.PrepaymentDTO;
import com.msb.pay.model.dto.UnifiedOrderDubboDTO;
import com.msb.pay.model.paydata.PayData;
import com.msb.pay.model.vo.ApplyRefundVO;
import com.msb.pay.model.vo.PrepaymentVO;
import com.msb.pay.model.vo.UnifiedOrderVO;
import com.msb.third.api.WxMpDubboService;
import com.msb.user.api.vo.UserDO;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 交易订单(TradeOrder)表服务实现类
 *
 * @author makejava
 * @since 2022-03-24 18:30:16
 */
@Slf4j
@Service("tradeOrderService")
public class TradeOrderService extends ServiceImpl<TradeOrderMapper, TradeOrder> {

    private static final BigDecimal ZERO = BigDecimal.ZERO;
    private static final BigDecimal ONE_HUNDRED = new BigDecimal(100);

    @Resource
    private TradeOrderConvert tradeOrderConvert;
    @Resource
    private TradeOrderProductConvert tradeOrderProductConvert;
    @Resource
    private TradeOrderLogisticsConvert tradeOrderLogisticsConvert;
    @Resource
    private TradeOrderLogConvert tradeOrderLogConvert;

    @Resource
    private PayCenterConfig payCenterConfig;
    @Resource
    private UserService userService;
    @Resource
    private AsyncService asyncService;
    @Resource
    private NotifyService notifyService;
    @Resource
    private TradeOrderProductService tradeOrderProductService;
    @Resource
    private TradeOrderLogisticsService tradeOrderLogisticsService;
    @Resource
    private TradeOrderLogService tradeOrderLogService;
    @Resource
    private TradeOrderPayCenterService tradeOrderPayCenterService;
    @Resource
    private RefundOrderPayCenterService refundOrderPayCenterService;
    @Resource
    private IdGenerator idGenerator;
    @Resource
    private TradeOrderMapper tradeOrderMapper;

    @DubboReference
    private ProductDubboService productDubboService;
    @DubboReference
    private ReceiveAddressDubboService receiveAddressDubboService;
    @DubboReference
    private ShoppingCartDubboService shoppingCartDubboService;
    @DubboReference
    private WxMpDubboService wxMpDubboService;
    @DubboReference
    private OrderConfigDubboService orderConfigDubboService;
    @DubboReference
    private PayCenterDubboService payCenterDubboService;
    @DubboReference
    private CommentDubboService commentDubboService;

    /**
     * 根据订单ID获取订单
     *
     * @param orderId：订单ID
     * @return com.msb.mall.trade.model.entity.TradeOrder
     * @author peng.xy
     * @date 2022/3/30
     */
    public TradeOrder getOrderById(@Nonnull Long orderId) {
        return this.getOrderByIdAndUserId(orderId, null);
    }

    /**
     * 根据订单ID获取订单，数据有误则抛出异常
     *
     * @param orderId：订单ID
     * @return com.msb.mall.trade.model.entity.TradeOrder
     * @author peng.xy
     * @date 2022/3/30
     */
    public TradeOrder getOrderByIdOrThrow(@Nonnull Long orderId) {
        TradeOrder tradeOrder = this.getOrderById(orderId);
        Assert.notNull(tradeOrder, TradeExceptionCodeEnum.ORDER_EXCEPTION);
        Assert.notTrue(tradeOrder.getIsDeleted(), TradeExceptionCodeEnum.ORDER_EXCEPTION);
        Assert.notTrue(tradeOrder.getIsDisabled(), TradeExceptionCodeEnum.ORDER_EXCEPTION);
        return tradeOrder;
    }

    /**
     * 根据订单ID，用户ID获取订单
     *
     * @param orderId：订单ID
     * @param userId：可选
     * @return com.msb.mall.trade.model.entity.TradeOrder
     * @author peng.xy
     * @date 2022/3/30
     */
    public TradeOrder getOrderByIdAndUserId(@Nonnull Long orderId, Long userId) {
        ShardTableContext.set(TradeOrder.class, orderId);
        TradeOrder tradeOrder = super.lambdaQuery()
                .eq(TradeOrder::getId, orderId)
                .eq(Objects.nonNull(userId), TradeOrder::getUserId, userId)
                .one();
        log.info("查询订单信息，订单ID：{}, 用户ID：{}，订单数据：{}", orderId, userId, tradeOrder);
        return tradeOrder;
    }

    /**
     * 根据订单ID，用户ID获取订单，数据有误则抛出异常
     *
     * @param orderId：订单ID
     * @param userId：可选
     * @return com.msb.mall.trade.model.entity.TradeOrder
     * @author peng.xy
     * @date 2022/3/30
     */
    public TradeOrder getOrderByIdAndUserIdOrThrow(@Nonnull Long orderId, Long userId) {
        TradeOrder tradeOrder = this.getOrderByIdAndUserId(orderId, userId);
        Assert.notNull(tradeOrder, TradeExceptionCodeEnum.ORDER_EXCEPTION);
        Assert.notTrue(tradeOrder.getIsDeleted(), TradeExceptionCodeEnum.ORDER_EXCEPTION);
        Assert.notTrue(tradeOrder.getIsDisabled(), TradeExceptionCodeEnum.ORDER_EXCEPTION);
        return tradeOrder;
    }

    /**
     * 延迟获取订单信息，最多等待两秒
     *
     * @param orderId：订单ID
     * @param userId：可选
     * @return com.msb.mall.trade.model.entity.TradeOrder
     * @author peng.xy
     * @date 2022/6/10
     */
    @SneakyThrows
    public TradeOrder getOrderByIdAndUserIdDelay(@Nonnull Long orderId, Long userId) {
        // 循环10次等待提交订单的异步线程执行完毕
        for (int i = 1; i <= 10; i++) {
            TradeOrder tradeOrder = this.getOrderByIdAndUserId(orderId, userId);
            Assert.notNull(tradeOrder, TradeExceptionCodeEnum.ORDER_EXCEPTION);
            Assert.notTrue(tradeOrder.getIsDeleted(), TradeExceptionCodeEnum.ORDER_EXCEPTION);
            if (tradeOrder.getIsDisabled()) {
                log.info("订单异步线程未处理完成，订单ID：{}，循环次数：{}", orderId, i);
                Thread.sleep(200);
                continue;
            }
            return tradeOrder;
        }
        throw new BizException(TradeExceptionCodeEnum.ORDER_EXCEPTION);
    }

    /**
     * 比较当前订单状态
     *
     * @param orderId：订单ID
     * @param orderStatus：进行比较的状态
     * @return boolean
     * @author peng.xy
     * @date 2022/4/1
     */
    public boolean compareOrderStatus(@Nonnull Long orderId, @Nonnull OrderStatusEnum... orderStatus) {
        Integer count = super.lambdaQuery().eq(TradeOrder::getId, orderId)
                .in(TradeOrder::getOrderStatus, ListUtil.convert(Arrays.asList(orderStatus), OrderStatusEnum::getCode)).count();
        return Objects.nonNull(count) && count > 0;
    }

    /**
     * 比较当前订单状态，数据有误则抛出异常
     *
     * @param orderId：订单ID
     * @param orderStatus：进行比较的状态
     * @author peng.xy
     * @date 2022/4/1
     */
    public void compareOrderStatusOrThrow(@Nonnull Long orderId, @Nonnull OrderStatusEnum... orderStatus) {
        boolean compare = this.compareOrderStatus(orderId, orderStatus);
        Assert.isTrue(compare, TradeExceptionCodeEnum.ORDER_STATUS_EXCEPTION);
    }

    /**
     * 修改订单状态，但需要对订单之前的状态进行比较
     *
     * @param orderId：订单ID
     * @param targetStatus：修改后的状态
     * @param currentStatus：支持修改的当前状态数组。可选
     * @return boolean
     * @author peng.xy
     * @date 2022/3/30
     */
    public boolean compareAndUpdateOrderStatus(@Nonnull Long orderId, @Nonnull OrderStatusEnum targetStatus, OrderStatusEnum... currentStatus) {
        LambdaUpdateChainWrapper<TradeOrder> wrapper = super.lambdaUpdate().eq(TradeOrder::getId, orderId);
        if (ArrayUtils.isNotEmpty(currentStatus)) {
            wrapper.in(TradeOrder::getOrderStatus, ListUtil.convert(Arrays.asList(currentStatus), OrderStatusEnum::getCode));
        }
        ShardTableContext.set(TradeOrder.class, orderId);
        return wrapper.set(TradeOrder::getOrderStatus, targetStatus.getCode())
                .set(TradeOrder::getUpdateTime, LocalDateTime.now())
                .set(UserContext.isLogin(), TradeOrder::getUpdateUser, UserContext.getUserIdOrDefault())
                .update();
    }

    /**
     * 修改订单状态，但需要对订单之前的状态进行比较，若订单状态有误则抛出异常
     *
     * @param orderId：订单ID
     * @param targetStatus：修改后的状态
     * @param currentStatus：比较的状态
     * @author peng.xy
     * @date 2022/4/7
     */
    public void compareAndUpdateOrderStatusOrThrow(@Nonnull Long orderId, @Nonnull OrderStatusEnum targetStatus, OrderStatusEnum... currentStatus) {
        boolean update = this.compareAndUpdateOrderStatus(orderId, targetStatus, currentStatus);
        Assert.isTrue(update, TradeExceptionCodeEnum.ORDER_STATUS_EXCEPTION);
    }

    /**
     * 提交订单
     *
     * @param orderSubmitDTO：提交订单参数
     * @return Boolean
     * @author peng.xy
     * @date 2022/3/25
     */
//    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(rollbackFor = Exception.class)
    public OrderSubmitVO submitOrder(@Nonnull OrderSubmitDTO orderSubmitDTO) {
        Long userId = UserContext.getUserId();
        // 创建订单商品信息
        CreateOrderProductBO createOrderProductBO = this.createOrderProducts(orderSubmitDTO.getProducts(), orderSubmitDTO.getRecipientAddressId(), orderSubmitDTO.getIsVirtual(), true);
        List<TradeOrderProduct> orderProductList = createOrderProductBO.getProductList();
        // 创建订单主体，不包括订单金额
        TradeOrder tradeOrder = this.createTradeOrderBody(orderSubmitDTO, userId);
        // 处理订单金额
        this.handleOrderAmount(tradeOrder, orderProductList, createOrderProductBO.getRemoteAreaPostage());
        // 处理订单状态
        tradeOrder.setOrderType(createOrderProductBO.getOrderType().getCode());
        this.handleOrderStatus(tradeOrder);
        // 保存订单信息
        Assert.isTrue(super.save(tradeOrder), TradeExceptionCodeEnum.ORDER_SAVE_FAIL);
        // 扣减商品库存
        productDubboService.checkAndReduceStock(tradeOrderProductConvert.toProductSkuOccupyDTOList(orderProductList));
        // 返回下单数据
        OrderSubmitVO orderSubmitVO = tradeOrderConvert.toOrderSubmitVO(tradeOrder);
        orderSubmitVO.setProducts(tradeOrderProductConvert.toAdvanceOrderProductVOList(orderProductList));
        orderSubmitVO.setServerTime(LocalDateTime.now());
        // 提交订单异步保存信息
        asyncService.submitOrderAsync(tradeOrder, orderProductList, createOrderProductBO.getReceiveAddressDO(), orderSubmitDTO.getShoppingCartIds());
        log.info("提交订单结果：{}", orderSubmitVO);
        return orderSubmitVO;
    }


    /**
     * 获取立即购买预订单，数据库中不保存订单数据
     *
     * @param advanceOrderDTO：立即购买预订单参数
     * @return com.msb.mall.trade.model.vo.app.AdvanceOrderVO
     * @author peng.xy
     * @date 2022/3/30
     */
    public AdvanceOrderVO advanceOrderForBuyNow(AdvanceOrderDTO advanceOrderDTO) {
        return this.getAdvanceOrderVO(Collections.singletonList(advanceOrderDTO), advanceOrderDTO.getRecipientAddressId(), advanceOrderDTO.getIsVirtual());
    }

    /**
     * 获取购物车预订单，数据库中不保存订单数据
     *
     * @param cartIds：购物车ids
     * @param recipientAddressId：收货地址ID
     * @param isVirtual：是否为虚拟商品订单
     * @return com.msb.mall.trade.model.vo.app.AdvanceOrderVO
     * @author peng.xy
     * @date 2022/3/30
     */
    public AdvanceOrderVO advanceOrderForCart(Long[] cartIds, Long recipientAddressId, boolean isVirtual) {
        Assert.isTrue(ArrayUtils.isNotEmpty(cartIds), TradeExceptionCodeEnum.SHOPPING_CART_ERROR);
        // 根据购物车ID查询购物车信息
        List<ShoppingCartDO> shoppingCarts = shoppingCartDubboService.listShoppingCartByIds(Arrays.asList(cartIds));
        List<OrderSubmitProductDTO> productDTOList = tradeOrderConvert.toShoppingCartProductDTOList(shoppingCarts);
        return this.getAdvanceOrderVO(productDTOList, recipientAddressId, isVirtual);
    }

    /**
     * 获取预订单
     *
     * @param recipientAddressId：收货地址ID
     * @param productListDTOList：生成商品业务对象
     * @param isVirtual：是否为虚拟商品订单
     * @return com.msb.mall.trade.model.vo.app.AdvanceOrderVO
     * @author peng.xy
     * @date 2022/4/7
     */
    private AdvanceOrderVO getAdvanceOrderVO(List<OrderSubmitProductDTO> productListDTOList, Long recipientAddressId, boolean isVirtual) {
        // 创建空的订单
        TradeOrder tradeOrder = new TradeOrder();
        // 生成订单商品
        CreateOrderProductBO createOrderProductBO = this.createOrderProducts(productListDTOList, recipientAddressId, isVirtual, false);
        List<TradeOrderProduct> orderProductList = createOrderProductBO.getProductList();
        BigDecimal remoteAreaPostage = createOrderProductBO.getRemoteAreaPostage();
        // 处理订单金额
        this.handleOrderAmount(tradeOrder, orderProductList, remoteAreaPostage);
        // 返回视图
        AdvanceOrderVO advanceOrderVO = tradeOrderConvert.toAdvanceOrderVO(tradeOrder);
        advanceOrderVO.setProducts(tradeOrderProductConvert.toAdvanceOrderProductVOList(orderProductList));
        // 计算实际商品金额（付款金额 - 运费）
        advanceOrderVO.setProductAmount(advanceOrderVO.getPayAmount().subtract(remoteAreaPostage));
        // 查询订单配置信息
        OrderConfigDO orderConfig = orderConfigDubboService.getOrderConfig();
        // 获取支付自动失效时间（分钟）
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireTime = now.plusMinutes(orderConfig.getOrderPayExpire());
        advanceOrderVO.setServerTime(now);
        advanceOrderVO.setExpireTime(expireTime);
        log.info("生成预订单：{}", advanceOrderVO);
        return advanceOrderVO;
    }

    /**
     * 生成订单编号
     *
     * @return java.lang.String
     * @author peng.xy
     * @date 2022/3/29
     */
    private String generateOrderNo(Long userId) {
        return "Y" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) + userId + RandomStringUtils.randomNumeric(6);
    }

    /**
     * 生成支付单编号
     *
     * @return java.lang.String
     * @author peng.xy
     * @date 2022/3/29
     */
    private String generatePayOrderNo() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) + RandomStringUtils.randomNumeric(5);
    }

    /**
     * 创建订单主体，但不包括订单金额
     *
     * @param orderSubmitDTO：创建订单参数
     * @param userId：用户ID
     * @author peng.xy
     * @date 2022/3/28
     */
    private TradeOrder createTradeOrderBody(@Nonnull OrderSubmitDTO orderSubmitDTO, Long userId) {
        // 生成订单编号
        String orderNo = this.generateOrderNo(userId);
        // 查询订单配置信息
        OrderConfigDO orderConfig = orderConfigDubboService.getOrderConfig();
        // 获取支付自动失效时间（分钟）
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireTime = now.plusMinutes(orderConfig.getOrderPayExpire());
        TradeOrder tradeOrder = new TradeOrder()
                .setId(idGenerator.getLongCosId("trade_order"))
                .setOrderNo(orderNo)                                // 订单号
                .setUserId(userId)                                  // 订单用户ID
                .setRefundAmount(BigDecimal.ZERO)                   // 已退款金额，默认为0
                .setSubmitTime(now)                                 // 下单时间
                .setExpireTime(expireTime)                          // 自动失效时间
                .setUserMessage(orderSubmitDTO.getUserMessage())    // 用户留言
                .setOrderSource(orderSubmitDTO.getOrderSource())    // 订单来源（1：未知来源，2：安卓端APP，3：IOS端APP，4：H5浏览器，5：微信浏览器，6：PC浏览器）
                .setOrderType(OrderTypeEnum.NORMAL.getCode())       // 订单类型（1：普通订单，2：免费订单，3：秒杀订单）
                .setOrderStatus(OrderStatusEnum.UNPAID.getCode())   // 订单状态（1：待支付，2：已关闭，3：已支付，4：已发货，5：已收货，6：已完成）
                .setPayType(OrderPayTypeEnum.UNPAID.getCode())      // 支付方式（1：未支付，2：微信支付，3：支付宝支付）
                .setIsPackageFree(CommonConst.NO)                   // 是否包邮（0：不包邮，1：包邮）
                .setIsAfterSale(CommonConst.NO)                     // 是否开启售后（0：未开启售后，1：已开启售后）
                .setIsDeleted(CommonConst.NO)                       // 是否删除（0：未删除，1：已删除）
                .setIsDisabled(CommonConst.YES);                    // 是否禁用（0：启用，1：禁用）
        log.info("创建订单主体：{}", tradeOrder);
        return tradeOrder;
    }

    /**
     * 创建订单商品信息
     *
     * @param productsDTOList：提交订单商品业务对象
     * @param recipientAddressId：收货地址ID
     * @param isVirtual：是否为虚拟商品订单
     * @param isReduceStock：创建后是否扣减库存
     * @return com.msb.mall.trade.model.bo.app.OrderProductBO
     * @author peng.xy
     * @date 2022/4/8
     */
    private CreateOrderProductBO createOrderProducts(@Nonnull List<OrderSubmitProductDTO> productsDTOList, Long recipientAddressId, boolean isVirtual, boolean isReduceStock) {
        Assert.notEmpty(productsDTOList, TradeExceptionCodeEnum.ORDER_PRODUCT_ERROR);
        // 获取订单商品信息
        List<Long> productSkuIds = ListUtil.convertDistinct(productsDTOList, OrderSubmitProductDTO::getProductSkuId);
        List<ProductSkuDO> productSkus = productDubboService.listProductSku(productSkuIds);
        Assert.notEmpty(productSkus, TradeExceptionCodeEnum.ORDER_PRODUCT_ERROR);
        Map<Long, ProductSkuDO> productMap = productSkus.stream().collect(Collectors.toMap(ProductSkuDO::getSkuId, Function.identity()));
        Integer activityType = productsDTOList.stream().findFirst().get().getActivityType();
        // 最高的商品运费
        BigDecimal maxRemoteAreaPostage = ZERO;
        // 订单类型枚举，非虚拟商品订单则默认为普通订单
        OrderTypeEnum orderType = isVirtual ? OrderTypeEnum.VIRTUAL : OrderTypeEnum.NORMAL;
        List<TradeOrderProduct> orderProductList = new ArrayList<>(productsDTOList.size());
        for (OrderSubmitProductDTO productDTO : productsDTOList) {
            // 根据SKU-ID获取商品信息
            ProductSkuDO productSku = productMap.get(productDTO.getProductSkuId());
            Assert.notNull(productSku, TradeExceptionCodeEnum.ORDER_PRODUCT_ERROR);
            if (!Objects.equals(activityType, productDTO.getActivityType())) {
                throw new BizException(productSku.getProductName() + "商品活动类型不一致");
            }
            if (isVirtual && productSku.getProductType() != 2) {
                throw new BizException(productSku.getProductName() + "非虚拟商品");
            }
            // 偏远地区运费
            BigDecimal remoteAreaPostage = productSku.getRemoteAreaPostage();
            if (Objects.nonNull(remoteAreaPostage) && remoteAreaPostage.compareTo(maxRemoteAreaPostage) > 0) {
                maxRemoteAreaPostage = remoteAreaPostage;
            }
            // 根据活动类型，处理订单商品信息
            OrderProductActivityHandle activityHandle = ActivityTypeEnum.getOrderProductActivityHandle(productDTO.getActivityType());
            TradeOrderProduct orderProduct = activityHandle.createOrderProduct(productDTO, productSku, isReduceStock);
            orderProductList.add(orderProduct);
            // 获取对应的订单类型
            OrderTypeEnum handleOrderType = activityHandle.getOrderType();
            if (!Objects.equals(OrderTypeEnum.NORMAL, handleOrderType)) {
                orderType = handleOrderType;
            }
        }
        // 非虚拟商品订单，则获取收货地址
        ReceiveAddressDO receiveAddressDO = null;
        BigDecimal remoteAreaPostage = ZERO;
        if (!isVirtual) {
            // 获取收件人信息
            if (Objects.nonNull(recipientAddressId)) {
                // 根据收货地址ID获取
                receiveAddressDO = receiveAddressDubboService.getReceiveAddressById(recipientAddressId);
                Assert.notNull(receiveAddressDO, TradeExceptionCodeEnum.RECIPIENT_ADDRESS_ERROR);
                // 偏远地区取运费，非偏远地区则包邮
                remoteAreaPostage = receiveAddressDO.getIsRemoteArea() ? maxRemoteAreaPostage : ZERO;
            } else {
                // 获取默认的收货地址
                receiveAddressDO = receiveAddressDubboService.getDefaultReceiveAddress(UserContext.getUserId());
                if (Objects.nonNull(receiveAddressDO)) {
                    remoteAreaPostage = receiveAddressDO.getIsRemoteArea() ? maxRemoteAreaPostage : ZERO;
                }
            }
        }
        log.info("创建订单商品，收货地址信息：{}，运费：{}", receiveAddressDO, remoteAreaPostage);
        log.info("创建订单商品，商品信息列表：{}", orderProductList);
        return new CreateOrderProductBO(receiveAddressDO, remoteAreaPostage, orderType, orderProductList);
    }

    /**
     * 处理订单金额
     *
     * @param tradeOrder：订单主体
     * @param orderProductList：订单商品信息
     * @param shippingAmount：运费
     * @author peng.xy
     * @date 2022/3/29
     */
    private void handleOrderAmount(@Nonnull TradeOrder tradeOrder, @Nonnull List<TradeOrderProduct> orderProductList, @Nonnull BigDecimal shippingAmount) {
        Assert.notEmpty(orderProductList, TradeExceptionCodeEnum.ORDER_PRODUCT_ERROR);
        BigDecimal totalAmount = ZERO, // 订单总金额
                payAmount = ZERO,      // 实付金额
                discountAmount;        // 优惠金额
        // 计算金额
        for (TradeOrderProduct orderProduct : orderProductList) {
            // 原始价格
            BigDecimal productPrice = orderProduct.getProductPrice();
            // 商品数量
            Integer quantity = orderProduct.getQuantity();
            // 原始金额 = 原始价格 * 商品数量
            BigDecimal originalAmount = productPrice.multiply(new BigDecimal(quantity));
            // 总金额累加
            totalAmount = totalAmount.add(originalAmount);
            // 实付金额累加
            payAmount = payAmount.add(orderProduct.getRealAmount());
        }
        // 优惠金额 = 总金额 - 实付金额
        discountAmount = totalAmount.subtract(payAmount);
        // 增加运费
        totalAmount = totalAmount.add(shippingAmount);
        payAmount = payAmount.add(shippingAmount);
        // 运费金额为0，则为包邮
        if (shippingAmount.compareTo(ZERO) == 0) {
            tradeOrder.setIsPackageFree(CommonConst.YES);
        }
        // 保存订单金额
        tradeOrder.setTotalAmount(totalAmount);
        tradeOrder.setShippingAmount(shippingAmount);
        tradeOrder.setDiscountAmount(discountAmount);
        tradeOrder.setPayAmount(payAmount);
        log.info("处理订单金额：{}", tradeOrder);
    }

    /**
     * 处理订单状态
     *
     * @param tradeOrder：交易订单
     * @author peng.xy
     * @date 2022/3/29
     */
    private void handleOrderStatus(@Nonnull TradeOrder tradeOrder) {
        BigDecimal payAmount = tradeOrder.getPayAmount();
        int compare = payAmount.compareTo(ZERO);
        // 价格为零，免费订单，无需支付
        if (compare == 0) {
            // 订单类型为免费订单
            tradeOrder.setOrderType(OrderTypeEnum.FREE.getCode());
            // 订单状态为待发货
            tradeOrder.setOrderStatus(OrderStatusEnum.PAID.getCode());
        }
        // 价格大于零，走支付逻辑
        else if (compare > 0) {
            tradeOrder.setOrderStatus(OrderStatusEnum.UNPAID.getCode());
        }
        // 其它异常情况，抛出订单金额异常
        else {
            throw new BizException(TradeExceptionCodeEnum.ORDER_AMOUNT_EXCEPTION);
        }
        log.info("处理订单状态：{}", tradeOrder);
    }

    /**
     * 查询APP订单分页列表
     *
     * @param orderQueryAppDTO：列表参数
     * @return Page<com.msb.mall.trade.model.vo.app.OrderAppListVO>
     * @author peng.xy
     * @date 2022/3/29
     */
    @Transactional(readOnly = true)
    public Page<OrderListAppVO> pageOrderByApp(OrderQueryAppDTO orderQueryAppDTO) {
        Long userId = UserContext.getUserId();
        List<Integer> orderStatus = orderQueryAppDTO.getOrderStatus();
        // 查询用户订单列表
        Page<TradeOrder> entityPage = super.lambdaQuery()
                .eq(TradeOrder::getIsDisabled, CommonConst.NO)
                .eq(TradeOrder::getIsDeleted, CommonConst.NO)
                .eq(TradeOrder::getUserId, userId)
                .in(!orderStatus.isEmpty(), TradeOrder::getOrderStatus, orderStatus)
                .orderByDesc(TradeOrder::getSubmitTime)
                .page(orderQueryAppDTO.page());
        Page<OrderListAppVO> voPage = tradeOrderConvert.toOrderListAppVOPage(entityPage);
        List<OrderListAppVO> voList = voPage.getRecords();
        if (CollectionUtils.isEmpty(voList)) {
            return voPage;
        }
        // 查询订单所有商品详情
        List<Long> orderIds = ListUtil.convertDistinct(voList, OrderListAppVO::getOrderId);
        Map<Long, List<TradeOrderProduct>> productsListMap = tradeOrderProductService.listMapByOrderIds(orderIds);
        // 视图转换
        return voPage.setRecords(voList.stream().map(orderListAppVO -> {
            List<TradeOrderProduct> orderProductList = productsListMap.get(orderListAppVO.getOrderId());
            orderListAppVO.setProducts(tradeOrderProductConvert.toOrderProductVOList(orderProductList));
            return orderListAppVO;
        }).collect(Collectors.toList()));
    }

    /**
     * 查询后管订单分页列表
     *
     * @param orderQueryAdminDTO：列表参数
     * @return Page<com.msb.mall.trade.model.vo.admin.OrderAdminListVO>
     * @author peng.xy
     * @date 2022/3/31
     */
    @Transactional(readOnly = true)
    public Page<OrderListAdminVO> pageOrderByAdmin(OrderQueryAdminDTO orderQueryAdminDTO) {
        List<Long> queryUserIds = userService.getQueryUserIdsOrDefault(orderQueryAdminDTO.getUserPhone());
        List<Integer> orderStatus = orderQueryAdminDTO.getOrderStatus();
        String orderNo = orderQueryAdminDTO.getOrderNo();
        // 查询后管订单列表
        Page<TradeOrder> entityPage = super.lambdaQuery()
                .eq(TradeOrder::getIsDeleted, CommonConst.NO)
                .eq(Objects.nonNull(orderQueryAdminDTO.getOrderSource()), TradeOrder::getOrderSource, orderQueryAdminDTO.getOrderSource())
                .like(Objects.nonNull(orderNo), TradeOrder::getOrderNo, orderNo)
                .eq(Objects.nonNull(orderQueryAdminDTO.getUserId()), TradeOrder::getUserId, orderQueryAdminDTO.getUserId())
                .ge(Objects.nonNull(orderQueryAdminDTO.getStartTime()), TradeOrder::getSubmitTime, orderQueryAdminDTO.getStartTime())
                .le(Objects.nonNull(orderQueryAdminDTO.getEndTime()), TradeOrder::getSubmitTime, orderQueryAdminDTO.getEndTime())
                .in(CollectionUtils.isNotEmpty(orderStatus), TradeOrder::getOrderStatus, orderStatus)
                .in(CollectionUtils.isNotEmpty(queryUserIds), TradeOrder::getUserId, queryUserIds)
                .orderByDesc(TradeOrder::getSubmitTime)
                .page(orderQueryAdminDTO.page());
        Page<OrderListAdminVO> voPage = tradeOrderConvert.toOrderListAdminVOPage(entityPage);
        List<OrderListAdminVO> voList = voPage.getRecords();
        if (CollectionUtils.isEmpty(voList)) {
            return voPage;
        }
        // 查询所有用户信息
        List<Long> userIds = ListUtil.convertDistinct(voList, OrderListAdminVO::getUserId);
        Map<Long, UserDO> userMapVO = userService.mapUserByIdsOrEmpty(userIds);
        // 转换视图对象
        return voPage.setRecords(voList.stream().map(orderListAdminVO -> {
            // 获取用户信息
            UserDO userVO = userMapVO.get(orderListAdminVO.getUserId());
            if (Objects.nonNull(userVO)) {
                orderListAdminVO.setUserPhone(userVO.getPhone());
            }
            return orderListAdminVO;
        }).collect(Collectors.toList()));
    }

    /**
     * 查询APP待收货订单物流信息列表
     *
     * @return java.util.List<com.msb.mall.trade.model.vo.app.OrderLogisticsListVO>
     * @author peng.xy
     * @date 2022/5/11
     */
    @Transactional(readOnly = true)
    public Page<OrderLogisticsListVO> listReceiveOrder(PageDTO pageDTO) {
        Page<TradeOrder> entityPage = super.lambdaQuery()
                .eq(TradeOrder::getIsDisabled, CommonConst.NO)
                .eq(TradeOrder::getIsDeleted, CommonConst.NO)
                .eq(TradeOrder::getUserId, UserContext.getUserId())
                .eq(TradeOrder::getOrderStatus, OrderStatusEnum.DELIVERED.getCode())
                .orderByDesc(TradeOrder::getSubmitTime)
                .page(pageDTO.page());
        Page<OrderLogisticsListVO> voPage = tradeOrderConvert.toOrderLogisticsListVOPage(entityPage);
        List<OrderLogisticsListVO> voList = voPage.getRecords();
        if (CollectionUtils.isEmpty(voList)) {
            return voPage;
        }
        List<Long> orderIds = ListUtil.convertDistinct(voList, OrderLogisticsListVO::getOrderId);
        // 获取订单商品信息
        Map<Long, List<TradeOrderProduct>> orderProductListMap = tradeOrderProductService.listMapByOrderIds(orderIds);
        // 获取订单物流信息
        Map<Long, TradeOrderLogistics> orderLogisticsMap = tradeOrderLogisticsService.mapByOrderIds(orderIds);
        // 转换视图对象
        return voPage.setRecords(voList.stream().map(orderLogisticsListVO -> {
            Long orderId = orderLogisticsListVO.getOrderId();
            // 解析商品信息
            List<TradeOrderProduct> orderProductList = orderProductListMap.get(orderId);
            orderLogisticsListVO.setProducts(tradeOrderProductConvert.toOrderProductVOList(orderProductList));
            // 解析物流信息
            TradeOrderLogistics orderLogistics = orderLogisticsMap.get(orderId);
            if (Objects.nonNull(orderLogistics)) {
                OrderLogisticsVO logistics = tradeOrderLogisticsConvert.toOrderLogisticsVO(orderLogistics);
                logistics.setLogisticsDataList(tradeOrderLogisticsService.parseData(orderLogistics.getLogisticsApi(), orderLogistics.getLogisticsData()));
                orderLogisticsListVO.setLogistics(logistics);
            }
            return orderLogisticsListVO;
        }).collect(Collectors.toList()));
    }

    /**
     * 查询APP订单详情
     *
     * @param orderId：订单ID
     * @return com.msb.mall.trade.model.vo.app.OrderAppInfoVO
     * @author peng.xy
     * @date 2022/3/30
     */
    @Transactional(readOnly = true)
    public OrderInfoAppVO getOrderInfoByApp(@Nonnull Long orderId) {
        Long userId = UserContext.getUserId();
        // 校验用户订单
        TradeOrder tradeOrder = this.getOrderByIdAndUserIdDelay(orderId, userId);
        // 查询商品详情
        List<TradeOrderProduct> orderProductList = tradeOrderProductService.listByOrderIds(orderId);
        // 获取是否支持售后
        List<OrderProductAfterSaleVO> orderProductVOList = this.getOrderProductAfterSaleVOList(tradeOrder, orderProductList);
        // 查询物流信息
        TradeOrderLogistics orderLogistics = tradeOrderLogisticsService.getByOrderIdOrEmpty(orderId);
        OrderLogisticsVO logistics = tradeOrderLogisticsConvert.toOrderLogisticsVO(orderLogistics);
        logistics.setLogisticsDataList(tradeOrderLogisticsService.parseData(orderLogistics.getLogisticsApi(), orderLogistics.getLogisticsData()));
        // 查询日志信息
        List<TradeOrderLog> tradeOrderLogList = tradeOrderLogService.listByOrderIdAndTypes(orderId);
        // 转换视图对象
        OrderInfoAppVO orderInfoAppVO = tradeOrderConvert.toAppInfoVO(tradeOrder);
        orderInfoAppVO.setOrderLogs(tradeOrderLogConvert.toOrderLogVOList(tradeOrderLogList));
        orderInfoAppVO.setProducts(orderProductVOList);
        orderInfoAppVO.setLogistics(logistics);
        // 返回服务器时间
        orderInfoAppVO.setServerTime(LocalDateTime.now());
        // 返回支付时间
        orderInfoAppVO.setPayTime(tradeOrderLogService.getLogTime(tradeOrderLogList, OrderOperationLogTypeEnum.PAY_ORDER));
        log.info("查询APP订单详情，订单ID：{}，数据：{}", orderId, orderInfoAppVO);
        return orderInfoAppVO;
    }

    /**
     * 查询订单支付是否成功
     *
     * @param orderId：订单ID
     * @return com.msb.mall.trade.model.vo.app.OrderPayResultVO
     * @author peng.xy
     * @date 2022/4/27
     */
    @Transactional(readOnly = true)
    public OrderPayResultVO orderPayResult(Long orderId) {
        TradeOrder tradeOrder = this.getOrderByIdOrThrow(orderId);
        OrderPayResultVO orderPayResultVO = tradeOrderConvert.toOrderPayResultVO(tradeOrder);
        boolean isSuccess = this.compareOrderStatus(orderId, OrderStatusEnum.PAID, OrderStatusEnum.DELIVERED, OrderStatusEnum.RECEIVING, OrderStatusEnum.FINISH, OrderStatusEnum.APPENDED);
        orderPayResultVO.setIsSuccess(isSuccess);
        return orderPayResultVO;
    }

    /**
     * 获取是否支持售后的订单商品详情VO列表
     *
     * @param tradeOrder：订单信息
     * @param orderProductList：订单商品信息
     * @return java.util.List<com.msb.mall.trade.model.vo.app.OrderProductAfterSaleVO>
     * @author peng.xy
     * @date 2022/4/15
     */
    private List<OrderProductAfterSaleVO> getOrderProductAfterSaleVOList(TradeOrder tradeOrder, List<TradeOrderProduct> orderProductList) {
        List<OrderProductAfterSaleVO> productAfterSaleList = tradeOrderProductConvert.toOrderProductAfterSaleVOList(orderProductList);
        if (CollectionUtils.isEmpty(productAfterSaleList)) {
            return productAfterSaleList;
        }
        // 已支付、已发货、已收货、已完成、已追评状态，首先判断是否超过售后截止时间
        Integer orderStatus = tradeOrder.getOrderStatus();
        if (EqualsUtil.anyEqualsIDict(orderStatus, OrderStatusEnum.PAID, OrderStatusEnum.DELIVERED, OrderStatusEnum.RECEIVING, OrderStatusEnum.FINISH, OrderStatusEnum.APPENDED)) {
            LocalDateTime afterSaleDeadlineTime = tradeOrder.getAfterSaleDeadlineTime();
            if (Objects.nonNull(afterSaleDeadlineTime)) {
                // 售后截止时间不为空，且当前时间大于售后截止时间
                if (LocalDateTime.now().compareTo(afterSaleDeadlineTime) > 0) {
                    return productAfterSaleList;
                }
            }
            // 判断商品详情状态
            for (OrderProductAfterSaleVO orderProduct : productAfterSaleList) {
                Integer detailStatus = orderProduct.getDetailStatus();
                // 只有正常和退款失败状态，可以申请售后
                if (EqualsUtil.anyEqualsIDict(detailStatus, OrderProductDetailEnum.NORMAL, OrderProductDetailEnum.REFUND_FAIL)) {
                    orderProduct.setAfterSaleApplyFlag(true);
                }
            }
        }
        return productAfterSaleList;
    }

    /**
     * 查询APP订单商品详情
     *
     * @param orderProductId：订单商品ID
     * @return com.msb.mall.trade.model.vo.app.OrderAppInfoVO
     * @author peng.xy
     * @date 2022/3/30
     */
    @Transactional(readOnly = true)
    public OrderProductInfoVO getOrderProductInfo(Long orderProductId) {
        Long userId = UserContext.getUserId();
        TradeOrderProduct orderProduct = tradeOrderProductService.getByIdOrThrow(orderProductId);
        TradeOrder tradeOrder = this.getOrderByIdAndUserIdOrThrow(orderProduct.getOrderId(), userId);
        OrderProductInfoVO orderProductInfo = tradeOrderConvert.toOrderProductInfoVO(tradeOrder);
        orderProductInfo.setOrderProduct(tradeOrderProductConvert.toOrderProductVO(orderProduct));
        return orderProductInfo;
    }

    /**
     * 查询后管订单详情
     *
     * @param orderId：订单ID
     * @return com.msb.mall.trade.model.vo.app.OrderAppInfoVO
     * @author peng.xy
     * @date 2022/3/30
     */
    @Transactional(readOnly = true)
    public OrderInfoAdminVO getOrderInfoByAdmin(@Nonnull Long orderId) {
        // 查询订单信息
        TradeOrder tradeOrder = this.getOrderByIdOrThrow(orderId);
        Long userId = tradeOrder.getUserId();
        // 查询商品详情
        List<TradeOrderProduct> orderProductList = tradeOrderProductService.listByOrderIds(orderId);
        // 查询物流信息
        TradeOrderLogistics orderLogistics = tradeOrderLogisticsService.getByOrderIdOrEmpty(orderId);
        // 查询日志信息
        List<TradeOrderLog> tradeOrderLogList = tradeOrderLogService.listByOrderIdAndTypes(orderId);
        // 查询用户信息
        UserDO userVO = userService.getUserInfoByIdOrEmpty(userId);
        // 查询订单配置信息
        OrderConfigDO orderConfig = orderConfigDubboService.getOrderConfig();
        // 转换视图信息
        OrderInfoAdminVO orderInfoAdminVO = tradeOrderConvert.toOrderAdminInfoVO(tradeOrder);
        orderInfoAdminVO.setUserPhone(userVO.getPhone());
        orderInfoAdminVO.setReceiveExpire(orderConfig.getAutomaticReceipt());
        orderInfoAdminVO.setProducts(tradeOrderProductConvert.toOrderProductAdminVOList(orderProductList));
        orderInfoAdminVO.setLogistics(tradeOrderLogisticsConvert.toOrderLogisticsVO(orderLogistics));
        orderInfoAdminVO.setOrderLogs(tradeOrderLogConvert.toOrderLogVOList(tradeOrderLogList));
        log.info("查询后管订单详情，订单ID：{}，数据：{}", orderId, orderInfoAdminVO);
        return orderInfoAdminVO;
    }

    /**
     * 用户手动取消订单
     *
     * @param orderCancelDTO：用户取消订单参数
     * @author peng.xy
     * @date 2022/3/30
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelByUser(@Nonnull OrderCancelDTO orderCancelDTO) {
        // 获取订单ID和用户ID
        Long orderId = orderCancelDTO.getOrderId();
        Long userId = UserContext.getUserId();
        // 校验用户订单
        this.getOrderByIdAndUserIdOrThrow(orderId, userId);
        // 修改订单状态为关闭，必须为待支付状态
        this.compareAndUpdateOrderStatusOrThrow(orderId, OrderStatusEnum.CLOSE, OrderStatusEnum.UNPAID);
        // 更新取消原因
        String cancelReason = IDict.getTextByCode(OrderCancelReasonTypeEnum.class, orderCancelDTO.getCancelReasonType());
        boolean update = super.updateById(new TradeOrder().setId(orderId).setCancelReason(cancelReason));
        Assert.isTrue(update, TradeExceptionCodeEnum.ORDER_UPDATE_FAIL);
        // 保存订单日志信息，并附加备注
        tradeOrderLogService.saveOrderLogs(orderId, OrderOperationLogTypeEnum.CANCEL_BY_USER, cancelReason);
        // 返还商品库存
        tradeOrderProductService.returnStockByOrderId(orderId);
        return true;
    }

    /**
     * 后台取消订单，并进行退款
     *
     * @param orderCancelAdminDTO：后台取消订单参数
     * @return boolean
     * @author peng.xy
     * @date 2022/4/1
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelByAdmin(@Nonnull OrderCancelAdminDTO orderCancelAdminDTO) {
        Long orderId = orderCancelAdminDTO.getOrderId();
        String cancelReason = orderCancelAdminDTO.getCancelReason();
        String remark = orderCancelAdminDTO.getRemark();
        TradeOrder tradeOrder = this.getOrderByIdOrThrow(orderId);
        // 检查是否有正在申请售后、或者退款成功的订单商品，有的话则不允许取消订单
        boolean compare = tradeOrderProductService.compareDetailStatusByOrderId(orderId, OrderProductDetailEnum.AFTER_SALE, OrderProductDetailEnum.REFUND_SUCCESS);
        BizAssert.notTrue(compare, "订单有正在申请售后或退款成功的商品，不允许取消");
        // 修改订单状态为关闭，必须为已支付状态
        this.compareAndUpdateOrderStatusOrThrow(orderId, OrderStatusEnum.CLOSE, OrderStatusEnum.PAID);
        boolean update = super.updateById(new TradeOrder().setId(orderId).setCancelReason(cancelReason));
        Assert.isTrue(update, TradeExceptionCodeEnum.ORDER_UPDATE_FAIL);
        // 保存订单日志信息，并附加备注
        tradeOrderLogService.saveOrderLogs(orderId, OrderOperationLogTypeEnum.CANCEL_BY_ADMIN, remark);
        // 获取订单支付信息
        TradeOrderPayCenter tradeOrderPayCenter = tradeOrderPayCenterService.getPaySuccessByOrderIdOrThrow(orderId);
        // 获取支付应用信息
        PayCenterConfig.PayApp payApp = payCenterConfig.getByPayCode(tradeOrderPayCenter.getPayCode());
        // 发起退款请求
        ApplyRefundDTO applyRefundDTO = new ApplyRefundDTO()
                .setAppCode(tradeOrderPayCenter.getAppCode())
                .setPayOrderNo(tradeOrderPayCenter.getPayOrderNo())
                .setRefundOrderNo(this.generateOrderNo(tradeOrder.getUserId()))
                .setRefundAmount(tradeOrder.getPayAmount())
                .setRefundReason(cancelReason);
        SignKit.setSign(applyRefundDTO, payApp.getSignKey());
        ApplyRefundVO applyRefundVO = payCenterDubboService.applyRefund(applyRefundDTO);
        // 判断申请返回状态
        if (applyRefundVO.getApplySuccess()) {
            RefundOrderPayCenter refundOrderPayCenter = new RefundOrderPayCenter()
                    .setRefundId(0L)
                    .setPayOrderId(tradeOrderPayCenter.getPayOrderId())
                    .setPayOrderNo(tradeOrderPayCenter.getPayOrderNo())
                    .setRefundOrderId(applyRefundVO.getRefundOrderId())
                    .setRefundOrderNo(applyRefundVO.getRefundOrderNo())
                    .setAppCode(applyRefundVO.getAppCode())
                    .setIsSuccess(CommonConst.NO);
            refundOrderPayCenterService.save(refundOrderPayCenter);
        }
        // 发送订单取消通知
        tradeOrder.setCancelReason(cancelReason);
        List<TradeOrderProduct> orderProductList = tradeOrderProductService.listByOrderIds(orderId);
        notifyService.orderCancelNotify(tradeOrder, orderProductList);
        return true;
    }

    /**
     * 后台关闭订单
     *
     * @param orderCloseDTO：后台关闭订单参数
     * @return boolean
     * @author peng.xy
     * @date 2022/4/1
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean closeByAdmin(OrderCloseDTO orderCloseDTO) {
        Long orderId = orderCloseDTO.getOrderId();
        String remark = orderCloseDTO.getRemark();
        // 修改订单状态为关闭，必须为待付款状态
        this.compareAndUpdateOrderStatusOrThrow(orderId, OrderStatusEnum.CLOSE, OrderStatusEnum.UNPAID);
        // 更新取消原因
        boolean update = super.updateById(new TradeOrder().setId(orderId).setCancelReason(remark));
        Assert.isTrue(update, TradeExceptionCodeEnum.ORDER_UPDATE_FAIL);
        // 保存订单日志信息，并附加备注
        tradeOrderLogService.saveOrderLogs(orderId, OrderOperationLogTypeEnum.CLOSE_BY_ADMIN, remark);
        // 返还商品库存
        tradeOrderProductService.returnStockByOrderId(orderId);
        return true;
    }

    /**
     * 查询APP物流详情信息
     *
     * @param orderId：订单ID
     * @return com.msb.mall.trade.model.vo.app.LogisticsAppInfoVO
     * @author peng.xy
     * @date 2022/3/30
     */
    @Transactional(readOnly = true)
    public LogisticsInfoAppVO logisticsInfoByApp(@Nonnull Long orderId) {
        // 检查订单信息
        TradeOrder tradeOrder = this.getOrderByIdAndUserIdOrThrow(orderId, UserContext.getUserId());
        // 查询商品详情
        List<TradeOrderProduct> orderProductList = tradeOrderProductService.listByOrderIds(orderId);
        // 查询物流信息
        TradeOrderLogistics orderLogistics = tradeOrderLogisticsService.getByOrderIdOrEmpty(orderId);
        Assert.notNull(orderLogistics, TradeExceptionCodeEnum.ORDER_LOGISTICS_ERROR);
        // 转换视图
        LogisticsInfoAppVO logisticsInfoAppVO = tradeOrderLogisticsConvert.toLogisticsAppInfoVO(orderLogistics);
        logisticsInfoAppVO.setProducts(tradeOrderProductConvert.toOrderProductVOList(orderProductList));
        logisticsInfoAppVO.setLogisticsDataList(tradeOrderLogisticsService.parseData(orderLogistics.getLogisticsApi(), orderLogistics.getLogisticsData()));
        logisticsInfoAppVO.setOrderType(tradeOrder.getOrderType());
        return logisticsInfoAppVO;
    }

    /**
     * 确认收货
     *
     * @param orderReceiveDTO：确认收货参数
     * @author peng.xy
     * @date 2022/3/30
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean receive(@Nonnull OrderReceiveDTO orderReceiveDTO) {
        // 校验用户订单
        Long orderId = orderReceiveDTO.getOrderId();
        this.getOrderByIdAndUserIdOrThrow(orderId, UserContext.getUserId());
        // 修改订单状态为完成，必须为已发货状态
        this.compareAndUpdateOrderStatusOrThrow(orderId, OrderStatusEnum.RECEIVING, OrderStatusEnum.DELIVERED);
        // 查询订单配置信息
        OrderConfigDO orderConfig = orderConfigDubboService.getOrderConfig();
        // 设置售后截止时间（天）
        LocalDateTime afterSaleDeadlineTime = LocalDateTime.now().plusDays(orderConfig.getAfterSalesExpire());
        // 设置自动好评时间
        LocalDateTime autoPraiseTime = LocalDateTime.now().plusDays(orderConfig.getAutomaticPraise());
        // 设置收货时间
        LocalDateTime receiveTime = LocalDateTime.now();
        boolean update = super.updateById(new TradeOrder().setId(orderId).setAfterSaleDeadlineTime(afterSaleDeadlineTime).setAutoPraise(autoPraiseTime).setReceiveTime(receiveTime));
        Assert.isTrue(update, TradeExceptionCodeEnum.ORDER_UPDATE_FAIL);
        // 设置订单详情的评价状态
        tradeOrderProductService.updateCommentStatus(orderId, CommentStatusEnum.WAITE_COMMENT.getCode());
        // 记录订单操作日志
        tradeOrderLogService.saveOrderLogs(orderId, OrderOperationLogTypeEnum.USER_RECEIVE);
        return true;
    }

    /**
     * 查询用户订单统计
     *
     * @return com.msb.mall.trade.model.vo.app.OrderStatisticsVO
     * @author peng.xy
     * @date 2022/3/31
     */
    public OrderStatisticsVO statisticsByUser(@Nonnull Long userId) {
        return new OrderStatisticsVO()
                .setAllCount(this.countOfStatistics(userId, null))
                .setUnpaidCount(this.countOfStatistics(userId, OrderStatusEnum.UNPAID))
                .setCloseCount(this.countOfStatistics(userId, OrderStatusEnum.CLOSE))
                .setWaitDeliveryCount(this.countOfStatistics(userId, OrderStatusEnum.PAID))
                .setDeliveredCount(this.countOfStatistics(userId, OrderStatusEnum.DELIVERED))
                .setReceivingCount(this.countOfStatistics(userId, OrderStatusEnum.RECEIVING))
                .setFinishCount(this.countOfStatistics(userId, OrderStatusEnum.FINISH))
                .setWaitComment(this.countOfStatistics(userId, OrderStatusEnum.RECEIVING));
    }

    /**
     * 查询所有订单统计
     *
     * @return com.msb.mall.trade.model.vo.app.OrderStatisticsVO
     * @author peng.xy
     * @date 2022/3/31
     */
    public OrderStatisticsVO statisticsAll() {
        return new OrderStatisticsVO()
                .setAllCount(this.countOfStatistics(null))
                .setUnpaidCount(this.countOfStatistics(OrderStatusEnum.UNPAID))
                .setCloseCount(this.countOfStatistics(OrderStatusEnum.CLOSE))
                .setWaitDeliveryCount(this.countOfStatistics(OrderStatusEnum.PAID))
                .setDeliveredCount(this.countOfStatistics(OrderStatusEnum.DELIVERED))
                .setReceivingCount(this.countOfStatistics(OrderStatusEnum.RECEIVING))
                .setFinishCount(this.countOfStatistics(OrderStatusEnum.FINISH));
    }

    /**
     * 全部订单统计信息
     *
     * @param orderStatus：订单状态：可选
     * @return int
     * @author peng.xy
     * @date 2022/3/31
     */
    public int countOfStatistics(OrderStatusEnum orderStatus) {
        return this.countOfStatistics(null, orderStatus);
    }

    /**
     * 用户订单统计信息
     *
     * @param userId：用户ID，可选
     * @param orderStatus：订单状态：可选
     * @return int
     * @author peng.xy
     * @date 2022/3/31
     */
    private int countOfStatistics(Long userId, OrderStatusEnum orderStatus) {
        LambdaQueryChainWrapper<TradeOrder> lambdaQuery = super.lambdaQuery();
        lambdaQuery.eq(TradeOrder::getIsDeleted, CommonConst.NO);
        lambdaQuery.eq(Objects.nonNull(userId), TradeOrder::getUserId, userId);
        lambdaQuery.eq(Objects.nonNull(userId), TradeOrder::getIsDisabled, CommonConst.NO);
        lambdaQuery.eq(Objects.nonNull(orderStatus), TradeOrder::getOrderStatus, Objects.nonNull(orderStatus) ? orderStatus.getCode() : null);
        return lambdaQuery.count();
    }

    /**
     * 修改收货人信息
     *
     * @param orderRecipientModifyDTO：修改收货人参数
     * @return boolean
     * @author peng.xy
     * @date 2022/4/1
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRecipient(OrderRecipientModifyDTO orderRecipientModifyDTO) {
        Long orderId = orderRecipientModifyDTO.getOrderId();
        // 比较当前订单状态，必须为待付款、已付款
        this.compareOrderStatusOrThrow(orderId, OrderStatusEnum.UNPAID, OrderStatusEnum.PAID);
        // 记录订单操作日志
        tradeOrderLogService.saveOrderLogs(orderId, OrderOperationLogTypeEnum.UPDATE_RECIPIENT);
        // 修改收件人信息
        String recipientAddress = (StringUtils.isNotBlank(orderRecipientModifyDTO.getProvince()) ? orderRecipientModifyDTO.getProvince() : StringUtils.EMPTY) +
                (StringUtils.isNotBlank(orderRecipientModifyDTO.getCity()) ? orderRecipientModifyDTO.getCity() : StringUtils.EMPTY) +
                (StringUtils.isNotBlank(orderRecipientModifyDTO.getArea()) ? orderRecipientModifyDTO.getArea() : StringUtils.EMPTY) +
                (StringUtils.isNotBlank(orderRecipientModifyDTO.getDetailAddress()) ? orderRecipientModifyDTO.getDetailAddress() : StringUtils.EMPTY);
        boolean update = tradeOrderLogisticsService.lambdaUpdate()
                .eq(TradeOrderLogistics::getOrderId, orderId)
                .set(TradeOrderLogistics::getRecipientName, orderRecipientModifyDTO.getRecipientName())
                .set(TradeOrderLogistics::getRecipientPhone, orderRecipientModifyDTO.getRecipientPhone())
                .set(TradeOrderLogistics::getRecipientAddress, recipientAddress)
                .set(TradeOrderLogistics::getProvinceCode, orderRecipientModifyDTO.getProvinceCode())
                .set(TradeOrderLogistics::getProvince, orderRecipientModifyDTO.getProvince())
                .set(TradeOrderLogistics::getCityCode, orderRecipientModifyDTO.getCityCode())
                .set(TradeOrderLogistics::getCity, orderRecipientModifyDTO.getCity())
                .set(TradeOrderLogistics::getAreaCode, orderRecipientModifyDTO.getAreaCode())
                .set(TradeOrderLogistics::getArea, orderRecipientModifyDTO.getArea())
                .set(TradeOrderLogistics::getDetailAddress, orderRecipientModifyDTO.getDetailAddress())
                .set(TradeOrderLogistics::getUpdateUser, UserContext.getUserId())
                .set(TradeOrderLogistics::getUpdateTime, LocalDateTime.now())
                .update();
        Assert.isTrue(update, TradeExceptionCodeEnum.ORDER_UPDATE_FAIL);
        return true;
    }

    /**
     * 修改订单费用
     *
     * @param orderAmountModifyDTO：修改费用参数
     * @return boolean
     * @author peng.xy
     * @date 2022/4/1
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAmount(OrderAmountModifyDTO orderAmountModifyDTO) {
        Long orderId = orderAmountModifyDTO.getOrderId();
        TradeOrder tradeOrder = this.getOrderByIdOrThrow(orderId);
        // 比较当前订单状态，必须为待付款
        this.compareOrderStatusOrThrow(orderId, OrderStatusEnum.UNPAID);
        // 记录订单操作日志
        tradeOrderLogService.saveOrderLogs(orderId, OrderOperationLogTypeEnum.UPDATE_AMOUNT);
        // 查询商品详情
        List<TradeOrderProduct> orderProductList = tradeOrderProductService.listByOrderIds(orderId);
        // 重新计算价格
        this.handleOrderAmount(tradeOrder, orderProductList, orderAmountModifyDTO.getShippingAmount());
        // 修改订单金额
        TradeOrder updateEntity = new TradeOrder().setId(orderId).setTotalAmount(tradeOrder.getTotalAmount()).setShippingAmount(tradeOrder.getShippingAmount())
                .setDiscountAmount(tradeOrder.getDiscountAmount()).setPayAmount(tradeOrder.getPayAmount()).setIsPackageFree(tradeOrder.getIsPackageFree());
        Assert.isTrue(super.updateById(updateEntity), TradeExceptionCodeEnum.ORDER_UPDATE_FAIL);
        return true;
    }

    /**
     * 查询发货列表订单
     *
     * @param orderIds：订单ID列表
     * @return java.util.List<com.msb.mall.trade.model.vo.admin.OrderDeliveryVO>
     * @author peng.xy
     * @date 2022/4/1
     */
    @Transactional(readOnly = true)
    public List<OrderDeliveryVO> listDeliveryOrder(Long... orderIds) {
        if (ArrayUtils.isEmpty(orderIds)) {
            return Collections.emptyList();
        }
        BizAssert.isTrue(orderIds.length <= 100, "单次发货数量限制为100条");
        // 查询所有订单
        List<TradeOrder> tradeOrderList = super.lambdaQuery()
                .select(TradeOrder::getId, TradeOrder::getOrderNo, TradeOrder::getOrderType)
                .eq(TradeOrder::getIsDisabled, CommonConst.NO)
                .eq(TradeOrder::getIsDeleted, CommonConst.NO)
                .eq(TradeOrder::getOrderStatus, OrderStatusEnum.PAID.getCode())
                .in(TradeOrder::getId, Arrays.asList(orderIds))
                .list();
        BizAssert.isTrue(orderIds.length == tradeOrderList.size(), "选中的待发货订单中，存在状态异常订单");
        // 查询所有的物流
        Map<Long, TradeOrderLogistics> logisticsMap = tradeOrderLogisticsService.mapByOrderIds(orderIds);
        // 视图转换
        return tradeOrderList.stream().map(tradeOrder -> {
            OrderDeliveryVO orderDeliveryVO = tradeOrderConvert.toOrderDeliveryVO(tradeOrder);
            TradeOrderLogistics logistics = logisticsMap.get(tradeOrder.getId());
            orderDeliveryVO.setLogistics(tradeOrderLogisticsConvert.toOrderLogisticsVO(logistics));
            return orderDeliveryVO;
        }).collect(Collectors.toList());
    }

    /**
     * 订单发货
     *
     * @param orderDeliveryDTO：订单发货参数
     * @return boolean
     * @author peng.xy
     * @date 2022/4/1
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean delivery(OrderDeliveryDTO orderDeliveryDTO) {
        Long orderId = orderDeliveryDTO.getOrderId();
        String companyCode = orderDeliveryDTO.getCompanyCode();
        String companyName = orderDeliveryDTO.getCompanyName();
        String trackingNo = orderDeliveryDTO.getTrackingNo();
        TradeOrder tradeOrder = this.getOrderByIdOrThrow(orderId);
        // 检查是否为虚拟订单
        if (Objects.equals(OrderTypeEnum.VIRTUAL.getCode(), tradeOrder.getOrderType())) {
            throw new BizException("虚拟商品订单，无法进行实物发货");
        }
        // 修改订单状态为发货，必须为已支付状态
        this.compareAndUpdateOrderStatusOrThrow(orderId, OrderStatusEnum.DELIVERED, OrderStatusEnum.PAID);
        // 查询订单配置信息
        OrderConfigDO orderConfig = orderConfigDubboService.getOrderConfig();
        // 设置自动确认收货时间（天）
        LocalDateTime autoReceiveTime = LocalDateTime.now().plusDays(orderConfig.getAutomaticReceipt());
        boolean update = super.updateById(new TradeOrder().setId(orderId).setAutoReceiveTime(autoReceiveTime));
        Assert.isTrue(update, TradeExceptionCodeEnum.ORDER_UPDATE_FAIL);
        // 更新物流信息，并订阅物流推送
        tradeOrderLogisticsService.updateAndSubscribe(orderId, companyCode, companyName, trackingNo);
        // 保存订单日志信息，并附加备注
        tradeOrderLogService.saveOrderLogs(orderId, OrderOperationLogTypeEnum.DELIVERY);
        // 发送订单发货通知
        TradeOrderLogistics orderLogistics = tradeOrderLogisticsService.getByOrderIdOrThrow(orderId);
        List<TradeOrderProduct> orderProductList = tradeOrderProductService.listByOrderIds(orderId);
        notifyService.deliveryNotify(tradeOrder, orderLogistics, orderProductList);
        return true;
    }

    /**
     * 批量发货
     *
     * @param deliveryDTOList：批量发货参数
     * @return java.util.List<com.msb.mall.trade.model.vo.admin.DeliveryResultVO>
     * @author peng.xy
     * @date 2022/4/7
     */
    public List<OrderDeliveryResultVO> batchDelivery(List<OrderDeliveryDTO> deliveryDTOList) {
        if (CollectionUtils.isEmpty(deliveryDTOList)) {
            return Collections.emptyList();
        }
        BizAssert.isTrue(deliveryDTOList.size() <= 100, "单次批量发货数量限制为100条");
        List<OrderDeliveryResultVO> resultList = new LinkedList<>();
        for (OrderDeliveryDTO deliveryDTO : deliveryDTOList) {
            try {
                this.delivery(deliveryDTO);
                resultList.add(new OrderDeliveryResultVO(deliveryDTO.getOrderId(), Boolean.TRUE, null));
            } catch (Exception e) {
                log.error("批量发货出现异常，参数：{}，异常信息：{}", deliveryDTO, e.getMessage());
                resultList.add(new OrderDeliveryResultVO(deliveryDTO.getOrderId(), Boolean.FALSE, e.getMessage()));
            }
        }
        return resultList;
    }

    /**
     * 虚拟商品订单发货
     *
     * @param orderId：订单ID
     * @return boolean
     * @author peng.xy
     * @date 2022/5/26
     */
    public boolean virtualDelivery(Long orderId) {
        TradeOrder tradeOrder = this.getOrderByIdOrThrow(orderId);
        // 检查是否为虚拟订单
        if (!Objects.equals(OrderTypeEnum.VIRTUAL.getCode(), tradeOrder.getOrderType())) {
            throw new BizException("不是虚拟商品订单，无法发货");
        }
        List<TradeOrderProduct> orderProductList = tradeOrderProductService.listByOrderIds(orderId);
        // 执行异步发货方法
        Future<Boolean> virtualOrderDelivery = notifyService.virtualOrderDelivery(tradeOrder, orderProductList);
        Boolean isSuccess = false;
        try {
            isSuccess = virtualOrderDelivery.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("虚拟商品订单发货异步线程执行失败", e);
        }
        if (!isSuccess) {
            throw new BizException("虚拟商品订单发货失败");
        }
        return true;
    }

    /**
     * 虚拟订单批量发货
     *
     * @param virtualDeliveryDTO：批量发货参数
     * @return java.util.List<com.msb.mall.trade.model.vo.admin.OrderDeliveryResultVO>
     * @author peng.xy
     * @date 2022/5/27
     */
    public List<OrderDeliveryResultVO> batchVirtualDelivery(VirtualDeliveryDTO virtualDeliveryDTO) {
        List<Long> orderIds = virtualDeliveryDTO.getOrderIds();
        if (CollectionUtils.isEmpty(orderIds)) {
            return Collections.emptyList();
        }
        BizAssert.isTrue(orderIds.size() <= 100, "单次批量发货数量限制为100条");
        List<OrderDeliveryResultVO> resultList = new LinkedList<>();
        for (Long orderId : orderIds) {
            try {
                boolean result = this.virtualDelivery(orderId);
                resultList.add(new OrderDeliveryResultVO(orderId, result, null));
            } catch (Exception e) {
                log.error("批量虚拟发货出现异常，订单ID：{}，异常信息：{}", orderId, e.getMessage());
                resultList.add(new OrderDeliveryResultVO(orderId, Boolean.FALSE, e.getMessage()));
            }
        }
        return resultList;
    }

    /**
     * 后管查询物流信息
     *
     * @param orderId：订单ID
     * @return com.msb.mall.trade.model.vo.app.OrderLogisticsVO
     * @author peng.xy
     * @date 2022/4/6
     */
    @Transactional(readOnly = true)
    public OrderLogisticsVO logisticsInfoByAdmin(@Nonnull Long orderId) {
        this.getOrderByIdOrThrow(orderId);
        TradeOrderLogistics orderLogistics = tradeOrderLogisticsService.getByOrderIdOrEmpty(orderId);
        OrderLogisticsVO orderLogisticsVO = tradeOrderLogisticsConvert.toOrderLogisticsVO(orderLogistics);
        List<LogisticsDataVO> logisticsDataListVO = tradeOrderLogisticsService.parseData(orderLogistics.getLogisticsApi(), orderLogistics.getLogisticsData());
        orderLogisticsVO.setLogisticsDataList(logisticsDataListVO);
        return orderLogisticsVO;
    }

    /**
     * 订单超时自动失效
     *
     * @param orderId：订单ID
     * @author peng.xy
     * @date 2022/4/7
     */
    @Transactional(rollbackFor = Exception.class)
    public void expirePay(@Nonnull Long orderId) {
        // 修改订单状态为关闭，必须为待支付状态
        this.compareAndUpdateOrderStatusOrThrow(orderId, OrderStatusEnum.CLOSE, OrderStatusEnum.UNPAID);
        // 更新取消原因
        super.updateById(new TradeOrder().setId(orderId).setCancelReason(OrderOperationLogTypeEnum.TIMEOUT_CANCEL.getRemark()));
        tradeOrderLogService.saveOrderLogs(orderId, OrderOperationLogTypeEnum.TIMEOUT_CANCEL);
        // 返还商品库存
        tradeOrderProductService.returnStockByOrderId(orderId);
    }

    /**
     * 订单到期自动收货
     *
     * @param orderId：订单ID
     * @author peng.xy
     * @date 2022/4/7
     */
    @Transactional(rollbackFor = Exception.class)
    public void expireReceive(@Nonnull Long orderId) {
        // 修改订单状态为已收货，必须为已发货状态
        this.compareAndUpdateOrderStatusOrThrow(orderId, OrderStatusEnum.RECEIVING, OrderStatusEnum.DELIVERED);
        // 查询订单配置信息
        OrderConfigDO orderConfig = orderConfigDubboService.getOrderConfig();
        // 设置自动好评时间
        LocalDateTime autoPraiseTime = LocalDateTime.now().plusDays(orderConfig.getAutomaticPraise());
        // 设置收货时间
        LocalDateTime receiveTime = LocalDateTime.now();
        boolean update = super.updateById(new TradeOrder().setId(orderId).setAutoPraise(autoPraiseTime).setReceiveTime(receiveTime));
        Assert.isTrue(update, TradeExceptionCodeEnum.ORDER_UPDATE_FAIL);
        // 设置订单详情的评价状态
        tradeOrderProductService.updateCommentStatus(orderId, CommentStatusEnum.WAITE_COMMENT.getCode());
        // 记录订单操作日志
        tradeOrderLogService.saveOrderLogs(orderId, OrderOperationLogTypeEnum.AUTO_RECEIVE);
    }

    /**
     * 订单已收货到期自动五星好评
     *
     * @param orderId 订单id
     * @author shumengjiao
     * @date 2022/6/17
     */
    @Transactional(rollbackFor = Exception.class)
    public void expireAutoPraise(@Nonnull Long orderId) {
        // 给订单下的订单详情新增默认评价
        List<TradeOrderProduct> tradeOrderProducts = tradeOrderProductService.listByOrderIds(orderId);
        BizAssert.notNull(tradeOrderProducts, TradeExceptionCodeEnum.ORDER_PRODUCT_ERROR);
        List<CommentDefaultDTO> commentList = new ArrayList<>();
        for (TradeOrderProduct tradeOrderProduct : tradeOrderProducts) {
            CommentDefaultDTO commentDefaultDTO = new CommentDefaultDTO()
                    .setOrderProductId(tradeOrderProduct.getId())
                    .setProductId(tradeOrderProduct.getProductId())
                    .setUserId(tradeOrderProduct.getUserId());
            commentList.add(commentDefaultDTO);
        }
        commentDubboService.saveDefaultComment(commentList);
        // 更新订单状态为已完成（已评论）
        compareAndUpdateOrderStatusOrThrow(orderId, OrderStatusEnum.FINISH, OrderStatusEnum.RECEIVING);
        // 设置订单详情的评价状态
        tradeOrderProductService.updateCommentStatus(orderId, CommentStatusEnum.WAITE_FOLLOW.getCode());
        // 保存日志
        tradeOrderLogService.saveOrderLogs(orderId, OrderOperationLogTypeEnum.AUTO_PRAISE);
    }

    /**
     * 获取用户订单，并开启售后
     *
     * @param orderId：订单ID
     * @param userId：用户ID
     * @return com.msb.mall.trade.model.entity.TradeOrder
     * @author peng.xy
     * @date 2022/4/11
     */
    @Transactional(rollbackFor = Exception.class)
    public TradeOrder getOrderAndOpenAfterSale(@Nonnull Long orderId, @Nonnull Long userId) {
        TradeOrder tradeOrder = this.getOrderByIdAndUserIdOrThrow(orderId, userId);
        // 只有已支付，已发货、已收货、已完成、已追评状态可以申请售后
        this.compareOrderStatusOrThrow(orderId, OrderStatusEnum.PAID, OrderStatusEnum.DELIVERED, OrderStatusEnum.RECEIVING, OrderStatusEnum.FINISH, OrderStatusEnum.APPENDED);
        // 如果已经发货，判断是否达到售后申请截止时间
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime afterSaleDeadlineTime = tradeOrder.getAfterSaleDeadlineTime();
        if (Objects.nonNull(afterSaleDeadlineTime)) {
            BizAssert.isTrue(now.compareTo(afterSaleDeadlineTime) < 0, "已超过售后申请截止时间，无法申请售后");
        }
        ShardTableContext.set(TradeOrder.class, orderId);
        boolean update = super.lambdaUpdate().eq(TradeOrder::getId, orderId)
                .set(TradeOrder::getIsAfterSale, CommonConst.YES)
                .set(TradeOrder::getUpdateTime, now)
                .set(TradeOrder::getUpdateUser, userId)
                .update();
        Assert.isTrue(update, TradeExceptionCodeEnum.ORDER_UPDATE_FAIL);
        return tradeOrder;
    }

    /**
     * 检查退款金额是否超过订单实付金额
     *
     * @param orderId：订单ID
     * @param refundAmount：退款金额
     * @return boolean
     * @author peng.xy
     * @date 2022/4/13
     */
    public void checkRefundAmount(@Nonnull Long orderId, @Nonnull BigDecimal refundAmount) {
        TradeOrder tradeOrder = this.getOrderByIdOrThrow(orderId);
        int compare = tradeOrder.getPayAmount().compareTo(tradeOrder.getRefundAmount().add(refundAmount));
        BizAssert.isTrue(compare >= 0, "累计退款金额不能大于订单实付金额，请检查退款金额");
    }

    /**
     * 累加订单的退款金额，但不能超过订单实付金额
     *
     * @param orderId：订单ID
     * @param cumulativeAmount：累加的退款金额
     * @return boolean
     * @author peng.xy
     * @date 2022/4/13
     */
    public boolean cumulativeRefundAmount(@Nonnull Long orderId, @Nonnull BigDecimal cumulativeAmount) {
        ShardTableContext.set(TradeOrder.class, orderId);
        boolean update = baseMapper.cumulativeRefundAmount(orderId, cumulativeAmount);
        BizAssert.isTrue(update, "累计退款金额不能大于订单实付金额，请检查退款金额");
        List<TradeOrderProduct> tradeOrderProductList = tradeOrderProductService.listByOrderIds(orderId);
        // 如果已经全额退款，则关闭订单
        boolean isRefundAll = true;
        for (TradeOrderProduct orderProduct : tradeOrderProductList) {
            if (!Objects.equals(OrderProductDetailEnum.REFUND_SUCCESS.getCode(), orderProduct.getDetailStatus())) {
                isRefundAll = false;
                break;
            }
        }
        if (isRefundAll) {
            // 修改订单状态为关闭
            this.compareAndUpdateOrderStatusOrThrow(orderId, OrderStatusEnum.CLOSE);
            String cancelReason = "订单已全额退款";
            Assert.isTrue(super.updateById(new TradeOrder().setId(orderId).setCancelReason(cancelReason)), TradeExceptionCodeEnum.ORDER_UPDATE_FAIL);
            // 保存订单日志信息，并附加备注
            tradeOrderLogService.saveOrderLogs(orderId, OrderOperationLogTypeEnum.CLOSE_BY_ADMIN, cancelReason);
        }
        return true;
    }

    /**
     * 发起支付请求
     *
     * @param basePayDTO：发起参数
     * @param payCode：支付方式
     * @return com.msb.pay.model.vo.UnifiedOrderVO<? extends com.msb.pay.model.paydata.PayData>
     * @author peng.xy
     * @date 2022/6/13
     */
    @Transactional(rollbackFor = Exception.class)
    public UnifiedOrderVO<? extends PayData> payRequest(BasePayDTO basePayDTO, PayCodeEnum payCode) {
        // 获取支付应用
        PayCenterConfig.PayApp payApp = payCenterConfig.getByPayCode(payCode.getCode());
        // 检查用户订单和状态
        Long orderId = basePayDTO.getOrderId();
        this.compareOrderStatusOrThrow(orderId, OrderStatusEnum.UNPAID);
        TradeOrder tradeOrder = this.getOrderByIdAndUserIdDelay(orderId, UserContext.getUserId());
        LocalDateTime expireTime = tradeOrder.getExpireTime();
        if (Objects.nonNull(expireTime)) {
            if (LocalDateTime.now().compareTo(expireTime) > 0) {
                throw new BizException(TradeExceptionCodeEnum.ORDER_PAY_EXPIRE);
            }
        }
        // 获取订单商品详情
        String productNames = tradeOrderProductService.getOrderProductNames(orderId);
        if (productNames.length() > 64) {
            productNames = productNames.substring(0, 64);
        }
        // 创建调用支付中台参数
        UnifiedOrderDubboDTO unifiedOrderDubboDTO = new UnifiedOrderDubboDTO();
        unifiedOrderDubboDTO.setAppCode(payApp.getAppCode());
        unifiedOrderDubboDTO.setPayCode(payCode);
//        unifiedOrderDubboDTO.setPayOrderNo(this.generateOrderNo(tradeOrder.getUserId()));
        unifiedOrderDubboDTO.setPayOrderNo(this.generatePayOrderNo());
        unifiedOrderDubboDTO.setAmount(tradeOrder.getPayAmount());
        unifiedOrderDubboDTO.setClientIp(ServletUtil.getClientIP());
        unifiedOrderDubboDTO.setSubject("严选商城订单");
        unifiedOrderDubboDTO.setBody(productNames);
        unifiedOrderDubboDTO.setNotifyUrl(payCenterConfig.getPayNotifyUrl());
        if (basePayDTO instanceof AuthPayDTO) {
            unifiedOrderDubboDTO.setChannelUser(((AuthPayDTO) basePayDTO).getOpenId());
        }
        if (basePayDTO instanceof ReturnPayDTO) {
            unifiedOrderDubboDTO.setReturnUrl(((ReturnPayDTO) basePayDTO).getReturnUrl());
        }
        SignKit.setSign(unifiedOrderDubboDTO, payApp.getSignKey());
        log.info("调用支付中台统一下单参数：{}", unifiedOrderDubboDTO);
        UnifiedOrderVO<? extends PayData> unifiedOrderVO = payCenterDubboService.unifiedOrder(unifiedOrderDubboDTO);
        // 保存支付请求
        TradeOrderPayCenter tradeOrderPayCenter = new TradeOrderPayCenter()
                .setOrderId(orderId)
                .setPayOrderNo(unifiedOrderVO.getPayOrderNo())
                .setPayOrderId(unifiedOrderVO.getPayOrderId())
                .setAppCode(unifiedOrderVO.getAppCode())
                .setPayCode(unifiedOrderVO.getPayCode())
                .setIsSuccess(CommonConst.NO);
        Assert.isTrue(tradeOrderPayCenterService.save(tradeOrderPayCenter), TradeExceptionCodeEnum.ORDER_PAY_SAVE_FAIL);
        return unifiedOrderVO;
    }

    /**
     * 收银台预支付
     *
     * @param returnPayDTO：收银台预支付参数
     * @return com.msb.pay.model.vo.PrepaymentVO
     * @author peng.xy
     * @date 2022/7/8
     */
    @Transactional(rollbackFor = Exception.class)
    public PrepaymentVO cashierPrepay(ReturnPayDTO returnPayDTO) {
        Long orderId = returnPayDTO.getOrderId();
        // 检查用户订单和状态
        this.compareOrderStatusOrThrow(orderId, OrderStatusEnum.UNPAID);
        TradeOrder tradeOrder = this.getOrderByIdAndUserIdDelay(orderId, UserContext.getUserId());
        LocalDateTime expireTime = tradeOrder.getExpireTime();
        if (Objects.nonNull(expireTime)) {
            if (LocalDateTime.now().compareTo(expireTime) > 0) {
                throw new BizException(TradeExceptionCodeEnum.ORDER_PAY_EXPIRE);
            }
        }
        // 获取订单商品详情
        String productNames = tradeOrderProductService.getOrderProductNames(orderId);
        if (productNames.length() > 64) {
            productNames = productNames.substring(0, 64);
        }
        PrepaymentDTO prepaymentDTO = new PrepaymentDTO();
        prepaymentDTO.setPayOrderNo(this.generatePayOrderNo());
        prepaymentDTO.setAmount(tradeOrder.getPayAmount());
        prepaymentDTO.setSubject("严选商城订单");
        prepaymentDTO.setBody(productNames);
        prepaymentDTO.setNotifyUrl(payCenterConfig.getPayNotifyUrl());
        prepaymentDTO.setReturnUrl(returnPayDTO.getReturnUrl());
        log.info("调用支付中台统一下单参数：{}", prepaymentDTO);
        PrepaymentVO prepaymentVO = payCenterDubboService.prepayment(prepaymentDTO);
        // 保存支付请求
        TradeOrderPayCenter tradeOrderPayCenter = new TradeOrderPayCenter()
                .setOrderId(orderId)
                .setPayOrderNo(prepaymentVO.getPayOrderNo())
                .setPayOrderId(prepaymentVO.getPayOrderId())
                .setIsSuccess(CommonConst.NO);
        Assert.isTrue(tradeOrderPayCenterService.save(tradeOrderPayCenter), TradeExceptionCodeEnum.ORDER_PAY_SAVE_FAIL);
        return prepaymentVO;
    }

    /**
     * 支付成功回调通知
     *
     * @param payNotifyDTO：支付成功回调参数
     * @return boolean
     * @author peng.xy
     * @date 2022/4/18
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean paySuccessCallback(PayNotifyDTO payNotifyDTO) {
        // 根据商户订单号获得支付信息
        TradeOrderPayCenter tradeOrderPayCenter = tradeOrderPayCenterService.getByPayOrderNoOrThrow(payNotifyDTO.getPayOrderNo());
        Long orderId = tradeOrderPayCenter.getOrderId();
        // 更新支付信息状态
        tradeOrderPayCenterService.paySuccess(tradeOrderPayCenter.getId(), payNotifyDTO.getAppCode(), payNotifyDTO.getPayCode());
        TradeOrder tradeOrder = this.getOrderByIdOrThrow(orderId);
        int payType = OrderPayTypeEnum.getPayCode(payNotifyDTO.getMchCode());
        tradeOrder.setPayType(payType);
        // 修改订单状态为已支付
        this.compareAndUpdateOrderStatusOrThrow(orderId, OrderStatusEnum.PAID);
        // 修改订单支付类型
        ShardTableContext.set(TradeOrder.class, orderId);
        super.lambdaUpdate().eq(TradeOrder::getId, orderId).set(TradeOrder::getPayType, payType).update();
        // 记录订单操作日志
        tradeOrderLogService.saveOrderLogs(orderId, OrderOperationLogTypeEnum.PAY_ORDER);
        // 发送订单支付成功通知
        List<TradeOrderProduct> orderProductList = tradeOrderProductService.listByOrderIds(orderId);
        notifyService.orderPayNotify(tradeOrder, orderProductList);
        // 如果为虚拟订单，则自动发货
        Integer orderType = tradeOrder.getOrderType();
        if (EqualsUtil.anyEqualsIDict(orderType, OrderTypeEnum.VIRTUAL)) {
            notifyService.virtualOrderDelivery(tradeOrder, orderProductList);
        }
        return true;
    }

    /**
     * 后管取消订单退款通知
     *
     * @param payOrderId：支付订单ID
     * @return boolean
     * @author peng.xy
     * @date 2022/5/7
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean refundSuccessCallback(String payOrderId) {
        // 根据商户订单号获得支付信息
        TradeOrderPayCenter tradeOrderPayCenter = tradeOrderPayCenterService.getByPayOrderIdOrThrow(payOrderId);
        Long orderId = tradeOrderPayCenter.getOrderId();
        // 获取订单信息
        TradeOrder tradeOrder = this.getOrderByIdOrThrow(orderId);
        // 修改订单商品详情状态为退款成功
//        tradeOrderProductService.updateDetailStatusByOrderIdOrThrow(orderId, OrderProductDetailEnum.REFUND_SUCCESS);
        ShardTableContext.set(TradeOrder.class, orderId);
        boolean update = baseMapper.cumulativeRefundAmount(orderId, tradeOrder.getPayAmount());
        BizAssert.isTrue(update, "累计退款金额不能大于订单实付金额，请检查退款金额");
        // 返还商品库存
        tradeOrderProductService.returnStockByOrderId(orderId);
        return true;
    }

    /**
     * 获取销售总额
     *
     * @param beginDateTime 开始时间
     * @param endDateTime   结束时间
     * @return 总额
     */
    public BigDecimal getSales(LocalDateTime beginDateTime, LocalDateTime endDateTime) {
        return tradeOrderMapper.getSales(beginDateTime, endDateTime);
    }

    /**
     * 获取订单数量
     *
     * @param beginDateTime 开始时间
     * @param endDateTime   结束时间
     * @return 订单数量
     */

    public Integer getOrderCount(LocalDateTime beginDateTime, LocalDateTime endDateTime) {
        return this.lambdaQuery()
                .eq(TradeOrder::getIsDeleted, Boolean.FALSE)
                .ge(TradeOrder::getCreateTime, beginDateTime)
                .le(TradeOrder::getCreateTime, endDateTime)
                .in(TradeOrder::getOrderStatus, OrderStatusEnum.PAID.getCode(), OrderStatusEnum.DELIVERED.getCode(), OrderStatusEnum.RECEIVING.getCode(), OrderStatusEnum.FINISH.getCode(), OrderStatusEnum.APPENDED.getCode())
                .count();
    }

    /**
     * 按小时获取销售额
     *
     * @param dayOfStatics 统计的日期
     * @return 订单数量
     */
    public List<SalesStatisticsByHourDO> listSalesStatisticsByHour(LocalDate dayOfStatics) {
        int localHour = LocalDateTime.now().getHour();
        List<SalesStatisticsByHourDO> hourList = IntStream.rangeClosed(0, 23)
                .mapToObj(i -> {
                    BigDecimal sales = i > localHour ? null : BigDecimal.ZERO;
                    return new SalesStatisticsByHourDO()
                            .setRecordDate(i)
                            .setSales(sales);
                })
                .collect(Collectors.toList());
        List<SalesStatisticsByHourDO> queryResultList = tradeOrderMapper.listSalesStaticsByHour(dayOfStatics);
        ListUtil.match(hourList, queryResultList, SalesStatisticsByHourDO::getRecordDate, (hour, queryResult) -> hour.setSales(queryResult.getSales()));
        return hourList;
    }

    /**
     * 按小时获取订单数量
     *
     * @param dayOfStatics 统计的日期
     * @return 订单数量
     */
    public List<OrderStatisticsByHourDO> listOrderCountStatisticsByHour(LocalDate dayOfStatics) {
        int localHour = LocalDateTime.now().getHour();
        List<OrderStatisticsByHourDO> hourList = IntStream.rangeClosed(0, 23)
                .mapToObj(i -> {
                    Integer count = i > localHour ? null : 0;
                    return new OrderStatisticsByHourDO()
                            .setRecordDate(i)
                            .setCount(count);
                })
                .collect(Collectors.toList());
        List<OrderStatisticsByHourDO> queryResultList = tradeOrderMapper.listOrderStaticsByHour(dayOfStatics);
        ListUtil.match(hourList, queryResultList, OrderStatisticsByHourDO::getRecordDate, (hour, queryResult) -> hour.setCount(queryResult.getCount()));
        return hourList;
    }

    /**
     * 查询待评价及待追评订单详情信息列表
     *
     * @return 订单详情信息
     */
    public IPage<OrderProductVO> listOrderProductWaitComment(PageDTO pageDTO) {
        // 查询待评价和待追评的订单详情
        IPage<OrderProductVO> orderProductVoPageList = tradeOrderProductService.listTradeOrderByCommentStatus(pageDTO, CommentStatusEnum.WAITE_COMMENT, CommentStatusEnum.WAITE_FOLLOW);
        List<OrderProductVO> orderProductVoList = orderProductVoPageList.getRecords();
        if (CollectionUtils.isEmpty(orderProductVoList)) {
            return orderProductVoPageList;
        }

        // 获取订单id
        List<Long> orderIdList = orderProductVoList.stream().map(OrderProductVO::getOrderId).distinct().collect(Collectors.toList());
        List<TradeOrder> orderList = this.lambdaQuery().in(TradeOrder::getId, orderIdList).eq(TradeOrder::getIsDeleted, false).list();
        Map<Long, TradeOrder> orderMap = orderList.stream().collect(Collectors.toMap(TradeOrder::getId, order -> order));

        // 给订单详情设置收货时间
        for (OrderProductVO orderProductVo : orderProductVoList) {
            Long orderId = orderProductVo.getOrderId();
            if (orderMap.containsKey(orderId)) {
                orderProductVo.setReceiveTime(orderMap.get(orderId).getReceiveTime());
            }
        }

        // 根据订单的收货时间进行排序
        List<OrderProductVO> sortedList = orderProductVoList.stream()
                .sorted(Comparator.comparing(OrderProductVO::getReceiveTime, Comparator.nullsLast(LocalDateTime::compareTo)))
                .collect(Collectors.toList());
        orderProductVoPageList.setRecords(sortedList);
        return orderProductVoPageList;
    }

    /**
     * 查询待评价订单详情
     *
     * @return 待评价订单详情
     */
    public IPage<OrderProductVO> listOrderProductByCommentStatus(OrderCommentStatusDTO orderCommentStatusDTO) {
        // 分页查询当前用户待评价/待追评/已评价的订单详情
        IPage<OrderProductVO> orderProductVoList = tradeOrderProductService.listTradeOrderByCommentStatusByPage(orderCommentStatusDTO);
        List<OrderProductVO> records = orderProductVoList.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return orderProductVoList;
        }
        // 处理商品信息
        handleProductInfo(records);
        // 获取订单id
        List<Long> orderIdList = records.stream().map(OrderProductVO::getOrderId).distinct().collect(Collectors.toList());
        List<TradeOrder> orderList = this.lambdaQuery().in(TradeOrder::getId, orderIdList).eq(TradeOrder::getIsDeleted, false).list();
        Map<Long, LocalDateTime> orderMap = orderList.stream().collect(Collectors.toMap(TradeOrder::getId, order -> order.getReceiveTime()));
        // 获取评价信息
        List<Long> orderProductIdList = records.stream().map(OrderProductVO::getOrderProductId).distinct().collect(Collectors.toList());
        List<CommentDO> commentDos = commentDubboService.listCommentByOrderProductIds(orderProductIdList);
        Map<Long, LocalDateTime> commentMap = commentDos.stream().collect(Collectors.toMap(CommentDO::getOrderProductId, comment -> comment.getCreateTime()));
        // 给订单详情设置收货时间 首次评价时间
        for (OrderProductVO orderProductVo : records) {
            Long orderId = orderProductVo.getOrderId();
            if (orderMap.containsKey(orderId)) {
                orderProductVo.setReceiveTime(orderMap.get(orderId));
            }
            Long orderProductId = orderProductVo.getOrderProductId();
            if (commentMap.containsKey(orderProductId)) {
                orderProductVo.setCommentTime(commentMap.get(orderProductId));
            }
        }
        // 对订单详情进行排序
        List<OrderProductVO> sortedList = sortOrderProduct(records, orderCommentStatusDTO.getCommentStatus());
        orderProductVoList.setRecords(sortedList);

        return orderProductVoList;
    }

    /**
     * 对订单详情进行排序
     *
     * @param orderProductVos
     * @param commentStatus
     * @return
     */
    public List<OrderProductVO> sortOrderProduct(List<OrderProductVO> orderProductVos, Integer commentStatus) {
        if (CommentStatusEnum.WAITE_COMMENT.getCode().equals(commentStatus)) {
            // 对订单详情进行排序 收货时间、商品id
            List<OrderProductVO> sortedList = orderProductVos.stream()
                    .sorted(Comparator.comparing(OrderProductVO::getReceiveTime, Comparator.nullsLast(LocalDateTime::compareTo))
                            .thenComparing(OrderProductVO::getProductId))
                    .collect(Collectors.toList());
            return sortedList;
        }
        if (CommentStatusEnum.WAITE_FOLLOW.getCode().equals(commentStatus)) {
            // 按照首次评价时间排序
            List<OrderProductVO> sortedList = orderProductVos.stream()
                    .sorted(Comparator.comparing(OrderProductVO::getCommentTime, Comparator.nullsLast(LocalDateTime::compareTo)))
                    .collect(Collectors.toList());
            return sortedList;
        }
        return orderProductVos;
    }

    /**
     * 将商品信息给订单详情
     *
     * @param orderProductList 订单详情列表
     */
    public void handleProductInfo(List<OrderProductVO> orderProductList) {
        // 获取商品id
        List<Long> productIdList = orderProductList.stream().map(OrderProductVO::getProductId).collect(Collectors.toList());
        // 查询商品信息
        List<ProductDO> productDoS = productDubboService.listProductById(productIdList);
        Map<Long, ProductDO> productMap = productDoS.stream().collect(Collectors.toMap(ProductDO::getId, product -> product));
        // 将商品信息赋值给订单详情
        for (OrderProductVO orderProductVo : orderProductList) {
            Long productId = orderProductVo.getProductId();
            if (productMap.containsKey(productId)) {
                ProductDO productDo = productMap.get(productId);
                orderProductVo.setProductName(productDo.getName());
                orderProductVo.setProductImageUrl(productDo.getMainPicture());
            }
        }
    }

}