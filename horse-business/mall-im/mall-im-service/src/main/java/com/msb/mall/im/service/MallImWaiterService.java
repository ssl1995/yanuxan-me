package com.msb.mall.im.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.framework.common.context.UserContext;
import com.msb.framework.common.exception.BizException;
import com.msb.im.api.StoreWaiterApi;
import com.msb.im.api.dto.StoreWaiterDTO;
import com.msb.im.api.dto.TransferWaiterDTO;
import com.msb.im.api.enums.TicketTypeEnum;
import com.msb.im.api.result.ImApiResultEnum;
import com.msb.im.api.util.TicketUtil;
import com.msb.im.api.vo.ResultVO;
import com.msb.im.api.vo.StoreWaiterVO;
import com.msb.mall.im.conifg.MallImConfig;
import com.msb.mall.im.model.vo.WaiterUserVO;
import com.msb.user.api.EmployeeDubboService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author zhou miao
 * @date 2022/06/07
 */
@Service
@Slf4j
public class MallImWaiterService {
    @Resource
    private StoreWaiterApi storeWaiterApi;
    @Resource
    private MallImConfig mallImConfig;
    @Resource
    private WaiterUserService waiterUserService;
    @Resource
    private EmployeeDubboService employeeDubboService;

    public IPage<StoreWaiterVO> page(StoreWaiterDTO storeWaiterDTO) {
        log.info("查询所有客服请求 {}", storeWaiterDTO);
        String userId = UserContext.getUserId().toString();
        String ticket = TicketUtil.ticket(mallImConfig.getClient(), userId, TicketTypeEnum.API_CURL_TICKET, System.currentTimeMillis(), mallImConfig.getSecret());
        ResultVO resultVO = storeWaiterApi.page(storeWaiterDTO, ticket, userId);
        log.info("查询所有客服响应 {}", resultVO);
        if (Objects.equals(ImApiResultEnum.API_CURL_SUCCESS.getCode(), resultVO.getCode())) {
            return JSON.parseObject(JSON.toJSONString(resultVO.getData()), Page.class);
        } else {
            return new Page<>();
        }
    }

    public void transfer(TransferWaiterDTO transferWaiterDTO) {
        Long userId = UserContext.getUserId();
        WaiterUserVO waiterUserVO = waiterUserService.getWaiterByUserId(userId);
        String waiterId = waiterUserVO.getWaiterId();
        transferWaiterDTO.setCurrentWaiterId(waiterId);
        log.info("转移客服会话请求 {}", transferWaiterDTO);
        String ticket = TicketUtil.ticket(mallImConfig.getClient(), userId.toString(), TicketTypeEnum.API_CURL_TICKET, System.currentTimeMillis(), mallImConfig.getSecret());
        ResultVO resultVO = storeWaiterApi.transfer(transferWaiterDTO, ticket, userId.toString());
        log.info("转移客服会话响应 {}", resultVO);
        if (!Objects.equals(ImApiResultEnum.API_CURL_SUCCESS.getCode(), resultVO.getCode())) {
            throw new BizException("转移失败");
        }
    }
}
