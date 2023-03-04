package com.msb.mall.trade.enums;

import com.msb.framework.common.model.IDict;

/**
 * 物流公司枚举
 */
public enum LogisticsCompanyEnum implements IDict<String> {

    // 物流公司枚举
    SF("shunfeng", "顺丰速运"),
    JD("jd", "京东物流"),
    YT("yuantong", "圆通速递"),
    ZT("zhongtong", "中通快递"),
    ST("shentong", "申通快递"),
    YD("yunda", "韵达快递"),
    EMS("ems", "EMS"),
    YZK("youzhengguonei", "邮政快递包裹"),
    YZB("youzhengbk", "邮政标准快递"),
    DB("debangwuliu", "德邦"),
    DBK("debangkuaidi", "德邦快递"),
    BS("huitongkuaidi", "百世快递"),
    JT("jtexpress", "极兔速递"),
    FW("fengwang", "丰网速运"),
    GJ("youzhengguoji", "国际包裹"),
    ZTG("zhongtongguoji", "中通国际"),
    SBD("subida", "速必达"),
    ZJS("zhaijisong", "宅急送"),
    YDK("yundakuaiyun", "韵达快运"),
    ZTK("zhongtongkuaiyun", "中通快运"),
    ;

    LogisticsCompanyEnum(String code, String text) {
        init(code, text);
    }

}
