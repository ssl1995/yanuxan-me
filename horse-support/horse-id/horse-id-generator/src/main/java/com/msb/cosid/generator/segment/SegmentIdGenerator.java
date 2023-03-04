package com.msb.cosid.generator.segment;

import com.msb.cosid.generator.IdGenerator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author liao
 */
@Service
public class SegmentIdGenerator implements IdGenerator {

    @Resource
    private BusinessCosIdSegmentCache businessCosIdSegment;

    @Override
    public Long getLongCosId(String businessId) {
        BusinessCosIdSegmentChain businessCosIdSegmentChain = businessCosIdSegment.getBusinessCosIdGenerator(businessId);
        return businessCosIdSegmentChain.getId();
    }

    @Override
    public BusinessCosIdSegmentChain getBusinessCosIdSegmentChain(String businessId) {
        return businessCosIdSegment.getBusinessCosIdGenerator(businessId);
    }
}
