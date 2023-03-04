package com.msb.im.convert;


import com.msb.im.model.entity.Session;
import com.msb.im.model.vo.SessionVO;
import org.mapstruct.Mapper;

/**
 *
 * @author zhoumiao
 * @date 2022-04-13 16:29:19
 */
@Mapper(componentModel = "spring")
public interface SessionConvert {

    SessionVO toVo(Session session);


}

