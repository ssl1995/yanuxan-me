package com.msb.like.api.dubbo;

import com.msb.like.api.model.LikesDO;
import com.msb.like.api.model.LikesModifyDO;
import com.msb.like.api.model.LikesQueryDO;

import java.util.List;

/**
 * 点赞dubboe服务
 */
public interface LikesDubboService {

    /**
     * 添加点赞
     * @param likesModifyDO 点赞dto
     * @return 添加结果
     */
    Boolean addLike(LikesModifyDO likesModifyDO);

    /**
     * 查询点赞信息
     * @param likesQueryDO 点赞查询dto
     * @return 点赞信息
     */
    List<LikesDO> listLike(LikesQueryDO likesQueryDO);

    /**
     * 查询单条点赞信息
     * @param likesQueryDO 点赞查询dto
     * @return 点赞信息
     */
    LikesDO getLike(LikesQueryDO likesQueryDO);

    /**
     * 判断单条数据是否点赞
     * @param likesQueryDO 点赞查询dto
     * @return 是否点赞
     */
    Boolean judgeSingleIsLike(LikesQueryDO likesQueryDO);
}
