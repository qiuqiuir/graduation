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

    // 根据比赛id选择比赛
    Contest selectContestById(int id);

    // 根据比赛id查看该比赛获奖人数
    int selectNumberById(int id);

    // 获取所有比赛数量
    int selectCountContest();

    // 根据比赛id删除该比赛
    boolean deleteContestById(int id);


}
