package com.msb.pay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.common.exception.BizException;
import com.msb.framework.common.model.IDict;
import com.msb.framework.common.utils.EqualsUtil;
import com.msb.framework.common.utils.ListUtil;
import com.msb.framework.web.result.BizAssert;
import com.msb.framework.web.util.ServletUtil;
import com.msb.pay.channel.IPayCodeService;
import com.msb.pay.channel.IPaymentService;
import com.msb.pay.channel.context.ChannelContext;
import com.msb.pay.config.PayCenterConfig;
import com.msb.pay.enums.NotifyStatusEnum;
import com.msb.pay.enums.PayCodeEnum;
import com.msb.pay.enums.PayStatusEnum;
import com.msb.pay.kit.SignKit;
import com.msb.pay.mapper.PayOrderMapper;
import com.msb.pay.model.bo.PayNotifyRES;
import com.msb.pay.model.bo.UnifiedOrderRES;
import com.msb.pay.model.dto.*;
import com.msb.pay.model.entity.AppInfo;
import com.msb.pay.model.entity.MchInfo;
import com.msb.pay.model.entity.PayOrder;
import com.msb.pay.model.paydata.PayData;
import com.msb.pay.model.vo.*;
import com.msb.pay.mq.model.PayOrderDelayNotifyMessage;
import com.msb.pay.service.convert.AppInfoConvert;
import com.msb.pay.service.convert.MchInfoConvert;
import com.msb.pay.service.convert.ModelConvert;
import com.msb.pay.service.convert.PayOrderConvert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 支付订单表(PayOrder)表服务实现类
 *
 * @author makejava
 * @date 2022-06-06 10:42:45
 */
@Slf4j
@Service("payOrderService")
public class PayOrderService extends ServiceImpl<PayOrderMapper, PayOrder> {

    @Resource
    private PayCenterConfig payCenterConfig;
    @Resource
    private MchInfoService mchInfoService;
    @Resource
    private AppInfoService appInfoService;
    @Lazy
    @Resource
    private NotifyService notifyService;

    @Resource
    private PayOrderConvert payOrderConvert;
    @Resource
    private MchInfoConvert mchInfoConvert;
    @Resource
    private AppInfoConvert appInfoConvert;
    @Resource
    private ModelConvert modelConvert;

    private static final int expired = 60;

    /**
     * 根据支付订单号获取支付订单
     *
     * @param payOrderNo：支付订单号
     * @return com.msb.pay.model.entity.PayOrder
     * @author peng.xy
     * @date 2022/6/9
     */
    public PayOrder getByPayOrderNo(String payOrderNo) {
        return super.lambdaQuery().eq(PayOrder::getPayOrderNo, payOrderNo)
                .oneOpt().orElseThrow(() -> new BizException("支付订单信息不存在"));
    }

    /**
     * 根据支付订单ID获取支付订单
     *
     * @param payOrderId：支付订单ID
     * @return com.msb.pay.model.entity.PayOrder
     * @author peng.xy
     * @date 2022/6/9
     */
    public PayOrder getByPayOrderId(Long payOrderId) {
        return super.lambdaQuery().eq(PayOrder::getId, payOrderId)
                .oneOpt().orElseThrow(() -> new BizException("支付订单信息不存在"));
    }

    /**
     * 获取交易订单Map
     *
     * @param payOrderIds：交易订单ID列表
     * @return java.util.Map<java.lang.Long, com.msb.pay.model.entity.PayOrder>
     * @author peng.xy
     * @date 2022/7/5
     */
    public Map<Long, PayOrder> getPayOrderMap(List<Long> payOrderIds) {
        if (CollectionUtils.isEmpty(payOrderIds)) {
            return Collections.emptyMap();
        }
        List<PayOrder> list = super.lambdaQuery().in(PayOrder::getId, payOrderIds).list();
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.toMap(PayOrder::getId, Function.identity()));
    }

    /**
     * 统一下单
     *
     * @param unifiedOrderDTO：下单参数
     * @return com.msb.pay.model.vo.UnifiedOrderVO
     * @author peng.xy
     * @date 2022/6/7
     */
    @Transactional(rollbackFor = Exception.class)
    public UnifiedOrderVO<? extends PayData> unifiedOrder(UnifiedOrderDTO unifiedOrderDTO) {
        log.info("统一下单参数：{}", unifiedOrderDTO);
        // 获取商户信息和应用信息
        AppInfo appInfo = appInfoService.getByCodeOrThrow(unifiedOrderDTO.getAppCode());
        MchInfo mchInfo = mchInfoService.getByPrimaryIdOrThrow(appInfo.getMchPrimaryId());
        // 签名校验、参数校验
        SignKit.signatureValidate(unifiedOrderDTO, appInfo.getSignKey());
        this.checkUnifiedOrder(unifiedOrderDTO, appInfo.getPayCodes());
        // 获取支付方式
        IPayCodeService payCodeService = ChannelContext.getPayCodeService(unifiedOrderDTO.getPayCode());
        // 发起支付请求
        UnifiedOrderRES<? extends PayData> unifiedOrderRes = payCodeService.payRequest(mchInfo, appInfo, unifiedOrderDTO);
        // 创建支付订单
        PayOrder payOrder = this.createPayOrderBody(mchInfo, appInfo, unifiedOrderDTO, unifiedOrderRes);
        payOrder.setExpiredTime(LocalDateTime.now().plusMinutes(expired));
        super.save(payOrder);
        // 设置签名，返回下单结果
        UnifiedOrderVO<PayData> unifiedOrderVO = modelConvert.toUnifiedOrderVO(unifiedOrderRes);
        unifiedOrderVO.setPayOrderId(String.valueOf(payOrder.getId()));
        SignKit.setSign(unifiedOrderVO, appInfo.getSignKey());
        return unifiedOrderVO;
    }

    /**
     * 统一下单前的参数校验
     *
     * @param unifiedOrderDTO：下单参数
     * @param payCodes：支付应用支持的支付方式
     * @author peng.xy
     * @date 2022/6/7
     */
    private void checkUnifiedOrder(UnifiedOrderDTO unifiedOrderDTO, String payCodes) {
        // 判断是否支持此支付方式
        PayCodeEnum payCodeEnum = IDict.getByCode(PayCodeEnum.class, unifiedOrderDTO.getPayCode());
        boolean match = Arrays.stream(payCodes.split(",")).anyMatch(payCode -> payCode.equals(payCodeEnum.getCode()));
        BizAssert.isTrue(match, "应用不支持[" + payCodeEnum.getText() + "]支付方式");
        // 判断支付订单号是否存在
        Integer count = super.lambdaQuery().eq(PayOrder::getPayOrderNo, unifiedOrderDTO.getPayOrderNo()).count();
        BizAssert.isTrue(count == 0, "支付订单号[" + unifiedOrderDTO.getPayOrderNo() + "]已存在");
    }

    /**
     * 创建支付订单
     *
     * @param mchInfo：商户信息
     * @param appInfo：应用信息
     * @param unifiedOrderDTO：下单参数
     * @param unifiedOrderRes：下单响应
     * @return com.msb.pay.model.entity.PayOrder
     * @author peng.xy
     * @date 2022/6/6
     */
    private PayOrder createPayOrderBody(MchInfo mchInfo, AppInfo appInfo, UnifiedOrderDTO unifiedOrderDTO, UnifiedOrderRES<? extends PayData> unifiedOrderRes) {
        return new PayOrder()
                .setMchPrimaryId(mchInfo.getId())
                .setAppPrimaryId(appInfo.getId())
                .setPayCode(unifiedOrderDTO.getPayCode())
                .setPayOrderNo(unifiedOrderDTO.getPayOrderNo())
                .setSubject(unifiedOrderDTO.getSubject())
                .setBody(unifiedOrderDTO.getBody())
                .setAmount(unifiedOrderDTO.getAmount())
                .setRefundAmount(BigDecimal.ZERO)
                .setRefundTimes(0)
                .setPayStatus(PayStatusEnum.UNPAID.getCode())
                .setNotifyStatus(NotifyStatusEnum.UN_NOTIFY.getCode())
                .setNotifyUrl(unifiedOrderDTO.getNotifyUrl())
                .setReturnUrl(unifiedOrderDTO.getReturnUrl())
                .setChannelRequest(unifiedOrderRes.getChannelRequest())
                .setChannelResponse(unifiedOrderRes.getChannelResponse());
    }

    /**
     * 接收支付回调
     *
     * @param request：请求对象
     * @param payOrderNo：支付订单号
     * @return boolean
     * @author peng.xy
     * @date 2022/6/9
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean payNotify(HttpServletRequest request, String payOrderNo) {
        // 获取支付订单，商户信息，应用信息
        PayOrder payOrder = this.getByPayOrderNo(payOrderNo);
        MchInfo mchInfo = mchInfoService.getByPrimaryIdOrThrow(payOrder.getMchPrimaryId());
        AppInfo appInfo = appInfoService.getByPrimaryIdOrThrow(payOrder.getAppPrimaryId());
        Long payOrderId = payOrder.getId();
        // 获取支付通道
        IPaymentService paymentService = ChannelContext.getIPaymentService(mchInfo.getMchCode());
        // 解析回调参数
        PayNotifyRES payNotifyRes = paymentService.payNotify(request, mchInfo);
        boolean update = this.updateChannelPayNotify(payOrderId, payNotifyRes);
        // 通知业务系统
        PayOrderDelayNotifyMessage message = new PayOrderDelayNotifyMessage().setPayOrderId(payOrder.getId());
        notifyService.payOrderNotify(mchInfo, appInfo, this.getByPayOrderId(payOrderId), message);
        return update;
    }

    /**
     * 更新渠道支付回调通知信息
     *
     * @param payOrderId：支付订单ID
     * @param payNotifyRes：通知信息
     * @return boolean
     * @author peng.xy
     * @date 2022/6/9
     */
    private boolean updateChannelPayNotify(Long payOrderId, PayNotifyRES payNotifyRes) {
        // 更新支付订单
        Boolean paySuccess = payNotifyRes.getPaySuccess();
        List<Integer> status = Arrays.asList(PayStatusEnum.UNPAID.getCode(), PayStatusEnum.PAY_FAIL.getCode());
        return super.lambdaUpdate().eq(PayOrder::getId, payOrderId)
                .in(PayOrder::getPayStatus, status)
                .set(PayOrder::getPayStatus, paySuccess ? PayStatusEnum.PAY_SUCCESS.getCode() : PayStatusEnum.PAY_FAIL.getCode())
                .set(PayOrder::getChannelPayOrderNo, payNotifyRes.getChannelPayOrderNo())
                .set(PayOrder::getChannelUserId, payNotifyRes.getChannelUserId())
                .set(PayOrder::getChannelNotify, payNotifyRes.getChannelNotify())
                .set(PayOrder::getSuccessTime, payNotifyRes.getSuccessTime())
                .update();
    }

    /**
     * 支付成功回调确认
     *
     * @param payNotifyConfirmDTO：确认参数
     * @return boolean
     * @author peng.xy
     * @date 2022/6/10
     */
    public boolean payNotifyConfirm(PayNotifyConfirmDTO payNotifyConfirmDTO) {
        log.info("接收支付确认的MQ消息：{}", payNotifyConfirmDTO);
        // 获取应用信息
        AppInfo appInfo = appInfoService.getByCodeOrThrow(payNotifyConfirmDTO.getAppCode());
        // 签名校验
        SignKit.signatureValidate(payNotifyConfirmDTO, appInfo.getSignKey());
        return super.lambdaUpdate().eq(PayOrder::getPayOrderNo, payNotifyConfirmDTO.getPayOrderNo()).set(PayOrder::getNotifyStatus, NotifyStatusEnum.HAVE_RESPONSE.getCode()).update();
    }

    /**
     * 累加支付订单的退款金额，但不能超过订单实付金额
     *
     * @param payOrderId：支付订单ID
     * @param cumulativeAmount：累加的退款金额
     * @return boolean
     * @author peng.xy
     * @date 2022/6/10
     */
    public boolean cumulativeRefundAmount(Long payOrderId, BigDecimal cumulativeAmount) {
        boolean cumulative = baseMapper.cumulativeRefundAmount(payOrderId, cumulativeAmount);
        if (cumulative) {
            PayOrder payOrder = this.getByPayOrderId(payOrderId);
            int compare = payOrder.getAmount().compareTo(payOrder.getRefundAmount());
            if (compare == 0) {
                super.lambdaUpdate().eq(PayOrder::getId, payOrderId).set(PayOrder::getPayStatus, PayStatusEnum.FULL_REFUND.getCode()).update();
            } else if (compare > 0) {
                super.lambdaUpdate().eq(PayOrder::getId, payOrderId).set(PayOrder::getPayStatus, PayStatusEnum.PORTION_REFUND.getCode()).update();
            }
        }
        return cumulative;
    }

    /**
     * 查询支付订单分页列表
     *
     * @param payOrderPageDTO：参数
     * @return Page<com.msb.pay.model.vo.PayOrderPageVO>
     * @author peng.xy
     * @date 2022/7/5
     */
    @Transactional(readOnly = true)
    public Page<PayOrderPageVO> page(PayOrderPageDTO payOrderPageDTO) {
        Page<PayOrder> entityPage = super.lambdaQuery()
                .eq(Objects.nonNull(payOrderPageDTO.getMchPrimaryId()), PayOrder::getMchPrimaryId, payOrderPageDTO.getMchPrimaryId())
                .eq(Objects.nonNull(payOrderPageDTO.getAppPrimaryId()), PayOrder::getAppPrimaryId, payOrderPageDTO.getAppPrimaryId())
                .like(StringUtils.isNotBlank(payOrderPageDTO.getPayOrderNo()), PayOrder::getPayOrderNo, payOrderPageDTO.getPayOrderNo())
                .eq(StringUtils.isNotBlank(payOrderPageDTO.getPayCode()), PayOrder::getPayCode, payOrderPageDTO.getPayCode())
                .eq(Objects.nonNull(payOrderPageDTO.getPayStatus()), PayOrder::getPayStatus, payOrderPageDTO.getPayStatus())
                .eq(Objects.nonNull(payOrderPageDTO.getNotifyStatus()), PayOrder::getNotifyStatus, payOrderPageDTO.getNotifyStatus())
                .ge(Objects.nonNull(payOrderPageDTO.getStartTime()), PayOrder::getCreateTime, payOrderPageDTO.getStartTime())
                .le(Objects.nonNull(payOrderPageDTO.getEndTime()), PayOrder::getCreateTime, payOrderPageDTO.getEndTime())
                .orderByDesc(PayOrder::getId)
                .page(payOrderPageDTO.page());
        Page<PayOrderPageVO> voPage = payOrderConvert.toPayOrderVOPage(entityPage);
        List<PayOrderPageVO> voList = voPage.getRecords();
        if (CollectionUtils.isEmpty(voList)) {
            return voPage;
        }
        List<Long> mchPrimaryIds = ListUtil.convertDistinct(voList, PayOrderPageVO::getMchPrimaryId);
        List<Long> appPrimaryIds = ListUtil.convertDistinct(voList, PayOrderPageVO::getAppPrimaryId);
        Map<Long, MchInfo> mchInfoMap = mchInfoService.getMchInfoMap(mchPrimaryIds);
        Map<Long, AppInfo> appInfoMap = appInfoService.getAppInfoMap(appPrimaryIds);
        voList.forEach(payOrderPageVO -> {
            MchInfo mchInfo = mchInfoMap.get(payOrderPageVO.getMchPrimaryId());
            payOrderPageVO.setMchInfo(mchInfoConvert.toMchSimpleVO(mchInfo));
            AppInfo appInfo = appInfoMap.get(payOrderPageVO.getAppPrimaryId());
            payOrderPageVO.setAppInfo(appInfoConvert.toAppSimpleInfoVO(appInfo));
        });
        return voPage;
    }

    /**
     * 交易订单详情
     *
     * @param payOrderId：支付订单ID
     * @return com.msb.pay.model.vo.PayOrderVO
     * @author peng.xy
     * @date 2022/6/23
     */
    @Transactional(readOnly = true)
    public PayOrderVO tradeQuery(Long payOrderId) {
        // 获取支付订单信息
        PayOrder payOrder = this.getByPayOrderId(payOrderId);
        PayOrderVO payOrderVO = payOrderConvert.toPayOrderVO(payOrder);
        // 获取商户信息
        MchInfo mchInfo = mchInfoService.getByPrimaryIdOrNull(payOrder.getMchPrimaryId());
        payOrderVO.setMchInfo(mchInfoConvert.toMchSimpleVO(mchInfo));
        // 获取应用信息
        AppInfo appInfo = appInfoService.getByPrimaryIdOrNull(payOrder.getAppPrimaryId());
        payOrderVO.setAppInfo(appInfoConvert.toAppSimpleInfoVO(appInfo));
        return payOrderVO;
    }

    /**
     * 预支付下单
     *
     * @param prepaymentDTO：预支付参数
     * @return com.msb.pay.model.vo.PrepaymentVO
     * @author peng.xy
     * @date 2022/7/6
     */
    @Transactional(rollbackFor = Exception.class)
    public PrepaymentVO prepayment(PrepaymentDTO prepaymentDTO) {
        // 判断支付订单号是否存在
        Integer count = super.lambdaQuery().eq(PayOrder::getPayOrderNo, prepaymentDTO.getPayOrderNo()).count();
        BizAssert.isTrue(count == 0, "支付订单号[" + prepaymentDTO.getPayOrderNo() + "]已存在");
        // 创建预支付订单
        PayOrder prepayOrder = new PayOrder()
                .setPayOrderNo(prepaymentDTO.getPayOrderNo())
                .setSubject(prepaymentDTO.getSubject())
                .setBody(prepaymentDTO.getBody())
                .setAmount(prepaymentDTO.getAmount())
                .setNotifyUrl(prepaymentDTO.getNotifyUrl())
                .setReturnUrl(prepaymentDTO.getReturnUrl())
                .setPayStatus(PayStatusEnum.PREPAY.getCode())
                .setNotifyStatus(NotifyStatusEnum.UN_NOTIFY.getCode())
                .setExpiredTime(LocalDateTime.now().plusMinutes(expired));
        // 如果回调页面地址为空，则取默认的收银台支付结果页
        if (StringUtils.isBlank(prepaymentDTO.getReturnUrl())) {
            prepayOrder.setReturnUrl(payCenterConfig.getWapResultUrl().concat(prepaymentDTO.getPayOrderNo()));
        }
        super.save(prepayOrder);
        return new PrepaymentVO()
                .setPayOrderId(String.valueOf(prepayOrder.getId()))
                .setPayOrderNo(prepaymentDTO.getPayOrderNo())
                .setWapCashierUrl(payCenterConfig.getWapCashierUrl() + prepaymentDTO.getPayOrderNo());
    }

    /**
     * 预支付订单信息
     *
     * @param payOrderNo：支付订单号
     * @return com.msb.pay.model.vo.PrepayOrderVO
     * @author peng.xy
     * @date 2022/7/6
     */
    public PrepayOrderVO prepayOrder(String payOrderNo) {
        PayOrder payOrder = this.getByPayOrderNo(payOrderNo);
        Integer payStatus = payOrder.getPayStatus();
        if (!EqualsUtil.anyEquals(payStatus, PayStatusEnum.PREPAY.getCode(), PayStatusEnum.UNPAID.getCode())) {
            throw new BizException("预支付订单状态有误");
        }
        LocalDateTime expiredTime = payOrder.getExpiredTime();
        if (expiredTime != null && LocalDateTime.now().compareTo(expiredTime) > 0) {
            throw new BizException("订单已超时，请重新下单");
        }
        // 获取支付应用
        PrepayOrderVO prepayOrder = payOrderConvert.toPrepayOrderVO(payOrder);
        prepayOrder.setPrepayWxApp(appInfoService.getPrepayApp(payCenterConfig.getPrepayWxAppCode()));
        prepayOrder.setPrepayAliApp(appInfoService.getPrepayApp(payCenterConfig.getPrepayAliAppCode()));
        return prepayOrder;
    }

    /**
     * 收银台支付
     *
     * @param cashierPayDTO：收银台支付参数
     * @return com.msb.pay.model.bo.UnifiedOrderRES<? extends com.msb.pay.model.paydata.PayData>
     * @author peng.xy
     * @date 2022/7/7
     */
    @Transactional(rollbackFor = Exception.class)
    public UnifiedOrderVO<? extends PayData> cashierPay(CashierPayDTO cashierPayDTO) {
        log.info("收银台支付参数：{}", cashierPayDTO);
        // 获取原订单信息
        PayOrder originalPayOrder = this.getByPayOrderNo(cashierPayDTO.getPayOrderNo());
        if (!EqualsUtil.anyEquals(originalPayOrder.getPayStatus(), PayStatusEnum.PREPAY.getCode(), PayStatusEnum.UNPAID.getCode())) {
            throw new BizException("支付订单状态有误");
        }
        // 获取商户信息
        AppInfo appInfo = appInfoService.getByCodeOrThrow(cashierPayDTO.getAppCode());
        // 获取应用信息
        MchInfo mchInfo = mchInfoService.getByPrimaryIdOrThrow(appInfo.getMchPrimaryId());
        // 获取支付方式
        IPayCodeService payCodeService = ChannelContext.getPayCodeService(cashierPayDTO.getPayCode());
        // 发起支付请求
        UnifiedOrderDTO unifiedOrderDTO = new UnifiedOrderDTO()
                .setAppCode(cashierPayDTO.getAppCode())
                .setPayCode(cashierPayDTO.getPayCode())
                .setPayOrderNo(cashierPayDTO.getPayOrderNo())
                .setAmount(originalPayOrder.getAmount())
                .setClientIp(ServletUtil.getClientIP())
                .setSubject(originalPayOrder.getSubject())
                .setBody(originalPayOrder.getBody())
                .setNotifyUrl(originalPayOrder.getNotifyUrl())
                .setReturnUrl(originalPayOrder.getReturnUrl())
                .setChannelUser(cashierPayDTO.getChannelUser());
        UnifiedOrderRES<? extends PayData> unifiedOrderRes = payCodeService.payRequest(mchInfo, appInfo, unifiedOrderDTO);
        // 保存订单信息
        PayOrder payOrder = this.createPayOrderBody(mchInfo, appInfo, unifiedOrderDTO, unifiedOrderRes);
        payOrder.setId(originalPayOrder.getId());
        super.updateById(payOrder);
        // 设置签名，返回下单结果
        UnifiedOrderVO<PayData> unifiedOrderVO = modelConvert.toUnifiedOrderVO(unifiedOrderRes);
        unifiedOrderVO.setPayOrderId(String.valueOf(payOrder.getId()));
        return unifiedOrderVO;
    }

    /**
     * 查询支付结果
     *
     * @param payOrderNo：支付订单号
     * @return com.msb.pay.model.vo.PayResultVO
     * @author peng.xy
     * @date 2022/7/7
     */
    public PayResultVO payResult(String payOrderNo) {
        PayOrder payOrder = this.getByPayOrderNo(payOrderNo);
        return payOrderConvert.toPayResultVO(payOrder);
    }

}

