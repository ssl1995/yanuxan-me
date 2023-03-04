package com.msb.mall.comment.dubbo;

import com.msb.mall.comment.api.dubbo.CommentDubboService;
import com.msb.mall.comment.api.model.CommentDO;
import com.msb.mall.comment.api.model.CommentDefaultDTO;
import com.msb.mall.comment.enums.CommentTypeEnum;
import com.msb.mall.comment.service.CommentService;
import com.msb.mall.comment.service.convert.CommentConvert;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.List;

@DubboService
public class CommentDubboServiceImpl implements CommentDubboService {
    @Resource
    private CommentService commentService;
    @Resource
    private CommentConvert commentConvert;

    @Override
    public List<CommentDO> listCommentByProductId(Long productId) {
        return commentConvert.toCommentDO(commentService.listByProductId(productId));
    }

    @Override
    public void saveDefaultComment(List<CommentDefaultDTO> list) {
        commentService.addDefaultComments(list);
    }

    @Override
    public List<CommentDO> listCommentByOrderProductIds(List<Long> orderProductIdList) {
        return commentService.listByOrderProductId(orderProductIdList, CommentTypeEnum.NORMAL_COMMENT.getCode(), CommentTypeEnum.FOLLOW_COMMENT.getCode());
    }

    @Override
    public List<CommentDO> listWaitCommentByOrderProductIds(List<Long> orderProductIdList) {
        return commentService.listByOrderProductId(orderProductIdList, CommentTypeEnum.NORMAL_COMMENT.getCode());
    }

    @Override
    public List<CommentDO> listWaitFollowByOrderProductIds(List<Long> orderProductIdList) {
        return commentService.listByOrderProductId(orderProductIdList, CommentTypeEnum.FOLLOW_COMMENT.getCode());
    }
}
