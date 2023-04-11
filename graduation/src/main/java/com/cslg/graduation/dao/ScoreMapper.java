package com.cslg.graduation.dao;

import com.cslg.graduation.entity.Score;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
 * @auther xurou
 * @date 2023/3/29
 */

@Mapper
public interface ScoreMapper {

    // 根据学号找到该用户总积分
    public double findTotalScoreByUsername(String username);

    // 根据学号和时间获取用户该日积分
    public double findDailyScoreByUsername(String username, Date time);

    // 根据学号和时间获取用户该日排名
    public double findRankByUsername(String username, Date time);

    // 新增一条积分
    public void insertScore(Score score);


    // 更新用户某日积分
    public void updateDailyScore(String username, Date time, double dailyScore);

    // 更新用户某日总积分
    public void updateTotalScore(String username, Date time, double addScore);

    // 统计某天多少人参加比赛
    public int selectCountByTime(Date time);

    // 计算某天所有人积分和
    public double selectSumByTime(Date time);

}
