package com.msb.third.api;

import com.msb.third.enums.WxMpAppEnum;
import com.msb.third.model.dto.*;

/**
 * 微信公众号Dubbo接口
 *
 * @author peng.xy
 * @date 2022/5/12
 */
public interface WxMpDubboService {

    /**
     * 获取AccessToken
     *
     * @param wxMpAppEnum：公众号应用枚举
     * @return java.lang.String
     * @author peng.xy
     * @date 2022/5/12
     */
    String getAccessToken(WxMpAppEnum wxMpAppEnum);

    /**
     * 获取JsapiTicket
     *
     * @param wxMpAppEnum：公众号应用枚举
     * @return java.lang.String
     * @author peng.xy
     * @date 2022/5/12
     */
    String getJsapiTicket(WxMpAppEnum wxMpAppEnum);

    /**
     * 发送模板消息
     *
     * @param templateMessageDTO：模板消息参数
     * @return java.lang.Boolean
     * @author peng.xy
     * @date 2022/5/21
     */
    Boolean sendMpTemplateMessage(TemplateMessageDTO templateMessageDTO);

    /**
     * 发送订单取消通知
     *
     * @param orderCancelMessageDTO：订单信息参数
     * @return java.lang.Boolean
     * @author peng.xy
     * @date 2022/5/18
     */
    Boolean sendOrderCancelMessage(OrderCancelMessageDTO orderCancelMessageDTO);

    /**
     * 发送订单支付成功模板消息
     *
     * @param orderPayMessageDTO：订单信息参数
     * @return java.lang.Boolean
     * @author peng.xy
     * @date 2022/5/13
     */
    Boolean sendOrderPayMessage(OrderPayMessageDTO orderPayMessageDTO);

    /**
     * 发送订单发货模板消息
     *
     * @param orderDeliveryMessageDTO：发货信息参数
     * @return java.lang.Boolean
     * @author peng.xy
     * @date 2022/5/17
     */
    Boolean sendOrderDeliveryMessage(OrderDeliveryMessageDTO orderDeliveryMessageDTO);

    /**
     * 发送填写退货物流模板消息
     *
     * @param returnLogisticsMessageDTO：填写退货物流参数
     * @return java.lang.Boolean
     * @author peng.xy
     * @date 2022/5/17
     */
    Boolean sendReturnLogisticsMessage(ReturnLogisticsMessageDTO returnLogisticsMessageDTO);

    /**
     * 发送退款失败模板消息
     *
     * @param refundFailMessageDTO：退款失败参数
     * @return java.lang.Boolean
     * @author peng.xy
     * @date 2022/5/18
     */
    Boolean sendRefundFailMessage(RefundFailMessageDTO refundFailMessageDTO);

    /**
     * 发送退款成功模板消息
     *
     * @param refundSuccessMessageDTO：退款成功参数
     * @return java.lang.Boolean
     * @author peng.xy
     * @date 2022/5/18
     */
    Boolean sendRefundSuccessMessage(RefundSuccessMessageDTO refundSuccessMessageDTO);
}
