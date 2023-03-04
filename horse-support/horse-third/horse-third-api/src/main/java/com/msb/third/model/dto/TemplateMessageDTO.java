package com.msb.third.model.dto;

import com.msb.third.enums.WxMpAppEnum;
import com.msb.third.enums.WxMpAppMessageTemplateEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

@Data
@Accessors(chain = true)
@ApiModel("模板消息DTO")
public class TemplateMessageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("公众号应用枚举")
    private WxMpAppEnum wxMpAppEnum;

    @ApiModelProperty("模板消息枚举")
    private WxMpAppMessageTemplateEnum wxMpAppMessageTemplateEnum;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("H5页面地址")
    private String webUrl;

    @ApiModelProperty("消息头部")
    private String first;

    @ApiModelProperty("消息备注")
    private String remark;

    @ApiModelProperty("消息数组")
    private String[] keywords;

    @ApiModelProperty("消息Map")
    private Map<String, String> keywordMap;

    @Deprecated
    @ApiModelProperty("业务主键ID")
    private Long primaryId;

    public TemplateMessageDTO() {
    }

    public TemplateMessageDTO(WxMpAppEnum wxMpAppEnum, Long primaryId, Long userId) {
        this.wxMpAppEnum = wxMpAppEnum;
        this.primaryId = primaryId;
        this.userId = userId;
    }

}
