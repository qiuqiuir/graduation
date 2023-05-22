package com.cslg.graduation;

import com.alibaba.fastjson.JSONObject;
import com.cslg.graduation.common.ResponseService;
import com.cslg.graduation.entity.Oj;
import com.cslg.graduation.entity.User;
import com.cslg.graduation.entity.Week;
import com.cslg.graduation.service.*;
import com.cslg.graduation.util.GetUrlJson;
import com.cslg.graduation.util.GraduationUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.text.SimpleDateFormat;
import java.util.*;
//import org.js

/**
 * @auther xurou
 * @date 2023/3/30
 */
@SpringBootTest
public class SpiderTest {

    @Autowired
    private SpiderService spiderService;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private KnowledgeService knowledgeService;

    @Autowired
    private UserService userService;

    @Autowired
    private OjService ojService;

    @Test
    public void test1() {
        Map<String,Integer>map = spiderService.getCfRating("qiuqiur");
        System.out.println(map);
    }

    @Test
    public void spider() throws InterruptedException {

    }
}
