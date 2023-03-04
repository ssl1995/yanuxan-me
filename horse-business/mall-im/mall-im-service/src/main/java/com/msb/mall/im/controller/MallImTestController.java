package com.msb.mall.im.controller;

import com.msb.im.api.MallImDubboService;
import com.msb.im.api.dto.SendMessageDTO;
import com.msb.im.api.dto.SendMoreUserMessageDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * (User)表控制层
 *
 * @author makejava
 * @since 2022-03-23 20:13:02
 */
@Api(tags = "im测试相关接口 用完请删除")
@RestController
@RequestMapping("/testIm")
@Slf4j
public class MallImTestController {

    @Resource
    private MallImDubboService mallImDubboService;

    @ApiOperation("批量推送消息")
    @PostMapping("/batchPush")
    public Boolean sendMessage(@RequestBody SendMoreUserMessageDTO sendMessageDTO) {
        return mallImDubboService.sendMessage(sendMessageDTO);
    }

    @ApiOperation("推送消息")
    @PostMapping("/push")
    public Boolean sendMessage(@RequestBody SendMessageDTO sendMessageDTO) {
        return mallImDubboService.sendMessage(sendMessageDTO);
    }

}

