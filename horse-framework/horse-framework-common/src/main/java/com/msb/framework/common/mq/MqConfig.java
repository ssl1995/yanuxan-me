package com.msb.framework.common.mq;

import com.msb.framework.common.utils.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * MQ消息配置
 *
 * @author liao
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class MqConfig implements Serializable {

    /**
     * tag
     */
    private String tag;

    /**
     * 设置了同一hashKey的消息可保证消费顺序
     */
    private String hashKey;

    /**
     * 延迟消息的延迟级别（支持开源rocketmq）
     */
    private MqDelayLevel delayLevel;

    /**
     * 消息交付时间（延时消息的发送时间，阿里云rocketmq）
     */
    private Long startDeliverTime;

    /**
     * 设置延时时长，会根据当前时间 + 延时时长确定最终发送时间
     *
     * @param delayDuration 延时 时长
     */
    public void setDelayTime(Duration delayDuration) {
        this.setStartDeliverTime(System.currentTimeMillis() + delayDuration.toMillis());
    }

    public void setStartDeliverTime(LocalDateTime startDeliverTime) {
        setStartDeliverTime(DateUtil.toMill(startDeliverTime));
    }

    public void setStartDeliverTime(Long startDeliverTime) {
        this.startDeliverTime = startDeliverTime;
    }
}
