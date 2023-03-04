package com.msb.mall.marketing.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.common.utils.DateUtil;
import com.msb.framework.web.result.BizAssert;
import com.msb.mall.marketing.mapper.ActivityTimeMapper;
import com.msb.mall.marketing.model.dto.ActivityTimeDTO;
import com.msb.mall.marketing.model.entity.Activity;
import com.msb.mall.marketing.model.entity.ActivityTime;
import com.msb.mall.marketing.model.vo.ActivityTimeVO;
import com.msb.mall.marketing.model.vo.ActivityVO;
import com.msb.mall.marketing.model.vo.app.CurrentActivityTimeVO;
import com.msb.mall.marketing.service.convert.ActivityTimeConvert;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 活动时段表(ActivityTime)表服务实现类
 *
 * @author makejava
 * @date 2022-04-08 13:38:55
 */
@Service("activityTimeService")
public class ActivityTimeService extends ServiceImpl<ActivityTimeMapper, ActivityTime> {

    @Resource
    private ActivityTimeConvert activityTimeConvert;

    @Resource
    private ActivityService activityService;

    @Resource
    private ActivityCacheService activityCacheService;

    @Resource
    private ActivityProductService activityProductService;


    public boolean checkActivityTimeProceed(Long activityTimeId) {
        ActivityTime activityTime = this.getById(activityTimeId);
        //先判断在不在活动时间内，再判断是否在时间段内
        Optional<Activity> activity = activityService.checkActivityProceed(activityTime.getActivityId());
        return activity.map(activity1 -> {
            LocalTime now = LocalTime.now();
            LocalTime startTime = activityTime.getStartTime();
            LocalTime endTime = activityTime.getEndTime();
            return (!(now.isBefore(startTime) || now.isAfter(endTime))) && activityTime.getIsEnable();
        }).orElse(Boolean.FALSE);
    }

    public List<ActivityTimeVO> listActivityTime(Long activityId) {
        return activityTimeConvert.toVo(this.lambdaQuery().eq(ActivityTime::getActivityId, activityId).list());
    }

    public List<ActivityTimeVO> listActivityTimeEnable(Long activityId) {
        return activityTimeConvert.toVo(this.lambdaQuery().eq(ActivityTime::getActivityId, activityId).eq(ActivityTime::getIsEnable, true).list());
    }

    public List<ActivityTimeVO> listActivityTimeAll(Long activityId) {
        ActivityVO activityOnline = activityService.getActivityOnline(activityId);
        int activityState = Optional.ofNullable(activityOnline).map(ActivityVO::getActivityState).orElse(3);
        List<ActivityTimeVO> activityTimeListVO = activityTimeConvert.toVo(this.lambdaQuery().eq(ActivityTime::getActivityId, activityId).list());
        activityTimeListVO.forEach(activityTimeVO -> {
            activityTimeVO.setProductIdList(activityProductService.listActivityProductId(activityTimeVO.getId()));
            switch (activityState) {
                case 1:
                case 3:
                    activityTimeVO.setState(activityState);
                    break;
                case 2:
                    activityTimeVO.setState(Boolean.TRUE.equals(DateUtil.isBetweenTime(LocalTime.now(), activityTimeVO.getStartTime(), activityTimeVO.getEndTime())) ? 2 : 1);
                    break;
                default:
                    break;
            }
        });
        return activityTimeListVO;
    }

    public List<ActivityTime> listActivityTime(List<Long> activityId) {
        if (activityId.isEmpty()) {
            return Collections.emptyList();
        }
        return this.lambdaQuery().in(ActivityTime::getActivityId, activityId).list();
    }


    public Optional<ActivityTimeVO> listActivityTimeInProcess(List<ActivityTimeVO> activityTimeListVO) {
        LocalTime now = LocalTime.now();
        return activityTimeListVO.stream()
                .filter(activityTimeVO -> !(now.isAfter(activityTimeVO.getEndTime()) || now.isBefore(activityTimeVO.getStartTime())))
                .findAny();
    }

    public List<CurrentActivityTimeVO> listCurrentActivityTime() {
        Optional<Activity> currentActivity = activityService.getCurrentActivity();
        LocalTime now = LocalTime.now();
        LocalDate localDate = LocalDate.now();
        return currentActivity.map(activity -> {
            List<ActivityTime> list = this.lambdaQuery()
                    .select(ActivityTime::getId,
                            ActivityTime::getTimeName,
                            ActivityTime::getStartTime,
                            ActivityTime::getEndTime)
                    .eq(ActivityTime::getActivityId, currentActivity.get().getId())
                    .eq(ActivityTime::getIsEnable, true)
                    .ge(ActivityTime::getEndTime, now).orderByAsc(ActivityTime::getStartTime).list();
            List<CurrentActivityTimeVO> currentActivityTimeListVO = activityTimeConvert.toCurrentVo(list);
            currentActivityTimeListVO.forEach(currentActivityTimeVO ->
                    currentActivityTimeVO.setIsInProgress(DateUtil.isBetweenTime(now, currentActivityTimeVO.getStartTime(), currentActivityTimeVO.getEndTime()))
                            .setActivityStartTime(localDate.atTime(currentActivityTimeVO.getStartTime()))
                            .setActivityEndTime(localDate.atTime(currentActivityTimeVO.getEndTime())));
            return currentActivityTimeListVO;
        }).orElse(Collections.emptyList());
    }

    public Boolean save(ActivityTimeDTO activityTimeDTO) {
        return this.save(activityTimeConvert.toDo(activityTimeDTO));
    }

    public Boolean update(Long id, ActivityTimeDTO activityTimeDTO) {
        BizAssert.notTrue(checkActivityTimeProceed(id), "活动正在进行中无法修改");
        this.updateById(activityTimeConvert.toDo(activityTimeDTO).setId(id));
        activityCacheService.updateProductIsActivityByActivityTime(id);
        return true;
    }

    public Boolean enable(Long id, Boolean isEnable) {
        this.updateById(new ActivityTime().setId(id).setIsEnable(isEnable));
        activityCacheService.updateProductIsActivityByActivityTime(id);
        return true;
    }

    public Boolean delete(List<Long> idList) {
        idList.forEach(id -> BizAssert.notTrue(checkActivityTimeProceed(id), "活动正在进行中无法修改"));
        idList.forEach(id -> activityCacheService.updateProductIsActivityByActivityTime(id));
        return this.removeByIds(idList);
    }
}

