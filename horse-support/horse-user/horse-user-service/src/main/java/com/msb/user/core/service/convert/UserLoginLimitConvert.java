package com.msb.user.core.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.user.core.model.entity.UserLoginLimit;
import com.msb.user.core.model.vo.UserLoginLimitVO;
import com.msb.user.core.model.dto.UserLoginLimitDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 用户登录限制(UserLoginLimit)表服务接口
 *
 * @author makejava
 * @date 2022-04-28 20:49:01
 */
@Mapper(componentModel = "spring")
public interface UserLoginLimitConvert {

    UserLoginLimitVO toVo(UserLoginLimit userLoginLimit);

    List<UserLoginLimitVO> toVo(List<UserLoginLimit> userLoginLimit);

    Page<UserLoginLimitVO> toVo(Page<UserLoginLimit> userLoginLimit);

    UserLoginLimit toDo(UserLoginLimitDTO userLoginLimitDTO);
}

