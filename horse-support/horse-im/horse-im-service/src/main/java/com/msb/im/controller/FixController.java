package com.msb.im.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.msb.im.api.enums.SessionTypeEnum;
import com.msb.im.model.entity.Session;
import com.msb.im.model.entity.SessionUser;
import com.msb.im.redis.SessionRedisService;
import com.msb.im.service.SessionService;
import com.msb.im.service.SessionUserService;
import com.msb.user.api.UserDubboService;
import com.msb.user.api.vo.UserDO;
import com.msb.user.auth.NoAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zhou miao
 * @date 2022/05/27
 */
@Api(tags = "im数据修复相关接口 用完请删除")
@RestController
@RequestMapping("")
@Slf4j
public class FixController {
    @Resource
    private SessionService sessionService;
    @Resource
    private SessionUserService sessionUserService;
    @DubboReference
    private UserDubboService userDubboService;
    @Resource
    private SessionRedisService sessionRedisService;

    @NoAuth
    @ApiOperation("fixRelationUser")
    @GetMapping("/fixRelationUser")
    public int fixRelationUserId() {
        LambdaQueryWrapper<SessionUser> sessionUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sessionUserLambdaQueryWrapper.isNull(SessionUser::getRelationUserId);
        List<SessionUser> relationUserIsNull = sessionUserService.list(sessionUserLambdaQueryWrapper);
        List<Long> sessionIds = relationUserIsNull.stream().map(SessionUser::getSessionId).collect(Collectors.toList());
        List<SessionUser> sessionUserAll = sessionUserService.lambdaQuery().in(SessionUser::getSessionId, sessionIds).list();
        Map<Long, List<SessionUser>> sessionMap = sessionUserAll.stream().collect(Collectors.groupingBy(SessionUser::getSessionId));
        int count = 0;
        for (SessionUser sessionUser : relationUserIsNull) {
            List<SessionUser> sessionUsers = sessionMap.getOrDefault(sessionUser.getSessionId(), Collections.emptyList());
            SessionUser relationUser = sessionUsers.stream().filter(e -> !Objects.equals(sessionUser.getUserId(), e.getUserId())).findAny().orElse(null);
            if (relationUser != null) {
                if (sessionUserService.lambdaUpdate().eq(SessionUser::getSessionId, sessionUser.getSessionId()).eq(SessionUser::getUserId, sessionUser.getUserId()).set(SessionUser::getRelationUserId, relationUser.getUserId()).update()) {
                    sessionRedisService.removeSessionUser(sessionUser.getUserId(), sessionUser.getSessionId());
                    count++;
                }
            }
        }
        return count;
    }

    @NoAuth
    @ApiOperation("fixSessionUserData")
    @GetMapping("/fixSessionUserData")
    public int fixSessionUserData() {
        List<SessionUser> sessionUserAll = sessionUserService.lambdaQuery().isNull(SessionUser::getUserAvatar).list();
        List<Long> userIds = sessionUserAll.stream().map(SessionUser::getUserId).map(Long::parseLong).collect(Collectors.toList());
        List<UserDO> userDOS = userDubboService.listUserByIds(userIds);
        Map<Long, UserDO> userDOMap = userDOS.stream().collect(Collectors.toMap(UserDO::getId, Function.identity()));
        int count = 0;
        for (SessionUser sessionUser : sessionUserAll) {
            String userId = sessionUser.getUserId();
            UserDO userDO = userDOMap.get(Long.parseLong(userId));
            if (userDO != null) {
                if (sessionUserService.lambdaUpdate().eq(SessionUser::getSessionId, sessionUser.getSessionId()).eq(SessionUser::getUserId, sessionUser.getUserId()).set(SessionUser::getUserAvatar, userDO.getAvatar()).set(SessionUser::getUserNickname, userDO.getNickname()).update()) {
                    sessionRedisService.removeSessionUser(sessionUser.getUserId(), sessionUser.getSessionId());
                    count++;
                }
            }
        }
        return count;
    }

    @NoAuth
    @ApiOperation("fixSessionCustom")
    @GetMapping("/fixSessionCustom")
    public int fixSessionCustom() {
        List<Session> list = sessionService.lambdaQuery().eq(Session::getType, SessionTypeEnum.SINGLE.getCode()).list();
        int count = 0;
        for (Session session : list) {
            if (sessionService.lambdaUpdate().eq(Session::getId, session.getId()).set(Session::getType, SessionTypeEnum.CUSTOM.getCode()).set(Session::getPayload, "{\"type\": \"system\"}").update()) {
                sessionRedisService.removeSession(session.getId());
                count++;
            }
        }
        return count;
    }

}
