package com.msb.im.mq.model;

import com.msb.im.model.entity.Message;
import com.msb.im.model.entity.Session;
import com.msb.im.model.entity.SessionUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 消息类型有：
 * 发送消息
 * 消息撤回
 * 更新未读
 * 会话置顶
 * 删除会话用户
 * 删除会话
 *
 * @author zhou miao
 * @date 2022/04/29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImMqMessage implements Serializable {
    /**
     * 用于顺序重复消费
     */
    private String hashKey;
    /**
     * 用于防止重复消费
     */
    private String uuid;
    private Message message;
    private Session session;
    private List<SessionUser> sessionUsers;
    // 消息类型
    private ImMqMessageTypeEnum imMqMessageTypeEnum;
}
