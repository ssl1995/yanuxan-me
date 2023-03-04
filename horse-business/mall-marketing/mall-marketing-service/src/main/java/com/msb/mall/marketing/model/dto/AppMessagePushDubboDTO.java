package com.msb.mall.marketing.model.dto;


import com.msb.im.api.dto.CustomTypeDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * app消息推送(AppMessagePush)表实体类
 *
 * @author makejava
 * @date 2022-04-06 14:11:49
 */
@Setter
@Getter
@Accessors(chain = true)
public class AppMessagePushDubboDTO extends CustomTypeDTO implements Serializable {

    @NotBlank
    @ApiModelProperty("消息标题")
    private String title;

    @NotBlank
    @ApiModelProperty("消息内容")
    private String content;

    @ApiModelProperty("链接跳转")
    private String linkJump;

    public AppMessagePushDubboDTO(String customType) {
        super(customType);
    }
}

