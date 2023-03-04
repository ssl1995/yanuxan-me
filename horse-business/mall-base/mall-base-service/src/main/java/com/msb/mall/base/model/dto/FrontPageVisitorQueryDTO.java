package com.msb.mall.base.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author 86151
 */
@Data
public class FrontPageVisitorQueryDTO {
    @ApiModelProperty("开始时间")
    @NotNull
    private LocalDate beginDate;
    @ApiModelProperty("结束时间")
    @NotNull
    private LocalDate endDate;
}
