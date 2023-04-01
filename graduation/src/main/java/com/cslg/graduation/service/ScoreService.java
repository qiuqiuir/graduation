package com.cslg.graduation.service;

import com.cslg.graduation.dao.ScoreMapper;
import com.cslg.graduation.entity.Score;
import com.cslg.graduation.entity.Week;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @auther xurou
 * @date 2023/3/29
 */

@Service
public class ScoreService {

    @Autowired
    private ScoreMapper scoreMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private SpiderService spiderService;

    @Autowired
    private OjService ojService;

    @Autowired
    private WeekService weekService;


    /**
     * 获取username总积分
     *
     * @param username
     * @return
     */
    public double getTotalScoreByUsername(String username) {
        return scoreMapper.findTotalScoreByUsername(username);
    }

    /**
     * 新增username在某天的分数
     *
     * @param username
     * @param time
     * @param dailyScore
     */
    public void addScore(String username, Date time, double dailyScore) {
        // 获取此前总分
        double totalScore = getTotalScoreByUsername(username);

        Score score = new Score()
                .setUsername(username)
                .setTime(time)
                .setDailyScore(dailyScore)
                .setTotalScore(dailyScore + totalScore);
        scoreMapper.insertScore(score);
        weekService.updateNumSumAvgByTime(time);
    }

    /**
     * 更改username某天积分
     *
     * @param username
     * @param time
     * @param dailyScore
     */
    public void updateScore(String username, Date time, double dailyScore) {

        //计算差值
        double difference = dailyScore - scoreMapper.findDailyScoreByUsername(username, time);
        // 更新当天积分
        scoreMapper.updateDailyScore(username, time, dailyScore);
        // 查找time之后的比赛日
        List<Date> timeList = weekService.getAllTime(time);
        // 更新所有比赛日
        for(Date date : timeList) {
            // 更新该用户的总积分
            scoreMapper.updateTotalScore(username, time, difference);
            // 更新该周积分情况
            weekService.updateNumSumAvgByTime(date);
        }
    }

    /**
     * 根据周赛信息计算所有需要计算积分的人的积分
     * @param week
     */
    public void addWeekScore(Week week) {
        // 所有计算积分的用户
        List<String> usernameList = userService.IsScoreUsers();
        for (String username : usernameList) {
            // 该用户的积分
            double score = 0.0;
            // 该用户在该平台的所有id
            List<String> ojIdlist = ojService.getAllOjId(username, week.getPlatform());
            for(String ojid: ojIdlist){
                if (week.getPlatform().equals("nowcoder")) {
                    double nowScore = spiderService.getNowcoderScore(ojid, week.getContestId());
                    if(nowScore > score) score = nowScore;
                } else if (week.getPlatform().equals("atcoder")) {
                    double nowScore = spiderService.getAtcoderScore(ojid, week.getContestId());
                    if(nowScore > score) score = nowScore;
                }
            }
            // 添加积分
            addScore(username, week.getTime(), score);
        }

    }

    /**
     * 返回time时间参加周赛的人数
     * @param time
     * @return
     */
    public int getNumByTime(Date time){
        return scoreMapper.selectCountByTime(time);
    }

    /**
     * 返回time时间周赛的总积分
     * @param time
     * @return
     */
    public double getSumByTime(Date time){
        return scoreMapper.selectSumByTime(time);
    }

    public double getDailyScoreByUsernameAndTime(String username, Date time){
        return scoreMapper.findDailyScoreByUsername(username, time);
    }


}
