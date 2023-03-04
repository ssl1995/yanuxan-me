package com.msb.mall.im.conifg;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zhou miao
 * @date 2022/05/25
 */
@Component
@ConfigurationProperties(prefix = "im")
@Data
public class MallImConfig {
    private String secret;
    private String client;
    private Long systemId = 1L;
}
