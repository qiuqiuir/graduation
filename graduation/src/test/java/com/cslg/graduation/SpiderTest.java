package com.cslg.graduation;

import com.alibaba.fastjson.JSONObject;
import com.cslg.graduation.service.SpiderService;
import com.cslg.graduation.util.GetUrlJson;
import com.cslg.graduation.util.GraduationUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
//import org.js

/**
 * @auther xurou
 * @date 2023/3/30
 */
@SpringBootTest
public class SpiderTest {

    @Autowired
    private SpiderService spiderService;

    @Test
    public void spider() {

//        System.out.println(spiderService.getNowcoderScore("115325176","53378"));
//        System.out.println(spiderService.getNowcoderScore("694559251","53378"));
//        System.out.println(spiderService.getNowcoderScore("684484379","53378"));


//        System.out.println(spiderService.getAtcoderScore("cslg092621114", "abc295"));
//        System.out.println(spiderService.getAtcoderScore("cslg092621106", "abc295"));
//        System.out.println(spiderService.getAtcoderScore("cslg092222128", "abc295"));
        System.out.println(GraduationUtil.md5("123456"));
    }
}
