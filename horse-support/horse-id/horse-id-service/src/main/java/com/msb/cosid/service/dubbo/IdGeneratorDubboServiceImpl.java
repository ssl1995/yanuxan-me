package com.msb.cosid.service.dubbo;

import com.msb.cosid.generator.IdGeneratorDubboService;
import com.msb.cosid.generator.model.SegmentIdDO;
import com.msb.cosid.service.manager.CosIdManager;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;


/**
 * @author liao
 */
@DubboService
public class IdGeneratorDubboServiceImpl implements IdGeneratorDubboService {

    @Resource
    private CosIdManager cosIdManager;

    @Override
    public SegmentIdDO generateSegmentId(String businessId, Long segmentCount) {
        return cosIdManager.getSegment(businessId, segmentCount);
    }
}
