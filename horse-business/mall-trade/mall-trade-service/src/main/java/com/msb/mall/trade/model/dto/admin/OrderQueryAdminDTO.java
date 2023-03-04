package com.msb.mall.trade.model.dto.admin;

import com.msb.framework.common.model.PageDTO;
import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.mall.trade.enums.OrderSourceEnum;
import com.msb.mall.trade.enums.OrderStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Data
@ApiModel("后管订单列表DTO")
public class OrderQueryAdminDTO extends PageDTO {

    @ApiModelPropertyEnum(dictEnum = OrderStatusEnum.class)
    @ApiModelProperty(value = "订单状态，多值示例：1,2,3", required = false)
    private List<Integer> orderStatus;

    @Min(1)
    @Max(6)
    @ApiModelPropertyEnum(dictEnum = OrderSourceEnum.class)
    @ApiModelProperty(value = "订单来源", required = false)
    private Integer orderSource;

    @Length(max = 32)
    @ApiModelProperty(value = "订单编号", required = false)
    private String orderNo;

    @Length(max = 11)
    @ApiModelProperty(value = "用户号码", required = false)
    private String userPhone;

    @ApiModelProperty(value = "用户ID", required = false)
    private Long userId;

    @ApiModelProperty(value = "下单开始时间（yyyy-MM-dd HH:mm:ss）", required = false)
    private LocalDateTime startTime;

    @ApiModelProperty(value = "下单结束时间（yyyy-MM-dd HH:mm:ss）", required = false)
    private LocalDateTime endTime;

    public OrderQueryAdminDTO() {
        this.orderStatus = Collections.emptyList();
    }

}
