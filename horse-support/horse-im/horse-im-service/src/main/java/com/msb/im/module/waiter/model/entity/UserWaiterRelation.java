package com.msb.im.module.waiter.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.msb.im.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户和客服实时绑定关系
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_waiter")
public class UserWaiterRelation extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String userId;
    private Long storeId;
    private String waiterId;
    private Long sessionId;
}
