package com.msb.user.core.manager;

import com.alibaba.nacos.common.utils.Md5Utils;
import com.msb.framework.common.exception.BaseResultCodeEnum;
import com.msb.framework.common.exception.BizException;
import com.msb.framework.common.model.UserLoginInfo;
import com.msb.framework.redis.RedisClient;
import com.msb.user.api.mq.UserTokenLogoutMessage;
import com.msb.user.auth.JwtProperties;
import com.msb.user.auth.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.msb.user.core.model.constant.RedisKeysConstants.USER_TOKEN_MAP;

@Slf4j
@Component
public class TokenManager {

    @Resource
    private RedisClient redisClient;

    @Resource
    private JwtProperties jwtProperties;

    @Resource
    private UserTokenLogoutMessage userTokenLogoutMessage;

    private Claims parsingToken(String token) {
        Jws<Claims> claimsJws;
        try {
            claimsJws = JwtUtils.parserToken(token, jwtProperties.getPublicKey());
            return claimsJws.getBody();
        } catch (Exception e) {
            log.error("解析token 错误  token {} ", token, e);
            throw new BizException(BaseResultCodeEnum.TOKEN_FAIL);
        }
    }

    private Long parsingTokenToUserId(String token) {
        Claims claims = parsingToken(token);
        return Long.valueOf(claims.get("id").toString());
    }

    private void cleanExpireUserSession(Long userId) {
        Map<String, UserLoginInfo> userTokenMap = getUserTokenMap(userId);
        userTokenMap.forEach((token, userLoginInfo) -> {
            if (LocalDateTime.now().isAfter(userLoginInfo.getTokenExpireTime().plusDays(7))) {
                removeToken(userLoginInfo);
            }
        });
    }

    public void removeToken(UserLoginInfo userLoginInfo) {
        if (Objects.nonNull(userLoginInfo.getToken())) {
            redisClient.hDelete(USER_TOKEN_MAP.concat(String.valueOf(userLoginInfo.getId())), userLoginInfo.getToken());
        }
//        UserTokenLogoutMessageBody userTokenLogoutMessageBody = new UserTokenLogoutMessageBody()
//                .setUserId(userLoginInfo.getId())
//                .setToken(userLoginInfo.getToken())
//                .setSessionId(userLoginInfo.getSessionId())
//                .setSystemId(userLoginInfo.getSystemId())
//                .setClientId(userLoginInfo.getClientId());
//        userTokenLogoutMessage.product(userTokenLogoutMessageBody);
    }


    public UserLoginInfo generateUserTokenAndSessionWriteRedis(UserLoginInfo userLoginInfo) {

        Long userId = userLoginInfo.getId();
        HashMap<String, Object> stringObjectHashMap = new HashMap<>(1);
        stringObjectHashMap.put("id", userId);
        stringObjectHashMap.put("time", System.currentTimeMillis());

        String jwtToken = JwtUtils.generateToken(stringObjectHashMap, jwtProperties.getPrivateKey());
        //设置sessionId，token 放入Redis
        userLoginInfo.setToken(jwtToken);
        userLoginInfo.setSessionId(Md5Utils.getMD5(jwtToken.getBytes()));
        userLoginInfo.setTokenExpireTime(LocalDateTime.now().plusMinutes(jwtProperties.getExpiration()));

        String key = USER_TOKEN_MAP.concat(userId.toString());
        redisClient.hSet(key, jwtToken, userLoginInfo);

        //清除过期的会话
        cleanExpireUserSession(userId);

        getExpireLongestTime(userId).ifPresent(localDateTime ->
                redisClient.expire(key, Duration.between(LocalDateTime.now(), localDateTime).toMinutes() + jwtProperties.getRefreshExpiration(), TimeUnit.MINUTES));

        return userLoginInfo;
    }

    private Optional<LocalDateTime> getExpireLongestTime(Long userId) {
        Map<String, UserLoginInfo> userTokenMap = getUserTokenMap(userId);
        return userTokenMap.values().stream().map(UserLoginInfo::getTokenExpireTime).max(LocalDateTime::compareTo);
    }


    public Map<String, UserLoginInfo> getUserTokenMap(Long userId) {
        return redisClient.hGetAll(USER_TOKEN_MAP.concat(String.valueOf(userId)));
    }

    public UserLoginInfo checkUserLoginInfo(String token) {
        log.info("checkUserLoginInfo token {}", token);
        Long userId = parsingTokenToUserId(token);
        UserLoginInfo userLoginInfo = redisClient.hGet(USER_TOKEN_MAP.concat(String.valueOf(userId)), token);
        if (userLoginInfo == null) {
            return null;
        }
        LocalDateTime tokenExpireTime = userLoginInfo.getTokenExpireTime();
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(tokenExpireTime)) {
            if (Duration.between(tokenExpireTime, now).toMinutes() < jwtProperties.getRefreshExpiration()) {
                //刷新token过期时间
                return generateUserTokenAndSessionWriteRedis(userLoginInfo);
            } else {
                //token 彻底失效了，清除一下
                cleanExpireUserSession(userLoginInfo.getId());
                return null;
            }
        }
        return userLoginInfo;
    }
}
