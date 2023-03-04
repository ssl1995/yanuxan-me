package com.msb.user.api;


import com.msb.framework.common.model.UserLoginInfo;
import com.msb.user.api.dto.ListUserDTO;
import com.msb.user.api.vo.UserDO;
import com.msb.user.api.vo.UserOverviewDO;

import java.util.List;
import java.util.Map;

public interface UserDubboService {

    /**
     * 根据id 查询用户详细信息
     *
     * @param id Long
     * @return UserVO
     */
    UserDO getUserDetailInfoById(Long id);

    /**
     * 根据id的集合查询用户
     *
     * @param userIds List<Long>
     * @return List<UserVO>
     */
    List<UserDO> listUserByIds(List<Long> userIds);

    /**
     * 根据手机号精准查询
     *
     * @param phone String
     * @return UserVO
     */
    UserDO getUserByPhone(String phone);

    /**
     * 根据条件查询用户
     *
     * @param listUserDTO ListUserDTO
     * @return List<UserVO>
     */
    List<UserDO> listUser(ListUserDTO listUserDTO);


    /**
     * 校验token获取在线的用户信息，当token失效后返回为NULL值  (token自动续期)
     *
     * @param token String
     * @return UserLoginInfo
     */
    UserLoginInfo checkUserLoginInfo(String token);

    /**
     * 校验B端接口权限
     *
     * @param token   String
     * @param request map
     * @return UserLoginInfo
     */
    UserLoginInfo checkEmployeePermission(String token, Map<String, String> request);

    /**
     * 根据系统id获取用户id列表
     *
     * @param systemId long
     * @return list
     */
    List<Long> listUserId(Long systemId);

    /**
     * 获取用户总览信息
     * @return UserOverviewDO
     */
    UserOverviewDO getUserOverview();

    /**
     * 检查用户是否属于系统
     *
     * @param userId 用户
     * @param systemId 系统
     * @return 是否属于系统
     */
    Boolean checkUserSystem(Long userId, Long systemId);

}
