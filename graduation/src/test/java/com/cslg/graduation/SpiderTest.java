package com.cslg.graduation;

import com.alibaba.fastjson.JSONObject;
import com.cslg.graduation.entity.User;
import com.cslg.graduation.entity.Week;
import com.cslg.graduation.service.*;
import com.cslg.graduation.util.GetUrlJson;
import com.cslg.graduation.util.GraduationUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private KnowledgeService knowledgeService;

    @Autowired
    private UserService userService;

    @Autowired
    private OjService ojService;

    @Test
    public void spider() {

//        System.out.println(spiderService.getNowcoderScore("115325176","53378"));
//        System.out.println(spiderService.getNowcoderScore("694559251","53378"));
//        System.out.println(spiderService.getNowcoderScore("684484379","53378"));


//        System.out.println(spiderService.getAtcoderScore("cslg092621114", "abc295"));
//        System.out.println(spiderService.getAtcoderScore("cslg092621106", "abc295"));
//        System.out.println(spiderService.getAtcoderScore("cslg092222128", "abc295"));
//        System.out.println(GraduationUtil.md5("123456"));
//        Week week = new Week().setTime(new Date(2023-1900,2,25)).setPlatform("atcoder").setContestId("abc295");
//        scoreService.addWeekScore(week);
//        Map<String,Integer>map = spiderService.getCfSubmission("qiuqiur");
//        for(Map.Entry<String, Integer> entry : map.entrySet()){
//            System.out.println(entry.getKey()+":"+entry.getValue());
//        }
//        knowledgeService.addCodeforcesKnowledge("093119134","cslg093119134");
//        spiderService.getAtcoderScore("abc290");

        List<String> userList = userService.getAllUser();
        for(String s:userList) {
            if(s.equals("093119134")) continue;
            System.out.print(s+":");
            List<String>list = ojService.getAllOjId(s,"codeforces");
            if(list.size()>0) {
                for(String name: list) {
                    knowledgeService.addCodeforcesKnowledge(s,name);
                }
                System.out.println(" over");
            }else{
                System.out.println("no");
            }
        }
    }
}
