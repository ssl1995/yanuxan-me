package com.msb.mall.trade.model.vo.admin;

import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.framework.web.transform.annotation.TransformEnum;
import com.msb.mall.trade.enums.RefundOperationLogTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("退款单日志信息VO")
public class RefundLogVO {

    @ApiModelPropertyEnum(dictEnum = RefundOperationLogTypeEnum.class)
    @ApiModelProperty("操作类型")
    private Integer operationType;

    @TransformEnum(value = RefundOperationLogTypeEnum.class, from = "operationType")
    @ApiModelProperty("操作类型文本")
    private String operationTypeDesc;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

}
