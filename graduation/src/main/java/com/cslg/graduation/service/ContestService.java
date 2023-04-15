package com.cslg.graduation.service;

import com.cslg.graduation.dao.ContestMapper;
import com.cslg.graduation.entity.Contest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @auther xurou
 * @date 2023/3/30
 */

@Service
public class ContestService {

    @Autowired
    private ContestMapper contestMapper;

    /**
     * 获取所有比赛获奖记录
     * @return
     */
    public List<Contest> getAllContest(){
        return contestMapper.selectAllContest();
    }

    /**
     * 添加一场比赛获奖记录
     * @param contest
     */
    public void addContest(Contest contest){
        contestMapper.insertContest(contest);
    }

    /**
     * 根据id获取该比赛单次获奖多少人
     * @param id
     * @return
     */
    public int getNumberById(int id){
        return contestMapper.selectNumberById(id);
    }

    /**
     * 获取比赛数量
     * @return
     */
    public int getCountContest(){
        return contestMapper.selectCountContest();
    }

    /**
     * 根据比赛id查找比赛
     * @param id
     * @return
     */
    public Contest getContestById(int id){
        return contestMapper.selectContestById(id);
    }


}
