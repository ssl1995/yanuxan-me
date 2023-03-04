package com.msb.user.core.controller;


import com.msb.user.auth.NoAuth;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 访客pv统计表(VisitorPvStatistics)表控制层
 *
 * @author shumengjiao
 * @since 2022-06-07 19:40:45
 */
@RestController
@Api("访客pv统计")
@NoAuth
@RequestMapping("visitorPvStatistics")
public class VisitorPvStatisticsController {

}

