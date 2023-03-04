package com.msb.im.controller.admin;

import com.msb.im.model.vo.CountDaysMessageVO;
import com.msb.im.model.vo.CountHoursMessageVO;
import com.msb.im.netty.service.NettyClusterManager;
import com.msb.im.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author zhou miao
 * @date 2022/05/31
 */
@RestController
@Api(tags = "(后管) 统计接口")
@RequestMapping("/admin/count")
public class AdminCountController {
    @Resource
    private NettyClusterManager nettyClusterManager;
    @Resource
    private MessageService messageService;

    @ApiOperation(value = "查询系统在线人数")
    @GetMapping("online")
    public Integer countOnline(@ApiParam(value = "系统id", required = true) Integer systemId) {
        return nettyClusterManager.getSysOnlineUserCount(systemId);
    }

    @ApiOperation(value = "查询某一天连续小时类的消息量")
    @GetMapping("hoursMessage")
    public List<CountHoursMessageVO> hoursMessage(@ApiParam(value = "系统id", required = true) Integer systemId,
                                                  @ApiParam(value = "某一天", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date days) {
        return messageService.groupHoursByDaysMessage(systemId, days);
    }

    @ApiOperation(value = "指天数间隔查询每日消息量")
    @GetMapping("daysMessage")
    public List<CountDaysMessageVO> daysMessage(@ApiParam(value = "系统id", required = true) Integer systemId,
                                                @ApiParam(value = "开始时间戳", required = true) Long start,
                                                @ApiParam(value = "结束时间戳", required = true) Long end) {
        return messageService.groupDaysMessage(systemId, start, end);
    }
}
