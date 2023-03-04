package com.msb.im.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ejlchina.okhttps.HttpUtils;
import com.msb.im.api.config.ApiProperties;
import com.msb.im.api.dto.*;
import com.msb.im.api.vo.ResultVO;

import java.util.Map;
import java.util.Objects;

import static com.msb.im.api.constant.ApiConstant.*;

/**
 * @author zhou miao
 * @date 2022/06/02
 */
public class StoreWaiterApi {

    private String client;

    public StoreWaiterApi(String client) {
        Objects.requireNonNull(client, "client 为空");
        this.client = client;
    }

    public ResultVO add(AddStoreWaiterDTO addStoreWaiterDTO, String ticket, String fromId) {
        return HttpUtils.sync(ApiProperties.getBaseUrl() + "/api/waiter")
                .addHeader(client_header, client)
                .addHeader(ticket_header, ticket)
                .addHeader(from_header, fromId)
                .bodyType(bodyType)
                .setBodyPara(addStoreWaiterDTO)
                .post()
                .getBody()
                .toBean(ResultVO.class);
    }

    public ResultVO delete(DeleteStoreWaiterDTO deleteStoreWaiterDTO, String ticket, String fromId) {
        return HttpUtils.sync(ApiProperties.getBaseUrl() + "/api/waiter")
                .addHeader(client_header, client)
                .addHeader(ticket_header, ticket)
                .addHeader(from_header, fromId)
                .bodyType(bodyType)
                .setBodyPara(deleteStoreWaiterDTO)
                .delete()
                .getBody()
                .toBean(ResultVO.class);
    }

    public ResultVO page(StoreWaiterDTO storeWaiterDTO, String ticket, String fromId) {
        String jsonParam = JSON.toJSONString(storeWaiterDTO);
        return HttpUtils.sync(ApiProperties.getBaseUrl() + "/api/waiter")
                .addHeader(client_header, client)
                .addHeader(ticket_header, ticket)
                .addHeader(from_header, fromId)
                .addUrlPara(JSON.parseObject(jsonParam, Map.class))
                .get()
                .getBody()
                .toBean(ResultVO.class);
    }


    public ResultVO update(UpdateStoreWaiterDTO updateStoreWaiterDTO, String ticket, String fromId) {
        return HttpUtils.sync(ApiProperties.getBaseUrl() + "/api/waiter")
                .addHeader(client_header, client)
                .addHeader(ticket_header, ticket)
                .addHeader(from_header, fromId)
                .bodyType(bodyType)
                .setBodyPara(updateStoreWaiterDTO)
                .put()
                .getBody()
                .toBean(ResultVO.class);
    }

    public ResultVO transfer(TransferWaiterDTO transferWaiterDTO, String ticket, String fromId) {
        return HttpUtils.sync(ApiProperties.getBaseUrl() + "/api/waiter/transfer")
                .addHeader(client_header, client)
                .addHeader(ticket_header, ticket)
                .addHeader(from_header, fromId)
                .bodyType(bodyType)
                .setBodyPara(transferWaiterDTO)
                .post()
                .getBody()
                .toBean(ResultVO.class);
    }

}
