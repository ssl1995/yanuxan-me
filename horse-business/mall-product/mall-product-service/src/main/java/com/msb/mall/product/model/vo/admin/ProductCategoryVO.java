package com.msb.mall.product.model.vo.admin;


import com.msb.framework.common.model.TreeNode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


/**
 * 商品分类(ProductCategory)表实体类
 *
 * @author luozhan
 * @date 2022-03-29 18:21:57
 */
@Data
public class ProductCategoryVO implements TreeNode<ProductCategoryVO> {

    @ApiModelProperty("商品分类id")
    private Long id;

    @ApiModelProperty("父分类id")
    private Long parentId;

    @ApiModelProperty("分类名称")
    private String name;

    @ApiModelProperty("图片地址")
    private String picture;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("创建人id")
    private Long createUser;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新人id")
    private Long updateUser;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("是否显示")
    private Boolean isEnable;

    private List<ProductCategoryVO> childList;

}

