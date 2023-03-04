package com.msb.im.context;

import com.msb.im.model.entity.ThirdSystemConfig;

/**
 * @author zhou miao
 * @date 2022/06/02
 */
public class ApiContext {
    private static final ThreadLocal<ThirdSystemConfig> CURRENT_SYSTEM = new ThreadLocal<>();
    private static final ThreadLocal<String> CURRENT_FROM = new ThreadLocal<>();

    private ApiContext() {
    }

    public static void setSystem(ThirdSystemConfig thirdSystemConfig) {
        CURRENT_SYSTEM.set(thirdSystemConfig);
    }

    public static void removeSystem() {
        CURRENT_SYSTEM.remove();
    }

    public static Integer getSystemId() {
        ThirdSystemConfig thirdSystemConfig = CURRENT_SYSTEM.get();
        return thirdSystemConfig == null ? null : thirdSystemConfig.getId();
    }

    public static void setFrom(String from) {
        CURRENT_FROM.set(from);
    }

    public static void removeFrom() {
        CURRENT_FROM.remove();
    }

    public static String getFrom() {
        return CURRENT_FROM.get();
    }
}
