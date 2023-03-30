package com.cslg.graduation.service;

import com.cslg.graduation.dao.WeekMapper;
import com.cslg.graduation.entity.Week;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @auther xurou
 * @date 2023/3/30
 */
@Service
public class WeekService {

    @Autowired
    private WeekMapper weekMapper;

    /**
     * 新增一场周赛信息
     * @param week
     */
    public void addWeek(Week week){
        weekMapper.insertWeek(week);
    }
}
