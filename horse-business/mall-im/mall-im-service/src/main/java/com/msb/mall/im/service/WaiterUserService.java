package com.msb.mall.im.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.common.context.UserContext;
import com.msb.framework.common.exception.BaseResultCodeEnum;
import com.msb.framework.common.exception.BizException;
import com.msb.framework.common.model.PageDTO;
import com.msb.framework.web.result.Assert;
import com.msb.im.api.StoreWaiterApi;
import com.msb.im.api.dto.DeleteStoreWaiterDTO;
import com.msb.im.api.dto.UpdateStoreWaiterDTO;
import com.msb.im.api.enums.TicketTypeEnum;
import com.msb.im.api.result.ImApiResultEnum;
import com.msb.im.api.util.TicketUtil;
import com.msb.im.api.vo.ResultVO;
import com.msb.mall.im.conifg.MallImConfig;
import com.msb.mall.im.mapper.WaiterUserMapper;
import com.msb.mall.im.model.dto.AddWaiterUserDTO;
import com.msb.mall.im.model.dto.DeleteWaiterUserDTO;
import com.msb.mall.im.model.dto.UpdateWaiterUserDTO;
import com.msb.mall.im.model.dto.WaiterUserDTO;
import com.msb.mall.im.model.entity.WaiterUser;
import com.msb.mall.im.model.vo.WaiterUserVO;
import com.msb.mall.im.service.convert.WaiterUserConvert;
import com.msb.user.api.EmployeeDubboService;
import com.msb.user.api.UserDubboService;
import com.msb.user.api.vo.EmployeeDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 客服用户关联
 *
 * @author zhou miao
 * @date 2022/06/11
 */
@Service
@Slf4j
public class WaiterUserService extends ServiceImpl<WaiterUserMapper, WaiterUser> {
    @Resource
    private WaiterUserConvert waiterUserConvert;
    @Resource
    private StoreWaiterApi storeWaiterApi;
    @Resource
    private MallImConfig mallImConfig;
    @DubboReference
    private UserDubboService userDubboService;
    @DubboReference
    private EmployeeDubboService employeeDubboService;

    @Transactional(rollbackFor = Exception.class)
    public WaiterUser add(AddWaiterUserDTO addWaiterUserDTO) {
        WaiterUser waiterUser = waiterUserConvert.toDo(addWaiterUserDTO);
        Long userId = setWaiterBasic(waiterUser);
        checkUserSystem(userId);
        save(waiterUser);
        return addImWaiter(waiterUser, userId);
    }

    private void checkUserSystem(Long userId) {
        if (!userDubboService.checkUserSystem(userId, mallImConfig.getSystemId())) {
            throw new BizException("用户不属于严选系统");
        }
    }

    private WaiterUser addImWaiter(WaiterUser waiterUser, Long userId) {
        String ticket = TicketUtil.ticket(mallImConfig.getClient(), userId.toString(), TicketTypeEnum.API_CURL_TICKET, System.currentTimeMillis(), mallImConfig.getSecret());
        ResultVO resultVO = storeWaiterApi.add(waiterUserConvert.toAddDTO(waiterUser), ticket, userId.toString());
        if (Objects.equals(ImApiResultEnum.API_CURL_SUCCESS.getCode(), resultVO.getCode())) {
            return waiterUser;
        } else {
            throw new BizException("新增失败");
        }
    }

    private Long setWaiterBasic(WaiterUser waiterUser) {
        waiterUser.setWaiterId(UUID.randomUUID().toString().replace("-", ""));
        Long userId = UserContext.getUserId();
        waiterUser.setCreateUser(userId);
        waiterUser.setUpdateUser(userId);
        LocalDateTime now = LocalDateTime.now();
        waiterUser.setCreateTime(now);
        waiterUser.setUpdateTime(now);
        return userId;
    }

    public IPage<WaiterUserVO> page(PageDTO pageDTO, WaiterUserDTO waiterUserDTO) {
        String waiterNickname = waiterUserDTO.getWaiterNickname();
        LambdaQueryWrapper<WaiterUser> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(waiterNickname)) {
            queryWrapper.like(WaiterUser::getWaiterNickname, waiterNickname);
        }
        return waiterUserConvert.toVo(this.page(pageDTO.page(), queryWrapper));
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(DeleteWaiterUserDTO deleteWaiterUserDTO) {
        List<Long> ids = deleteWaiterUserDTO.getIds();
        List<WaiterUser> waiterUsers = listByIds(ids);
        if (removeByIds(ids)) {
            deleteImWaiter(waiterUsers);
        }
    }

    private void deleteImWaiter(List<WaiterUser> waiterUsers) {
        List<String> waiterIds = waiterUsers.stream().map(WaiterUser::getWaiterId).collect(Collectors.toList());
        DeleteStoreWaiterDTO deleteStoreWaiterDTO = new DeleteStoreWaiterDTO();
        deleteStoreWaiterDTO.setWaiterIds(waiterIds);
        String userId = UserContext.getUserId().toString();
        String ticket = TicketUtil.ticket(mallImConfig.getClient(), userId, TicketTypeEnum.API_CURL_TICKET, System.currentTimeMillis(), mallImConfig.getSecret());
        ResultVO resultVO = storeWaiterApi.delete(deleteStoreWaiterDTO, ticket, userId);
        if (!Objects.equals(ImApiResultEnum.API_CURL_SUCCESS.getCode(), resultVO.getCode())) {
            throw new BizException("删除失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(UpdateWaiterUserDTO updateWaiterUserDTO) {
        WaiterUser dbWaiterUser = getById(updateWaiterUserDTO.getId());
        Assert.notNull(dbWaiterUser, BaseResultCodeEnum.RESOURCE_NOT_FOUND);
        WaiterUser waiterUser = waiterUserConvert.toDo(updateWaiterUserDTO);
        Long userId = UserContext.getUserId();
        checkUserSystem(userId);
        waiterUser.setUpdateTime(LocalDateTime.now());
        waiterUser.setUpdateUser(userId);
        boolean update = updateById(waiterUser);
        if (update) {
            updateImWaiter(dbWaiterUser, userId);
        }

    }

    private void updateImWaiter(WaiterUser dbWaiterUser, Long userId) {
        String ticket = TicketUtil.ticket(mallImConfig.getClient(), userId.toString(), TicketTypeEnum.API_CURL_TICKET, System.currentTimeMillis(), mallImConfig.getSecret());
        UpdateStoreWaiterDTO updateStoreWaiterDTO = waiterUserConvert.toUpdateDTO(dbWaiterUser);
        ResultVO resultVO = storeWaiterApi.update(updateStoreWaiterDTO, ticket, userId.toString());
        if (!Objects.equals(ImApiResultEnum.API_CURL_SUCCESS.getCode(), resultVO.getCode())) {
            throw new BizException("更新失败");
        }
    }

    public WaiterUserVO getWaiterByUserId(Long userId) {
        EmployeeDO employeeDO = employeeDubboService.getEmployeeByUserId(userId);
        if (employeeDO == null) {
            throw new BizException("用户不是员工");
        }

        WaiterUser waiterUser = lambdaQuery()
                .eq(WaiterUser::getUserId, employeeDO.getId())
                .one();
        if (waiterUser == null) {
            throw new BizException("用户不是客服");
        }
        log.info("用户客服信息 {}", waiterUser);
        return waiterUserConvert.toVo(waiterUser);
    }
}
