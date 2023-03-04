package com.msb.user.core.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.framework.common.model.UserLoginInfo;
import com.msb.user.api.vo.UserDO;
import com.msb.user.core.model.dto.UserDTO;
import com.msb.user.core.model.entity.User;
import com.msb.user.core.model.vo.UserVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * (User)表服务接口
 *
 * @author makejava
 * @since 2022-03-23 20:13:02
 */
@Mapper(componentModel = "spring")
public interface UserConvert {

    UserDO toDo(User user);

    List<UserDO> toDo(List<User> user);

    Page<UserDO> toDo(Page<User> user);

    UserVO toVo(User user);

    List<UserVO> toVo(List<User> user);

    Page<UserVO> toVo(Page<User> user);

    User toDo(UserDTO userDTO);

    UserLoginInfo toUserLoginInfo(User user);
}

