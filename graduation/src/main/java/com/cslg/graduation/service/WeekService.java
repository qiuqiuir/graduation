package com.cslg.graduation.service;

import com.cslg.graduation.dao.WeekMapper;
import com.cslg.graduation.entity.Week;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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

    /**
     * 新增一场周赛信息
     *
     * @param week
     */
    public void addWeek(Week week) {
        week = week.setIsShow(1);
        // 插入新周赛
        weekMapper.insertWeek(week);
        System.out.println(week.getTime()+"周赛已添加完成，正在计算积分");
        // 对于每位同学计算积分
        scoreService.addWeekScore(week);
        System.out.println("积分已计算完成，正在更新周赛数据");
        // 更新周赛数据
        updateNumSumAvgByTime(week.getTime());

    }

    /**
     * 更新一场周赛的总信息：总分、人数、平均分
     *
     * @param time
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
     * @param time
     * @return
     */
    public List<Week> getLegalWeek(Date time) {
        return weekMapper.selectLegalWeek(time);
    }

    /**
     * 获取所有周赛信息
     *
     * @return
     */
    public List<Week> getAllWeek() {
        return weekMapper.selectAllWeek();
    }

    /**
     * 获取所有本学期周赛时间
     *
     * @return
     */
    public List<Week> getLegalAllWeek() {
        return weekMapper.selectLegalWeek(new Date(0, 0, 0));
    }


    /**
     * 根据time时间获取那场周赛信息
     * @param time
     * @return
     */
    public Week getWeekByTime(Date time){
        return weekMapper.selectWeekByTime(time);
    }

    /**
     * 更改时间为time的周赛的是否展示状态
     * @param time
     */
    public void updateIsShow(Date time){
        Week week = getWeekByTime(time);
        weekMapper.updateIsShow(time, 1- week.getIsShow());
    }

    /**
     * 更改本学期所有周赛为不显示状态
     */
    public void updateLegalWeek(){
        List<Week> weekList = getLegalAllWeek();
        for(Week week : weekList){
            updateIsShow(week.getTime());
        }
    }
}
