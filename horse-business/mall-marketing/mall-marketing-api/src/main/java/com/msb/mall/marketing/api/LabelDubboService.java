package com.msb.mall.marketing.api;

import com.msb.mall.marketing.api.model.ProductLabelDO;
import com.msb.mall.marketing.api.model.ProductLabelEnum;

import java.util.List;
import java.util.Map;

public interface LabelDubboService {

    List<ProductLabelDO> listProductLabel(List<Long> productIds);
}
