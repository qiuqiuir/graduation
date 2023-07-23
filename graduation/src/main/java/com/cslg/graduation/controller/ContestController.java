package com.cslg.graduation.controller;

import com.cslg.graduation.common.ResponseService;
import com.cslg.graduation.entity.Award;
import com.cslg.graduation.entity.Contest;
import com.cslg.graduation.service.AwardService;
import com.cslg.graduation.service.ContestService;
import com.cslg.graduation.service.UserService;
import com.cslg.graduation.util.GraduationUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @auther xurou
 * @date 2023/3/30
 */
@RestController
@RequestMapping("/contest")
//@CrossOrigin(origins = "http://localhost:5173")
public class ContestController {

    @Autowired
    private ContestService contestService;

    @Autowired
    private UserService userService;

    @Autowired
    private AwardService awardService;

    /**
     * 获取所有比赛
     * @return
     */
    @RequestMapping("/getAllContest")
    public ResponseService getAllContest(){
        List<Contest> contestList = contestService.getAllContest();
        Collections.sort(contestList, new Comparator<Contest>() {
            @Override
            public int compare(Contest o1, Contest o2) {
                return o2.getTime().compareTo(o1.getTime());
            }
        });
        return ResponseService.createBySuccess(contestList);
    }

    @RequestMapping("/getNearlyYearContest")
    public ResponseService getNearlyYearContest(){
        List<Contest> contestList = contestService.getAllContest();
        List<Contest> result = new ArrayList<>();
        Date date = new Date();
        for(Contest contest:contestList){
            Date contestData = contest.getTime();
            long millisecond = date.getTime() - contestData.getTime();
            double day = millisecond/(1000*60*60*24);
            if(day<180){
                result.add(contest);
            }
        }
        Collections.sort(result, new Comparator<Contest>() {
            @Override
            public int compare(Contest o1, Contest o2) {
                return o2.getTime().compareTo(o1.getTime());
            }
        });
        return ResponseService.createBySuccess(result);
    }

    /**
     * 传入一场比赛，添加
     * @param contest
     */
    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/addContest")
    public ResponseService addContest(@RequestBody Contest contest){
        contest.setTime(GraduationUtil.changeTime(contest.getTime()));
        contestService.addContest(contest);
        return ResponseService.createBySuccess();
    }

    /**
     * 获得参加比赛次数
     * @return
     */
    @RequestMapping("/getCount")
    public ResponseService getCount(){
        int count = contestService.getCountContest();
        return ResponseService.createBySuccess(count);
    }

//    @RequestMapping("/getTeam")
//    public ResponseService getTeam(){
//        List<Contest> contestList = contestService.getAllContest();
//        List<Contest> team = new ArrayList<>();
//        for(Contest contest : contestList){
//            if(contest.getNumber() == 3){
//                team.add(contest);
//            }
//        }
//        return ResponseService.createBySuccess(team);
//    }



}
