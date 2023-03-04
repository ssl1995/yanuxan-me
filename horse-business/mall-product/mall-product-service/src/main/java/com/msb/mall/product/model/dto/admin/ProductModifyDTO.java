package com.msb.mall.product.model.dto.admin;


import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.mall.product.enums.ProductTypeEnums;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;


/**
 * 商品查询条件
 *
 * @author luozhan
 * @date 2022-03-30 18:04:03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("商品数据")
public class ProductModifyDTO {


    @Length(max = 50)
    @NotNull
    @ApiModelProperty("商品名称")
    private String name;

    @NotNull
    @ApiModelProperty("商品分类ID")
    private Long categoryId;

    @Length(max = 200)
    @ApiModelProperty("商品备注")
    private String remark;


    @ApiModelProperty("上下架状态:1-上架,0-下架")
    private Boolean isEnable;

    @Min(0)
    @ApiModelProperty("偏远地区邮费（不填为全国包邮）")
    private BigDecimal remoteAreaPostage;

    @Min(0)
    @ApiModelProperty("单次购买上限（不填不限购）")
    private Integer singleBuyLimit;

    @NotNull
    @Size(min = 1, max = 10)
    @ApiModelProperty("商品图片列表（主图放第一个）")
    private List<String> pictureList;

    @NotNull
    @ApiModelProperty("商品详情")
    private String detail;

    @NotNull
    @ApiModelProperty("商家推荐")
    private Boolean isRecommend;

    @NotNull
    @ApiModelProperty("商品类型")
    @ApiModelPropertyEnum(dictEnum = ProductTypeEnums.class)
    private Integer productType;

    @ApiModelProperty("虚拟商品")
    @Valid
    List<VirtualProductModifyDTO> virtualProductModifyDTOList;

    /**
     * 默认值设置
     */
    public ProductModifyDTO() {
        // 偏远地区运费默认0
        this.remoteAreaPostage = BigDecimal.ZERO;
        // 购买限制默认0-不限制
        this.singleBuyLimit = 0;
    }
}

