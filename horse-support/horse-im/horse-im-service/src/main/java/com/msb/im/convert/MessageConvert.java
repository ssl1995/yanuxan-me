package com.msb.im.convert;

import com.msb.im.api.dto.SendMessageDTO;
import com.msb.im.api.dto.SendMoreUserMessageDTO;
import com.msb.im.model.bo.SendMessageBO;
import com.msb.im.model.entity.Message;
import com.msb.im.model.vo.MessageVO;
import org.mapstruct.Mapper;

/**
 *
 * @author zhou miao
 * @date 2022/04/21
 */
@Mapper(componentModel = "spring")
public interface MessageConvert {
    MessageVO toVo(Message message);
    SendMessageBO toBo(SendMessageDTO sendMessageDTO);
    SendMessageBO toBo(SendMoreUserMessageDTO sendMessage);
    SendMessageDTO toDto(SendMoreUserMessageDTO sendMessage);
}
