package com.msb.mall.base.controller;

import com.msb.mall.base.model.dto.FrontPageOrderQueryDTO;
import com.msb.mall.base.model.dto.FrontPageSalesQueryDTO;
import com.msb.mall.base.model.dto.FrontPageVisitorQueryDTO;
import com.msb.mall.base.model.vo.*;
import com.msb.mall.base.service.FrontPageService;
import com.msb.user.auth.AuthAdmin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author shumengjiao
 */
@Api(tags = "后管-首页Controller")
@AuthAdmin
@RestController
@RequestMapping("frontPage")
public class FrontPageController {
    @Resource
    private FrontPageService frontPageService;

    @ApiOperation("获取头部数据 今日访客 今日订单 今日销售额 近七天销售额")
    @GetMapping("/getHeaderData")
    public FrontPageHeaderDataVO getHeaderData() {
        return frontPageService.getHeaderData();
    }

    @ApiOperation("获取待处理事务数据")
    @GetMapping("/getWaitHandleAffairs")
    public FrontPageWaitHandleVO getWaitHandleAffairs() {
        return frontPageService.getWaitHandleAffairs();
    }

    @ApiOperation("获取商品总览数据")
    @GetMapping("/getProductOverview")
    public FrontPageProductVO getProductOverview() {
        return frontPageService.getProductOverview();
    }

    @ApiOperation("获取用户总览数据")
    @GetMapping("/getUserOverviewData")
    public FrontPageUserVO getUserOverviewData() {
        return frontPageService.getUserOverviewData();
    }

    @ApiOperation("获取销售额同比增长数据")
    @GetMapping("/getSalesTotal")
    public FrontPageSalesTotalVO getSalesTotal() {
        return frontPageService.getSalesTotal();
    }

    @ApiOperation("获取今日销售额统计数据")
    @GetMapping("/listTodaySalesStatistics")
    public List<FrontPageSalesStatisticsByHourVO> listTodaySalesStatistics() {
        return frontPageService.listTodaySalesStatistics();
    }

    @ApiOperation("获取销售额统计数据")
    @GetMapping("/listSalesStatistics")
    public List<FrontPageSalesStatisticsByDayVO> listSalesStatistics(@Valid FrontPageSalesQueryDTO frontPageSalesQueryDTO) {
        return frontPageService.listSalesStatistics(frontPageSalesQueryDTO);
    }

    @ApiOperation("获取订单数量同比增长数据")
    @GetMapping("/getOrderTotal")
    public FrontPageOrderTotalVO getOrderTotal() {
        return frontPageService.getOrderTotal();
    }

    @ApiOperation("获取今日订单统计数据")
    @GetMapping("/listTodayOrderStatistics")
    public List<FrontPageOrderStatisticsByHourVO> listTodayOrderStatistics() {
        return frontPageService.listTodayOrderStatistics();

    }

    @ApiOperation("获取订单统计数据")
    @GetMapping("/listOrderStatistics")
    public List<FrontPageOrderStatisticsByDayVO> listOrderStatistics(@Valid FrontPageOrderQueryDTO frontPageOrderQueryDTO) {
        return frontPageService.listOrderStatistics(frontPageOrderQueryDTO);
    }

    @ApiOperation("获取访客数量同比增长数据")
    @GetMapping("/getVisitorTotal")
    public FrontPageVisitorTotalVO getVisitorTotal() {
        return frontPageService.getVisitorTotal();
    }

    @ApiOperation("获取访客统计数据")
    @GetMapping("/listVisitorStatistics")
    public List<FrontPageVisitorStatisticsByDayVO> listVisitorStatistics(@Valid FrontPageVisitorQueryDTO frontPageVisitorQueryDTO) {
        return frontPageService.listVisitorStatistics(frontPageVisitorQueryDTO);
    }
}
