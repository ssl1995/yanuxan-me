package com.msb.business.model.dto;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class HorseUserDTO implements Serializable {

    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private String address;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;
}

