package com.cslg.graduation.dao;

import com.cslg.graduation.entity.Contest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ContestMapper {

    // 获取所有比赛记录
    List<Contest> selectAllContest();

    // 插入一条比赛记录
    void insertContest(Contest contest);

    Contest selectContestById(int id);

    int selectNumberById(int id);

    int selectCountContest();


}
