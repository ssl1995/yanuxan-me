package com.msb.like.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.like.api.model.LikesDO;
import com.msb.like.api.model.LikesModifyDO;
import com.msb.like.model.entity.Likes;
import com.msb.like.model.vo.LikesVO;
import com.msb.like.model.dto.LikesDTO;

import java.util.List;
import org.mapstruct.Mapper;

/**
 * 点赞表(Like)表服务接口
 *
 * @author shumengjiao
 * @since 2022-06-22 20:30:58
 */
@Mapper(componentModel = "spring")
public interface LikesConvert {

    LikesVO toVo(Likes likes);

    List<LikesVO> toVo(List<Likes> likes);

    Page<LikesVO> toVo(Page<Likes> like);

    Likes toEntity(LikesDTO likesDTO);

    Likes toEntity(LikesModifyDO likesModifyDO);

    List<LikesDO> toLikeDO(List<Likes> list);

    LikesDO toLikeDO(Likes like);
}

