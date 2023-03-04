package com.msb.third.dubbo;

import com.msb.third.api.WxMpDubboService;
import com.msb.third.config.wx.mp.WxMpConfig;
import com.msb.third.enums.WxMpAppEnum;
import com.msb.third.enums.WxMpAppMessageTemplateEnum;
import com.msb.third.model.dto.*;
import com.msb.third.model.entity.MpApp;
import com.msb.third.model.entity.MpMessageTemplate;
import com.msb.third.model.entity.ThirdInfo;
import com.msb.third.service.ThirdInfoService;
import com.msb.third.service.ThirdWxMpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Slf4j
@DubboService
public class WxMpDubboServiceImpl implements WxMpDubboService {

    @Resource
    private ThirdWxMpService thirdWxMpService;
    @Resource
    private ThirdInfoService thirdInfoService;
    @Resource
    private WxMpConfig wxMpConfig;

    /**
     * 获取AccessToken
     *
     * @param wxMpAppEnum：公众号应用枚举
     * @return java.lang.String
     * @author peng.xy
     * @date 2022/5/12
     */
    @Override
    public String getAccessToken(WxMpAppEnum wxMpAppEnum) {
        MpApp mpApp = wxMpConfig.getMpAppByMpEnum(wxMpAppEnum);
        return thirdWxMpService.getAccessToken(mpApp.getAppId());
    }

    /**
     * 获取JsapiTicket
     *
     * @param wxMpAppEnum：公众号应用枚举
     * @return java.lang.String
     * @author peng.xy
     * @date 2022/5/12
     */
    @Override
    public String getJsapiTicket(WxMpAppEnum wxMpAppEnum) {
        MpApp mpApp = wxMpConfig.getMpAppByMpEnum(wxMpAppEnum);
        return thirdWxMpService.getJsapiTicket(mpApp.getAppId());
    }

    /**
     * 发送模板消息
     *
     * @param templateMessageDTO：模板消息参数
     * @return java.lang.Boolean
     * @author peng.xy
     * @date 2022/5/21
     */
    @Override
    public Boolean sendMpTemplateMessage(TemplateMessageDTO templateMessageDTO) {
        log.info("发送模板消息：{}", templateMessageDTO);
        // 获取公众号应用
        MpApp mpApp = wxMpConfig.getMpAppByMpEnum(templateMessageDTO.getWxMpAppEnum());
        // 获取消息模板
        MpMessageTemplate messageTemplate = wxMpConfig.getMpMessageTemplate(mpApp.getAppId(), templateMessageDTO.getWxMpAppMessageTemplateEnum());
        // 获取授权关系
        String appId = mpApp.getAppId();
        Long userId = templateMessageDTO.getUserId();
        ThirdInfo thirdInfo = thirdInfoService.getByAppIdAndUserId(appId, userId);
        if (Objects.isNull(thirdInfo)) {
            log.info("用户userId【{}】没有绑定appId【{}】授权关系", userId, appId);
            return false;
        }
        if (!thirdInfo.getIsSubscribe()) {
            log.info("用户userId【{}】没有关注appId【{}】公众号", userId, appId);
            return false;
        }
        return thirdWxMpService.sendTemplateMessage(mpApp.getAppId(), messageTemplate.getTemplateId(), thirdInfo.getAppUserId(), templateMessageDTO.getWebUrl(),
                templateMessageDTO.getFirst(), templateMessageDTO.getRemark(), templateMessageDTO.getKeywords(), templateMessageDTO.getKeywordMap());
    }

    /**
     * 发送模板消息
     *
     * @param templateMessageDTO：模板消息基础DTO
     * @param wxMpAppMessageTemplateEnum：模板消息枚举
     * @param params：参数数组
     * @return boolean
     * @author peng.xy
     * @date 2022/5/18
     */
    private boolean sendMessage(TemplateMessageDTO templateMessageDTO, WxMpAppMessageTemplateEnum wxMpAppMessageTemplateEnum, String[] params) {
        // 获取公众号应用
        MpApp mpApp = wxMpConfig.getMpAppByMpEnum(templateMessageDTO.getWxMpAppEnum());
        // 获取消息模板
        MpMessageTemplate messageTemplate = wxMpConfig.getMpMessageTemplate(mpApp.getAppId(), wxMpAppMessageTemplateEnum);
        // 获取授权关系
        String appId = mpApp.getAppId();
        Long userId = templateMessageDTO.getUserId();
        ThirdInfo thirdInfo = thirdInfoService.getByAppIdAndUserId(appId, userId);
        if (Objects.isNull(thirdInfo)) {
            log.info("用户userId【{}】没有绑定appId【{}】授权关系", userId, appId);
            return false;
        }
        if (!thirdInfo.getIsSubscribe()) {
            log.info("用户userId【{}】没有关注appId【{}】公众号", userId, appId);
            return false;
        }
        String url = null;
        if (StringUtils.isNotBlank(messageTemplate.getUrl()) && Objects.nonNull(templateMessageDTO.getPrimaryId())) {
            url = messageTemplate.getUrl() + templateMessageDTO.getPrimaryId();
        }
        return thirdWxMpService.sendTemplateMessage(mpApp.getAppId(), messageTemplate.getTemplateId(), thirdInfo.getAppUserId(), url,
                messageTemplate.getFirst(), messageTemplate.getRemark(), params, null);
    }

    /**
     * 发送订单取消通知
     *
     * @param orderCancelMessageDTO：订单信息参数
     * @return java.lang.Boolean
     * @author peng.xy
     * @date 2022/5/18
     */
    @Override
    public Boolean sendOrderCancelMessage(OrderCancelMessageDTO orderCancelMessageDTO) {
        log.info("发送订单取消通知，{}", orderCancelMessageDTO);
        String[] params = {orderCancelMessageDTO.getOrderNo(),
                orderCancelMessageDTO.getPayAmount().toString(),
                orderCancelMessageDTO.getProductNames(),
                orderCancelMessageDTO.getCancelReason()};
        return this.sendMessage(orderCancelMessageDTO, WxMpAppMessageTemplateEnum.ORDER_CANCEL, params);
    }

    /**
     * 发送订单支付成功模板消息
     *
     * @param orderPayMessageDTO：订单信息参数
     * @return java.lang.Boolean
     * @author peng.xy
     * @date 2022/5/13
     */
    @Override
    public Boolean sendOrderPayMessage(OrderPayMessageDTO orderPayMessageDTO) {
        log.info("发送订单支付成功模板消息，{}", orderPayMessageDTO);
        String[] params = {orderPayMessageDTO.getUserNickName(),
                orderPayMessageDTO.getOrderNo(),
                orderPayMessageDTO.getPayAmount().toString(),
                orderPayMessageDTO.getProductNames()};
        return this.sendMessage(orderPayMessageDTO, WxMpAppMessageTemplateEnum.ORDER_PAY, params);
    }

    /**
     * 发送订单发货模板消息
     *
     * @param orderDeliveryMessageDTO：发货信息参数
     * @return java.lang.Boolean
     * @author peng.xy
     * @date 2022/5/17
     */
    @Override
    public Boolean sendOrderDeliveryMessage(OrderDeliveryMessageDTO orderDeliveryMessageDTO) {
        log.info("发送订单发货模板消息，{}", orderDeliveryMessageDTO);
        String[] params = {orderDeliveryMessageDTO.getOrderNo(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()),
                orderDeliveryMessageDTO.getCompanyName(),
                orderDeliveryMessageDTO.getTrackingNo()};
        return this.sendMessage(orderDeliveryMessageDTO, WxMpAppMessageTemplateEnum.ORDER_DELIVERY, params);
    }

    /**
     * 发送填写退货物流模板消息
     *
     * @param returnLogisticsMessageDTO：填写退货物流参数
     * @return java.lang.Boolean
     * @author peng.xy
     * @date 2022/5/17
     */
    @Override
    public Boolean sendReturnLogisticsMessage(ReturnLogisticsMessageDTO returnLogisticsMessageDTO) {
        log.info("发送填写退货物流模板消息，{}", returnLogisticsMessageDTO);
        String[] params = {returnLogisticsMessageDTO.getRefundNo(),
                returnLogisticsMessageDTO.getProductName(),
                String.valueOf(returnLogisticsMessageDTO.getQuantity()),
                returnLogisticsMessageDTO.getRefundAmount().toString()};
        return this.sendMessage(returnLogisticsMessageDTO, WxMpAppMessageTemplateEnum.RETURN_LOGISTICS, params);
    }

    /**
     * 发送退款失败模板消息
     *
     * @param refundFailMessageDTO：退款失败参数
     * @return java.lang.Boolean
     * @author peng.xy
     * @date 2022/5/18
     */
    @Override
    public Boolean sendRefundFailMessage(RefundFailMessageDTO refundFailMessageDTO) {
        log.info("发送退款失败模板消息，{}", refundFailMessageDTO);
        String[] params = {refundFailMessageDTO.getRefundNo(),
                refundFailMessageDTO.getProductName(),
                refundFailMessageDTO.getRefundAmount().toString(),
                refundFailMessageDTO.getCloseReason()};
        return this.sendMessage(refundFailMessageDTO, WxMpAppMessageTemplateEnum.REFUND_FAIL, params);
    }

    /**
     * 发送退款成功模板消息
     *
     * @param refundSuccessMessageDTO：退款成功参数
     * @return java.lang.Boolean
     * @author peng.xy
     * @date 2022/5/18
     */
    @Override
    public Boolean sendRefundSuccessMessage(RefundSuccessMessageDTO refundSuccessMessageDTO) {
        log.info("发送退款成功模板消息，{}", refundSuccessMessageDTO);
        String[] params = {refundSuccessMessageDTO.getProductName(),
                refundSuccessMessageDTO.getRefundNo(),
                refundSuccessMessageDTO.getRefundReason(),
                refundSuccessMessageDTO.getRefundAmount().toString()};
        return this.sendMessage(refundSuccessMessageDTO, WxMpAppMessageTemplateEnum.REFUND_SUCCESS, params);
    }

}
