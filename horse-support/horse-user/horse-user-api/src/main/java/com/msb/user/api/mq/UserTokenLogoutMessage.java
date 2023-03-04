package com.msb.user.api.mq;

import com.msb.framework.common.mq.MqTopic;
import com.msb.framework.common.mq.annotation.Topic;
import com.msb.user.api.mq.body.UserTokenLogoutMessageBody;

/**
 * 用户下线消息
 *
 * @author liao
 */
@Topic("user_token_logout")
public interface UserTokenLogoutMessage extends MqTopic<UserTokenLogoutMessageBody> {

}
