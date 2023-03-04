package com.msb.im.model.bo;

import com.msb.im.model.entity.Message;
import com.msb.im.model.entity.Session;
import lombok.Builder;
import lombok.Data;

/**
 * 发送消息返回结果BO
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Data
@Builder
public class CustomSendMessageResultBO {
    private Message message;
    private Session session;
}
