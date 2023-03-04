package com.msb.push.model;

import com.msb.im.api.enums.MessageTypeEnum;
import com.msb.third.enums.WxMpAppEnum;
import com.msb.third.enums.WxMpAppMessageTemplateEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
@ApiModel("发送消息通知DTO")
public class SendNotifyDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 业务主键ID，例如：订单ID、退款单ID
     */
    @ApiModelProperty("业务主键ID")
    private String primaryId;

    /**
     * 通知的用户ID
     */
    @ApiModelProperty("用户ID")
    private Long userId;

    /**
     * 进行跳转的h5页面地址
     */
    @ApiModelProperty("h5页面地址")
    private String webUrl;

    /**
     * 进行跳转的pc页面地址
     */
    @ApiModelProperty("pc页面地址")
    private String pageUrl;

    /**
     * ----------------------------公众号通知参数----------------------------
     */
    @ApiModelProperty("是否启用公众号通知")
    private Boolean enableWxMpNotify = false;

    @ApiModelProperty("公众号通知参数")
    private MpNotify mpNotifyParam;
    /**
     * ----------------------------IM通知参数----------------------------
     */
    @ApiModelProperty("是否启用公众号通知")
    private Boolean enableImNotify = false;
    @ApiModelProperty("IM通知参数")
    private ImNotify imNotifyParam;

    @Data
    public static class MpNotify implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * 公众号枚举
         */
        @ApiModelProperty("推送的公众号枚举")
        private WxMpAppEnum wxMpAppEnum;

        /**
         * 模板消息枚举
         */
        @ApiModelProperty("模板消息枚举")
        private WxMpAppMessageTemplateEnum wxMpAppMessageTemplateEnum;

        /**
         * 公众号的消息头部
         */
        @ApiModelProperty("消息头部")
        private String first;

        /**
         * 公众号的消息备注
         */
        @ApiModelProperty("消息备注")
        private String remark;

        /**
         * 消息数组，用于公众号模板消息keyword1，keyword2...参数
         */
        @ApiModelProperty("消息数组")
        private String[] keywords;

        /**
         * 消息键值对Map，用于公众号通知中有明确命名的参数
         */
        @ApiModelProperty("消息Map")
        private Map<String, String> keywordMap;

    }

    @Data
    public static class ImNotify implements Serializable {

        private static final long serialVersionUID = 1L;

        @ApiModelProperty("消息类型")
        private MessageTypeEnum type;

        /**
         * 消息标题，例如：您的订单已发货、订单发货通知
         */
        @ApiModelProperty("消息标题")
        private String title;

        /**
         * 主要用于IM消息内容体，例如：您购买的商品【xxx】已经发货，点击查看物流
         */
        @ApiModelProperty("消息内容")
        private String content;


    }

}
