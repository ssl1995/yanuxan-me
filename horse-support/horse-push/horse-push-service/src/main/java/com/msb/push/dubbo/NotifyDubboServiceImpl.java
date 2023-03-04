package com.msb.push.dubbo;

import com.msb.push.api.NotifyDubboService;
import com.msb.push.model.SendNotifyDTO;
import com.msb.third.api.WxMpDubboService;
import com.msb.third.model.dto.TemplateMessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;

@Slf4j
@DubboService
public class NotifyDubboServiceImpl implements NotifyDubboService {

    @DubboReference
    private WxMpDubboService wxMpDubboService;

    @Override
    public Boolean sendNotify(SendNotifyDTO sendNotifyDTO) {
        Boolean enableWxMpNotify = sendNotifyDTO.getEnableWxMpNotify();
        // 开启公众号通知
        if (enableWxMpNotify) {
            SendNotifyDTO.MpNotify mpNotifyParam = sendNotifyDTO.getMpNotifyParam();
            TemplateMessageDTO templateMessageDTO = new TemplateMessageDTO()
                    .setWxMpAppEnum(mpNotifyParam.getWxMpAppEnum())
                    .setWxMpAppMessageTemplateEnum(mpNotifyParam.getWxMpAppMessageTemplateEnum())
                    .setUserId(sendNotifyDTO.getUserId())
                    .setWebUrl(sendNotifyDTO.getWebUrl())
                    .setFirst(mpNotifyParam.getFirst())
                    .setRemark(mpNotifyParam.getRemark())
                    .setKeywords(mpNotifyParam.getKeywords())
                    .setKeywordMap(mpNotifyParam.getKeywordMap());
            log.info("发送微信公众号通知参数：{}", templateMessageDTO);
            wxMpDubboService.sendMpTemplateMessage(templateMessageDTO);
        }
        return false;
    }

}
