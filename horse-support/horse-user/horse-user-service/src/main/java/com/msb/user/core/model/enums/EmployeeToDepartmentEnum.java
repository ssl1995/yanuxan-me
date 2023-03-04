package com.msb.user.core.model.enums;

import com.msb.framework.common.model.IDict;

public enum EmployeeToDepartmentEnum implements IDict<Integer> {

    /**
     * 清除历史所有权限
     */
    CLEAR_HISTORY_ALL_ROLE(1, "clear_history_all_role"),

    /**
     * 清除之前部门下所有的权限
     */
    CLEAR_BEFORE_DEPARTMENT_ROLE(2, "clear_before_department_role"),

    /**
     * 不清除权限
     */
    NO_CLEAR(3, "no_clear"),
    ;

    EmployeeToDepartmentEnum(Integer code, String message) {
        init(code, message);
    }
}
