package com.msb.third.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.msb.framework.mysql.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * 第三方授权表(ThirdInfo)表实体类
 *
 * @author makejava
 * @date 2022-05-12 18:09:47
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("third_info")
public class ThirdInfo extends BaseEntity implements Serializable {

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 平台类型（1：微信公众号）
     */
    private Integer platformType;

    /**
     * 平台应用ID
     */
    private String appId;

    /**
     * 平台用户ID
     */
    private String appUserId;

    /**
     * 平台用户唯一标识
     */
    private String appUnionId;

    /**
     * 是否关注
     */
    private Boolean isSubscribe;

}
