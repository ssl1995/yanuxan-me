package com.msb.sensitive.model.dto;


import com.msb.framework.common.model.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 敏感词库(SensitiveWords)表实体类
 *
 * @author shumengjiao
 * @since 2022-06-15 15:14:12
 */
@Data
public class SensitiveWordsQueryDTO extends PageDTO implements Serializable {

    @ApiModelProperty("内容")
    private String content;
    
    @ApiModelProperty("类型（1-反动词库,2-广告,3-政治类,4-敏感词,5-暴恐词,6-民生词库,7-涉枪涉爆违法信息关键词,8-色情词,9-other,10-广告高危词）")
    private Integer wordsType;
    
    @ApiModelProperty("使用状态（0禁用，1启用）")
    private Integer enabled;
    
}

