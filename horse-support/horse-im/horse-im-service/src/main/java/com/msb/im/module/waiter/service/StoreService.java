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
 * ????????????
 *
 * @author zhou miao
 * @date 2022/05/10
 */
@Service
@Slf4j
public class StoreService {
    // ????????????????????????????????? ????????????
    private static final int SESSION_WITHIN_TIME_HOURS = 3;
    // ????????????????????????????????? ????????????
    private static final int SESSION_WITHIN_TIME_SIZE = 20;
    // ??????????????????????????????
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
     * ??????????????????
     *
     * @param userId  ??????id
     * @param sysId   ??????id
     * @param storeId ??????id
     * @param handshakeParam ????????????
     * @return ????????????
     */
    public Session createStoreSession(String userId, Integer sysId, Long storeId, HandshakeParam handshakeParam) {
        log.info("?????????????????? {} {} {} {}", sysId, userId, storeId, handshakeParam);
        Long sessionId = sessionService.findStoreUserSessionId(sysId, storeId.toString(), userId);
        Session session;
        if (sessionId != null) {
            sessionService.updateIsDelete(userId, sessionId, false);
            session = sessionService.findSession(sessionId);
        } else {
            Long now = System.currentTimeMillis();
            session = sessionService.createStoreSession(userId, storeId.toString(), sysId, now, handshakeParam);

            // ??????
            sessionRedisService.addSession(session);
            HashSet<String> sessionUserIds = Sets.newHashSet(userId, storeId.toString());
            // ????????????
            sessionRedisService.updateUserSessionIdSort(session.getId(), now, sessionUserIds, session.getSysId());
            // ????????????????????????
            sessionRedisService.addSessionUserIds(session.getId(), sessionUserIds);
        }
        log.info("???????????????????????? {}", session);
        return session;
    }

    /**
     * ??????????????????
     *
     * @param storeUserSendMessage
     * @param oldUserWaiterRelation
     * @return
     */
    public String allocateWaiter(StoreUserSendMessage storeUserSendMessage, UserWaiterRelation oldUserWaiterRelation) {
        String allocateWaiterId;
        if (oldUserWaiterRelation != null) {
            // ????????????????????? ????????????????????????????????????
            if (redisService.existStoreWaiterHeart(oldUserWaiterRelation.getWaiterId(), storeUserSendMessage.getSysId(), storeUserSendMessage.getStoreId())) {
                allocateWaiterId = oldUserWaiterRelation.getWaiterId();
                // ?????????????????????
                redisService.incrementStoreWaiter(storeUserSendMessage.getSysId(), storeUserSendMessage.getStoreId(), allocateWaiterId);
                // ?????????????????????
                userWaiterRelationService.addOrUpdate(allocateWaiterId, storeUserSendMessage.getStoreId(), storeUserSendMessage.getFromId(), storeUserSendMessage.getToSessionId());
                // ??????????????????
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
        log.info("?????????????????????????????? {}", storeUserSendMessage);

        // ?????????????????????????????????????????????????????????????????????????????????
        boolean isTips = isFriendlyTips(storeUserSendMessage.getToSessionId());
        log.info("??????????????????????????????????????? ????????????????????????????????? {}", isTips);

        Long nowTimestamp = System.currentTimeMillis();
        StoreConfig storeConfig = storeConfigService.getById(storeUserSendMessage.getStoreId());
        if (storeConfig == null) {
            log.error("?????????????????? {}", storeConfig);
            return;
        }

        // ????????????????????????
        UserWaiterRelation oldUserWaiterRelation = userWaiterRelationService.findBySessionId(storeUserSendMessage.getToSessionId());
        // ???????????????????????????????????? ???????????????????????????
        boolean isNotAllocate = oldUserWaiterRelation != null
                && redisService.existStoreWaiterHeart(oldUserWaiterRelation.getWaiterId(), storeUserSendMessage.getSysId(), storeUserSendMessage.getStoreId());
        if (isNotAllocate) {
            // ???????????????????????????
            CustomSendMessageResultBO customSendMessageResultBO = sendUserMessage(storeUserSendMessage, nowTimestamp, storeConfig);

            MessageVO waiterMessageVO = createFromUserWaiterMessageVo(storeUserSendMessage, customSendMessageResultBO, oldUserWaiterRelation.getWaiterId());
            log.info("????????????????????? {} {}", oldUserWaiterRelation.getWaiterId(), waiterMessageVO);
            RspMessageProto.Model successRspJson = RspFrameUtil.createModel(null, PUSH_MESSAGE.getCode(), RspMessage.SUCCESS, null, waiterMessageVO);
            storeWaiterChannelManager.pushOrForwardMessage(storeUserSendMessage.getSysId(), storeUserSendMessage.getStoreId(), oldUserWaiterRelation.getWaiterId(), successRspJson);
            return;
        }

        // ????????????????????????????????????
        String allocateWaiterId = allocateWaiter(storeUserSendMessage, oldUserWaiterRelation);
        log.info("??????????????? {}", allocateWaiterId);
        if (allocateWaiterId == null) {
            // ???????????????????????????????????????????????????
            if (!isTips) {
                return;
            }

            // ???????????????????????????
            sendUserMessage(storeUserSendMessage, nowTimestamp, storeConfig);

            // ?????????????????????
            Message userTipsMessage = messageService.generateMqMessage(null, MessageTypeEnum.TEXT.getCode(), createNotAllocateWaiterPayload(), storeUserSendMessage.getToSessionId(), nowTimestamp);
            SendMessageBO userSendTipsMessageBO = createStoreUserSendMessageBO(storeUserSendMessage, userTipsMessage, Sets.newHashSet(storeUserSendMessage.getFromId()));
            CustomSendMessageResultBO userTipsCustomSendMessage = messageService.createCustomSendMessage(userSendTipsMessageBO);

            MessageVO userTipsMessageVO = createFromStoreUserMessageVo(userTipsCustomSendMessage.getMessage(), userTipsCustomSendMessage.getSession(), storeConfig);
            RspMessageProto.Model userTipsRsp = RspFrameUtil.createModel(null, PUSH_MESSAGE.getCode(), RspMessage.SUCCESS, null, userTipsMessageVO);
            log.info("????????????????????????????????? {} {}", storeUserSendMessage.getFromId(), userTipsMessageVO);
            userChannelManager.pushOrForwardMessage(storeUserSendMessage.getSysId(), storeUserSendMessage.getFromId(), userTipsRsp);

            // ??????????????????
            String waiterId = oldUserWaiterRelation != null ? oldUserWaiterRelation.getWaiterId() : null;
            storeUserQueueService.pushUser(storeUserSendMessage, waiterId);
        } else {

            // ???????????????????????????
            CustomSendMessageResultBO customSendMessageResultBO = sendUserMessage(storeUserSendMessage, nowTimestamp, storeConfig);

            // ??????
            StoreWaiter storeWaiter = storeWaiterService.findByStoreIdAndWaiterId(storeUserSendMessage.getStoreId(), allocateWaiterId);

            MessageVO waiterMessageVO = createFromUserWaiterMessageVo(storeUserSendMessage, customSendMessageResultBO, allocateWaiterId);
            log.info("????????????????????? {} {}", allocateWaiterId, waiterMessageVO);
            RspMessageProto.Model rsp = RspFrameUtil.createModel(null, PUSH_MESSAGE.getCode(), RspMessage.SUCCESS, null, waiterMessageVO);
            storeWaiterChannelManager.pushOrForwardMessage(customSendMessageResultBO.getSession().getSysId(), storeUserSendMessage.getStoreId(), allocateWaiterId, rsp);

            // ????????????
            String payload = createAllocateWaiterPayload(storeWaiter);
            Message message = messageService.generateMqMessage(allocateWaiterId, MessageTypeEnum.TEXT.getCode(), payload, storeUserSendMessage.getToSessionId(), nowTimestamp);
            SendMessageBO autoReplyMessage = createStoreUserSendMessageBO(storeUserSendMessage, message, Sets.newHashSet(storeUserSendMessage.getFromId(), storeUserSendMessage.getStoreId().toString()));
            customSendMessageResultBO = messageService.createCustomSendMessage(autoReplyMessage);

            // ??????????????????????????????
            MessageVO userMessageVO = createFromWaiterUserMessageVo(storeUserSendMessage.getFromId(), customSendMessageResultBO.getMessage(), customSendMessageResultBO.getSession(), storeWaiter, storeConfig);
            log.info("????????????????????????????????? {} {}", storeUserSendMessage.getFromId(), userMessageVO);
            RspMessageProto.Model userHelloRsp = RspFrameUtil.createModel(null, PUSH_MESSAGE.getCode(), RspMessage.SUCCESS, null, userMessageVO);
            userChannelManager.pushOrForwardMessage(storeUserSendMessage.getSysId(), storeUserSendMessage.getFromId(), userHelloRsp);

            waiterMessageVO = createFromWaiterWaiterMessageVo(customSendMessageResultBO.getMessage(), customSendMessageResultBO.getSession(), storeWaiter, storeUserSendMessage);
            log.info("????????????????????????????????? {} {}", allocateWaiterId, waiterMessageVO);
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
        log.info("????????????????????? {} {}", storeUserSendMessage.getFromId(), userMessageVO);
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
     * ????????????????????????????????????????????????????????????????????????????????????????????????
     *
     * @param sessionId ??????id
     * @return ??????????????????
     */
    private boolean isFriendlyTips(Long sessionId) {
        boolean isTips = true;
        Message sessionLastMessage = messageService.findSessionLastMessage(sessionId);
        if (sessionLastMessage != null) {
            Long createTimeStamp = sessionLastMessage.getCreateTimeStamp();
            log.info("??????????????????????????? {}", sessionLastMessage);
            isTips = DateUtil.betweenHours(DateUtil.toLocalDateTime(createTimeStamp), LocalDateTime.now()) > USER_TIPS_INTERVAL;
        }
        return isTips;
    }

    /**
     * ???????????????????????????
     *
     * @param storeUserSendMessage      ??????????????????
     * @param customSendMessageResultBO ????????????????????????????????????
     * @param storeConfig               ????????????
     * @return
     */
    private MessageVO createFromUserUserMessageVo(StoreUserSendMessage storeUserSendMessage, CustomSendMessageResultBO customSendMessageResultBO, StoreConfig storeConfig) {
        // ???????????????
        MessageVO messageVO = messageConvert.toVo(customSendMessageResultBO.getMessage());
        messageVO.setOriginTraceId(storeUserSendMessage.getTraceId());
        messageVO.setOriginTraceType(storeUserSendMessage.getTraceType());
        messageVO.setToId(storeUserSendMessage.getFromId());
        messageVO.setFromNickname(storeUserSendMessage.getHandshakeParam().getNickname());
        messageVO.setFromAvatar(storeUserSendMessage.getHandshakeParam().getAvatar());

        // ????????????
        SessionVO sessionVO = sessionConvert.toVo(customSendMessageResultBO.getSession());
        sessionVO.setFromNickname(storeConfig.getName());
        sessionVO.setFromAvatar(storeConfig.getAvatar());
        messageVO.setSession(sessionVO);

        // ???????????????????????????
        messageVO.setSession(sessionVO);
        return messageVO;
    }

    /**
     * ???????????????????????????
     *
     * @param storeUserSendMessage      ??????????????????
     * @param customSendMessageResultBO ????????????????????????????????????
     * @param allocateWaiterId          ??????id
     * @return
     */
    private MessageVO createFromUserWaiterMessageVo(StoreUserSendMessage storeUserSendMessage, CustomSendMessageResultBO customSendMessageResultBO, String allocateWaiterId) {
        // ???????????????
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

        // ???????????????????????????
        messageVO.setSession(sessionVO);
        return messageVO;
    }

    /**
     * ???????????????????????????
     *
     * @param message     ??????
     * @param session     ??????
     * @param storeConfig ?????????????????????
     * @return
     */
    private MessageVO createFromStoreUserMessageVo(Message message, Session session, StoreConfig storeConfig) {
        // ???????????????
        MessageVO messageVO = messageConvert.toVo(message);
        messageVO.setFromNickname(storeConfig.getName());
        messageVO.setFromAvatar(storeConfig.getAvatar());
        messageVO.setOriginTraceType(PUSH_MESSAGE.getCode());
        messageVO.setOriginTraceId(null);
        messageVO.setToId(message.getFromId());

        // ????????????
        SessionVO sessionVO = sessionConvert.toVo(session);
        sessionVO.setFromNickname(storeConfig.getName());
        sessionVO.setFromAvatar(storeConfig.getAvatar());
        messageVO.setSession(sessionVO);

        // ???????????????????????????
        messageVO.setSession(sessionVO);
        return messageVO;
    }

    /**
     * ???????????????????????????
     *
     * @param userId      ??????
     * @param message     ??????
     * @param session     ??????
     * @param storeWaiter ??????????????????
     * @return
     */
    private MessageVO createFromWaiterUserMessageVo(String userId, Message message, Session session, StoreWaiter storeWaiter, StoreConfig storeConfig) {
        // ???????????????
        MessageVO messageVO = messageConvert.toVo(message);
        messageVO.setFromNickname(storeWaiter.getWaiterNickname());
        messageVO.setFromAvatar(storeWaiter.getWaiterAvatar());
        messageVO.setOriginTraceId(null);
        messageVO.setOriginTraceType(PUSH_MESSAGE.getCode());
        messageVO.setToId(userId);

        // ????????????
        SessionVO sessionVO = sessionConvert.toVo(session);
        sessionVO.setFromNickname(storeConfig.getName());
        sessionVO.setFromAvatar(storeConfig.getAvatar());
        messageVO.setSession(sessionVO);

        // ???????????????????????????
        messageVO.setSession(sessionVO);
        return messageVO;
    }

    /**
     * ???????????????????????????
     *
     * @param message     ??????
     * @param session     ??????
     * @param storeWaiter ??????????????????
     * @param storeUserSendMessage
     * @return
     */
    private MessageVO createFromWaiterWaiterMessageVo(Message message, Session session, StoreWaiter storeWaiter, StoreUserSendMessage storeUserSendMessage) {
        // ???????????????
        MessageVO messageVO = messageConvert.toVo(message);
        messageVO.setFromNickname(storeWaiter.getWaiterNickname());
        messageVO.setFromAvatar(storeWaiter.getWaiterAvatar());
        messageVO.setOriginTraceType(PUSH_MESSAGE.getCode());
        messageVO.setOriginTraceId(null);
        messageVO.setStoreId(storeWaiter.getStoreId());
        messageVO.setToId(storeWaiter.getWaiterId());

        // ????????????
        SessionVO sessionVO = sessionConvert.toVo(session);
        sessionVO.setFromNickname(storeUserSendMessage.getHandshakeParam().getNickname());
        sessionVO.setFromAvatar(storeUserSendMessage.getHandshakeParam().getAvatar());
        sessionVO.setFromId(storeUserSendMessage.getHandshakeParam().getUser());
        messageVO.setSession(sessionVO);

        // ???????????????????????????
        messageVO.setSession(sessionVO);
        return messageVO;
    }

    /**
     * ???????????????????????????
     *
     * @param message     ??????
     * @param session     ??????
     * @param storeWaiter ??????????????????
     * @param traceType
     * @param traceId
     * @return
     */
    private MessageVO createFromWaiterWaiterMessageVo(Message message, Session session, StoreWaiter storeWaiter, SessionUser sessionUser, int traceType, String traceId) {
        // ???????????????
        MessageVO messageVO = messageConvert.toVo(message);
        messageVO.setFromNickname(storeWaiter.getWaiterNickname());
        messageVO.setFromAvatar(storeWaiter.getWaiterAvatar());
        messageVO.setOriginTraceType(traceType);
        messageVO.setOriginTraceId(traceId);
        messageVO.setStoreId(storeWaiter.getStoreId());
        messageVO.setToId(storeWaiter.getWaiterId());

        // ????????????
        SessionVO sessionVO = sessionConvert.toVo(session);
        sessionVO.setFromNickname(sessionUser.getUserNickname());
        sessionVO.setFromAvatar(sessionUser.getUserAvatar());
        sessionVO.setFromId(sessionUser.getUserId());
        messageVO.setSession(sessionVO);

        // ???????????????????????????
        messageVO.setSession(sessionVO);
        return messageVO;
    }

    /**
     * ??????????????????
     *
     * @param storeWaiterSendMessage
     * @return
     */
    public void waiterSendMessage(StoreWaiterSendMessage storeWaiterSendMessage) {
        log.info("?????????????????? {}", storeWaiterSendMessage);

        UserWaiterRelation userWaiterRelation = userWaiterRelationService.findBySessionId(storeWaiterSendMessage.getToSessionId());
        if (!Objects.equals(userWaiterRelation.getWaiterId(), storeWaiterSendMessage.getFromId())) {
            log.error("??????????????? {} {}", storeWaiterSendMessage, userWaiterRelation);
            return;
        }

        // ????????????
        long now = System.currentTimeMillis();
        Message message = messageService.generateMqMessage(userWaiterRelation.getWaiterId(), storeWaiterSendMessage.getType(), storeWaiterSendMessage.getPayload(), storeWaiterSendMessage.getToSessionId(), now);
        SendMessageBO sendMessageBO = new SendMessageBO();
        sendMessageBO.setMessage(message);
        sendMessageBO.setAddUnreadUserIds(Sets.newHashSet(userWaiterRelation.getUserId()));
        sendMessageBO.setSysId(storeWaiterSendMessage.getSysId());
        CustomSendMessageResultBO customSendMessageResultBO = messageService.createCustomSendMessage(sendMessageBO);


        // ????????????
        Session session = customSendMessageResultBO.getSession();
        StoreWaiter storeWaiter = storeWaiterService.findByStoreIdAndWaiterId(storeWaiterSendMessage.getStoreId(), storeWaiterSendMessage.getFromId());
        StoreConfig storeConfig = storeConfigService.getById(storeWaiterSendMessage.getStoreId());
        MessageVO userMessageVO = createFromWaiterUserMessageVo(userWaiterRelation.getUserId(), customSendMessageResultBO.getMessage(), customSendMessageResultBO.getSession(), storeWaiter, storeConfig);
        RspMessageProto.Model rsp = RspFrameUtil.createModel(null, ChannelMessageTypeEnum.PUSH_MESSAGE.getCode(), RspMessage.SUCCESS, null, userMessageVO);
        log.info("????????????????????? {} {}", userWaiterRelation.getUserId(), userMessageVO);
        userChannelManager.pushOrForwardMessage(session.getSysId(), userWaiterRelation.getUserId(), rsp);

        SessionUser sessionUser = sessionUserService.findBySessionIdAndUserId(userWaiterRelation.getSessionId(), userWaiterRelation.getUserId());
        MessageVO waiterMessageVO = createFromWaiterWaiterMessageVo(customSendMessageResultBO.getMessage(), customSendMessageResultBO.getSession(), storeWaiter, sessionUser, storeWaiterSendMessage.getTraceType(), storeWaiterSendMessage.getTraceId());
        log.info("????????????????????? {} {}", userWaiterRelation.getWaiterId(), waiterMessageVO);
        rsp = RspFrameUtil.createModel(storeWaiterSendMessage.getTraceId(), storeWaiterSendMessage.getTraceType(), RspMessage.SUCCESS, null, waiterMessageVO);
        storeWaiterChannelManager.pushOrForwardMessage(session.getSysId(), storeWaiterSendMessage.getStoreId(), userWaiterRelation.getWaiterId(), rsp);
    }

    private String createAllocateWaiterPayload(StoreWaiter storeWaiter) {
        Map<String, String> payload = new HashMap<>();
        payload.put("text", "????????????" + storeWaiter.getWaiterNickname() + "???????????????????????????????????????????????????????????????");
        return JSON.toJSONString(payload);
    }

    private String createNotAllocateWaiterPayload() {
        Map<String, String> payload = new HashMap<>();
        payload.put("text", "??????????????????????????????~????????????????????????????????????????????????");
        return JSON.toJSONString(payload);
    }


    /**
     * ???????????????????????????
     *
     * @param sysId
     * @param storeId
     * @param waiterId
     * @param localAddress
     */
    public void renewalWaiterStoreSessionHeart(Integer sysId, Long storeId, String waiterId, String localAddress) {
        redisService.renewalStoreWaiterHeart(sysId, storeId, waiterId, localAddress);
        if (!redisService.existStoreWaiter(sysId, storeId, waiterId)) {
            // ????????????redis????????????
            redisService.addStoreWaiter(sysId, storeId, waiterId);
        }
    }

    /**
     * ???????????????????????????
     *
     * @param sysId
     * @param storeId
     * @param userId
     */
    public void renewalUserStoreSessionHeart(Integer sysId, Long storeId, String userId) {
        redisService.renewalUserStoreSessionHeart(sysId, storeId, userId);
    }

    /**
     * ????????????????????? ????????????????????????+???????????????????????????(??????20???)???
     *
     * @param systemId
     * @param storeId
     * @param waiterId
     * @return
     */
    public ListSessionVO findWaiterSessions(Integer systemId, Long storeId, String waiterId) {
        log.info("???????????????????????? {} {} {}", systemId, storeId, waiterId);
        // ????????????????????????
        List<UserWaiterRelation> dbUserWaiterRelations = userWaiterRelationService.findByWaiterAndStore(waiterId, storeId);
        Map<Long, String> dbWaiterSessionUserMap = dbUserWaiterRelations.stream().collect(Collectors.toMap(UserWaiterRelation::getSessionId, UserWaiterRelation::getUserId));
        log.info("????????????????????? {}", dbUserWaiterRelations);

        // ?????????????????????????????????
        List<UserWaiterRelation> redisUserWaiterRelations = storeUserQueueService.allocateUser(systemId, storeId, waiterId);
        log.info("????????????????????????????????? {}", redisUserWaiterRelations);
        Set<Long> allocateSessionIds = new HashSet<>();
        for (UserWaiterRelation userWaiterRelation : redisUserWaiterRelations) {
            userWaiterRelationService.addOrUpdate(waiterId, storeId, userWaiterRelation.getUserId(), userWaiterRelation.getSessionId());
            allocateSessionIds.add(userWaiterRelation.getSessionId());
        }

        // ????????????????????? + ??????????????????
        allocateSessionIds.addAll(dbWaiterSessionUserMap.keySet());
        log.info("?????????????????????????????? {}", allocateSessionIds);

        // ??????????????????
        List<Session> unReadSessions = sessionService.findUnreadSession(allocateSessionIds, storeId.toString());
        Set<Long> unreadSessionId = unReadSessions.stream().map(Session::getId).collect(Collectors.toSet());

        // ??????????????????
        List<Session> sessionsWithinTimes = sessionService.findWithinTimes(dbWaiterSessionUserMap.keySet(), SESSION_WITHIN_TIME_HOURS, SESSION_WITHIN_TIME_SIZE, storeId.toString());
        List<Session> waiterSessions = new LinkedList<>(unReadSessions);

        // ??????????????????+?????????????????? ???????????????
        waiterSessions.addAll(sessionsWithinTimes.stream().filter(session -> !unreadSessionId.contains(session.getId())).collect(Collectors.toList()));

        // ????????????????????????
        List<SessionUser> sessionUsers = sessionUserService.findSessionUser(dbWaiterSessionUserMap);
        Map<String, SessionUser> sessionUserMap = sessionUsers.stream().collect(Collectors.toMap(SessionUser::getUserId, Function.identity()));

        Long totalUnreadCount = 0L;
        List<SessionVO> sessionVOS = new ArrayList<>(waiterSessions.size());
        // todo ??????????????? ??????????????????redis
        for (Session session : waiterSessions) {
            SessionVO sessionVO = sessionConvert.toVo(session);

            // ??????????????????
            setLastMessage(session, sessionVO);

            // ????????????
            setOppositeUser(dbWaiterSessionUserMap, sessionUserMap, session, sessionVO);

            // ???????????????
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
     * ?????????????????????
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
     * ????????????????????????
     *
     * @param storeWaiterSendMessage
     */
    public void waiterReadMessage(StoreWaiterAlreadyReadMessage storeWaiterSendMessage) {
        log.info("???????????????????????? {}", storeWaiterSendMessage);
        Long sessionId = storeWaiterSendMessage.getSessionId();
        UserWaiterRelation userWaiterRelation = userWaiterRelationService.findBySessionId(sessionId);
        if (userWaiterRelation == null) {
            log.warn("???????????????????????? {}", sessionId);
            return;
        }
        String waiterId = userWaiterRelation.getWaiterId();

        if (!Objects.equals(waiterId, storeWaiterSendMessage.getWaiterId())) {
            return;
        }

        // ??????mq??????
        ImMqMessage imMqMessage = updateUnReadHandleService.createUpdateUnReadImMqMessage(sessionId, userWaiterRelation.getStoreId().toString());

        // ??????mq
        imMqProducerService.sendMq(imMqMessage, () -> updateUnReadHandleService.handle2Db(imMqMessage));

        // ????????????
        updateUnReadHandleService.handle2Cache(imMqMessage);
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param storeId
     * @param waiterId
     * @param start
     * @param end
     * @return
     */
    public ListSessionVO findWaiterHistorySession(Long storeId, String waiterId, Long start, Long end) {
        // ????????????????????????
        LocalDateTime startTime = DateUtil.toLocalDateTime(start);
        LocalDateTime endTime = DateUtil.toLocalDateTime(end);
        Map<Long, String> sessionUserMap = userWaiterHistoryLogService.findWaiterHistorySession(storeId, waiterId, startTime, endTime).stream().collect(Collectors.toMap(UserWaiterBo::getSessionId, UserWaiterBo::getUserId));

        // ?????????????????????
        List<Session> sessionsWithinTimes = sessionService.findBySessionIdsAndUserId(storeId.toString(), sessionUserMap.keySet());

        // ????????????
        List<SessionUser> sessionUsers = sessionUserService.findSessionUser(sessionUserMap);
        Map<String, SessionUser> userMap = sessionUsers.stream().collect(Collectors.toMap(SessionUser::getUserId, Function.identity()));

        Long totalUnreadCount = 0L;
        List<SessionVO> sessionVOS = new ArrayList<>(sessionsWithinTimes.size());
        // todo ??????????????? ??????????????????redis
        for (Session session : sessionsWithinTimes) {
            SessionVO sessionVO = sessionConvert.toVo(session);

            // ??????????????????
            setLastMessage(session, sessionVO);

            // ???????????????????????????
            setOppositeUser(sessionUserMap, userMap, session, sessionVO);

            // ???????????????
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
     * ??????????????????
     *
     * @param sysId        ??????id
     * @param sessionId    ??????id
     * @param waiterId     ??????id
     * @param topMessageId ????????????????????????id
     * @param size         ????????????
     * @return ????????????
     */
    public List<MessageVO> findWaiterSessionMessages(Integer sysId, Long sessionId, String waiterId, Long topMessageId, Long size) {

        List<Message> messages = messageService.findSessionMessages(sessionId, waiterId, topMessageId, size);
        if (messages.isEmpty()) {
            return Collections.emptyList();
        }

        UserWaiterRelation userWaiterRelation = userWaiterRelationService.findBySessionId(sessionId);
        String userId = userWaiterRelation.getUserId();

        // ???????????? ????????????
        StoreConfig storeConfig = storeConfigService.findBySysId(sysId);

        // ????????????
        List<StoreWaiter> storeWaiters = storeWaiterService.findBySysId(sysId);
        Map<String, StoreWaiter> waiterMap = storeWaiters.stream().collect(Collectors.toMap(StoreWaiter::getWaiterId, Function.identity()));
        Set<String> waiterIds = waiterMap.keySet();

        // ????????????
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
                // ???????????????????????????
                avatar = storeConfig.getAvatar();
                nickname = storeConfig.getName();
            } else if (Objects.equals(userId, fromId)) {
                // ?????????????????????
                SessionUser sessionUser = sessionUserMap.get(fromId);
                if (sessionUser != null) {
                    avatar = sessionUser.getUserAvatar();
                    nickname = sessionUser.getUserNickname();
                }
            } else {
                // ?????????????????????
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
     * ????????????????????????????????????id ????????????????????????
     *
     * @param messages  ??????
     * @param waiterIds ??????
     * @return ??????????????????id
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
