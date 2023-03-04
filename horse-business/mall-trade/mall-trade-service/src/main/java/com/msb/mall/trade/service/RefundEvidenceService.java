package com.msb.mall.trade.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.common.constant.CommonConst;
import com.msb.framework.web.result.Assert;
import com.msb.mall.trade.enums.EvidenceFileEnum;
import com.msb.mall.trade.enums.EvidenceTypeEnum;
import com.msb.mall.trade.exception.TradeExceptionCodeEnum;
import com.msb.mall.trade.mapper.RefundEvidenceMapper;
import com.msb.mall.trade.model.entity.RefundEvidence;
import com.msb.mall.trade.service.convert.RefundEvidenceConvert;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 退款单退货凭证(RefundEvidence)表服务实现类
 *
 * @author makejava
 * @date 2022-04-08 18:24:32
 */
@Service("refundEvidenceService")
public class RefundEvidenceService extends ServiceImpl<RefundEvidenceMapper, RefundEvidence> {

    @Resource
    private RefundEvidenceConvert refundEvidenceConvert;

    /**
     * 保存退款单凭证
     *
     * @param refundId：退款单ID
     * @param fileUrls：文件地址数组
     * @param evidenceTypeEnum：凭证类型枚举
     * @param evidenceFileEnum：文件类型枚举
     * @return boolean
     * @author peng.xy
     * @date 2022/4/11
     */
    public boolean saveEvidence(@Nonnull Long refundId, @Nonnull String[] fileUrls, @Nonnull EvidenceTypeEnum evidenceTypeEnum, @Nonnull EvidenceFileEnum evidenceFileEnum) {
        if (ArrayUtils.isEmpty(fileUrls)) {
            return false;
        }
        List<RefundEvidence> evidenceList = Arrays.stream(fileUrls).map(fileUrl -> {
            return new RefundEvidence()
                    .setRefundId(refundId)
                    .setEvidenceType(evidenceTypeEnum.getCode())
                    .setFileType(evidenceFileEnum.getCode())
                    .setFileUrl(fileUrl)
                    .setIsDeleted(CommonConst.NO);
        }).collect(Collectors.toList());
        Assert.isTrue(super.saveBatch(evidenceList), TradeExceptionCodeEnum.REFUND_EVIDENCE_SAVE_FAIL);
        return true;
    }

    /**
     * 根据退款单ID和凭证类型查询凭证列表
     *
     * @param refundId：退款ID
     * @param evidenceTypeEnum：凭证类型枚举
     * @return java.util.List<com.msb.mall.trade.model.entity.RefundEvidence>
     * @author peng.xy
     * @date 2022/4/12
     */
    public List<RefundEvidence> listByRefundIdAndEvidenceType(@Nonnull Long refundId, @Nonnull EvidenceTypeEnum evidenceTypeEnum) {
        return super.lambdaQuery()
                .eq(RefundEvidence::getRefundId, refundId)
                .eq(RefundEvidence::getEvidenceType, evidenceTypeEnum.getCode())
                .eq(RefundEvidence::getIsDeleted, CommonConst.NO)
                .list();
    }

    /**
     * 根据退款单ID和凭证类型删除凭证
     *
     * @param refundId：退款ID
     * @param evidenceTypeEnum：凭证类型枚举
     * @return boolean
     * @author peng.xy
     * @date 2022/4/12
     */
    public boolean removeByRefundIdAndEvidenceType(@Nonnull Long refundId, @Nonnull EvidenceTypeEnum evidenceTypeEnum) {
        return super.lambdaUpdate()
                .eq(RefundEvidence::getRefundId, refundId)
                .eq(RefundEvidence::getEvidenceType, evidenceTypeEnum.getCode())
                .set(RefundEvidence::getIsDeleted, CommonConst.YES)
                .update();
    }

}

