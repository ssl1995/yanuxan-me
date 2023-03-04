package com.msb.mall.marketing.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.msb.mall.marketing.model.dto.ActivityProductDTO;
import com.msb.mall.marketing.model.dto.ActivityProductQueryDTO;
import com.msb.mall.marketing.model.vo.ActivityProductVO;
import com.msb.mall.marketing.service.ActivityProductService;
import com.msb.user.auth.AuthAdmin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Arrays;

/**
 * 活动商品表(ActivityProduct)表控制层
 *
 * @author makejava
 * @date 2022-04-08 13:38:54
 */
@Api(tags = "（后管）活动商品相关接口")
@AuthAdmin
@RestController
@RequestMapping("activityProduct")
public class ActivityProductController {

    /**
     * 服务对象
     */
    @Resource
    private ActivityProductService activityProductService;

    @ApiOperation("查询某个时间段内的秒杀商品（不带sku信息）")
    @GetMapping
    public IPage<ActivityProductVO> pageActivityProductByTimeId(@Validated ActivityProductQueryDTO activityProductDTO) {
        return activityProductService.pageActivityProduct(activityProductDTO);
    }

    @ApiOperation("添加活动商品")
    @PostMapping
    public Boolean saveActivityProduct(@Validated @RequestBody ActivityProductDTO activityProductDTO) {
        return this.activityProductService.saveActivityProduct(activityProductDTO);
    }

    @ApiOperation("根据id 移除活动时间段的商品")
    @DeleteMapping
    public Boolean deleteActivityProduct(@Validated @NotNull @RequestParam("idList") Long[] idList) {
        return this.activityProductService.delete(Arrays.asList(idList));
    }
}

