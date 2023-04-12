package com.cslg.graduation.controller;

import com.cslg.graduation.common.ResponseService;
import com.cslg.graduation.dao.ScoreMapper;
import com.cslg.graduation.entity.User;
import com.cslg.graduation.service.ScoreService;
import com.cslg.graduation.service.UserService;
import com.cslg.graduation.service.WeekService;
import com.cslg.graduation.util.GraduationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * @auther xurou
 * @date 2023/3/29
 */

@RestController
@RequestMapping("/score")
@CrossOrigin(origins = "http://localhost:5173")
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private ScoreMapper scoreMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private WeekService weekService;

    @RequestMapping("/getAllScore")
    @ResponseBody
    public ResponseService getScore(){
        // 返回的所有积分数据
        List<Map<String,Object>> scoreList = new ArrayList<>();
        // 获取所有显示的学号
        List<String> userList = userService.IsScoreUsers();
        // 获取所有周赛时间
        List<Date> allTime = weekService.getAllTime();
        Collections.reverse(allTime);
        for(String username: userList){
            // 存储该用户的数据
            Map<String,Object> map = new HashMap<>();
            User user = userService.findUserByUsername(username);
            double totalScore = scoreService.getTotalScoreByUsername(username);
            List<Map<String ,Object>> allDailyScore = new ArrayList<>();
            for(Date time : allTime){
                Map<String ,Object> ScoreAll = new HashMap<>();
                double dailyScore = scoreService.getDailyScoreByUsernameAndTime(username,time);
                ScoreAll.put("score",dailyScore);
                ScoreAll.put("rank",scoreService.getRankByUsernameAndTime(username,time));
                ScoreAll.put("time", GraduationUtil.DateToString(time));
                allDailyScore.add(ScoreAll);
            }
            map.put("total",totalScore);
            map.put("name",user.getName());
            map.put("username",user.getUsername());
            map.put("week",allDailyScore);
            scoreList.add(map);
        }

        Collections.sort(scoreList, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                double a = (double) o1.get("total");
                double b = (double) o2.get("total");
                return -Double.compare(a,b);
            }
        });

        for(int i=0;i<scoreList.size();i++){
            scoreList.get(i).put("rank",i+1);
        }


        return ResponseService.createBySuccess(scoreList);
    }



}
