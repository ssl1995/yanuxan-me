package com.msb.framework.web.transform;

import com.msb.framework.web.transform.transformer.EnumTransformer;
import com.msb.framework.web.transform.unwrapper.PageUnWrapper;
import com.msb.framework.web.transform.unwrapper.ResultUnWrapper;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 转换器配置类
 * 约定自定义转换器都放在api包的transformer目录下，方便给其他模块使用
 *
 * @author R
 */
@Configuration
@ComponentScan("com.**.transformer")
@Import({EnumTransformer.class, PageUnWrapper.class, ResultUnWrapper.class})
public class TransformConfig {


}
