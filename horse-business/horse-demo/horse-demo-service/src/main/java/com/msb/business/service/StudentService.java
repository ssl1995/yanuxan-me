package com.msb.business.service;

import com.msb.business.model.entity.Student;
import com.msb.business.model.entity.StudentVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author luozhan
 * @date 2022-08
 */
@Service
public class StudentService {
    @Resource
    private DictionaryService dictionaryService;

    public Student getById(Long id) {
        int male = 1;
        Student student = new Student();
        student.setId(1L);
        student.setName("张三");
        student.setClassId(300L);
        // 男
        student.setSex(male);
        // 班长
        student.setClassLeader(StudentVO.ClassLeaderEnum.MONITOR.getCode());
        return student;
    }
}
