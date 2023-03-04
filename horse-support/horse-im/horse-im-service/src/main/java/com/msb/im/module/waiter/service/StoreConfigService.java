package com.msb.im.module.waiter.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.common.exception.BizException;
import com.msb.framework.common.model.PageDTO;
import com.msb.im.api.dto.UpdateStoreConfigDTO;
import com.msb.im.convert.StoreConfigConvert;
import com.msb.im.module.waiter.mapper.StoreConfigMapper;
import com.msb.im.module.waiter.model.dto.AddStoreConfigDTO;
import com.msb.im.module.waiter.model.dto.DeleteStoreConfigDTO;
import com.msb.im.module.waiter.model.dto.StoreConfigDTO;
import com.msb.im.module.waiter.model.entity.StoreConfig;
import com.msb.im.module.waiter.model.entity.StoreWaiter;
import com.msb.im.module.waiter.model.vo.StoreConfigVO;
import com.msb.im.service.ThirdSystemConfigService;
import com.msb.im.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;


/**
 * 店铺配置
 *
 * @author zhoumiao
 * @since 2022-04-13 16:28:56
 */
@Service
@Slf4j
public class StoreConfigService extends ServiceImpl<StoreConfigMapper, StoreConfig> {
    @Resource
    private StoreConfigConvert storeConfigConvert;
    @Resource
    private ThirdSystemConfigService thirdSystemConfigService;
    @Resource
    private StoreWaiterService storeWaiterService;

    public IPage<StoreConfigVO> page(PageDTO pageDTO, StoreConfigDTO storeConfigDTO) {
        String storeName = storeConfigDTO.getName();
        LambdaQueryWrapper<StoreConfig> queryWrapper = new LambdaQueryWrapper<>();
        StoreConfig storeConfig = storeConfigConvert.toDo(storeConfigDTO);
        storeConfig.setName(null);
        queryWrapper.setEntity(storeConfig);
        if (!StringUtils.isEmpty(storeName)) {
            queryWrapper.like(StoreConfig::getName, storeName);
        }
        return storeConfigConvert.toVo(this.page(pageDTO.page(), queryWrapper));
    }

    public StoreConfig findBySysId(Integer sysId) {
        return lambdaQuery().eq(StoreConfig::getSysId, sysId).one();
    }

    public boolean sysExist(Integer sysId) {
        return baseMapper.sysExist(sysId);
    }

    /**
     * 批量根据id逻辑删除
     *
     * @param deleteThirdSystemConfigDTO 参数
     * @return 删除个数
     */
    public int delete(DeleteStoreConfigDTO deleteThirdSystemConfigDTO) {
        List<StoreWaiter> storeWaiters = storeWaiterService.findByStoreIds(deleteThirdSystemConfigDTO.getIds());
        if (!storeWaiters.isEmpty()) {
            throw new BizException("店铺下有客服，不能删除");
        }
        int count = 0;
        for (Long id : deleteThirdSystemConfigDTO.getIds()) {
            boolean update = lambdaUpdate()
                    .eq(StoreConfig::getId, id)
                    .set(StoreConfig::getIsDeleted, true)
                    .update();
            if (update) {
                count++;
            }
        }
        return count;
    }

    public void add(AddStoreConfigDTO addStoreConfigDTO) {
        StoreConfig storeConfig = findBySysId(addStoreConfigDTO.getSysId());
        if (storeConfig != null) {
            throw new BizException("暂时只支持一个系统一个店铺");
        }
        storeConfig = storeConfigConvert.toDo(addStoreConfigDTO);
        storeConfig.setIsDeleted(false);
        String userId = UserUtil.getUserId();
        storeConfig.setCreateUser(userId);
        storeConfig.setUpdateUser(userId);
        LocalDateTime now = LocalDateTime.now();
        storeConfig.setCreateTime(now);
        storeConfig.setUpdateTime(now);
        save(storeConfig);
    }

    /**
     * 修改店铺的头像和昵称
     *
     * @param updateStoreConfigDTO 修改数据
     * @return 是否修改成功
     */
    public boolean updateStoreData(UpdateStoreConfigDTO updateStoreConfigDTO) {
        StoreConfig storeConfig = storeConfigConvert.toDo(updateStoreConfigDTO);
        storeConfig.setUpdateTime(LocalDateTime.now());
        storeConfig.setUpdateUser(UserUtil.getUserId());
        LambdaUpdateWrapper<StoreConfig> storeConfigLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        storeConfigLambdaUpdateWrapper.eq(StoreConfig::getSysId, updateStoreConfigDTO.getSystemId());
        return update(storeConfig, storeConfigLambdaUpdateWrapper);
    }

    public List<StoreConfig> findBySysIds(List<Integer> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }
        return lambdaQuery()
                .in(StoreConfig::getSysId, ids)
                .list();
    }
}
