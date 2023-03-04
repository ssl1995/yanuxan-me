package com.msb.im.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.common.exception.BizException;
import com.msb.framework.common.model.PageDTO;
import com.msb.im.api.enums.TicketTypeEnum;
import com.msb.im.convert.ThirdSystemConfigConvert;
import com.msb.im.mapper.ThirdSystemConfigMapper;
import com.msb.im.model.dto.AddThirdSystemConfigDTO;
import com.msb.im.model.dto.DeleteThirdSystemConfigDTO;
import com.msb.im.model.dto.ThirdSystemConfigDTO;
import com.msb.im.model.entity.ThirdSystemConfig;
import com.msb.im.model.vo.ThirdSystemConfigVO;
import com.msb.im.module.waiter.model.entity.StoreConfig;
import com.msb.im.module.waiter.service.StoreConfigService;
import com.msb.im.redis.SystemRedisService;
import com.msb.im.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * (HorseImThirdSys)表服务实现类
 *
 * @author zhoumiao
 * @since 2022-04-25 16:24:17
 */
@Service("thirdSystemConfigService")
@Slf4j
public class ThirdSystemConfigService extends ServiceImpl<ThirdSystemConfigMapper, ThirdSystemConfig> {

    @Value("${spring.profiles.active:dev}")
    private String env;
    @Resource
    private ThirdSystemConfigConvert thirdSystemConfigConvert;
    @Resource
    private SystemRedisService systemRedisService;
    @Resource
    private InvalidTicketService invalidTicketService;
    @Resource
    private StoreConfigService storeConfigService;

    public IPage<ThirdSystemConfigVO> page(PageDTO pageDTO, ThirdSystemConfigDTO thirdSystemConfigDTO) {
        String name = thirdSystemConfigDTO.getName();
        LambdaQueryWrapper<ThirdSystemConfig> queryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like(ThirdSystemConfig::getName, name);
        }
        return thirdSystemConfigConvert.toVo(this.page(pageDTO.page(), queryWrapper));
    }

    public ThirdSystemConfigVO getOne(Serializable id) {
        return thirdSystemConfigConvert.toVo(this.getById(id));
    }

    public Boolean save(ThirdSystemConfigDTO thirdSystemConfigDTO) {
        return this.save(thirdSystemConfigConvert.toDo(thirdSystemConfigDTO));
    }

    public Boolean update(ThirdSystemConfigDTO thirdSystemConfigDTO) {
        return this.updateById(thirdSystemConfigConvert.toDo(thirdSystemConfigDTO));
    }

    public Boolean delete(List<Long> idList) {
        return this.removeByIds(idList);
    }

    /**
     * 查询系统配置 优先查询缓存
     *
     * @param sysId 系统id
     * @return 系统配置
     */
    public ThirdSystemConfig findBySysId(int sysId) {

        ThirdSystemConfig thirdSystemConfig = systemRedisService.getThirdSystem(sysId);
        if (thirdSystemConfig == null) {
            //  从数据库查询
            thirdSystemConfig = findDbBySysIdAndUpdateRedis(sysId);
        }
        return thirdSystemConfig;
    }

    /**
     * 查询系统配置 优先查询缓存
     *
     * @param client 系统标识
     * @return 系统配置
     */
    public ThirdSystemConfig findByClient(String client) {

        ThirdSystemConfig thirdSystemConfig = systemRedisService.getThirdSystem(client);
        if (thirdSystemConfig == null) {
            //  从数据库查询
            thirdSystemConfig = findDbByClientAndUpdateRedis(client);
        }
        return thirdSystemConfig;
    }

    /**
     * 通过系统id查询 更新缓存
     *
     * @param client 系统标识
     * @return 系统配置
     */
    public ThirdSystemConfig findDbByClientAndUpdateRedis(String client) {
        LambdaQueryWrapper<ThirdSystemConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ThirdSystemConfig::getClient, client);
        queryWrapper.eq(ThirdSystemConfig::getIsDeleted, Boolean.FALSE);
        ThirdSystemConfig one = getOne(queryWrapper);
        systemRedisService.addThirdSystem(one);
        return one;
    }

    /**
     * 通过系统id查询 更新缓存
     *
     * @param sysId 系统id
     * @return 系统配置
     */
    public ThirdSystemConfig findDbBySysIdAndUpdateRedis(int sysId) {
        LambdaQueryWrapper<ThirdSystemConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ThirdSystemConfig::getId, sysId);
        queryWrapper.eq(ThirdSystemConfig::getIsDeleted, Boolean.FALSE);
        ThirdSystemConfig one = getOne(queryWrapper);
        systemRedisService.addThirdSystem(one);
        return one;
    }

    public void add(AddThirdSystemConfigDTO addThirdSystemConfigDTO) {
        ThirdSystemConfig thirdSystemConfig = thirdSystemConfigConvert.toDo(addThirdSystemConfigDTO);
        String userId = UserUtil.getUserId();
        thirdSystemConfig.setCreateUser(userId);
        thirdSystemConfig.setUpdateUser(userId);
        LocalDateTime now = LocalDateTime.now();
        thirdSystemConfig.setCreateTime(now);
        thirdSystemConfig.setUpdateTime(now);
        thirdSystemConfig.setSecret(RandomStringUtils.randomAlphanumeric(16));
        thirdSystemConfig.setIsDeleted(false);
        save(thirdSystemConfig);
    }

    /**
     * 批量根据id逻辑删除
     *
     * @param deleteThirdSystemConfigDTO 删除参数
     * @return 删除个数
     */
    public int delete(DeleteThirdSystemConfigDTO deleteThirdSystemConfigDTO) {
        List<StoreConfig> storeConfigs = storeConfigService.findBySysIds(deleteThirdSystemConfigDTO.getIds());
        if (!storeConfigs.isEmpty()) {
            throw new BizException("系统下有店铺，不能删除");
        }
        int count = 0;
        List<Integer> ids = deleteThirdSystemConfigDTO.getIds();
        for (Integer id : ids) {
            boolean update = lambdaUpdate()
                    .eq(ThirdSystemConfig::getId, id)
                    .set(ThirdSystemConfig::getIsDeleted, true)
                    .update();
            if (update) {
                count++;
            }
        }
        return count;
    }

    /**
     * 根据接入系统标识检查ticket ticket未使用返回系统 使用了返回null
     *
     * @param sysId          接入系统id
     * @param ticket         ticket
     * @param fromId         操作发起人
     * @param ticketTypeEnum ticket类型
     * @return 系统
     */
    public ThirdSystemConfig checkTicketBySysIdAndReturnEntity(Integer sysId, String ticket, String fromId, TicketTypeEnum ticketTypeEnum) {
        ThirdSystemConfig thirdSystemConfig = findBySysId(sysId);
        if (thirdSystemConfig == null) {
            log.error("系统不存在 {}", sysId);
            return null;
        }
        boolean isUse = invalidTicketService.checkIsUseAndUpdateTicket(thirdSystemConfig, ticket, fromId, ticketTypeEnum);
        if (isUse) {
            return null;
        } else {
            return thirdSystemConfig;
        }
    }
}

