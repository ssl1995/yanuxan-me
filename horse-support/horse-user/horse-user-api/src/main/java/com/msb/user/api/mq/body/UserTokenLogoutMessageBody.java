package com.msb.user.api.mq.body;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Accessors(chain = true)
@Data
public class UserTokenLogoutMessageBody implements Serializable {

    private Long userId;

    private String sessionId;

    private String token;

    private Integer systemId;

    private Integer clientId;
}
