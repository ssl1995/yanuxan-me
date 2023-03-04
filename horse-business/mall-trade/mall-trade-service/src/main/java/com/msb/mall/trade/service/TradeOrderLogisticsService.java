package com.msb.mall.trade.service;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.common.constant.CommonConst;
import com.msb.framework.common.context.UserContext;
import com.msb.framework.web.result.Assert;
import com.msb.mall.base.api.model.ReceiveAddressDO;
import com.msb.mall.trade.enums.LogisticsStatusEnum;
import com.msb.mall.trade.exception.TradeExceptionCodeEnum;
import com.msb.mall.trade.mapper.TradeOrderLogisticsMapper;
import com.msb.mall.trade.model.entity.TradeOrderLogistics;
import com.msb.mall.trade.model.vo.app.LogisticsDataVO;
import com.msb.mall.trade.model.vo.app.LogisticsExpressQueryVO;
import com.msb.mall.trade.model.vo.app.LogisticsSubscribeCallbackVO;
import com.msb.mall.trade.model.vo.app.LogisticsSubscribeVO;
import com.msb.mall.trade.service.convert.TradeOrderLogisticsConvert;
import com.msb.mall.trade.third.logistics.LogisticsApiClient;
import com.msb.mall.trade.third.logistics.LogisticsApiConfigEnum;
import com.msb.mall.trade.third.logistics.config.LogisticsApiConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 订单物流信息(TradeOrderLogistics)表服务实现类
 *
 * @author makejava
 * @since 2022-03-26 17:07:42
 */
@Slf4j
@Service("tradeOrderLogisticsService")
public class TradeOrderLogisticsService extends ServiceImpl<TradeOrderLogisticsMapper, TradeOrderLogistics> {

    @Resource(name = "logisticsApiClient")
    private LogisticsApiClient logisticsApiClient;
    @Resource
    private LogisticsApiConfig logisticsApiConfig;
    @Resource
    private TradeOrderLogisticsConvert tradeOrderLogisticsConvert;
    @Resource
    private TradeOrderLogService tradeOrderLogService;

    /**
     * 保存订单物流信息
     *
     * @param orderId：订单ID
     * @param receiveAddressDO：收货地址DO
     * @return com.msb.mall.trade.model.entity.TradeOrderLogistics
     * @author peng.xy
     * @date 2022/3/29
     */
    public TradeOrderLogistics saveOrderLogistics(@Nonnull Long orderId, ReceiveAddressDO receiveAddressDO) {
        TradeOrderLogistics tradeOrderLogistics = new TradeOrderLogistics()
                .setOrderId(orderId).setIsSubscribe(CommonConst.NO).setLogisticsStatus(LogisticsStatusEnum.NO_DATA.getCode());
        if (Objects.nonNull(receiveAddressDO)) {
            // 根据收货ID获取收件人信息
            String province = receiveAddressDO.getProvince();
            String city = receiveAddressDO.getCity();
            String area = receiveAddressDO.getArea();
            String detailAddress = receiveAddressDO.getDetailAddress();
            String recipientAddress = (StringUtils.isNotBlank(province) ? province : StringUtils.EMPTY) +
                    (StringUtils.isNotBlank(city) ? city : StringUtils.EMPTY) +
                    (StringUtils.isNotBlank(area) ? area : StringUtils.EMPTY) +
                    (StringUtils.isNotBlank(detailAddress) ? detailAddress : StringUtils.EMPTY);
            // 创建订单物流数据
            tradeOrderLogistics.setRecipientName(receiveAddressDO.getName())
                    .setRecipientPhone(receiveAddressDO.getPhone())
                    .setRecipientAddress(recipientAddress)
                    .setProvinceCode(receiveAddressDO.getProvinceCode())
                    .setProvince(province)
                    .setCityCode(receiveAddressDO.getCityCode())
                    .setCity(city)
                    .setAreaCode(receiveAddressDO.getAreaCode())
                    .setArea(area)
                    .setDetailAddress(detailAddress);
        }
        log.info("保存订单物流信息：{}", tradeOrderLogistics);
        Assert.isTrue(super.save(tradeOrderLogistics), TradeExceptionCodeEnum.ORDER_LOGISTICS_SAVE_FAIL);
        return tradeOrderLogistics;
    }

    /**
     * 根据订单ID获取物流信息，未查询到则抛出异常
     *
     * @param orderId：订单ID
     * @return com.msb.mall.trade.model.entity.TradeOrderLogistics
     * @author peng.xy
     * @date 2022/3/30
     */
    public TradeOrderLogistics getByOrderIdOrThrow(@Nonnull Long orderId) {
        TradeOrderLogistics orderLogistics = super.lambdaQuery().eq(TradeOrderLogistics::getOrderId, orderId).one();
        Assert.notNull(orderLogistics, TradeExceptionCodeEnum.ORDER_LOGISTICS_ERROR);
        return orderLogistics;
    }


    /**
     * 根据订单ID获取物流信息，未查询到则返回空对象
     *
     * @param orderId：订单ID
     * @return com.msb.mall.trade.model.entity.TradeOrderLogistics
     * @author peng.xy
     * @date 2022/3/30
     */
    public TradeOrderLogistics getByOrderIdOrEmpty(@Nonnull Long orderId) {
        return super.lambdaQuery().eq(TradeOrderLogistics::getOrderId, orderId).oneOpt().orElse(new TradeOrderLogistics().setOrderId(orderId));
    }

    /**
     * 根据订单ID列表获取物流信息List
     *
     * @param orderIds：订单ID列表
     * @return java.util.List<com.msb.mall.trade.model.entity.TradeOrderLogistics>
     * @author peng.xy
     * @date 2022/4/1
     */
    public List<TradeOrderLogistics> listByOrderIds(@Nonnull Long... orderIds) {
        return super.lambdaQuery().in(TradeOrderLogistics::getOrderId, Arrays.asList(orderIds)).list();
    }

    /**
     * 根据订单ID列表获取物流信息Map
     *
     * @param orderIds：订单ID列表
     * @return java.util.Map<java.lang.Long, com.msb.mall.trade.model.entity.TradeOrderLogistics>
     * @author peng.xy
     * @date 2022/4/7
     */
    public Map<Long, TradeOrderLogistics> mapByOrderIds(@Nonnull List<Long> orderIds) {
        return this.mapByOrderIds(orderIds.toArray(new Long[]{}));
    }

    /**
     * 根据订单ID列表获取物流信息Map
     *
     * @param orderIds：订单ID列表
     * @return java.util.Map<java.lang.Long, com.msb.mall.trade.model.entity.TradeOrderLogistics>
     * @author peng.xy
     * @date 2022/4/7
     */
    public Map<Long, TradeOrderLogistics> mapByOrderIds(@Nonnull Long... orderIds) {
        List<TradeOrderLogistics> orderLogisticsList = this.listByOrderIds(orderIds);
        if (CollectionUtils.isEmpty(orderLogisticsList)) {
            return Collections.emptyMap();
        }
        return orderLogisticsList.stream().collect(Collectors.toMap(TradeOrderLogistics::getOrderId, Function.identity()));
    }

    /**
     * 更新物流信息，并订阅物流推送
     *
     * @param orderId：订单ID
     * @param companyCode：物流公司编号
     * @param companyName：物流公司名称
     * @param trackingNo：物流单号
     * @return boolean
     * @author peng.xy
     * @date 2022/4/1
     */
    public boolean updateAndSubscribe(@Nonnull Long orderId, @Nonnull String companyCode, @Nonnull String companyName, @Nonnull String trackingNo) {
        TradeOrderLogistics orderLogistics = this.getByOrderIdOrThrow(orderId);
        LogisticsSubscribeVO logisticsSubscribeVO = logisticsApiClient.subscribe(companyCode, trackingNo, orderLogistics.getRecipientPhone(), LogisticsApiClient.ORDER);
        log.info("订阅订单物流通知，订单ID：{}，物流单号：{}，订阅结果：{}", orderId, trackingNo, logisticsSubscribeVO);
        boolean update = super.lambdaUpdate().eq(TradeOrderLogistics::getOrderId, orderId)
                .set(TradeOrderLogistics::getCompanyCode, companyCode)
                .set(TradeOrderLogistics::getCompanyName, companyName)
                .set(TradeOrderLogistics::getTrackingNo, trackingNo)
                .set(TradeOrderLogistics::getLogisticsApi, logisticsSubscribeVO.getLogisticsApi())
                .set(TradeOrderLogistics::getIsSubscribe, logisticsSubscribeVO.getIsSuccess())
                .set(TradeOrderLogistics::getUpdateUser, UserContext.getUserId())
                .set(TradeOrderLogistics::getUpdateTime, LocalDateTime.now())
                .update();
        // 订阅失败，直接调用查询进行刷新
        if (!logisticsSubscribeVO.getIsSuccess()) {
            this.refreshLogistics(orderId);
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
     * @date 2022/4/11
     */
    public boolean subscribeCallback(@Nonnull String logisticsApi, @Nonnull String param) {
        log.info("订单物流订阅通知回调，API：{}，参数：{}", logisticsApi, param);
        if (StringUtils.isAnyBlank(logisticsApi, param)) {
            return false;
        }
        LogisticsApiClient apiClient = logisticsApiConfig.getApiClientById(logisticsApi);
        LogisticsSubscribeCallbackVO subscribeCallbackVO = apiClient.subscribeCallback(param);
        log.info("订单物流订阅通知回调，回调处理结果：{}", subscribeCallbackVO);
        if (subscribeCallbackVO.getIsSuccess()) {
            // 更新订单物流订阅结果
            return super.lambdaUpdate().eq(TradeOrderLogistics::getTrackingNo, subscribeCallbackVO.getTrackingNo())
                    .set(TradeOrderLogistics::getLogisticsStatus, subscribeCallbackVO.getStatus())
                    .set(TradeOrderLogistics::getLogisticsApi, subscribeCallbackVO.getLogisticsApi())
                    .set(TradeOrderLogistics::getLogisticsData, subscribeCallbackVO.getLogisticsData())
                    .set(TradeOrderLogistics::getUpdateTime, LocalDateTime.now())
                    .update();
        }
        return false;
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

    /**
     * 刷新物流
     *
     * @param orderId：订单ID
     * @return com.msb.mall.trade.model.vo.app.LogisticsExpressQueryVO
     * @author peng.xy
     * @date 2022/4/11
     */
    @Transactional(rollbackFor = Exception.class)
    public LogisticsExpressQueryVO refreshLogistics(@Nonnull Long orderId) {
        TradeOrderLogistics orderLogistics = this.getByOrderIdOrThrow(orderId);
        LogisticsExpressQueryVO expressQueryVO = logisticsApiClient.query(orderLogistics.getCompanyCode(), orderLogistics.getTrackingNo(), orderLogistics.getRecipientPhone());
        if (expressQueryVO.getIsSuccess()) {
            // 更新物流查询结果
            super.lambdaUpdate().eq(TradeOrderLogistics::getOrderId, orderId)
                    .set(TradeOrderLogistics::getLogisticsStatus, expressQueryVO.getStatus())
                    .set(TradeOrderLogistics::getLogisticsApi, expressQueryVO.getLogisticsApi())
                    .set(TradeOrderLogistics::getLogisticsData, expressQueryVO.getLogisticsData())
                    .set(TradeOrderLogistics::getUpdateTime, LocalDateTime.now())
                    .update();
        }
        return expressQueryVO;
    }

    /**
     * 保存虚拟订单发货物流
     *
     * @param orderId：订单ID
     * @return boolean
     * @author peng.xy
     * @date 2022/5/26
     */
    public boolean updateVirtualLogistics(Long orderId) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        List<LogisticsDataVO> dataList = Collections.singletonList(new LogisticsDataVO().setTime(time).setContext("已自动发货，请前往系统消息中查收"));
        return super.lambdaUpdate().eq(TradeOrderLogistics::getOrderId, orderId)
                .set(TradeOrderLogistics::getLogisticsApi, LogisticsApiConfigEnum.VIRTUAL.getCode())
                .set(TradeOrderLogistics::getLogisticsData, JSONObject.toJSONString(dataList))
                .set(TradeOrderLogistics::getUpdateTime, LocalDateTime.now())
                .update();
    }

}

