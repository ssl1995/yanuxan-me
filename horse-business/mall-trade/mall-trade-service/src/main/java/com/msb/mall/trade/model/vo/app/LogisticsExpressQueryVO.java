package com.msb.mall.trade.model.vo.app;

import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.mall.trade.enums.BooleanEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel("物流查询VO")
public class LogisticsExpressQueryVO {

    @ApiModelPropertyEnum(dictEnum = BooleanEnum.class)
    @ApiModelProperty("查询是否成功")
    private Boolean isSuccess;

    @ApiModelProperty("物流状态")
    private Integer status;

    @ApiModelProperty("物流API")
    private String logisticsApi;

    @ApiModelProperty("物流数据")
    private String logisticsData;

    @ApiModelProperty("物流数据解析列表")
    private List<LogisticsDataVO> logisticsDataList = Collections.emptyList();

    public LogisticsExpressQueryVO() {
    }

    public LogisticsExpressQueryVO(Boolean isSuccess, String logisticsApi) {
        this.isSuccess = isSuccess;
        this.logisticsApi = logisticsApi;
    }
}
