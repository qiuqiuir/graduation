package com.cslg.graduation.controller;

import com.cslg.graduation.common.ResponseService;
import com.cslg.graduation.entity.Score;
import com.cslg.graduation.entity.User;
import com.cslg.graduation.entity.Week;
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
    private UserService userService;

    @Autowired
    private WeekService weekService;

    @RequestMapping("/getLegalScore")
    @ResponseBody
    public ResponseService getLegalScore() {
        // 返回的所有积分数据
        List<Map<String, Object>> scoreList = new ArrayList<>();
        // 获取所有显示的学号
        List<User> userList = userService.getAllUserMessage();
        // 获取所有周赛时间
        List<Week> allWeek = weekService.getLegalAllWeek();
        List<Date> allTime = new ArrayList<>();
        for (Week week : allWeek) {
            allTime.add(week.getTime());
        }
        // 每周最高积分
        Map<Date, Double> maxScore = new HashMap<>();
        for (Date time : allTime) {
            maxScore.put(time, scoreService.getMaxDailyScore(time));
        }
        Collections.reverse(allTime);
        for (User user : userList) {
            // 存储该用户的数据
            Map<String, Object> map = new HashMap<>();
            String username = user.getUsername();
            // 积分总分
            double totalScore = scoreService.getTotalScoreByUsername(username);
            // 每周积分合集
            List<Map<String, Object>> allDailyScore = new ArrayList<>();
            // 参加周赛次数
            int cnt = 0;
            // 学号为username的学生参加的所有场次
            List<Score> scoreListByUsername = scoreService.getScoresByUsername(username);
            for (Score score : scoreListByUsername) {
                Double dailyScore = score.getDailyScore();
                Date time = score.getTime();
                Map<String, Object> ScoreAll = new HashMap<>();
                ScoreAll.put("score", dailyScore);
                ScoreAll.put("rank", score.getRank());
                ScoreAll.put("time", GraduationUtil.DateToString(time).substring(5));
                ScoreAll.put("isOne", dailyScore.equals(maxScore.get(time)));
                allDailyScore.add(ScoreAll);
                if (dailyScore > 0) {
                    cnt++;
                }
            }
            map.put("total", totalScore);
            map.put("name", user.getName());
            map.put("isAcm", user.getIsScore());
            map.put("username", user.getUsername());
            map.put("week", allDailyScore);
            map.put("attendance", cnt == allTime.size());
            scoreList.add(map);
        }
        // 按照积分总分降序排序
        scoreList.sort((o1, o2) -> {
            double a = (double) o1.get("total");
            double b = (double) o2.get("total");
            return -Double.compare(a, b);
        });
        // 排序后计算排名
        for (int i = 0; i < scoreList.size(); i++) {
            scoreList.get(i).put("rank", i + 1);
        }
        return ResponseService.createBySuccess(scoreList);
    }

    @RequestMapping("/getAllScore")
    @ResponseBody
    public ResponseService getAllScore() {
        // 返回的所有积分数据
        List<Map<String, Object>> scoreList = new ArrayList<>();
        // 获取所有显示的学号
        List<User> userList = userService.getAllUsers();
        // 获取所有周赛时间
        List<Week> allWeek = weekService.getAllWeek();
        List<Date> allTime = new ArrayList<>();
        for (Week week : allWeek) {
            allTime.add(week.getTime());
        }
        Collections.reverse(allTime);
        for (User user : userList) {
            // 存储该用户的数据
            Map<String, Object> map = new HashMap<>();
            String username = user.getUsername();
            // 积分总分
            double totalScore = 0;
            for (Date time : allTime) {
                Score score = scoreService.getScore(username, time);
                if (score == null) {
                    continue;
                }
                totalScore += score.getDailyScore();
                map.put(GraduationUtil.DateToString(time), score.getDailyScore());
            }
            map.put("total", totalScore);
            map.put("name", user.getName());
            map.put("isAcm", user.getIsScore());
            map.put("username", user.getUsername());
            scoreList.add(map);
        }
        // 按照积分总分降序排序
        scoreList.sort((o1, o2) -> {
            double a = (double) o1.get("total");
            double b = (double) o2.get("total");
            return -Double.compare(a, b);
        });
        // 排序后计算排名
        for (int i = 0; i < scoreList.size(); i++) {
            scoreList.get(i).put("rank", i + 1);
        }
        return ResponseService.createBySuccess(scoreList);
    }


}
