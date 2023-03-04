package com.msb.framework.web.transform.unwrapper;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * mybatis-plus的IPage解包器
 *
 * @author R
 * @date 2023-3-1
 */
public class PageUnWrapper<T> implements UnWrapper<IPage<T>> {

    @Override
    public List<T> unWrap(IPage<T> source) {
        return source.getRecords();
    }
}

