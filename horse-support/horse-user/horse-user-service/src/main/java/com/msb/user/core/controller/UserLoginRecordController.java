package com.msb.user.core.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.msb.user.api.vo.UserLoginRecordDO;
import com.msb.user.core.model.dto.UserLoginRecordDTO;
import com.msb.user.core.service.UserLoginRecordService;
import org.springframework.web.bind.annotation.*;
import com.msb.framework.common.model.PageDTO;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (UserLoginRecord)表控制层
 *
 * @author makejava
 * @since 2022-03-23 20:13:02
 */
@ApiIgnore
@RestController
@RequestMapping("userLoginRecord")
public class UserLoginRecordController {
    /**
     * 服务对象
     */
    @Resource
    private UserLoginRecordService userLoginRecordService;

    @GetMapping
    public IPage<UserLoginRecordDO> page(PageDTO pageDTO, UserLoginRecordDTO userLoginRecordDTO) {
        return userLoginRecordService.page(pageDTO, userLoginRecordDTO);
    }

    @GetMapping("{id}")
    public UserLoginRecordDO getOne(@PathVariable Serializable id) {
        return this.userLoginRecordService.getOne(id);
    }

    @PutMapping
    public Boolean update(@RequestBody UserLoginRecordDTO userLoginRecordDTO) {
        return this.userLoginRecordService.update(userLoginRecordDTO);
    }

    @DeleteMapping
    public Boolean delete(@RequestParam("idList") List<Long> idList) {
        return this.userLoginRecordService.delete(idList);
    }
}

