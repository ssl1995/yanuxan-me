package com.msb.im.api;

import com.ejlchina.okhttps.HttpUtils;
import com.msb.im.api.config.ApiProperties;
import com.msb.im.api.dto.SendMessageDTO;
import com.msb.im.api.dto.SendMoreUserMessageDTO;
import com.msb.im.api.vo.ResultVO;

import java.util.Objects;

import static com.msb.im.api.constant.ApiConstant.*;

/**
 * @author zhou miao
 * @date 2022/06/02
 */
public class MessageApi  {

    private String client;

    public MessageApi(String client) {
        Objects.requireNonNull(client, "client 为空");
        this.client = client;
    }

    public ResultVO sendMessage(SendMessageDTO sendMessageDTO, String ticket, String fromId) {
        return HttpUtils.sync(ApiProperties.getBaseUrl() + "/api/message/sendMessage")
                .addHeader(client_header, client)
                .addHeader(ticket_header, ticket)
                .addHeader(from_header, fromId)
                .bodyType(bodyType)
                .setBodyPara(sendMessageDTO)
                .post()
                .getBody()
                .toBean(ResultVO.class);
    }

    public ResultVO sendMoreMessage(SendMoreUserMessageDTO sendMessageDTO, String ticket, String fromId) {
        return HttpUtils.sync(ApiProperties.getBaseUrl() + "/api/message/sendMoreMessage")
                .addHeader(client_header, client)
                .addHeader(ticket_header, ticket)
                .addHeader(from_header, fromId)
                .bodyType(bodyType)
                .setBodyPara(sendMessageDTO)
                .post()
                .getBody()
                .toBean(ResultVO.class);
    }
}
