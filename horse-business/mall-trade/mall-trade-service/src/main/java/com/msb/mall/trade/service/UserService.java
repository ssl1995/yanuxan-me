package com.msb.mall.trade.service;

import com.msb.framework.common.utils.ListUtil;
import com.msb.framework.web.result.Assert;
import com.msb.framework.web.result.BizAssert;
import com.msb.mall.trade.exception.TradeExceptionCodeEnum;
import com.msb.user.api.UserDubboService;
import com.msb.user.api.dto.ListUserDTO;
import com.msb.user.api.vo.UserDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service("userService")
public class UserService {

    @DubboReference
    private UserDubboService userDubboService;

    /**
     * 根据用户ID查询用户信息，获取不到则返回空
     *
     * @param userId：用户ID
     * @return com.msb.user.api.vo.UserVO
     * @author peng.xy
     * @date 2022/4/6
     */
    public UserDO getUserInfoById(Long userId) {
        try {
            return userDubboService.getUserDetailInfoById(userId);
        } catch (Exception e) {
            log.error("调用用户服务失败", e);
            return null;
        }
    }

    /**
     * 根据用户ID查询用户信息，获取不到则抛出异常
     *
     * @param userId：用户ID
     * @return com.msb.user.api.vo.UserVO
     * @author peng.xy
     * @date 2022/4/6
     */
    public UserDO getUserInfoByIdOrThrow(Long userId) {
        UserDO userVO = this.getUserInfoById(userId);
        Assert.notNull(userVO, TradeExceptionCodeEnum.USER_EXCEPTION);
        return userVO;
    }

    /**
     * 根据用户ID查询用户信息，获取不到则返回空对象
     *
     * @param userId：用户ID
     * @return com.msb.user.api.vo.UserVO
     * @author peng.xy
     * @date 2022/4/6
     */
    public UserDO getUserInfoByIdOrEmpty(Long userId) {
        UserDO userVO = this.getUserInfoById(userId);
        return Optional.ofNullable(userVO).orElse(new UserDO());
    }

    /**
     * 根据用户ID列表获取用户信息List
     *
     * @param userIds：用户ID列表
     * @return java.util.List<com.msb.user.api.vo.UserVO>
     * @author peng.xy
     * @date 2022/3/31
     */
    public List<UserDO> listUserByIdsOrEmpty(@Nonnull List<Long> userIds) {
        try {
            return userDubboService.listUserByIds(userIds);
        } catch (Exception e) {
            log.error("调用用户服务失败", e);
            return Collections.emptyList();
        }
    }

    /**
     * 根据用户ID列表获取用户信息Map
     *
     * @param userIds：用户ID列表
     * @return java.util.Map<java.lang.Long, com.msb.user.api.vo.UserVO>
     * @author peng.xy
     * @date 2022/3/31
     */
    public Map<Long, UserDO> mapUserByIdsOrEmpty(@Nonnull List<Long> userIds) {
        List<UserDO> userListVO = this.listUserByIdsOrEmpty(userIds);
        if (CollectionUtils.isEmpty(userListVO)) {
            return Collections.emptyMap();
        }
        return userListVO.stream().collect(Collectors.toMap(UserDO::getId, Function.identity()));
    }

    /**
     * 获取查询用的用户ID列表，没有数据到则返回默认的列表
     *
     * @param phone：手机号码，不为空的话最少需要4位
     * @return java.util.List<java.lang.Long>
     * @author peng.xy
     * @date 2022/3/31
     */
    public List<Long> getQueryUserIdsOrDefault(String phone) {
        List<Long> queryUserIds = Collections.emptyList();
        if (StringUtils.isBlank(phone)) {
            return queryUserIds;
        }
        BizAssert.isTrue(phone.length() >= 4, "手机号码不能小于4位");
        ListUserDTO listUserDTO = new ListUserDTO().setPhone(phone);
        try {
            List<UserDO> userVOList = userDubboService.listUser(listUserDTO);
            if (CollectionUtils.isNotEmpty(userVOList)) {
                return ListUtil.convertDistinct(userVOList, UserDO::getId);
            }
        } catch (Exception e) {
            log.error("调用用户服务失败", e);
        }
        return Collections.singletonList(0L);
    }

}
