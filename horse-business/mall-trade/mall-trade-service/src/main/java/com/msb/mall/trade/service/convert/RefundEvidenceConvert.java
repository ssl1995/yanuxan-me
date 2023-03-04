package com.msb.mall.trade.service.convert;


import com.msb.mall.trade.model.entity.RefundEvidence;
import com.msb.mall.trade.model.vo.app.RefundEvidenceVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 退款单退货凭证(RefundEvidence)表服务接口
 *
 * @author makejava
 * @date 2022-04-08 18:24:32
 */
@Mapper(componentModel = "spring")
public interface RefundEvidenceConvert {

    List<RefundEvidenceVO> toRefundEvidenceVOList(List<RefundEvidence> refundEvidenceList);

    RefundEvidenceVO toRefundEvidenceVO(RefundEvidence refundEvidence);

}

