package com.msb.mall.trade.third.logistics;

import com.msb.mall.trade.model.vo.app.LogisticsDataVO;
import com.msb.mall.trade.model.vo.app.LogisticsExpressQueryVO;
import com.msb.mall.trade.model.vo.app.LogisticsSubscribeCallbackVO;
import com.msb.mall.trade.model.vo.app.LogisticsSubscribeVO;
import com.msb.mall.trade.third.logistics.config.LogisticsApiConfig;

import java.util.List;

/**
 * 快递API服务接口
 *
 * @author peng.xy
 * @date 2022/4/2
 */
public interface LogisticsApiClient {

    // 订阅类型（1：订单发货物流，2：退款单退货物流）
    int ORDER = 1;
    int REFUND = 2;

    /**
     * 初始化快递API
     *
     * @param apiConfig：apiConfig
     * @return void
     * @author peng.xy
     * @date 2022/4/6
     */
    void initApiConfig(LogisticsApiConfig.ApiConfig apiConfig);

    /**
     * 获取物流API配置ID
     *
     * @return java.lang.String
     * @author peng.xy
     * @date 2022/4/6
     */
    String getLogisticsApiId();

    /**
     * 实时查询快递
     *
     * @param companyCode：快递公司编号
     * @param trackingNo：快递单号
     * @param phone：收件人或寄件人的号码
     * @author peng.xy
     * @date 2022/4/2
     */
    LogisticsExpressQueryVO query(String companyCode, String trackingNo, String phone);

    /**
     * 订阅物流信息推送
     *
     * @param companyCode：快递公司编号
     * @param trackingNo：快递单号
     * @param phone：收件人或寄件人的号码
     * @param type：订阅类型（1：订单发货物流，2：退款单退货物流）
     * @return boolean
     * @author peng.xy
     * @date 2022/4/6
     */
    LogisticsSubscribeVO subscribe(String companyCode, String trackingNo, String phone, int type);

    /**
     * 物流订阅推送回调解析
     *
     * @param param：回调参数
     * @return com.msb.mall.trade.model.vo.app.LogisticsSubscribeCallbackVO
     * @author peng.xy
     * @date 2022/4/11
     */
    LogisticsSubscribeCallbackVO subscribeCallback(String param);

    /**
     * 解析物流数据列表
     *
     * @param data：物流详情数据
     * @return java.util.List<com.msb.mall.trade.model.vo.app.LogisticsDataVO>
     * @author peng.xy
     * @date 2022/4/6
     */
    List<LogisticsDataVO> parseData(String data);

}
