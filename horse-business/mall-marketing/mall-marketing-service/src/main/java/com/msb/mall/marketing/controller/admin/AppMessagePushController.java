package com.msb.mall.marketing.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.msb.mall.marketing.model.dto.AppMessagePushDTO;
import com.msb.mall.marketing.model.dto.AppMessagePushQueryDTO;
import com.msb.mall.marketing.model.vo.AppMessagePushVO;
import com.msb.mall.marketing.service.AppMessagePushService;
import com.msb.user.auth.AuthAdmin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * app消息推送(AppMessagePush)表控制层
 *
 * @author makejava
 * @date 2022-04-06 14:11:49
 */
@AuthAdmin
@Api(tags = "（后管）app消息推送相关接口")
@RestController
@RequestMapping("appMessagePush")
public class AppMessagePushController {
    /**
     * 服务对象
     */
    @Resource
    private AppMessagePushService appMessagePushService;

    @ApiOperation(value = "分页查询")
    @GetMapping
    public IPage<AppMessagePushVO> page(AppMessagePushQueryDTO appMessagePushDTO) {
        return appMessagePushService.page(appMessagePushDTO);
    }

    @ApiOperation(value = "根据id查询")
    @GetMapping("{id}")
    public AppMessagePushVO getOne(@PathVariable Long id) {
        return this.appMessagePushService.getOne(id);
    }

    @ApiOperation(value = "保存app（发布）")
    @PostMapping
    public Boolean save(@Validated @RequestBody AppMessagePushDTO appMessagePushDTO) {
        return this.appMessagePushService.save(appMessagePushDTO);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping
    public Boolean delete(@Validated @NotNull @RequestParam("idList") List<Long> idList) {
        return this.appMessagePushService.delete(idList);
    }
}

