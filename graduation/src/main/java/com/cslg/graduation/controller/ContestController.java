package com.cslg.graduation.controller;

import com.cslg.graduation.common.ResponseService;
import com.cslg.graduation.dao.ContestMapper;
import com.cslg.graduation.entity.Contest;
import com.cslg.graduation.service.ContestService;
import com.cslg.graduation.service.UserService;
import com.cslg.graduation.util.GraduationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    /**
     * 获取所有比赛获奖记录
     * @return
     */
    @RequestMapping("/getAll")
    @ResponseBody
    public ResponseService getAll(){
        List<Contest> contestList = contestService.getAllContest();
        for(int i=0;i<contestList.size();i++){
            String username = contestList.get(i).getUsername();
            String name = userService.findUserByUsername(username).getName();
            contestList.get(i).setUsername(name);
        }
        return ResponseService.createBySuccess(contestList);
    }

    /**
     * 传入一场比赛，添加
     * @param contest
     */
    @PostMapping("/addContest")
    public ResponseService addContest(@RequestBody Contest contest){
        contestService.addContest(contest);
        return ResponseService.createBySuccess();
    }

}
