package com.msb.pay.model.dto;

import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.pay.enums.PayCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@ApiModel("测试下单DTO")
public class TestUnifiedOrderDTO implements Serializable {

    @NotBlank
    @Length(max = 64)
    @ApiModelProperty(value = "应用代号", required = true)
    private String appCode;

    @NotBlank
    @Length(max = 64)
    @ApiModelPropertyEnum(dictEnum = PayCodeEnum.class)
    @ApiModelProperty(value = "支付方式", required = true)
    private String payCode;

    @Length(max = 25)
    @NotBlank
    @ApiModelProperty(value = "支付订单号", required = true)
    private String payOrderNo;

    @NotNull
    @DecimalMin("0.01")
    @ApiModelProperty(value = "金额", required = true)
    private BigDecimal amount;

    @NotBlank
    @ApiModelProperty(value = "客户端IP", required = true)
    private String clientIp;

    @NotBlank
    @ApiModelProperty(value = "商品标题", required = true)
    private String subject;

    @NotBlank
    @ApiModelProperty(value = "商品描述", required = true)
    private String body;

    @ApiModelProperty(value = "回调通知地址（为空则采用MQ进行通知）")
    private String notifyUrl;

    @ApiModelProperty(value = "回调页面地址", required = false)
    private String returnUrl;

    @ApiModelProperty(value = "渠道用户ID（微信openId）", required = false)
    private String channelUser;

}
