package com.msb.im.convert;

import com.msb.im.module.waiter.model.bo.WaiterBO;
import com.msb.im.module.waiter.model.vo.WaiterVO;
import com.msb.user.api.vo.EmployeeDO;
import org.mapstruct.Mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author zhoumiao
 * @date 2022-04-25 16:24:17
 */
@Mapper(componentModel = "spring")
public interface WaiterConvert {

    default WaiterBO toBo(EmployeeDO employeeDO) {
        if (employeeDO == null) {
            return null;
        } else {
            return WaiterBO.builder()
                    .waiterNickname(employeeDO.getEmployeeName())
                    .userId(employeeDO.getUserId() + "")
                    .isEnable(employeeDO.getIsEnable())
                    .build();
        }
    }

    default List<WaiterBO> toBos(List<EmployeeDO> employeeDOs) {
        if (employeeDOs.isEmpty()) {
            return Collections.emptyList();
        }
        return employeeDOs.stream().map(this::toBo).collect(Collectors.toList());
    }

    default WaiterVO toVo(EmployeeDO employeeDO) {
        if (employeeDO == null) {
            return null;
        } else {
            return WaiterVO.builder()
                    .waiterNickname(employeeDO.getEmployeeName())
                    .userId(employeeDO.getUserId() + "")
                    .isEnable(employeeDO.getIsEnable())
                    .build();
        }
    }

    default List<WaiterVO> toVos(List<EmployeeDO> employeeDOs) {
        if (employeeDOs.isEmpty()) {
            return Collections.emptyList();
        }
        return employeeDOs.stream().map(this::toVo).collect(Collectors.toList());
    }
}
