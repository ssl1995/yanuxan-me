package com.msb.business.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.business.mapper.HorseUserDao;
import com.msb.business.model.entity.HorseUser;
import org.springframework.stereotype.Service;

/**
 * (HorseUser)表服务实现类
 *
 * @author makejava
 * @since 2022-03-16 11:12:35
 */
@Service("horseUserService")
public class HorseUserService extends ServiceImpl<HorseUserDao, HorseUser> {

}

