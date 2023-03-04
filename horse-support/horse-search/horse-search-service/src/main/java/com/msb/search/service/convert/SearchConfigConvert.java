package com.msb.search.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.search.model.dto.SearchConfigDTO;
import com.msb.search.model.entity.SearchConfig;
import com.msb.search.model.vo.SearchConfigVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 搜索配置(SearchConfig)表服务接口
 *
 * @author luozhan
 * @since 2022-06-10 15:13:02
 */
@Mapper(componentModel = "spring")
public interface SearchConfigConvert {

    SearchConfigVO toVo(SearchConfig searchConfig);

    List<SearchConfigVO> toVo(List<SearchConfig> searchConfig);

    Page<SearchConfigVO> toVo(Page<SearchConfig> searchConfig);

    SearchConfig toDo(SearchConfigDTO searchConfigDTO);
}

