package com.msb.business.controller;

import com.msb.business.model.entity.Student;
import com.msb.business.model.entity.StudentVO;
import com.msb.business.service.ClassService;
import com.msb.business.service.DictionaryService;
import com.msb.business.service.StudentService;
import com.msb.business.service.convert.StudentConvert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping
@Slf4j

public class StudentController {
    @Resource
    private StudentConvert studentConvert;
    @Resource
    private DictionaryService dictionaryService;
    @Resource
    private ClassService classService;
    @Resource
    private StudentService studentService;


    @GetMapping("student/{id}")
    public StudentVO getStudent(@PathVariable Long id) {
        Student student = studentService.getById(id);
        StudentVO studentVO = studentConvert.toVo(student);
        // 翻译班级名
        studentVO.setClassName(classService.getName(studentVO.getClassId()));
        // 翻译性别
        Integer sex = studentVO.getSex();
        studentVO.setSexName(dictionaryService.getText("sex", String.valueOf(sex)));
        return studentVO;
    }

}
