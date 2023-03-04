package com.msb.user.core.controller;

import com.msb.user.auth.NoAuth;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

/**
 * 访客统计表(VisitorStatistics)表控制层
 *
 * @author shumengjiao
 * @since 2022-06-07 12:55:09
 */
@Api("访客uv统计")
@RestController
@NoAuth
@RequestMapping("visitorStatistics")
public class VisitorUvStatisticsController {

}

