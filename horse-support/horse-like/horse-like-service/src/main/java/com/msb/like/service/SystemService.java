package com.msb.like.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.web.result.BizAssert;
import com.msb.like.mapper.SystemMapper;
import com.msb.like.model.entity.System;
import org.springframework.stereotype.Service;

/**
 * 系统表(System)表服务实现类
 *
 * @author shumengjiao
 * @since 2022-06-22 20:31:58
 */
@Service("systemService")
public class SystemService extends ServiceImpl<SystemMapper, System> {


    /**
     * 查询系统id
     * @param systemCode
     * @return
     */
    public Long getSystemId(String systemCode) {
        System one = this.lambdaQuery().eq(System::getSystemCode, systemCode).eq(System::getIsDeleted, false).one();
        BizAssert.notNull(one, "系统id查询有误");
        return one.getId();
    }
}

