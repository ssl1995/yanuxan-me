package com.msb.pay.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.framework.web.result.BizAssert;
import com.msb.framework.web.transform.annotation.Transform;
import com.msb.pay.enums.MchCodeEnum;
import com.msb.pay.model.dto.MchInfoDTO;
import com.msb.pay.model.dto.MchInfoPageDTO;
import com.msb.pay.model.dto.UpdateMchInfoDTO;
import com.msb.pay.model.entity.AppInfo;
import com.msb.pay.model.entity.MchInfo;
import com.msb.pay.model.vo.MchInfoPageVO;
import com.msb.pay.model.vo.MchInfoVO;
import com.msb.pay.model.vo.MchSelectorVO;
import com.msb.pay.service.AppInfoService;
import com.msb.pay.service.MchInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Api(tags = "商户信息")
@RestController
@RequestMapping("/mchInfo")
public class MchInfoController {

    @Resource
    private MchInfoService mchInfoService;
    @Resource
    private AppInfoService appInfoService;

    @ApiOperation(value = "获取商户代号")
    @GetMapping("/mchCode")
    public MchCodeEnum[] mchCode() {
        return MchCodeEnum.values();
    }

    @ApiOperation(value = "商户选择框")
    @GetMapping("/mchSelector")
    public List<MchSelectorVO> mchSelector() {
        return mchInfoService.mchSelector();
    }

    @Transform
    @ApiOperation(value = "商户列表")
    @GetMapping("/page")
    public Page<MchInfoPageVO> page(@Validated MchInfoPageDTO mchInfoPageDTO) {
        return mchInfoService.page(mchInfoPageDTO);
    }

    @Transform
    @ApiOperation(value = "商户详情")
    @GetMapping("/{mchPrimaryId}")
    public MchInfoVO mchInfo(@ApiParam("商户主键ID") @PathVariable Long mchPrimaryId) {
        return mchInfoService.mchInfo(mchPrimaryId);
    }

    @ApiOperation(value = "新增商户")
    @PostMapping
    public Boolean saveMchInfo(@Validated @RequestBody MchInfoDTO mchInfoDTO) {
        return mchInfoService.saveMchInfo(mchInfoDTO);
    }

    @ApiOperation(value = "修改商户")
    @PutMapping
    public Boolean updateMchInfo(@Validated @RequestBody UpdateMchInfoDTO updateMchInfoDTO) {
        return mchInfoService.updateMchInfo(updateMchInfoDTO);
    }

    @ApiOperation(value = "启用/禁用商户")
    @PutMapping("/updateStatus")
    public Boolean update(@ApiParam("商户主键ID") @RequestParam Long mchPrimaryId,
                          @ApiParam("商户状态（false：启用，true：禁用）") @RequestParam Boolean isDisabled) {
        return mchInfoService.lambdaUpdate().eq(MchInfo::getId, mchPrimaryId).set(MchInfo::getIsDisabled, isDisabled).update();
    }

    @ApiOperation(value = "删除商户")
    @DeleteMapping("/{mchPrimaryId}")
    public Boolean delete(@ApiParam("商户主键ID") @PathVariable Long mchPrimaryId) {
        Integer count = appInfoService.lambdaQuery().eq(AppInfo::getMchPrimaryId, mchPrimaryId).count();
        BizAssert.notNull(count > 0, "存在与商户关联的应用，不允许删除");
        return mchInfoService.removeById(mchPrimaryId);
    }

}
