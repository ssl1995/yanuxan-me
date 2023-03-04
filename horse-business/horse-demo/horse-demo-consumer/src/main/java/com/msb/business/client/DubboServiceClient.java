package com.msb.business.client;

import com.msb.business.api.model.HorseUserVO;
import com.msb.business.api.service.DemoDubboService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DubboServiceClient {

    @DubboReference
    private DemoDubboService demoDubboService;

    public HorseUserVO getById(Long id) {
        return demoDubboService.getById(id);
    }
}
