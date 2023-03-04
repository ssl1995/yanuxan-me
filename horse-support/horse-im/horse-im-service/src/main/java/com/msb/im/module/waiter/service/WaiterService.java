//package com.msb.im.module.waiter.service;
//
//import com.msb.framework.common.exception.BaseResultCodeEnum;
//import com.msb.framework.common.exception.BizException;
//import com.msb.im.convert.WaiterConvert;
//import com.msb.im.module.waiter.model.bo.WaiterBO;
//import com.msb.im.module.waiter.model.vo.WaiterVO;
//import com.msb.user.api.EmployeeDubboService;
//import com.msb.user.api.UserDubboService;
//import com.msb.user.api.vo.EmployeeDO;
//import com.msb.user.api.vo.UserDO;
//import org.apache.dubbo.config.annotation.DubboReference;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import java.util.*;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
///**
// * 客服
// *
// * @author zhou miao
// * @date 2022/05/10
// */
//@Service
//public class WaiterService {
//    @Value("${store.roleId}")
//    private Long roleId;
//    @DubboReference
//    private UserDubboService userDubboService;
//    @DubboReference
//    private EmployeeDubboService employeeDubboService;
//    @Resource
//    private WaiterConvert waiterConvert;
//
//    /**
//     * 通过商铺id查询所有客服
//     *
//     * @param storeId
//     * @return
//     */
//    public List<WaiterVO> findAllWaiter(Long storeId) {
//        if (Objects.equals(storeId, 1)) { // 严选
//            List<EmployeeDO> employeeDOS = employeeDubboService.listEmployeeByRoleId(roleId);
//            List<WaiterVO> waiterVOS = waiterConvert.toVos(employeeDOS);
//            setUserInfo(employeeDOS, waiterVOS);
//            return waiterVOS;
//        } else {
//            return Collections.emptyList();
//        }
//    }
//
//    private void setUserInfo(List<EmployeeDO> employeeDOS, List<WaiterVO> waiterVOS) {
//        List<Long> employeeUserIds = employeeDOS.stream().map(EmployeeDO::getUserId).collect(Collectors.toList());
//        List<UserDO> userDOS = userDubboService.listUserByIds(employeeUserIds);
//        Map<Long, UserDO> userDOMap = userDOS.stream().collect(Collectors.toMap(UserDO::getId, Function.identity()));
//
//        for (WaiterVO waiterBO : waiterVOS) {
//            UserDO userDO = userDOMap.get(waiterBO.getUserId());
//            if (userDO != null) {
//                waiterBO.setNickname(userDO.getNickname());
//                waiterBO.setAccount(userDO.getAccount());
//            }
//        }
//    }
//
//    public List<WaiterBO> findByIds(List<Long> userIds) {
//        List<EmployeeDO> employeeDOS = employeeDubboService.listEmployeeByUserId(userIds);
//        List<WaiterBO> waiterBOS = new ArrayList<>(employeeDOS.size());
//        for (EmployeeDO employeeDO : employeeDOS) {
//            waiterBOS.add(WaiterBO.builder().waiterNickname(employeeDO.getEmployeeName()).userId(employeeDO.getUserId() + "").isEnable(employeeDO.getIsEnable()).build());
//        }
//        return waiterBOS;
//    }
//
//    public WaiterBO findById(String userId) {
//        EmployeeDO employeeDO = employeeDubboService.getEmployeeByUserId(Long.parseLong(userId));
//        return WaiterBO.builder().waiterNickname(employeeDO.getEmployeeName()).userId(employeeDO.getUserId() + "").isEnable(employeeDO.getIsEnable()).build();
//    }
//
//    public void checkIsWaiter(String userId) {
//        List<EmployeeDO> employeeDOS = employeeDubboService.listEmployeeByRoleId(roleId);
//        if (employeeDOS.stream().map(EmployeeDO::getUserId).anyMatch(e -> Objects.equals(e, userId))) { // 属于客服可以申请
//            return;
//        }
//        throw new BizException(BaseResultCodeEnum.BIZ_ERROR.getCode(), "不是客服不允许申请");
//    }
//}
