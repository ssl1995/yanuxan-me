package com.msb.mall.base.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.mall.base.mapper.OrderConfigMapper;
import com.msb.mall.base.model.dto.OrderConfigDTO;
import com.msb.mall.base.model.entity.OrderConfig;
import com.msb.mall.base.model.vo.OrderConfigVO;
import com.msb.mall.base.service.convert.OrderConfigConvert;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 订单配置表(OrderConfig)表服务实现类
 *
 * @author shumengjiao
 * @since 2022-06-09 16:21:51
 */
@Service("orderConfigService")
public class OrderConfigService extends ServiceImpl<OrderConfigMapper, OrderConfig> {

    @Resource
    private OrderConfigConvert orderConfigConvert;

    /**
     * 新增或者更新订单配置
     * @param orderConfigDTO 订单配置dto
     * @return boolean
     */
    public Boolean saveOrUpdate(OrderConfigDTO orderConfigDTO) {
        OrderConfig orderConfig = orderConfigConvert.toEntity(orderConfigDTO);
        OrderConfig one = this.lambdaQuery().one();
        // 没有订单配置则新增
        if (one == null) {
            this.save(orderConfig);
            return true;
        }
        // 存在订单配置则更新
        orderConfig.setId(one.getId());
        this.updateById(orderConfig);
        return true;
    }

    /**
     * 查询订单配置
     * @return 订单配置vo
     */
    public OrderConfigVO getOrderConfig() {
        return orderConfigConvert.toVo(this.lambdaQuery().one());
    }
}

