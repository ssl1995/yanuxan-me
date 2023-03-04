package com.msb.mall.trade.model.vo.app;

import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.framework.web.transform.annotation.TransformEnum;
import com.msb.mall.trade.enums.EvidenceFileEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("APP退款单凭证VO")
public class RefundEvidenceVO {

    @ApiModelProperty("退款单ID")
    private Long refundId;

    @ApiModelPropertyEnum(dictEnum = EvidenceFileEnum.class)
    @ApiModelProperty("文件类型")
    private Integer fileType;

    @TransformEnum(value = EvidenceFileEnum.class, from = "fileType")
    @ApiModelProperty("文件类型文本")
    private String fileTypeDesc;

    @ApiModelProperty("文件地址")
    private String fileUrl;

}
