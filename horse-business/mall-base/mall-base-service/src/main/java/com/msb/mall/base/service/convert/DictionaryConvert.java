package com.msb.mall.base.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.mall.base.model.entity.Dictionary;
import com.msb.mall.base.model.vo.DictionaryVO;
import com.msb.mall.base.model.dto.DictionaryDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 数据字典表（任何有可能修改的以及有业务含义的数据字典或者前端需要用来展示下拉框）(Dictionary)表服务接口
 *
 * @author makejava
 * @date 2022-03-31 15:04:14
 */
@Mapper(componentModel = "spring")
public interface DictionaryConvert {

    DictionaryVO toVo(Dictionary dictionary);

    List<DictionaryVO> toVo(List<Dictionary> dictionary);

    Page<DictionaryVO> toVo(Page<Dictionary> dictionary);

    Dictionary toDo(DictionaryDTO dictionaryDTO);
}

