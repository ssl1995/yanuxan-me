package com.msb.user.core.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.msb.framework.mysql.BaseEntity;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * (User)表实体类
 *
 * @author makejava
 * @since 2022-03-23 20:13:02
 */
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user")
public class User extends BaseEntity implements Serializable {

    /**
     * 用户id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 账号
     */
    private String account;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 性别 1：男 2 女 3 未知
     */
    private Integer gender;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 密码
     */
    private String password;

    /**
     * 证件号码
     */
    private String idCard;

    /**
     * 系统id
     */
    private Integer systemId;

    /**
     * 客户端id
     */
    private Integer clientId;

    /**
     * unionid
     */
    private String unionId;

    /**
     * 0 可用  1不可用
     */
    private Boolean isEnable;

    /**
     * 最后一次登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Boolean isDeleted;

}

