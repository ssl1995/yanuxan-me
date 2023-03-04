package com.msb.mall.marketing.controller.app;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.msb.framework.common.model.PageDTO;
import com.msb.mall.marketing.model.vo.app.ActivityProductTopTreeVO;
import com.msb.mall.marketing.model.vo.app.AppActivityProductVO;
import com.msb.mall.marketing.model.vo.app.CurrentActivityTimeVO;
import com.msb.mall.marketing.service.ActivityProductService;
import com.msb.mall.marketing.service.ActivityService;
import com.msb.mall.marketing.service.ActivityTimeService;
import com.msb.user.auth.NoAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

@Api(tags = "（app）秒杀相关接口")
@NoAuth
@RestController
@RequestMapping("app/activity")
public class AppActivityController {

    @Resource
    private ActivityService activityService;

    @Resource
    private ActivityTimeService activityTimeService;

    @Resource
    private ActivityProductService activityProductService;

    @ApiOperation("首页秒杀活动查看")
    @GetMapping("home")
    public ActivityProductTopTreeVO home() {
        return activityService.listActivityProductTopTree();
    }

    @ApiOperation("查询当天秒杀时段")
    @GetMapping("time")
    public List<CurrentActivityTimeVO> listActivityTime() {
        return activityTimeService.listCurrentActivityTime();
    }

    @ApiOperation("查询根据时间段查询秒杀列表")
    @GetMapping("product")
    public IPage<AppActivityProductVO> pageActivityProduct(@Validated PageDTO pageDTO, @Validated @NotNull Long activityTimeId) {
        return activityProductService.pageActivityProduct(pageDTO, activityTimeId);
    }

    @ApiOperation("获取服务器当前时间戳")
    @GetMapping("timestamp")
    public Long currentTimestamp() {
        return System.currentTimeMillis();
    }
}