package com.msb.im.module.waiter.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 客服统计数据
 *
 * @author zhou miao
 * @date 2022/05/14
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WaiterStatisticsVO {
    private Long historyNum;
    private Long historyPeopleNum;
    private Long todayNum;
    private Long todayPeopleNum;
}
