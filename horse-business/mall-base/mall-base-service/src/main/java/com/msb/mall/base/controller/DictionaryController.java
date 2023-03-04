package com.msb.mall.base.controller;


import com.msb.mall.base.model.vo.DictionaryVO;
import com.msb.mall.base.service.DictionaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 数据字典表（任何有可能修改的以及有业务含义的数据字典或者前端需要用来展示下拉框）(Dictionary)表控制层
 *
 * @author makejava
 * @date 2022-03-31 15:04:13
 */
@Slf4j
@Api(tags = "字典表管理")
@RestController
@RequestMapping("dictionary")
public class DictionaryController {
    /**
     * 服务对象
     */
    @Resource
    private DictionaryService dictionaryService;

    @ApiOperation("根据type 获取字典列表")
    @GetMapping
    public List<DictionaryVO> listDictionaryByType(String type) {
        return this.dictionaryService.listDictionaryByType(type);
    }

}

