package com.msb.pay.channel;

import com.alibaba.fastjson.JSONObject;
import com.msb.pay.model.bo.ApplyRefundRES;
import com.msb.pay.model.bo.PayNotifyRES;
import com.msb.pay.model.bo.RefundNotifyRES;
import com.msb.pay.model.dto.ApplyRefundDTO;
import com.msb.pay.model.dto.BaseAppDataDTO;
import com.msb.pay.model.dto.BaseMchDataDTO;
import com.msb.pay.model.entity.AppInfo;
import com.msb.pay.model.entity.MchInfo;
import com.msb.pay.model.entity.PayOrder;
import com.msb.pay.model.entity.RefundOrder;
import com.msb.pay.model.vo.BaseAppDataVO;
import com.msb.pay.model.vo.BaseMchDataVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 支付厂商对接接口
 *
 * @author peng.xy
 * @date 2022/6/1
 */
public interface IPaymentService {

    /**
     * 处理支付回调
     *
     * @param request：请求对象
     * @param mchInfo：商户信息
     * @return com.msb.pay.model.bo.PayNotifyBO
     * @author peng.xy
     * @date 2022/6/9
     */
    PayNotifyRES payNotify(HttpServletRequest request, MchInfo mchInfo);

    /**
     * 发起退款请求
     *
     * @param mchInfo：商户信息
     * @param appInfo：应用信息
     * @param payOrder：支付订单
     * @param applyRefundDTO：申请参数
     * @return com.msb.pay.model.bo.RefundApplyBO
     * @author peng.xy
     * @date 2022/6/11
     */
    ApplyRefundRES refundRequest(MchInfo mchInfo, AppInfo appInfo, PayOrder payOrder, ApplyRefundDTO applyRefundDTO);

    /**
     * 处理退款回调
     *
     * @param request：请求对象
     * @param mchInfo：商户信息
     * @return com.msb.pay.model.bo.RefundNotifyRES
     * @author peng.xy
     * @date 2022/6/13
     */
    RefundNotifyRES refundNotify(HttpServletRequest request, MchInfo mchInfo);

    /**
     * 交易订单查询
     *
     * @param mchInfo：商户信息
     * @param appInfo：应用信息
     * @param payOrder：支付订单
     * @return com.alibaba.fastjson.JSONObject
     * @author peng.xy
     * @date 2022/6/23
     */
    JSONObject tradeQuery(MchInfo mchInfo, AppInfo appInfo, PayOrder payOrder);

    /**
     * 退款订单查询
     *
     * @param mchInfo：商户信息
     * @param appInfo：应用信息
     * @param payOrder：支付订单
     * @param refundOrder：退款订单
     * @return com.alibaba.fastjson.JSONObject
     * @author peng.xy
     * @date 2022/6/23
     */
    JSONObject refundQuery(MchInfo mchInfo, AppInfo appInfo, PayOrder payOrder, RefundOrder refundOrder);

    /**
     * 解析商户资料信息，对重要数据脱敏
     *
     * @param mchInfo：商户信息
     * @param baseMchDataVO：商户资料信息VO
     * @author peng.xy
     * @date 2022/6/29
     */
    void parseMchInfoData(MchInfo mchInfo, BaseMchDataVO baseMchDataVO);

    /**
     * 保存商户资料信息
     *
     * @param mchInfo：商户信息
     * @param baseMchDataDTO：提交的商户资料信息DTO
     * @return boolean：证书是否发生变更
     * @author peng.xy
     * @date 2022/6/30
     */
    boolean saveMchInfoData(MchInfo mchInfo, BaseMchDataDTO baseMchDataDTO);

    /**
     * 解析应用资料信息，对重要数据脱敏
     *
     * @param appInfo：应用信息
     * @param baseAppDataVO：应用资料信息VO
     * @return void
     * @author peng.xy
     * @date 2022/7/1
     */
    void parseAppInfoData(AppInfo appInfo, BaseAppDataVO baseAppDataVO);

    /**
     * 保存应用资料信息
     *
     * @param mchInfo：商户信息
     * @param appInfo：应用信息
     * @param baseAppDataDTO：提交的应用资料信息DTO\
     * @return boolean：证书是否发生变更
     * @author peng.xy
     * @date 2022/7/1
     */
    boolean saveAppInfoData(MchInfo mchInfo, AppInfo appInfo, BaseAppDataDTO baseAppDataDTO);

    /**
     * 下载商户证书
     *
     * @param mchInfo：商户信息
     * @author peng.xy
     * @date 2022/7/9
     */
    void downloadMchInfoCert(MchInfo mchInfo);

    /**
     * 下载应用证书
     *
     * @param mchInfo：商户信息
     * @param appInfo：应用信息
     * @author peng.xy
     * @date 2022/7/9
     */
    void downloadAppInfoCert(MchInfo mchInfo, AppInfo appInfo);

}
