package com.msb.im.module.waiter.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Sets;
import com.msb.framework.common.exception.BizException;
import com.msb.framework.common.model.PageDTO;
import com.msb.im.api.dto.*;
import com.msb.im.api.enums.MessageTypeEnum;
import com.msb.im.api.result.ImApiResultEnum;
import com.msb.im.api.vo.StoreWaiterVO;
import com.msb.im.context.ApiContext;
import com.msb.im.convert.MessageConvert;
import com.msb.im.convert.SessionConvert;
import com.msb.im.convert.StoreWaiterConvert;
import com.msb.im.model.bo.CustomSendMessageResultBO;
import com.msb.im.model.bo.SendMessageBO;
import com.msb.im.model.entity.Message;
import com.msb.im.model.entity.Session;
import com.msb.im.model.entity.SessionUser;
import com.msb.im.model.vo.MessageVO;
import com.msb.im.model.vo.SessionVO;
import com.msb.im.module.chat.channel.UserChannelManager;
import com.msb.im.module.chat.model.RspMessage;
import com.msb.im.module.waiter.channel.StoreWaiterChannelManager;
import com.msb.im.module.waiter.mapper.StoreWaiterMapper;
import com.msb.im.module.waiter.model.entity.StoreConfig;
import com.msb.im.module.waiter.model.entity.StoreWaiter;
import com.msb.im.module.waiter.model.entity.UserWaiterRelation;
import com.msb.im.portobuf.RspMessageProto;
import com.msb.im.service.MessageService;
import com.msb.im.service.SessionUserService;
import com.msb.im.util.RspFrameUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

import static com.msb.im.netty.ChannelMessageTypeEnum.PUSH_MESSAGE;


/**
 * 商铺和客服的关联
 *
 * @author zhoumiao
 * @since 2022-04-13 16:28:56
 */
@Service
@Slf4j
public class StoreWaiterService extends ServiceImpl<StoreWaiterMapper, StoreWaiter> {

    @Resource
    private StoreWaiterConvert storeWaiterConvert;
    @Resource
    private StoreConfigService storeConfigService;
    @Resource
    private UserWaiterRelationService userWaiterRelationService;
    @Resource
    private UserWaiterHistoryLogService userWaiterHistoryLogService;
    @Resource
    private UserChannelManager userChannelManager;
    @Resource
    private StoreWaiterChannelManager storeWaiterChannelManager;
    @Resource
    private SessionUserService sessionUserService;
    @Resource
    private MessageService messageService;
    @Resource
    private MessageConvert messageConvert;
    @Resource
    private SessionConvert sessionConvert;

    public IPage<StoreWaiterVO> page(PageDTO pageDTO, StoreWaiterDTO storeWaiterDTO) {
        Long storeId = storeWaiterDTO.getStoreId();
        if (storeId == null) {
            StoreConfig storeConfig = storeConfigService.findBySysId(storeWaiterDTO.getSystemId());
            storeId = storeConfig.getId();
        }
        storeWaiterDTO.setStoreId(storeId);

        LambdaQueryWrapper<StoreWaiter> queryWrapper = new LambdaQueryWrapper<>();

        StoreWaiter selectStoreWaiter = storeWaiterConvert.toDo(storeWaiterDTO);
        selectStoreWaiter.setWaiterNickname(null);
        queryWrapper.setEntity(selectStoreWaiter);

        String waiterNickname = storeWaiterDTO.getWaiterNickname();
        if (!StringUtils.isEmpty(waiterNickname)) {
            queryWrapper.like(StoreWaiter::getWaiterNickname, waiterNickname);
        }
        return storeWaiterConvert.toVo(this.page(pageDTO.page(), queryWrapper));
    }

    public StoreWaiter findByStoreIdAndWaiterId(Long storeId, String waiterId) {
        return lambdaQuery()
                .eq(StoreWaiter::getStoreId, storeId)
                .eq(StoreWaiter::getWaiterId, waiterId)
                .one();
    }

    public List<StoreWaiter> findBySysId(int sysId) {
        return baseMapper.findBySysId(sysId);
    }

    public void add(AddStoreWaiterDTO addStoreWaiterDTO) {
        StoreWaiter storeWaiter = storeWaiterConvert.toDo(addStoreWaiterDTO);
        Long storeId = addStoreWaiterDTO.getStoreId();
        if (storeId == null) {
            StoreConfig storeConfig = storeConfigService.findBySysId(addStoreWaiterDTO.getSystemId());
            storeId = storeConfig.getId();
        }
        storeWaiter.setStoreId(storeId);
        String userId = ApiContext.getFrom();
        storeWaiter.setCreateUser(userId);
        storeWaiter.setUpdateUser(userId);
        LocalDateTime now = LocalDateTime.now();
        storeWaiter.setCreateTime(now);
        storeWaiter.setUpdateTime(now);
        storeWaiter.setIsDeleted(false);
        save(storeWaiter);
    }

    /**
     * 根据店铺id和waiterId进行删除
     *
     * @param deleteStoreWaiterDTO
     */
    public void delete(DeleteStoreWaiterDTO deleteStoreWaiterDTO) {
        Long storeId = deleteStoreWaiterDTO.getStoreId();
        if (storeId == null) {
            StoreConfig storeConfig = storeConfigService.findBySysId(deleteStoreWaiterDTO.getSystemId());
            storeId = storeConfig.getId();
        }
        List<String> waiterIds = deleteStoreWaiterDTO.getWaiterIds();
        lambdaUpdate()
                .in(StoreWaiter::getWaiterId, waiterIds)
                .eq(StoreWaiter::getStoreId, storeId)
                .set(StoreWaiter::getIsDeleted, true)
                .update();
    }

    /**
     * 根据店铺id和客服id更新客服的头像和昵称
     *
     * @param updateStoreWaiterDTO
     */
    public void update(UpdateStoreWaiterDTO updateStoreWaiterDTO) {
        Long storeId = updateStoreWaiterDTO.getStoreId();
        if (storeId == null) {
            StoreConfig storeConfig = storeConfigService.findBySysId(updateStoreWaiterDTO.getSystemId());
            storeId = storeConfig.getId();
        }
        lambdaUpdate()
                .eq(StoreWaiter::getStoreId, storeId)
                .eq(StoreWaiter::getWaiterId, updateStoreWaiterDTO.getWaiterId())
                .set(StoreWaiter::getWaiterNickname, updateStoreWaiterDTO.getWaiterNickname())
                .set(StoreWaiter::getWaiterAvatar, updateStoreWaiterDTO.getWaiterAvatar())
                .set(StoreWaiter::getUpdateTime, LocalDateTime.now())
                .set(StoreWaiter::getUpdateUser, ApiContext.getFrom())
                .update();
    }

    public void transfer(TransferWaiterDTO transferWaiterDTO) {
        log.info("客服转移会话 {}", transferWaiterDTO);

        // 当前会话关联信息
        Long sessionId = transferWaiterDTO.getSessionId();
        UserWaiterRelation userWaiterRelation = userWaiterRelationService.findBySessionId(sessionId);

        if (Objects.equals(transferWaiterDTO.getToWaiterId(), transferWaiterDTO.getCurrentWaiterId())) {
            log.error("不能自己转移自己 {}", transferWaiterDTO);
            throw new BizException(ImApiResultEnum.API_CURL_FAIL.getCode(), "不能自己转移自己");
        }

        if (!Objects.equals(userWaiterRelation.getWaiterId(), transferWaiterDTO.getCurrentWaiterId())) {
            log.error("当前会话不属于当前客服无法转移 {} {}", transferWaiterDTO, userWaiterRelation);
            throw new BizException(ImApiResultEnum.API_CURL_FAIL.getCode(), "当前会话不属于当前客服无法转移");
        }

        // 客服的消息
        StoreWaiter currentWaiter = findByStoreIdAndWaiterId(transferWaiterDTO.getStoreId(), transferWaiterDTO.getCurrentWaiterId());
        StoreWaiter toWaiter = findByStoreIdAndWaiterId(transferWaiterDTO.getStoreId(), transferWaiterDTO.getToWaiterId());
        if (toWaiter == null) {
            throw new BizException(ImApiResultEnum.API_CURL_FAIL.getCode(), "被转移客服不存在");
        }


        // 更新转移记录
        userWaiterRelationService.updateWaiterId(transferWaiterDTO.getStoreId(), userWaiterRelation.getUserId(), transferWaiterDTO.getToWaiterId(), transferWaiterDTO.getCurrentWaiterId(), LocalDateTime.now());
        userWaiterHistoryLogService.add(transferWaiterDTO.getStoreId(), userWaiterRelation.getUserId(), transferWaiterDTO.getToWaiterId(), transferWaiterDTO.getCurrentWaiterId(), transferWaiterDTO.getCurrentWaiterId());

        // 消息入库
        CustomSendMessageResultBO customSendMessageResultBO = createCustomSendMessageResultBO(transferWaiterDTO, userWaiterRelation, currentWaiter, toWaiter);

        // 给被分配的客服、分配的客服、用户推送消息
        StoreConfig storeConfig = storeConfigService.findBySysId(transferWaiterDTO.getSystemId());
        MessageVO userMessageVO = createFromWaiterUserMessageVo(transferWaiterDTO.getCurrentWaiterId(), customSendMessageResultBO.getMessage(), customSendMessageResultBO.getSession(), currentWaiter, storeConfig);
        RspMessageProto.Model rsp = RspFrameUtil.createModel(null, PUSH_MESSAGE.getCode(), RspMessage.SUCCESS, null, userMessageVO);
        log.info("给用户发送消息 {} {}", userWaiterRelation.getUserId(), userMessageVO);
        userChannelManager.pushOrForwardMessage(customSendMessageResultBO.getSession().getSysId(), userWaiterRelation.getUserId(), rsp);

        /*SessionUser sessionUser = sessionUserService.findBySessionIdAndUserId(userWaiterRelation.getSessionId(), userWaiterRelation.getUserId());
        MessageVO waiterMessageVO = createFromWaiterWaiterMessageVo(customSendMessageResultBO.getMessage(), customSendMessageResultBO.getSession(), currentWaiter, sessionUser, PUSH_MESSAGE.getCode(), null);
        log.info("给当前客服发送消息 {} {}", transferWaiterDTO.getCurrentWaiterId(), waiterMessageVO);
        rsp = RspFrameUtil.createModel(null, PUSH_MESSAGE.getCode(), RspMessage.SUCCESS, null, waiterMessageVO);
        storeWaiterChannelManager.pushOrForwardMessage(customSendMessageResultBO.getSession().getSysId(), userWaiterRelation.getStoreId(), transferWaiterDTO.getCurrentWaiterId(), rsp);*/

        SessionUser sessionUser = sessionUserService.findBySessionIdAndUserId(userWaiterRelation.getSessionId(), userWaiterRelation.getUserId());
        MessageVO waiterMessageVO = createFromWaiterWaiterMessageVo(customSendMessageResultBO.getMessage(), customSendMessageResultBO.getSession(), toWaiter, sessionUser, PUSH_MESSAGE.getCode(), null);
        log.info("给被转移客服发送消息 {} {}", transferWaiterDTO.getToWaiterId(), waiterMessageVO);
        rsp = RspFrameUtil.createModel(null, PUSH_MESSAGE.getCode(), RspMessage.SUCCESS, null, waiterMessageVO);
        storeWaiterChannelManager.pushOrForwardMessage(customSendMessageResultBO.getSession().getSysId(), userWaiterRelation.getStoreId(), transferWaiterDTO.getToWaiterId(), rsp);
    }

    private CustomSendMessageResultBO createCustomSendMessageResultBO(TransferWaiterDTO transferWaiterDTO, UserWaiterRelation userWaiterRelation, StoreWaiter currentWaiter, StoreWaiter toWaiter) {
        String payload = createTransferPayload(transferWaiterDTO, currentWaiter, toWaiter);
        Message message2Mq = messageService.generateMqMessage(transferWaiterDTO.getCurrentWaiterId(), MessageTypeEnum.CUSTOM.getCode(), payload, transferWaiterDTO.getSessionId(), System.currentTimeMillis());
        SendMessageBO sendMessageBO = new SendMessageBO();
        sendMessageBO.setMessage(message2Mq);
        HashSet<String> addUnreadUserIds = Sets.newHashSet(transferWaiterDTO.getStoreId().toString(), userWaiterRelation.getUserId());
        sendMessageBO.setAddUnreadUserIds(addUnreadUserIds);
        sendMessageBO.setSysId(transferWaiterDTO.getSystemId());
        return messageService.createCustomSendMessage(sendMessageBO);
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
     * 创建转移会话的消息
     *
     * @param transferWaiterDTO
     * @param currentWaiter
     * @param toWaiter
     * @return
     */
    private String createTransferPayload(TransferWaiterDTO transferWaiterDTO, StoreWaiter currentWaiter, StoreWaiter toWaiter) {
        Map<String, String> payload = new HashMap<>(1);
        if (!StringUtils.isEmpty(transferWaiterDTO.getReason())) {
            payload.put("reason", transferWaiterDTO.getReason());
        }
        payload.put("fromNickname", currentWaiter.getWaiterNickname());
        payload.put("toNickname", toWaiter.getWaiterNickname());
        payload.put("customType", "transferWaiterSession");
        return JSON.toJSONString(payload);
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
    private MessageVO createFromWaiterWaiterMessageVo(Message message, Session session, StoreWaiter storeWaiter, SessionUser sessionUser, Integer traceType, String traceId) {
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

    public List<StoreWaiter> findByStoreIds(List<Long> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }
        return lambdaQuery()
                .in(StoreWaiter::getStoreId, ids)
                .list();
    }
}
