package com.msb.mall.marketing.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.mall.marketing.model.dto.AppMessagePushDTO;
import com.msb.mall.marketing.model.dto.AppMessagePushQueryDTO;
import com.msb.mall.marketing.model.entity.AppMessagePush;
import com.msb.mall.marketing.model.vo.AppMessagePushVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * app消息推送(AppMessagePush)表服务接口
 *
 * @author makejava
 * @date 2022-04-06 14:11:49
 */
@Mapper(componentModel = "spring")
public interface AppMessagePushConvert {

    AppMessagePushVO toVo(AppMessagePush appMessagePush);

    List<AppMessagePushVO> toVo(List<AppMessagePush> appMessagePush);

    Page<AppMessagePushVO> toVo(Page<AppMessagePush> appMessagePush);

    AppMessagePush toDo(AppMessagePushDTO appMessagePushDTO);
}

