package com.msb.business.api.model;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * (HorseUser)表实体类
 *
 * @author makejava
 * @since 2022-03-16 11:12:35
 */
@SuppressWarnings("serial")
@Data
public class HorseUserVO implements Serializable {

    private Long id;

    private String name;

    private String address;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;
}

