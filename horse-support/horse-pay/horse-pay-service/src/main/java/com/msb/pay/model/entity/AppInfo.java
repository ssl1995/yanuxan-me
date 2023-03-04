package com.msb.pay.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.msb.framework.mysql.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * 应用信息表(AppInfo)表实体类
 *
 * @author makejava
 * @date 2022-06-06 10:42:41
 */
@Accessors(chain = true)
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("app_info")
public class AppInfo extends BaseEntity implements Serializable {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 商户主键ID
     */
    private Long mchPrimaryId;

    /**
     * 应用代号
     */
    private String appCode;

    /**
     * 支持的支付方式
     */
    private String payCodes;

    /**
     * 签名秘钥
     */
    private String signKey;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 应用资料
     */
    private String appData;

    /**
     * 是否禁用（0：启用，1：禁用）
     */
    private Boolean isDisabled;

}
