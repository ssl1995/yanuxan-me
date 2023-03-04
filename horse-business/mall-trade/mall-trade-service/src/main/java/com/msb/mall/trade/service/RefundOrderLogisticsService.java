package com.msb.mall.trade.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.common.constant.CommonConst;
import com.msb.framework.common.context.UserContext;
import com.msb.framework.web.result.Assert;
import com.msb.mall.trade.enums.LogisticsStatusEnum;
import com.msb.mall.trade.exception.TradeExceptionCodeEnum;
import com.msb.mall.trade.mapper.RefundOrderLogisticsMapper;
import com.msb.mall.trade.model.dto.admin.ReturnAuditDTO;
import com.msb.mall.trade.model.entity.RefundOrderLogistics;
import com.msb.mall.trade.model.vo.app.LogisticsDataVO;
import com.msb.mall.trade.model.vo.app.LogisticsExpressQueryVO;
import com.msb.mall.trade.model.vo.app.LogisticsSubscribeCallbackVO;
import com.msb.mall.trade.model.vo.app.LogisticsSubscribeVO;
import com.msb.mall.trade.service.convert.RefundOrderLogisticsConvert;
import com.msb.mall.trade.third.logistics.LogisticsApiClient;
import com.msb.mall.trade.third.logistics.config.LogisticsApiConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * 退货单物流信息(RefundOrderLogistics)表服务实现类
 *
 * @author makejava
 * @date 2022-04-08 18:24:34
 */
@Slf4j
@Service("refundOrderLogisticsService")
public class RefundOrderLogisticsService extends ServiceImpl<RefundOrderLogisticsMapper, RefundOrderLogistics> {

    @Resource(name = "logisticsApiClient")
    private LogisticsApiClient logisticsApiClient;
    @Resource
    private LogisticsApiConfig logisticsApiConfig;
    @Resource
    private RefundOrderLogisticsConvert refundOrderLogisticsConvert;

    /**
     * 根据退款单ID获取退款单物流信息
     *
     * @param refundId：退款单ID
     * @return com.msb.mall.trade.model.entity.RefundOrderLogistics
     * @author peng.xy
     * @date 2022/4/12
     */
    public RefundOrderLogistics getByRefundId(@Nonnull Long refundId) {
        return super.lambdaQuery().eq(RefundOrderLogistics::getRefundId, refundId).one();
    }

    /**
     * 根据退款单ID获取退款单物流信息，数据有误则抛出异常
     *
     * @param refundId：退款单ID
     * @return com.msb.mall.trade.model.entity.RefundOrderLogistics
     * @author peng.xy
     * @date 2022/4/12
     */
    public RefundOrderLogistics getByRefundIdOrThrow(@Nonnull Long refundId) {
        RefundOrderLogistics orderLogistics = this.getByRefundId(refundId);
        Assert.notNull(orderLogistics, TradeExceptionCodeEnum.REFUND_LOGISTICS_EXCEPTION);
        return super.lambdaQuery().eq(RefundOrderLogistics::getRefundId, refundId).one();
    }

    /**
     * 保存退款单物流信息
     *
     * @param returnAuditDTO：参数
     * @return com.msb.mall.trade.model.entity.RefundOrderLogistics
     * @author peng.xy
     * @date 2022/4/13
     */
    public RefundOrderLogistics saveRefundLogistics(ReturnAuditDTO returnAuditDTO) {
        String recipientAddress = new StringBuilder()
                .append(StringUtils.isNotBlank(returnAuditDTO.getProvince()) ? returnAuditDTO.getProvince() : StringUtils.EMPTY)
                .append(StringUtils.isNotBlank(returnAuditDTO.getCity()) ? returnAuditDTO.getCity() : StringUtils.EMPTY)
                .append(StringUtils.isNotBlank(returnAuditDTO.getArea()) ? returnAuditDTO.getArea() : StringUtils.EMPTY)
                .append(StringUtils.isNotBlank(returnAuditDTO.getDetailAddress()) ? returnAuditDTO.getDetailAddress() : StringUtils.EMPTY)
                .toString();
        RefundOrderLogistics refundOrderLogistics = new RefundOrderLogistics()
                .setRefundId(returnAuditDTO.getRefundId())
                .setRecipientName(returnAuditDTO.getRecipientName())
                .setRecipientPhone(returnAuditDTO.getRecipientPhone())
                .setRecipientAddress(recipientAddress)
                .setIsSubscribe(CommonConst.NO)
                .setLogisticsStatus(LogisticsStatusEnum.NO_DATA.getCode());
        log.info("保存退款单物流信息：{}", refundOrderLogistics);
        Assert.isTrue(super.save(refundOrderLogistics), TradeExceptionCodeEnum.REFUND_LOGISTICS_SAVE_FAIL);
        return refundOrderLogistics;
    }

    /**
     * 更新物流信息，并订阅物流推送
     *
     * @param refundId：退款单ID
     * @param companyCode：物流公司编号
     * @param companyName：物流公司名称
     * @param trackingNo：物流单号
     * @return boolean
     * @author peng.xy
     * @date 2022/4/13
     */
    public boolean updateAndSubscribe(@Nonnull Long refundId, @Nonnull String companyCode, @Nonnull String companyName, @Nonnull String trackingNo) {
        RefundOrderLogistics orderLogistics = this.getByRefundIdOrThrow(refundId);
        LogisticsSubscribeVO logisticsSubscribeVO = logisticsApiClient.subscribe(companyCode, trackingNo, orderLogistics.getRecipientPhone(), LogisticsApiClient.REFUND);
        log.info("订阅退款单物流通知，退款单ID：{}，物流单号：{}，订阅结果：{}", refundId, trackingNo, logisticsSubscribeVO);
        boolean update = super.lambdaUpdate().eq(RefundOrderLogistics::getRefundId, refundId)
                .set(RefundOrderLogistics::getCompanyCode, companyCode)
                .set(RefundOrderLogistics::getCompanyName, companyName)
                .set(RefundOrderLogistics::getTrackingNo, trackingNo)
                .set(RefundOrderLogistics::getIsSubscribe, logisticsSubscribeVO.getIsSuccess())
                .set(RefundOrderLogistics::getLogisticsApi, logisticsSubscribeVO.getLogisticsApi())
                .set(RefundOrderLogistics::getUpdateUser, UserContext.getUserId())
                .set(RefundOrderLogistics::getUpdateTime, LocalDateTime.now())
                .update();
        // 订阅失败，直接调用查询进行刷新
        if (!logisticsSubscribeVO.getIsSuccess()) {
            this.refreshLogistics(refundId);
        }
        return update;
    }

    /**
     * 物流订阅回调
     *
     * @param logisticsApi：物流API
     * @param param：回调参数
     * @return boolean
     * @author peng.xy
     * @date 2022/4/13
     */
    public boolean subscribeCallback(@Nonnull String logisticsApi, @Nonnull String param) {
        log.info("退款单物流订阅通知回调，API：{}，参数：{}", logisticsApi, param);
        if (StringUtils.isAnyBlank(logisticsApi, param)) {
            return false;
        }
        LogisticsApiClient apiClient = logisticsApiConfig.getApiClientById(logisticsApi);
        LogisticsSubscribeCallbackVO subscribeCallbackVO = apiClient.subscribeCallback(param);
        log.info("退款单物流订阅通知回调，回调处理结果：{}", subscribeCallbackVO);
        if (subscribeCallbackVO.getIsSuccess()) {
            // 更新退款物流订阅结果
            return super.lambdaUpdate().eq(RefundOrderLogistics::getTrackingNo, subscribeCallbackVO.getTrackingNo())
                    .set(RefundOrderLogistics::getLogisticsApi, subscribeCallbackVO.getLogisticsApi())
                    .set(RefundOrderLogistics::getLogisticsData, subscribeCallbackVO.getLogisticsData())
                    .set(RefundOrderLogistics::getLogisticsStatus, subscribeCallbackVO.getStatus())
                    .set(RefundOrderLogistics::getUpdateTime, LocalDateTime.now())
                    .update();
        }
        return false;
    }

    /**
     * 刷新物流
     *
     * @param refundId：退款单ID
     * @return com.msb.mall.trade.model.vo.app.LogisticsExpressQueryVO
     * @author peng.xy
     * @date 2022/4/13
     */
    @Transactional(rollbackFor = Exception.class)
    public LogisticsExpressQueryVO refreshLogistics(@Nonnull Long refundId) {
        RefundOrderLogistics refundLogistics = this.getByRefundIdOrThrow(refundId);
        LogisticsExpressQueryVO expressQueryVO = logisticsApiClient.query(refundLogistics.getCompanyCode(), refundLogistics.getTrackingNo(), refundLogistics.getRecipientPhone());
        if (expressQueryVO.getIsSuccess()) {
            // 更新物流查询结果
            super.lambdaUpdate().eq(RefundOrderLogistics::getRefundId, refundId)
                    .set(RefundOrderLogistics::getLogisticsApi, expressQueryVO.getLogisticsApi())
                    .set(RefundOrderLogistics::getLogisticsData, expressQueryVO.getLogisticsData())
                    .set(RefundOrderLogistics::getLogisticsStatus, expressQueryVO.getStatus())
                    .set(RefundOrderLogistics::getUpdateTime, LocalDateTime.now())
                    .update();
        }
        return expressQueryVO;
    }


    /**
     * 解析物流详情列表
     *
     * @param logisticsApi：物流API
     * @param logisticsData：物流详情数据
     * @return java.util.List<com.msb.mall.trade.model.vo.app.LogisticsDataVO>
     * @author peng.xy
     * @date 2022/4/6
     */
    public List<LogisticsDataVO> parseData(String logisticsApi, String logisticsData) {
        if (StringUtils.isAnyBlank(logisticsApi, logisticsData)) {
            return Collections.emptyList();
        }
        LogisticsApiClient apiClient = logisticsApiConfig.getApiClientById(logisticsApi);
        return apiClient.parseData(logisticsData);
    }

}

