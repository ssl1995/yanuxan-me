package com.msb.business.dubbo;

import com.msb.business.api.model.HorseUserVO;
import com.msb.business.api.service.DemoDubboService;
import com.msb.business.model.entity.HorseUser;
import com.msb.business.service.HorseUserService;
import com.msb.business.service.convert.HorseUserConvert;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class DubboServiceDemoImpl implements DemoDubboService {

    @Resource
    private HorseUserService horseUserService;

    @Resource
    private HorseUserConvert horseUserConvert;

    @Override
    public HorseUserVO getById(Long id) {
        HorseUser horseUser = horseUserService.getById(id);
        return horseUserConvert.toVo(horseUser);
    }
}
