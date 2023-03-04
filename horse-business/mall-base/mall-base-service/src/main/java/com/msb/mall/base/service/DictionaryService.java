package com.msb.mall.base.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.mall.base.mapper.DictionaryMapper;
import com.msb.mall.base.model.entity.Dictionary;
import com.msb.mall.base.model.vo.DictionaryVO;
import com.msb.mall.base.model.dto.DictionaryDTO;
import com.msb.mall.base.service.convert.DictionaryConvert;
import org.springframework.stereotype.Service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.msb.framework.common.model.PageDTO;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 数据字典表（任何有可能修改的以及有业务含义的数据字典或者前端需要用来展示下拉框）(Dictionary)表服务实现类
 *
 * @author makejava
 * @date 2022-03-31 15:04:14
 */
@Service("dictionaryService")
public class DictionaryService extends ServiceImpl<DictionaryMapper, Dictionary> {

    @Resource
    private DictionaryConvert dictionaryConvert;

    public IPage<DictionaryVO> page(PageDTO pageDTO, DictionaryDTO dictionaryDTO) {
        return dictionaryConvert.toVo(this.page(pageDTO.page(), new QueryWrapper<Dictionary>().setEntity(dictionaryConvert.toDo(dictionaryDTO))));
    }

    public List<DictionaryVO> listDictionaryByType(String type) {
        List<Dictionary> list = this.lambdaQuery().eq(Dictionary::getType, type).eq(Dictionary::getIsEnable, true).list();
        return dictionaryConvert.toVo(list);
    }

    public Boolean save(DictionaryDTO dictionaryDTO) {
        return this.save(dictionaryConvert.toDo(dictionaryDTO));
    }

    public Boolean update(DictionaryDTO dictionaryDTO) {
        return this.updateById(dictionaryConvert.toDo(dictionaryDTO));
    }

    public Boolean delete(List<Long> idList) {
        return this.removeByIds(idList);
    }
}

