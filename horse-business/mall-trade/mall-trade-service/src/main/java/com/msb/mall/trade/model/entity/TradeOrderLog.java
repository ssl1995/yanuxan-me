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
 * 订单操作记录(TradeOrderLog)表实体类
 *
 * @author makejava
 * @since 2022-03-24 18:30:18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("trade_order_log")
public class TradeOrderLog extends BaseEntity implements Serializable {

    /**
     * 记录ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 操作类型（1：提交订单，2：超时自动取消，3：用户手动取消，4：后台手动取消，5：后台手动关闭，6：支付订单，7：平台发货，8：用户确认收货，9：超时自动收货，10：用户完成评价，11：修改收货人地址，12：修改费用）
     */
    private Integer operationType;

    /**
     * 备注信息
     */
    private String remark;

}

