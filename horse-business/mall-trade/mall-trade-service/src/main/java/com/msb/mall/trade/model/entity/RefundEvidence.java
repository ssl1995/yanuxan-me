package com.msb.mall.trade.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.msb.framework.mysql.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * 退款单退货凭证(RefundEvidence)表实体类
 *
 * @author makejava
 * @date 2022-04-08 18:24:32
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("refund_evidence")
public class RefundEvidence extends BaseEntity implements Serializable {

    /**
     * 凭证ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 退款单ID
     */
    private Long refundId;

    /**
     * 凭证类型（1：申请凭证，2：物流凭证）
     */
    private Integer evidenceType;

    /**
     * 文件类型（1：图片）
     */
    private Integer fileType;

    /**
     * 文件地址
     */
    private String fileUrl;

    /**
     * 删除状态（0：未删除，1：已删除）
     */
    @TableField(fill = FieldFill.INSERT)
    private Boolean isDeleted;

}
