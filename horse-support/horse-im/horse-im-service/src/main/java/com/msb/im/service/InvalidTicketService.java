package com.msb.im.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.im.api.enums.TicketTypeEnum;
import com.msb.im.mapper.InvalidTicketMapper;
import com.msb.im.model.entity.InvalidTicket;
import com.msb.im.model.entity.ThirdSystemConfig;
import com.msb.im.api.util.TicketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 连接时已经使用过的ticket表实体类
 *
 * @author zhoumiao
 * @since 2022-04-25 16:24:17
 */
@Service("invalidTicketService")
@Slf4j
public class InvalidTicketService extends ServiceImpl<InvalidTicketMapper, InvalidTicket> {
    @Value("${spring.profiles.active:dev}")
    private String env;

    /**
     * 连接的ticket是否是错误的ticket
     *
     * @param thirdSystemConfig 系统标识
     * @param ticket            ticket
     * @param fromId            操作发起人
     * @param checkType         检查的类型
     * @return 连接的ticket是否是错误的ticket
     */
    public boolean checkIsUseAndUpdateTicket(ThirdSystemConfig thirdSystemConfig, String ticket, String fromId, TicketTypeEnum checkType) {
        boolean isUse = false;
        String srcTicket = TicketUtil.decrypt(ticket, thirdSystemConfig.getSecret());
        if (srcTicket == null) {
            isUse = true;
            log.warn("ticket 错误 {} {}", thirdSystemConfig, ticket);
        } else {
            Long lastTimestamp = TicketUtil.getLastTimestamp(srcTicket);
            Long dbLastTimestamp = baseMapper.findLastTimestamp(thirdSystemConfig.getId(), fromId, checkType.getCode());
            if (dbLastTimestamp != null && (lastTimestamp <= dbLastTimestamp)) {
                isUse = true;
            }
            if (!isUse && !Objects.equals(env, "dev")) {
                if (dbLastTimestamp == null) {
                    saveTicket(thirdSystemConfig, fromId, checkType, lastTimestamp);
                } else {
                    updateTicket(thirdSystemConfig, fromId, checkType, lastTimestamp);
                }
            }
        }
        return isUse;
    }

    private void saveTicket(ThirdSystemConfig thirdSystemConfig, String fromId, TicketTypeEnum checkType, Long lastTimestamp) {
        save(createInvalidTicket(thirdSystemConfig.getId(), fromId, lastTimestamp, checkType));
    }

    private void updateTicket(ThirdSystemConfig thirdSystemConfig, String fromId, TicketTypeEnum checkType, Long lastTimestamp) {
        lambdaUpdate()
                .eq(InvalidTicket::getSysId, thirdSystemConfig.getId())
                .eq(InvalidTicket::getFromId, fromId)
                .eq(InvalidTicket::getType, checkType.getCode())
                .set(InvalidTicket::getLastTimestamp, lastTimestamp)
                .update();
    }

    private InvalidTicket createInvalidTicket(Integer sysId, String fromId, Long lastTimestamp, TicketTypeEnum ticketTypeEnum) {
        InvalidTicket invalidTicket = new InvalidTicket();
        invalidTicket.setSysId(sysId);
        invalidTicket.setFromId(fromId);
        invalidTicket.setLastTimestamp(lastTimestamp);
        invalidTicket.setType(ticketTypeEnum.getCode());
        return invalidTicket;
    }
}

