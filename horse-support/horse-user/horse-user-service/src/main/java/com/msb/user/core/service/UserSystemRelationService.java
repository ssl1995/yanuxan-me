package com.msb.user.core.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.user.core.mapper.UserSystemRelationMapper;
import com.msb.user.core.model.entity.UserSystemRelation;
import com.msb.user.core.model.vo.UserSystemRelationVO;
import com.msb.user.core.model.dto.UserSystemRelationDTO;
import com.msb.user.core.service.convert.UserSystemRelationConvert;
import org.springframework.stereotype.Service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.msb.framework.common.model.PageDTO;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 用户系统关联表(UserSystemRelation)表服务实现类
 *
 * @author makejava
 * @date 2022-04-25 16:56:40
 */
@Service("userSystemRelationService")
public class UserSystemRelationService extends ServiceImpl<UserSystemRelationMapper, UserSystemRelation> {

    @Resource
    private UserSystemRelationConvert userSystemRelationConvert;

    public void save(Long userId, Integer systemId) {
        Optional<UserSystemRelation> userSystemRelationOptional = this.lambdaQuery().eq(UserSystemRelation::getSystemId, systemId).eq(UserSystemRelation::getUserId, userId).oneOpt();
        if (!userSystemRelationOptional.isPresent()) {
            UserSystemRelation userSystemRelation = new UserSystemRelation()
                    .setSystemId(systemId).setUserId(userId);
            this.save(userSystemRelation);
        }
    }

    public List<Long> getUserIdList(Long systemId) {
        return this.lambdaQuery().eq(UserSystemRelation::getSystemId, systemId).list()
                .stream().map(UserSystemRelation::getUserId).collect(Collectors.toList());
    }

    public boolean checkUserSystem(Long userId, Long systemId) {
        return !this.lambdaQuery().eq(UserSystemRelation::getSystemId, systemId).eq(UserSystemRelation::getUserId, userId)
                .list().isEmpty();
    }
}

