package com.msb.im.api;

import com.ejlchina.okhttps.HttpUtils;
import com.msb.im.api.config.ApiProperties;
import com.msb.im.api.dto.UpdateSessionUserDTO;
import com.msb.im.api.vo.ResultVO;

import java.util.Objects;

import static com.msb.im.api.constant.ApiConstant.*;

/**
 * @author zhou miao
 * @date 2022/06/02
 */
public class UserApi {

    private String client;

    public UserApi(String client) {
        Objects.requireNonNull(client, "client 为空");
        this.client = client;
    }

    public ResultVO updateUserData(UpdateSessionUserDTO updateSessionUserDTO, String ticket, String fromId) {
        return HttpUtils.sync(ApiProperties.getBaseUrl() + "/api/user")
                .addHeader(client_header, client)
                .addHeader(ticket_header, ticket)
                .addHeader(from_header, fromId)
                .bodyType(bodyType)
                .setBodyPara(updateSessionUserDTO)
                .put()
                .getBody()
                .toBean(ResultVO.class);
    }


}
