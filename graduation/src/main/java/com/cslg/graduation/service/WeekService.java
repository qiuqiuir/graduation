package com.cslg.graduation.service;

import com.cslg.graduation.dao.WeekMapper;
import com.cslg.graduation.entity.Score;
import com.cslg.graduation.entity.User;
import com.cslg.graduation.entity.Week;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @auther xurou
 * @date 2023/3/30
 */
@Service
public class WeekService {

    @Autowired
    private WeekMapper weekMapper;

    @Autowired
    @Lazy
    private ScoreService scoreService;

    @Autowired
    private UserService userService;

    @Autowired
    private AcnumberService acnumberService;

    /**
     * 新增一场周赛信息
     *
     * @param week
     */
    public void addWeek(Week week) {
        week = week.setIsShow(1);
        // 插入新周赛
        weekMapper.insertWeek(week);
        System.out.println(week.getTime() + "周赛已添加完成，正在计算积分");
        // 对于每位同学计算积分
        scoreService.addWeekScore(week);
        System.out.println("积分已计算完成，正在更新周赛数据");
        // 更新周赛数据
        updateNumSumAvgByTime(week.getTime());

    }

    /**
     * 更新一场周赛的总信息：总分、人数、平均分
     *
     */
    public void updateNumSumAvgByTime(Date time) {
        // 统计周赛总积分
        double sumScore = scoreService.getSumByTime(time);
        // 获取周赛参与人数
        int cntScore = scoreService.getNumByTime(time);
        weekMapper.updateSum(time, sumScore);
        weekMapper.updateCount(time, cntScore);
        weekMapper.updateAvg(time);
        scoreService.updateWeekRank(time);
    }

    /**
     * 获取本学期某天之后的所有周赛时间
     *
     * @param time 时间
     * @return List<Week>
     */
    public List<Week> getLegalWeek(Date time) {
        return weekMapper.selectLegalWeek(time);
    }

    /**
     * 获取所有周赛信息
     *
     * @return List<Week>所有周赛信息
     */
    public List<Week> getAllWeek() {
        return weekMapper.selectAllWeek();
    }

    /**
     * 获取所有本学期周赛时间
     *
     * @return List<Week>所有本学期周赛信息
     */
    public List<Week> getLegalAllWeek() {
        return weekMapper.selectLegalWeek(new Date(0, Calendar.JANUARY, 0));
    }


    /**
     * 根据time时间获取那场周赛信息
     *
     * @param time 时间
     * @return Week周赛信息
     */
    public Week getWeekByTime(Date time) {
        return weekMapper.selectWeekByTime(time);
    }

    /**
     * 更改时间为time的周赛的是否展示状态
     *
     * @param time 时间
     */
    public void updateIsShow(Date time) {
        Week week = getWeekByTime(time);
        weekMapper.updateIsShow(time, 1 - week.getIsShow());
    }

    /**
     * 更改本学期所有周赛为不显示状态
     */
    public void updateLegalWeek() {
        List<Week> weekList = getLegalAllWeek();
        for (Week week : weekList) {
            updateIsShow(week.getTime());
        }
    }

    /**
     * 删除本学期所有积分
     */
    public void deleteLegalWeek() {
        weekMapper.deleteLegalWeek();
    }

    /**
     * 重跑本学期积分
     */
    public void reloadScore() {
        List<Week> weekList = getLegalAllWeek();
        deleteLegalWeek();
        for (Week week : weekList) {
            addWeek(week);
        }
    }

    /**
     * 获取公假名单
     *
     * @return List<Map<String, String>>,Map<String, String>包含"username"学号,"name"姓名
     */
    public List<Map<String, String>> getNote(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        int year = calendar.get(Calendar.YEAR);
        if (calendar.get(Calendar.MONTH) + 1 < 9) {
            year = year - 1;
        }
        calendar.add(Calendar.DATE, -7);
        Date last = calendar.getTime();
        String findYear = String.valueOf(year % 100);
        List<Score> scoreList = scoreService.getScoresByTime(time);
        double sumScore = scoreService.getSumByTime(time);
        // 获取周赛参与人数
        int cntScore = scoreService.getNumByTime(time);
        double avg = sumScore / cntScore;
//        System.out.println(avg);
        List<Map<String, String>> list = new ArrayList<>();
        for (Score score : scoreList) {
            String username = score.getUsername();
            if (username.charAt(4)==findYear.charAt(0)&&username.charAt(5)==findYear.charAt(1)) {
                int nowNumber = acnumberService.getAllCount(username, time);
                int lastNumber = acnumberService.getAllCount(username, last);
                int number = Math.max(0, nowNumber - lastNumber);
                double nowScore = score.getDailyScore() + number;
//                System.out.print(username + " " + nowScore + " ");
                if (nowScore >= avg / 2) {
                    User user = userService.findUserByUsername(username);
                    if(user.getIsScore() == 1) {
                        Map<String, String> map = new HashMap<>();
                        map.put("username", username);
                        map.put("name", user.getName());
                        list.add(map);
                    }
                }
            }
        }
        return list;
    }
}
