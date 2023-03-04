package com.msb.framework.web.transform.unwrapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.msb.framework.common.result.ResultWrapper;

/**
 * ResultWrapper的解包器
 *
 * @author R
 * @date 2023-3-1
 */
public class ResultUnWrapper implements UnWrapper<ResultWrapper<?>> {
    @Override
    public Object unWrap(ResultWrapper<?> source) {
        Object data = source.getData();
        if (data instanceof IPage) {
            return ((IPage<?>) data).getRecords();
        }
        // List或bean直接返回
        return data;
    }
}