package com.msb.mall.marketing.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.msb.framework.common.context.UserContext;
import com.msb.im.api.MallImDubboService;
import com.msb.im.api.dto.SendMoreUserMessageDTO;
import com.msb.im.api.dto.UserDTO;
import com.msb.im.api.enums.MallImSessionTypeEnum;
import com.msb.im.api.enums.MessageTypeEnum;
import com.msb.im.api.enums.SessionTypeEnum;
import com.msb.mall.marketing.model.dto.AppMessagePushDTO;
import com.msb.mall.marketing.model.dto.AppMessagePushDubboDTO;
import com.msb.user.api.UserDubboService;
import com.msb.user.api.vo.UserDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ImSendMessageService {

    @DubboReference(timeout = 10000)
    private UserDubboService userDubboService;

    @DubboReference(timeout = 10000)
    private MallImDubboService mallImDubboService;

    @Async
    public void sendMarketingMessage(AppMessagePushDTO appMessagePushDTO) {
        AppMessagePushDubboDTO appMessagePushDubboDTO = new AppMessagePushDubboDTO("app_push");
        appMessagePushDubboDTO.setContent(appMessagePushDTO.getContent()).setTitle(appMessagePushDTO.getTitle()).setLinkJump(appMessagePushDTO.getLinkJump());

        log.info("发送系统消息，{}", appMessagePushDTO);

        SendMoreUserMessageDTO sendMoreUserMessageDTO = createSendMoreUserMessageDTO(appMessagePushDubboDTO);

        List<Long> userIdList = userDubboService.listUserId(1L);
        log.info("准备需要发送的用户 {}", userIdList);
        List<UserDO> userDOS = userDubboService.listUserByIds(userIdList);
        List<List<UserDO>> partitions = Lists.partition(userDOS, 200);

        for (List<UserDO> partition : partitions) {
            createUserDTOS(sendMoreUserMessageDTO, partition);
            Boolean result = mallImDubboService.sendMessage(sendMoreUserMessageDTO);
            log.info("发送IM 消息结束 参数 {} 结果{}", sendMoreUserMessageDTO, result);
        }

    }

    private void createUserDTOS(SendMoreUserMessageDTO sendMoreUserMessageDTO, List<UserDO> partition) {
        List<UserDTO> userDTOS = new ArrayList<>(partition.size());
        for (UserDO userDO : partition) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(userDO.getId().toString());
            userDTO.setNickname(userDO.getNickname());
            userDTO.setAvatar(userDO.getAvatar());
            userDTOS.add(userDTO);
            sendMoreUserMessageDTO.setToUsers(userDTOS);
        }
    }

    private SendMoreUserMessageDTO createSendMoreUserMessageDTO(AppMessagePushDubboDTO appMessagePushDubboDTO) {
        SendMoreUserMessageDTO sendMoreUserMessageDTO = new SendMoreUserMessageDTO();
        sendMoreUserMessageDTO.setSysId(1);
        sendMoreUserMessageDTO.setType(MessageTypeEnum.CUSTOM);
        sendMoreUserMessageDTO.setPayload(JSONObject.toJSONString(appMessagePushDubboDTO));
        long sysUserId = UserContext.getSysUserId();
        sendMoreUserMessageDTO.setFromId(sysUserId + "");
        UserDO userDetailInfoById = userDubboService.getUserDetailInfoById(sysUserId);
        sendMoreUserMessageDTO.setFromAvatar(userDetailInfoById.getAvatar());
        sendMoreUserMessageDTO.setFromNickname(userDetailInfoById.getNickname());
        sendMoreUserMessageDTO.setSessionTypeEnum(SessionTypeEnum.CUSTOM);
        sendMoreUserMessageDTO.setSessionPayload(MallImSessionTypeEnum.SYSTEM_SESSION.getPayload());
        return sendMoreUserMessageDTO;
    }

}
