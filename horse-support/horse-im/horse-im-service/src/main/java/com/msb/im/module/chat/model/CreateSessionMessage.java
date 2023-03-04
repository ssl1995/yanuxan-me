package com.msb.im.module.chat.model;

import lombok.Data;

/**
 * 创建会话参数
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Data
public class CreateSessionMessage {
    /**
     * 创建单人会话参数需要使用的参数
     */
    private String toId;
    private String toAvatar;
    private String toNickname;
    private String ticket;
    /**
     * {@link com.msb.im.api.enums.SessionTypeEnum}
     * 单人会话或者客服会话
     */
    private Integer sessionType;

    /**
     * 系统中可以查到的参数，不需要前端传递
     */
    private String fromId;
    private String fromAvatar;
    private String fromNickname;
    private Integer sysId;
}
