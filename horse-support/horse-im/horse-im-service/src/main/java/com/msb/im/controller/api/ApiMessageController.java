package com.msb.im.controller.api;

import com.google.common.collect.Lists;
import com.msb.im.api.dto.SendMessageDTO;
import com.msb.im.api.dto.SendMoreUserMessageDTO;
import com.msb.im.api.dto.UserDTO;
import com.msb.im.convert.MessageConvert;
import com.msb.im.model.bo.SendMessageBO;
import com.msb.im.netty.ChannelMessageTypeEnum;
import com.msb.im.service.MessageService;
import com.msb.user.auth.NoAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author zhou miao
 * @date 2022/06/02
 */
@Api(tags = "(api) 消息接口")
@RestController
@RequestMapping("/api/message")
@Slf4j
@NoAuth
public class ApiMessageController {
    private static final int PUSH_PARTITION_SIZE = 50;
    @Resource
    private MessageService messageService;
    @Resource
    private MessageConvert messageConvert;
    @Resource
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    @ApiOperation(value = "发送单人消息接口")
    @PostMapping("/test")
    public void test() {
    }

    @ApiOperation(value = "发送单人消息接口")
    @NoAuth
    @PostMapping("/sendMessage")
    public void sendMessage(@RequestBody  SendMessageDTO sendMessageDTO) {
        log.info("其他服务推送消息 {}", sendMessageDTO);
        SendMessageBO sendMessageBO = messageConvert.toBo(sendMessageDTO);
        sendMessageBO.setTraceId(null);
        sendMessageBO.setTraceType(ChannelMessageTypeEnum.PUSH_MESSAGE.getCode());
        messageService.sendSingleMessage(sendMessageBO);
    }

    @NoAuth
    @ApiOperation(value = "发送多人消息接口")
    @PostMapping("/sendMoreMessage")
    public void sendMoreMessage(@RequestBody SendMoreUserMessageDTO sendMoreUserMessageDTO) {
        log.info("其他服务推送消息 {}", sendMoreUserMessageDTO);
        List<UserDTO> toUsers = sendMoreUserMessageDTO.getToUsers();
        if (toUsers.isEmpty()) {
            return;
        }
        toUsers = toUsers.stream().filter(Objects::nonNull).collect(Collectors.toList());
        SendMessageDTO sendMessageDTO = messageConvert.toDto(sendMoreUserMessageDTO);
        List<List<UserDTO>> partitions = Lists.partition(toUsers, PUSH_PARTITION_SIZE);
        for (List<UserDTO> partition : partitions) {
            scheduledThreadPoolExecutor.submit(() -> {
                for (UserDTO user : partition) {
                    try {
                        sendMessageDTO.setToAvatar(user.getAvatar());
                        sendMessageDTO.setToNickname(user.getNickname());
                        sendMessageDTO.setToId(user.getId());
                        sendMessage(sendMessageDTO);
                        // 缓冲
                        TimeUnit.SECONDS.sleep(1);
                    } catch (Exception e) {
                        log.error("推送消息产生错误 {}", e.getMessage());
                    }
                }
            });
        }
    }

}
