package com.msb.im.api;


import com.msb.im.api.dto.SendMessageDTO;
import com.msb.im.api.dto.SendMoreUserMessageDTO;
import com.msb.im.api.dto.UpdateSessionUserDTO;

/**
 * 发送消息dubbo
 *
 * @author zhou miao
 * @date 2022/04/22
 */
public interface MallImDubboService {
    /**
     * 发送消息
     *
     * @param sendMessageDTO
     * @return 是否发送成功
     */
    Boolean sendMessage(SendMessageDTO sendMessageDTO);

    /**
     * 批量发送消息
     *
     * @param sendMessageDTO
     * @return 是否发送成功
     */
    Boolean sendMessage(SendMoreUserMessageDTO sendMessageDTO);

    /**
     * 修改im中台用户信息
     *
     * @param updateSessionUserDTO
     * @return 是否发送成功
     */
    Boolean updateImCenterUserData(UpdateSessionUserDTO updateSessionUserDTO);

}
