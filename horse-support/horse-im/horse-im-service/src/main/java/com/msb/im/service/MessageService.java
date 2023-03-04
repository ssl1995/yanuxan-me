package com.msb.im.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.common.utils.DateUtil;
import com.msb.framework.common.utils.ListUtil;
import com.msb.framework.redis.RedisClient;
import com.msb.im.convert.MessageConvert;
import com.msb.im.convert.SessionConvert;
import com.msb.im.mapper.MessageMapper;
import com.msb.im.model.bo.CustomSendMessageResultBO;
import com.msb.im.model.bo.SendMessageBO;
import com.msb.im.model.entity.Message;
import com.msb.im.model.entity.Session;
import com.msb.im.model.entity.SessionUser;
import com.msb.im.model.vo.CountDaysMessageVO;
import com.msb.im.model.vo.CountHoursMessageVO;
import com.msb.im.model.vo.MessageVO;
import com.msb.im.model.vo.SessionVO;
import com.msb.im.module.chat.channel.UserChannelManager;
import com.msb.im.module.chat.model.RspMessage;
import com.msb.im.mq.model.ImMqMessage;
import com.msb.im.mq.model.ImMqMessageTypeEnum;
import com.msb.im.mq.producer.ImMqProducerService;
import com.msb.im.mq.service.SendAndInsertSessionImMqMessageHandleService;
import com.msb.im.mq.service.SendAndUpdateSessionImMqMessageHandleService;
import com.msb.im.netty.ChannelMessageTypeEnum;
import com.msb.im.portobuf.RspMessageProto;
import com.msb.im.redis.MessageRedisService;
import com.msb.im.util.RspFrameUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.msb.im.redis.MessageRedisService.SESSION_MESSAGE_CACHE_SIZE;

/**
 * (HorseImMessage)表服务实现类
 *
 * @author zhoumiao
 * @since 2022-04-13 16:28:57
 */
@Service("messageService")
@Slf4j
public class MessageService extends ServiceImpl<MessageMapper, Message> {

    @Resource
    private IIdService iIdService;
    @Resource
    private MessageRedisService messageRedisService;
    @Resource
    private SessionService sessionService;
    @Resource
    private SessionUserService sessionUserService;
    @Resource
    private UserChannelManager userChannelManager;
    @Resource
    private SessionConvert sessionConvert;
    @Resource
    private MessageConvert messageConvert;
    @Resource
    private ImMqProducerService imMqProducerService;
    @Resource
    private SendAndUpdateSessionImMqMessageHandleService sendAndUpdateSessionImMqMessageHandleService;
    @Resource
    private RedisClient redisClient;
    @Resource
    private SendAndInsertSessionImMqMessageHandleService sendAndInsertSessionImMqMessageHandleService;

    /**
     * 从数据库查询会话消息id 更新缓存
     *
     * @param sessionId
     * @param size
     * @return
     */
    public List<Long> findDbSessionMessageIdsAndUpdateRedis(Long sessionId, Long size) {
        RLock lock = messageRedisService.getAddSessionMessageIdsLock(sessionId);
        try {
            lock.lock(1, TimeUnit.MINUTES);
            List<Long> sessionMessageIds = messageRedisService.getSessionMessageIds(sessionId);
            if (!sessionMessageIds.isEmpty()) {// 双层校验防止并发情况下多次查询数据库并写缓存浪费性能
                return sessionMessageIds;
            }
            List<Long> messageIds = findDbSessionMessageIds(sessionId, size);
            if (!messageIds.isEmpty()) {
                int sub = Math.min(messageIds.size(), SESSION_MESSAGE_CACHE_SIZE.intValue());
                messageRedisService.updateSessionMessageIds(messageIds.subList(0, sub), sessionId);
            }
            return messageIds;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 从数据库查询会话消息id
     *
     * @param sessionId
     * @param size
     * @return
     */
    public List<Long> findDbSessionMessageIds(Long sessionId, Long size) {
        return baseMapper.findDbSessionMessageIds(sessionId, size);
    }


    /**
     * 从数据库查询会话中的消息id 同时和数据库数据进行比较是否需要更新会话中的消息id
     *
     * @param sessionId
     * @param size
     * @return
     */
    public void loadDbSessionMessageIds2Redis(Long sessionId, Long size, Long addMessageId) {
        RLock lock = messageRedisService.getAddSessionMessageIdsLock(sessionId);
        try {
            lock.lock(1, TimeUnit.MINUTES);
            List<Long> messageIds = baseMapper.findDbSessionMessageIds(sessionId, size);

            if (!messageIds.contains(addMessageId)) { // 数据库还没有当前数据 数据还在mq中
                LinkedList<Long> newMessageIds = new LinkedList<>(messageIds);
                newMessageIds.addFirst(addMessageId);
                messageIds = newMessageIds;
            }
            int sub = Math.min(messageIds.size(), SESSION_MESSAGE_CACHE_SIZE.intValue());
            messageRedisService.updateSessionMessageIds(messageIds.subList(0, sub), sessionId);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 从数据库查询当前的最大id 即数据库记录数 更新缓存
     *
     * @return
     */
    public Long findDbMaxId() {
        return baseMapper.findMaxId();
    }

    /**
     * 从数据库查询指定会话中最大的消息id
     *
     * @param sessionId
     * @return
     */
    public Long findDbMessageIndexMaxIdAndUpdateRedis(Long sessionId) {
        Long dbSessionMessageMaxId = baseMapper.findSessionMessageMaxId(sessionId);
        if (dbSessionMessageMaxId == null) {
            dbSessionMessageMaxId = 0L;
        }
        iIdService.updateSessionMessageMaxId(sessionId, dbSessionMessageMaxId);
        return dbSessionMessageMaxId;
    }

    /**
     * 从数据库查询消息 更新缓存
     *
     * @param messageId
     * @return
     */
    public Message findDbByIdAndUpdateRedis(Long messageId) {
        Message message = getById(messageId);
        if (message != null) {
            messageRedisService.addMessage(message);
        }
        return message;
    }

    /**
     * 从数据库查询消息 更新缓存
     *
     * @param messageIds
     * @return
     */
    public List<Message> findDbByIdsAndUpdateRedis(List<Long> messageIds) {
        if (messageIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<Message> messages = listByIds(messageIds);
        if (!messages.isEmpty()) {
            for (Message message : messages) {
                messageRedisService.addMessage(message);
            }
        }
        return messages;
    }

    /**
     * 新增消息到数据库
     *
     * @param message
     */
    public Message addDbMessage(Message message) {
        save(message);
        return message;
    }

    /**
     * 查询会话最后一条消息 优先缓存
     *
     * @param sessionId
     * @return
     */
    public Message findSessionLastMessage(Long sessionId) {
        Long lastMessageId = messageRedisService.getSessionLastMessageId(sessionId);
        if (lastMessageId == null) {
            List<Long> dbSessionMessageIds = findDbSessionMessageIdsAndUpdateRedis(sessionId, SESSION_MESSAGE_CACHE_SIZE);
            if (dbSessionMessageIds.isEmpty()) {
                return null;
            }
            lastMessageId = dbSessionMessageIds.get(0);
        }
        Message message = messageRedisService.getMessage(lastMessageId);
        if (message != null) {
            return message;
        }
        return findDbByIdAndUpdateRedis(lastMessageId);
    }

    /**
     * 发送会话存在的单人消息
     *
     * @param sendMessageBO 消息bo
     * @return mq中的消息
     */
    public ImMqMessage sendSessionExistSingleMessage(SendMessageBO sendMessageBO) {
        log.info("发送会话存在的消息 {}", sendMessageBO);

        Set<String> sessionUserIds = sessionUserService.findSessionUserIds(sendMessageBO.getToSessionId());
        SessionUser toSessionUser = getToSessionUser(sendMessageBO.getToSessionId(), sendMessageBO.getFromId(), sessionUserIds);
        if (toSessionUser == null) {
            log.error("会话和用户关系不正确 {} {}", sendMessageBO, sessionUserIds);
            return null;
        }
        sendMessageBO.setToId(toSessionUser.getUserId());
        sendMessageBO.setToNickname(toSessionUser.getUserNickname());
        sendMessageBO.setToAvatar(toSessionUser.getUserAvatar());

        ImMqMessage imMqMessage = createSendAndUpdateImMqMessage(sendMessageBO, sendMessageBO.getToSessionId(), sessionUserIds, System.currentTimeMillis());
        createAndPushMessage(sendMessageBO, imMqMessage);
        sendAndUpdateSessionImMqMessageHandleService.handle2Cache(imMqMessage);
        return imMqMessage;
    }

    /**
     * 创建会话同时发送消息，会话存在就直接发送消息
     *
     * @param sendMessageBO 消息体
     * @return 发送成功
     */
    public Boolean sendSingleMessage(SendMessageBO sendMessageBO) {
        log.info("发送单人消息 {}", sendMessageBO);
        if (sendMessageBO.getToSessionId() != null) {
            sendSessionExistSingleMessage(sendMessageBO);
        } else {
            // 防止多条消息并发发送会产生多条相同会话 需要加锁
            String sendMessageLockKey = sendAndInsertSessionImMqMessageHandleService.getSendMessageLockKey(sendMessageBO.getSysId(), sendMessageBO.getFromId(), sendMessageBO.getToId());
            RLock lock = redisClient.getLock(sendMessageLockKey);
            try {
                lock.lock(1, TimeUnit.MINUTES);
                Long sessionId = sessionService.findDbSessionId(sendMessageBO.getSysId(), sendMessageBO.getFromId(), sendMessageBO.getToId(), sendMessageBO.getSessionTypeEnum(), sendMessageBO.getSessionPayload());
                if (sessionId == null) {
                    // 会话不存在的流程
                    ImMqMessage imMqMessage = createSendAndInsertImMqMessage(sendMessageBO, System.currentTimeMillis());
                    createAndPushMessage(sendMessageBO, imMqMessage);
                    sendAndUpdateSessionImMqMessageHandleService.handle2Cache(imMqMessage);
                } else {
                    // 会话存在的流程
                    sendMessageBO.setToSessionId(sessionId);
                    sendSessionExistSingleMessage(sendMessageBO);
                }
            } finally {
                lock.unlock();
            }

        }
        return true;
    }

    private void createAndPushMessage(SendMessageBO sendMessageBO, ImMqMessage imMqMessage) {
        MessageVO fromMessageVO = createUserMessageVo(imMqMessage, sendMessageBO, true);
        MessageVO toMessageVO = createUserMessageVo(imMqMessage, sendMessageBO, false);

        pushMessageVo(sendMessageBO, fromMessageVO, true);
        pushMessageVo(sendMessageBO, toMessageVO, false);
    }

    private void pushMessageVo(SendMessageBO sendMessageBO, MessageVO messageVO, boolean isFromUser) {
        if (isFromUser) {
            RspMessageProto.Model model = RspFrameUtil.createModel(sendMessageBO.getTraceId(), sendMessageBO.getTraceType(), RspMessage.SUCCESS, null, messageVO);
            userChannelManager.pushOrForwardMessage(sendMessageBO.getSysId(), sendMessageBO.getFromId(), model);
        } else {
            RspMessageProto.Model model = RspFrameUtil.createModel(null, ChannelMessageTypeEnum.PUSH_MESSAGE.getCode(), RspMessage.SUCCESS, null, messageVO);
            userChannelManager.pushOrForwardMessage(sendMessageBO.getSysId(), sendMessageBO.getToId(), model);
        }
    }

    private ImMqMessage createSendAndUpdateImMqMessage(SendMessageBO sendMessageBO, Long sessionId, Set<String> sessionUserIds, Long nowTimestamp) {
        ImMqMessage imMqMessage;
        Session session = Session.builder()
                .updateTimeStamp(nowTimestamp)
                .updateUser(sendMessageBO.getFromId())
                .sysId(sendMessageBO.getSysId())
                .payload(sendMessageBO.getSessionPayload())
                .type(sendMessageBO.getSessionTypeEnum().getCode())
                .id(sessionId)
                .isDeleted(false)
                .build();

        List<SessionUser> sessionUsers = new LinkedList<>();
        for (String sessionUserId : sessionUserIds) {
            SessionUser sessionUser = SessionUser.builder()
                    .userId(sessionUserId)
                    .sessionId(sessionId)
                    .updateUser(sendMessageBO.getFromId())
                    .updateTimeStamp(nowTimestamp)
                    .isDeleted(false)
                    .build();

            boolean isToUser = !Objects.equals(sendMessageBO.getFromId(), sessionUserId);
            if (isToUser) {
                sessionUser.setUserAvatar(sendMessageBO.getToAvatar());
                sessionUser.setUserNickname(sendMessageBO.getToNickname());
            } else {
                sessionUser.setUserAvatar(sendMessageBO.getFromAvatar());
                sessionUser.setUserNickname(sendMessageBO.getFromNickname());
            }

            sessionUsers.add(sessionUser);
        }

        Message message = generateMqMessage(sendMessageBO, sessionId, nowTimestamp);
        imMqMessage = createSendImMqMessage(message, session, sessionUsers, ImMqMessageTypeEnum.SEND_AND_UPDATE_SESSION_MESSAGE);

        // 发送mq或者直接入库 这个操作保证成功后 再写缓存
        imMqProducerService.sendMq(imMqMessage, () -> sendAndUpdateSessionImMqMessageHandleService.handle2Db(imMqMessage));
        return imMqMessage;
    }

    private ImMqMessage createSendAndInsertImMqMessage(SendMessageBO sendMessageBO, Long nowTimestamp) {
        ImMqMessage imMqMessage;
        Session session = sessionService.generateMqSession(sendMessageBO, nowTimestamp);

        List<SessionUser> sessionUsers = new LinkedList<>();
        SessionUser fromSessionUser = sessionUserService.generateFromMqSessionUser(sendMessageBO, session.getId(), nowTimestamp);
        sessionUsers.add(fromSessionUser);
        SessionUser toSessionUser = sessionUserService.generateToMqSessionUser(sendMessageBO, session.getId(), nowTimestamp);
        sessionUsers.add(toSessionUser);

        Message message = generateMqMessage(sendMessageBO, session.getId(), nowTimestamp);

        imMqMessage = createSendImMqMessage(message, session, sessionUsers, ImMqMessageTypeEnum.SEND_AND_INSERT_SESSION_MESSAGE);

        // 发送mq或者直接入库 这个操作保证成功后 再写缓存
        imMqProducerService.sendMq(imMqMessage, () -> sendAndInsertSessionImMqMessageHandleService.handle2Db(imMqMessage));
        return imMqMessage;
    }

    private SessionUser getToSessionUser(Long sessionId, String fromId, Set<String> sessionUserIds) {
        for (String sessionUserId : sessionUserIds) {
            if (!Objects.equals(fromId, sessionUserId)) {
                return sessionUserService.findBySessionIdAndUserId(sessionId, sessionUserId);
            }
        }
        return null;
    }

    /**
     * 创建发送消息时候的mq消费数据
     *
     * @param message             消息
     * @param session             会话
     * @param sessionUsers        会话用户需要变更的数据
     * @param imMqMessageTypeEnum 消息类型
     * @return mq中的组合消息
     */
    public ImMqMessage createSendImMqMessage(Message message, Session session, List<SessionUser> sessionUsers, ImMqMessageTypeEnum imMqMessageTypeEnum) {
        return ImMqMessage.builder()
                .message(message)
                .session(session)
                .sessionUsers(sessionUsers)
                .imMqMessageTypeEnum(imMqMessageTypeEnum)
                .hashKey(message.getSessionId().toString())
                .uuid(UUID.randomUUID().toString())
                .build();
    }

    private MessageVO createUserMessageVo(ImMqMessage imMqMessage, SendMessageBO sendMessageBO, boolean isFromUser) {
        MessageVO messageVO = messageConvert.toVo(imMqMessage.getMessage());
        messageVO.setFromNickname(sendMessageBO.getFromNickname());
        messageVO.setFromAvatar(sendMessageBO.getFromAvatar());

        // 消息转发时使用
        messageVO.setOriginTraceId(null);
        messageVO.setOriginTraceType(ChannelMessageTypeEnum.PUSH_MESSAGE.getCode());
        messageVO.setToId(sendMessageBO.getToId());

        SessionVO sessionVO = sessionConvert.toVo(imMqMessage.getSession());
        if (isFromUser) {
            sessionVO.setFromAvatar(sendMessageBO.getToAvatar());
            sessionVO.setFromNickname(sendMessageBO.getToNickname());
            sessionVO.setFromId(sendMessageBO.getToId());
        } else {
            sessionVO.setFromAvatar(sendMessageBO.getFromAvatar());
            sessionVO.setFromNickname(sendMessageBO.getFromNickname());
            sessionVO.setFromId(sendMessageBO.getFromId());
        }
        messageVO.setSession(sessionVO);
        return messageVO;
    }

    /**
     * 自定义发送消息：如客服转移会话时 通知客服会话中的用户 通知转移对象 通知被转移的人
     *
     * @param sendMessageBO
     */
    public CustomSendMessageResultBO createCustomSendMessage(SendMessageBO sendMessageBO) {
        log.info("往指定会话中新增一条消息 同时向指定人推送 {}", sendMessageBO);
        Message message = sendMessageBO.getMessage();

        // 会话需要更新的信息
        Session session = new Session();
        session.setUpdateTimeStamp(message.getCreateTimeStamp());
        session.setSysId(sendMessageBO.getSysId());
        session.setUpdateUser(message.getFromId());
        session.setId(message.getSessionId());
        session.setIsDeleted(false);

        // 会话用户需要更新的信息
        List<SessionUser> sessionUsers = new LinkedList<>();
        Set<String> sessionUserIds = sessionUserService.findSessionUserIds(message.getSessionId());
        for (String sessionUserId : sessionUserIds) {
            SessionUser sessionUser = new SessionUser();
            sessionUser.setUpdateUser(message.getFromId());
            sessionUser.setUpdateTimeStamp(message.getCreateTimeStamp());
            sessionUser.setSessionId(message.getSessionId());
            sessionUser.setUserId(sessionUserId);

            // 防止用户删除了会话
            sessionUser.setIsDeleted(false);
            sessionUsers.add(sessionUser);
        }

        ImMqMessage imMqMessage = createSendImMqMessage(message, session, sessionUsers, ImMqMessageTypeEnum.SEND_AND_UPDATE_SESSION_MESSAGE);

        // 发送mq或者直接入库 这个操作保证成功后 再写缓存
        imMqProducerService.sendMq(imMqMessage, () -> sendAndUpdateSessionImMqMessageHandleService.handle2Db(imMqMessage));

        // 更新缓存
        sendAndUpdateSessionImMqMessageHandleService.handle2Cache(imMqMessage);

        return CustomSendMessageResultBO.builder().message(message).session(session).build();
    }

    public Message generateMqMessage(String fromId, Integer type, String payload, Long sessionId, Long nowTimestamp) {
        Message message = new Message();
        Long messageId = iIdService.generateMessageId();
        message.setId(messageId);
        message.setFromId(fromId);
        message.setType(type);
        message.setSessionId(sessionId);
        Long sessionMessageId = iIdService.generateSessionMessageId(sessionId);
        message.setMessageIndex(sessionMessageId);
        message.setPayload(payload);
        message.setIsDeleted(false);
        message.setCreateTimeStamp(nowTimestamp);
        return message;
    }

    public Message generateMqMessage(SendMessageBO sendMessageBO, Long sessionId, Long nowTimestamp) {
        Message message = new Message();
        Long messageId = iIdService.generateMessageId();
        message.setId(messageId);
        message.setFromId(sendMessageBO.getFromId());
        message.setType(sendMessageBO.getType().getCode());
        message.setSessionId(sessionId);
        Long sessionMessageId = iIdService.generateSessionMessageId(sessionId);
        message.setMessageIndex(sessionMessageId);
        message.setPayload(sendMessageBO.getPayload());
        message.setIsDeleted(false);
        message.setCreateTimeStamp(nowTimestamp);
        return message;
    }

    /**
     * 查询会话中的消息
     * 默认查询全部未读  未读小于100 查询100条
     *
     * @param sessionId
     * @param userId
     * @param topMessageId
     * @param sessionId
     * @return
     */
    public List<Message> findSessionMessages(Long sessionId, String userId, Long topMessageId, Long size) {
        if (size == null || size < SESSION_MESSAGE_CACHE_SIZE) {
            size = SESSION_MESSAGE_CACHE_SIZE;
        }
        Long unreadCount = sessionUserService.findUnreadCount(userId, sessionId);
        Long selectSize;
        if (unreadCount < size) {
            // 查询100条
            selectSize = size;
        } else {
            // 查询所有未读
            selectSize = unreadCount;
        }

        List<Long> messageIds;
        if (topMessageId == null && unreadCount < SESSION_MESSAGE_CACHE_SIZE) {
            messageIds = findSessionMessageIds(sessionId, selectSize);
        } else {
            messageIds = findDbSessionMessageIdsByLastMessageId(sessionId, SESSION_MESSAGE_CACHE_SIZE, topMessageId);
        }
        // 反转顺序 前端展示需要
        Collections.reverse(messageIds);

        return messageRedisService.getMessages(messageIds);
    }

    /**
     * 通过消息id 查询消息优先缓存
     *
     * @param messageId
     * @return
     */
    public Message findById(Long messageId) {
        Message message = messageRedisService.getMessage(messageId);
        if (message == null) {
            message = findDbByIdAndUpdateRedis(messageId);
        }
        return message;
    }

    /**
     * 分页查询同一个会话中的消息id
     * <p>
     * 直接查库 不更新缓存
     *
     * @param sessionId
     * @param selectSize
     * @param lastMessageId
     * @return
     */
    public List<Long> findDbSessionMessageIdsByLastMessageId(Long sessionId, Long selectSize, Long lastMessageId) {
        return baseMapper.findSessionMessageIdsByLastMessageId(sessionId, selectSize, lastMessageId);
    }

    /**
     * 查询会话中最后的size条消息
     *
     * @param sessionId
     * @param size
     * @return
     */
    public List<Long> findSessionMessageIds(Long sessionId, Long size) {
        List<Long> messageIds;
        if (size > SESSION_MESSAGE_CACHE_SIZE) {
            messageIds = findDbSessionMessageIds(sessionId, size);
        } else {
            messageIds = messageRedisService.getSessionMessageIds(sessionId);
            if (messageIds.isEmpty()) {
                messageIds = findDbSessionMessageIdsAndUpdateRedis(sessionId, size);
            }
        }
        return messageIds;
    }


    /**
     * 查询会话中指定messageIndex消息 更新缓存
     *
     * @param sessionId
     * @param messageIndexIds
     * @return
     */
    public List<Message> findDbBySessionIdAndMessageIndexIds(Long sessionId, List<Long> messageIndexIds) {
        List<Message> messages = baseMapper.findDbBySessionIdAndMessageIndexIds(sessionId, messageIndexIds);
        for (Message message : messages) {
            // 更新缓存
            messageRedisService.addMessage(message);
        }
        return messages;
    }

    /**
     * 按小时统计消息量
     *
     * @param systemId 系统
     * @param days     开始时间戳
     * @return 小时统计消息量
     */
    public List<CountHoursMessageVO> groupHoursByDaysMessage(Integer systemId, Date days) {
        LocalDate selectDate = DateUtil.toLocalDate(days);
        LocalDate now = LocalDate.now();
        boolean isToday = selectDate.equals(now);
        int nowHour = LocalDateTime.now().getHour();
        List<CountHoursMessageVO> countHoursMessageVOS = baseMapper.groupHoursByDaysMessage(systemId, days);
        List<CountHoursMessageVO> hourList = IntStream.rangeClosed(0, 23)
                .mapToObj(i -> {
                    Integer messageCount = isToday && i > nowHour ? null : 0;
                    return new CountHoursMessageVO()
                            .setHours(i)
                            .setMessageCount(messageCount);
                })
                .collect(Collectors.toList());
        if (countHoursMessageVOS.isEmpty()) {
            return hourList;
        }
        ListUtil.match(hourList, countHoursMessageVOS, CountHoursMessageVO::getHours, (hour, queryResult) -> hour.setMessageCount(queryResult.getMessageCount()));
        return hourList;
    }

    /**
     * 按天统计消息量
     *
     * @param systemId 系统
     * @param start    开始时间戳
     * @param end      结束时间戳
     * @return 天统计消息量
     */
    public List<CountDaysMessageVO> groupDaysMessage(Integer systemId, Long start, Long end) {
        List<CountDaysMessageVO> messages = baseMapper.groupDaysMessage(systemId, start, end);

        // 获取连续的时间集合
        LocalDate beginDate = DateUtil.toLocalDate(start);
        LocalDate endDate = DateUtil.toLocalDate(end);
        List<LocalDate> localDateList = new ArrayList<>();
        localDateList.add(beginDate);
        while (beginDate.isBefore(endDate)) {
            beginDate = beginDate.plusDays(1);
            localDateList.add(beginDate);
        }

        // 将时间集合赋值给销售额集合
        List<CountDaysMessageVO> dayList = localDateList.stream().map(item -> {
            CountDaysMessageVO countDaysMessageVO = new CountDaysMessageVO();
            countDaysMessageVO.setDays(item);
            Integer messageCount = item.compareTo(LocalDate.now()) > 0 ? null : 0;
            countDaysMessageVO.setMessageCount(messageCount);
            return countDaysMessageVO;
        }).collect(Collectors.toList());


        if (messages.isEmpty()) {
            return Collections.emptyList();
        }
        ListUtil.match(dayList, messages, CountDaysMessageVO::getDays, (day, queryResult) -> day.setMessageCount(queryResult.getMessageCount()));
        return dayList;

    }
}

