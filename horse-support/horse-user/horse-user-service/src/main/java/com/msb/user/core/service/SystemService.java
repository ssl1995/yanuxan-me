package com.msb.user.core.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.user.core.mapper.SystemMapper;
import com.msb.user.core.model.entity.System;
import com.msb.user.core.model.vo.SystemVO;
import com.msb.user.core.model.dto.SystemDTO;
import com.msb.user.core.service.convert.SystemConvert;
import org.springframework.stereotype.Service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.msb.framework.common.model.PageDTO;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.io.Serializable;
import java.util.List;

/**
 * 系统表(System)表服务实现类
 *
 * @author makejava
 * @date 2022-04-25 16:56:41
 */
@Service("systemService")
public class SystemService extends ServiceImpl<SystemMapper, System> {

    @Resource
    private SystemConvert systemConvert;

    public IPage<SystemVO> page(PageDTO pageDTO, SystemDTO systemDTO) {
        return systemConvert.toVo(this.page(pageDTO.page(), new QueryWrapper<System>().setEntity(systemConvert.toDo(systemDTO))));
    }

    public List<SystemVO> listSystem() {
        return systemConvert.toVo(this.lambdaQuery().list());
    }

    public SystemVO getOne(Serializable id) {
        return systemConvert.toVo(this.getById(id));
    }

    public Boolean save(SystemDTO systemDTO) {
        return this.save(systemConvert.toDo(systemDTO));
    }

    public Boolean update(SystemDTO systemDTO) {
        return this.updateById(systemConvert.toDo(systemDTO));
    }

    public Boolean delete(List<Long> idList) {
        return this.removeByIds(idList);
    }
}

