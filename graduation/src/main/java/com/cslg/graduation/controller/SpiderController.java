package com.cslg.graduation.controller;

import com.alibaba.fastjson2.JSONObject;
import com.cslg.graduation.util.GetUrlJson;
import com.cslg.graduation.util.GraduationUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @auther xurou
 * @date 2023/3/29
 */

@Controller
@RequestMapping("/as")
public class SpiderController {

    @ResponseBody
    @RequestMapping("/a")
    public String spider() {
        String url = "http://47.94.81.95:8081/rank/list?page=1&size=100&keyword=&date=2023-03-28";
        try {
//            String object = GetUrlJson.getHttpJson(url);
//            Map<String,Object> map = GraduationUtil.getJSONToMap(object);
            return "1";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
