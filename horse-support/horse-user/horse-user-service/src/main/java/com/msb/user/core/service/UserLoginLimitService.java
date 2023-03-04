package com.msb.user.core.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.user.core.mapper.UserLoginLimitMapper;
import com.msb.user.core.model.dto.UserLoginLimitDTO;
import com.msb.user.core.model.dto.UserLoginLimitQueryDTO;
import com.msb.user.core.model.entity.UserLoginLimit;
import com.msb.user.core.model.vo.UserLoginLimitVO;
import com.msb.user.core.service.convert.UserLoginLimitConvert;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * 用户登录限制(UserLoginLimit)表服务实现类
 *
 * @author makejava
 * @date 2022-04-28 20:49:01
 */
@Service("userLoginLimitService")
public class UserLoginLimitService extends ServiceImpl<UserLoginLimitMapper, UserLoginLimit> {

    @Resource
    private UserLoginLimitConvert userLoginLimitConvert;

    public Optional<UserLoginLimit> getUserLoginLimit(Integer systemId, Integer systemClientId) {
        return this.lambdaQuery().eq(UserLoginLimit::getSystemId, systemId).eq(UserLoginLimit::getSystemClientId, systemClientId).oneOpt();
    }

    public IPage<UserLoginLimitVO> page(UserLoginLimitQueryDTO userLoginLimitQueryDTO) {
        return userLoginLimitConvert.toVo(this.page(userLoginLimitQueryDTO.page()));
    }

    public Boolean save(UserLoginLimitDTO userLoginLimitDTO) {
        return this.save(userLoginLimitConvert.toDo(userLoginLimitDTO));
    }

    public Boolean update(Long id, UserLoginLimitDTO userLoginLimitDTO) {
        return this.updateById(userLoginLimitConvert.toDo(userLoginLimitDTO).setId(id));
    }

    public Boolean delete(List<Long> idList) {
        return this.removeByIds(idList);
    }
}

