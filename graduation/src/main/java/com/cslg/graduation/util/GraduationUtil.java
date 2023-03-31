package com.cslg.graduation.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.*;

public class GraduationUtil {

    // 生成随机字符串
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    // MD5加密
    public static String md5(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    public static JSONObject getJSONString(int code, String msg, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        if (map != null) {
            for (String key : map.keySet()) {
                json.put(key, map.get(key));
            }
        }
        return json;
    }

    public static JSONObject getJSONString(int code, String msg) {
        return getJSONString(code, msg, null);
    }

    // 时间加一天，解决前后端差8h问题
    public static Date changeTime(Date time){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(time);
        calendar.add(Calendar.DATE,1);
        return calendar.getTime();
    }
}
