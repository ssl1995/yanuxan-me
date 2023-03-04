package com.msb.im.model.vo;

import lombok.Data;

import java.util.List;

/**
 * 会话列表VO
 *
 * @author zhou miao
 * @create: 2022/04/21
 */
@Data
public class SessionListVO {
    private Integer unreadTotal;
    private List<SessionVO>  conversations;
}
