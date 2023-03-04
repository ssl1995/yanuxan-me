package com.msb.mall.marketing.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.msb.mall.marketing.model.dto.PageActivityDTO;
import com.msb.mall.marketing.model.dto.SaveActivityDTO;
import com.msb.mall.marketing.model.vo.ActivityVO;
import com.msb.mall.marketing.service.ActivityService;
import com.msb.user.auth.AuthAdmin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * 活动表(Activity)表控制层
 *
 * @author makejava
 * @date 2022-04-08 13:38:53
 */
@Api(tags = "（后管）活动相关接口")
@AuthAdmin
@RestController
@RequestMapping("activity")
public class ActivityController {

    /**
     * 服务对象
     */
    @Resource
    private ActivityService activityService;

    @ApiOperation("分页查询活动列表（不带时间段信息）")
    @GetMapping
    public IPage<ActivityVO> pageActivity(PageActivityDTO activityDTO) {
        return activityService.pageActivity(activityDTO);
    }

    @ApiOperation("根据活动id 获取活动信息，附带活动时间段列表")
    @GetMapping("{id}")
    public ActivityVO getActivityById(@PathVariable Serializable id) {
        return this.activityService.getActivityById(id);
    }

    @ApiOperation("活动保存新增")
    @PostMapping
    public ActivityVO saveActivity(@Validated @RequestBody SaveActivityDTO activityDTO) {
        return this.activityService.save(activityDTO);
    }

    @ApiOperation("活动根据id 修改")
    @PutMapping("{id}")
    public Boolean updateActivity(@Validated @RequestBody SaveActivityDTO activityDTO, @PathVariable Long id) {
        return this.activityService.update(activityDTO, id);
    }

    @ApiOperation("活动上下线")
    @PutMapping("online/{id}")
    public Boolean activityOnline(@NotNull @PathVariable Long id, Boolean isOnline) {
        return this.activityService.online(id, isOnline);
    }

    @ApiOperation("活动根据id删除")
    @DeleteMapping
    public Boolean delete(@Validated @NotEmpty @RequestParam("idList") Long[] idList) {
        return this.activityService.delete(Arrays.asList(idList));
    }
}

