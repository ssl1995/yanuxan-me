package com.msb.user.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.common.context.UserContext;
import com.msb.framework.common.utils.DateUtil;
import com.msb.framework.common.utils.SpringContextUtil;
import com.msb.framework.redis.annotation.CacheExpire;
import com.msb.framework.redis.consts.Time;
import com.msb.im.api.MallImDubboService;
import com.msb.im.api.dto.UpdateSessionUserDTO;
import com.msb.framework.common.utils.DateUtil;
import com.msb.user.api.dto.ListUserDTO;
import com.msb.user.api.vo.UserDO;
import com.msb.user.api.vo.UserOverviewDO;
import com.msb.user.core.mapper.UserMapper;
import com.msb.user.core.model.dto.UserAdminQueryDTO;
import com.msb.user.core.model.dto.UserInfoUpdateDTO;
import com.msb.user.core.model.entity.User;
import com.msb.user.core.model.vo.UserVO;
import com.msb.user.core.service.convert.UserConvert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * (User)表服务实现类
 *
 * @author makejava
 * @since 2022-03-23 20:13:02
 */
@Service("userService")
@Slf4j
public class UserService extends ServiceImpl<UserMapper, User> {

    @Resource
    private UserConvert userConvert;

    @Resource
    private UserMapper userMapper;

    @Value("${user.default.head}")
    private String userDefaultHead;

    @DubboReference
    private MallImDubboService mallImDubboService;

    public Optional<User> getUserOptional(Long userId) {
        return this.lambdaQuery().eq(User::getId, userId).oneOpt();
    }

    public Optional<User> getUserOptional(String phone) {
        return super.lambdaQuery().eq(User::getPhone, phone).oneOpt();
    }

    public User register(String phone, int systemId, int clientId) {
        User user = User.builder().account("").nickname("随机昵称").avatar(userDefaultHead).gender(3).phone(phone).password("").idCard("")
                .systemId(systemId).clientId(clientId).unionId("").isEnable(true).lastLoginTime(LocalDateTime.now()).build();
        this.save(user);
        return user;
    }

    @CacheEvict(value = "user", key = "#userId")
    public void updateUserToC(Long userId, UserInfoUpdateDTO userInfoUpdateDTO) {
        User user = new User();
        user.setAvatar(userInfoUpdateDTO.getAvatar())
                .setNickname(userInfoUpdateDTO.getNickname())
                .setGender(userInfoUpdateDTO.getGender())
                .setId(userId);
        boolean update = this.updateById(user);
        if (update) {
            updateImCenterUserData(userInfoUpdateDTO, userId);
        }
    }

    private void updateImCenterUserData(UserInfoUpdateDTO userInfoUpdateDTO, Long userId) {
        UpdateSessionUserDTO updateSessionUserDTO = new UpdateSessionUserDTO();
        updateSessionUserDTO.setUserId(userId.toString()).setUserNickname(userInfoUpdateDTO.getNickname()).setUserAvatar(userInfoUpdateDTO.getAvatar());
        Boolean result = mallImDubboService.updateImCenterUserData(updateSessionUserDTO);
        log.info("修改用信息 {} {}", updateSessionUserDTO, result);
    }

    @CacheExpire(Time.DAY)
    @Cacheable(value = "user")
    public User getUser(Long id) {
        return this.getById(id);
    }

    public IPage<UserVO> pageUser(UserAdminQueryDTO userAdminQueryDTO) {
        Page<User> page = this.lambdaQuery()
                .eq(StringUtils.isNotBlank(userAdminQueryDTO.getAccount()), User::getAccount, userAdminQueryDTO.getAccount())
                .eq(StringUtils.isNotBlank(userAdminQueryDTO.getPhone()), User::getPhone, userAdminQueryDTO.getPhone())
                .like(StringUtils.isNotBlank(userAdminQueryDTO.getNickname()), User::getNickname, userAdminQueryDTO.getNickname())
                .between(Objects.nonNull(userAdminQueryDTO.getRegisteredEndTime()), User::getCreateTime, userAdminQueryDTO.getRegisteredStartTime(), userAdminQueryDTO.getRegisteredEndTime())
                .page(userAdminQueryDTO.page());
        return userConvert.toVo(page);
    }

    public List<UserDO> listUserByIds(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Collections.emptyList();
        }
        return userConvert.toDo(this.lambdaQuery().in(User::getId, userIds).list());
    }

    public UserDO getUserByPhone(String phone) {
        return userConvert.toDo(this.lambdaQuery().eq(User::getPhone, phone).one());
    }

    public List<UserDO> listUser(ListUserDTO listUserDTO) {
        if (StringUtils.isBlank(listUserDTO.getNickname()) && StringUtils.isBlank(listUserDTO.getPhone())) {
            return Collections.emptyList();
        }
        List<User> list = this.lambdaQuery().like(StringUtils.isNotBlank(listUserDTO.getNickname()), User::getNickname, listUserDTO.getNickname())
                .and(StringUtils.isNotBlank(listUserDTO.getPhone()), phoneQuery -> phoneQuery.eq(User::getPhone, listUserDTO.getPhone()).or().likeLeft(User::getPhone, listUserDTO.getPhone()).or().likeRight(User::getPhone, listUserDTO.getPhone()))
                .list();
        return userConvert.toDo(list);
    }

    /**
     * 获取用户总览
     * @return UserOverviewDO
     */
    public UserOverviewDO getUserOverview() {
        LocalDateTime yesterdayBegin = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIN);
        LocalDateTime yesterdayEnd = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MAX);

        LocalDateTime thisMonthFirstDayBegin = LocalDateTime.of(DateUtil.getMonthFirstDay(), LocalTime.MIN);
        LocalDateTime thisMonthLastDayEnd = LocalDateTime.of(DateUtil.getMonthLastDay(), LocalTime.MAX);
        return new UserOverviewDO()
                .setAllCount(userMapper.getAllUserCount())
                .setTodayIncreaseCount(userMapper.getAddUserCount(DateUtil.todayStart(), DateUtil.todayEnd()))
                .setYesterdayIncreaseCount(userMapper.getAddUserCount(yesterdayBegin, yesterdayEnd))
                .setMonthIncreaseCount(userMapper.getAddUserCount(thisMonthFirstDayBegin, thisMonthLastDayEnd));
    }
}

