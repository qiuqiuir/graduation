package com.cslg.graduation.dao;

import com.cslg.graduation.entity.Week;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
 * @auther xurou
 * @date 2023/3/30
 */
@Mapper
public interface WeekMapper {

    public void insertWeek(Week week);

    public void updateCount(Date time, int count);

    public void updateSum(Date time, double sum);

    public void updateAvg(Date time);

    // 获取所有周赛时间
    List<Date> selectAllTime();

}
