package com.msb.mall.trade.service;

import com.alibaba.fastjson.JSONObject;
import com.msb.framework.common.context.UserContext;
import com.msb.framework.common.utils.EqualsUtil;
import com.msb.framework.common.utils.ListUtil;
import com.msb.im.api.MallImDubboService;
import com.msb.im.api.dto.SendMessageDTO;
import com.msb.im.api.enums.MallImSessionTypeEnum;
import com.msb.im.api.enums.MessageTypeEnum;
import com.msb.im.api.enums.SessionTypeEnum;
import com.msb.mall.base.api.OrderConfigDubboService;
import com.msb.mall.base.api.model.OrderConfigDO;
import com.msb.mall.product.api.dubbo.ProductDubboService;
import com.msb.mall.product.api.model.ProductVirtualShipDO;
import com.msb.mall.trade.enums.OrderOperationLogTypeEnum;
import com.msb.mall.trade.enums.OrderPayTypeEnum;
import com.msb.mall.trade.enums.OrderStatusEnum;
import com.msb.mall.trade.model.dto.notify.ImTradeNotifyDTO;
import com.msb.mall.trade.model.dto.notify.ImVirtualOrderDeliveryDTO;
import com.msb.mall.trade.model.entity.RefundOrder;
import com.msb.mall.trade.model.entity.TradeOrder;
import com.msb.mall.trade.model.entity.TradeOrderLogistics;
import com.msb.mall.trade.model.entity.TradeOrderProduct;
import com.msb.third.api.WxMpDubboService;
import com.msb.third.enums.WxMpAppEnum;
import com.msb.third.enums.WxMpAppMessageTemplateEnum;
import com.msb.third.model.dto.*;
import com.msb.user.api.UserDubboService;
import com.msb.user.api.vo.UserDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Async
@Service("notifyService")
public class NotifyService {

    @Resource
    private UserService userService;
    @Lazy
    @Resource
    private TradeOrderService tradeOrderService;
    @Resource
    private TradeOrderLogService tradeOrderLogService;
    @Resource
    private TradeOrderLogisticsService tradeOrderLogisticsService;

    @DubboReference
    private ProductDubboService productDubboService;
    @DubboReference
    private WxMpDubboService wxMpDubboService;
    @DubboReference
    private MallImDubboService mallImDubboService;
    @DubboReference
    private UserDubboService userDubboService;
    @DubboReference
    private OrderConfigDubboService orderConfigDubboService;

    /**
     * 订单关闭通知
     *
     * @param tradeOrder：交易订单
     * @param orderProductList：订单商品信息
     * @author peng.xy
     * @date 2022/5/23
     */
    public void orderCancelNotify(TradeOrder tradeOrder, List<TradeOrderProduct> orderProductList) {
        String productNames = orderProductList.stream().map(TradeOrderProduct::getProductName).collect(Collectors.joining("-"));
        List<String> productImageUrls = orderProductList.stream().map(TradeOrderProduct::getProductImageUrl).collect(Collectors.toList());
        String title = "订单已关闭";
        String content = "您购买的[" + productNames + "]订单已关闭，点击查看详情";


        ImTradeNotifyDTO imTradeNotifyDTO = new ImTradeNotifyDTO(WxMpAppMessageTemplateEnum.ORDER_CANCEL.getCode()).setTitle(title).setContent(content).setPrimaryId(tradeOrder.getId()).setProductNames(productNames).setProductImageUrls(productImageUrls);
        SendMessageDTO sendMessageDTO = createSendMessageDTO(tradeOrder, imTradeNotifyDTO);
        Boolean im = mallImDubboService.sendMessage(sendMessageDTO);
        log.info("订单关闭IM通知：{}，{}", sendMessageDTO, im);

        OrderCancelMessageDTO orderCancelMessageDTO = new OrderCancelMessageDTO(WxMpAppEnum.YANXUAN, tradeOrder.getId(), tradeOrder.getUserId());
        orderCancelMessageDTO.setOrderNo(tradeOrder.getOrderNo()).setPayAmount(tradeOrder.getPayAmount()).setCancelReason(tradeOrder.getCancelReason()).setProductNames(productNames);
        Boolean mp = wxMpDubboService.sendOrderCancelMessage(orderCancelMessageDTO);
        log.info("订单关闭公众号通知：{}，{}", orderCancelMessageDTO, mp);
    }

    private SendMessageDTO createSendMessageDTO(TradeOrder tradeOrder, ImTradeNotifyDTO imTradeNotifyDTO) {
        return createSendMessageDTO(tradeOrder.getUserId(), imTradeNotifyDTO);
    }

    private SendMessageDTO createSendMessageDTO(RefundOrder refundOrder, ImTradeNotifyDTO imTradeNotifyDTO) {
        return createSendMessageDTO(refundOrder.getUserId(), imTradeNotifyDTO);
    }

    private SendMessageDTO createSendMessageDTO(Long tradeUserId, ImTradeNotifyDTO imTradeNotifyDTO) {
        long sysUserId = UserContext.getSysUserId();
        UserDO sysUser = userDubboService.getUserDetailInfoById(sysUserId);
        UserDO traceUser = userDubboService.getUserDetailInfoById(tradeUserId);
        SendMessageDTO sendMessageDTO = new SendMessageDTO();
        sendMessageDTO.setType(MessageTypeEnum.CUSTOM);
        sendMessageDTO.setFromId(sysUserId + "");
        sendMessageDTO.setFromAvatar(sysUser.getAvatar());
        sendMessageDTO.setFromNickname(sysUser.getNickname());
        sendMessageDTO.setToId(tradeUserId.toString());
        sendMessageDTO.setToAvatar(traceUser.getAvatar());
        sendMessageDTO.setToNickname(traceUser.getNickname());
        sendMessageDTO.setSysId(1);
        sendMessageDTO.setPayload(JSONObject.toJSONString(imTradeNotifyDTO));
        sendMessageDTO.setSessionTypeEnum(SessionTypeEnum.CUSTOM);
        sendMessageDTO.setSessionPayload(MallImSessionTypeEnum.SYSTEM_SESSION.getPayload());
        return sendMessageDTO;
    }

    /**
     * 订单支付成功通知
     *
     * @param tradeOrder：交易订单
     * @param orderProductList：订单商品信息
     * @author peng.xy
     * @date 2022/5/21
     */
    public void orderPayNotify(TradeOrder tradeOrder, List<TradeOrderProduct> orderProductList) {
        UserDO userDO = userService.getUserInfoByIdOrThrow(tradeOrder.getUserId());
        String productNames = orderProductList.stream().map(TradeOrderProduct::getProductName).collect(Collectors.joining("-"));
        List<String> productImageUrls = orderProductList.stream().map(TradeOrderProduct::getProductImageUrl).collect(Collectors.toList());
        String title = "订单支付成功";
        String content = "您购买的[" + productNames + "]订单已支付成功，感谢您的使用";

        ImTradeNotifyDTO imTradeNotifyDTO = new ImTradeNotifyDTO(WxMpAppMessageTemplateEnum.ORDER_PAY.getCode()).setTitle(title).setContent(content).setPrimaryId(tradeOrder.getId()).setProductNames(productNames).setProductImageUrls(productImageUrls);
        SendMessageDTO sendMessageDTO = createSendMessageDTO(tradeOrder, imTradeNotifyDTO);
        Boolean im = mallImDubboService.sendMessage(sendMessageDTO);
        log.info("订单支付成功IM通知：{}，{}", sendMessageDTO, im);

        // 如果支付方式为微信支付，则发送公众号通知
        if (EqualsUtil.anyEqualsIDict(tradeOrder.getPayType(), OrderPayTypeEnum.WXPAY)) {
            OrderPayMessageDTO orderPayMessageDTO = new OrderPayMessageDTO(WxMpAppEnum.YANXUAN, tradeOrder.getId(), tradeOrder.getUserId());
            orderPayMessageDTO.setOrderNo(tradeOrder.getOrderNo()).setPayAmount(tradeOrder.getPayAmount())
                    .setUserNickName(userDO.getNickname()).setProductNames(productNames);
            Boolean mp = wxMpDubboService.sendOrderPayMessage(orderPayMessageDTO);
            log.info("订单支付成功公众号通知：{}，{}", orderPayMessageDTO, mp);
        }
    }

    /**
     * 订单发货通知
     *
     * @param tradeOrder：交易订单
     * @param orderLogistics：订单物流信息
     * @param orderProductList：订单商品信息
     * @author peng.xy
     * @date 2022/5/21
     */
    public void deliveryNotify(TradeOrder tradeOrder, TradeOrderLogistics orderLogistics, List<TradeOrderProduct> orderProductList) {
        String productNames = orderProductList.stream().map(TradeOrderProduct::getProductName).collect(Collectors.joining("-"));
        List<String> productImageUrls = orderProductList.stream().map(TradeOrderProduct::getProductImageUrl).collect(Collectors.toList());
        String title = "订单已发货";
        String content = "您购买的商品[" + productNames + "]已发货，点击查看物流";

        ImTradeNotifyDTO imTradeNotifyDTO = new ImTradeNotifyDTO(WxMpAppMessageTemplateEnum.ORDER_DELIVERY.getCode()).setTitle(title).setContent(content).setPrimaryId(tradeOrder.getId()).setProductNames(productNames).setProductImageUrls(productImageUrls);
        SendMessageDTO sendMessageDTO = createSendMessageDTO(tradeOrder, imTradeNotifyDTO);
        Boolean im = mallImDubboService.sendMessage(sendMessageDTO);
        log.info("订单发货IM通知：{}，{}", sendMessageDTO, im);

        OrderDeliveryMessageDTO orderDeliveryMessageDTO = new OrderDeliveryMessageDTO(WxMpAppEnum.YANXUAN, tradeOrder.getId(), tradeOrder.getUserId());
        orderDeliveryMessageDTO.setOrderNo(tradeOrder.getOrderNo()).setCompanyName(orderLogistics.getCompanyName()).setTrackingNo(orderLogistics.getTrackingNo());
        Boolean mp = wxMpDubboService.sendOrderDeliveryMessage(orderDeliveryMessageDTO);
        log.info("订单发货IM通知：{}，{}", orderDeliveryMessageDTO, mp);
    }

    /**
     * 填写退货物流通知
     *
     * @param refundOrder：退款单
     * @param orderProduct：退款单商品信息
     * @author peng.xy
     * @date 2022/5/24
     */
    public void returnLogisticsNotify(RefundOrder refundOrder, TradeOrderProduct orderProduct) {
        // 查询订单配置信息
        OrderConfigDO orderConfig = orderConfigDubboService.getOrderConfig();
        String title = "商家已同意退货，请寄回商品";
        String content = "商家已同意您的退货申请，请在" + orderConfig.getReturnGoodsExpire() + "天内将商品寄回，并完成物流信息的填写，点击查看详情";
        String productName = orderProduct.getProductName();
        List<String> productImageUrls = Collections.singletonList(orderProduct.getProductImageUrl());

        ImTradeNotifyDTO imTradeNotifyDTO = new ImTradeNotifyDTO(WxMpAppMessageTemplateEnum.RETURN_LOGISTICS.getCode()).setTitle(title).setContent(content).setPrimaryId(refundOrder.getId()).setProductNames(productName).setProductImageUrls(productImageUrls);
        SendMessageDTO sendMessageDTO = createSendMessageDTO(refundOrder, imTradeNotifyDTO);
        Boolean im = mallImDubboService.sendMessage(sendMessageDTO);
        log.info("填写退货物流IM通知：{}，{}", sendMessageDTO, im);

        ReturnLogisticsMessageDTO returnLogisticsMessageDTO = new ReturnLogisticsMessageDTO(WxMpAppEnum.YANXUAN, refundOrder.getId(), refundOrder.getUserId());
        returnLogisticsMessageDTO.setRefundNo(refundOrder.getRefundNo()).setProductName(productName).setQuantity(orderProduct.getQuantity()).setRefundAmount(refundOrder.getRefundAmount());
        Boolean mp = wxMpDubboService.sendReturnLogisticsMessage(returnLogisticsMessageDTO);
        log.info("填写退货物流公众号通知：{}，{}", returnLogisticsMessageDTO, mp);
    }

    /**
     * 退款失败通知
     *
     * @param refundOrder：退款单
     * @param orderProduct：退款单商品信息
     * @author peng.xy
     * @date 2022/5/24
     */
    public void refundFailNotify(RefundOrder refundOrder, TradeOrderProduct orderProduct) {
        String closeReason = StringUtils.isNotBlank(refundOrder.getCloseReason()) ? refundOrder.getCloseReason() : StringUtils.EMPTY;
        String title = "退款失败通知";
        String content = "您的退款申请未通过，失败原因：" + closeReason + "；您可以修改并重新申请，或联系客服人员，点击查看详情";
        String productName = orderProduct.getProductName();
        List<String> productImageUrls = Collections.singletonList(orderProduct.getProductImageUrl());

        ImTradeNotifyDTO imTradeNotifyDTO = new ImTradeNotifyDTO(WxMpAppMessageTemplateEnum.REFUND_FAIL.getCode()).setTitle(title).setContent(content).setPrimaryId(refundOrder.getId()).setProductNames(productName).setProductImageUrls(productImageUrls);
        SendMessageDTO sendMessageDTO = createSendMessageDTO(refundOrder, imTradeNotifyDTO);
        Boolean im = mallImDubboService.sendMessage(sendMessageDTO);
        log.info("退款失败IM通知：{}，{}", sendMessageDTO, im);

        RefundFailMessageDTO refundFailMessageDTO = new RefundFailMessageDTO(WxMpAppEnum.YANXUAN, refundOrder.getId(), refundOrder.getUserId());
        refundFailMessageDTO.setRefundNo(refundOrder.getRefundNo()).setProductName(productName).setRefundAmount(refundOrder.getRefundAmount()).setCloseReason(closeReason);
        Boolean mp = wxMpDubboService.sendRefundFailMessage(refundFailMessageDTO);
        log.info("退款失败公众号通知：{}，{}", refundFailMessageDTO, mp);
    }

    /**
     * 退款成功通知
     *
     * @param refundOrder：退款单
     * @param orderProduct：退款单商品信息
     * @author peng.xy
     * @date 2022/5/24
     */
    public void refundSuccessNotify(RefundOrder refundOrder, TradeOrderProduct orderProduct) {
        String title = "退款成功通知";
        String content = "退款成功，退款金额将在1-3个工作日内原路返回，请注意查收，点击查看详情";
        String productName = orderProduct.getProductName();
        List<String> productImageUrls = Collections.singletonList(orderProduct.getProductImageUrl());

        ImTradeNotifyDTO imTradeNotifyDTO = new ImTradeNotifyDTO(WxMpAppMessageTemplateEnum.REFUND_SUCCESS.getCode()).setTitle(title).setContent(content).setPrimaryId(refundOrder.getId()).setProductNames(productName).setProductImageUrls(productImageUrls);
        SendMessageDTO sendMessageDTO = createSendMessageDTO(refundOrder, imTradeNotifyDTO);
        Boolean im = mallImDubboService.sendMessage(sendMessageDTO);
        log.info("退款成功IM通知：{}，{}", sendMessageDTO, im);

        RefundSuccessMessageDTO refundSuccessMessageDTO = new RefundSuccessMessageDTO(WxMpAppEnum.YANXUAN, refundOrder.getId(), refundOrder.getUserId());
        refundSuccessMessageDTO.setRefundNo(refundOrder.getRefundNo()).setProductName(productName).setRefundAmount(refundOrder.getRefundAmount()).setRefundReason(refundOrder.getRefundReason());
        Boolean mp = wxMpDubboService.sendRefundSuccessMessage(refundSuccessMessageDTO);
        log.info("退款成功公众号通知：{}，{}", refundSuccessMessageDTO, mp);
    }

    /**
     * 虚拟商品自定发货
     *
     * @param tradeOrder：交易订单
     * @param orderProductList：订单商品信息
     * @return void
     * @author peng.xy
     * @date 2022/5/26
     */
    @Transactional(rollbackFor = Exception.class)
    public Future<Boolean> virtualOrderDelivery(TradeOrder tradeOrder, List<TradeOrderProduct> orderProductList) {
        Long orderId = tradeOrder.getId();
        List<Long> skuIds = ListUtil.convertDistinct(orderProductList, TradeOrderProduct::getProductSkuId);
        List<ProductVirtualShipDO> productVirtualShipDOS = productDubboService.listVirtualShip(skuIds);
        log.info("虚拟商品订单，订单ID：{}，skuIds：{}，发货信息：{}", orderId, skuIds, productVirtualShipDOS);
        if (CollectionUtils.isEmpty(productVirtualShipDOS)) {
            log.error("虚拟商品订单，自动发货信息错误，订单ID：{}", orderId);
            return new AsyncResult(false);
        }
        // 是否发货成功
        boolean isSuccess = true;
        Map<Long, ProductVirtualShipDO> productVirtualShipMap = productVirtualShipDOS.stream().collect(Collectors.toMap(ProductVirtualShipDO::getSkuId, Function.identity()));
        for (TradeOrderProduct tradeOrderProduct : orderProductList) {
            ProductVirtualShipDO productVirtualShipDO = productVirtualShipMap.get(tradeOrderProduct.getProductSkuId());
            if (Objects.isNull(productVirtualShipDO)) {
                log.error("虚拟商品订单自动发货失败，订单ID：{}，订单商品ID：{}，skuId：{}", orderId, tradeOrderProduct.getId(), tradeOrderProduct.getProductSkuId());
                isSuccess = false;
                continue;
            }
            String productName = tradeOrderProduct.getProductName();
            List<String> productImageUrls = Collections.singletonList(tradeOrderProduct.getProductImageUrl());
            String title = "虚拟商品订单自动发货";
            String content = "您购买的商品[" + productName + "]自动发货";

            ImVirtualOrderDeliveryDTO deliveryDTO = new ImVirtualOrderDeliveryDTO("orderAutoDelivery");
            deliveryDTO.setVirtualProductContentList(productVirtualShipDO.getVirtualProductDOList());
            deliveryDTO.setTitle(title);
            deliveryDTO.setContent(content);
            deliveryDTO.setPrimaryId(tradeOrder.getId());
            deliveryDTO.setProductNames(productName);
            deliveryDTO.setProductImageUrls(productImageUrls);

            SendMessageDTO sendMessageDTO = createSendMessageDTO(tradeOrder, deliveryDTO);
            Boolean im = mallImDubboService.sendMessage(sendMessageDTO);
            log.info("虚拟商品订单自动发货IM通知：{}，{}", sendMessageDTO, im);
            if (!im) {
                log.info("调用IM自动发货失败：{}", orderId);
                isSuccess = false;
            }
        }
        if (isSuccess) {
            boolean isNotUpdate = tradeOrderService.compareOrderStatus(orderId, OrderStatusEnum.PAID);
            if (isNotUpdate) {
                // 修改订单状态为发货，必须为已支付状态
                tradeOrderService.compareAndUpdateOrderStatusOrThrow(orderId, OrderStatusEnum.DELIVERED, OrderStatusEnum.PAID);
                // 保存虚拟商品物流信息
                tradeOrderLogisticsService.updateVirtualLogistics(orderId);
                // 查询订单配置信息
                OrderConfigDO orderConfig = orderConfigDubboService.getOrderConfig();
                // 设置自动确认收货时间（天）
                LocalDateTime autoReceiveTime = LocalDateTime.now().plusDays(orderConfig.getAutomaticReceipt());
                tradeOrderService.updateById(new TradeOrder().setId(orderId).setAutoReceiveTime(autoReceiveTime));
                // 保存订单日志信息，并附加备注
                tradeOrderLogService.saveOrderLogs(orderId, OrderOperationLogTypeEnum.DELIVERY, "虚拟商品订单，已自动发货");
            }
            return new AsyncResult(true);
        } else {
            log.error("虚拟商品订单自动发货失败，订单ID：{}，订单商品信息：{}", orderId, orderProductList);
            return new AsyncResult(false);
        }
    }

}
