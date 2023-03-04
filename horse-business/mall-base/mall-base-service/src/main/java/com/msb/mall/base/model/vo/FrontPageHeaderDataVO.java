package com.msb.mall.base.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 头部数据VO
 * @author shumengjiao
 */
@Accessors(chain = true)
@Data
public class FrontPageHeaderDataVO {

    @ApiModelProperty("今日访客数")
    private Integer todayVisitorCount;

    @ApiModelProperty("今日订单数")
    private Integer todayOrderCount;

    @ApiModelProperty("今日销售额")
    private BigDecimal todaySales;

    @ApiModelProperty("近七天销售总额")
    private BigDecimal lastSevenDaysSales;

}
