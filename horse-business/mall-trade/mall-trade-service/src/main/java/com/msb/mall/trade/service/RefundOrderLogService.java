package com.msb.mall.trade.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.common.utils.ListUtil;
import com.msb.framework.web.result.Assert;
import com.msb.mall.trade.enums.RefundOperationLogTypeEnum;
import com.msb.mall.trade.exception.TradeExceptionCodeEnum;
import com.msb.mall.trade.mapper.RefundOrderLogMapper;
import com.msb.mall.trade.model.entity.RefundOrderLog;
import com.msb.mall.trade.model.vo.admin.RefundLogInfoVO;
import com.msb.mall.trade.service.convert.RefundOrderLogConvert;
import com.msb.user.api.vo.EmployeeDO;
import com.msb.user.api.vo.UserDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 退款单操作记录(RefundOrderLog)表服务实现类
 *
 * @author makejava
 * @date 2022-04-08 18:24:33
 */
@Slf4j
@Service("refundOrderLogService")
public class RefundOrderLogService extends ServiceImpl<RefundOrderLogMapper, RefundOrderLog> {

    @Resource
    private RefundOrderLogConvert refundOrderLogConvert;
    @Resource
    private UserService userService;

    /**
     * 保存退款单日志信息，无附加备注
     *
     * @param refundId：退款单ID
     * @param operationTypeEnum：退款单操作类型枚举
     * @return com.msb.mall.trade.model.entity.RefundOrderLog
     * @author peng.xy
     * @date 2022/4/11
     */
    public RefundOrderLog saveRefundLogs(@Nonnull Long refundId, RefundOperationLogTypeEnum operationTypeEnum) {
        return this.saveRefundLogs(refundId, operationTypeEnum, StringUtils.EMPTY);
    }

    /**
     * 保存退款单日志信息，并附加备注
     *
     * @param refundId：退款单ID
     * @param operationTypeEnum：退款单操作类型枚举
     * @param remark：备注
     * @return com.msb.mall.trade.model.entity.RefundOrderLog
     * @author peng.xy
     * @date 2022/4/11
     */
    public RefundOrderLog saveRefundLogs(@Nonnull Long refundId, RefundOperationLogTypeEnum operationTypeEnum, String remark) {
        RefundOrderLog refundLog = new RefundOrderLog()
                .setRefundId(refundId)
                .setOperationType(operationTypeEnum.getCode())
                .setRemark(operationTypeEnum.getRemark().concat(remark));
        log.info("保存退款单单日志信息：{}", refundLog);
        Assert.isTrue(super.save(refundLog), TradeExceptionCodeEnum.REFUND_LOG_SAVE_FAIL);
        return refundLog;
    }

    /**
     * 查询退款单日志信息列表，可指定操作类型
     *
     * @param refundId：订单ID
     * @param refundOperationLogTypes：操作类型枚举
     * @return java.util.List<com.msb.mall.trade.model.entity.RefundOrderLog>
     * @author peng.xy
     * @date 2022/4/1
     */
    public List<RefundOrderLog> listByRefundId(@Nonnull Long refundId, RefundOperationLogTypeEnum... refundOperationLogTypes) {
        return super.lambdaQuery().eq(RefundOrderLog::getRefundId, refundId)
                .in(ArrayUtils.isNotEmpty(refundOperationLogTypes), RefundOrderLog::getOperationType,
                        Arrays.stream(refundOperationLogTypes).map(RefundOperationLogTypeEnum::getCode).collect(Collectors.toList()))
                .orderByAsc(RefundOrderLog::getId)
                .list();
    }

    /**
     * 根据退款单ID和操作类型数组，查询一个退款单日志信息
     * 按照数据的顺序查询日志，找到不为空的就返回，如果一个都没有找到，则返回null
     *
     * @param refundId：订单ID
     * @param refundOperationLogTypes：操作类型枚举数组
     * @return java.util.List<com.msb.mall.trade.model.entity.RefundOrderLog>
     * @author peng.xy
     * @date 2022/4/1
     */
    public RefundOrderLog getOneByRefundIdAndTypesOrNull(@Nonnull Long refundId, RefundOperationLogTypeEnum... refundOperationLogTypes) {
        List<RefundOrderLog> list = this.listByRefundId(refundId, refundOperationLogTypes);
        return list.stream().findFirst().orElse(null);
    }

    /**
     * 根据退款单ID和操作类型数组，查询一个退款单日志信息
     * 按照数据的顺序查询日志，找到不为空的就返回，如果一个都没有找到，则返回一个空对象
     *
     * @param refundId：订单ID
     * @param refundOperationLogTypes：操作类型枚举数组
     * @return java.util.List<com.msb.mall.trade.model.entity.RefundOrderLog>
     * @author peng.xy
     * @date 2022/4/1
     */
    public RefundOrderLog getOneByRefundIdAndTypesOrEmpty(@Nonnull Long refundId, RefundOperationLogTypeEnum... refundOperationLogTypes) {
        RefundOrderLog refundOrderLog = this.getOneByRefundIdAndTypesOrNull(refundId, refundOperationLogTypes);
        return Optional.ofNullable(refundOrderLog).orElse(new RefundOrderLog());
    }

    /**
     * 查询退款单日志信息列表详情VO，可指定操作类型
     *
     * @param refundId：订单ID
     * @param refundOperationLogTypes：操作类型枚举
     * @return java.util.List<com.msb.mall.trade.model.entity.RefundOrderLog>
     * @author peng.xy
     * @date 2022/4/1
     */
    public List<RefundLogInfoVO> listVOByRefundIdAndTypes(@Nonnull Long refundId, RefundOperationLogTypeEnum... refundOperationLogTypes) {
        List<RefundOrderLog> refundOrderLogs = this.listByRefundId(refundId, refundOperationLogTypes);
        List<Long> userIds = ListUtil.convertDistinct(refundOrderLogs, RefundOrderLog::getCreateUser);
        Map<Long, UserDO> userVOMap = userService.mapUserByIdsOrEmpty(userIds);
        return refundOrderLogs.stream().map(refundOrderLog -> {
            RefundLogInfoVO refundLogInfoVO = refundOrderLogConvert.toRefundLogInfoVO(refundOrderLog);
            UserDO userVO = userVOMap.get(refundLogInfoVO.getCreateUser());
            if (Objects.nonNull(userVO)) {
                refundLogInfoVO.setCreateUserNickName(userVO.getNickname());
                EmployeeDO employeeDO = userVO.getEmployeeDO();
                // 如果员工信息不为空，取员工姓名
                if (Objects.nonNull(employeeDO)) {
                    refundLogInfoVO.setCreateUserNickName(employeeDO.getEmployeeName());
                }
            }
            return refundLogInfoVO;
        }).collect(Collectors.toList());
    }

    /**
     * 从退款单日志信息VO列表中，获取指定操作类型的日志
     *
     * @param refundLogInfoVoList：日志信息VO列表
     * @param refundOperationLogTypes：操作类型数组
     * @return com.msb.mall.trade.model.vo.admin.RefundLogInfoVO
     * @author peng.xy
     * @date 2022/4/14
     */
    public RefundLogInfoVO filterVOByTypes(@Nonnull List<RefundLogInfoVO> refundLogInfoVoList, @Nonnull RefundOperationLogTypeEnum... refundOperationLogTypes) {
        for (RefundLogInfoVO refundLogInfoVO : refundLogInfoVoList) {
            for (RefundOperationLogTypeEnum refundOperationLogTypeEnum : refundOperationLogTypes) {
                if (Objects.equals(refundOperationLogTypeEnum.getCode(), refundLogInfoVO.getOperationType())) {
                    return refundLogInfoVO;
                }
            }
        }
        return null;
    }

    /**
     * 从退款单日志信息VO列表中，获取指定操作类型的日志备注
     *
     * @param refundLogInfoVoList：日志信息VO列表
     * @param refundOperationLogType：操作类型数组
     * @return java.lang.String
     * @author peng.xy
     * @date 2022/5/9
     */
    public String getLogRemark(@Nonnull List<RefundLogInfoVO> refundLogInfoVoList, @Nonnull RefundOperationLogTypeEnum refundOperationLogType) {
        RefundLogInfoVO refundLogInfoVO = this.filterVOByTypes(refundLogInfoVoList, refundOperationLogType);
        if (Objects.nonNull(refundLogInfoVO)) {
            return refundLogInfoVO.getRemark();
        }
        return null;
    }

}

