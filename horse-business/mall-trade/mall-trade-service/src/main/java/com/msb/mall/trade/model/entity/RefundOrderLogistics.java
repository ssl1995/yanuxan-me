package com.msb.mall.trade.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.msb.framework.mysql.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * 退货单物流信息(RefundOrderLogistics)表实体类
 *
 * @author makejava
 * @date 2022-04-08 18:24:34
 */
@Accessors(chain = true)
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("refund_order_logistics")
public class RefundOrderLogistics extends BaseEntity implements Serializable {

    /**
     * 物流ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 退款单ID
     */
    private Long refundId;

    /**
     * 收件人姓名
     */
    private String recipientName;

    /**
     * 收件人号码
     */
    private String recipientPhone;

    /**
     * 收货地址
     */
    private String recipientAddress;

    /**
     * 物流公司编号
     */
    private String companyCode;

    /**
     * 物流公司名称
     */
    private String companyName;

    /**
     * 物流单号
     */
    private String trackingNo;

    /**
     * 物流API
     */
    private String logisticsApi;

    /**
     * 物流详情数据
     */
    private String logisticsData;

    /**
     * 是否订阅（0：未订阅，1：已订阅）
     */
    private Boolean isSubscribe;

    /**
     * 物流状态（-1：无数据，0：在途，1：揽收，2：疑难，3：签收，4：退签，5：派件，8：清关，14：拒签）
     */
    private Integer logisticsStatus;


}
