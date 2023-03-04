package com.msb.user.core.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.common.exception.BizException;
import com.msb.framework.redis.annotation.CacheExpire;
import com.msb.user.api.vo.EmployeeDO;
import com.msb.user.core.mapper.EmployeeMapper;
import com.msb.user.core.model.dto.EmployeeQueryDTO;
import com.msb.user.core.model.dto.EmployeeSaveDTO;
import com.msb.user.core.model.dto.EmployeeUpdateDTO;
import com.msb.user.core.model.entity.Employee;
import com.msb.user.core.model.entity.EmployeeDepartment;
import com.msb.user.core.model.entity.User;
import com.msb.user.core.model.vo.EmployeeVO;
import com.msb.user.core.service.convert.EmployeeConvert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.msb.user.core.model.constant.RedisKeysConstants.GET_EMPLOYEE_USERID;

/**
 * (Employee)表服务实现类
 *
 * @author makejava
 * @since 2022-03-23 20:13:01
 */
@Service("employeeService")
public class EmployeeService extends ServiceImpl<EmployeeMapper, Employee> {

    @Resource
    private EmployeeConvert employeeConvert;

    @Resource
    private UserService userService;

    @Resource
    private EmployeeDepartmentService employeeDepartmentService;

    @Resource
    private EmployeeRoleService employeeRoleService;

    @Resource
    private RoleService roleService;

    @Resource
    private DepartmentService departmentService;

    public void resetPassword(Long employeeId) {
        Employee oldEmployee = this.getById(employeeId);
        Employee employee = new Employee().setId(employeeId).setPassword(oldEmployee.getPhone());
        this.updateById(employee);
    }

    public List<EmployeeDO> listEmployeeByRoleId(Long roleId) {
        List<Long> employeeIdList = employeeRoleService.listEmployee(roleId);
        List<Employee> list = this.lambdaQuery().in(Employee::getId, employeeIdList).list();
        return employeeConvert.toDo(list);
    }

    public List<EmployeeVO> listEmployee(Long departmentId) {
        List<Long> employeeIds = employeeDepartmentService.listEmployeeId(departmentId);
        if (employeeIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<Employee> list = this.lambdaQuery().in(Employee::getId, employeeIds).list();
        return employeeConvert.toVo(list);
    }

    public Optional<Employee> getEmployee(String phone, String password) {
        return this.lambdaQuery().eq(Employee::getPhone, phone).eq(Employee::getPassword, password).oneOpt();
    }

    public IPage<EmployeeVO> page(EmployeeQueryDTO employeeDTO) {
        List<Long> employeeIdList = employeeDepartmentService.listEmployeeId(employeeDTO.getDepartmentId());
        if (employeeIdList.isEmpty() && Objects.nonNull(employeeDTO.getDepartmentId())) {
            return new Page<>();
        }

        Page<Employee> page = this.lambdaQuery()
                .and(StringUtils.isNotBlank(employeeDTO.getUserName()), employeeLambdaQueryWrapper -> employeeLambdaQueryWrapper
                        .eq(StringUtils.isNotBlank(employeeDTO.getUserName()), Employee::getUserName, employeeDTO.getUserName()).or()
                        .eq(StringUtils.isNotBlank(employeeDTO.getUserName()), Employee::getEmployeeName, employeeDTO.getUserName()))
                .in(!employeeIdList.isEmpty(), Employee::getId, employeeIdList)
                .eq(StringUtils.isNotBlank(employeeDTO.getPhone()), Employee::getPhone, employeeDTO.getPhone())
                .page(employeeDTO.page());
        Page<EmployeeVO> employeePageVO = employeeConvert.toVo(page);
        List<EmployeeVO> records = employeePageVO.getRecords();
        records.forEach(employeeVO -> {
            employeeVO.setRoleList(roleService.listEmployeeRole(employeeVO.getId()));
            EmployeeDepartment employeeDepartment = employeeDepartmentService.getEmployeeDepartment(employeeVO.getId());
            Optional.ofNullable(employeeDepartment)
                    .ifPresent(e -> employeeVO.setDepartmentNameChain(departmentService.getDepartmentChainName(e.getDepartmentId())));
        });
        return employeePageVO;
    }

    public EmployeeVO getEmployeeVoByUserId(Long userId) {
        Employee employee = this.lambdaQuery().eq(Employee::getUserId, userId).one();
        return employeeConvert.toVo(employee);
    }

    public Employee getEmployeeByUserId(Long userId) {
        return this.lambdaQuery().eq(Employee::getUserId, userId).one();
    }

    @CacheExpire(value = 3600 * 24)
    @Cacheable(value = GET_EMPLOYEE_USERID, key = "#userId")
    public String getEmployeeName(Long userId) {
        return this.lambdaQuery().select(Employee::getEmployeeName).eq(Employee::getUserId, userId).oneOpt().map(Employee::getEmployeeName).orElse("");
    }

    public List<EmployeeDO> listEmployeeByUserId(List<Long> userIdList) {
        if (userIdList.isEmpty()) {
            return Collections.emptyList();
        }
        List<Employee> list = this.lambdaQuery().in(Employee::getUserId, userIdList).list();
        return employeeConvert.toDo(list);
    }

    public List<Employee> getEmployeeByUserIds(List<Long> userIds) {
        return this.lambdaQuery().in(Employee::getUserId, userIds).list();
    }

    public EmployeeDO getEmployee(Long id) {
        return employeeConvert.toDo(this.lambdaQuery().eq(Employee::getId, id).one());
    }

    @Transactional(rollbackFor = Exception.class)
    public Employee save(EmployeeSaveDTO employeeDTO) {
        Optional<User> userOptional = userService.lambdaQuery().eq(User::getPhone, employeeDTO.getPhone()).oneOpt();
        //标识是员工注册
        User user = userOptional.orElseGet(() -> userService.register(employeeDTO.getPhone(), 0, 0));
        Optional.ofNullable(this.getEmployeeByUserId(user.getId())).ifPresent(employee -> {
            throw new BizException("该员工已经存在");
        });
        Employee employee = employeeConvert.toDo(employeeDTO);
        employee.setUserId(user.getId());
        this.save(employee);
        return employee;
    }

    public Boolean update(EmployeeUpdateDTO employeeDTO) {
        return this.updateById(employeeConvert.toDo(employeeDTO));
    }

    public Boolean delete(List<Long> idList) {
        return this.removeByIds(idList);
    }
}

