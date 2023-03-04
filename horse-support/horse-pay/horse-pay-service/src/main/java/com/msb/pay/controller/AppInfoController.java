package com.msb.pay.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.framework.web.result.BizAssert;
import com.msb.framework.web.transform.annotation.Transform;
import com.msb.pay.enums.PayCodeEnum;
import com.msb.pay.model.dto.AppInfoDTO;
import com.msb.pay.model.dto.AppInfoPageDTO;
import com.msb.pay.model.dto.UpdateAppInfoDTO;
import com.msb.pay.model.entity.AppInfo;
import com.msb.pay.model.entity.PayOrder;
import com.msb.pay.model.vo.AppInfoPageVO;
import com.msb.pay.model.vo.AppInfoVO;
import com.msb.pay.model.vo.AppSelectorVO;
import com.msb.pay.service.AppInfoService;
import com.msb.pay.service.PayOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Api(tags = "应用信息")
@RestController
@RequestMapping("/appInfo")
public class AppInfoController {

    @Resource
    private AppInfoService appInfoService;
    @Resource
    private PayOrderService payOrderService;

    @ApiOperation(value = "获取支付方式")
    @GetMapping("/payCode")
    public PayCodeEnum[] payCode() {
        return PayCodeEnum.values();
    }

    @ApiOperation(value = "应用选择框")
    @GetMapping("/appSelector")
    public List<AppSelectorVO> appSelector() {
        return appInfoService.appSelector();
    }

    @Transform
    @ApiOperation(value = "应用列表")
    @GetMapping("/page")
    public Page<AppInfoPageVO> page(@Validated AppInfoPageDTO appInfoPageDTO) {
        return appInfoService.page(appInfoPageDTO);
    }

    @Transform
    @ApiOperation(value = "应用详情")
    @GetMapping("/{appPrimaryId}")
    public AppInfoVO appInfo(@ApiParam("应用主键ID") @PathVariable Long appPrimaryId) {
        return appInfoService.appInfo(appPrimaryId);
    }

    @ApiOperation(value = "新增应用")
    @PostMapping
    public Boolean saveAppInfo(@Validated @RequestBody AppInfoDTO appInfoDTO) {
        return appInfoService.saveAppInfo(appInfoDTO);
    }

    @ApiOperation(value = "修改应用")
    @PutMapping
    public Boolean updateAppInfo(@Validated @RequestBody UpdateAppInfoDTO updateAppInfoDTO) {
        return appInfoService.updateAppInfo(updateAppInfoDTO);
    }

    @ApiOperation(value = "启用/禁用应用")
    @PutMapping("/updateStatus")
    public Boolean update(@ApiParam("应用主键ID") @RequestParam Long appPrimaryId,
                          @ApiParam("应用状态（false：启用，true：禁用）") @RequestParam Boolean isDisabled) {
        return appInfoService.lambdaUpdate().eq(AppInfo::getId, appPrimaryId).set(AppInfo::getIsDisabled, isDisabled).update();
    }

    @ApiOperation(value = "删除应用")
    @DeleteMapping("/{appPrimaryId}")
    public Boolean delete(@ApiParam("应用主键ID") @PathVariable Long appPrimaryId) {
        Integer count = payOrderService.lambdaQuery().eq(PayOrder::getAppPrimaryId, appPrimaryId).count();
        BizAssert.notNull(count > 0, "存在与应用关联的订单，不允许删除");
        return appInfoService.removeById(appPrimaryId);
    }

}
