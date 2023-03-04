package com.msb.mall.trade.model.dto.notify;

import com.msb.mall.product.api.model.VirtualProductDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("虚拟商品订单自动发货IM通知DTO")
public class ImVirtualOrderDeliveryDTO extends ImTradeNotifyDTO {

    @ApiModelProperty("虚拟商品发货内容")
    private List<VirtualProductDO> virtualProductContentList;

    public ImVirtualOrderDeliveryDTO(String customType) {
        super(customType);
    }

}
