package com.cslg.graduation.controller;

import com.cslg.graduation.dao.ContestMapper;
import com.cslg.graduation.entity.Contest;
import com.cslg.graduation.service.ContestService;
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
@Controller
@RequestMapping("/contest")
@CrossOrigin(origins = "http://localhost:5173")
public class ContestController {

    @Autowired
    private ContestService contestService;

    /**
     * 获取所有比赛获奖记录
     * @return
     */
    @RequestMapping("/getAll")
    @ResponseBody
    public List<Contest> getAll(){
        return contestService.getAllContest();
    }

    /**
     * 传入一场比赛，添加
     * @param contest
     */
    @PostMapping("/addContest")
    public void addContest(@RequestBody Contest contest){
        contestService.addContest(contest);
    }

}
