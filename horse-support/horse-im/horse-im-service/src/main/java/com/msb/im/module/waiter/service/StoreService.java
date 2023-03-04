package com.msb.im.module.waiter.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import com.msb.framework.common.utils.DateUtil;
import com.msb.im.api.enums.MessageTypeEnum;
import com.msb.im.convert.MessageConvert;
import com.msb.im.convert.SessionConvert;
import com.msb.im.model.bo.CustomSendMessageResultBO;
import com.msb.im.model.bo.SendMessageBO;
import com.msb.im.model.entity.Message;
import com.msb.im.model.entity.Session;
import com.msb.im.model.entity.SessionUser;
import com.msb.im.model.vo.ListSessionVO;
import com.msb.im.model.vo.MessageVO;
import com.msb.im.model.vo.SessionVO;
import com.msb.im.module.ChannelManager;
import com.msb.im.module.chat.channel.UserChannelManager;
import com.msb.im.module.chat.model.RspMessage;
import com.msb.im.module.waiter.channel.StoreWaiterChannelManager;
import com.msb.im.module.waiter.model.bo.UserWaiterBo;
import com.msb.im.module.waiter.model.channelmessage.StoreUserSendMessage;
import com.msb.im.module.waiter.model.channelmessage.StoreWaiterAlreadyReadMessage;
import com.msb.im.module.waiter.model.channelmessage.StoreWaiterSendMessage;
import com.msb.im.module.waiter.model.entity.StoreConfig;
import com.msb.im.module.waiter.model.entity.StoreWaiter;
import com.msb.im.module.waiter.model.entity.UserWaiterRelation;
import com.msb.im.mq.model.ImMqMessage;
import com.msb.im.mq.producer.ImMqProducerService;
import com.msb.im.mq.service.UpdateUnReadImMqMessageHandleService;
import com.msb.im.netty.ChannelMessageTypeEnum;
import com.msb.im.netty.model.HandshakeParam;
import com.msb.im.netty.service.UserConnectService;
import com.msb.im.portobuf.RspMessageProto;
import com.msb.im.redis.RedisService;
import com.msb.im.redis.SessionRedisService;
import com.msb.im.service.MessageService;
import com.msb.im.service.SessionService;
import com.msb.im.service.SessionUserService;
import com.msb.im.util.RspFrameUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.msb.im.netty.ChannelMessageTypeEnum.PUSH_MESSAGE;

/**
 * 店铺服务
 *
 * @author zhou miao
 * @date 2022/05/10
 */
@Service
@Slf4j
public class StoreService {
    // 查询客服指定时间内会话 时间配置
    private static final int SESSION_WITHIN_TIME_HOURS = 3;
    // 查询客服指定时间内会话 会话长度
    private static final int SESSION_WITHIN_TIME_SIZE = 20;
    // 分配客服提示间隔时间
    private static final int USER_TIPS_INTERVAL = 2;
    @Resource
    private StoreConfigService storeConfigService;
    @Resource
    private RedisService redisService;
    @Resource
    private SessionService sessionService;
    @Resource
    private SessionUserService sessionUserService;
    @Resource
    private MessageService messageService;
    @Resource
    private StoreWaiterService storeWaiterService;
    @Resource
    private ImMqProducerService imMqProducerService;
    @Resource
    private UserWaiterRelationService userWaiterRelationService;
    @Resource
    private SessionConvert sessionConvert;
    @Resource
    private MessageConvert messageConvert;
    @Resource
    private UserWaiterHistoryLogService userWaiterHistoryLogService;
    @Resource
    private SessionRedisService sessionRedisService;
    @Resource
    private UpdateUnReadImMqMessageHandleService updateUnReadHandleService;
    @Resource
    private UserChannelManager userChannelManager;
    @Resource
    private StoreWaiterChannelManager storeWaiterChannelManager;
    @Resource
    private StoreUserQueueService storeUserQueueService;
    @Resource
    private UserConnectService userConnectService;
    @Resource
    private ChannelManager channelManager;

    /**
     * 创建客服会话
     *
     * @param userId  用户id
     * @param sysId   系统id
     * @param storeId 店铺id
     * @param handshakeParam 连接参数
     * @return 客服会话
     */
    public Session createStoreSession(String userId, Integer sysId, Long storeId, HandshakeParam handshakeParam) {
        log.info("创建客服会话 {} {} {} {}", sysId, userId, storeId, handshakeParam);
        Long sessionId = sessionService.findStoreUserSessionId(sysId, storeId.toString(), userId);
        Session session;
        if (sessionId != null) {
            sessionService.updateIsDelete(userId, sessionId, false);
            session = sessionService.findSession(sessionId);
        } else {
            Long now = System.currentTimeMillis();
            session = sessionService.createStoreSession(userId, storeId.toString(), sysId, now, handshakeParam);

            // 缓存
            sessionRedisService.addSession(session);
            HashSet<String> sessionUserIds = Sets.newHashSet(userId, storeId.toString());
            // 会话顺序
            sessionRedisService.updateUserSessionIdSort(session.getId(), now, sessionUserIds, session.getSysId());
            // 会话中包含的用户
            sessionRedisService.addSessionUserIds(session.getId(), sessionUserIds);
        }
        log.info("创建客服会话结束 {}", session);
        return session;
    }

    /**
     * 分配一个客服
     *
     * @param storeUserSendMessage
     * @param oldUserWaiterRelation
     * @return
     */
    public String allocateWaiter(StoreUserSendMessage storeUserSendMessage, UserWaiterRelation oldUserWaiterRelation) {
        String allocateWaiterId;
        if (oldUserWaiterRelation != null) {
            // 之前分配过客服 该客服在线优先分配该客服
            if (redisService.existStoreWaiterHeart(oldUserWaiterRelation.getWaiterId(), storeUserSendMessage.getSysId(), storeUserSendMessage.getStoreId())) {
                allocateWaiterId = oldUserWaiterRelation.getWaiterId();
                // 增加客服分配数
                redisService.incrementStoreWaiter(storeUserSendMessage.getSysId(), storeUserSendMessage.getStoreId(), allocateWaiterId);
                // 关联客服和用户
                userWaiterRelationService.addOrUpdate(allocateWaiterId, storeUserSendMessage.getStoreId(), storeUserSendMessage.getFromId(), storeUserSendMessage.getToSessionId());
                // 记录分配历史
                userWaiterHistoryLogService.add(storeUserSendMessage.getStoreId(), storeUserSendMessage.getFromId(), allocateWaiterId, storeUserSendMessage.getFromId(), storeUserSendMessage.getFromId());
                return allocateWaiterId;
            }
        }
        allocateWaiterId = redisService.allocateStoreWaiter(storeUserSendMessage.getSysId(), storeUserSendMessage.getStoreId());
        if (allocateWaiterId != null) {
            redisService.incrementStoreWaiter(storeUserSendMessage.getSysId(), storeUserSendMessage.getStoreId(), allocateWaiterId);
            userWaiterRelationService.addOrUpdate(allocateWaiterId, storeUserSendMessage.getStoreId(), storeUserSendMessage.getFromId(), storeUserSendMessage.getToSessionId());
            userWaiterHistoryLogService.add(storeUserSendMessage.getStoreId(), storeUserSendMessage.getFromId(), allocateWaiterId, storeUserSendMessage.getFromId(), storeUserSendMessage.getFromId());
        }
        return allocateWaiterId;
    }

    @Transactional(rollbackFor = Exception.class)
    public void userSendMessage(StoreUserSendMessage storeUserSendMessage) {
        log.info("用户发送客服会话消息 {}", storeUserSendMessage);

        // 当前会话中最后一条消息时间如果太短，不用给用户友好提示
        boolean isTips = isFriendlyTips(storeUserSendMessage.getToSessionId());
        log.info("根据最后一条消息的发送时间 是否需要给用户友好提示 {}", isTips);

        Long nowTimestamp = System.currentTimeMillis();
        StoreConfig storeConfig = storeConfigService.getById(storeUserSendMessage.getStoreId());
        if (storeConfig == null) {
            log.error("店铺配置不存 {}", storeConfig);
            return;
        }

        // 上一次分配的客服
        UserWaiterRelation oldUserWaiterRelation = userWaiterRelationService.findBySessionId(storeUserSendMessage.getToSessionId());
        // 老客服在线不需要分配客服 直接给客服发送消息
        boolean isNotAllocate = oldUserWaiterRelation != null
                && redisService.existStoreWaiterHeart(oldUserWaiterRelation.getWaiterId(), storeUserSendMessage.getSysId(), storeUserSendMessage.getStoreId());
        if (isNotAllocate) {
            // 用户返回发送的消息
            CustomSendMessageResultBO customSendMessageResultBO = sendUserMessage(storeUserSendMessage, nowTimestamp, storeConfig);

            MessageVO waiterMessageVO = createFromUserWaiterMessageVo(storeUserSendMessage, customSendMessageResultBO, oldUserWaiterRelation.getWaiterId());
            log.info("给客服发送消息 {} {}", oldUserWaiterRelation.getWaiterId(), waiterMessageVO);
            RspMessageProto.Model successRspJson = RspFrameUtil.createModel(null, PUSH_MESSAGE.getCode(), RspMessage.SUCCESS, null, waiterMessageVO);
            storeWaiterChannelManager.pushOrForwardMessage(storeUserSendMessage.getSysId(), storeUserSendMessage.getStoreId(), oldUserWaiterRelation.getWaiterId(), successRspJson);
            return;
        }

        // 下面是需要分配客服的逻辑
        String allocateWaiterId = allocateWaiter(storeUserSendMessage, oldUserWaiterRelation);
        log.info("分配的客服 {}", allocateWaiterId);
        if (allocateWaiterId == null) {
            // 会话中最后消息时间太短不用友好提示
            if (!isTips) {
                return;
            }

            // 用户返回发送的消息
            sendUserMessage(storeUserSendMessage, nowTimestamp, storeConfig);

            // 给用户友好提示
            Message userTipsMessage = messageService.generateMqMessage(null, MessageTypeEnum.TEXT.getCode(), createNotAllocateWaiterPayload(), storeUserSendMessage.getToSessionId(), nowTimestamp);
            SendMessageBO userSendTipsMessageBO = createStoreUserSendMessageBO(storeUserSendMessage, userTipsMessage, Sets.newHashSet(storeUserSendMessage.getFromId()));
            CustomSendMessageResultBO userTipsCustomSendMessage = messageService.createCustomSendMessage(userSendTipsMessageBO);

            MessageVO userTipsMessageVO = createFromStoreUserMessageVo(userTipsCustomSendMessage.getMessage(), userTipsCustomSendMessage.getSession(), storeConfig);
            RspMessageProto.Model userTipsRsp = RspFrameUtil.createModel(null, PUSH_MESSAGE.getCode(), RspMessage.SUCCESS, null, userTipsMessageVO);
            log.info("给用户返回友好提示消息 {} {}", storeUserSendMessage.getFromId(), userTipsMessageVO);
            userChannelManager.pushOrForwardMessage(storeUserSendMessage.getSysId(), storeUserSendMessage.getFromId(), userTipsRsp);

            // 用户进入队列
            String waiterId = oldUserWaiterRelation != null ? oldUserWaiterRelation.getWaiterId() : null;
            storeUserQueueService.pushUser(storeUserSendMessage, waiterId);
        } else {

            // 用户返回发送的消息
            CustomSendMessageResultBO customSendMessageResultBO = sendUserMessage(storeUserSendMessage, nowTimestamp, storeConfig);

            // 客服
            StoreWaiter storeWaiter = storeWaiterService.findByStoreIdAndWaiterId(storeUserSendMessage.getStoreId(), allocateWaiterId);

            MessageVO waiterMessageVO = createFromUserWaiterMessageVo(storeUserSendMessage, customSendMessageResultBO, allocateWaiterId);
            log.info("给客服发送消息 {} {}", allocateWaiterId, waiterMessageVO);
            RspMessageProto.Model rsp = RspFrameUtil.createModel(null, PUSH_MESSAGE.getCode(), RspMessage.SUCCESS, null, waiterMessageVO);
            storeWaiterChannelManager.pushOrForwardMessage(customSendMessageResultBO.getSession().getSysId(), storeUserSendMessage.getStoreId(), allocateWaiterId, rsp);

            // 消息入库
            String payload = createAllocateWaiterPayload(storeWaiter);
            Message message = messageService.generateMqMessage(allocateWaiterId, MessageTypeEnum.TEXT.getCode(), payload, storeUserSendMessage.getToSessionId(), nowTimestamp);
            SendMessageBO autoReplyMessage = createStoreUserSendMessageBO(storeUserSendMessage, message, Sets.newHashSet(storeUserSendMessage.getFromId(), storeUserSendMessage.getStoreId().toString()));
            customSendMessageResultBO = messageService.createCustomSendMessage(autoReplyMessage);

            // 推送消息给用户和客服
            MessageVO userMessageVO = createFromWaiterUserMessageVo(storeUserSendMessage.getFromId(), customSendMessageResultBO.getMessage(), customSendMessageResultBO.getSession(), storeWaiter, storeConfig);
            log.info("给用户发送客服欢迎消息 {} {}", storeUserSendMessage.getFromId(), userMessageVO);
            RspMessageProto.Model userHelloRsp = RspFrameUtil.createModel(null, PUSH_MESSAGE.getCode(), RspMessage.SUCCESS, null, userMessageVO);
            userChannelManager.pushOrForwardMessage(storeUserSendMessage.getSysId(), storeUserSendMessage.getFromId(), userHelloRsp);

            waiterMessageVO = createFromWaiterWaiterMessageVo(customSendMessageResultBO.getMessage(), customSendMessageResultBO.getSession(), storeWaiter, storeUserSendMessage);
            log.info("给客服发送客服欢迎消息 {} {}", allocateWaiterId, waiterMessageVO);
            RspMessageProto.Model waiterHellpRsp = RspFrameUtil.createModel(null, PUSH_MESSAGE.getCode(), RspMessage.SUCCESS, null, waiterMessageVO);
            storeWaiterChannelManager.pushOrForwardMessage(storeUserSendMessage.getSysId(), storeUserSendMessage.getStoreId(), allocateWaiterId, waiterHellpRsp);
        }
    }

    private CustomSendMessageResultBO sendUserMessage(StoreUserSendMessage storeUserSendMessage, Long nowTimestamp, StoreConfig storeConfig) {
        Message message = messageService.generateMqMessage(storeUserSendMessage.getFromId(), storeUserSendMessage.getType(), storeUserSendMessage.getPayload(), storeUserSendMessage.getToSessionId(), nowTimestamp);
        SendMessageBO sendMessageBO = createStoreUserSendMessageBO(storeUserSendMessage, message, Sets.newHashSet(storeUserSendMessage.getStoreId().toString()));
        CustomSendMessageResultBO customSendMessageResultBO = messageService.createCustomSendMessage(sendMessageBO);

        MessageVO userMessageVO = createFromUserUserMessageVo(storeUserSendMessage, customSendMessageResultBO, storeConfig);
        RspMessageProto.Model rsp = RspFrameUtil.createModel(storeUserSendMessage.getTraceId(), storeUserSendMessage.getTraceType(), RspMessage.SUCCESS, null, userMessageVO);
        log.info("给用户返回消息 {} {}", storeUserSendMessage.getFromId(), userMessageVO);
        userChannelManager.pushOrForwardMessage(storeUserSendMessage.getSysId(), storeUserSendMessage.getFromId(), rsp);
        return customSendMessageResultBO;
    }

    private SendMessageBO createStoreUserSendMessageBO(StoreUserSendMessage storeUserSendMessage, Message message, Set<String> addUnreadUserIds) {
        SendMessageBO sendMessageBO = new SendMessageBO();
        sendMessageBO.setMessage(message);
        sendMessageBO.setAddUnreadUserIds(addUnreadUserIds);
        sendMessageBO.setSysId(storeUserSendMessage.getSysId());
        return sendMessageBO;
    }

    /**
     * 根据会话中最后一条消息的创建时间，校验是否需要给用户回复友好提示
     *
     * @param sessionId 会话id
     * @return 需要友好提示
     */
    private boolean isFriendlyTips(Long sessionId) {
        boolean isTips = true;
        Message sessionLastMessage = messageService.findSessionLastMessage(sessionId);
        if (sessionLastMessage != null) {
            Long createTimeStamp = sessionLastMessage.getCreateTimeStamp();
            log.info("会话中最后一条消息 {}", sessionLastMessage);
            isTips = DateUtil.betweenHours(DateUtil.toLocalDateTime(createTimeStamp), LocalDateTime.now()) > USER_TIPS_INTERVAL;
        }
        return isTips;
    }

    /**
     * 创建用户发送的消息
     *
     * @param storeUserSendMessage      用户发送消息
     * @param customSendMessageResultBO 自定义发送消息返回的结果
     * @param storeConfig               店铺信息
     * @return
     */
    private MessageVO createFromUserUserMessageVo(StoreUserSendMessage storeUserSendMessage, CustomSendMessageResultBO customSendMessageResultBO, StoreConfig storeConfig) {
        // 发送人信息
        MessageVO messageVO = messageConvert.toVo(customSendMessageResultBO.getMessage());
        messageVO.setOriginTraceId(storeUserSendMessage.getTraceId());
        messageVO.setOriginTraceType(storeUserSendMessage.getTraceType());
        messageVO.setToId(storeUserSendMessage.getFromId());
        messageVO.setFromNickname(storeUserSendMessage.getHandshakeParam().getNickname());
        messageVO.setFromAvatar(storeUserSendMessage.getHandshakeParam().getAvatar());

        // 会话信息
        SessionVO sessionVO = sessionConvert.toVo(customSendMessageResultBO.getSession());
        sessionVO.setFromNickname(storeConfig.getName());
        sessionVO.setFromAvatar(storeConfig.getAvatar());
        messageVO.setSession(sessionVO);

        // 消息对应的会话信息
        messageVO.setSession(sessionVO);
        return messageVO;
    }

    /**
     * 创建用户发送的消息
     *
     * @param storeUserSendMessage      用户发送消息
     * @param customSendMessageResultBO 自定义发送消息返回的结果
     * @param allocateWaiterId          客服id
     * @return
     */
    private MessageVO createFromUserWaiterMessageVo(StoreUserSendMessage storeUserSendMessage, CustomSendMessageResultBO customSendMessageResultBO, String allocateWaiterId) {
        // 发送人信息
        MessageVO messageVO = messageConvert.toVo(customSendMessageResultBO.getMessage());
        messageVO.setOriginTraceType(PUSH_MESSAGE.getCode());
        messageVO.setOriginTraceId(null);
        messageVO.setStoreId(storeUserSendMessage.getStoreId());
        messageVO.setToId(allocateWaiterId);
        messageVO.setFromNickname(storeUserSendMessage.getHandshakeParam().getNickname());
        messageVO.setFromAvatar(storeUserSendMessage.getHandshakeParam().getAvatar());

        SessionVO sessionVO = sessionConvert.toVo(customSendMessageResultBO.getSession());
        sessionVO.setFromId(storeUserSendMessage.getHandshakeParam().getUser());
        sessionVO.setFromAvatar(storeUserSendMessage.getHandshakeParam().getAvatar());
        sessionVO.setFromNickname(storeUserSendMessage.getHandshakeParam().getNickname());

        // 消息对应的会话信息
        messageVO.setSession(sessionVO);
        return messageVO;
    }

    /**
     * 创建店铺发送的消息
     *
     * @param message     消息
     * @param session     会话
     * @param storeConfig 会话头像和名称
     * @return
     */
    private MessageVO createFromStoreUserMessageVo(Message message, Session session, StoreConfig storeConfig) {
        // 发送人信息
        MessageVO messageVO = messageConvert.toVo(message);
        messageVO.setFromNickname(storeConfig.getName());
        messageVO.setFromAvatar(storeConfig.getAvatar());
        messageVO.setOriginTraceType(PUSH_MESSAGE.getCode());
        messageVO.setOriginTraceId(null);
        messageVO.setToId(message.getFromId());

        // 会话信息
        SessionVO sessionVO = sessionConvert.toVo(session);
        sessionVO.setFromNickname(storeConfig.getName());
        sessionVO.setFromAvatar(storeConfig.getAvatar());
        messageVO.setSession(sessionVO);

        // 消息对应的会话信息
        messageVO.setSession(sessionVO);
        return messageVO;
    }

    /**
     * 创建客服发送的消息
     *
     * @param userId      用户
     * @param message     消息
     * @param session     会话
     * @param storeWaiter 消息发送客服
     * @return
     */
    private MessageVO createFromWaiterUserMessageVo(String userId, Message message, Session session, StoreWaiter storeWaiter, StoreConfig storeConfig) {
        // 发送人信息
        MessageVO messageVO = messageConvert.toVo(message);
        messageVO.setFromNickname(storeWaiter.getWaiterNickname());
        messageVO.setFromAvatar(storeWaiter.getWaiterAvatar());
        messageVO.setOriginTraceId(null);
        messageVO.setOriginTraceType(PUSH_MESSAGE.getCode());
        messageVO.setToId(userId);

        // 会话信息
        SessionVO sessionVO = sessionConvert.toVo(session);
        sessionVO.setFromNickname(storeConfig.getName());
        sessionVO.setFromAvatar(storeConfig.getAvatar());
        messageVO.setSession(sessionVO);

        // 消息对应的会话信息
        messageVO.setSession(sessionVO);
        return messageVO;
    }

    /**
     * 创建客服发送的消息
     *
     * @param message     消息
     * @param session     会话
     * @param storeWaiter 消息发送客服
     * @param storeUserSendMessage
     * @return
     */
    private MessageVO createFromWaiterWaiterMessageVo(Message message, Session session, StoreWaiter storeWaiter, StoreUserSendMessage storeUserSendMessage) {
        // 发送人信息
        MessageVO messageVO = messageConvert.toVo(message);
        messageVO.setFromNickname(storeWaiter.getWaiterNickname());
        messageVO.setFromAvatar(storeWaiter.getWaiterAvatar());
        messageVO.setOriginTraceType(PUSH_MESSAGE.getCode());
        messageVO.setOriginTraceId(null);
        messageVO.setStoreId(storeWaiter.getStoreId());
        messageVO.setToId(storeWaiter.getWaiterId());

        // 会话信息
        SessionVO sessionVO = sessionConvert.toVo(session);
        sessionVO.setFromNickname(storeUserSendMessage.getHandshakeParam().getNickname());
        sessionVO.setFromAvatar(storeUserSendMessage.getHandshakeParam().getAvatar());
        sessionVO.setFromId(storeUserSendMessage.getHandshakeParam().getUser());
        messageVO.setSession(sessionVO);

        // 消息对应的会话信息
        messageVO.setSession(sessionVO);
        return messageVO;
    }

    /**
     * 创建客服发送的消息
     *
     * @param message     消息
     * @param session     会话
     * @param storeWaiter 消息发送客服
     * @param traceType
     * @param traceId
     * @return
     */
    private MessageVO createFromWaiterWaiterMessageVo(Message message, Session session, StoreWaiter storeWaiter, SessionUser sessionUser, int traceType, String traceId) {
        // 发送人信息
        MessageVO messageVO = messageConvert.toVo(message);
        messageVO.setFromNickname(storeWaiter.getWaiterNickname());
        messageVO.setFromAvatar(storeWaiter.getWaiterAvatar());
        messageVO.setOriginTraceType(traceType);
        messageVO.setOriginTraceId(traceId);
        messageVO.setStoreId(storeWaiter.getStoreId());
        messageVO.setToId(storeWaiter.getWaiterId());

        // 会话信息
        SessionVO sessionVO = sessionConvert.toVo(session);
        sessionVO.setFromNickname(sessionUser.getUserNickname());
        sessionVO.setFromAvatar(sessionUser.getUserAvatar());
        sessionVO.setFromId(sessionUser.getUserId());
        messageVO.setSession(sessionVO);

        // 消息对应的会话信息
        messageVO.setSession(sessionVO);
        return messageVO;
    }

    /**
     * 客服发送消息
     *
     * @param storeWaiterSendMessage
     * @return
     */
    public void waiterSendMessage(StoreWaiterSendMessage storeWaiterSendMessage) {
        log.info("客服发送消息 {}", storeWaiterSendMessage);

        UserWaiterRelation userWaiterRelation = userWaiterRelationService.findBySessionId(storeWaiterSendMessage.getToSessionId());
        if (!Objects.equals(userWaiterRelation.getWaiterId(), storeWaiterSendMessage.getFromId())) {
            log.error("无会话权限 {} {}", storeWaiterSendMessage, userWaiterRelation);
            return;
        }

        // 消息入库
        long now = System.currentTimeMillis();
        Message message = messageService.generateMqMessage(userWaiterRelation.getWaiterId(), storeWaiterSendMessage.getType(), storeWaiterSendMessage.getPayload(), storeWaiterSendMessage.getToSessionId(), now);
        SendMessageBO sendMessageBO = new SendMessageBO();
        sendMessageBO.setMessage(message);
        sendMessageBO.setAddUnreadUserIds(Sets.newHashSet(userWaiterRelation.getUserId()));
        sendMessageBO.setSysId(storeWaiterSendMessage.getSysId());
        CustomSendMessageResultBO customSendMessageResultBO = messageService.createCustomSendMessage(sendMessageBO);


        // 推送消息
        Session session = customSendMessageResultBO.getSession();
        StoreWaiter storeWaiter = storeWaiterService.findByStoreIdAndWaiterId(storeWaiterSendMessage.getStoreId(), storeWaiterSendMessage.getFromId());
        StoreConfig storeConfig = storeConfigService.getById(storeWaiterSendMessage.getStoreId());
        MessageVO userMessageVO = createFromWaiterUserMessageVo(userWaiterRelation.getUserId(), customSendMessageResultBO.getMessage(), customSendMessageResultBO.getSession(), storeWaiter, storeConfig);
        RspMessageProto.Model rsp = RspFrameUtil.createModel(null, ChannelMessageTypeEnum.PUSH_MESSAGE.getCode(), RspMessage.SUCCESS, null, userMessageVO);
        log.info("给用户发送消息 {} {}", userWaiterRelation.getUserId(), userMessageVO);
        userChannelManager.pushOrForwardMessage(session.getSysId(), userWaiterRelation.getUserId(), rsp);

        SessionUser sessionUser = sessionUserService.findBySessionIdAndUserId(userWaiterRelation.getSessionId(), userWaiterRelation.getUserId());
        MessageVO waiterMessageVO = createFromWaiterWaiterMessageVo(customSendMessageResultBO.getMessage(), customSendMessageResultBO.getSession(), storeWaiter, sessionUser, storeWaiterSendMessage.getTraceType(), storeWaiterSendMessage.getTraceId());
        log.info("给客服返回消息 {} {}", userWaiterRelation.getWaiterId(), waiterMessageVO);
        rsp = RspFrameUtil.createModel(storeWaiterSendMessage.getTraceId(), storeWaiterSendMessage.getTraceType(), RspMessage.SUCCESS, null, waiterMessageVO);
        storeWaiterChannelManager.pushOrForwardMessage(session.getSysId(), storeWaiterSendMessage.getStoreId(), userWaiterRelation.getWaiterId(), rsp);
    }

    private String createAllocateWaiterPayload(StoreWaiter storeWaiter) {
        Map<String, String> payload = new HashMap<>();
        payload.put("text", "我是客服" + storeWaiter.getWaiterNickname() + "，很高兴为你服务，请问有什么可以帮您的呢？");
        return JSON.toJSONString(payload);
    }

    private String createNotAllocateWaiterPayload() {
        Map<String, String> payload = new HashMap<>();
        payload.put("text", "你好，客服小姐姐正忙~，有事请留言，看到会第一时间回复");
        return JSON.toJSONString(payload);
    }


    /**
     * 续期商铺客服的心跳
     *
     * @param sysId
     * @param storeId
     * @param waiterId
     * @param localAddress
     */
    public void renewalWaiterStoreSessionHeart(Integer sysId, Long storeId, String waiterId, String localAddress) {
        redisService.renewalStoreWaiterHeart(sysId, storeId, waiterId, localAddress);
        if (!redisService.existStoreWaiter(sysId, storeId, waiterId)) {
            // 客服进入redis的分配池
            redisService.addStoreWaiter(sysId, storeId, waiterId);
        }
    }

    /**
     * 续期商铺用户的心跳
     *
     * @param sysId
     * @param storeId
     * @param userId
     */
    public void renewalUserStoreSessionHeart(Integer sysId, Long storeId, String userId) {
        redisService.renewalUserStoreSessionHeart(sysId, storeId, userId);
    }

    /**
     * 查询客服的会话 （查询未读的会话+最近时间之内的会话(只查20条)）
     *
     * @param systemId
     * @param storeId
     * @param waiterId
     * @return
     */
    public ListSessionVO findWaiterSessions(Integer systemId, Long storeId, String waiterId) {
        log.info("查询客服会话列表 {} {} {}", systemId, storeId, waiterId);
        // 客服会话对应用户
        List<UserWaiterRelation> dbUserWaiterRelations = userWaiterRelationService.findByWaiterAndStore(waiterId, storeId);
        Map<Long, String> dbWaiterSessionUserMap = dbUserWaiterRelations.stream().collect(Collectors.toMap(UserWaiterRelation::getSessionId, UserWaiterRelation::getUserId));
        log.info("属于客服的会话 {}", dbUserWaiterRelations);

        // 从队列中取出所有的用户
        List<UserWaiterRelation> redisUserWaiterRelations = storeUserQueueService.allocateUser(systemId, storeId, waiterId);
        log.info("从排队队列中取出的会话 {}", redisUserWaiterRelations);
        Set<Long> allocateSessionIds = new HashSet<>();
        for (UserWaiterRelation userWaiterRelation : redisUserWaiterRelations) {
            userWaiterRelationService.addOrUpdate(waiterId, storeId, userWaiterRelation.getUserId(), userWaiterRelation.getSessionId());
            allocateSessionIds.add(userWaiterRelation.getSessionId());
        }

        // 客服本来的会话 + 新分配的会话
        allocateSessionIds.addAll(dbWaiterSessionUserMap.keySet());
        log.info("分配给当前客服的会话 {}", allocateSessionIds);

        // 有未读的会话
        List<Session> unReadSessions = sessionService.findUnreadSession(allocateSessionIds, storeId.toString());
        Set<Long> unreadSessionId = unReadSessions.stream().map(Session::getId).collect(Collectors.toSet());

        // 近期内的会话
        List<Session> sessionsWithinTimes = sessionService.findWithinTimes(dbWaiterSessionUserMap.keySet(), SESSION_WITHIN_TIME_HOURS, SESSION_WITHIN_TIME_SIZE, storeId.toString());
        List<Session> waiterSessions = new LinkedList<>(unReadSessions);

        // 有未读的会话+近期内的会话 去除重复的
        waiterSessions.addAll(sessionsWithinTimes.stream().filter(session -> !unreadSessionId.contains(session.getId())).collect(Collectors.toList()));

        // 查询所有用户信息
        List<SessionUser> sessionUsers = sessionUserService.findSessionUser(dbWaiterSessionUserMap);
        Map<String, SessionUser> sessionUserMap = sessionUsers.stream().collect(Collectors.toMap(SessionUser::getUserId, Function.identity()));

        Long totalUnreadCount = 0L;
        List<SessionVO> sessionVOS = new ArrayList<>(waiterSessions.size());
        // todo 有时间优化 这里会循环查redis
        for (Session session : waiterSessions) {
            SessionVO sessionVO = sessionConvert.toVo(session);

            // 最后一条消息
            setLastMessage(session, sessionVO);

            // 用户信息
            setOppositeUser(dbWaiterSessionUserMap, sessionUserMap, session, sessionVO);

            // 会话未读数
            totalUnreadCount = setUnread(storeId, totalUnreadCount, session, sessionVO);

            sessionVOS.add(sessionVO);
        }

        ListSessionVO listSessionVO = new ListSessionVO();
        listSessionVO.setSessionVOS(sessionVOS);
        listSessionVO.setTotalUnreadCount(totalUnreadCount);

        redisService.setStoreWaiterSessionNum(systemId, storeId, waiterId, (long) sessionVOS.size());
        return listSessionVO;
    }

    /**
     * 设置会话对面人
     *
     * @param sessionUserMap
     * @param sessionUserMa
     * @param session
     * @param sessionVO
     */
    private void setOppositeUser(Map<Long, String> sessionUserMap, Map<String, SessionUser> sessionUserMa, Session session, SessionVO sessionVO) {
        String userId = sessionUserMap.get(session.getId());
        SessionUser sessionUser = sessionUserMa.get(userId);
        if (sessionUser != null) {
            sessionVO.setFromAvatar(sessionUser.getUserAvatar());
            sessionVO.setFromNickname(sessionUser.getUserNickname());
            sessionVO.setFromId(userId);
        }
    }


    /**
     * 客服标记消息已读
     *
     * @param storeWaiterSendMessage
     */
    public void waiterReadMessage(StoreWaiterAlreadyReadMessage storeWaiterSendMessage) {
        log.info("客服标记消息已读 {}", storeWaiterSendMessage);
        Long sessionId = storeWaiterSendMessage.getSessionId();
        UserWaiterRelation userWaiterRelation = userWaiterRelationService.findBySessionId(sessionId);
        if (userWaiterRelation == null) {
            log.warn("客服的会话不存在 {}", sessionId);
            return;
        }
        String waiterId = userWaiterRelation.getWaiterId();

        if (!Objects.equals(waiterId, storeWaiterSendMessage.getWaiterId())) {
            return;
        }

        // 组装mq消息
        ImMqMessage imMqMessage = updateUnReadHandleService.createUpdateUnReadImMqMessage(sessionId, userWaiterRelation.getStoreId().toString());

        // 发送mq
        imMqProducerService.sendMq(imMqMessage, () -> updateUnReadHandleService.handle2Db(imMqMessage));

        // 存入缓存
        updateUnReadHandleService.handle2Cache(imMqMessage);
    }

    /**
     * 查询客服指定时间段内会话列表
     *
     * @param storeId
     * @param waiterId
     * @param start
     * @param end
     * @return
     */
    public ListSessionVO findWaiterHistorySession(Long storeId, String waiterId, Long start, Long end) {
        // 客服会话对应用户
        LocalDateTime startTime = DateUtil.toLocalDateTime(start);
        LocalDateTime endTime = DateUtil.toLocalDateTime(end);
        Map<Long, String> sessionUserMap = userWaiterHistoryLogService.findWaiterHistorySession(storeId, waiterId, startTime, endTime).stream().collect(Collectors.toMap(UserWaiterBo::getSessionId, UserWaiterBo::getUserId));

        // 指定时间内会话
        List<Session> sessionsWithinTimes = sessionService.findBySessionIdsAndUserId(storeId.toString(), sessionUserMap.keySet());

        // 客户信息
        List<SessionUser> sessionUsers = sessionUserService.findSessionUser(sessionUserMap);
        Map<String, SessionUser> userMap = sessionUsers.stream().collect(Collectors.toMap(SessionUser::getUserId, Function.identity()));

        Long totalUnreadCount = 0L;
        List<SessionVO> sessionVOS = new ArrayList<>(sessionsWithinTimes.size());
        // todo 有时间优化 这里会循环查redis
        for (Session session : sessionsWithinTimes) {
            SessionVO sessionVO = sessionConvert.toVo(session);

            // 最后一条消息
            setLastMessage(session, sessionVO);

            // 会话对面人信息信息
            setOppositeUser(sessionUserMap, userMap, session, sessionVO);

            // 会话未读数
            totalUnreadCount = setUnread(storeId, totalUnreadCount, session, sessionVO);

            sessionVOS.add(sessionVO);
        }

        ListSessionVO listSessionVO = new ListSessionVO();
        listSessionVO.setSessionVOS(sessionVOS);
        listSessionVO.setTotalUnreadCount(totalUnreadCount);
        return listSessionVO;
    }

    private Long setUnread(Long storeId, Long totalUnreadCount, Session session, SessionVO sessionVO) {
        Long unreadCount = sessionUserService.findUnreadCount(storeId.toString(), session.getId());
        sessionVO.setUnreadCount(unreadCount);
        totalUnreadCount += unreadCount;
        return totalUnreadCount;
    }

    private void setLastMessage(Session session, SessionVO sessionVO) {
        Message messageLast = messageService.findSessionLastMessage(session.getId());
        MessageVO messageVO = messageConvert.toVo(messageLast);
        sessionVO.setLastMessage(messageVO);
    }

    /**
     * 查询会话消息
     *
     * @param sysId        系统id
     * @param sessionId    会话id
     * @param waiterId     客服id
     * @param topMessageId 分页时的顶层消息id
     * @param size         消息长度
     * @return 会话消息
     */
    public List<MessageVO> findWaiterSessionMessages(Integer sysId, Long sessionId, String waiterId, Long topMessageId, Long size) {

        List<Message> messages = messageService.findSessionMessages(sessionId, waiterId, topMessageId, size);
        if (messages.isEmpty()) {
            return Collections.emptyList();
        }

        UserWaiterRelation userWaiterRelation = userWaiterRelationService.findBySessionId(sessionId);
        String userId = userWaiterRelation.getUserId();

        // 店铺信息 客服信息
        StoreConfig storeConfig = storeConfigService.findBySysId(sysId);

        // 客服信息
        List<StoreWaiter> storeWaiters = storeWaiterService.findBySysId(sysId);
        Map<String, StoreWaiter> waiterMap = storeWaiters.stream().collect(Collectors.toMap(StoreWaiter::getWaiterId, Function.identity()));
        Set<String> waiterIds = waiterMap.keySet();

        // 用户信息
        Map<Long, String> sessionIdAndUserId = getSessionUserMap(messages, waiterIds);
        List<SessionUser> sessionUsers = sessionUserService.findSessionUser(sessionIdAndUserId);
        Map<String, SessionUser> sessionUserMap = sessionUsers.stream().collect(Collectors.toMap(SessionUser::getUserId, Function.identity()));

        return composeMessageVOS(messages, userId, storeConfig, waiterMap, sessionUserMap);
    }

    private List<MessageVO> composeMessageVOS(List<Message> messages, String userId, StoreConfig storeConfig, Map<String, StoreWaiter> waiterMap, Map<String, SessionUser> sessionUserMap) {
        List<MessageVO> messageVOS = new ArrayList<>(messages.size());
        for (Message message : messages) {
            MessageVO messageVO = messageConvert.toVo(message);
            String fromId = message.getFromId();
            String avatar = null, nickname = null;
            if (fromId == null) {
                // 店铺自己发送的消息
                avatar = storeConfig.getAvatar();
                nickname = storeConfig.getName();
            } else if (Objects.equals(userId, fromId)) {
                // 用户发送的消息
                SessionUser sessionUser = sessionUserMap.get(fromId);
                if (sessionUser != null) {
                    avatar = sessionUser.getUserAvatar();
                    nickname = sessionUser.getUserNickname();
                }
            } else {
                // 客服发送的消息
                StoreWaiter storeWaiter = waiterMap.get(fromId);
                if (storeWaiter != null) {
                    avatar = storeWaiter.getWaiterAvatar();
                    nickname = storeWaiter.getWaiterNickname();
                }
            }
            messageVO.setFromAvatar(avatar);
            messageVO.setFromNickname(nickname);
            messageVOS.add(messageVO);
        }
        return messageVOS;
    }

    /**
     * 从消息中获取发送人和会话id 排除发送人是客服
     *
     * @param messages  消息
     * @param waiterIds 客服
     * @return 发送人和会话id
     */
    private Map<Long, String> getSessionUserMap(List<Message> messages, Set<String> waiterIds) {
        Map<Long, String> sessionUserMap = new HashMap<>(messages.size());
        for (Message message : messages) {
            if (!waiterIds.contains(message.getFromId())) {
                sessionUserMap.put(message.getSessionId(), message.getFromId());
            }
        }
        return sessionUserMap;
    }
}
