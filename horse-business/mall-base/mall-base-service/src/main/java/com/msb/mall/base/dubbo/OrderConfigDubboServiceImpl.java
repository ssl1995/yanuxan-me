package com.msb.mall.base.dubbo;

import com.msb.mall.base.api.OrderConfigDubboService;
import com.msb.mall.base.api.model.OrderConfigDO;
import com.msb.mall.base.model.entity.OrderConfig;
import com.msb.mall.base.service.OrderConfigService;
import com.msb.mall.base.service.convert.OrderConfigConvert;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author shumengjiao
 */
@DubboService
public class OrderConfigDubboServiceImpl implements OrderConfigDubboService {

    @Resource
    private OrderConfigService orderConfigService;
    @Resource
    private OrderConfigConvert orderConfigConvert;

    /**
     * 查询订单配置
     * @return 订单配置
     */
    @Override
    public OrderConfigDO getOrderConfig() {
        OrderConfig one = orderConfigService.lambdaQuery().one();
        return orderConfigConvert.toDo(one);
    }
}
