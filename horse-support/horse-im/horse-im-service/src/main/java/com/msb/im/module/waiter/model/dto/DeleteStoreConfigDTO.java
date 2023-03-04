package com.msb.im.module.waiter.model.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 店铺配置
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Data
@ApiModel("删除店铺DTO")
public class DeleteStoreConfigDTO {
    @ApiModelProperty("id集合")
    @NotEmpty(message = "id不能为空")
    private List<Long> ids;
}
