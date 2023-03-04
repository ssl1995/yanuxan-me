package com.msb.business.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.msb.business.client.DubboServiceClient;
import com.msb.business.model.dto.HorseUserDTO;
import com.msb.business.model.vo.HorseUserVO;
import com.msb.business.service.HorseUserService;
import com.msb.framework.common.model.PageDTO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * (HorseUser)表控制层
 *
 * @author makejava
 * @since 2022-03-21 14:57:36
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
    private DubboServiceClient dubboServiceClient;

    @GetMapping
    public IPage<HorseUserVO> page(PageDTO pageDTO, HorseUserDTO horseUserDTO) {
        return horseUserService.page(pageDTO, horseUserDTO);
    }

    @GetMapping("{id}")
    public com.msb.business.api.model.HorseUserVO getOne(@PathVariable Long id) {
        return dubboServiceClient.getById(id);
    }

    @PostMapping
    public Boolean save(@RequestBody HorseUserDTO horseUserDTO) {
        return this.horseUserService.save(horseUserDTO);
    }

    @PutMapping
    public Boolean update(@RequestBody HorseUserDTO horseUserDTO) {
        return this.horseUserService.update(horseUserDTO);
    }

    @DeleteMapping
    public Boolean delete(@RequestParam("idList") List<Long> idList) {
        return this.horseUserService.delete(idList);
    }
}

