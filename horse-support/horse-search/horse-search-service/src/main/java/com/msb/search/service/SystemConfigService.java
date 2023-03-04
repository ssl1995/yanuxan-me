package com.msb.search.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.search.mapper.SystemConfigMapper;
import com.msb.search.model.dto.SystemConfigDTO;
import com.msb.search.model.entity.SystemConfig;
import com.msb.search.model.vo.SystemConfigVO;
import com.msb.search.service.convert.SystemConfigConvert;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (SystemConfig)表服务实现类
 *
 * @author luozhan
 * @since 2022-06-10 14:34:01
 */
@Service("systemConfigService")
public class SystemConfigService extends ServiceImpl<SystemConfigMapper, SystemConfig> {

    @Resource
    private SystemConfigConvert systemConfigConvert;

    public IPage<SystemConfigVO> page(SystemConfigDTO systemConfigDTO) {
        return systemConfigConvert.toVo(this.page(systemConfigDTO.page(), new QueryWrapper<SystemConfig>().setEntity(systemConfigConvert.toDo(systemConfigDTO))));
    }

    public SystemConfigVO getOne(Serializable id) {
        return systemConfigConvert.toVo(this.getById(id));
    }

    public Boolean save(SystemConfigDTO systemConfigDTO) {
        return this.save(systemConfigConvert.toDo(systemConfigDTO));
    }

    public Boolean update(SystemConfigDTO systemConfigDTO) {
        return this.updateById(systemConfigConvert.toDo(systemConfigDTO));
    }

    public Boolean delete(List<Long> idList) {
        return this.removeByIds(idList);
    }
}

