package com.msb.mall.product.model.vo.admin;


import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.mall.product.enums.ProductTypeEnums;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;


/**
 * 商品编辑详情
 *
 * @author luozhan
 * @date 2022-03-30 18:04:03
 */
@Data
public class ProductModifyVO {

    @ApiModelProperty("商品id")
    private String id;

    @ApiModelProperty("商品名称")
    private String name;

    @ApiModelProperty("商品分类ID")
    private Long categoryId;

    @ApiModelProperty("商品备注")
    private String remark;

    @ApiModelProperty("上下架状态:1-上架,0-下架")
    private Boolean isEnable;

    @ApiModelProperty("偏远地区邮费（不填为全国包邮）")
    private BigDecimal remoteAreaPostage;

    @ApiModelProperty("单次购买上限（不填不限购）")
    private Integer singleBuyLimit;

    @ApiModelProperty("商品图片列表（主图放第一个）")
    private List<String> pictureList;

    @NotNull
    @ApiModelProperty("商品详情")
    private String detail;

    @NotNull
    @ApiModelProperty("商家推荐")
    private Boolean isRecommend;

    @ApiModelProperty("商品类型")
    @ApiModelPropertyEnum(dictEnum = ProductTypeEnums.class)
    private Integer productType;

    @ApiModelProperty("虚拟发货信息")
    private List<VirtualProductVO> virtualProductVOList;

}

