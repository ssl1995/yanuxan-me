package com.msb.mall.im.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.msb.framework.mysql.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


/**
 * 客服用户关联
 *
 * @author shumengjiao
 * @since 2022-06-09 16:21:51
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("waiter_user")
public class WaiterUser extends BaseEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 客服id uuid
     */
    private String waiterId;

    /**
     * 用户id
     */
    private Long userId;
    private String waiterAvatar;
    private String waiterNickname;
}

