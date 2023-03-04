package com.msb.im.redis;

import com.msb.framework.redis.RedisClient;
import com.msb.im.model.entity.Message;
import com.msb.im.service.MessageService;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.msb.im.redis.RedisConstant.*;

/**
 * 消息相关的redis操作服务
 *
 * @author zhoumioa
 * @date 2022-04-23
 */
@Service
public class MessageRedisService {
    public static final Long SESSION_MESSAGE_CACHE_SIZE = 20L;

    @Autowired
    private RedisClient redisClient;
    @Resource
    private MessageService messageService;

    /**
     * 查询会话中消息id
     *
     * @param sessionId
     * @return
     */
    public List<Long> getSessionMessageIds(Long sessionId) {
        String userSessionMessageIdListKey = getSessionMessageIdListKey(sessionId);
        List<Object> list = redisClient.lAll(userSessionMessageIdListKey);
        if (list != null) {
            return list.stream().map(o -> Long.parseLong(o.toString())).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }


    /**
     * 新增会话消息
     *
     * @param sessionId
     */
    public boolean existSessionMessageId(Long sessionId) {
        String userMessageListKey = getSessionMessageIdListKey(sessionId);
        return redisClient.exists(userMessageListKey);
    }

    /**
     * 新增会话消息
     *
     * @param messageId
     * @param sessionId
     */
    public void addSessionMessageId(Long messageId, Long sessionId) {
        String userMessageListKey = getSessionMessageIdListKey(sessionId);
        if (!redisClient.exists(userMessageListKey)) { // 缓存不存在 更新缓存后再添加
            messageService.loadDbSessionMessageIds2Redis(sessionId, SESSION_MESSAGE_CACHE_SIZE, messageId);
        } else {
            Long size = redisClient.lPush(userMessageListKey, messageId);
            if (size > SESSION_MESSAGE_CACHE_SIZE) { // 裁剪缓存
                redisClient.rRemove(userMessageListKey, size - SESSION_MESSAGE_CACHE_SIZE);
            }
            redisClient.expire(userMessageListKey, SESSION_MESSAGE_ID_LIST_EXPIRED_DAYS, TimeUnit.DAYS);
        }
    }

    /**
     * 添加会话消息id的锁
     *
     * @param sessionId
     * @return
     */
    public RLock getAddSessionMessageIdsLock(Long sessionId) {
        String sessionMessageIdListKeyLock = getSessionMessageIdListKeyLock(sessionId);
        return redisClient.getLock(sessionMessageIdListKeyLock);
    }

    /**
     * 更新会话中的消息id
     *
     * @param messageIds
     * @param sessionId
     */
    public void updateSessionMessageIds(List<Long> messageIds, Long sessionId) {
        String userMessageListKey = getSessionMessageIdListKey(sessionId);
        redisClient.rPushAll(userMessageListKey, messageIds.toArray());
        redisClient.expire(userMessageListKey, SESSION_MESSAGE_ID_LIST_EXPIRED_DAYS, TimeUnit.DAYS);
    }

    /**
     * 恶心
     * 新增消息
     *
     * @param message
     */
    public void addMessage(Message message) {
        String messageKey = getMessageKey(message.getId());
        redisClient.set(messageKey, message, MESSAGE_EXPIRED_DAYS, TimeUnit.DAYS);
    }

    /**
     * 查询会话最后一条消息id
     *
     * @param sessionId
     */
    public Long getSessionLastMessageId(Long sessionId) {
        String sessionMessageIdListKey = getSessionMessageIdListKey(sessionId);
        Object lastMessageId = redisClient.lIndex(sessionMessageIdListKey, 0);
        if (lastMessageId == null) {
            return null;
        }
        return Long.parseLong(lastMessageId.toString());
    }


    /**
     * 查询消息
     *
     * @param messageId
     * @return
     */
    public Message getMessage(Long messageId) {
        String messageKey = getMessageKey(messageId);
        return redisClient.get(messageKey);
    }


    /**
     * 批量查询消息
     *
     * @param messageIds
     * @return
     */
    public List<Message> getMessages(List<Long> messageIds) {
        if (messageIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<Message> redisMessages = getRedisMessageList(messageIds);

        Set<Long> redisMessageIds = redisMessages.stream().map(Message::getId).collect(Collectors.toSet());
        List<Long> selectDbMessageIds = new ArrayList<>(messageIds);
        if (redisMessages.isEmpty() || (selectDbMessageIds.removeAll(redisMessageIds) && !selectDbMessageIds.isEmpty())) {
            redisMessages.addAll(messageService.findDbByIdsAndUpdateRedis(selectDbMessageIds));
        }

        return sortMessages(messageIds, redisMessages);
    }

    private List<Message> sortMessages(List<Long> messageIds, List<Message> messages) {
        Map<Long, Message> messageMap = messages.stream().collect(Collectors.toMap(Message::getId, Function.identity()));
        List<Message> sortList = new ArrayList<>(messages.size());
        for (Long messageId : messageIds) {
            sortList.add(messageMap.get(messageId));
        }
        return sortList;
    }

    private List<Message>  getRedisMessageList(List<Long> messageIds) {
        List<Message> messages = new ArrayList<>(messageIds.size());
        for (Long messageId : messageIds) {
            Message message = getMessage(messageId);
            if (message != null) {
                messages.add(message);
            }
        }
        return messages;
    }

    public String getSessionMessageIdListKey(Long sessionId) {
        return RedisConstant.format(SESSION_MESSAGE_ID_LIST, sessionId);
    }

    public String getSessionMessageIdListKeyLock(Long sessionId) {
        return RedisConstant.format(ADD_SESSION_MESSAGE_ID_LIST_LOCK, sessionId);
    }

    public String getMessageKey(Long messageId) {
        return RedisConstant.format(MESSAGE, messageId);
    }

}
