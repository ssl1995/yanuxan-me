package com.msb.pay.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.msb.framework.mysql.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * 商户信息表(MchInfo)表实体类
 *
 * @author makejava
 * @date 2022-06-06 10:42:43
 */
@Accessors(chain = true)
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mch_info")
public class MchInfo extends BaseEntity implements Serializable {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 商户代号（wxpay：微信，alipay：支付宝）
     */
    private String mchCode;

    /**
     * 商户类型（1：普通商户，3：服务商）
     */
    private Integer mchType;

    /**
     * 商户名称
     */
    private String mchName;

    /**
     * 商户ID
     */
    private String mchId;

    /**
     * 商户资料
     */
    private String mchData;

    /**
     * 是否禁用（0：启用，1：禁用）
     */
    private Boolean isDisabled;

}
