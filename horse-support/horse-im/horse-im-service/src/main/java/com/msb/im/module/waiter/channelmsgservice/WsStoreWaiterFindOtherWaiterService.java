package com.msb.im.module.waiter.channelmsgservice;

import com.msb.im.convert.StoreWaiterConvert;
import com.msb.im.module.chat.model.RspMessage;
import com.msb.im.module.waiter.model.channelmessage.StoreWaiterFindOtherWaiter;
import com.msb.im.module.waiter.model.entity.StoreWaiter;
import com.msb.im.api.vo.StoreWaiterVO;
import com.msb.im.module.waiter.service.StoreWaiterService;
import com.msb.im.netty.AbstractClientMessageService;
import com.msb.im.util.RspFrameUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * channel中客服功能客服查询其他客服
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Service
@Slf4j
public class WsStoreWaiterFindOtherWaiterService extends AbstractClientMessageService<StoreWaiterFindOtherWaiter> {
    @Resource
    private StoreWaiterService storeWaiterService;
    @Resource
    private StoreWaiterConvert storeWaiterConvert;

    @Override
    protected boolean handleError(ChannelHandlerContext ctx, String traceId, Integer traceType, StoreWaiterFindOtherWaiter storeWaiterFindOtherWaiter) {
        String waiterId = getStoreWaiterId(ctx);
        List<StoreWaiter> storeWaiters = storeWaiterService.findBySysId(getStoreSysId(ctx));

        List<StoreWaiter> waiterList = storeWaiters.stream().filter(storeWaiter -> !Objects.equals(storeWaiter.getWaiterId(), waiterId)).collect(Collectors.toList());
        List<StoreWaiterVO> otherWaiters = convert(waiterList);
        ctx.writeAndFlush(RspFrameUtil.createRspFrame(traceId, traceType, RspMessage.SUCCESS, null, otherWaiters));
        return false;
    }

    private List<StoreWaiterVO> convert(List<StoreWaiter> waiterList) {
        List<StoreWaiterVO> waiterVOS = new ArrayList<>(waiterList.size());
        for (StoreWaiter storeWaiter : waiterList) {
            waiterVOS.add(storeWaiterConvert.toVo(storeWaiter));
        }
        return waiterVOS;
    }

    /**
     * 参数校验
     *
     * @param storeWaiterFindOtherWaiter
     */
    @Override
    public boolean paramError(StoreWaiterFindOtherWaiter storeWaiterFindOtherWaiter) {
        return false;
    }


}
