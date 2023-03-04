package com.msb.mall.product.model.vo.admin;



import lombok.Data;

import java.io.Serializable;
import io.swagger.annotations.ApiModelProperty;


/**
 * 虚拟商品表(VirtualProduct)表实体类
 *
 * @author shumengjiao
 * @since 2022-05-25 10:13:39
 */
@Data
public class VirtualProductVO implements Serializable {
    
    @ApiModelProperty("发货类型（1-文件 2-消息）")
    private Integer shipType;
    
    @ApiModelProperty("发货内容")
    private String shipContent;
    
    @ApiModelProperty("商品id")
    private Long productId;
    
}

