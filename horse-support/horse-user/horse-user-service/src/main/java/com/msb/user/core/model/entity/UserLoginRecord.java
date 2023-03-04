package com.msb.user.core.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.msb.framework.mysql.BaseEntity;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * (UserLoginRecord)表实体类
 *
 * @author makejava
 * @since 2022-03-23 20:13:02
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_login_record")
public class UserLoginRecord extends BaseEntity implements Serializable {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 登录下放token
     */
    private String token;

    /**
     *  系统id，严选商城，学习平台
     */
    private Integer systemId;

    /**
     * 客户端id
     */
    private Integer clientId;

    /**
     * ip
     */
    private String ip;

    /**
     * 逻辑删除
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Boolean isDeleted;

}

