package com.msb.mall.comment.service;

import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import com.msb.framework.common.utils.ListUtil;
import com.msb.framework.redis.RedisClient;
import com.msb.mall.comment.mapper.CommentMapper;
import com.msb.mall.comment.model.entity.Comment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shumengjiao
 */
@Service("commentExtraService")
public class CommentExtraService {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private RedisClient redisClient;

    /**
     * 评论
     */
    private static final String COMMENT_LIST_REDIS_KEY = "horse-comment:comment_sequence:";

    /**
     * 异步对评论按照默认排序规则进行排序且保存到Redis
     */
    @Async
    public void sortCommentByDefaultAndToRedisAsync(Long productId) {
        List<Comment> commentList = commentMapper.listCommentSortByDefault(productId);
        saveToRedis(commentList, productId);
    }

    /**
     * 对评论按照默认排序规则进行排序且保存到Redis
     */
    public void sortCommentByDefaultAndToRedis(Long productId) {
        List<Comment> commentList = commentMapper.listCommentSortByDefault(productId);
        saveToRedis(commentList, productId);
    }

    public synchronized void saveToRedis(List<Comment> commentList, Long productId) {
        if (CollectionUtils.isNotEmpty(commentList)) {
            // 保存到redis中
            // todo 这里循环要改掉
            Object o = redisClient.lPop(COMMENT_LIST_REDIS_KEY + productId);
            while (o != null) {
                o = redisClient.lPop(COMMENT_LIST_REDIS_KEY + productId);
            }
            redisClient.lPushAll(COMMENT_LIST_REDIS_KEY + productId, ListUtil.convert(commentList, Comment::getId));
        }
    }


}
