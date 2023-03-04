package com.msb.pay.mq.model;

import com.msb.framework.common.model.IDict;
import com.msb.framework.common.mq.MqDelayLevel;

/**
 * 支付中台延时级别
 */
public enum PayCenterDelayLevel implements IDict<Integer> {
    LEVEL1(1, "10s", MqDelayLevel.TIME_10S),
    LEVEL2(2, "10s", MqDelayLevel.TIME_10S),
    LEVEL3(3, "30s", MqDelayLevel.TIME_30S),
    LEVEL4(4, "1m", MqDelayLevel.TIME_1M),
    LEVEL5(5, "5m", MqDelayLevel.TIME_5M),
    LEVEL6(6, "30m", MqDelayLevel.TIME_30M),
    LEVEL7(7, "30m", MqDelayLevel.TIME_30M),
    LEVEL8(8, "30m", MqDelayLevel.TIME_30M),
    LEVEL9(9, "1h", MqDelayLevel.TIME_1H),
    LEVEL10(10, "1h", MqDelayLevel.TIME_1H),
    LEVEL11(11, "1h", MqDelayLevel.TIME_1H),
    LEVEL12(12, "1h", MqDelayLevel.TIME_1H),
    LEVEL13(13, "2h", MqDelayLevel.TIME_2H),
    LEVEL14(14, "2h", MqDelayLevel.TIME_2H),
    LEVEL15(15, "2h", MqDelayLevel.TIME_2H),
    LEVEL16(16, "2h", MqDelayLevel.TIME_2H),
    ;

    private final Integer code;

    private final String text;

    private final MqDelayLevel level;

    PayCenterDelayLevel(Integer code, String text, MqDelayLevel level) {
        init(code, text);
        this.code = code;
        this.text = text;
        this.level = level;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getText() {
        return text;
    }

    public MqDelayLevel getLevel() {
        return level;
    }

    public PayCenterDelayLevel getNext() {
        if (this.code < PayCenterDelayLevel.values().length) {
            return IDict.getByCode(PayCenterDelayLevel.class, this.code + 1);
        }
        return null;
    }

}
