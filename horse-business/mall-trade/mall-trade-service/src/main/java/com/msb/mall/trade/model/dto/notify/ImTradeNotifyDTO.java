package com.msb.mall.trade.model.dto.notify;

import com.msb.im.api.dto.CustomTypeDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@ApiModel("交易模块IM通知DTO")
public class ImTradeNotifyDTO extends CustomTypeDTO {

    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("内容")
    private String content;
    @ApiModelProperty("业务主键ID（订单ID、退款单ID）")
    private Long primaryId;
    @ApiModelProperty("商品名称")
    private String productNames;
    @ApiModelProperty("商品图片")
    private List<String> productImageUrls;
    @ApiModelProperty("跳转链接")
    private String link;

    public ImTradeNotifyDTO(String customType) {
        super(customType);
    }

}
