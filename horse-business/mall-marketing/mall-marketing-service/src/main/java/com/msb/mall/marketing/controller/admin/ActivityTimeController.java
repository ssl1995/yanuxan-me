package com.msb.mall.marketing.controller.admin;


import com.msb.mall.marketing.model.dto.ActivityTimeDTO;
import com.msb.mall.marketing.model.vo.ActivityTimeVO;
import com.msb.mall.marketing.service.ActivityTimeService;
import com.msb.user.auth.AuthAdmin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 活动时段表(ActivityTime)表控制层
 *
 * @author makejava
 * @date 2022-04-08 13:38:55
 */
@Api(tags = "（后管）活动时间段相关")
@AuthAdmin
@RestController
@RequestMapping("activityTime")
public class ActivityTimeController {
    /**
     * 服务对象
     */
    @Resource
    private ActivityTimeService activityTimeService;

    @ApiOperation("根据活动id 获取活动时间段")
    @GetMapping
    public List<ActivityTimeVO> list(Long activityId) {
        return activityTimeService.listActivityTimeAll(activityId);
    }

    @ApiOperation("保存活动时间段")
    @PostMapping
    public Boolean save(@Validated @RequestBody ActivityTimeDTO activityTimeDTO) {
        return this.activityTimeService.save(activityTimeDTO);
    }

    @ApiOperation("修改活动时间段")
    @PutMapping("{id}")
    public Boolean update(@PathVariable Long id, @Validated @RequestBody ActivityTimeDTO activityTimeDTO) {
        return this.activityTimeService.update(id, activityTimeDTO);
    }

    @ApiOperation("时间段开关")
    @PutMapping("/enable/{id}")
    public Boolean enable(@NotNull @PathVariable Long id, Boolean isEnable) {
        return this.activityTimeService.enable(id, isEnable);
    }

    @ApiOperation("删除活动时间段")
    @DeleteMapping
    public Boolean delete(@Validated @NotNull @RequestParam("idList") Long[] idList) {
        return this.activityTimeService.delete(Arrays.asList(idList));
    }
}

