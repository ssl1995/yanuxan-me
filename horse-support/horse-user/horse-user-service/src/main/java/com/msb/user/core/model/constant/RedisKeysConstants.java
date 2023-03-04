package com.msb.user.core.model.constant;

public class RedisKeysConstants {

    private static final String APP_KEY = "horse-user:";

    public static final String LOGIN_VERIFICATION_CODE = APP_KEY + "login-verification-code:";

    public static final String LOGIN_VERIFICATION_CODE_INTERVAL = APP_KEY + "login-verification-code-interval:";

    public static final String USER_TOKEN_MAP = APP_KEY + "user_token_mapV2:";

    public static final String USER_PERMISSION_LIST = APP_KEY + "user_permission_list:";

    public static final String ROLE_PERMISSION_LIST = APP_KEY + "role_permission_list:";

    public static final String GET_DEPARTMENT_CHAIN_NAME = APP_KEY + "get_department_chain_name:";

    public static final String GET_EMPLOYEE_USERID = APP_KEY + "get_employee_userid:";

    public static final String GET_PERMISSION = APP_KEY + "get_permission:";

    public static final String VISITOR_UV_KEY = "horse-user:user_visitor:visitor_uuid_uv:";

    public static final String VISITOR_PV_KEY = "horse-user:user_visitor:visitor_uuid_pv:";

}
