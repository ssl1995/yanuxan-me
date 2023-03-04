package com.msb.mall.marketing.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 广告排序修改DTO
 * @author shumengjiao
 */
@Data
public class AdvertisementSortModifyDTO implements Serializable {
    @NotNull
    @ApiModelProperty("广告id")
    private Long id;

    @NotNull
    @ApiModelProperty("广告平台（1-移动端 2-PC端）")
    private Integer platform;

    @NotNull
    @ApiModelProperty("广告位置（1-首页轮播 2-首页金刚区 3-商品分类广告）")
    private Integer location;

    @NotNull
    @ApiModelProperty("原来的sort值")
    private Integer oldSort;

    @NotNull
    @ApiModelProperty("现在的sort值")
    private Integer newSort;

}
