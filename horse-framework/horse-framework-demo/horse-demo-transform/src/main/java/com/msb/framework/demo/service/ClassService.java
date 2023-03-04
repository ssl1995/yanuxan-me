package com.msb.framework.demo.service;

import org.springframework.stereotype.Service;

/**
 * @author luozhan
 * @date 2022-08
 */
@Service
public class ClassService {

    public String getName(Long classId) {
        return "三年二班";
    }
}
