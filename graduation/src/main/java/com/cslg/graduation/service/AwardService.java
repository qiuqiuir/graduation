package com.cslg.graduation.service;

import com.cslg.graduation.dao.AwardMapper;
import com.cslg.graduation.entity.Award;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @auther xurou
 * @date 2023/4/13
 */

@Service
public class AwardService {

    @Autowired
    private AwardMapper awardMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private ContestService contestService;

    /**
     * 获取所有获奖信息
     * @return
     */
    public List<Award> getAllAward() {
        return awardMapper.selectAllAward();
    }

    /**
     * 根据比赛id获取所有获奖信息
     * @param id
     * @return
     */
    public List<Award> getAwardById(int id) {
        return awardMapper.selectAwardById(id);
    }

    /**
     * 根据比赛id和比赛类型type获取获奖人员姓名
     * @param id
     * @param type
     * @return
     */
    public String getAwardByIdAndType(int id, String type) {
        List<Award> awardList = awardMapper.selectAwardByIdAndType(id, type);
        for (int i = 0; i < awardList.size(); i++) {
            String username = awardList.get(i).getUsername();
            String name = userService.findUserByUsername(username).getName();
            awardList.get(i).setUsername(name);
        }
        Collections.sort(awardList, new Comparator<Award>() {
            @Override
            public int compare(Award o1, Award o2) {
                return Integer.compare(o2.getNumber(), o1.getNumber());
            }
        });
        String winner = "";
        boolean isFirst = true;
        for (int i = 0; i < awardList.size(); i++) {
            if(i!=awardList.size()-1&&awardList.get(i).getNumber() == awardList.get(i+1).getNumber()){
                if(isFirst) winner += "【";
                winner+=awardList.get(i).getUsername();
                winner+="、";
                isFirst=false;
            }
            else if(i>0&&awardList.get(i).getNumber() == awardList.get(i-1).getNumber()){
                winner+=awardList.get(i).getUsername();
                winner+="】";
                isFirst=true;
                if(i!=awardList.size()-1) winner+="、";
            }else if(i!=awardList.size()-1){
                winner+=awardList.get(i).getUsername();
                winner+="、";
            }else{
                winner+=awardList.get(i).getUsername();
            }
        }
        return winner;
    }

    /**
     * 根据比赛id和比赛类型查找该比赛已经有几个人获奖
     * @param id
     * @param type
     * @return
     */
    public int getNumberByIdAndType(int id,String type){
        return awardMapper.selectNumberByIdAndType(id, type);
    }

    /**
     * 插入一条获奖记录
     * @param award
     */
    public void insertAward(Award award){
        awardMapper.insertAward(award);
    }

    /**
     * 获取比赛总获奖次数
     * @return
     */
    public int getCountAward(){
        return awardMapper.selectCountAward();
    }

    /**
     * 获得比赛获奖人次
     * @return
     */
    public int getCountPersonTimes(){
        return awardMapper.selectCountRenci();
    }

    /**
     * 根据学号username获取该用户所有获奖记录
     * @param username
     * @return
     */
    public List<Award> getAwardByUsername(String username){
        return awardMapper.selectAwardByUsername(username);
    }

    /**
     * 根据比赛id和第几个获奖number获取获奖人员的学号
     * @param id
     * @param number
     * @return List<String>
     */
    public List<String> getAwardsByIdAndNumber(int id, int number){
        return awardMapper.selectAwardsByIdAndNumber(id, number);
    }


}
