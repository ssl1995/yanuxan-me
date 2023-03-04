package com.msb.pay.model.dto;

import com.msb.pay.model.BaseSign;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("Dubbo支付成功回调确认-DTO")
public class PayNotifyConfirmDTO extends BaseSign implements Serializable {

    @NotBlank
    @Length(max = 64)
    @ApiModelProperty(value = "应用代号", required = true)
    private String appCode;

    @NotBlank
    @ApiModelProperty(value = "支付订单号", required = true)
    private String payOrderNo;

}
