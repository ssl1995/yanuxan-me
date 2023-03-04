package com.msb.mall.trade.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.web.result.Assert;
import com.msb.mall.trade.enums.OrderOperationLogTypeEnum;
import com.msb.mall.trade.exception.TradeExceptionCodeEnum;
import com.msb.mall.trade.mapper.TradeOrderLogMapper;
import com.msb.mall.trade.model.entity.TradeOrderLog;
import com.msb.mall.trade.service.convert.TradeOrderLogConvert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 订单操作记录(TradeOrderLog)表服务实现类
 *
 * @author makejava
 * @since 2022-03-24 18:30:18
 */
@Slf4j
@Service("tradeOrderLogService")
public class TradeOrderLogService extends ServiceImpl<TradeOrderLogMapper, TradeOrderLog> {

    @Resource
    private TradeOrderLogConvert tradeOrderLogConvert;

    /**
     * 保存订单日志信息，无附加备注
     *
     * @param orderId：订单ID
     * @param operationTypeEnum：日志操作类型枚举
     * @return com.msb.mall.trade.model.entity.TradeOrderLog
     * @author peng.xy
     * @date 2022/3/29
     */
    public TradeOrderLog saveOrderLogs(@Nonnull Long orderId, @Nonnull OrderOperationLogTypeEnum operationTypeEnum) {
        return this.saveOrderLogs(orderId, operationTypeEnum, StringUtils.EMPTY);
    }

    /**
     * 保存订单日志信息，并附加备注
     *
     * @param orderId：订单ID
     * @param operationTypeEnum：日志操作类型枚举
     * @return com.msb.mall.trade.model.entity.TradeOrderLog
     * @author peng.xy
     * @date 2022/3/29
     */
    public TradeOrderLog saveOrderLogs(@Nonnull Long orderId, @Nonnull OrderOperationLogTypeEnum operationTypeEnum, @Nonnull String remark) {
        TradeOrderLog orderLog = new TradeOrderLog()
                .setOrderId(orderId)
                .setOperationType(operationTypeEnum.getCode())
                .setRemark(operationTypeEnum.getRemark().concat(remark));
        log.info("保存订单日志信息：{}", orderLog);
        Assert.isTrue(super.save(orderLog), TradeExceptionCodeEnum.ORDER_LOG_SAVE_FAIL);
        return orderLog;
    }

    /**
     * 查询订单日志信息列表，可指定操作类型
     *
     * @param orderId：订单ID
     * @param operationTypes：操作类型枚举
     * @return java.util.List<com.msb.mall.trade.model.entity.TradeOrderLog>
     * @author peng.xy
     * @date 2022/4/1
     */
    public List<TradeOrderLog> listByOrderIdAndTypes(@Nonnull Long orderId, OrderOperationLogTypeEnum... operationTypes) {
        return super.lambdaQuery().eq(TradeOrderLog::getOrderId, orderId)
                .in(ArrayUtils.isNotEmpty(operationTypes), TradeOrderLog::getOperationType,
                        Arrays.stream(operationTypes).map(OrderOperationLogTypeEnum::getCode).collect(Collectors.toList()))
                .orderByAsc(TradeOrderLog::getId)
                .list();
    }

    /**
     * 从订单日志信息列表中，获取指定操作类型的日志
     *
     * @param tradeOrderLogList：订单日志列表
     * @param operationTypes：指定操作类型
     * @return com.msb.mall.trade.model.entity.TradeOrderLog
     * @author peng.xy
     * @date 2022/5/10
     */
    public TradeOrderLog filterVOByTypes(List<TradeOrderLog> tradeOrderLogList, OrderOperationLogTypeEnum... operationTypes) {
        for (TradeOrderLog tradeOrderLog : tradeOrderLogList) {
            for (OrderOperationLogTypeEnum orderOperationLogTypeEnum : operationTypes) {
                if (Objects.equals(orderOperationLogTypeEnum.getCode(), tradeOrderLog.getOperationType())) {
                    return tradeOrderLog;
                }
            }
        }
        return null;
    }

    /**
     * 从退款单日志信息VO列表中，获取指定操作类型的日志时间
     *
     * @param tradeOrderLogList：订单日志列表
     * @param operationType：指定操作类型
     * @return java.time.LocalDateTime
     * @author peng.xy
     * @date 2022/5/10
     */
    public LocalDateTime getLogTime(@Nonnull List<TradeOrderLog> tradeOrderLogList, OrderOperationLogTypeEnum operationType) {
        TradeOrderLog tradeOrderLog = this.filterVOByTypes(tradeOrderLogList, operationType);
        if (Objects.nonNull(tradeOrderLog)) {
            return tradeOrderLog.getCreateTime();
        }
        return null;
    }

    /**
     * 从退款单日志信息VO列表中，获取指定操作类型的日志时间
     *
     * @param orderId：订单ID
     * @param operationType：指定操作类型
     * @return java.time.LocalDateTime
     * @author peng.xy
     * @date 2022/5/10
     */
    public LocalDateTime getLogTime(@Nonnull Long orderId, OrderOperationLogTypeEnum operationType) {
        List<TradeOrderLog> logList = this.listByOrderIdAndTypes(orderId, operationType);
        if (CollectionUtils.isNotEmpty(logList)) {
            return logList.stream().findFirst().get().getCreateTime();
        }
        return null;
    }

}

