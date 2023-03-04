package com.msb.business.service;

import org.springframework.stereotype.Service;

/**
 * @author luozhan
 * @date 2022-08
 */
@Service
public class DictionaryService {

    public String getText(String type, String code) {
        if ("sex".equals(type)) {
            return "1".equals(code) ? "男" : "女";
        }
        return null;
    }
}
