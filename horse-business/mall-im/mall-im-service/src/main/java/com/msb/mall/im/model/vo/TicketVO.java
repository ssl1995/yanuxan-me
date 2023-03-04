package com.msb.mall.im.model.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author zhou miao
 * @date 2022/05/25
 */
@Data
@Builder
public class TicketVO {
    private String ticket;
    private String client;
}
