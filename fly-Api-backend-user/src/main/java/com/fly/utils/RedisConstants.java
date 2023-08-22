package com.fly.utils;

public class RedisConstants {
    public static final String LIST_USERS_KEY = "list:user:";
    public static final Long LIST_USER_TIME = 180L;

    public static final String LOGIN_TOKEN_KEY = "login:user:";
    public static final Long LOGIN_TIME = 40L;

    public static final String LOGIN_CODE_KEY = "login:code:";
    public static final Long LOGIN_CODE_TIME = 5L;

    public static final String LOGIN_TOKEN = "login:token:";
    public static final Long LOGIN_USER_TIME = 30L;

    public static final String USER_AK_SK = "client_";
    public static final Long USER_AK_SK_TIME = 30L;

    public static final String LIST_INTERFACE_SIZE = "list:interface:size:";
    public static final Long LIST_INTERFACE_TIME = 180L;


    public static final String LIST_INTERFACE = "list:interface:";


    public static final String SUM_INTERFACE = "sum:interface:";
    public static final Long SUM_INTERFACE_TIME = 60L;


    public static final String TOP_SUM_INTERFACE = "top:sum:interface";
    public static final Long TOP_SUM_INTERFACE_TIME = 60L;

    public static final String COUNT_LIST_INTERFACE = "count:list:interface";
    public static final Long COUNT_LIST_INTERFACE_TIME = 10L;

    public static final String LIST_PAGE_INTERFACE = "list:page:interface";
    public static final Long LIST_PAGE_INTERFACE_TIME = 600L;

    public static final String COUNT_USER_REGISTER = "count:user:register";
    public static final Long COUNT_USER_REGISTER_TIME = 60L;


    public static final String SCHEDULED_TASK = "schedule_task_";

    public static final String USER_INTERFACE_LIST_PAGE = "user:interface:page:";
    public static final Long USER_INTERFACE_LIST_PAGE_TIME = 5L;


    public static final String USER_INTERFACE_DETAIL_LIST_PAGE = "user:interface:detail:page:";
    public static final Long USER_INTERFACE_DETAIL_LIST_PAGE_TIME = 5L;

    public static final String USER_LIST_PAGE = "user:page:";
    public static final Long USER_LIST_PAGE_TIME = 5L;


    public static final String TOP_LIST_INTERFACE = "top:interface:";
    public static final Long TOP_LIST_INTERFACE_TIME = 5L;


    public static final String USER_INVOKE_COUNT_ACCESS = "user:invoke:count:access";

    public static final String USER_GET_INTERFACE = "user:interface:";

}
