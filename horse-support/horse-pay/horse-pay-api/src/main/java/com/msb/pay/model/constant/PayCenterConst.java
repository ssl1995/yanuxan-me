package com.msb.pay.model.constant;

/**
 * 支付中台常量类
 *
 * @author peng.xy
 * @date 2022/6/7
 */
public class PayCenterConst {

    public interface PAYMENT_CODE {
        String PREFIX = "payment-";
    }

    public interface PAY_CODE {
        String PREFIX = "padeCode-";
    }

    public interface NOTIFY_RESPONSE {
        String SUCCESS = "SUCCESS";
        String FAIL = "FAIL";
    }

    public interface CURRENCY {
        String CNY = "CNY";
    }

    public interface CERT {
        // 本地证书根目录
        String LOCAL_DIR = "/cert/";

        // 微信私钥证书文件名
        String WX_PAY_KEY_PEM = "apiclient_key.pem";
        // 微信商户证书文件名
        String WX_PAY_CERT_PEM = "apiclient_cert.pem";
        // 微信p12证书文件名
        String WX_PAY_CERT_P12 = "apiclient_cert.p12";
        // 微信平台证书文件名
        String WX_PAY_PLATFORM_PEM = "platform_cert.pem";

        // 支付宝证书文件名
        String ALI_PAY_CERT_CRT = "alipay_cert.crt";
        // 支付宝根证书文件名
        String ALI_PAY_ROOT_CERT_CRT = "alipay_root_cert.crt";
        // 支付宝应用证书文件名
        String ALI_PAY_APP_CERT_CRT = "app_%s.crt";
    }

}
