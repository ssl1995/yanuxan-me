package com.msb.user.core.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.user.core.model.entity.Department;
import com.msb.user.core.model.vo.DepartmentTreeVO;
import com.msb.user.core.model.vo.DepartmentVO;
import com.msb.user.core.model.dto.DepartmentDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 部门表(Department)表服务接口
 *
 * @author makejava
 * @date 2022-05-09 10:18:10
 */
@Mapper(componentModel = "spring")
public interface DepartmentConvert {

    DepartmentVO toVo(Department department);

    DepartmentTreeVO toTreeVo(Department department);

    List<DepartmentTreeVO> toTreeVo(List<Department> department);

    List<DepartmentVO> toVo(List<Department> department);

    Page<DepartmentVO> toVo(Page<Department> department);

    Department toDo(DepartmentDTO departmentDTO);
}

