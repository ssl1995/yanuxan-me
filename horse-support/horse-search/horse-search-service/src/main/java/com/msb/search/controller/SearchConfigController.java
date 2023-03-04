package com.msb.search.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.msb.search.model.dto.SearchConfigDTO;
import com.msb.search.model.vo.SearchConfigVO;
import com.msb.search.service.SearchConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * 搜索配置(SearchConfig)表控制层
 *
 * @author luozhan
 * @since 2022-06-10 15:12:59
 */
@Api(tags = "后管-搜索配置管理")
@RestController
@RequestMapping("admin/searchConfig")
public class SearchConfigController {
    /**
     * 服务对象
     */
    @Resource
    private SearchConfigService searchConfigService;

    @ApiOperation("分页查询")
    @GetMapping
    public IPage<SearchConfigVO> page(SearchConfigDTO searchConfigDTO) {
        return searchConfigService.page(searchConfigDTO);
    }

    @ApiOperation("根据id查询")
    @GetMapping("{id}")
    public SearchConfigVO getOne(@PathVariable Serializable id) {
        return this.searchConfigService.getOne(id);
    }

    @ApiOperation("新增")
    @PostMapping
    public Boolean save(@RequestBody SearchConfigDTO searchConfigDTO) {
        return this.searchConfigService.save(searchConfigDTO);
    }

    @ApiOperation("修改")
    @PutMapping
    public Boolean update(@RequestBody SearchConfigDTO searchConfigDTO) {
        return this.searchConfigService.update(searchConfigDTO);
    }

    @ApiOperation("删除（可批量）")
    @DeleteMapping
    public Boolean delete(@ApiParam("id（多个用逗号分隔）") @RequestParam("id") List<Long> idList) {
        return this.searchConfigService.delete(idList);
    }
}

