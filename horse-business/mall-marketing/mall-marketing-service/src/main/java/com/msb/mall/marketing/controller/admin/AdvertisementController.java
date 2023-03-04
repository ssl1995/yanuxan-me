package com.msb.mall.marketing.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.msb.mall.marketing.model.dto.AdvertisementPageQueryDTO;
import com.msb.mall.marketing.model.dto.AdvertisementModifyDTO;
import com.msb.mall.marketing.model.dto.AdvertisementSortModifyDTO;
import com.msb.mall.marketing.model.vo.AdvertisementVO;
import com.msb.mall.marketing.service.AdvertisementService;
import com.msb.user.auth.AuthAdmin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 广告(Advertisement)表控制层
 *
 * @author shumengjiao
 * @since 2022-05-21 16:36:27
 */
@Api(tags = "（后管）广告相关接口")
@AuthAdmin
@RestController
@RequestMapping("advertisement")
public class AdvertisementController {
    /**
     * 服务对象
     */
    @Resource
    private AdvertisementService advertisementService;

    @ApiOperation("分页查询广告信息")
    @GetMapping
    public IPage<AdvertisementVO> page(AdvertisementPageQueryDTO advertisementPageQueryDTO) {
        return advertisementService.page(advertisementPageQueryDTO);
    }

    @ApiOperation("根据广告id查询广告信息")
    @GetMapping("{id}")
    public AdvertisementVO getOne(@PathVariable @NotNull Serializable id) {
        return this.advertisementService.getOne(id);
    }

    @ApiOperation("广告保存新增")
    @PostMapping
    public Boolean save(@Validated @RequestBody AdvertisementModifyDTO advertisementModifyDTO) {
        return this.advertisementService.save(advertisementModifyDTO);
    }

    @ApiOperation("根据广告id修改广告信息")
    @PutMapping("{id}")
    public Boolean update(@Validated @RequestBody AdvertisementModifyDTO advertisementModifyDTO, @PathVariable @NotNull Long id) {
        return this.advertisementService.update(advertisementModifyDTO, id);
    }

    @ApiOperation("修改广告排序")
    @PutMapping("updateSort")
    public Boolean updateSort(@Validated @RequestBody AdvertisementSortModifyDTO advertisementSortModifyDTO) {
        return advertisementService.updateSort(advertisementSortModifyDTO);
    }

    @ApiOperation("删除广告")
    @DeleteMapping
    public Boolean delete(@ApiParam("广告id（多个用逗号分隔）") @RequestParam("id") List<Long> idList) {
        return advertisementService.delete(idList);
    }

}

