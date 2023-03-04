package com.msb.framework.feign;

import com.msb.framework.common.exception.BaseResultCodeEnum;
import com.msb.framework.common.exception.BizException;
import com.msb.framework.common.result.ResultWrapper;
import feign.FeignException;
import feign.Response;
import feign.codec.Decoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Feign调用结果解码器
 *
 * @author liao, R
 */
@Slf4j
public class FeignResultDecoder implements Decoder {
    private final SpringDecoder decoder;

    public FeignResultDecoder(SpringDecoder decoder) {
        this.decoder = decoder;
    }

    /**
     * @param response 响应
     * @param type     接口方法返回值类型
     * @return 解码后的结果
     */
    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        ParameterizedTypeImpl resultWrapperType = ParameterizedTypeImpl.make(ResultWrapper.class, new Type[]{type}, null);
        ResultWrapper<?> resultWrapper = (ResultWrapper<?>) this.decoder.decode(response, resultWrapperType);
        if (!BaseResultCodeEnum.SUCCESS.getCode().equals(resultWrapper.getCode())) {
            throw new BizException(resultWrapper.getCode(), resultWrapper.getMessage());
        }
        log.info("feign 响应结果 {}", resultWrapper);
        return resultWrapper.getData();
    }
}
