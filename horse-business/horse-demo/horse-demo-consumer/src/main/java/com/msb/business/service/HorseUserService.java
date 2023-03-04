package com.msb.business.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.business.mapper.HorseUserMapper;
import com.msb.business.model.dto.HorseUserDTO;
import com.msb.business.model.entity.HorseUser;
import com.msb.business.model.vo.HorseUserVO;
import com.msb.business.service.convert.HorseUserConvert;
import com.msb.framework.common.model.PageDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (HorseUser)表服务实现类
 *
 * @author makejava
 * @since 2022-03-21 14:57:36
 */
@Service("horseUserService")
public class HorseUserService extends ServiceImpl<HorseUserMapper, HorseUser> {

    @Resource
    private HorseUserConvert horseUserConvert;

    public IPage<HorseUserVO> page(PageDTO pageDTO, HorseUserDTO horseUserDTO) {
        this.list(new LambdaQueryWrapper<HorseUser>().select(HorseUser::getId, HorseUser::getName));
        return horseUserConvert.toVo(this.page(pageDTO.page(), new QueryWrapper<HorseUser>().setEntity(horseUserConvert.toDo(horseUserDTO))));
    }

    public HorseUserVO getOne(Serializable id) {
        return horseUserConvert.toVo(this.getById(id));
    }

    public Boolean save(HorseUserDTO horseUserDTO) {
        return this.save(horseUserConvert.toDo(horseUserDTO));
    }

    public Boolean update(HorseUserDTO horseUserDTO) {
        return this.updateById(horseUserConvert.toDo(horseUserDTO));
    }

    public Boolean delete(List<Long> idList) {
        return this.removeByIds(idList);
    }
}

