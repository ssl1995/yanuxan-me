package com.msb.like.controller;

import com.msb.like.service.LikesService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 点赞表(Like)表控制层
 *
 * @author shumengjiao
 * @since 2022-06-22 20:30:55
 */
@RestController
@RequestMapping("like")
public class LikesController {
    /**
     * 服务对象
     */
    @Resource
    private LikesService likesService;

}

