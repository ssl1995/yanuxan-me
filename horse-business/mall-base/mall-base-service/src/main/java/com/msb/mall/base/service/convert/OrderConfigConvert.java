package com.msb.mall.base.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.mall.base.api.model.OrderConfigDO;
import com.msb.mall.base.model.entity.OrderConfig;
import com.msb.mall.base.model.vo.OrderConfigVO;
import com.msb.mall.base.model.dto.OrderConfigDTO;

import java.util.List;
import org.mapstruct.Mapper;

/**
 * 订单配置表(OrderConfig)表服务接口
 *
 * @author shumengjiao
 * @since 2022-06-11 10:54:05
 */
@Mapper(componentModel = "spring")
public interface OrderConfigConvert {

    OrderConfigVO toVo(OrderConfig orderConfig);

    List<OrderConfigVO> toVo(List<OrderConfig> orderConfig);

    Page<OrderConfigVO> toVo(Page<OrderConfig> orderConfig);

    OrderConfig toEntity(OrderConfigDTO orderConfigDTO);

    OrderConfigDO toDo(OrderConfig orderConfig);
}

