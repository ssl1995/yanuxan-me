package com.msb.like.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.common.context.UserContext;
import com.msb.framework.web.result.BizAssert;
import com.msb.like.api.model.LikesDO;
import com.msb.like.api.model.LikesModifyDO;
import com.msb.like.api.model.LikesQueryDO;
import com.msb.like.mapper.LikesMapper;
import com.msb.like.model.entity.Likes;
import com.msb.like.service.convert.LikesConvert;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 点赞表(Like)表服务实现类
 *
 * @author shumengjiao
 * @since 2022-06-22 20:30:57
 */
@Service("likesService")
public class LikesService extends ServiceImpl<LikesMapper, Likes> {

    @Resource
    private LikesConvert likesConvert;

    /**
     * 新增点赞信息
     *
     * @param likesModifyDO 点赞信息
     * @return 新增结果
     */
    public Boolean save(LikesModifyDO likesModifyDO) {
        Boolean isLike = likesModifyDO.getIsLike();
        // 点赞则保存
        if (Boolean.TRUE.equals(isLike)) {
            // 查询是否已存在
            Likes one = this.lambdaQuery().eq(Likes::getSystemId, likesModifyDO.getSystemId()).eq(Likes::getScenesId, likesModifyDO.getScenesId())
                    .eq(Likes::getBusinessId, likesModifyDO.getBusinessId()).eq(Likes::getUserId, likesModifyDO.getUserId()).one();
            BizAssert.isNull(one, "点赞有误，已存在点赞数据");
            Likes likesEntity = likesConvert.toEntity(likesModifyDO);
            return this.save(likesEntity);
        }
        // 否则取消点赞则删除
        Likes one = this.lambdaQuery()
                .select(Likes::getId)
                .eq(Likes::getSystemId, likesModifyDO.getSystemId())
                .eq(Likes::getScenesId, likesModifyDO.getScenesId())
                .eq(Likes::getBusinessId, likesModifyDO.getBusinessId())
                .eq(Likes::getUserId, likesModifyDO.getUserId())
                .one();
        BizAssert.notNull(one, "点赞信息有误");
        return this.removeById(one.getId());
    }

    /**
     * 查询点赞信息
     *
     * @param likesQueryDO 查询dto
     * @return 点赞信息
     */
    public List<LikesDO> listLikes(LikesQueryDO likesQueryDO) {
        List<Likes> list = this.lambdaQuery()
                .eq(Objects.nonNull(likesQueryDO.getSystemId()), Likes::getSystemId, likesQueryDO.getSystemId())
                .eq(Objects.nonNull(likesQueryDO.getScenesId()), Likes::getScenesId, likesQueryDO.getScenesId())
                .in(CollectionUtils.isNotEmpty(likesQueryDO.getBusinessIdList()), Likes::getBusinessId, likesQueryDO.getBusinessIdList())
                .in(CollectionUtils.isNotEmpty(likesQueryDO.getUserIdList()), Likes::getUserId, likesQueryDO.getUserIdList())
                .list();
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return likesConvert.toLikeDO(list);
    }

    /**
     * 查询单条点赞信息
     *
     * @param likesQueryDO 查询dto
     * @return 点赞信息
     */
    public LikesDO getLikeInfo(LikesQueryDO likesQueryDO) {
        Likes like = queryLike(likesQueryDO);
        return likesConvert.toLikeDO(like);
    }

    /**
     * 判断是否点赞
     *
     * @param likesQueryDO
     * @return
     */
    public Boolean judgeSingleIsLike(LikesQueryDO likesQueryDO) {
        BizAssert.notNull(likesQueryDO.getSystemId(), "系统id不能为空");
        BizAssert.notNull(likesQueryDO.getScenesId(), "场景id不能为空");
        BizAssert.notNull(likesQueryDO.getBusinessId(), "业务id不能为空");
        BizAssert.notNull(likesQueryDO.getUserId(), "用户id不能为空");
        Likes like = queryLike(likesQueryDO);
        if (like == null) {
            return false;
        }
        return true;
    }

    /**
     * 查询点赞信息
     *
     * @param likesQueryDO
     * @return
     */
    public Likes queryLike(LikesQueryDO likesQueryDO) {
        return this.lambdaQuery()
                .eq(Objects.nonNull(likesQueryDO.getSystemId()), Likes::getSystemId, likesQueryDO.getSystemId())
                .eq(Objects.nonNull(likesQueryDO.getScenesId()), Likes::getScenesId, likesQueryDO.getScenesId())
                .eq(Objects.nonNull(likesQueryDO.getBusinessId()), Likes::getBusinessId, likesQueryDO.getBusinessId())
                .eq(Objects.nonNull(likesQueryDO.getUserId()), Likes::getUserId, likesQueryDO.getUserId())
                .one();
    }
}

