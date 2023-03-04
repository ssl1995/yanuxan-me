package com.msb.mall.marketing.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.msb.mall.marketing.model.dto.ActivityProductSkuSaveDTO;
import com.msb.mall.marketing.model.vo.ActivityProductSkuVO;
import com.msb.mall.marketing.model.dto.ActivityProductSkuDTO;
import com.msb.mall.marketing.service.ActivityProductSkuService;
import com.msb.user.auth.AuthAdmin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import com.msb.framework.common.model.PageDTO;
import org.mapstruct.Mapper;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * 活动商品表(ActivityProductSku)表控制层
 *
 * @author makejava
 * @date 2022-04-08 13:38:55
 */
@Api(tags = "（后管）活动商品sku相关接口")
@AuthAdmin
@RestController
@RequestMapping("activityProductSku")
public class ActivityProductSkuController {
    /**
     * 服务对象
     */
    @Resource
    private ActivityProductSkuService activityProductSkuService;

    @ApiOperation("根据活动商品id 查询现有秒杀sku信息")
    @GetMapping
    public List<ActivityProductSkuVO> list(Long activityProductId) {
        return activityProductSkuService.listActivityProductSkuVO(activityProductId);
    }

    @ApiOperation("保存秒杀产品sku信息")
    @PostMapping
    public Boolean save(@RequestBody ActivityProductSkuSaveDTO activityProductSkuDTO) {
        return this.activityProductSkuService.save(activityProductSkuDTO);
    }
}

