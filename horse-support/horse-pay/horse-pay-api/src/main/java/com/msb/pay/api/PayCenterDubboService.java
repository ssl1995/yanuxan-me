package com.msb.pay.api;

import com.msb.pay.model.dto.*;
import com.msb.pay.model.paydata.PayData;
import com.msb.pay.model.vo.ApplyRefundVO;
import com.msb.pay.model.vo.PrepaymentVO;
import com.msb.pay.model.vo.UnifiedOrderVO;

/**
 * 支付中台Dubbo接口
 *
 * @author peng.xy
 * @date 2022/6/8
 */
public interface PayCenterDubboService {

    /**
     * 统一下单
     *
     * @param unifiedOrderDubboDTO：下单参数
     * @return com.msb.pay.model.vo.UnifiedOrderVO<? extends com.msb.pay.model.paydata.PayData>
     * @author peng.xy
     * @date 2022/6/8
     */
    UnifiedOrderVO<? extends PayData> unifiedOrder(UnifiedOrderDubboDTO unifiedOrderDubboDTO);

    /**
     * 支付成功回调确认
     *
     * @param payNotifyConfirmDTO：确认参数
     * @return boolean
     * @author peng.xy
     * @date 2022/6/10
     */
    boolean payNotifyConfirm(PayNotifyConfirmDTO payNotifyConfirmDTO);

    /**
     * 申请退款
     *
     * @param applyRefundDTO：退款参数
     * @return com.msb.pay.model.vo.ApplyRefundVO
     * @author peng.xy
     * @date 2022/6/13
     */
    ApplyRefundVO applyRefund(ApplyRefundDTO applyRefundDTO);

    /**
     * 退款成功回调确认
     *
     * @param refundNotifyConfirmDTO：确认参数
     * @return boolean
     * @author peng.xy
     * @date 2022/6/10
     */
    boolean refundNotifyConfirm(RefundNotifyConfirmDTO refundNotifyConfirmDTO);

    /**
     * 收银台预支付
     *
     * @param prepaymentDTO：预支付下单参数
     * @return com.msb.pay.model.vo.PrepaymentVO
     * @author peng.xy
     * @date 2022/7/8
     */
    PrepaymentVO prepayment(PrepaymentDTO prepaymentDTO);

}
