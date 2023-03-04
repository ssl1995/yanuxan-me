package com.msb.mall.marketing.controller.app;

import com.msb.mall.marketing.model.dto.AdvertisementQueryDTO;
import com.msb.mall.marketing.model.vo.app.AdvertisementSimpleVO;
import com.msb.mall.marketing.service.AdvertisementService;
import com.msb.user.auth.NoAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 广告(Advertisement)表控制层
 *
 * @author shumengjiao
 * @since 2022-05-21 16:36:27
 */
@Api(tags = "（app）广告相关接口")
@NoAuth
@RestController
@RequestMapping("app/advertisement")
public class AppAdvertisementController {

    /**
     * 服务对象
     */
    @Resource
    private AdvertisementService advertisementService;

    @ApiOperation("查询广告信息")
    @GetMapping
    public List<AdvertisementSimpleVO> list(AdvertisementQueryDTO advertisementQueryDTO) {
        return advertisementService.list(advertisementQueryDTO);
    }
}
