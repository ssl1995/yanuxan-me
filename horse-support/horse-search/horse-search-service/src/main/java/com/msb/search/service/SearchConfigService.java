package com.msb.search.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.search.mapper.SearchConfigMapper;
import com.msb.search.model.dto.SearchConfigDTO;
import com.msb.search.model.entity.SearchConfig;
import com.msb.search.model.vo.SearchConfigVO;
import com.msb.search.service.convert.SearchConfigConvert;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * 搜索配置(SearchConfig)表服务实现类
 *
 * @author luozhan
 * @since 2022-06-10 15:13:02
 */
@Service("searchConfigService")
public class SearchConfigService extends ServiceImpl<SearchConfigMapper, SearchConfig> {

    @Resource
    private SearchConfigConvert searchConfigConvert;

    public IPage<SearchConfigVO> page(SearchConfigDTO searchConfigDTO) {
        return searchConfigConvert.toVo(this.page(searchConfigDTO.page(), new QueryWrapper<SearchConfig>().setEntity(searchConfigConvert.toDo(searchConfigDTO))));
    }

    public SearchConfigVO getOne(Serializable id) {
        return searchConfigConvert.toVo(this.getById(id));
    }

    public Boolean save(SearchConfigDTO searchConfigDTO) {
        return this.save(searchConfigConvert.toDo(searchConfigDTO));
    }

    public Boolean update(SearchConfigDTO searchConfigDTO) {
        return this.updateById(searchConfigConvert.toDo(searchConfigDTO));
    }

    public Boolean delete(List<Long> idList) {
        return this.removeByIds(idList);
    }
}

