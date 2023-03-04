package com.msb.mall.trade.third.logistics.impl;

import com.alibaba.fastjson.JSONArray;
import com.msb.mall.trade.model.vo.app.LogisticsDataVO;
import com.msb.mall.trade.model.vo.app.LogisticsExpressQueryVO;
import com.msb.mall.trade.model.vo.app.LogisticsSubscribeCallbackVO;
import com.msb.mall.trade.model.vo.app.LogisticsSubscribeVO;
import com.msb.mall.trade.third.logistics.AbstractLogisticsApiClient;
import com.msb.mall.trade.third.logistics.LogisticsApiClient;
import com.msb.mall.trade.third.logistics.LogisticsApiConfigEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 虚拟商品物流
 *
 * @author peng.xy
 * @date 2022/5/26
 */
@Slf4j
@Service
public class VirtualLogisticsClient extends AbstractLogisticsApiClient implements LogisticsApiClient {

    @Override
    public LogisticsExpressQueryVO query(String companyCode, String trackingNo, String phone) {
        return new LogisticsExpressQueryVO(false, LogisticsApiConfigEnum.VIRTUAL.getCode());
    }

    @Override
    public LogisticsSubscribeVO subscribe(String companyCode, String trackingNo, String phone, int type) {
        return new LogisticsSubscribeVO(false, LogisticsApiConfigEnum.VIRTUAL.getCode());
    }

    @Override
    public LogisticsSubscribeCallbackVO subscribeCallback(String param) {
        return new LogisticsSubscribeCallbackVO(false, LogisticsApiConfigEnum.VIRTUAL.getCode());
    }

    @Override
    public List<LogisticsDataVO> parseData(String data) {
        if (StringUtils.isBlank(data)) {
            return Collections.emptyList();
        }
        JSONArray jsonArray = JSONArray.parseArray(data);
        return jsonArray.toJavaList(LogisticsDataVO.class);
    }

}
