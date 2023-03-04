package com.msb.mall.trade.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.msb.framework.mysql.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * 订单物流信息(TradeOrderLogistics)表实体类
 *
 * @author makejava
 * @since 2022-03-26 17:07:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("trade_order_logistics")
public class TradeOrderLogistics extends BaseEntity implements Serializable {

    /**
     * 物流ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 收件人姓名
     */
    private String recipientName;

    /**
     * 收件人号码
     */
    private String recipientPhone;

    /**
     * 完整收货地址
     */
    private String recipientAddress;

    /**
     * 省区域代码
     */
    private String provinceCode;

    /**
     * 省
     */
    private String province;

    /**
     * 市区域代码
     */
    private String cityCode;

    /**
     * 市
     */
    private String city;

    /**
     * 区/县代码
     */
    private String areaCode;

    /**
     * 区/县
     */
    private String area;

    /**
     * 详细地址
     */
    private String detailAddress;

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
