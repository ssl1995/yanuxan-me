package com.msb.mall.marketing.dubbo;

import com.msb.mall.marketing.api.LabelDubboService;
import com.msb.mall.marketing.api.model.ProductLabelDO;
import com.msb.mall.marketing.manager.ProductLabelManager;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@DubboService
public class LabelDubboServiceImpl implements LabelDubboService {

    @Resource
    private ProductLabelManager productLabelManager;

    @Override
    public List<ProductLabelDO> listProductLabel(List<Long> productIds) {
        return productIds.parallelStream().map(id -> new ProductLabelDO().setProductId(id).setLabelList(productLabelManager.getProductLabel(id))).collect(Collectors.toList());
    }
}
