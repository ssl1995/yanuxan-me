package com.msb.user.core.model.dto;


import com.msb.framework.common.model.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户登录限制(UserLoginLimit)表实体类
 *
 * @author makejava
 * @date 2022-04-28 20:49:01
 */
@Data
@Accessors(chain = true)
public class UserLoginLimitQueryDTO extends PageDTO implements Serializable {

}

