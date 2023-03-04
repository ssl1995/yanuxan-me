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
 * 退款单操作记录(RefundOrderLog)表实体类
 *
 * @author makejava
 * @date 2022-04-08 18:24:33
 */
@Accessors(chain = true)
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("refund_order_log")
public class RefundOrderLog extends BaseEntity implements Serializable {

    /**
     * 记录ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 退款单ID
     */
    private Long refundId;

    /**
     * 操作类型（1：用户提交申请，2：用户修改申请，3：用户撤销申请，4：超时自动处理申请，5：商家同意退款，6：商家拒绝退款，7：超时自动同意退货，8：商家同意退货，9：商家拒绝退货，10：超时取消退货，11：用户填写退货物流单，12：超时自动退款，13：商家同意收货，14：商家拒绝收货，15：退款成功通知）
     */
    private Integer operationType;

    /**
     * 备注信息
     */
    private String remark;


}
