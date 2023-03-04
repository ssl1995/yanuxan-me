package com.msb.im.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 客服连接socket获取ticket的VO
 *
 * @author zhou miao
 * @date 2022/05/12
 */
@Data
@ApiModel("按小时统计消息量")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CountHoursMessageVO {
    /*@ApiModelProperty("小时时间 比如2020-02-02 1:00")
    private String hours;
    @ApiModelProperty("消息数量")
    private Integer messageCount;*/

    @ApiModelProperty("小时")
    private Integer hours;
    @ApiModelProperty("消息数量")
    private Integer messageCount;

}
