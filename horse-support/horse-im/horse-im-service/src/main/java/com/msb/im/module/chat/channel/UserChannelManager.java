package com.msb.im.module.chat.channel;

import com.msb.im.context.ImServerContext;
import com.msb.im.netty.service.NettyClusterManager;
import com.msb.im.util.RspFrameUtil;
import com.msb.im.netty.client.IClient;
import com.msb.im.netty.server.ServerAsClientAble;
import com.msb.im.portobuf.RspMessageProto;
import com.msb.im.redis.RedisService;
import com.msb.im.util.AddressUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 通用通道管理类
 *
 * @author zhou miao
 * @date 2022/05/12
 */
@Component
@Slf4j
public class UserChannelManager {
    private static final String SYSTEM_USER_SPLIT = "-";
    /**
     * 记录通道和用户的对应关系
     * {channelId -> userId-sysId}
     */
    private static final ConcurrentMap<String, String> CHANNEL_USER_MAP = new ConcurrentHashMap<>();


    /**
     * 记录用户对应session和channel的关系
     * {userId-sysId -> {[channel1,channel2]}}
     */
    private static final ConcurrentMap<String, Set<Channel>> SYSTEM_USER_CHANNELS_MAP = new ConcurrentHashMap<>();

    @Resource
    private RedisService redisService;
    @Resource
    private NettyClusterManager nettyClusterManager;

    /**
     * 获取用户channel集合
     *
     * @param systemId 系统id
     * @param userId   用户id
     * @return 用户channel集合
     */
    public Set<Channel> getChannels(Integer systemId, String userId) {
        if (userId == null || systemId == null) {
            return null;
        } else {
            String systemAndUserKey = getSystemAndUserKey(systemId, userId);
            return SYSTEM_USER_CHANNELS_MAP.getOrDefault(systemAndUserKey, new HashSet<>(1));
        }
    }

    /**
     * 获取系统和用户的对应channel集合的组合key
     *
     * @param systemId 系统id
     * @param userId   用户id
     * @return 系统和用户的组合key
     */
    private String getSystemAndUserKey(Integer systemId, String userId) {
        return systemId + SYSTEM_USER_SPLIT + userId;
    }


    private String parseUserId(String userIdAndSystemId) {
        String[] split = userIdAndSystemId.split(SYSTEM_USER_SPLIT);
        return split[1];
    }

    private Integer parseSystemId(String userIdAndSystemId) {
        String[] split = userIdAndSystemId.split(SYSTEM_USER_SPLIT);
        return Integer.parseInt(split[0]);
    }

    /**
     * 通过channel获取用户id
     *
     * @param userChannel 用户通道
     * @return 用户id
     */
    public String getUserId(Channel userChannel) {
        String channelId = userChannel.id().asLongText();
        String userIdAndSystemId = CHANNEL_USER_MAP.get(channelId);
        if (userIdAndSystemId == null) {
            return null;
        }
        return parseUserId(userIdAndSystemId);
    }

    /**
     * 通过channel获取用户systemId
     *
     * @param userChannel 用户通道
     * @return 用户系统
     */
    public Integer getSystemId(Channel userChannel) {
        String channelId = userChannel.id().asLongText();
        String userIdAndSystemId = CHANNEL_USER_MAP.get(channelId);
        if (userIdAndSystemId == null) {
            return null;
        }
        return parseSystemId(userIdAndSystemId);
    }

    /**
     * 校验用户的心跳以及所在在服务器 然后推送或者转发消息
     *
     * @param sysId    系统id
     * @param toUserId 接收人id
     * @param model    消息
     */
    public void pushOrForwardMessage(Integer sysId, String toUserId, RspMessageProto.Model model) {
        // todo 待优化 多线程实现
        Set<String> listUserServerAddress = redisService.userServerAddressSet(toUserId, sysId);
        for (String address : listUserServerAddress) {
            // 用户已断线
            if (!redisService.existUserAddress(toUserId, address, sysId)) {
                log.info("服务器不存在 删除用户记录的服务器 {} {}", toUserId, address);
                redisService.removeUserServerAddressSet(toUserId, address, sysId);
                continue;
            }
            if (ImServerContext.isCurrentServer(address)) {
                pushMessage(sysId, toUserId, model);
            } else {
                ServerAsClientAble server = (ServerAsClientAble) ImServerContext.getServer();
                IClient asClient = server.getAsClient(address);
                // 服务器失联 删除服务器信息
                if (asClient == null) {
                    log.error("服务器 {} 在当前服务器中不存在客户端", server);
                } else {
                    asClient.sendMessage(model);
                }
            }
        }
    }


    /**
     * 推送消息给某个人
     *
     * @param systemId 系统id
     * @param toId     接收人id
     * @param model    消息
     */
    public void pushMessage(Integer systemId, String toId, RspMessageProto.Model model) {
        if (model == null || toId == null || systemId == null) {
            return;
        }
        log.info("通用通道 服务器推送消息 {} {} {}", model, toId, systemId);
        String userSystemKey = getSystemAndUserKey(systemId, toId);
        Set<Channel> channels = SYSTEM_USER_CHANNELS_MAP.getOrDefault(userSystemKey, Collections.emptySet());
        for (Channel channel : channels) {
            if (channel.isActive()) {
                channel.writeAndFlush(RspFrameUtil.createRspFrame(model));
            } else {
                removeChannel(channel);
            }
        }
    }


    /**
     * 记录连接的channel：记录通道和用户的关联信息，记录用户session通道的关系，记录用户连接的服务器地址，记录服务器当前连接数
     *
     * @param channel  用户通道
     * @param systemId 系统id
     * @param userId   用户id
     * @return 是否添加成功
     */
    public boolean addChannel(Channel channel, Integer systemId, String userId) {
        addChannelUserRelation(systemId, userId, channel);

        addUserSessionChannel(systemId, userId, channel);

        String serverAddress = AddressUtil.localAddress(channel);
        redisService.addUserServerAddress(userId, serverAddress, systemId);
        redisService.incrementServerConnectedCount(serverAddress);
        nettyClusterManager.increment(systemId);
        return true;
    }

    /**
     * 删除内存中管理的channel、删除服务器连接数、用户连接通道为空时删除用户连接地址
     *
     * @param removeChannel 移除的通道
     */
    public void removeChannel(Channel removeChannel) {
        String systemAndUserId = removeChannelUserRelation(removeChannel);
        if (systemAndUserId == null) {
            return;
        }

        removeUserChannelRelation(removeChannel, systemAndUserId);

        // 释放服务器连接数
        redisService.decrementServerConnectedCount(AddressUtil.localAddress(removeChannel));

        // 用户在线统计
        nettyClusterManager.decrement(parseSystemId(systemAndUserId));

        // 释放channel
        removeChannel.close();
    }

    private void removeUserChannelRelation(Channel removeChannel, String systemAndUserId) {
        Set<Channel> userChannels = SYSTEM_USER_CHANNELS_MAP.getOrDefault(systemAndUserId, Collections.emptySet());
        userChannels.remove(removeChannel);
        if (userChannels.isEmpty()) {
            SYSTEM_USER_CHANNELS_MAP.remove(systemAndUserId);
            redisService.removeUserServerAddress(parseUserId(systemAndUserId), AddressUtil.localAddress(removeChannel), parseSystemId(systemAndUserId));

        }
    }


    /**
     * 新增用户会话channel
     *
     * @param systemId 系统id
     * @param userId   用户id
     * @param channel  通道
     */
    private void addUserSessionChannel(Integer systemId, String userId, Channel channel) {
        String systemAndUserKey = getSystemAndUserKey(systemId, userId);
        Set<Channel> userChannels = SYSTEM_USER_CHANNELS_MAP.getOrDefault(systemAndUserKey, new HashSet<>(1));
        userChannels.add(channel);
        SYSTEM_USER_CHANNELS_MAP.put(systemAndUserKey, userChannels);
    }

    /**
     * 删除用户通道的记录
     *
     * @param userIdAndSystem 用户所有通道的key
     */
    private void removeUserSessionChannel(String userIdAndSystem) {
        SYSTEM_USER_CHANNELS_MAP.remove(userIdAndSystem);
    }

    /**
     * 添加通道和用户的关联
     *
     * @param channel  通道
     * @param userId   用户id
     * @param systemId 系统id
     */
    private void addChannelUserRelation(Integer systemId, String userId, Channel channel) {
        String userAndSystem = getSystemAndUserKey(systemId, userId);
        CHANNEL_USER_MAP.put(channel.id().asLongText(), userAndSystem);
    }

    /**
     * 删除通道和用户的关联
     *
     * @param removerChannel 被移除的通道
     */
    private String removeChannelUserRelation(Channel removerChannel) {
        return CHANNEL_USER_MAP.remove(removerChannel.id().asLongText());
    }


}