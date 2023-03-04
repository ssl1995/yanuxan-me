package com.msb.user.core.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.user.core.model.entity.UserSystemRelation;
import com.msb.user.core.model.vo.UserSystemRelationVO;
import com.msb.user.core.model.dto.UserSystemRelationDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 用户系统关联表(UserSystemRelation)表服务接口
 *
 * @author makejava
 * @date 2022-04-25 16:56:40
 */
@Mapper(componentModel = "spring")
public interface UserSystemRelationConvert {

    UserSystemRelationVO toVo(UserSystemRelation userSystemRelation);

    List<UserSystemRelationVO> toVo(List<UserSystemRelation> userSystemRelation);

    Page<UserSystemRelationVO> toVo(Page<UserSystemRelation> userSystemRelation);

    UserSystemRelation toDo(UserSystemRelationDTO userSystemRelationDTO);
}

