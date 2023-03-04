package com.msb.cosid.generator.segment;


import com.msb.cosid.generator.IdGeneratorDubboService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * @author liao
 */
@Component
public class SegmentGenerator {

    @DubboReference
    private IdGeneratorDubboService idGeneratorDubboService;



}
