package com.msb.im.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 客服连接socket获取ticket的VO
 *
 * @author zhou miao
 * @date 2022/05/12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountDaysMessageVO {
    @ApiModelProperty("日期时间 比如2020-02-03")
    private LocalDate days;
    @ApiModelProperty("消息数量")
    private Integer messageCount;
}
