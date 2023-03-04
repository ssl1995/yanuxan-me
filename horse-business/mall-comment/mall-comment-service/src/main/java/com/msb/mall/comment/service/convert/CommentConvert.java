package com.msb.mall.comment.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.mall.comment.api.model.CommentDO;
import com.msb.mall.comment.api.model.CommentDefaultDTO;
import com.msb.mall.comment.model.dto.admin.CommentAdminQueryDTO;
import com.msb.mall.comment.model.dto.admin.CommentDTO;
import com.msb.mall.comment.model.dto.CommentModifyDTO;
import com.msb.mall.comment.model.entity.Comment;
import com.msb.mall.comment.model.vo.CommentAnswerVO;
import com.msb.mall.comment.model.vo.CommentFollowVO;
import com.msb.mall.comment.model.vo.CommentDetailVO;
import com.msb.mall.comment.model.vo.CommentSimpleVO;
import com.msb.mall.comment.model.vo.admin.CommentAdminVO;
import com.msb.mall.comment.model.vo.app.CommentAppVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * (Comment)表服务接口
 *
 * @author shumengjiao
 * @since 2022-06-13 21:08:57
 */
@Mapper(componentModel = "spring")
public interface CommentConvert {

    CommentAdminVO toAdminVO(Comment comment);

    CommentDetailVO toDetailVo(Comment comment);

    List<CommentAdminVO> toAdminVO(List<Comment> comment);

    List<CommentDetailVO> toDetailVo(List<Comment> comment);

    Page<CommentAdminVO> toAdminVO(Page<Comment> comment);

    Comment toEntity(CommentAdminQueryDTO commentAdminQueryDTO);

    Comment toEntity(CommentDTO commentDTO);

    Comment toEntity(CommentModifyDTO commentModifyDTO);

    List<Comment> toEntity(List<CommentModifyDTO> commentModifyDTOList);

    List<CommentDO> toCommentDO(List<Comment> list);

    List<CommentAppVO> toAppVo(List<Comment> commentList);

    CommentAppVO toAppVo(Comment comment);

    List<Comment> toEntityDefault(List<CommentDefaultDTO> list);

    CommentFollowVO toFollowVo(Comment followComment);

    List<CommentFollowVO> toFollowVo(List<Comment> list);

    List<CommentAnswerVO> toAnswerVo(List<Comment> answerCommentList);

    CommentSimpleVO toVO(Comment comment);
}

