package com.msb.mall.trade.service;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.common.constant.CommonConst;
import com.msb.framework.common.context.UserContext;
import com.msb.framework.common.exception.BizException;
import com.msb.framework.common.model.IDict;
import com.msb.framework.common.model.PageDTO;
import com.msb.framework.common.utils.EqualsUtil;
import com.msb.framework.common.utils.ListUtil;
import com.msb.framework.web.result.Assert;
import com.msb.framework.web.result.BizAssert;
import com.msb.mall.base.api.OrderConfigDubboService;
import com.msb.mall.base.api.RefundAddressDubboService;
import com.msb.mall.base.api.model.OrderConfigDO;
import com.msb.mall.base.api.model.RefundAddressDO;
import com.msb.mall.trade.config.PayCenterConfig;
import com.msb.mall.trade.enums.*;
import com.msb.mall.trade.exception.TradeExceptionCodeEnum;
import com.msb.mall.trade.mapper.RefundOrderMapper;
import com.msb.mall.trade.model.dto.admin.ReceivingAuditDTO;
import com.msb.mall.trade.model.dto.admin.RefundAuditDTO;
import com.msb.mall.trade.model.dto.admin.RefundQueryAdminDTO;
import com.msb.mall.trade.model.dto.admin.ReturnAuditDTO;
import com.msb.mall.trade.model.dto.app.*;
import com.msb.mall.trade.model.entity.*;
import com.msb.mall.trade.model.vo.admin.*;
import com.msb.mall.trade.model.vo.app.*;
import com.msb.mall.trade.service.convert.*;
import com.msb.pay.api.PayCenterDubboService;
import com.msb.pay.kit.SignKit;
import com.msb.pay.model.dto.ApplyRefundDTO;
import com.msb.pay.model.vo.ApplyRefundVO;
import com.msb.third.api.WxMpDubboService;
import com.msb.user.api.vo.UserDO;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 退款订单表(RefundOrder)表服务实现类
 *
 * @author makejava
 * @date 2022-04-08 18:24:33
 */
@Slf4j
@Service("refundOrderService")
public class RefundOrderService extends ServiceImpl<RefundOrderMapper, RefundOrder> {

    private static final BigDecimal ONE_HUNDRED = new BigDecimal(100);

    @Resource
    private RefundOrderConvert refundOrderConvert;
    @Resource
    private RefundOrderLogisticsConvert refundOrderLogisticsConvert;
    @Resource
    private TradeOrderProductConvert tradeOrderProductConvert;
    @Resource
    private RefundEvidenceConvert refundEvidenceConvert;
    @Resource
    private TradeOrderConvert tradeOrderConvert;

    @Resource
    private PayCenterConfig payCenterConfig;
    @Resource
    private UserService userService;
    @Resource
    private NotifyService notifyService;
    @Resource
    private TradeOrderService tradeOrderService;
    @Resource
    private TradeOrderProductService tradeOrderProductService;
    @Resource
    private RefundEvidenceService refundEvidenceService;
    @Resource
    private RefundOrderLogService refundOrderLogService;
    @Resource
    private RefundOrderLogisticsService refundOrderLogisticsService;
    @Resource
    private TradeOrderPayCenterService tradeOrderPayCenterService;
    @Resource
    private RefundOrderPayCenterService refundOrderPayCenterService;
    @DubboReference
    private RefundAddressDubboService refundAddressDubboService;
    @DubboReference
    private WxMpDubboService wxMpDubboService;
    @DubboReference
    private OrderConfigDubboService orderConfigDubboService;
    @DubboReference
    private PayCenterDubboService payCenterDubboService;

    /**
     * 根据退款单ID获取退款单
     *
     * @param refundId：退款单ID
     * @return com.msb.mall.trade.model.entity.RefundOrder
     * @author peng.xy
     * @date 2022/4/12
     */
    private RefundOrder getRefundById(Long refundId) {
        return this.getRefundByIdAndUserId(refundId, null);
    }

    /**
     * 根据退款单ID获取退款单，数据有误则抛出异常
     *
     * @param refundId：退款单ID
     * @return com.msb.mall.trade.model.entity.RefundOrder
     * @author peng.xy
     * @date 2022/4/12
     */
    private RefundOrder getRefundByIdOrThrow(Long refundId) {
        RefundOrder refundOrder = this.getRefundById(refundId);
        Assert.notNull(refundOrder, TradeExceptionCodeEnum.REFUND_EXCEPTION);
        Assert.notTrue(refundOrder.getIsDeleted(), TradeExceptionCodeEnum.REFUND_EXCEPTION);
        return refundOrder;
    }

    /**
     * 根据退款单ID和用户ID查询退款单
     *
     * @param refundId：退款单ID
     * @param userId：用户ID
     * @return com.msb.mall.trade.model.entity.RefundOrder
     * @author peng.xy
     * @date 2022/4/12
     */
    private RefundOrder getRefundByIdAndUserId(@Nonnull Long refundId, Long userId) {
        RefundOrder refundOrder = super.lambdaQuery()
                .eq(RefundOrder::getId, refundId)
                .eq(Objects.nonNull(userId), RefundOrder::getUserId, userId)
                .one();
        log.info("查询退款单信息，退款单ID：{}, 用户ID：{}，退款单数据：{}", refundId, userId, refundOrder);
        return refundOrder;
    }

    /**
     * 根据退款单ID和用户ID查询退款单，数据有误则抛出异常
     *
     * @param refundId：退款单ID
     * @param userId：用户ID
     * @return com.msb.mall.trade.model.entity.RefundOrder
     * @author peng.xy
     * @date 2022/4/12
     */
    private RefundOrder getRefundByIdAndUserIdOrThrow(@Nonnull Long refundId, @Nonnull Long userId) {
        RefundOrder refundOrder = this.getRefundByIdAndUserId(refundId, userId);
        Assert.notNull(refundOrder, TradeExceptionCodeEnum.REFUND_EXCEPTION);
        Assert.notTrue(refundOrder.getIsDeleted(), TradeExceptionCodeEnum.REFUND_EXCEPTION);
        return refundOrder;
    }

    /**
     * 比较当前退款单状态
     *
     * @param refundId：退款单ID
     * @param refundStatus：进行比较的状态
     * @return boolean
     * @author peng.xy
     * @date 2022/4/12
     */
    public boolean compareRefundStatus(@Nonnull Long refundId, @Nonnull RefundStatusEnum... refundStatus) {
        Integer count = super.lambdaQuery().eq(RefundOrder::getId, refundId)
                .in(RefundOrder::getRefundStatus, ListUtil.convert(Arrays.asList(refundStatus), RefundStatusEnum::getCode))
                .count();
        return Objects.nonNull(count) && count > 0;
    }

    /**
     * 比较当前退款单状态，数据有误则抛出异常
     *
     * @param refundId：退款单ID
     * @param refundStatus：进行比较的状态
     * @author peng.xy
     * @date 2022/4/12
     */
    public void compareRefundStatusOrThrow(@Nonnull Long refundId, @Nonnull RefundStatusEnum... refundStatus) {
        boolean compare = this.compareRefundStatus(refundId, refundStatus);
        Assert.isTrue(compare, TradeExceptionCodeEnum.REFUND_STATUS_EXCEPTION);
    }

    /**
     * 修改退款单状态，但需要对退款单之前的状态进行比较
     *
     * @param refundId：退款单ID
     * @param targetStatus：修改后的状态
     * @param currentStatus：支持修改的当前状态数组，可选
     * @return boolean
     * @author peng.xy
     * @date 2022/4/12
     */
    public boolean compareAndUpdateRefundStatus(@Nonnull Long refundId, @Nonnull RefundStatusEnum targetStatus, RefundStatusEnum... currentStatus) {
        LambdaUpdateChainWrapper<RefundOrder> wrapper = super.lambdaUpdate().eq(RefundOrder::getId, refundId);
        if (ArrayUtils.isNotEmpty(currentStatus)) {
            wrapper.in(RefundOrder::getRefundStatus, ListUtil.convert(Arrays.asList(currentStatus), RefundStatusEnum::getCode));
        }
        return wrapper.set(RefundOrder::getRefundStatus, targetStatus.getCode())
                .set(RefundOrder::getUpdateTime, LocalDateTime.now())
                .set(UserContext.isLogin(), RefundOrder::getUpdateUser, UserContext.getUserIdOrDefault())
                .update();
    }

    /**
     * 修改退款单状态，但需要对退款单之前的状态进行比较，若退款单状态有误则抛出异常
     *
     * @param refundId：退款单ID
     * @param targetStatus：修改后的状态
     * @param currentStatus：支持修改的当前状态数组，可选
     * @author peng.xy
     * @date 2022/4/12
     */
    public void compareAndUpdateRefundStatusOrThrow(@Nonnull Long refundId, @Nonnull RefundStatusEnum targetStatus, RefundStatusEnum... currentStatus) {
        boolean update = this.compareAndUpdateRefundStatus(refundId, targetStatus, currentStatus);
        Assert.isTrue(update, TradeExceptionCodeEnum.REFUND_STATUS_EXCEPTION);
    }

    /**
     * 申请售后退款
     *
     * @param refundApplyDTO：申请售后参数
     * @return void
     * @author peng.xy
     * @date 2022/4/9
     */
    @Transactional(rollbackFor = Exception.class)
    public RefundOrder applyRefund(RefundApplyDTO refundApplyDTO) {
        Long userId = UserContext.getUserId();
        // 获取用户订单和商品详情，并开启售后
        TradeOrderProduct tradeOrderProduct = tradeOrderProductService.getOrderProductAndOpenAfterSale(refundApplyDTO.getOrderProductId());
        TradeOrder tradeOrder = tradeOrderService.getOrderAndOpenAfterSale(tradeOrderProduct.getOrderId(), userId);
        // 申请退款之前的检查
        this.refundCheck(tradeOrder, refundApplyDTO.getRefundType());
        // 创建退款申请单
        RefundOrder refundOrder = this.createRefundOrderBody(refundApplyDTO, tradeOrder, tradeOrderProduct, userId);
        Assert.isTrue(super.save(refundOrder), TradeExceptionCodeEnum.REFUND_SAVE_FAIL);
        Long refundId = refundOrder.getId();
        // 保存退款单凭证
        refundEvidenceService.saveEvidence(refundId, refundApplyDTO.getEvidenceImages(), EvidenceTypeEnum.APPLY, EvidenceFileEnum.IMAGE);
        // 保存退款单日志
        refundOrderLogService.saveRefundLogs(refundId, RefundOperationLogTypeEnum.APPLY_REFUND);
        return refundOrder;
    }

    /**
     * 申请退款之前的检查
     *
     * @param tradeOrder：交易订单
     * @param refundType：退款单类型
     * @return void
     * @author peng.xy
     * @date 2022/4/13
     */
    private void refundCheck(TradeOrder tradeOrder, Integer refundType) {
        // 检查是否为免费订单
        Integer orderType = tradeOrder.getOrderType();
        if (Objects.equals(OrderTypeEnum.FREE.getCode(), orderType)) {
            throw new BizException("免费订单，无需进行退款");
        }
        // 检查是否为虚拟订单
        if (Objects.equals(OrderTypeEnum.VIRTUAL.getCode(), orderType)) {
            throw new BizException("虚拟商品订单不支持申请售后，如有疑问请联系客服");
        }
        // 申请仅退款
        if (EqualsUtil.anyEqualsIDict(refundType, RefundTypeEnum.ONLY_REFUND)) {
            // 订单状态不能为已发货、已收货、已完成状态
            boolean compareOrderStatus = tradeOrderService.compareOrderStatus(tradeOrder.getId(), OrderStatusEnum.DELIVERED, OrderStatusEnum.RECEIVING, OrderStatusEnum.FINISH, OrderStatusEnum.APPENDED);
            BizAssert.notTrue(compareOrderStatus, "订单已发货，不支持申请仅退款");
        }
        // 申请退货退款
        else if (EqualsUtil.anyEqualsIDict(refundType, RefundTypeEnum.REFUND_AND_RETURN)) {
            // 订单状态必须为已发货、已收货、已完成状态
            boolean compareOrderStatus = tradeOrderService.compareOrderStatus(tradeOrder.getId(), OrderStatusEnum.DELIVERED, OrderStatusEnum.RECEIVING, OrderStatusEnum.FINISH, OrderStatusEnum.APPENDED);
            BizAssert.isTrue(compareOrderStatus, "订单未发货，不支持申请退货退款");
        }

    }

    /**
     * 创建退款单主体
     *
     * @param refundApplyDTO：创建退款单参数
     * @param tradeOrder：交易订单
     * @param tradeOrderProduct：交易订单商品
     * @param userId：用户ID
     * @return com.msb.mall.trade.model.entity.RefundOrder
     * @author peng.xy
     * @date 2022/4/9
     */
    private RefundOrder createRefundOrderBody(RefundApplyDTO refundApplyDTO, TradeOrder tradeOrder, TradeOrderProduct tradeOrderProduct, Long userId) {
        // 生成订单编号
        String refundNo = this.generateRefundNo(userId);
        // 查询订单配置信息
        OrderConfigDO orderConfig = orderConfigDubboService.getOrderConfig();
        // 获取商家处理到期时间（天）
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime handleExpireTime = now.plusDays(orderConfig.getAfterSalesApplicationExpire());
        Boolean isBackShippingAmount = CommonConst.NO;
        BigDecimal applyAmount = tradeOrderProduct.getRealAmount();
        // 订单未发货，并且没有退过运费，则默认退运费
        if (Objects.equals(tradeOrder.getOrderStatus(), OrderStatusEnum.PAID.getCode())) {
            if (!this.hasBackShippingRefund(tradeOrder.getId())) {
                isBackShippingAmount = CommonConst.YES;
                applyAmount = applyAmount.add(tradeOrder.getShippingAmount());
            }
        }
        RefundOrder refundOrder = new RefundOrder()
                .setRefundNo(refundNo)
                .setUserId(userId)
                .setOrderId(tradeOrder.getId())
                .setOrderProductId(tradeOrderProduct.getId())
                .setProductAmount(tradeOrderProduct.getRealAmount())
                .setBackShippingAmount(tradeOrder.getShippingAmount())
                .setApplyAmount(applyAmount)
                .setRefundAmount(applyAmount)
                .setProblemDescribe(refundApplyDTO.getProblemDescribe())
                .setRefundReason(IDict.getTextByCode(RefundReasonEnum.class, refundApplyDTO.getRefundReason()))
                .setApplyTime(now)
                .setHandleExpireTime(handleExpireTime)
                .setReceiveStatus(refundApplyDTO.getReceiveStatus())
                .setRefundType(refundApplyDTO.getRefundType())
                .setRefundStatus(RefundStatusEnum.APPLY.getCode())
                .setHandleStatus(RefundHandleEnum.WAIT_HANDLE.getCode())
                .setIsBackShippingAmount(isBackShippingAmount)
                .setIsDeleted(CommonConst.NO);
        log.info("创建退款单主体：{}", refundOrder);
        return refundOrder;
    }

    /**
     * 查询是否有正在退款、已经退款的退过运费的售后
     *
     * @param orderId：订单ID
     * @return boolean
     * @author peng.xy
     * @date 2022/4/22
     */
    private boolean hasBackShippingRefund(Long orderId) {
        Integer[] status = new Integer[]{
                RefundStatusEnum.APPLY.getCode(),
                RefundStatusEnum.WAIT_RETURN.getCode(),
                RefundStatusEnum.IN_RETURN.getCode(),
                RefundStatusEnum.IN_REFUND.getCode(),
                RefundStatusEnum.REFUND_SUCCESS.getCode()
        };
        Integer count = super.lambdaQuery().eq(RefundOrder::getOrderId, orderId)
                .eq(RefundOrder::getIsBackShippingAmount, CommonConst.YES)
                .in(RefundOrder::getRefundStatus, status).count();
        return Objects.nonNull(count) && count > 0;
    }

    /**
     * 生成退款单编号
     *
     * @return java.lang.String
     * @author peng.xy
     * @date 2022/3/29
     */
    private String generateRefundNo(Long userId) {
        return "R" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) + userId + RandomStringUtils.randomNumeric(6);
    }

    /**
     * 查询APP退款单分页列表
     *
     * @param pageDTO：列表参数
     * @return Page<com.msb.mall.trade.model.vo.app.RefundListAppVO>
     * @author peng.xy
     * @date 2022/4/11
     */
    @Transactional(readOnly = true)
    public Page<RefundListAppVO> pageRefundByApp(PageDTO pageDTO) {
        Page<RefundOrder> entityPage = super.lambdaQuery()
                .eq(RefundOrder::getIsDeleted, CommonConst.NO)
                .eq(RefundOrder::getUserId, UserContext.getUserId())
                .orderByDesc(RefundOrder::getId)
                .page(pageDTO.page());
        Page<RefundListAppVO> voPage = refundOrderConvert.toRefundListAppVOPage(entityPage);
        List<RefundListAppVO> voList = voPage.getRecords();
        if (CollectionUtils.isEmpty(voList)) {
            return voPage;
        }
        // 查询所有的商品详情
        List<Long> orderProductIds = ListUtil.convertDistinct(voList, RefundListAppVO::getOrderProductId);
        Map<Long, TradeOrderProduct> tradeOrderProductMap = tradeOrderProductService.mapByOrderProductIds(orderProductIds);
        // 视图转换
        return voPage.setRecords(voList.stream().map(refundListAppVO -> {
            // 获取订单商品信息
            TradeOrderProduct tradeOrderProduct = tradeOrderProductMap.get(refundListAppVO.getOrderProductId());
            refundListAppVO.setRefundProduct(tradeOrderProductConvert.toRefundProductVO(tradeOrderProduct));
            return refundListAppVO;
        }).collect(Collectors.toList()));
    }

    /**
     * 查询后管退款单列表
     *
     * @param refundQueryAdminDTO：列表参数
     * @return Page<com.msb.mall.trade.model.vo.admin.RefundListAdminVO>
     * @author peng.xy
     * @date 2022/4/12
     */
    @Transactional(readOnly = true)
    public Page<RefundListAdminVO> pageRefundByAdmin(RefundQueryAdminDTO refundQueryAdminDTO) {
        List<Long> queryUserIds = userService.getQueryUserIdsOrDefault(refundQueryAdminDTO.getUserPhone());
        List<Integer> statusList = refundQueryAdminDTO.getRefundStatus();
        Page<RefundOrder> entityPage = super.lambdaQuery()
                .eq(RefundOrder::getIsDeleted, CommonConst.NO)
                .like(StringUtils.isNotBlank(refundQueryAdminDTO.getRefundNo()), RefundOrder::getRefundNo, refundQueryAdminDTO.getRefundNo())
                .eq(Objects.nonNull(refundQueryAdminDTO.getRefundType()), RefundOrder::getRefundType, refundQueryAdminDTO.getRefundType())
                .eq(Objects.nonNull(refundQueryAdminDTO.getHandleStatus()), RefundOrder::getHandleStatus, refundQueryAdminDTO.getHandleStatus())
                .ge(Objects.nonNull(refundQueryAdminDTO.getApplyStartTime()), RefundOrder::getApplyTime, refundQueryAdminDTO.getApplyStartTime())
                .le(Objects.nonNull(refundQueryAdminDTO.getApplyEndTime()), RefundOrder::getApplyTime, refundQueryAdminDTO.getApplyEndTime())
                .in(CollectionUtils.isNotEmpty(queryUserIds), RefundOrder::getUserId, queryUserIds)
                .in(CollectionUtils.isNotEmpty(statusList), RefundOrder::getRefundStatus, statusList)
                .orderByDesc(RefundOrder::getId)
                .page(refundQueryAdminDTO.page());
        Page<RefundListAdminVO> voPage = refundOrderConvert.toRefundListAdminVOPage(entityPage);
        List<RefundListAdminVO> voList = voPage.getRecords();
        if (CollectionUtils.isEmpty(voList)) {
            return voPage;
        }
        // 查询所有用户信息
        List<Long> userIds = ListUtil.convertDistinct(voList, RefundListAdminVO::getUserId);
        Map<Long, UserDO> userMapVO = userService.mapUserByIdsOrEmpty(userIds);
        return voPage.setRecords(voList.stream().map(refundListAdminVO -> {
            // 获取用户信息
            UserDO userVO = userMapVO.get(refundListAdminVO.getUserId());
            if (Objects.nonNull(userVO)) {
                refundListAdminVO.setUserPhone(userVO.getPhone());
            }
            return refundListAdminVO;
        }).collect(Collectors.toList()));
    }

    /**
     * 退款单详情（多参数）
     *
     * @param refundInfoDTO：查询参数
     * @return com.msb.mall.trade.model.vo.app.RefundInfoAppVO
     * @author peng.xy
     * @date 2022/4/15
     */
    public RefundInfoAppVO getRefundInfoByApp(RefundInfoDTO refundInfoDTO) {
        BizAssert.isTrue(refundInfoDTO.validate(), "退款单ID/订单商品ID，其中一项必传");
        // 根据退款单ID查询
        if (Objects.nonNull(refundInfoDTO.getRefundId())) {
            return this.getRefundInfoByApp(refundInfoDTO.getRefundId());
        }
        // 根据订单商品ID查询最近的退款单
        else if (Objects.nonNull(refundInfoDTO.getOrderProductId())) {
            Optional<RefundOrder> refundOptional = super.lambdaQuery().select(RefundOrder::getId).eq(RefundOrder::getIsDeleted, CommonConst.NO)
                    .eq(RefundOrder::getOrderProductId, refundInfoDTO.getOrderProductId()).orderByDesc(RefundOrder::getId).list().stream().findFirst();
            if (refundOptional.isPresent()) {
                return this.getRefundInfoByApp(refundOptional.get().getId());
            } else {
                throw new BizException(TradeExceptionCodeEnum.NO_REFUND_EXCEPTION);
            }
        } else {
            throw new BizException(TradeExceptionCodeEnum.REFUND_EXCEPTION);
        }
    }

    /**
     * 查询APP退款单详情
     *
     * @param refundId：退款单ID
     * @return com.msb.mall.trade.model.vo.app.RefundInfoAppVO
     * @author peng.xy
     * @date 2022/4/11
     */
    @Transactional(readOnly = true)
    public RefundInfoAppVO getRefundInfoByApp(@Nonnull Long refundId) {
        // 获取用户退款单
        RefundOrder refundOrder = this.getRefundByIdAndUserIdOrThrow(refundId, UserContext.getUserId());
        RefundInfoAppVO refundInfoAppVO = refundOrderConvert.toRefundInfoAppVO(refundOrder);
        // 获取退款商品
        TradeOrderProduct tradeOrderProduct = tradeOrderProductService.getByIdOrThrow(refundOrder.getOrderProductId());
        refundInfoAppVO.setRefundProduct(tradeOrderProductConvert.toRefundProductVO(tradeOrderProduct));
        // 获取物流信息
        RefundOrderLogistics refundOrderLogistics = refundOrderLogisticsService.getByRefundId(refundId);
        refundInfoAppVO.setRefundLogistics(refundOrderLogisticsConvert.toRefundLogisticsVO(refundOrderLogistics));
        // 获取申请凭证
        List<RefundEvidence> refundEvidenceList = refundEvidenceService.listByRefundIdAndEvidenceType(refundId, EvidenceTypeEnum.APPLY);
        refundInfoAppVO.setRefundEvidences(refundEvidenceConvert.toRefundEvidenceVOList(refundEvidenceList));
        // 获取服务器时间
        refundInfoAppVO.setServerTime(LocalDateTime.now());
        // 自动同意退货、商家同意退货备注
        RefundOrderLog agreeReturnLog = refundOrderLogService.getOneByRefundIdAndTypesOrEmpty(refundId, RefundOperationLogTypeEnum.AUTO_AGREE_RETURN, RefundOperationLogTypeEnum.AGREE_RETURN);
        refundInfoAppVO.setAgreeReturnRemark(agreeReturnLog.getRemark());
        // 用户填写物流备注
        RefundOrderLog completeRefundLog = refundOrderLogService.getOneByRefundIdAndTypesOrEmpty(refundId, RefundOperationLogTypeEnum.COMPLETE_REFUND);
        refundInfoAppVO.setCompleteReturnRemark(completeRefundLog.getRemark());
        // 转换退款原因
        refundInfoAppVO.setRefundReasonType(IDict.getCodeByText(RefundReasonEnum.class, refundInfoAppVO.getRefundReason()));
        log.info("查询APP退款单详情，退款单ID：{}，数据：{}", refundId, refundInfoAppVO);
        return refundInfoAppVO;
    }

    /**
     * 查询后管退款单详情公共部分
     *
     * @param refundId：退款单ID
     * @return com.msb.mall.trade.model.vo.admin.RefundInfoAdminVO
     * @author peng.xy
     * @date 2022/4/14
     */
    private RefundInfoAdminVO getRefundCommonInfo(@Nonnull Long refundId) {
        // 获取退款单信息
        RefundOrder refundOrder = this.getRefundById(refundId);
        RefundInfoAdminVO refundInfoAdminVO = refundOrderConvert.toRefundInfoAdminVO(refundOrder);
        // 获取所属订单信息
        TradeOrder tradeOrder = tradeOrderService.getOrderByIdOrThrow(refundOrder.getOrderId());
        refundInfoAdminVO.setRefundTradeOrderInfo(tradeOrderConvert.toRefundTradeOrderInfoVO(tradeOrder));
        // 获取用户信息
        UserDO userVO = userService.getUserInfoByIdOrEmpty(refundOrder.getUserId());
        refundInfoAdminVO.setUserPhone(userVO.getPhone());
        refundInfoAdminVO.setUserNickName(userVO.getNickname());
        // 获取退款单商品信息
        TradeOrderProduct tradeOrderProduct = tradeOrderProductService.getByIdOrThrow(refundOrder.getOrderProductId());
        refundInfoAdminVO.setRefundProduct(tradeOrderProductConvert.toRefundProductVO(tradeOrderProduct));
        // 获取申请凭证
        List<RefundEvidence> refundEvidenceList = refundEvidenceService.listByRefundIdAndEvidenceType(refundId, EvidenceTypeEnum.APPLY);
        refundInfoAdminVO.setRefundEvidences(refundEvidenceConvert.toRefundEvidenceVOList(refundEvidenceList));
        return refundInfoAdminVO;
    }

    /**
     * （仅退款）查询后管退款单详情
     *
     * @param refundId：退款单ID
     * @return com.msb.mall.trade.model.vo.admin.RefundInfoAdminVO
     * @author peng.xy
     * @date 2022/4/12
     */
    @Transactional(readOnly = true)
    public RefundInfoByOnlyRefundVO getRefundInfoByAdmin(@Nonnull Long refundId) {
        // 获取退款单信息
        RefundInfoByOnlyRefundVO refundInfoByOnlyRefundVO = refundOrderConvert.toRefundInfoByOnlyRefundVO(this.getRefundCommonInfo(refundId));
        // 查询所有退款单日志
        List<RefundLogInfoVO> refundOrderLogInfoVOList = refundOrderLogService.listVOByRefundIdAndTypes(refundId);
        refundInfoByOnlyRefundVO.setRefundLogs(refundOrderLogInfoVOList);
        // 同意退款/拒绝退款-操作日志
        RefundLogInfoVO handleRefundLog = refundOrderLogService.filterVOByTypes(refundOrderLogInfoVOList, RefundOperationLogTypeEnum.AUTO_AGREE_REFUND,
                RefundOperationLogTypeEnum.AGREE_REFUND, RefundOperationLogTypeEnum.DISAGREE_REFUND);
        refundInfoByOnlyRefundVO.setHandleRefundLog(handleRefundLog);
        log.info("查询APP退款单详情（仅退款），退款单ID：{}，数据：{}", refundId, refundInfoByOnlyRefundVO);
        return refundInfoByOnlyRefundVO;
    }

    /**
     * （退货退款）查询后管退款单详情
     *
     * @param refundId：退款单ID
     * @return com.msb.mall.trade.model.vo.admin.RefundInfoAdminVO
     * @author peng.xy
     * @date 2022/4/12
     */
    @Transactional(readOnly = true)
    public RefundInfoByReturnVO getReturnInfoByAdmin(@Nonnull Long refundId) {
        // 获取退款单信息
        RefundInfoByReturnVO refundInfoByReturnVO = refundOrderConvert.toRefundInfoByReturnVO(this.getRefundCommonInfo(refundId));
        // 查询退货单收货点
        RefundAddressDO refundAddressDO = refundAddressDubboService.getRefundAddress();
        refundInfoByReturnVO.setRefundAddress(refundOrderConvert.toRefundAddressVO(refundAddressDO));
        // 获取退货物流凭证信息
        List<RefundEvidence> logisticsEvidences = refundEvidenceService.listByRefundIdAndEvidenceType(refundId, EvidenceTypeEnum.RETURN);
        refundInfoByReturnVO.setLogisticsEvidences(refundEvidenceConvert.toRefundEvidenceVOList(logisticsEvidences));
        // 查询所有退款单日志
        List<RefundLogInfoVO> refundOrderLogInfoVOList = refundOrderLogService.listVOByRefundIdAndTypes(refundId);
        refundInfoByReturnVO.setRefundLogs(refundOrderLogInfoVOList);
        // 同意退货/拒绝退货-操作日志
        RefundLogInfoVO handleReturnLog = refundOrderLogService.filterVOByTypes(refundOrderLogInfoVOList, RefundOperationLogTypeEnum.AUTO_AGREE_RETURN,
                RefundOperationLogTypeEnum.AGREE_RETURN, RefundOperationLogTypeEnum.DISAGREE_REFUND);
        refundInfoByReturnVO.setHandleReturnLog(handleReturnLog);
        // 确认收货/拒绝收货-操作日志
        RefundLogInfoVO handleReceivingLog = refundOrderLogService.filterVOByTypes(refundOrderLogInfoVOList, RefundOperationLogTypeEnum.AUTO_AGREE_RECEIVING,
                RefundOperationLogTypeEnum.AGREE_RECEIVING, RefundOperationLogTypeEnum.DISAGREE_RECEIVING);
        refundInfoByReturnVO.setHandleReceivingLog(handleReceivingLog);
        // 查询退货物流信息
        RefundOrderLogistics refundLogistics = refundOrderLogisticsService.getByRefundId(refundId);
        RefundLogisticsVO refundLogisticsVO = refundOrderLogisticsConvert.toRefundLogisticsVO(refundLogistics);
        if (Objects.nonNull(refundLogistics)) {
            List<LogisticsDataVO> logisticsDataListVO = refundOrderLogisticsService.parseData(refundLogistics.getLogisticsApi(), refundLogistics.getLogisticsData());
            refundLogisticsVO.setLogisticsDataList(logisticsDataListVO);
        }
        refundInfoByReturnVO.setRefundLogistics(refundLogisticsVO);
        // 填写退货物流备注
        refundInfoByReturnVO.setRefundLogisticsRemark(refundOrderLogService.getLogRemark(refundOrderLogInfoVOList, RefundOperationLogTypeEnum.COMPLETE_REFUND));
        log.info("查询APP退款单详情（退货退款），退款单ID：{}，数据：{}", refundId, refundInfoByReturnVO);
        return refundInfoByReturnVO;
    }

    /**
     * 用户修改退款申请
     *
     * @param refundModifyDTO：修改退款申请参数
     * @return boolean
     * @author peng.xy
     * @date 2022/4/12
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRefund(RefundModifyDTO refundModifyDTO) {
        Long refundId = refundModifyDTO.getRefundId();
        // 只有已申请状态，可以进行修改
        this.compareRefundStatusOrThrow(refundId, RefundStatusEnum.APPLY);
        // 校验用户退款单
        RefundOrder refundOrder = this.getRefundByIdAndUserIdOrThrow(refundId, UserContext.getUserId());
        // 获取交易订单
        TradeOrder tradeOrder = tradeOrderService.getOrderByIdOrThrow(refundOrder.getOrderId());
        // 修改之前的检查
        this.refundCheck(tradeOrder, refundModifyDTO.getRefundType());
        // 修改退款单申请
        RefundOrder updateEntity = new RefundOrder()
                .setId(refundId)
                .setRefundType(refundModifyDTO.getRefundType())
                .setReceiveStatus(refundModifyDTO.getReceiveStatus())
                .setRefundReason(IDict.getTextByCode(RefundReasonEnum.class, refundModifyDTO.getRefundReason()))
                .setProblemDescribe(refundModifyDTO.getProblemDescribe());
        Assert.isTrue(super.updateById(updateEntity), TradeExceptionCodeEnum.REFUND_UPDATE_FAIL);
        // 清空原申请凭证
        refundEvidenceService.removeByRefundIdAndEvidenceType(refundId, EvidenceTypeEnum.APPLY);
        // 保存新申请凭证
        String[] evidenceImages = refundModifyDTO.getEvidenceImages();
        if (ArrayUtils.isNotEmpty(evidenceImages)) {
            refundEvidenceService.saveEvidence(refundId, evidenceImages, EvidenceTypeEnum.APPLY, EvidenceFileEnum.IMAGE);
        }
        // 保存退款单日志
        refundOrderLogService.saveRefundLogs(refundId, RefundOperationLogTypeEnum.UPDATE_BY_USER);
        return true;
    }

    /**
     * 撤销退款申请
     *
     * @param refundCancelDTO：撤销退款参数
     * @return boolean
     * @author peng.xy
     * @date 2022/4/12
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelRefund(RefundCancelDTO refundCancelDTO) {
        // 校验用户退款单
        Long refundId = refundCancelDTO.getRefundId();
        RefundOrder refundOrder = this.getRefundByIdAndUserIdOrThrow(refundId, UserContext.getUserId());
        // 修改退款状态为已关闭，必须为已申请、待退货状态
        this.compareAndUpdateRefundStatusOrThrow(refundId, RefundStatusEnum.CLOSE, RefundStatusEnum.APPLY, RefundStatusEnum.WAIT_RETURN);
        boolean update = super.updateById(new RefundOrder().setId(refundId).setCloseReason(RefundOperationLogTypeEnum.CANCEL_BY_USER.getRemark()));
        Assert.isTrue(update, TradeExceptionCodeEnum.REFUND_UPDATE_FAIL);
        // 撤销退款商品售后状态，必须为申请售后状态
        tradeOrderProductService.compareAndUpdateDetailStatusOrThrow(refundOrder.getOrderProductId(), OrderProductDetailEnum.NORMAL, OrderProductDetailEnum.AFTER_SALE);
        // 保存退款单日志
        refundOrderLogService.saveRefundLogs(refundId, RefundOperationLogTypeEnum.CANCEL_BY_USER);
        return true;
    }

    /**
     * 获取并检查退款单类型
     *
     * @param refundId：退款单ID
     * @param refundTypeEnum：退款单类型
     * @return com.msb.mall.trade.model.entity.RefundOrder
     * @author peng.xy
     * @date 2022/4/13
     */
    private RefundOrder checkRefundOrderType(@Nonnull Long refundId, @Nonnull RefundTypeEnum refundTypeEnum) {
        RefundOrder refundOrder = this.getRefundByIdOrThrow(refundId);
        Assert.isTrue(Objects.equals(refundTypeEnum.getCode(), refundOrder.getRefundType()), TradeExceptionCodeEnum.REFUND_TYPE_EXCEPTION);
        return refundOrder;
    }

    /**
     * （仅退款）同意退款
     *
     * @param refundAuditDTO：审核参数
     * @param operationLogType：操作日志枚举
     * @return boolean
     * @author peng.xy
     * @date 2022/4/12
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean agreeRefund(RefundAuditDTO refundAuditDTO, RefundOperationLogTypeEnum operationLogType) {
        Long refundId = refundAuditDTO.getRefundId();
        // 获取并检查退款单类型，必须为仅退款类型
        RefundOrder refundOrder = this.checkRefundOrderType(refundId, RefundTypeEnum.ONLY_REFUND);
        // 检查退款单状态，必须为已申请状态
        this.compareRefundStatusOrThrow(refundId, RefundStatusEnum.APPLY);
        // 检查退款金额，不能超过订单实付金额
        tradeOrderService.checkRefundAmount(refundOrder.getOrderId(), refundAuditDTO.getRefundAmount());
        // 修改实际退款金额，商家处理时间，商家处理状态为同意退款
        boolean update = super.updateById(new RefundOrder().setId(refundId).setRefundAmount(refundAuditDTO.getRefundAmount()).setHandleTime(LocalDateTime.now()).setHandleStatus(RefundHandleEnum.AGREE_REFUND.getCode()));
        Assert.isTrue(update, TradeExceptionCodeEnum.REFUND_UPDATE_FAIL);
        // 保存退款单日志
        refundOrderLogService.saveRefundLogs(refundId, operationLogType, refundAuditDTO.getRemark());
        // 发起退款请求
        boolean requestSuccess = this.refundRequest(refundOrder);
        // 退款申请成功
        if (requestSuccess) {
            // 修改退款单状态为退款中，必须为已申请状态
            this.compareAndUpdateRefundStatusOrThrow(refundId, RefundStatusEnum.IN_REFUND, RefundStatusEnum.APPLY);
            return true;
        }
        // 退款申请失败
        else {
            this.refundApplyFailHandle(refundOrder);
            return false;
        }
    }

    /**
     * （仅退款）拒绝退款
     *
     * @param refundAuditDTO：审核参数
     * @return boolean
     * @author peng.xy
     * @date 2022/4/12
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean disagreeRefund(RefundAuditDTO refundAuditDTO) {
        Long refundId = refundAuditDTO.getRefundId();
        // 获取并检查退款单类型，必须为仅退款类型
        RefundOrder refundOrder = this.checkRefundOrderType(refundId, RefundTypeEnum.ONLY_REFUND);
        // 检查退款金额，不能超过订单实付金额
        tradeOrderService.checkRefundAmount(refundOrder.getOrderId(), refundAuditDTO.getRefundAmount());
        // 修改退款单状态为关闭，必须为已申请状态
        this.compareAndUpdateRefundStatusOrThrow(refundId, RefundStatusEnum.CLOSE, RefundStatusEnum.APPLY);
        // 修改退款商品状态为退款失败，必须为申请售后状态
        tradeOrderProductService.compareAndUpdateDetailStatusOrThrow(refundOrder.getOrderProductId(), OrderProductDetailEnum.REFUND_FAIL, OrderProductDetailEnum.AFTER_SALE);
        // 修改实际退款金额，关闭原因，商家处理时间，商家处理状态为拒绝退款
        boolean update = super.updateById(new RefundOrder().setId(refundId)
                .setRefundAmount(refundAuditDTO.getRefundAmount())
                .setHandleTime(LocalDateTime.now())
                .setHandleStatus(RefundHandleEnum.DISAGREE_REFUND.getCode())
                .setCloseReason(refundAuditDTO.getRemark()));
        Assert.isTrue(update, TradeExceptionCodeEnum.REFUND_UPDATE_FAIL);
        // 保存退款单日志
        refundOrderLogService.saveRefundLogs(refundId, RefundOperationLogTypeEnum.DISAGREE_REFUND, refundAuditDTO.getRemark());
        // 发送退款失败消息通知
        this.sendRefundFailNotify(refundId);
        return true;
    }

    /**
     * （退货退款）同意退货
     *
     * @param returnAuditDTO：审核参数
     * @param operationLogType：操作日志枚举
     * @return boolean
     * @author peng.xy
     * @date 2022/4/13
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean agreeReturn(ReturnAuditDTO returnAuditDTO, RefundOperationLogTypeEnum operationLogType) {
        Long refundId = returnAuditDTO.getRefundId();
        // 获取并检查退款单类型，必须为退货退款类型
        RefundOrder refundOrder = this.checkRefundOrderType(refundId, RefundTypeEnum.REFUND_AND_RETURN);
        // 检查退款金额，不能超过订单实付金额
        tradeOrderService.checkRefundAmount(refundOrder.getOrderId(), returnAuditDTO.getRefundAmount());
        // 修改退款单状态为待退货，必须为已申请状态
        this.compareAndUpdateRefundStatusOrThrow(refundId, RefundStatusEnum.WAIT_RETURN, RefundStatusEnum.APPLY);
        // 查询订单配置信息
        OrderConfigDO orderConfig = orderConfigDubboService.getOrderConfig();
        // 获取用户退货到期时间（天）
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime returnExpireTime = now.plusDays(orderConfig.getReturnGoodsExpire());
        // 修改用户退货到期时间，商家处理时间，商家处理状态为同意退货
        boolean update = super.updateById(new RefundOrder().setId(refundId).setRefundAmount(returnAuditDTO.getRefundAmount()).setHandleTime(LocalDateTime.now())
                .setHandleStatus(RefundHandleEnum.AGREE_RETURN.getCode()).setReturnExpireTime(returnExpireTime));
        Assert.isTrue(update, TradeExceptionCodeEnum.REFUND_UPDATE_FAIL);
        // 创建退款物流信息
        refundOrderLogisticsService.saveRefundLogistics(returnAuditDTO);
        // 保存退款单日志
        refundOrderLogService.saveRefundLogs(refundId, operationLogType, returnAuditDTO.getRemark());
        // 发送填写退货物流通知
        TradeOrderProduct tradeOrderProduct = tradeOrderProductService.getByIdOrThrow(refundOrder.getOrderProductId());
        notifyService.returnLogisticsNotify(refundOrder, tradeOrderProduct);
        return true;
    }

    /**
     * （退货退款）拒绝退货
     *
     * @param returnAuditDTO：审核参数
     * @return boolean
     * @author peng.xy
     * @date 2022/4/13
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean disagreeReturn(ReturnAuditDTO returnAuditDTO) {
        Long refundId = returnAuditDTO.getRefundId();
        // 获取并检查退款单类型，必须为退货退款类型
        RefundOrder refundOrder = this.checkRefundOrderType(refundId, RefundTypeEnum.REFUND_AND_RETURN);
        // 检查退款金额，不能超过订单实付金额
        tradeOrderService.checkRefundAmount(refundOrder.getOrderId(), returnAuditDTO.getRefundAmount());
        // 修改退款单状态为关闭，必须为已申请状态
        this.compareAndUpdateRefundStatusOrThrow(refundId, RefundStatusEnum.CLOSE, RefundStatusEnum.APPLY);
        // 修改退款商品状态为退款失败，必须为申请售后状态
        tradeOrderProductService.compareAndUpdateDetailStatusOrThrow(refundOrder.getOrderProductId(), OrderProductDetailEnum.REFUND_FAIL, OrderProductDetailEnum.AFTER_SALE);
        // 修改实际退款金额，关闭原因，商家处理时间，商家处理状态为拒绝退款
        boolean update = super.updateById(new RefundOrder().setId(refundId).setRefundAmount(returnAuditDTO.getRefundAmount()).setHandleTime(LocalDateTime.now())
                .setHandleStatus(RefundHandleEnum.DISAGREE_RETURN.getCode()).setCloseReason(returnAuditDTO.getRemark()));
        Assert.isTrue(update, TradeExceptionCodeEnum.REFUND_UPDATE_FAIL);
        // 保存退款单日志
        refundOrderLogService.saveRefundLogs(refundId, RefundOperationLogTypeEnum.DISAGREE_RETURN, returnAuditDTO.getRemark());
        // 发送退款失败消息通知
        this.sendRefundFailNotify(refundId);
        return true;
    }

    /**
     * 用户填写退货信息
     *
     * @param refundCompleteDTO：退货信息参数
     * @return boolean
     * @author peng.xy
     * @date 2022/4/13
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean completeRefund(RefundCompleteDTO refundCompleteDTO) {
        // 校验用户退款单
        Long refundId = refundCompleteDTO.getRefundId();
        this.getRefundByIdAndUserIdOrThrow(refundId, UserContext.getUserId());
        // 修改退款单状态为退货中，必须为待退货状态
        this.compareAndUpdateRefundStatusOrThrow(refundId, RefundStatusEnum.IN_RETURN, RefundStatusEnum.WAIT_RETURN);
        // 查询订单配置信息
        OrderConfigDO orderConfig = orderConfigDubboService.getOrderConfig();
        // 获取收货到期时间（天）
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime receivingExpireTime = now.plusDays(orderConfig.getMerchantReceiptExpire());
        // 修改商家收货到期时间
        boolean update = super.updateById(new RefundOrder().setId(refundId).setReceivingExpireTime(receivingExpireTime));
        Assert.isTrue(update, TradeExceptionCodeEnum.REFUND_UPDATE_FAIL);
        // 保存退货物流凭证
        refundEvidenceService.saveEvidence(refundId, refundCompleteDTO.getLogisticsImages(), EvidenceTypeEnum.RETURN, EvidenceFileEnum.IMAGE);
        // 更新物流信息，并订阅物流推送
        refundOrderLogisticsService.updateAndSubscribe(refundId, refundCompleteDTO.getCompanyCode(), refundCompleteDTO.getCompanyName(), refundCompleteDTO.getTrackingNo());
        // 保存退款单日志
        refundOrderLogService.saveRefundLogs(refundId, RefundOperationLogTypeEnum.COMPLETE_REFUND, refundCompleteDTO.getRemark());
        return true;
    }

    /**
     * （退货退款）确认收货
     *
     * @param receivingAuditDTO：确认收货参数
     * @return boolean
     * @author peng.xy
     * @date 2022/4/14
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean agreeReceiving(ReceivingAuditDTO receivingAuditDTO, RefundOperationLogTypeEnum operationLogType) {
        Long refundId = receivingAuditDTO.getRefundId();
        // 获取并检查退款单类型，必须为退货退款类型
        RefundOrder refundOrder = this.checkRefundOrderType(refundId, RefundTypeEnum.REFUND_AND_RETURN);
        // 检查退款单状态，必须为退货中状态
        this.compareRefundStatusOrThrow(refundId, RefundStatusEnum.IN_RETURN);
        // 修改商家处理状态为确认收货
        boolean update = super.updateById(new RefundOrder().setId(refundId).setHandleStatus(RefundHandleEnum.AGREE_RECEIVING.getCode()));
        Assert.isTrue(update, TradeExceptionCodeEnum.REFUND_UPDATE_FAIL);
        // 保存退款单日志
        refundOrderLogService.saveRefundLogs(refundId, operationLogType, receivingAuditDTO.getRemark());
        // 发起退款请求
        boolean requestSuccess = this.refundRequest(refundOrder);
        // 退款申请成功
        if (requestSuccess) {
            // 修改退款单状态为退款中，必须为退货中状态
            this.compareAndUpdateRefundStatusOrThrow(refundId, RefundStatusEnum.IN_REFUND, RefundStatusEnum.IN_RETURN);
            return true;
        }
        // 退款申请失败
        else {
            this.refundApplyFailHandle(refundOrder);
            return false;
        }
    }

    /**
     * （退货退款）拒绝收货
     *
     * @param receivingAuditDTO：确认收货参数
     * @return boolean
     * @author peng.xy
     * @date 2022/4/14
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean disagreeReceiving(ReceivingAuditDTO receivingAuditDTO) {
        Long refundId = receivingAuditDTO.getRefundId();
        // 获取并检查退款单类型，必须为退货退款类型
        RefundOrder refundOrder = this.checkRefundOrderType(refundId, RefundTypeEnum.REFUND_AND_RETURN);
        // 修改退款单状态为退款失败，必须为退货中状态
        this.compareAndUpdateRefundStatusOrThrow(refundId, RefundStatusEnum.REFUND_FAIL, RefundStatusEnum.IN_RETURN);
        // 修改退款商品状态为退款失败，必须为申请售后状态
        tradeOrderProductService.compareAndUpdateDetailStatusOrThrow(refundOrder.getOrderProductId(), OrderProductDetailEnum.REFUND_FAIL, OrderProductDetailEnum.AFTER_SALE);
        // 修改关闭原因，商家处理状态为拒绝收货
        boolean update = super.updateById(new RefundOrder().setId(refundId).setHandleStatus(RefundHandleEnum.DISAGREE_RECEIVING.getCode()).setCloseReason(receivingAuditDTO.getRemark()));
        Assert.isTrue(update, TradeExceptionCodeEnum.REFUND_UPDATE_FAIL);
        // 保存退款单日志
        refundOrderLogService.saveRefundLogs(refundId, RefundOperationLogTypeEnum.DISAGREE_RECEIVING, receivingAuditDTO.getRemark());
        // 发送退款失败消息通知
        this.sendRefundFailNotify(refundId);
        return true;
    }

    /**
     * 后管查询物流信息
     *
     * @param refundId：退款单ID
     * @return com.msb.mall.trade.model.vo.app.OrderLogisticsVO
     * @author peng.xy
     * @date 2022/4/6
     */
    public RefundLogisticsVO logisticsInfoByAdmin(@Nonnull Long refundId) {
        this.getRefundByIdOrThrow(refundId);
        RefundOrderLogistics refundLogistics = refundOrderLogisticsService.getByRefundIdOrThrow(refundId);
        RefundLogisticsVO refundLogisticsVO = refundOrderLogisticsConvert.toRefundLogisticsVO(refundLogistics);
        if (Objects.nonNull(refundLogistics)) {
            List<LogisticsDataVO> logisticsDataListVO = refundOrderLogisticsService.parseData(refundLogistics.getLogisticsApi(), refundLogistics.getLogisticsData());
            refundLogisticsVO.setLogisticsDataList(logisticsDataListVO);
        }
        return refundLogisticsVO;
    }

    /**
     * 处理超时自动同意售后申请
     *
     * @param refundId：退款单ID
     * @return boolean
     * @author peng.xy
     * @date 2022/4/15
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean expireHandle(@Nonnull Long refundId) {
        RefundOrder refundOrder = this.getRefundByIdOrThrow(refundId);
        Integer refundType = refundOrder.getRefundType();
        if (EqualsUtil.anyEqualsIDict(refundType, RefundTypeEnum.ONLY_REFUND)) {
            // 自动同意退款
            RefundAuditDTO refundAuditDTO = new RefundAuditDTO(refundId, refundOrder.getRefundAmount(), StringUtils.EMPTY);
            return this.agreeRefund(refundAuditDTO, RefundOperationLogTypeEnum.AUTO_AGREE_REFUND);
        } else if (EqualsUtil.anyEqualsIDict(refundType, RefundTypeEnum.REFUND_AND_RETURN)) {
            // 查询退货单收货点
            RefundAddressDO refundAddressDO = refundAddressDubboService.getRefundAddress();
            // 自动同意退货
            ReturnAuditDTO returnAuditDTO = new ReturnAuditDTO().setRefundId(refundId)
                    .setRefundAmount(refundOrder.getRefundAmount())
                    .setRecipientName(refundAddressDO.getReceiveName())
                    .setRecipientPhone(refundAddressDO.getReceivePhone())
                    .setProvinceCode(refundAddressDO.getProvinceCode())
                    .setProvince(refundAddressDO.getProvince())
                    .setCityCode(refundAddressDO.getCityCode())
                    .setCity(refundAddressDO.getCity())
                    .setAreaCode(refundAddressDO.getAreaCode())
                    .setArea(refundAddressDO.getArea())
                    .setDetailAddress(refundAddressDO.getDetailAddress())
                    .setRemark(StringUtils.EMPTY);
            return this.agreeReturn(returnAuditDTO, RefundOperationLogTypeEnum.AUTO_AGREE_RETURN);
        } else {
            throw new BizException(TradeExceptionCodeEnum.REFUND_TYPE_EXCEPTION);
        }
    }

    /**
     * 退货超时自动关闭退款单
     *
     * @param refundId：退款单ID
     * @return boolean
     * @author peng.xy
     * @date 2022/4/15
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean expireReturn(@Nonnull Long refundId) {
        // 获取并检查退款单类型，必须为退货退款类型
        RefundOrder refundOrder = this.checkRefundOrderType(refundId, RefundTypeEnum.REFUND_AND_RETURN);
        // 修改退款单状态为关闭，必须为待发货状态
        this.compareAndUpdateRefundStatusOrThrow(refundId, RefundStatusEnum.CLOSE, RefundStatusEnum.WAIT_RETURN);
        // 修改退款商品状态为退款失败，必须为申请售后状态
        tradeOrderProductService.compareAndUpdateDetailStatusOrThrow(refundOrder.getOrderProductId(), OrderProductDetailEnum.REFUND_FAIL, OrderProductDetailEnum.AFTER_SALE);
        // 修改关闭原因
        boolean update = super.updateById(new RefundOrder().setId(refundId).setCloseReason(RefundOperationLogTypeEnum.AUTO_CLOSE_RECEIVING.getRemark()));
        Assert.isTrue(update, TradeExceptionCodeEnum.REFUND_UPDATE_FAIL);
        // 保存退款单日志
        refundOrderLogService.saveRefundLogs(refundId, RefundOperationLogTypeEnum.AUTO_CLOSE_RECEIVING);
        return true;
    }

    /**
     * 商家收货超时，自动确认收货
     *
     * @param refundId：退款单ID
     * @return boolean
     * @author peng.xy
     * @date 2022/4/15
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean expireReceiving(Long refundId) {
        ReceivingAuditDTO receivingAuditDTO = new ReceivingAuditDTO(refundId, StringUtils.EMPTY);
        return this.agreeReceiving(receivingAuditDTO, RefundOperationLogTypeEnum.AUTO_AGREE_RECEIVING);
    }

    /**
     * 用户订单统计信息
     *
     * @param refundStatus：退款单状态：可选
     * @return int
     * @author peng.xy
     * @date 2022/3/31
     */
    public int countOfStatistics(RefundStatusEnum... refundStatus) {
        return this.countOfStatistics(null, refundStatus);
    }

    /**
     * 用户订单统计信息
     *
     * @param userId：用户ID，可选
     * @param refundStatus：退款单状态：可选
     * @return int
     * @author peng.xy
     * @date 2022/3/31
     */
    public int countOfStatistics(@Nonnull Long userId, RefundStatusEnum... refundStatus) {
        LambdaQueryChainWrapper<RefundOrder> lambdaQuery = super.lambdaQuery();
        lambdaQuery.eq(RefundOrder::getIsDeleted, CommonConst.NO);
        lambdaQuery.eq(Objects.nonNull(userId), RefundOrder::getUserId, userId);
        if (ArrayUtils.isNotEmpty(refundStatus)) {
            lambdaQuery.in(RefundOrder::getRefundStatus, Arrays.stream(refundStatus).map(RefundStatusEnum::getCode).collect(Collectors.toList()));
        }
        return lambdaQuery.count();
    }

    /**
     * APP用户退款单统计
     *
     * @param userId：用户ID
     * @return com.msb.mall.trade.model.vo.app.RefundStatisticsAppVO
     * @author peng.xy
     * @date 2022/4/15
     */
    public RefundStatisticsAppVO statisticsByUser(@Nonnull Long userId) {
        // 统计退款进行中的状态，除了退款成功和关闭
        int progressCount = this.countOfStatistics(userId, RefundStatusEnum.APPLY, RefundStatusEnum.WAIT_RETURN,
                RefundStatusEnum.IN_RETURN, RefundStatusEnum.IN_REFUND, RefundStatusEnum.REFUND_FAIL);
        return new RefundStatisticsAppVO().setProgressCount(progressCount);
    }

    /**
     * 后管退款单统计
     *
     * @return com.msb.mall.trade.model.vo.app.RefundStatisticsAppVO
     * @author peng.xy
     * @date 2022/4/15
     */
    @Transactional(readOnly = true)
    public RefundStatisticsAdminVO statisticsByAdmin() {
        return new RefundStatisticsAdminVO()
                .setAllCount(this.countOfStatistics())
                .setApplyCount(this.countOfStatistics(RefundStatusEnum.APPLY))
                .setCloseCount(this.countOfStatistics(RefundStatusEnum.CLOSE))
                .setWaitReturnCount(this.countOfStatistics(RefundStatusEnum.WAIT_RETURN))
                .setInReturnCount(this.countOfStatistics(RefundStatusEnum.IN_RETURN))
                .setInRefundCount(this.countOfStatistics(RefundStatusEnum.IN_REFUND))
                .setRefundSuccessCount(this.countOfStatistics(RefundStatusEnum.REFUND_SUCCESS))
                .setRefundFailCount(this.countOfStatistics(RefundStatusEnum.REFUND_FAIL));
    }

    /**
     * 发起退款请求
     *
     * @param refundOrder：退款单
     * @return boolean：申请是否成功
     * @author peng.xy
     * @date 2022/4/19
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean refundRequest(@Nonnull RefundOrder refundOrder) {
        // 获取订单支付信息
        TradeOrderPayCenter tradeOrderPayCenter = tradeOrderPayCenterService.getPaySuccessByOrderIdOrThrow(refundOrder.getOrderId());
        // 获取支付应用信息
        PayCenterConfig.PayApp payApp = payCenterConfig.getByPayCode(tradeOrderPayCenter.getPayCode());
        // 发起退款请求
        ApplyRefundDTO applyRefundDTO = new ApplyRefundDTO()
                .setAppCode(tradeOrderPayCenter.getAppCode())
                .setPayOrderNo(tradeOrderPayCenter.getPayOrderNo())
                .setRefundOrderNo(this.generateRefundNo(refundOrder.getUserId()))
                .setRefundAmount(refundOrder.getRefundAmount())
                .setRefundReason(refundOrder.getRefundReason())
                .setNotifyUrl(payCenterConfig.getRefundNotifyUrl());
        SignKit.setSign(applyRefundDTO, payApp.getSignKey());
        ApplyRefundVO applyRefundVO = payCenterDubboService.applyRefund(applyRefundDTO);
        // 判断申请返回状态
        if (applyRefundVO.getApplySuccess()) {
            RefundOrderPayCenter refundOrderPayCenter = new RefundOrderPayCenter()
                    .setRefundId(refundOrder.getId())
                    .setPayOrderId(tradeOrderPayCenter.getPayOrderId())
                    .setPayOrderNo(tradeOrderPayCenter.getPayOrderNo())
                    .setRefundOrderId(applyRefundVO.getRefundOrderId())
                    .setRefundOrderNo(applyRefundVO.getRefundOrderNo())
                    .setAppCode(applyRefundVO.getAppCode())
                    .setIsSuccess(CommonConst.NO);
            return refundOrderPayCenterService.save(refundOrderPayCenter);
        }
        return false;
    }

    /**
     * 退款成功回调通知
     *
     * @param refundOrderNo：支付订单号
     * @return boolean
     * @author peng.xy
     * @date 2022/4/19
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean refundSuccessCallback(@Nonnull String refundOrderNo) {
        // 根据退款订单号获取退款单和退款申请信息
        RefundOrderPayCenter refundOrderPayCenter = refundOrderPayCenterService.getByRefundOrderNoOrThrow(refundOrderNo);
        refundOrderPayCenterService.refundSuccess(refundOrderPayCenter.getId());
        Long refundId = refundOrderPayCenter.getRefundId();
        String payOrderId = refundOrderPayCenter.getPayOrderId();
        // 退款单ID为0，为后管直接取消的订单，执行订单退款回调
        if (Objects.equals(refundId, 0L)) {
            return tradeOrderService.refundSuccessCallback(payOrderId);
        }
        // 获取退款单信息
        RefundOrder refundOrder = this.getRefundByIdOrThrow(refundId);
        // 修改退款单状态为退款成功，必须为退款中状态
        this.compareAndUpdateRefundStatusOrThrow(refundId, RefundStatusEnum.REFUND_SUCCESS, RefundStatusEnum.IN_REFUND);
        // 修改退款商品状态为退款成功，必须为申请售后状态
        tradeOrderProductService.compareAndUpdateDetailStatusOrThrow(refundOrder.getOrderProductId(), OrderProductDetailEnum.REFUND_SUCCESS, OrderProductDetailEnum.AFTER_SALE);
        // 累计订单已退款金额
        tradeOrderService.cumulativeRefundAmount(refundOrder.getOrderId(), refundOrder.getRefundAmount());
        // 保存退款单日志
        refundOrderLogService.saveRefundLogs(refundId, RefundOperationLogTypeEnum.REFUND_SUCCESS_NOTIFY);
        // 返还商品库存
        tradeOrderProductService.returnStockByOrderProductIds(refundOrder.getOrderProductId());
        // 发送退款成功通知
        TradeOrderProduct tradeOrderProduct = tradeOrderProductService.getByIdOrThrow(refundOrder.getOrderProductId());
        notifyService.refundSuccessNotify(refundOrder, tradeOrderProduct);
        return true;
    }

    /**
     * 退款申请失败处理
     *
     * @param refundOrder：退款单
     * @return void
     * @author peng.xy
     * @date 2022/4/20
     */
    @Transactional(rollbackFor = Exception.class)
    public void refundApplyFailHandle(@Nonnull RefundOrder refundOrder) {
        // 修改退款单状态为退款失败，关闭原因为退款错误
        this.compareAndUpdateRefundStatusOrThrow(refundOrder.getId(), RefundStatusEnum.REFUND_FAIL);
        super.updateById(new RefundOrder().setId(refundOrder.getId()).setCloseReason("退款请求错误"));
        // 修改退款商品状态为退款失败
        tradeOrderProductService.compareAndUpdateDetailStatusOrThrow(refundOrder.getOrderProductId(), OrderProductDetailEnum.REFUND_FAIL);
        // 发送退款失败消息通知
        this.sendRefundFailNotify(refundOrder.getId());
    }

    /**
     * 发送退款失败消息通知
     *
     * @param refundId：退款单ID
     * @return boolean
     * @author peng.xy
     * @date 2022/5/18
     */
    private boolean sendRefundFailNotify(@Nonnull Long refundId) {
        RefundOrder refundOrder = this.getRefundByIdOrThrow(refundId);
        TradeOrderProduct tradeOrderProduct = tradeOrderProductService.getByIdOrThrow(refundOrder.getOrderProductId());
        notifyService.refundFailNotify(refundOrder, tradeOrderProduct);
        return true;
    }

}

