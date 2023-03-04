package com.msb.pay.service.convert;

import com.msb.pay.model.bo.ApplyRefundRES;
import com.msb.pay.model.bo.UnifiedOrderRES;
import com.msb.pay.model.dto.TestUnifiedOrderDTO;
import com.msb.pay.model.dto.UnifiedOrderDTO;
import com.msb.pay.model.dto.UnifiedOrderDubboDTO;
import com.msb.pay.model.paydata.PayData;
import com.msb.pay.model.vo.ApplyRefundVO;
import com.msb.pay.model.vo.UnifiedOrderVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * 模型服务接口
 *
 * @author makejava
 * @date 2022-06-06 10:42:45
 */
@Mapper(componentModel = "spring")
public interface ModelConvert {

    @Mapping(target = "payCode", source = "payCode", ignore = true)
    UnifiedOrderDTO toUnifiedOrderDTO(UnifiedOrderDubboDTO unifiedOrderDubboDTO);

    UnifiedOrderVO<PayData> toUnifiedOrderVO(UnifiedOrderRES<? extends PayData> unifiedOrderRes);

    ApplyRefundVO toApplyRefundVO(ApplyRefundRES applyRefundRes);

    UnifiedOrderDTO toUnifiedOrderDTO(TestUnifiedOrderDTO testUnifiedOrderDTO);

}

