package com.msb.im.model.vo;

import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * 会话列表VO
 *
 * @author zhou miao
 * @date 2022/04/21
 */
@Data
public class ListSessionVO {

    public static final ListSessionVO EMPTY;
    static {
        EMPTY = new ListSessionVO();
        EMPTY.setTotalUnreadCount(0L);
        EMPTY.setSessionVOS(Collections.emptyList());
    }

    private Long totalUnreadCount;
    private List<SessionVO> sessionVOS;
}
