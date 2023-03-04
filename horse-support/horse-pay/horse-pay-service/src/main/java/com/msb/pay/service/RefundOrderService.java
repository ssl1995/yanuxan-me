package com.msb.pay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.common.exception.BizException;
import com.msb.framework.common.utils.EqualsUtil;
import com.msb.framework.common.utils.ListUtil;
import com.msb.framework.web.result.BizAssert;
import com.msb.pay.channel.IPaymentService;
import com.msb.pay.channel.context.ChannelContext;
import com.msb.pay.enums.NotifyStatusEnum;
import com.msb.pay.enums.PayStatusEnum;
import com.msb.pay.enums.RefundStatusEnum;
import com.msb.pay.kit.SignKit;
import com.msb.pay.mapper.RefundOrderMapper;
import com.msb.pay.model.bo.ApplyRefundRES;
import com.msb.pay.model.bo.RefundNotifyRES;
import com.msb.pay.model.dto.ApplyRefundDTO;
import com.msb.pay.model.dto.RefundNotifyConfirmDTO;
import com.msb.pay.model.dto.RefundOrderPageDTO;
import com.msb.pay.model.entity.AppInfo;
import com.msb.pay.model.entity.MchInfo;
import com.msb.pay.model.entity.PayOrder;
import com.msb.pay.model.entity.RefundOrder;
import com.msb.pay.model.vo.ApplyRefundVO;
import com.msb.pay.model.vo.RefundOrderPageVO;
import com.msb.pay.model.vo.RefundOrderVO;
import com.msb.pay.mq.model.RefundOrderDelayNotifyMessage;
import com.msb.pay.service.convert.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 退款订单表(RefundOrder)表服务实现类
 *
 * @author makejava
 * @date 2022-06-06 10:42:46
 */
@Slf4j
@Service("refundOrderService")
public class RefundOrderService extends ServiceImpl<RefundOrderMapper, RefundOrder> {

    @Resource
    private MchInfoService mchInfoService;
    @Resource
    private AppInfoService appInfoService;
    @Resource
    private PayOrderService payOrderService;
    @Lazy
    @Resource
    private NotifyService notifyService;

    @Resource
    private RefundOrderConvert refundOrderConvert;
    @Resource
    private PayOrderConvert payOrderConvert;
    @Resource
    private MchInfoConvert mchInfoConvert;
    @Resource
    private AppInfoConvert appInfoConvert;
    @Resource
    private ModelConvert modelConvert;

    /**
     * 根据退款订单号获取退款订单
     *
     * @param refundOrderNo：退款订单号
     * @return com.msb.pay.model.entity.RefundOrder
     * @author peng.xy
     * @date 2022/6/13
     */
    public RefundOrder getByRefundOrderNo(String refundOrderNo) {
        return super.lambdaQuery().eq(RefundOrder::getRefundOrderNo, refundOrderNo)
                .oneOpt().orElseThrow(() -> new BizException("退款订单信息不存在"));
    }

    /**
     * 根据退款订单ID获取退款订单
     *
     * @param refundOrderId：退款订单ID
     * @return com.msb.pay.model.entity.RefundOrder
     * @author peng.xy
     * @date 2022/6/13
     */
    public RefundOrder getByRefundOrderId(Long refundOrderId) {
        return super.lambdaQuery().eq(RefundOrder::getId, refundOrderId)
                .oneOpt().orElseThrow(() -> new BizException("退款订单信息不存在"));
    }

    /**
     * 申请退款
     *
     * @param applyRefundDTO：申请退款参数
     * @return com.msb.pay.model.vo.ApplyRefundVO
     * @author peng.xy
     * @date 2022/6/11
     */
    @Transactional(rollbackFor = Exception.class)
    public ApplyRefundVO applyRefund(ApplyRefundDTO applyRefundDTO) {
        // 获取支付订单信息
        PayOrder payOrder = payOrderService.getByPayOrderNo(applyRefundDTO.getPayOrderNo());
        // 获取商户信息和应用信息
        AppInfo appInfo = appInfoService.getByCodeOrThrow(applyRefundDTO.getAppCode());
        MchInfo mchInfo = mchInfoService.getByPrimaryIdOrThrow(appInfo.getMchPrimaryId());
        // 签名校验、参数校验
        SignKit.signatureValidate(applyRefundDTO, appInfo.getSignKey());
        this.checkRefundApply(payOrder, applyRefundDTO);
        // 获取支付通道
        IPaymentService paymentService = ChannelContext.getIPaymentService(mchInfo.getMchCode());
        ApplyRefundRES applyRefundRes = paymentService.refundRequest(mchInfo, appInfo, payOrder, applyRefundDTO);
        // 创建退款订单
        RefundOrder refundOrder = this.createRefundOrderBody(mchInfo, appInfo, payOrder, applyRefundDTO, applyRefundRes);
        super.save(refundOrder);
        // 设置签名，返回申请结果
        ApplyRefundVO applyRefundVO = modelConvert.toApplyRefundVO(applyRefundRes);
        applyRefundVO.setRefundOrderId(String.valueOf(refundOrder.getId()));
        SignKit.setSign(applyRefundVO, appInfo.getSignKey());
        // 退款成功，发送延迟通知
        if (Objects.equals(RefundStatusEnum.REFUND_SUCCESS, applyRefundRes.getRefundStatus())) {
            RefundNotifyRES refundNotifyRes = new RefundNotifyRES().setRefundSuccess(true).setSuccessTime(LocalDateTime.now());
            this.updateChannelRefundNotify(refundOrder.getId(), payOrder.getId(), refundOrder.getRefundAmount(), refundNotifyRes);
            RefundOrderDelayNotifyMessage message = new RefundOrderDelayNotifyMessage().setRefundOrderId(refundOrder.getId());
            notifyService.refundOrderDelayNotify(message);
        }
        return applyRefundVO;
    }

    /**
     * 检查退款申请
     *
     * @param payOrder：退款单
     * @param applyRefundDTO：退款申请参数
     * @author peng.xy
     * @date 2022/6/11
     */
    private void checkRefundApply(PayOrder payOrder, ApplyRefundDTO applyRefundDTO) {
        // 判断支付订单状态
        if (EqualsUtil.anyEqualsIDict(payOrder.getPayStatus(), PayStatusEnum.UNPAID, PayStatusEnum.CLOSE, PayStatusEnum.PAY_FAIL, PayStatusEnum.FULL_REFUND)) {
            throw new BizException("支付订单状态有误");
        }
        // 校验退款金额
        BigDecimal totalRefundAmount = payOrder.getRefundAmount().add(applyRefundDTO.getRefundAmount());
        if (totalRefundAmount.compareTo(payOrder.getAmount()) > 0) {
            throw new BizException("退款金额不能超过支付金额");
        }
        // 判断退款订单号是否存在
        Integer count = super.lambdaQuery().eq(RefundOrder::getRefundOrderNo, applyRefundDTO.getRefundOrderNo()).count();
        BizAssert.isTrue(count == 0, "退款订单号[" + applyRefundDTO.getRefundOrderNo() + "]已存在");
    }

    /**
     * 创建退款订单信息
     *
     * @param mchInfo：商户信息
     * @param appInfo：应用信息
     * @param payOrder：支付订单
     * @param applyRefundDTO：申请参数
     * @param applyRefundRes：申请响应
     * @return com.msb.pay.model.entity.RefundOrder
     * @author peng.xy
     * @date 2022/6/11
     */
    private RefundOrder createRefundOrderBody(MchInfo mchInfo, AppInfo appInfo, PayOrder payOrder, ApplyRefundDTO applyRefundDTO, ApplyRefundRES applyRefundRes) {
        return new RefundOrder()
                .setMchPrimaryId(mchInfo.getId())
                .setAppPrimaryId(appInfo.getId())
                .setPayOrderId(payOrder.getId())
                .setRefundOrderNo(applyRefundDTO.getRefundOrderNo())
                .setRefundAmount(applyRefundDTO.getRefundAmount())
                .setRefundReason(applyRefundDTO.getRefundReason())
                .setRefundStatus(RefundStatusEnum.APPLY.getCode())
                .setNotifyStatus(NotifyStatusEnum.UN_NOTIFY.getCode())
                .setNotifyUrl(applyRefundDTO.getNotifyUrl())
                .setChannelRefundOrderNo(applyRefundRes.getChannelRefundOrderNo())
                .setChannelRequest(applyRefundRes.getChannelRequest())
                .setChannelResponse(applyRefundRes.getChannelResponse());
    }

    /**
     * 接收退款回调
     *
     * @param request：请求对象
     * @param refundOrderNo：退款订单号
     * @return boolean
     * @author peng.xy
     * @date 2022/6/13
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean refundNotify(HttpServletRequest request, String refundOrderNo) {
        // 获取退款订单，支付订单，商户信息，应用信息
        RefundOrder refundOrder = this.getByRefundOrderNo(refundOrderNo);
        PayOrder payOrder = payOrderService.getByPayOrderId(refundOrder.getPayOrderId());
        AppInfo appInfo = appInfoService.getByPrimaryIdOrThrow(payOrder.getAppPrimaryId());
        MchInfo mchInfo = mchInfoService.getByPrimaryIdOrThrow(refundOrder.getMchPrimaryId());
        Long refundOrderId = refundOrder.getId();
        // 获取支付通道
        IPaymentService paymentService = ChannelContext.getIPaymentService(mchInfo.getMchCode());
        // 解析回调参数
        RefundNotifyRES refundNotifyRes = paymentService.refundNotify(request, mchInfo);
        boolean update = this.updateChannelRefundNotify(refundOrderId, payOrder.getId(), refundOrder.getRefundAmount(), refundNotifyRes);
        // 通知业务系统
        RefundOrderDelayNotifyMessage message = new RefundOrderDelayNotifyMessage().setRefundOrderId(refundOrderId);
        notifyService.refundOrderNotify(mchInfo, appInfo, payOrder, this.getByRefundOrderId(refundOrderId), message);
        return update;
    }

    /**
     * 更新渠道退款回调通知信息
     *
     * @param refundOrderId：退款订单ID
     * @param payOrderId：支付订单ID
     * @param refundAmount：退款金额
     * @param refundNotifyRes：通知信息
     * @return boolean
     * @author peng.xy
     * @date 2022/6/13
     */
    private boolean updateChannelRefundNotify(Long refundOrderId, Long payOrderId, BigDecimal refundAmount, RefundNotifyRES refundNotifyRes) {
        payOrderService.cumulativeRefundAmount(payOrderId, refundAmount);
        Boolean refundSuccess = refundNotifyRes.getRefundSuccess();
        List<Integer> status = Arrays.asList(RefundStatusEnum.APPLY.getCode(), RefundStatusEnum.REFUND_FAIL.getCode());
        return super.lambdaUpdate().eq(RefundOrder::getId, refundOrderId)
                .in(RefundOrder::getRefundStatus, status)
                .set(RefundOrder::getRefundStatus, refundSuccess ? RefundStatusEnum.REFUND_SUCCESS.getCode() : RefundStatusEnum.REFUND_FAIL.getCode())
                .set(RefundOrder::getChannelRefundOrderNo, refundNotifyRes.getChannelRefundOrderNo())
                .set(RefundOrder::getChannelNotify, refundNotifyRes.getChannelNotify())
                .set(RefundOrder::getSuccessTime, refundNotifyRes.getSuccessTime())
                .update();
    }

    /**
     * 退款成功回调确认
     *
     * @param refundNotifyConfirmDTO：确认参数
     * @return boolean
     * @author peng.xy
     * @date 2022/6/13
     */
    public boolean refundNotifyConfirm(RefundNotifyConfirmDTO refundNotifyConfirmDTO) {
        log.info("接收退款确认的MQ消息：{}", refundNotifyConfirmDTO);
        // 获取应用信息
        AppInfo appInfo = appInfoService.getByCodeOrThrow(refundNotifyConfirmDTO.getAppCode());
        // 签名校验
        SignKit.signatureValidate(refundNotifyConfirmDTO, appInfo.getSignKey());
        return super.lambdaUpdate().eq(RefundOrder::getRefundOrderNo, refundNotifyConfirmDTO.getRefundOrderNo()).set(RefundOrder::getNotifyStatus, NotifyStatusEnum.HAVE_RESPONSE.getCode()).update();
    }

    /**
     * 查询退款订单
     *
     * @param refundOrderId：退款订单ID
     * @return com.msb.pay.model.vo.RefundOrderChannelVO
     * @author peng.xy
     * @date 2022/6/23
     */
    @Transactional(readOnly = true)
    public RefundOrderVO refundQuery(Long refundOrderId) {
        // 获取退款订单信息
        RefundOrder refundOrder = this.getByRefundOrderId(refundOrderId);
        RefundOrderVO refundOrderVO = refundOrderConvert.toRefundOrderVO(refundOrder);
        // 获取支付订单信息
        PayOrder payOrder = payOrderService.getByPayOrderId(refundOrder.getPayOrderId());
        refundOrderVO.setPayOrderInfo(payOrderConvert.toPayOrderSimpleVO(payOrder));
        // 获取商户信息
        MchInfo mchInfo = mchInfoService.getByPrimaryId(refundOrder.getMchPrimaryId());
        refundOrderVO.setMchInfo(mchInfoConvert.toMchSimpleVO(mchInfo));
        // 获取应用信息
        AppInfo appInfo = appInfoService.getByPrimaryId(refundOrder.getAppPrimaryId());
        refundOrderVO.setAppInfo(appInfoConvert.toAppSimpleInfoVO(appInfo));
        return refundOrderVO;
    }

    /**
     * 退款订单分页列表
     *
     * @param refundOrderPageDTO：分页参数
     * @return Page<com.msb.pay.model.vo.RefundOrderPageVO>
     * @author peng.xy
     * @date 2022/7/5
     */
    @Transactional(readOnly = true)
    public Page<RefundOrderPageVO> page(RefundOrderPageDTO refundOrderPageDTO) {
        // 获取退款订单分页信息
        Page<RefundOrder> entityPage = refundOrderPageDTO.page();
        List<RefundOrder> refundOrderList = baseMapper.refundOrderPage(refundOrderPageDTO, entityPage);
        entityPage.setRecords(refundOrderList);
        Page<RefundOrderPageVO> voPage = refundOrderConvert.toRefundOrderPage(entityPage);
        List<RefundOrderPageVO> voList = voPage.getRecords();
        if (CollectionUtils.isEmpty(voList)) {
            return voPage;
        }
        List<Long> mchPrimaryIds = ListUtil.convertDistinct(voList, RefundOrderPageVO::getMchPrimaryId);
        List<Long> appPrimaryIds = ListUtil.convertDistinct(voList, RefundOrderPageVO::getAppPrimaryId);
        List<Long> payOrderIds = ListUtil.convertDistinct(voList, RefundOrderPageVO::getPayOrderId);
        Map<Long, MchInfo> mchInfoMap = mchInfoService.getMchInfoMap(mchPrimaryIds);
        Map<Long, AppInfo> appInfoMap = appInfoService.getAppInfoMap(appPrimaryIds);
        Map<Long, PayOrder> payOrderMap = payOrderService.getPayOrderMap(payOrderIds);
        voList.forEach(refundOrderPageVO -> {
            MchInfo mchInfo = mchInfoMap.get(refundOrderPageVO.getMchPrimaryId());
            refundOrderPageVO.setMchInfo(mchInfoConvert.toMchSimpleVO(mchInfo));
            AppInfo appInfo = appInfoMap.get(refundOrderPageVO.getAppPrimaryId());
            refundOrderPageVO.setAppInfo(appInfoConvert.toAppSimpleInfoVO(appInfo));
            PayOrder payOrder = payOrderMap.get(refundOrderPageVO.getPayOrderId());
            refundOrderPageVO.setPayOrderInfo(payOrderConvert.toPayOrderSimpleVO(payOrder));
        });
        return voPage;
    }

}

