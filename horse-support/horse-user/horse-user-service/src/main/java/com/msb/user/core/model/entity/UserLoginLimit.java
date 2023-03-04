package com.msb.user.core.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.msb.framework.mysql.BaseEntity;

import lombok.experimental.Accessors;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 用户登录限制(UserLoginLimit)表实体类
 *
 * @author makejava
 * @date 2022-04-28 20:49:01
 */
@Accessors(chain = true)
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_login_limit")
public class UserLoginLimit extends BaseEntity implements Serializable {

    /**
     * 用户id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 系统id
     */
    private Long systemId;

    /**
     * 系统客户端id
     */
    private Long systemClientId;

    /**
     * 登录端限制个数
     */
    private Long loginLimitNum;

    /**
     * 超出限制后，1，拒绝登录，2，挤掉最老的一个会话
     */
    private Integer beyondMode;


    /**
     * 逻辑删除
     */
    @TableField(fill = FieldFill.INSERT)
    private Boolean isDeleted;

}
