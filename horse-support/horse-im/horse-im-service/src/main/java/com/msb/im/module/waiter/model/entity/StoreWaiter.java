package com.msb.im.module.waiter.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.msb.im.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 店铺下的客服
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("store_waiter")
public class StoreWaiter extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long storeId;
    private String waiterId;
    private String waiterNickname;
    private String waiterAvatar;
    private String type;
    @TableLogic
    private Boolean isDeleted;
}
