package com.msb.user.core.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.user.core.mapper.SystemClientMapper;
import com.msb.user.core.model.dto.SystemClientDTO;
import com.msb.user.core.model.entity.SystemClient;
import com.msb.user.core.model.vo.SystemClientVO;
import com.msb.user.core.service.convert.SystemClientConvert;
import org.springframework.stereotype.Service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.msb.framework.common.model.PageDTO;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.io.Serializable;
import java.util.List;

/**
 * 系统客户端表(SystemClient)表服务实现类
 *
 * @author makejava
 * @date 2022-04-25 16:56:40
 */
@Service("systemClientService")
public class SystemClientService extends ServiceImpl<SystemClientMapper, SystemClient> {

    @Resource
    private SystemClientConvert systemClientConvert;

    public IPage<SystemClientVO> page(PageDTO pageDTO, SystemClientDTO systemClientDTO) {
        return systemClientConvert.toVo(this.page(pageDTO.page(), new QueryWrapper<SystemClient>().setEntity(systemClientConvert.toDo(systemClientDTO))));
    }

    public SystemClientVO getOne(Serializable id) {
        return systemClientConvert.toVo(this.getById(id));
    }

    public Boolean save(SystemClientDTO systemClientDTO) {
        return this.save(systemClientConvert.toDo(systemClientDTO));
    }

    public Boolean update(SystemClientDTO systemClientDTO) {
        return this.updateById(systemClientConvert.toDo(systemClientDTO));
    }

    public Boolean delete(List<Long> idList) {
        return this.removeByIds(idList);
    }
}

