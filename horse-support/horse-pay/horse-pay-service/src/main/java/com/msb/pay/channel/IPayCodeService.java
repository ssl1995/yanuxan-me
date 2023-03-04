package com.msb.pay.channel;

import com.msb.pay.enums.PayCodeEnum;
import com.msb.pay.model.bo.UnifiedOrderRES;
import com.msb.pay.model.dto.UnifiedOrderDTO;
import com.msb.pay.model.entity.AppInfo;
import com.msb.pay.model.entity.MchInfo;
import com.msb.pay.model.paydata.PayData;

/**
 * 支付方式对接接口
 * D：支付数据类型
 *
 * @author peng.xy
 * @date 2022/6/1
 */
public interface IPayCodeService<D extends PayData> {

    /**
     * 发起支付请求
     *
     * @param mchInfo：商户信息
     * @param appInfo：应用信息
     * @param unifiedOrderDTO：下单参数
     * @return com.msb.pay.model.bo.UnifiedOrderBO
     * @author peng.xy
     * @date 2022/6/7
     */
    UnifiedOrderRES<D> payRequest(MchInfo mchInfo, AppInfo appInfo, UnifiedOrderDTO unifiedOrderDTO);

    /**
     * 获取支付代号
     *
     * @return com.msb.pay.enums.PayCodeEnum
     * @author peng.xy
     * @date 2022/7/13
     */
    PayCodeEnum payCode();

}
