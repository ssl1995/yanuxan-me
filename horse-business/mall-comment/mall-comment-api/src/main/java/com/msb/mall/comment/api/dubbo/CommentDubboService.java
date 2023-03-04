package com.msb.mall.comment.api.dubbo;

import com.msb.mall.comment.api.model.CommentDO;
import com.msb.mall.comment.api.model.CommentDefaultDTO;

import java.util.List;

/**
 *
 * @author 86151
 */
public interface CommentDubboService {

    List<CommentDO> listCommentByProductId(Long productId);

    /**
     * 批量新增默认评价
     * @param list 评价
     */
    void saveDefaultComment(List<CommentDefaultDTO> list);

    /**
     * 根据订单详情id查询评论和追评
     * @param orderProductIdList 订单详情id
     * @return 评论信息
     */
    List<CommentDO> listCommentByOrderProductIds(List<Long> orderProductIdList);

    /**
     * 根据订单详情id查询评论
     * @param orderProductIdList 订单详情id
     * @return 评论信息
     */
    List<CommentDO> listWaitCommentByOrderProductIds(List<Long> orderProductIdList);

    /**
     * 根据订单详情id查询追评
     * @param orderProductIdList 订单详情id
     * @return 追评信息
     */
    List<CommentDO> listWaitFollowByOrderProductIds(List<Long> orderProductIdList);
}
