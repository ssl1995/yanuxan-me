package com.msb.mall.trade.third.logistics.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kuaidi100.sdk.api.QueryTrack;
import com.kuaidi100.sdk.api.Subscribe;
import com.kuaidi100.sdk.contant.ApiInfoConstant;
import com.kuaidi100.sdk.core.IBaseClient;
import com.kuaidi100.sdk.pojo.HttpResult;
import com.kuaidi100.sdk.request.*;
import com.kuaidi100.sdk.response.SubscribePushParamResp;
import com.kuaidi100.sdk.response.SubscribePushResult;
import com.kuaidi100.sdk.utils.SignUtils;
import com.msb.framework.common.exception.BizException;
import com.msb.framework.web.result.Assert;
import com.msb.mall.trade.exception.TradeExceptionCodeEnum;
import com.msb.mall.trade.model.vo.app.LogisticsDataVO;
import com.msb.mall.trade.model.vo.app.LogisticsExpressQueryVO;
import com.msb.mall.trade.model.vo.app.LogisticsSubscribeCallbackVO;
import com.msb.mall.trade.model.vo.app.LogisticsSubscribeVO;
import com.msb.mall.trade.third.logistics.AbstractLogisticsApiClient;
import com.msb.mall.trade.third.logistics.LogisticsApiClient;
import com.msb.mall.trade.third.logistics.LogisticsApiConfigEnum;
import com.msb.mall.trade.third.logistics.config.LogisticsApiConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 快递100API实现
 *
 * @author peng.xy
 * @date 2022/4/2
 */
@Slf4j
@Service
public class Express100ApiClient extends AbstractLogisticsApiClient implements LogisticsApiClient, ApplicationRunner {

    @Resource
    private LogisticsApiConfig logisticsApiConfig;

    /**
     * 初始化快递API配置类
     *
     * @author peng.xy
     * @date 2022/4/2
     */
    @Override
    public void run(ApplicationArguments args) {
        super.initApiConfig(logisticsApiConfig.getApiById(LogisticsApiConfigEnum.EXPRESS100.getCode()));
    }


    /**
     * 实时查询快递
     *
     * @param companyCode：快递公司编号
     * @param trackingNo：快递单号
     * @param phone：收件人或寄件人的号码
     * @return com.msb.mall.trade.model.vo.app.LogisticsExpressQueryVO
     * @author peng.xy
     * @date 2022/4/2
     */
    @Override
    public LogisticsExpressQueryVO query(String companyCode, String trackingNo, String phone) {
        QueryTrackParam queryTrackParam = new QueryTrackParam();
        queryTrackParam.setCom(companyCode);
        queryTrackParam.setNum(trackingNo);
        queryTrackParam.setPhone(phone);
        String param = JSONObject.toJSONString(queryTrackParam);

        QueryTrackReq queryTrackReq = new QueryTrackReq();
        queryTrackReq.setParam(param);
        queryTrackReq.setCustomer(apiConfig.getCustomer());
        queryTrackReq.setSign(SignUtils.querySign(param, apiConfig.getKey(), apiConfig.getCustomer()));

        IBaseClient baseClient = new QueryTrack();
        try {
            HttpResult execute = baseClient.execute(queryTrackReq);
            log.info("快递100实时查询快递响应：{}", execute);
            Assert.isTrue(execute.getStatus() == 200, TradeExceptionCodeEnum.EXPRESS100_QUERY_API_ERROR);

            JSONObject body = JSONObject.parseObject(execute.getBody());
            Boolean result = body.getBoolean("result");
            if (Objects.equals(Boolean.FALSE, result)) {
                throw new BizException(body.getString("message"));
            }

            Integer state = body.getInteger("state");
            JSONArray data = body.getJSONArray("data");

            return new LogisticsExpressQueryVO()
                    .setIsSuccess(true)
                    .setLogisticsApi(this.apiConfig.getId())
                    .setStatus(state)
                    .setLogisticsData(data.toJSONString())
                    .setLogisticsDataList(data.toJavaList(LogisticsDataVO.class));
        } catch (Exception e) {
            log.error("调用快递100查询SDK异常", e);
            return new LogisticsExpressQueryVO(false, apiConfig.getId());
        }
    }


    /**
     * 订阅物流信息推送
     *
     * @param companyCode：快递公司编号
     * @param trackingNo：快递单号
     * @param phone：收件人或寄件人的号码
     * @param type：订阅类型（1：订单发货物流，2：退款单退货物流）
     * @return com.msb.mall.trade.model.vo.app.LogisticsSubscribeVO
     * @author peng.xy
     * @date 2022/4/6
     */
    @Override
    public LogisticsSubscribeVO subscribe(String companyCode, String trackingNo, String phone, int type) {
        String callbackUrl;
        switch (type) {
            case ORDER: {
                callbackUrl = apiConfig.getOrderPollUrl();
                break;
            }
            case REFUND: {
                callbackUrl = apiConfig.getRefundPollUrl();
                break;
            }
            default: {
                throw new BizException("订阅类型错误");
            }
        }
        SubscribeParameters subscribeParameters = new SubscribeParameters();
        subscribeParameters.setCallbackurl(callbackUrl);
        subscribeParameters.setPhone(phone);

        SubscribeParam subscribeParam = new SubscribeParam();
        subscribeParam.setParameters(subscribeParameters);
        subscribeParam.setCompany(companyCode);
        subscribeParam.setNumber(trackingNo);
        subscribeParam.setKey(apiConfig.getKey());

        SubscribeReq subscribeReq = new SubscribeReq();
        subscribeReq.setSchema(ApiInfoConstant.SUBSCRIBE_SCHEMA);
        subscribeReq.setParam(JSONObject.toJSONString(subscribeParam));

        IBaseClient subscribe = new Subscribe();
        try {
            HttpResult execute = subscribe.execute(subscribeReq);
            log.info("快递100订阅通知响应：{}", execute);
            Assert.isTrue(execute.getStatus() == 200, TradeExceptionCodeEnum.EXPRESS100_SUBSCRIBE_API_ERROR);

            JSONObject body = JSONObject.parseObject(execute.getBody());
            Boolean result = body.getBoolean("result");
            if (Objects.equals(Boolean.FALSE, result)) {
                throw new BizException(body.getString("message"));
            }
            return new LogisticsSubscribeVO(true, apiConfig.getId());
        } catch (Exception e) {
            log.error("调用快递100订阅SDK异常", e);
            return new LogisticsSubscribeVO(false, apiConfig.getId());
        }
    }

    /**
     * 物流订阅推送回调解析
     *
     * @param param：回调参数
     * @return com.msb.mall.trade.model.vo.app.LogisticsSubscribeCallbackVO
     * @author peng.xy
     * @date 2022/4/11
     */
    @Override
    public LogisticsSubscribeCallbackVO subscribeCallback(String param) {
        SubscribePushParamResp subscribePushParamResp = JSONObject.parseObject(param).toJavaObject(SubscribePushParamResp.class);
        SubscribePushResult lastResult = subscribePushParamResp.getLastResult();
        String status = lastResult.getStatus();
        if (Objects.equals("200", status)) {
            String logisticsData = JSONArray.toJSONString(lastResult.getData());
            return new LogisticsSubscribeCallbackVO()
                    .setIsSuccess(true)
                    .setStatus(Integer.valueOf(lastResult.getState()))
                    .setTrackingNo(lastResult.getNu())
                    .setLogisticsApi(this.apiConfig.getId())
                    .setLogisticsData(logisticsData)
                    .setLogisticsDataList(this.parseData(logisticsData));
        }
        return new LogisticsSubscribeCallbackVO(false, this.apiConfig.getId());
    }

    /**
     * 解析物流数据列表
     *
     * @param data：物流详情数据
     * @return java.util.List<com.msb.mall.trade.model.vo.app.LogisticsDataVO>
     * @author peng.xy
     * @date 2022/4/6
     */
    @Override
    public List<LogisticsDataVO> parseData(String data) {
        if (StringUtils.isBlank(data)) {
            return Collections.emptyList();
        }
        JSONArray jsonArray = JSONArray.parseArray(data);
        return jsonArray.toJavaList(LogisticsDataVO.class);
    }

}
