package com.msb.mall.im.dubbo;

import com.msb.im.api.MallImDubboService;
import com.msb.im.api.MessageApi;
import com.msb.im.api.UserApi;
import com.msb.im.api.dto.SendMessageDTO;
import com.msb.im.api.dto.SendMoreUserMessageDTO;
import com.msb.im.api.dto.UpdateSessionUserDTO;
import com.msb.im.api.enums.TicketTypeEnum;
import com.msb.im.api.result.ImApiResultEnum;
import com.msb.im.api.util.TicketUtil;
import com.msb.im.api.vo.ResultVO;
import com.msb.mall.im.conifg.MallImConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Slf4j
@DubboService
@Service
public class MallMallImDubboServiceImpl implements MallImDubboService {
    @Resource
    private MessageApi messageApi;
    @Resource
    private MallImConfig mallImConfig;
    @Resource
    private UserApi userApi;

    @Override
    public Boolean sendMessage(SendMessageDTO sendMessageDTO) {
        log.info("发送单人消息参数 {}", sendMessageDTO);
        ResultVO resultVO = messageApi.sendMessage(sendMessageDTO, TicketUtil.ticket(mallImConfig.getClient(), sendMessageDTO.getFromId(), TicketTypeEnum.API_CURL_TICKET, System.currentTimeMillis(), mallImConfig.getSecret()), sendMessageDTO.getFromId());
        log.info("发送单人消息响应 {}", resultVO);
        return Objects.equals(ImApiResultEnum.API_CURL_SUCCESS.getCode(), resultVO.getCode());
    }

    @Override
    public Boolean sendMessage(SendMoreUserMessageDTO sendMessageDTO) {
        log.info("发送多人消息参数 {}", sendMessageDTO);
        ResultVO resultVO = messageApi.sendMoreMessage(sendMessageDTO, TicketUtil.ticket(mallImConfig.getClient(), sendMessageDTO.getFromId(), TicketTypeEnum.API_CURL_TICKET, System.currentTimeMillis(), mallImConfig.getSecret()), sendMessageDTO.getFromId());
        log.info("发送多人消息响应 {}", resultVO);
        return Objects.equals(ImApiResultEnum.API_CURL_SUCCESS.getCode(), resultVO.getCode());
    }

    @Override
    public Boolean updateImCenterUserData(UpdateSessionUserDTO updateSessionUserDTO) {
        log.info("修改im中台用户信息参数 {}", updateSessionUserDTO);
        ResultVO resultVO = userApi.updateUserData(updateSessionUserDTO, TicketUtil.ticket(mallImConfig.getClient(), updateSessionUserDTO.getUserId(), TicketTypeEnum.API_CURL_TICKET, System.currentTimeMillis(), mallImConfig.getSecret()), updateSessionUserDTO.getUserId());
        log.info("修改im中台用户信息响应 {}", resultVO);
        return Objects.equals(ImApiResultEnum.API_CURL_SUCCESS.getCode(), resultVO.getCode());
    }
}
