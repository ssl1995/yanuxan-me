package com.msb.cosid.service.manager;

import com.msb.cosid.generator.model.SegmentIdDO;
import com.msb.cosid.service.model.entity.CosIdSegment;
import com.msb.cosid.service.service.CosIdSegmentService;
import com.msb.framework.web.result.BizAssert;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.swing.text.Segment;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

/**
 * @author liao
 */
@Component
public class CosIdManager {

    @Resource
    private CosIdSegmentService cosIdSegmentService;

    @Transactional(rollbackFor = Exception.class)
    public SegmentIdDO getSegment(String businessId, Long segmentCount) {
        CosIdSegment cosIdSegment = initBusinessSegment(businessId);
        Boolean result = cosIdSegmentService.getBaseMapper().updateCosIdSegmentAutoIncrement(cosIdSegment.getId(), segmentCount);
        BizAssert.isTrue(result, "获取号段异常");
        cosIdSegment = cosIdSegmentService.getById(cosIdSegment.getId());
        return new SegmentIdDO()
                .setStartId(cosIdSegment.getAutoIncrement() - segmentCount)
                .setEndId(cosIdSegment.getAutoIncrement())
                .setBusinessId(businessId);
    }

    public CosIdSegment initBusinessSegment(String businessId) {
        return cosIdSegmentService.saveOrUpdate(businessId);
    }
}
