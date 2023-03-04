package com.msb.sensitive.model.dto;



import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import com.msb.framework.common.model.PageDTO;


/**
 * 敏感词库(SensitiveWords)表实体类
 *
 * @author shumengjiao
 * @since 2022-06-15 15:14:12
 */
@Data
public class SensitiveWordsDTO extends PageDTO implements Serializable {

    @ApiModelProperty("敏感词ID")
    private Long id;
    
    @ApiModelProperty("内容")
    private String content;
    
    @ApiModelProperty("类型（1-反动词库,2-广告,3-政治类,4-敏感词,5-暴恐词,6-民生词库,7-涉枪涉爆违法信息关键词,8-色情词,9-other,10-广告高危词）")
    private Integer wordsType;
    
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    
    @ApiModelProperty("创建人")
    private String createUser;
    
    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;
    
    @ApiModelProperty("修改人")
    private String updateUser;
    
    @ApiModelProperty("删除状态（0未删除，1已删除）")
    private Integer isDeleted;
    
    @ApiModelProperty("使用状态（0禁用，1启用）")
    private Integer enabled;
    
}

