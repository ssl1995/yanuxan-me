package com.msb.sensitive.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.msb.sensitive.model.dto.SensitiveWordsModifyDTO;
import com.msb.sensitive.model.dto.SensitiveWordsQueryDTO;
import com.msb.sensitive.model.vo.SensitiveWordsVO;
import com.msb.sensitive.service.SensitiveWordsService;
import com.msb.user.auth.AuthAdmin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 敏感词库(SensitiveWords)表控制层
 *
 * @author shumengjiao
 * @since 2022-06-15 15:14:08
 */
@Api(tags = "后管-敏感词")
@AuthAdmin
@RestController
@RequestMapping("admin/sensitiveWords")
public class SensitiveWordsController {
    /**
     * 服务对象
     */
    @Resource
    private SensitiveWordsService sensitiveWordsService;

    @ApiOperation("保存敏感词")
    @PostMapping
    private Boolean save(@Validated SensitiveWordsModifyDTO sensitiveWordsModifyDTO) {
        return sensitiveWordsService.save(sensitiveWordsModifyDTO);
    }

    @ApiOperation("分页查询敏感词")
    @GetMapping
    public IPage<SensitiveWordsVO> page(SensitiveWordsQueryDTO sensitiveWordsQueryDTO) {
        return sensitiveWordsService.page(sensitiveWordsQueryDTO);
    }

    @ApiOperation("更新敏感词")
    @PutMapping
    public Boolean update(SensitiveWordsModifyDTO sensitiveWordsModifyDTO) {
        return sensitiveWordsService.update(sensitiveWordsModifyDTO);
    }

}

