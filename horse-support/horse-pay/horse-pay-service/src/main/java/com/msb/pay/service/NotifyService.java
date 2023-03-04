package com.msb.pay.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpUtil;
import com.msb.framework.common.exception.BizException;
import com.msb.framework.common.model.IDict;
import com.msb.framework.common.mq.MqConfig;
import com.msb.pay.enums.NotifyStatusEnum;
import com.msb.pay.kit.SignKit;
import com.msb.pay.model.constant.PayCenterConst;
import com.msb.pay.model.dto.PayNotifyDTO;
import com.msb.pay.model.dto.RefundNotifyDTO;
import com.msb.pay.model.entity.AppInfo;
import com.msb.pay.model.entity.MchInfo;
import com.msb.pay.model.entity.PayOrder;
import com.msb.pay.model.entity.RefundOrder;
import com.msb.pay.mq.model.CertSyncNotifyMessage;
import com.msb.pay.mq.model.PayCenterDelayLevel;
import com.msb.pay.mq.model.PayOrderDelayNotifyMessage;
import com.msb.pay.mq.model.RefundOrderDelayNotifyMessage;
import com.msb.pay.mq.topic.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * 异步通知业务系统实现类
 *
 * @author makejava
 * @since 2022-03-24 18:30:16
 */
@Slf4j
@Async
@Service("notifyService")
public class NotifyService {

    @Resource
    private MchInfoService mchInfoService;
    @Resource
    private AppInfoService appInfoService;
    @Resource
    private PayOrderService payOrderService;
    @Resource
    private RefundOrderService refundOrderService;
    @Resource
    private PayNotifyMessageTopic payNotifyMessageTopic;
    @Resource
    private PayOrderDelayNotifyTopic payOrderDelayNotifyTopic;
    @Resource
    private RefundNotifyMessageTopic refundNotifyMessageTopic;
    @Resource
    private RefundOrderDelayNotifyTopic refundOrderDelayNotifyTopic;
    @Resource
    private MchCertSyncNotifyTopic mchCertSyncNotifyTopic;
    @Resource
    private AppCertSyncNotifyTopic appCertSyncNotifyTopic;

    /**
     * 支付订单业务系统通知
     *
     * @param message：消息
     * @author peng.xy
     * @date 2022/6/16
     */
    public void payOrderNotify(PayOrderDelayNotifyMessage message) {
        PayOrder payOrder = payOrderService.getByPayOrderId(message.getPayOrderId());
        MchInfo mchInfo = mchInfoService.getByPrimaryIdOrThrow(payOrder.getMchPrimaryId());
        AppInfo appInfo = appInfoService.getByPrimaryIdOrThrow(payOrder.getAppPrimaryId());
        this.payOrderNotify(mchInfo, appInfo, payOrder, message);
    }

    /**
     * 支付订单业务系统通知
     *
     * @param message：消息
     * @author peng.xy
     * @date 2022/6/16
     */
    public void payOrderNotify(MchInfo mchInfo, AppInfo appInfo, PayOrder payOrder, PayOrderDelayNotifyMessage message) {
        log.info("【支付订单通知】执行支付订单MQ延迟通知：{}", message);
        Long payOrderId = payOrder.getId();
        if (Objects.equals(payOrder.getNotifyStatus(), NotifyStatusEnum.HAVE_RESPONSE.getCode())) {
            log.info("【支付订单通知】支付订单已完成通知：{}", payOrder);
            return;
        }
        // 封装回调参数
        String createTime = payOrder.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String successTime = payOrder.getSuccessTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        PayNotifyDTO payNotifyDTO = new PayNotifyDTO()
                .setMchId(mchInfo.getMchId())
                .setMchCode(mchInfo.getMchCode())
                .setAppId(appInfo.getAppId())
                .setAppCode(appInfo.getAppCode())
                .setPayCode(payOrder.getPayCode())
                .setPayOrderId(String.valueOf(payOrder.getId()))
                .setPayOrderNo(payOrder.getPayOrderNo())
                .setChannelPayOrderNo(payOrder.getChannelPayOrderNo())
                .setPayStatus(payOrder.getPayStatus())
                .setSubject(payOrder.getSubject())
                .setBody(payOrder.getBody())
                .setAmount(payOrder.getAmount())
                .setCreateTime(createTime)
                .setSuccessTime(successTime);
        // 设置签名
        SignKit.setSign(payNotifyDTO, appInfo.getSignKey());
        boolean isNotify = false;
        String notifyUrl = payOrder.getNotifyUrl();
        if (StringUtils.isNotBlank(notifyUrl)) {
            try {
                // 调用接口通知业务系统
                String response = HttpUtil.post(notifyUrl, BeanUtil.beanToMap(payNotifyDTO));
                isNotify = StringUtils.equals(PayCenterConst.NOTIFY_RESPONSE.SUCCESS, response);
                Integer notifyStatus = NotifyStatusEnum.HAVE_NOTIFY.getCode();
                if (isNotify) {
                    notifyStatus = NotifyStatusEnum.HAVE_RESPONSE.getCode();
                }
                payOrderService.lambdaUpdate().eq(PayOrder::getId, payOrderId).set(PayOrder::getNotifyStatus, notifyStatus).update();
                log.info("【支付订单通知】调用支付订单业务系统接口通知：{}，{}", response, isNotify);
            } catch (Exception e) {
                isNotify = false;
                log.error("【支付订单通知】调用支付订单业务系统接口失败，支付订单ID：{}", payOrderId, e);
            }
        } else {
            try {
                // 发送MQ通知业务系统
                payNotifyMessageTopic.product(payNotifyDTO);
                payOrderService.lambdaUpdate().eq(PayOrder::getId, payOrderId).set(PayOrder::getNotifyStatus, NotifyStatusEnum.HAVE_NOTIFY.getCode()).update();
                log.info("【支付订单通知】发送支付订单业务系统MQ通知：{}", payNotifyDTO);
            } catch (Exception e) {
                isNotify = false;
                log.info("【支付订单通知】发送支付订单业务系统MQ失败", e);
            }
        }
        // 继续发送MQ延时消息
        if (!isNotify) {
            PayCenterDelayLevel currentDelayLevel = IDict.getByCode(PayCenterDelayLevel.class, message.getPayCenterDelayLevel());
            log.info("【支付订单通知】当前支付订单MQ延迟级别：{}", currentDelayLevel);
            PayCenterDelayLevel nextDelayLevel = null;
            if (currentDelayLevel != null) {
                nextDelayLevel = currentDelayLevel.getNext();
            } else {
                nextDelayLevel = PayCenterDelayLevel.LEVEL1;
            }
            log.info("【支付订单通知】下次支付订单MQ延迟级别：{}", nextDelayLevel);
            if (nextDelayLevel != null) {
                MqConfig mqConfig = new MqConfig().setHashKey(payOrder.getPayOrderNo()).setDelayLevel(nextDelayLevel.getLevel());
                PayOrderDelayNotifyMessage nextNotifyMessage = new PayOrderDelayNotifyMessage()
                        .setPayOrderId(payOrderId).setPayCenterDelayLevel(nextDelayLevel.getCode());
                log.info("【支付订单通知】发送下一次支付订单MQ延迟通知：{}，{}", mqConfig, nextNotifyMessage);
                payOrderDelayNotifyTopic.product(mqConfig, nextNotifyMessage);
            }
        }
    }

    /**
     * 退款订单延迟通知业务系统
     *
     * @param message：消息
     * @author peng.xy
     * @date 2022/6/16
     */
    public void refundOrderDelayNotify(RefundOrderDelayNotifyMessage message) {
        try {
            Thread.sleep(3000);
            log.info("退款订单延迟通知业务系统：{}", message);
            this.refundOrderNotify(message);
        } catch (InterruptedException e) {
            log.error("退款订单延迟通知业务系统失败", e);
            throw new BizException("退款订单延迟通知业务系统失败");
        }
    }

    /**
     * 退款订单业务系统通知
     *
     * @param message：消息
     * @author peng.xy
     * @date 2022/6/16
     */
    public void refundOrderNotify(RefundOrderDelayNotifyMessage message) {
        RefundOrder refundOrder = refundOrderService.getByRefundOrderId(message.getRefundOrderId());
        PayOrder payOrder = payOrderService.getByPayOrderId(refundOrder.getPayOrderId());
        MchInfo mchInfo = mchInfoService.getByPrimaryIdOrThrow(refundOrder.getMchPrimaryId());
        AppInfo appInfo = appInfoService.getByPrimaryIdOrThrow(refundOrder.getAppPrimaryId());
        this.refundOrderNotify(mchInfo, appInfo, payOrder, refundOrder, message);
    }

    /**
     * 退款订单业务系统通知
     *
     * @param message：消息
     * @author peng.xy
     * @date 2022/6/16
     */
    public void refundOrderNotify(MchInfo mchInfo, AppInfo appInfo, PayOrder payOrder, RefundOrder refundOrder, RefundOrderDelayNotifyMessage message) {
        log.info("【退款订单通知】执行退款订单MQ延迟通知：{}", message);
        Long refundOrderId = refundOrder.getId();
        if (Objects.equals(refundOrder.getNotifyStatus(), NotifyStatusEnum.HAVE_RESPONSE.getCode())) {
            log.info("【退款订单通知】退款订单已完成通知：{}", refundOrder);
            return;
        }
        // 封装回调参数
        String createTime = refundOrder.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String successTime = refundOrder.getSuccessTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        RefundNotifyDTO refundNotifyDTO = new RefundNotifyDTO()
                .setMchId(mchInfo.getMchId())
                .setMchCode(mchInfo.getMchCode())
                .setAppId(appInfo.getAppId())
                .setAppCode(appInfo.getAppCode())
                .setPayOrderId(String.valueOf(payOrder.getId()))
                .setPayOrderNo(payOrder.getPayOrderNo())
                .setRefundOrderId(String.valueOf(refundOrder.getId()))
                .setRefundOrderNo(refundOrder.getRefundOrderNo())
                .setChannelRefundOrderNo(refundOrder.getChannelRefundOrderNo())
                .setRefundStatus(refundOrder.getRefundStatus())
                .setRefundAmount(refundOrder.getRefundAmount())
                .setCreateTime(createTime)
                .setSuccessTime(successTime);
        // 设置签名
        SignKit.setSign(refundNotifyDTO, appInfo.getSignKey());
        boolean isNotify = false;
        String notifyUrl = refundOrder.getNotifyUrl();
        if (StringUtils.isNotBlank(notifyUrl)) {
            try {
                // 调用接口通知业务系统
                String response = HttpUtil.post(notifyUrl, BeanUtil.beanToMap(refundNotifyDTO));
                isNotify = StringUtils.equals(PayCenterConst.NOTIFY_RESPONSE.SUCCESS, response);
                Integer notifyStatus = NotifyStatusEnum.HAVE_NOTIFY.getCode();
                if (isNotify) {
                    notifyStatus = NotifyStatusEnum.HAVE_RESPONSE.getCode();
                }
                refundOrderService.lambdaUpdate().eq(RefundOrder::getId, refundOrder.getId()).set(RefundOrder::getNotifyStatus, notifyStatus).update();
                log.info("【退款订单通知】调用退款订单业务系统接口通知：{}，{}", response, isNotify);
            } catch (Exception e) {
                isNotify = false;
                log.error("【退款订单通知】调用退款订单业务系统接口失败，退款订单ID：{}", refundOrderId, e);
            }
        } else {
            try {
                // 发送MQ消息通知业务系统
                refundNotifyMessageTopic.product(refundNotifyDTO);
                refundOrderService.lambdaUpdate().eq(RefundOrder::getId, refundOrder.getId()).set(RefundOrder::getNotifyStatus, NotifyStatusEnum.HAVE_NOTIFY.getCode()).update();
                log.info("【退款订单通知】发送退款订单业务系统MQ通知：{}", refundNotifyDTO);
            } catch (Exception e) {
                isNotify = false;
                log.info("【退款订单通知】发送退款订单业务系统MQ失败", e);
            }
        }
        // 继续发送MQ延时消息
        if (!isNotify) {
            PayCenterDelayLevel currentDelayLevel = IDict.getByCode(PayCenterDelayLevel.class, message.getPayCenterDelayLevel());
            log.info("【退款订单通知】当前退款订单MQ延迟级别：{}", currentDelayLevel);
            PayCenterDelayLevel nextDelayLevel = null;
            if (currentDelayLevel != null) {
                nextDelayLevel = currentDelayLevel.getNext();
            } else {
                nextDelayLevel = PayCenterDelayLevel.LEVEL1;
            }
            log.info("【退款订单通知】下次退款订单MQ延迟级别：{}", nextDelayLevel);
            if (nextDelayLevel != null) {
                MqConfig mqConfig = new MqConfig().setHashKey(refundOrder.getRefundOrderNo()).setDelayLevel(nextDelayLevel.getLevel());
                RefundOrderDelayNotifyMessage nextNotifyMessage = new RefundOrderDelayNotifyMessage()
                        .setRefundOrderId(refundOrderId).setPayCenterDelayLevel(nextDelayLevel.getCode());
                log.info("【退款订单通知】发送下一次退款订单MQ延迟通知：{}，{}", mqConfig, nextNotifyMessage);
                refundOrderDelayNotifyTopic.product(mqConfig, nextNotifyMessage);
            }
        }
    }

    /**
     * 发送商户证书同步消息
     *
     * @param mchPrimaryId：商户主键ID
     * @return void
     * @author peng.xy
     * @date 2022/7/13
     */
    public void sendMchCertSyncNotify(Long mchPrimaryId) {
        CertSyncNotifyMessage message = new CertSyncNotifyMessage().setMchPrimaryId(mchPrimaryId);
        log.info("发送商户证书同步消息：{}", message);
        mchCertSyncNotifyTopic.product(message);
    }

    /**
     * 发送应用证书同步消息
     *
     * @param appPrimaryId：应用主键ID
     * @return void
     * @author peng.xy
     * @date 2022/7/13
     */
    public void sendAppCertSyncNotify(Long appPrimaryId) {
        CertSyncNotifyMessage message = new CertSyncNotifyMessage().setAppPrimaryId(appPrimaryId);
        log.info("发送应用证书同步消息：{}", message);
        appCertSyncNotifyTopic.product(message);
    }

}