package com.msb.im.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.msb.framework.mysql.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


/**
 * 连接时已经使用过的ticket表实体类
 *
 * @author zhoumiao
 * @since 2022-04-25 16:24:17
 */
@Data
@TableName("invalid_ticket")
public class InvalidTicket {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Integer sysId;
    private String fromId;
    private Long lastTimestamp;
    /**
     * {@link com.msb.im.api.enums.TicketTypeEnum}
     */
    private String type;

}

