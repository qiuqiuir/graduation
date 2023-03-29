package com.cslg.graduation.dao;

import com.cslg.graduation.entity.Score;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;

/**
 * @auther xurou
 * @date 2023/3/29
 */

@Mapper
public interface ScoreMapper {

    public double findTotalScoreByUsername(String username);

    public double findDailyScoreByUsername(String username, Date time);

    public void insertScore(Score score);

    public void updateDailyScore(String username, Date time, double dailyScore);

    public void updateTotalScore(String username, Date time, double addScore);

    public Score selectScoreByUsernameTime(String username, Date time);
}
