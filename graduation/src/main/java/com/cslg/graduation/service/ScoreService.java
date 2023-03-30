package com.cslg.graduation.service;

import com.cslg.graduation.dao.ScoreMapper;
import com.cslg.graduation.entity.Score;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @auther xurou
 * @date 2023/3/29
 */

@Service
public class ScoreService {

    @Autowired
    private ScoreMapper scoreMapper;

    // 获取username总积分
    public double getTotalScoreByUsername(String username) {
        return scoreMapper.findTotalScoreByUsername(username);
    }

    // 新增username在某天的分数
    public void addScore(String username, Date time, double dailyScore) {
        // 获取此前总分
        double totalScore = getTotalScoreByUsername(username);

        Score score = new Score()
                .setUsername(username)
                .setTime(time)
                .setDailyScore(dailyScore)
                .setTotalScore(dailyScore + totalScore);
        scoreMapper.insertScore(score);
    }

    // 更改username某天积分
    public void updateScore(String username, Date time, double dailyScore) {

        //计算差值
        double difference = dailyScore - scoreMapper.findDailyScoreByUsername(username, time);
        scoreMapper.updateDailyScore(username, time, dailyScore);
        scoreMapper.updateTotalScore(username, time, difference);
    }




}
