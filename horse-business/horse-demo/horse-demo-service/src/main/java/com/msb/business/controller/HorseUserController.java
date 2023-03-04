package com.msb.business.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.business.api.model.HorseUserVO;
import com.msb.business.model.dto.HorseUserDTO;
import com.msb.business.model.entity.HorseUser;
import com.msb.business.service.HorseUserService;
import com.msb.business.service.convert.HorseUserConvert;
import com.msb.framework.common.model.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * (HorseUser)表控制层
 *
 * @author makejava
 * @since 2022-03-16 11:12:35
 */
@RestController
@RequestMapping("horseUser")
public class HorseUserController {
    /**
     * 服务对象
     */
    @Resource
    private HorseUserService horseUserService;

    @Resource
    private HorseUserConvert horseUserConvert;

    @GetMapping
    public List<HorseUserVO> findHorseUser() {
        List<HorseUser> list = horseUserService.list();
        return horseUserConvert.toVo(list);
    }

    @GetMapping("page")
    @ApiModelProperty
    public IPage<HorseUserVO> pageHorseUser(PageDTO pageDTO) {
        Page<HorseUser> page = horseUserService.page(pageDTO.page());
        return horseUserConvert.toVo(page);
    }


    @PostMapping
    public Boolean addHorseUser(@RequestBody @Valid HorseUserDTO horseUserDTO) {
        HorseUser horseUser = horseUserConvert.toDo(horseUserDTO);
        return horseUserService.save(horseUser);
    }

    @DeleteMapping
    public Boolean delHorseUser(Long id) {
        return horseUserService.removeById(id);
    }
}

