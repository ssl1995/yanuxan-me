package com.msb.mall.comment.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.mall.comment.model.entity.MerchantComment;
import com.msb.mall.comment.model.vo.admin.MerchantCommentVO;
import com.msb.mall.comment.model.dto.admin.MerchantCommentDTO;

import java.util.List;
import org.mapstruct.Mapper;

/**
 * 商家评论(MerchantComment)表服务接口
 *
 * @author shumengjiao
 * @since 2022-06-20 16:14:38
 */
@Mapper(componentModel = "spring")
public interface MerchantCommentConvert {

    MerchantCommentVO toVo(MerchantComment merchantComment);

    List<MerchantCommentVO> toVo(List<MerchantComment> merchantComment);

    Page<MerchantCommentVO> toVo(Page<MerchantComment> merchantComment);

    MerchantComment toEntity(MerchantCommentDTO merchantCommentDTO);
}

