package com.msb.mall.marketing.manager;

import com.msb.framework.common.model.IDict;
import com.msb.mall.marketing.api.model.ProductLabelEnum;
import com.msb.mall.marketing.manager.label.ProductLabel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class ProductLabelManager {

    @Autowired
    private Map<String, ProductLabel> productLabel;

    public List<ProductLabelEnum> getProductLabel(Long productId) {
        return productLabel.keySet().stream()
                .map((Function<String, ProductLabelEnum>) labelComponentKey -> IDict.getByCode(ProductLabelEnum.class, labelComponentKey))
                .filter(productLabelEnum -> Optional.ofNullable(productLabel.get(productLabelEnum.getCode()).checkLabel(productId)).orElse(Boolean.FALSE))
                .sorted(Comparator.comparingInt(ProductLabelEnum::ordinal)).collect(Collectors.toList());
    }
}
