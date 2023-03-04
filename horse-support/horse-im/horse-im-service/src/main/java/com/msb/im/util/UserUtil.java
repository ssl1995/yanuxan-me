package com.msb.im.util;

import com.msb.framework.common.context.UserContext;

/**
 * @author zhou miao
 * @date 2022/05/31
 */
public class UserUtil {

    private UserUtil () {}

    public static String getUserId() {
        return UserContext.getUserId().toString();
    }
}
