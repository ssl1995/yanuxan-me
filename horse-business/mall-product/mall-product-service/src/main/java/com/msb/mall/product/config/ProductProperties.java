package com.msb.mall.product.config;

import com.msb.mall.product.model.vo.app.ProductRecommendVO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 商品配置类
 *
 * @author luozhan
 */
@Getter
@Setter
@Component
@ConfigurationProperties("product")
public class ProductProperties {
    private List<ProductRecommendVO> recommend;
}
