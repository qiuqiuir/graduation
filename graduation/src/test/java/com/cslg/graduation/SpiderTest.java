package com.cslg.graduation;

import com.alibaba.fastjson.JSONArray;
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
    public void test1() throws InterruptedException {
        Date date = new Date();
        System.out.println("当前时间:" + date);
        System.out.println("开始爬取队员各大oj上的rating");
        List<Oj> ojList = ojService.getAllOj();
        for (Oj oj : ojList) {
            if(oj.getUsername().equals("092621114")) {
//            Thread.sleep(1000);
                String platform = oj.getPlatform();
                String username = oj.getUsername();
                String id = oj.getOjId();
                System.out.println(username + "   " + platform + "   " + id);
                if (platform.equals("nowcoder")) {
                    Map<String, Integer> rating = spiderService.getNowcoderRating(id);
                    if (rating.get("current") != null) {
                        ojService.updateNowRating(username, platform, id, rating.get("current"));
                    }
                    if (rating.get("history") != null) {
                        ojService.updateHistoryRating(username, platform, id, rating.get("history"));
                    }
                } else if (platform.equals("codeforces")) {
                    Map<String, Integer> rating = spiderService.getCfRating(id);
                    if (rating.get("current") != null) {
                        ojService.updateNowRating(username, platform, id, rating.get("current"));
                    }
                    if (rating.get("history") != null) {
                        ojService.updateHistoryRating(username, platform, id, rating.get("history"));
                    }
                } else if (platform.equals("atcoder")) {
                    Map<String, Integer> rating = spiderService.getAtcoderRating(id);
                    if (rating.get("current") != null) {
                        ojService.updateNowRating(username, platform, id, rating.get("current"));
                    }
                    if (rating.get("history") != null) {
                        ojService.updateHistoryRating(username, platform, id, rating.get("history"));
                    }
                }
                System.out.println("over");
            }
        }
        date = new Date();
        System.out.println("当前时间:" + date);
        System.out.println("rating统计已完成");
    }

    @Test
    public void spider() throws InterruptedException {
        Map<String, Integer> rating = spiderService.getCfRating("cslg052922104");
        System.out.println(rating);
    }
}
