package com.msb.like.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.web.result.BizAssert;
import com.msb.like.mapper.ScenesMapper;
import com.msb.like.model.entity.Scenes;
import org.springframework.stereotype.Service;


/**
 * 场景表(Scenes)表服务实现类
 *
 * @author shumengjiao
 * @since 2022-06-22 20:31:43
 */
@Service("scenesService")
public class ScenesService extends ServiceImpl<ScenesMapper, Scenes> {

    /**
     * 查询场景id
     * @param scenesCode 场景标识
     * @return 场景id
     */
    public Long getScenesId(String scenesCode) {
        Scenes one = this.lambdaQuery().eq(Scenes::getScenesCode, scenesCode).eq(Scenes::getIsDeleted, false).one();
        BizAssert.notNull(one, "场景id查询有误");
        return one.getId();
    }
}

