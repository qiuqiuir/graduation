package com.cslg.graduation.util;

/**
 * @auther xurou
 * @date 2022/7/17
 */

public class RedisKeyUtil {

    private static final String SPLIT = ":";
    private static final String PREFIX_TICKET = "ticket";
    private static final String PREFIX_USER = "user";



    // 登录凭证
    public static String getTicketKey(String ticket) {
        return PREFIX_TICKET + SPLIT + ticket;
    }

    // 用户
    public static String getUserKey(int userId) {
        return PREFIX_USER + SPLIT + userId;
    }


}
