package com.msb.search.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.msb.search.model.dto.SystemConfigDTO;
import com.msb.search.model.vo.SystemConfigVO;
import com.msb.search.service.SystemConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (SystemConfig)表控制层
 *
 * @author luozhan
 * @since 2022-06-10 14:34:00
 */
@Api(tags = "后管-系统配置管理")
@RestController
@RequestMapping("admin/systemConfig")
public class SystemConfigController {
    /**
     * 服务对象
     */
    @Resource
    private SystemConfigService systemConfigService;

    @ApiOperation("分页查询")
    @GetMapping
    public IPage<SystemConfigVO> page(SystemConfigDTO systemConfigDTO) {
        return systemConfigService.page(systemConfigDTO);
    }

    @ApiOperation("根据id查询")
    @GetMapping("{id}")
    public SystemConfigVO getOne(@PathVariable Serializable id) {
        return this.systemConfigService.getOne(id);
    }

    @ApiOperation("新增")
    @PostMapping
    public Boolean save(@RequestBody SystemConfigDTO systemConfigDTO) {
        return this.systemConfigService.save(systemConfigDTO);
    }

    @ApiOperation("修改")
    @PutMapping
    public Boolean update(@RequestBody SystemConfigDTO systemConfigDTO) {
        return this.systemConfigService.update(systemConfigDTO);
    }

    @ApiOperation("删除（可批量）")
    @DeleteMapping
    public Boolean delete(@ApiParam("id（多个用逗号分隔）") @RequestParam("id") List<Long> idList) {
        return this.systemConfigService.delete(idList);
    }
}

