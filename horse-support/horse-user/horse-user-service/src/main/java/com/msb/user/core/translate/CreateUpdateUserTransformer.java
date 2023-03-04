package com.msb.user.core.translate;

import com.msb.framework.web.transform.transformer.SimpleTransformer;
import com.msb.user.core.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import java.util.Optional;

@Slf4j
@Component
public class CreateUpdateUserTransformer implements SimpleTransformer<Long> {

    @Resource
    private EmployeeService employeeService;

    @Override
    public String transform(@Nonnull Long original) {
        String employeeName = employeeService.getEmployeeName(original);
        return Optional.ofNullable(employeeName).orElse("");
    }
}
