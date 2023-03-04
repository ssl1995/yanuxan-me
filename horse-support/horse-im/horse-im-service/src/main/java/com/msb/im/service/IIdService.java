package com.msb.im.service;

/**
 * id服务的接口 暂时只有redis的实现 后期可以扩展其他实现
 *
 * @author zhoumiao
 * @since 2022-04-13 16:28:57
 */
public interface IIdService {
    /**
     * 生成消息id
     *
     * @return
     */
    Long generateMessageId();

    /**
     * 存在消息id的缓存
     *
     * @return
     */
    Boolean existMessageId();

    /**
     * 加载数据库最大id到缓存
     */
    void loadDbMaxMessageId2Cache();


    /**
     * 更新最大id记录
     *
     * @param maxId
     */
    void updateMessageId(Long maxId);


    /**
     * 生成会话id
     *
     * @return
     */
    Long generateSessionId();

    /**
     * 存在会话id的缓存
     *
     * @return
     */
    Boolean existSessionId();

    void loadDbMaxSessionId2Cache();

    /**
     * 更新会话id
     */
    void updateSessionId(Long maxId);


    /**
     * 生成会话和用户关联id
     *
     * @return
     */
    Long generateSessionUserId();

    /**
     * 存在会话用户id的缓存
     *
     * @return
     */
    Boolean existSessionUserId();

    void loadDbMaxSessionUserId2Cache();

    /**
     * 更新会话和用户的关联id
     *
     * @param maxId
     */
    void updateSessionUserId(Long maxId);

    /**
     * 生成会话中消息连续id
     *
     * @return
     */
    Long generateSessionMessageId(Long sessionId);

    /**
     * 更新会话中最大id
     *
     * @param sessionId
     * @param sessionMessageMaxId
     */
    void updateSessionMessageMaxId(Long sessionId, Long sessionMessageMaxId);
}
