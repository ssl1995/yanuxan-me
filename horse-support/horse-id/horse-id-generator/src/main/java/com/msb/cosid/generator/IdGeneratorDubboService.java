package com.msb.cosid.generator;


import com.msb.cosid.generator.model.SegmentIdDO;

/**
 * @author liao
 */
public interface IdGeneratorDubboService {

    /**
     * 生成号段
     * @return 号段实体
     */
    SegmentIdDO generateSegmentId(String businessId, Long segmentCount);
}
