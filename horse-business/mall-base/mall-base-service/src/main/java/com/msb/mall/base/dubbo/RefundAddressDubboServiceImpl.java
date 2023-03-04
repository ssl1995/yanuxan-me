package com.msb.mall.base.dubbo;


import com.msb.mall.base.api.RefundAddressDubboService;
import com.msb.mall.base.api.model.RefundAddressDO;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class RefundAddressDubboServiceImpl implements RefundAddressDubboService {

    @Override
    public RefundAddressDO getRefundAddress() {
        RefundAddressDO refundAddressDO = new RefundAddressDO();
        refundAddressDO.setReceiveName("马士兵");
        refundAddressDO.setReceivePhone("13818182828");
        refundAddressDO.setProvinceCode("430000");
        refundAddressDO.setProvince("湖南省");
        refundAddressDO.setCityCode("430100");
        refundAddressDO.setCity("长沙市");
        refundAddressDO.setAreaCode("430104");
        refundAddressDO.setArea("岳麓区");
        refundAddressDO.setDetailAddress("长沙市岳麓区芯城科技园8栋");
        return refundAddressDO;
    }
}
