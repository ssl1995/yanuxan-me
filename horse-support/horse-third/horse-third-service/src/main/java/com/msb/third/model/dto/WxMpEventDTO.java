package com.msb.third.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

@Data
@ApiModel("公众号事件推送DTO")
@XmlRootElement(name = "xml")
public class WxMpEventDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "开发者微信号")
    @XmlElement(name = "ToUserName")
    private String toUserName;

    @ApiModelProperty(value = "发送方帐号（openId）")
    @XmlElement(name = "FromUserName")
    private String fromUserName;

    @ApiModelProperty(value = "消息创建时间（整型）")
    @XmlElement(name = "CreateTime")
    private String createTime;

    @ApiModelProperty(value = "消息类型，event")
    @XmlElement(name = "MsgType")
    private String msgType;

    @ApiModelProperty(value = "事件类型，subscribe")
    @XmlElement(name = "Event")
    private String event;

    @XmlTransient
    public String getToUserName() {
        return toUserName;
    }

    @XmlTransient
    public String getFromUserName() {
        return fromUserName;
    }

    @XmlTransient
    public String getCreateTime() {
        return createTime;
    }

    @XmlTransient
    public String getMsgType() {
        return msgType;
    }

    @XmlTransient
    public String getEvent() {
        return event;
    }

}
