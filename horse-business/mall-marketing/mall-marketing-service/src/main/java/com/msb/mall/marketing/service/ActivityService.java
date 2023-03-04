package com.msb.mall.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.common.exception.BizException;
import com.msb.framework.web.result.BizAssert;
import com.msb.mall.marketing.mapper.ActivityMapper;
import com.msb.mall.marketing.model.dto.PageActivityDTO;
import com.msb.mall.marketing.model.dto.SaveActivityDTO;
import com.msb.mall.marketing.model.entity.Activity;
import com.msb.mall.marketing.model.entity.ActivityProduct;
import com.msb.mall.marketing.model.vo.ActivityTimeVO;
import com.msb.mall.marketing.model.vo.ActivityVO;
import com.msb.mall.marketing.model.vo.app.ActivityProductTopTreeVO;
import com.msb.mall.marketing.model.vo.app.AppActivityProductVO;
import com.msb.mall.marketing.service.convert.ActivityConvert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 活动表(Activity)表服务实现类
 *
 * @author makejava
 * @date 2022-04-08 13:38:54
 */
@Service("activityService")
public class ActivityService extends ServiceImpl<ActivityMapper, Activity> {

    @Resource
    private ActivityConvert activityConvert;

    @Resource
    private ActivityTimeService activityTimeService;

    @Resource
    private ActivityProductService activityProductService;

    @Resource
    private ActivityCacheService activityCacheService;


    public ActivityVO getActivityOnline(Long activityId) {
        return activityConvert.toVo(this.lambdaQuery().eq(Activity::getId, activityId).eq(Activity::getIsOnline, true).one());
    }

    /**
     * 查询 N天内开启的活动
     */
    public List<Activity> listActivity(int days) {
        LocalDateTime now = LocalDateTime.now();
        //当前时间大于等于 活动开始时间，并且 5天后的时间小于等于活动结束时间
        return this.lambdaQuery()
                .eq(Activity::getIsOnline, true)
                .and(wrapper -> wrapper.between(Activity::getActivityStartTime, now, now.plusDays(days).toLocalDate().atTime(LocalTime.MAX).withNano(0))
                        .or(w -> w.between(Activity::getActivityEndTime, now, now.plusDays(days).toLocalDate().atTime(LocalTime.MAX).withNano(0))))
                .orderByAsc(Activity::getActivityStartTime).list();
    }

    public Optional<Activity> checkActivityProceed(Long activityId) {
        LocalDateTime now = LocalDateTime.now();
        return this.lambdaQuery().eq(Activity::getId, activityId)
                .le(Activity::getActivityStartTime, now)
                .ge(Activity::getActivityEndTime, now)
                .eq(Activity::getIsOnline, Boolean.TRUE)
                .oneOpt();
    }

    public void checkActivityIsStart(Activity activity) {
        if (!activity.getIsOnline()) {
            return;
        }
        BizAssert.isTrue(!LocalDateTime.now().isAfter(activity.getActivityEndTime()), "活动已经结束，无法修改活动");
        BizAssert.isTrue(!LocalDateTime.now().isAfter(activity.getActivityStartTime()), "活动已经开始，无法修改活动");
    }

    private LambdaQueryChainWrapper<Activity> checkActivityTimeOverlap(LocalDate startTime, LocalDate endTime) {
        LocalDateTime startLocalTime = startTime.atTime(LocalTime.MIN);
        LocalDateTime endLocalTime = endTime.atTime(LocalTime.MAX);
        return this.lambdaQuery()
                .and(wrapper -> wrapper
                        .and(w -> w.le(Activity::getActivityStartTime, startLocalTime).ge(Activity::getActivityEndTime, startLocalTime))
                        .or(w -> w.le(Activity::getActivityStartTime, endLocalTime).ge(Activity::getActivityEndTime, endLocalTime))
                );
    }

    private void setActivityStartTimeAndEndTime(Activity activity, SaveActivityDTO saveActivityDTO) {
        activity.setActivityStartTime(saveActivityDTO.getActivityStartTime().atTime(LocalTime.MIN).withNano(0));
        activity.setActivityEndTime(saveActivityDTO.getActivityEndTime().atTime(LocalTime.MAX).withNano(0));
    }

    public Optional<Activity> getCurrentActivity() {
        LocalDateTime now = LocalDateTime.now();
        return this.lambdaQuery()
                .le(Activity::getActivityStartTime, now)
                .ge(Activity::getActivityEndTime, now)
                .eq(Activity::getIsOnline, Boolean.TRUE)
                .list().stream().findFirst();
    }

    public IPage<ActivityVO> pageActivity(PageActivityDTO activityDTO) {
        LambdaQueryChainWrapper<Activity> activityLambdaQueryChainWrapper = this.lambdaQuery().like(StringUtils.isNotBlank(activityDTO.getActivityName()), Activity::getName, activityDTO.getActivityName())
                //开始时间大于当前时间，活动未开始
                .gt(Objects.equals(activityDTO.getActivityStatus(), 1), Activity::getActivityStartTime, LocalDateTime.now())
                //开始时间小于等于当前时间，并且结束时间大于等于当前时间，活动正在进行
                .le(Objects.equals(activityDTO.getActivityStatus(), 2), Activity::getActivityStartTime, LocalDateTime.now()).ge(Objects.equals(activityDTO.getActivityStatus(), 2), Activity::getActivityEndTime, LocalDateTime.now())
                //结束时间小于当前时间，活动已结束
                .lt(Objects.equals(activityDTO.getActivityStatus(), 3), Activity::getActivityEndTime, LocalDateTime.now());
        return activityConvert.toVo(activityLambdaQueryChainWrapper.page(activityDTO.page()));
    }

    public ActivityVO getActivityById(Serializable id) {
        Activity activity = this.getById(id);
        ActivityVO activityVO = activityConvert.toVo(activity);
        List<ActivityTimeVO> activityTimeList = activityTimeService.listActivityTime(activityVO.getId());
        activityVO.setActivityTimeList(activityTimeList);
        return activityVO;
    }

    public ActivityVO save(SaveActivityDTO activityDTO) {
        Activity activity = activityConvert.toEntity(activityDTO);
        setActivityStartTimeAndEndTime(activity, activityDTO);
        checkActivityTimeOverlap(activityDTO.getActivityStartTime(), activityDTO.getActivityEndTime())
                .list().stream().findAny().ifPresent(a -> {
                    throw new BizException("活动时间不可以重叠");
                });
        this.save(activity);
        return activityConvert.toVo(activity);
    }

    public Boolean online(Long id, Boolean online) {
        this.updateById(new Activity().setId(id).setIsOnline(online));
        activityCacheService.updateProductIsActivityByActivity(id);
        return true;
    }

    public Boolean update(SaveActivityDTO activityDTO, Long id) {
        Activity activity = activityConvert.toEntity(activityDTO).setId(id);
        checkActivityIsStart(this.getById(id));
        setActivityStartTimeAndEndTime(activity, activityDTO);
        checkActivityTimeOverlap(activityDTO.getActivityStartTime(), activityDTO.getActivityEndTime())
                .and(activityLambdaQueryWrapper -> activityLambdaQueryWrapper.ne(Activity::getId, id)).list()
                .stream().findAny().ifPresent(activity1 -> {
                    throw new BizException("活动时间不可以重叠");
                });
        this.updateById(activity);
        activityCacheService.updateProductIsActivityByActivity(id);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(List<Long> idList) {
        idList.forEach(id -> checkActivityProceed(id).ifPresent(activity -> {
            throw new BizException("活动正在进行中");
        }));
        idList.forEach(id -> activityCacheService.updateProductIsActivityByActivity(id));
        return this.removeByIds(idList);
    }

    private ActivityProductTopTreeVO buildActivityProductTopTree(ActivityTimeVO activityTimeVO, Boolean isStartActivity) {
        //直接拼当前时间，因为今天只查出来今天的活动
        activityTimeVO.setActivityStartTime(LocalDate.now().atTime(activityTimeVO.getStartTime()));
        activityTimeVO.setActivityEndTime(LocalDate.now().atTime(activityTimeVO.getEndTime()));
        return new ActivityProductTopTreeVO()
                .setActivityTimeVO(activityTimeVO)
                .setIsStartActivity(isStartActivity)
                .setActivityProductListVO(listActivityProductTopTree(activityTimeVO.getId()))
                .setCurrentTime(LocalTime.now());
    }

    private List<AppActivityProductVO> listActivityProductTopTree(Long activityTimeId) {
        List<ActivityProduct> activityProducts = activityProductService.listActivityProduct(activityTimeId, 3);
        return activityProductService.toAppVo(activityProducts);
    }

    public ActivityProductTopTreeVO listActivityProductTopTree() {
        // 查询当前正在进行的活动
        Optional<Activity> currentActivity = getCurrentActivity();
        if (!currentActivity.isPresent()) {
            return new ActivityProductTopTreeVO();
        }
        //查询当前的活动的时间点，判断是否在某一个时间段内
        List<ActivityTimeVO> activityTimeListVO = activityTimeService.listActivityTimeEnable(currentActivity.get().getId());
        //当前时间内
        Optional<ActivityTimeVO> activityTimeOptional = activityTimeService.listActivityTimeInProcess(activityTimeListVO);
        return activityTimeOptional.map(activityTimeVO -> buildActivityProductTopTree(activityTimeVO, Boolean.TRUE))
                .orElseGet(() -> {
                    //判断当前时间是否已经错过所有的秒杀时间段
                    Optional<ActivityTimeVO> firstActivityTimeOptional = activityTimeListVO.stream()
                            .filter(activityTimeVO -> LocalTime.now().isBefore(activityTimeVO.getStartTime())).findFirst();
                    // 有没有错过的秒杀段 ，则显示第一个 没错过的，就是即将开始的
                    return firstActivityTimeOptional.map(activityTimeVO -> buildActivityProductTopTree(activityTimeVO, Boolean.FALSE))
                            .orElseGet(ActivityProductTopTreeVO::new);
                });
    }
}

