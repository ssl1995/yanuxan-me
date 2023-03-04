package com.msb.user.core.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.web.util.ServletUtil;
import com.msb.user.api.vo.UserLoginRecordDO;
import com.msb.user.core.mapper.UserLoginRecordMapper;
import com.msb.user.core.model.dto.UserLoginRecordDTO;
import com.msb.user.core.model.entity.UserLoginRecord;
import com.msb.user.core.service.convert.UserLoginRecordConvert;
import io.swagger.models.auth.In;
import org.apache.rocketmq.common.message.MessageClientIDSetter;
import org.springframework.stereotype.Service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.msb.framework.common.model.PageDTO;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * (UserLoginRecord)表服务实现类
 *
 * @author makejava
 * @since 2022-03-23 20:13:02
 */
@Service("userLoginRecordService")
public class UserLoginRecordService extends ServiceImpl<UserLoginRecordMapper, UserLoginRecord> {

    @Resource
    private UserLoginRecordConvert userLoginRecordConvert;

    public IPage<UserLoginRecordDO> page(PageDTO pageDTO, UserLoginRecordDTO userLoginRecordDTO) {
        return userLoginRecordConvert.toVo(this.page(pageDTO.page(), new QueryWrapper<UserLoginRecord>().setEntity(userLoginRecordConvert.toDo(userLoginRecordDTO))));
    }

    public UserLoginRecordDO getOne(Serializable id) {
        return userLoginRecordConvert.toVo(this.getById(id));
    }

    public Boolean save(Long userId, String token, Integer systemId, Integer clientId) {
        String clientIp = Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .map(requestAttributes1 -> ServletUtil.getClientIP(((ServletRequestAttributes) requestAttributes1).getRequest()))
                .orElse("");

        UserLoginRecord userLoginRecord = UserLoginRecord.builder()
                .userId(userId)
                .token(token)
                .systemId(systemId)
                .clientId(clientId)
                .ip(clientIp).build();
        return this.save(userLoginRecord);
    }

    public Boolean update(UserLoginRecordDTO userLoginRecordDTO) {
        return this.updateById(userLoginRecordConvert.toDo(userLoginRecordDTO));
    }

    public Boolean delete(List<Long> idList) {
        return this.removeByIds(idList);
    }
}

