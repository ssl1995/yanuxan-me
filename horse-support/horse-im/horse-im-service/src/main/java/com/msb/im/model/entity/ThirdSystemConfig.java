package com.msb.im.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


/**
 * (HorseImThirdSys)表实体类
 *
 * @author zhoumiao
 * @since 2022-04-25 16:24:17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("third_system_config")
public class ThirdSystemConfig extends BaseEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String secret;

    private String client;

    private String name;

    @TableLogic
    private Boolean isDeleted;


}

