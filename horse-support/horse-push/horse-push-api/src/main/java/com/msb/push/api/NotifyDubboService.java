package com.msb.push.api;

import com.msb.push.model.SendNotifyDTO;

public interface NotifyDubboService {

    /**
     * 发送通知公共接口
     *
     * @param sendNotifyDTO：通知参数
     * @return boolean
     * @author peng.xy
     * @date 2022/5/21
     */
    Boolean sendNotify(SendNotifyDTO sendNotifyDTO);

}
