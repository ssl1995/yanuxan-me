package com.msb.user.core.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.user.api.vo.UserLoginRecordDO;
import com.msb.user.core.model.entity.UserLoginRecord;
import com.msb.user.core.model.dto.UserLoginRecordDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * (UserLoginRecord)表服务接口
 *
 * @author makejava
 * @since 2022-03-23 20:13:02
 */
@Mapper(componentModel = "spring")
public interface UserLoginRecordConvert {

    UserLoginRecordDO toVo(UserLoginRecord userLoginRecord);

    List<UserLoginRecordDO> toVo(List<UserLoginRecord> userLoginRecord);

    Page<UserLoginRecordDO> toVo(Page<UserLoginRecord> userLoginRecord);

    UserLoginRecord toDo(UserLoginRecordDTO userLoginRecordDTO);
}

