package com.msb.business.service.convert;

import com.msb.business.model.entity.Student;
import com.msb.business.model.entity.StudentVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentConvert {

    StudentVO toVo(Student student);


}
