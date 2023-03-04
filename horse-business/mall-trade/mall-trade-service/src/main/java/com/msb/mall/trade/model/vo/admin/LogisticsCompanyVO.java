package com.msb.mall.trade.model.vo.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("物流公司VO")
public class LogisticsCompanyVO {

    @ApiModelProperty("物流公司编号")
    private String companyCode;

    @ApiModelProperty("物流公司名称")
    private String companyName;

}
