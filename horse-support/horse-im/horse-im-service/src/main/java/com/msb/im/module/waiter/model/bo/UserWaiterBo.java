package com.msb.im.module.waiter.model.bo;

import lombok.Data;


/**
 * 用户和客服实时绑定bo
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Data
public class UserWaiterBo {
    private String userId;
    private Long storeId;
    private Long waiterId;
    private Long sessionId;

    private String account;
    private String nickname;
    private String waiterNickname;
    private Integer type;
    private Boolean isEnabled;
}
