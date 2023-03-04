package com.msb.mall.comment.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.mall.comment.model.entity.Comment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * (Comment)表数据库访问层
 *
 * @author shumengjiao
 * @since 2022-06-13 21:08:52
 */
public interface CommentMapper extends BaseMapper<Comment> {
    /**
     * 按照评论时间排序
     * @return 评论列表
     */
    @Select("SELECT * FROM comment WHERE product_id = #{productId} AND comment_type = 1 AND is_deleted = 0 ORDER BY create_time DESC")
    List<Comment> listCommentSortByCreateTime(@Param("page") Page<Comment> page, @Param("productId") Long productId);

    /**
     * 按照默认排序规则排序 晒图靠前、评论字数多靠前、有用次数多靠前、评价时间近靠前
     * @return 评论列表
     */
    @Select("SELECT * FROM comment WHERE product_id = #{productId} AND comment_type = 1 AND is_deleted = 0 ORDER BY picture_url DESC, LENGTH( comment_content ) DESC, useful_count DESC, create_time DESC")
    List<Comment> listCommentSortByDefault(@Param("productId") Long productId);

    /**
     * 查询评论列表
     * @return 评论列表
     */
    @Select("SELECT * FROM comment WHERE product_id = #{productId} AND comment_type = 1 AND is_deleted = 0")
    List<Comment> listComment(@Param("page") Page<Comment> page, @Param("productId") Long productId);
}

