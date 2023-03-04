package com.msb.mall.trade.model.vo.admin;

import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.framework.web.transform.annotation.TransformEnum;
import com.msb.mall.trade.enums.OrderOperationLogTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("订单日志信息VO")
public class OrderLogVO {

    @ApiModelPropertyEnum(dictEnum = OrderOperationLogTypeEnum.class)
    @ApiModelProperty("操作类型")
    private Integer operationType;

    @TransformEnum(value = OrderOperationLogTypeEnum.class, from = "operationType")
    @ApiModelProperty("操作类型文本")
    private String operationTypeDesc;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

}
