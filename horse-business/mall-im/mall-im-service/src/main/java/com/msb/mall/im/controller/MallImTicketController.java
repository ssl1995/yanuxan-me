package com.msb.mall.im.controller;

import com.msb.framework.common.context.UserContext;
import com.msb.framework.common.exception.BizException;
import com.msb.im.api.enums.TicketTypeEnum;
import com.msb.im.api.util.TicketUtil;
import com.msb.mall.im.conifg.MallImConfig;
import com.msb.mall.im.model.vo.TicketVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * 获取登录凭证的
 *
 * @author zhou miao
 * @date 2022/05/25
 */
@RestController
@Api(tags = "（用户端）获取凭证的接口")
@RequestMapping("/ticket")
public class MallImTicketController {
    @Resource
    private MallImConfig mallImConfig;

    /**
     * 针对ticket要做权限校验
     *
     * @param ticketTypeEnum
     * @return
     */
    @ApiOperation("获取ticket")
    @GetMapping
    public TicketVO ticket(
            @ApiParam(value = "CONNECT_TICKET-连接的ticket、CREATE_SINGLE_SESSION_TICKET-创建单人会话ticket、CREATE_GROUP_SESSION_TICKET-创建群会话的ticket")
            @NotNull(message = "ticket类型不能为空")
                           @RequestParam("ticketType") TicketTypeEnum ticketTypeEnum) {
        if (Objects.equals(ticketTypeEnum, TicketTypeEnum.API_CURL_TICKET)) {
            throw new BizException("不允许生成api调用ticket");
        }
        Long userId = UserContext.getUserId();
        long currentTimeMillis = System.currentTimeMillis();
        String ticket = TicketUtil.ticket(mallImConfig.getClient(), userId.toString(), ticketTypeEnum, currentTimeMillis, mallImConfig.getSecret());
        return TicketVO.builder().ticket(ticket).client(mallImConfig.getClient()).build();
    }

}
