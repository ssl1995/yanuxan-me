package com.msb.mall.trade.model.vo.app;

import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.mall.trade.enums.BooleanEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel("订阅物流数据VO")
public class LogisticsSubscribeVO {

    @ApiModelPropertyEnum(dictEnum = BooleanEnum.class)
    @ApiModelProperty("是否订阅成功")
    private Boolean isSuccess;

    @ApiModelProperty("物流API")
    private String logisticsApi;

    public LogisticsSubscribeVO() {
    }

    public LogisticsSubscribeVO(Boolean isSuccess, String logisticsApi) {
        this.isSuccess = isSuccess;
        this.logisticsApi = logisticsApi;
    }

}
