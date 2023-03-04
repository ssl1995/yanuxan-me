package com.msb.mall.product.model.vo.app;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * 商品推荐VO
 *
 * @author luozhan
 * @date 2022-03-30 18:04:03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ProductRecommendVO extends ProductSimpleVO {

    @ApiModelProperty("主标题（页面展示）")
    private String title;

    @ApiModelProperty("副标题（页面展示）")
    private String subtitle;

    @ApiModelProperty("推荐图（页面展示）")
    private String recommendPicture;

}

