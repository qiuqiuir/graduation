package com.cslg.graduation.dao;

import com.cslg.graduation.entity.Contest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ContestMapper {

    // 获取所有比赛获奖记录
    public List<Contest> selectAllContest();

    // 插入一条比赛获奖记录
    public void insertContest(Contest contest);

}
