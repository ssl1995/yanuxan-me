package com.msb.like.dubbo;

import com.msb.like.api.dubbo.LikesDubboService;
import com.msb.like.api.model.LikesDO;
import com.msb.like.api.model.LikesModifyDO;
import com.msb.like.api.model.LikesQueryDO;
import com.msb.like.service.LikesService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.List;

@DubboService
public class LikesDubboServiceImpl implements LikesDubboService {
    @Resource
    private LikesService likesService;

    @Override
    public Boolean addLike(LikesModifyDO likesModifyDO) {
        return likesService.save(likesModifyDO);
    }

    @Override
    public List<LikesDO> listLike(LikesQueryDO likesQueryDO) {
        return likesService.listLikes(likesQueryDO);
    }

    @Override
    public LikesDO getLike(LikesQueryDO likesQueryDO) {
        return likesService.getLikeInfo(likesQueryDO);
    }

    @Override
    public Boolean judgeSingleIsLike(LikesQueryDO likesQueryDO) {
        return likesService.judgeSingleIsLike(likesQueryDO);
    }
}
