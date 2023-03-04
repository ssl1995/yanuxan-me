package com.msb.mall.marketing.client;

import com.msb.framework.mysql.BaseEntity;
import com.msb.mall.marketing.model.vo.AppMessagePushVO;
import com.msb.user.api.EmployeeDubboService;
import com.msb.user.api.vo.EmployeeDO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.aspectj.weaver.ast.Var;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class EmployeeDubboClient {

    @DubboReference
    private EmployeeDubboService employeeDubboService;

    public Map<Long, EmployeeDO> listEmployeeByUserId(List<? extends BaseEntity> list) {
        List<Long> userIdList = list.stream().map(BaseEntity::getCreateUser).collect(Collectors.toList());
        List<EmployeeDO> employeeList = employeeDubboService.listEmployeeByUserId(userIdList);
        return employeeList.stream().collect(Collectors.toMap(EmployeeDO::getUserId, Function.identity()));
    }
}
