package com.msb.im.module.waiter.channel;

import com.msb.im.context.ImServerContext;
import com.msb.im.netty.client.IClient;
import com.msb.im.netty.server.ServerAsClientAble;
import com.msb.im.netty.service.NettyClusterManager;
import com.msb.im.portobuf.RspMessageProto;
import com.msb.im.redis.RedisService;
import com.msb.im.util.AddressUtil;
import com.msb.im.util.RspFrameUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 客服通道管理
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Component
@Slf4j
public class StoreWaiterChannelManager {
    /**
     * 客服保存格式 sysId-storeId-waiterId(userId)
     */
    private static final String WAITER_SPLIT = "-";

    /**
     * 记录通道和用户的对应关系
     * {channelId -> sysId-storeId-waiterId}
     */
    private static final ConcurrentMap<String, String> CHANNEL_STORE_WAITER_MAP = new ConcurrentHashMap<>();

    /**
     * 记录用户对应session和channel的关系
     * {sysId-storeId-waiterId->[channel1,channel2]}
     */
    private static final ConcurrentMap<String, Set<Channel>> STORE_WAITER_CHANNELS_MAP = new ConcurrentHashMap<>();

    @Resource
    private RedisService redisService;
    @Resource
    private NettyClusterManager nettyClusterManager;

    private String createSysStoreWaiterKey(String userId, Integer systemId, Long storeId) {
        return systemId + WAITER_SPLIT + storeId + WAITER_SPLIT + userId;
    }

    private String parseWaiterId(String sysStoreWaiterKey) {
        String[] split = sysStoreWaiterKey.split(WAITER_SPLIT);
        return split[2];
    }

    private Integer parseSystemId(String sysStoreWaiterKey) {
        String[] split = sysStoreWaiterKey.split(WAITER_SPLIT);
        return Integer.parseInt(split[0]);
    }

    private Long parseStoreId(String sysStoreWaiterKey) {
        String[] split = sysStoreWaiterKey.split(WAITER_SPLIT);
        return Long.parseLong(split[1]);
    }

    public String getWaiterId(Channel channel) {
        String channelId = channel.id().asLongText();
        String sysStoreWaiterKey = CHANNEL_STORE_WAITER_MAP.get(channelId);
        return parseWaiterId(sysStoreWaiterKey);
    }

    public Integer getSystemId(Channel channel) {
        String channelId = channel.id().asLongText();
        String sysStoreWaiterKey = CHANNEL_STORE_WAITER_MAP.get(channelId);
        return parseSystemId(sysStoreWaiterKey);
    }

    public Long getStoreId(Channel channel) {
        String channelId = channel.id().asLongText();
        String sysStoreWaiterKey = CHANNEL_STORE_WAITER_MAP.get(channelId);
        return parseStoreId(sysStoreWaiterKey);
    }


    public void pushOrForwardMessage(Integer sysId, Long storeId, String toId, RspMessageProto.Model message) {
        // todo 待优化 多线程实现
        Set<String> waiterServerAddress = redisService.storeWaiterServerAddressSet(toId, sysId, storeId);
        for (String address : waiterServerAddress) {
            if (!redisService.existStoreWaiterHeart(toId, sysId, storeId)) { // 用户已断线
                log.info("服务器不存在 删除用户记录的服务器 {} {} {} {}", toId, address, sysId, storeId);
                continue;
            }
            if (ImServerContext.isCurrentServer(address)) {
                pushMessage(sysId, storeId, toId, message);
            } else {
                ServerAsClientAble server = (ServerAsClientAble) ImServerContext.getServer();
                IClient asClient = server.getAsClient(address);
                if (asClient == null) { // 服务器失联 删除服务器信息
                    log.error("服务器 {} 在当前服务器中不存在客户端", server);
                } else {
                    asClient.sendMessage(message);
                }
            }
        }
    }

    /**
     * 推送消息给某个人
     *
     * @param systemId   系统id
     * @param storeId    店铺id
     * @param toWaiterId 客服id
     * @param message    消息
     */
    public void pushMessage(Integer systemId, Long storeId, String toWaiterId, RspMessageProto.Model message) {
        if (message == null || storeId == null || toWaiterId == null || systemId == null) {
            return;
        }
        log.info("客服通道 服务器推送消息 {} {} {} {}", systemId, storeId, toWaiterId, message);
        String sysStoreWaiterKey = createSysStoreWaiterKey(toWaiterId, systemId, storeId);
        Set<Channel> waiterChannelSet = STORE_WAITER_CHANNELS_MAP.getOrDefault(sysStoreWaiterKey, new HashSet<>());
        log.debug("用户存在的通道 {} {}", sysStoreWaiterKey, waiterChannelSet);
        for (Channel channel : waiterChannelSet) {
            if (channel.isActive()) {
                channel.writeAndFlush(RspFrameUtil.createRspFrame(message));
            } else {
                removeChannel(channel);
            }
        }
    }

    public boolean addChannel(Channel channel, String userId, Integer systemId, Long storeId) {
        putChannelWaiter(channel, userId, systemId, storeId);
        putWaiterChannel(userId, channel, systemId, storeId);
        add2Redis(channel, userId, systemId, storeId);
        return true;
    }

    private void add2Redis(Channel channel, String userId, Integer systemId, Long storeId) {
        String serverAddress = AddressUtil.localAddress(channel);
        redisService.addStoreWaiter(userId, serverAddress, systemId, storeId);
        redisService.incrementServerConnectedCount(serverAddress);
        nettyClusterManager.increment(systemId);
    }

    public void removeChannel(Channel removeChannel) {
        String sysStoreWaiterKey = removeChannelWaiter(removeChannel);
        if (sysStoreWaiterKey == null) {
            return;
        }

        Set<Channel> waiterChannelsSet = STORE_WAITER_CHANNELS_MAP.get(sysStoreWaiterKey);
        if (waiterChannelsSet == null) {
            return;
        }
        waiterChannelsSet.remove(removeChannel);

        removeRedis(sysStoreWaiterKey, removeChannel);

        // 释放channel
        removeChannel.close();
    }

    private void removeRedis(String sysStoreWaiterKey, Channel removeChannel) {
        Integer systemId = parseSystemId(sysStoreWaiterKey);
        redisService.deleteStoreWaiterHeart(systemId, parseStoreId(sysStoreWaiterKey), parseWaiterId(sysStoreWaiterKey));
        // 释放服务器连接数
        redisService.decrementServerConnectedCount(AddressUtil.localAddress(removeChannel));
        nettyClusterManager.decrement(systemId);
    }

    private void putWaiterChannel(String userId, Channel channel, Integer systemId, Long storeId) {
        String sysStoreWaiterKey = createSysStoreWaiterKey(userId, systemId, storeId);
        Set<Channel> channelSet = STORE_WAITER_CHANNELS_MAP.getOrDefault(sysStoreWaiterKey, new HashSet<>());
        channelSet.add(channel);
        STORE_WAITER_CHANNELS_MAP.put(sysStoreWaiterKey, channelSet);
    }

    private void putChannelWaiter(Channel channel, String userId, Integer systemId, Long storeId) {
        String userAndSystem = createSysStoreWaiterKey(userId, systemId, storeId);
        CHANNEL_STORE_WAITER_MAP.put(channel.id().asLongText(), userAndSystem);
    }

    private String removeChannelWaiter(Channel channel) {
        return CHANNEL_STORE_WAITER_MAP.remove(channel.id().asLongText());
    }
}