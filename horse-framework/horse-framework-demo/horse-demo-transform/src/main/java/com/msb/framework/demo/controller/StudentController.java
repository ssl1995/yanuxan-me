package com.msb.framework.demo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.framework.demo.entity.StudentVO;
import com.msb.framework.demo.service.ClassService;
import com.msb.framework.demo.service.DictionaryService;
import com.msb.framework.demo.service.StudentService;
import com.msb.framework.demo.service.convert.StudentConvert;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping
@Slf4j
@Api
@Validated
public class StudentController {
    @Resource
    private StudentConvert studentConvert;
    @Resource
    private DictionaryService dictionaryService;
    @Resource
    private ClassService classService;
    @Resource
    private StudentService studentService;


    @PostMapping("student")
    public IPage<StudentVO> getStudent(@Valid @RequestBody StudentVO v) {
        StudentVO currentStudent = studentConvert.toVo(studentService.getById(1L));
        StudentVO student2 = studentConvert.toVo(studentService.getById(2L));
        StudentVO student3 = studentConvert.toVo(studentService.getById(3L));
        List<StudentVO> list = Arrays.asList(student2, student3);
//        currentStudent.setDeskmate(student2);
//        currentStudent.setTeam(list);
//        // 转换逻辑
//        // 1、翻译班级名
//        String className = classService.getName(currentStudent.getClassId());
//        currentStudent.setClassName(className);
//        // 2、翻译性别
//        Integer sex = currentStudent.getSex();
//        String sexText = dictionaryService.getText("sex", sex);
//        currentStudent.setSexName(sexText);
//        ///3、翻译班干部职位
//        Integer classLeaderCode = currentStudent.getClassLeader();
//        String classLeader = IDict.getTextByCode(StudentVO.ClassLeaderEnum.class, classLeaderCode);
//        currentStudent.setClassLeaderName(classLeader);

        Page<StudentVO> objectPage = new Page<StudentVO>().setRecords(Lists.list(currentStudent));
        return objectPage;
    }


}