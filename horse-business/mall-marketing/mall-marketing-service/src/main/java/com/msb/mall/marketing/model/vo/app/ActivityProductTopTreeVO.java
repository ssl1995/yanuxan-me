package com.msb.mall.marketing.model.vo.app;

import com.msb.mall.marketing.model.vo.ActivityTimeVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Accessors(chain = true)
@Data
public class ActivityProductTopTreeVO {

    @ApiModelProperty("马上要进行或者正在进行的秒杀时间段")
    private ActivityTimeVO activityTimeVO;

    @ApiModelProperty("是否开始秒杀，如果没有开始秒杀则是即将开抢")
    private Boolean isStartActivity;

    @ApiModelProperty("活动商品列表")
    private List<AppActivityProductVO> activityProductListVO;

    @ApiModelProperty("当前服务器时间")
    private LocalTime currentTime;
}
