package com.cslg.graduation.controller;

import com.cslg.graduation.entity.Week;
import com.cslg.graduation.service.WeekService;
import com.cslg.graduation.util.GraduationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @auther xurou
 * @date 2023/3/30
 */

@RestController
@RequestMapping("/week")
@CrossOrigin(origins = "http://localhost:5173")
public class WeekController {

    @Autowired
    WeekService weekService;

    /**
     * 新增一场周赛
     * @param week
     */
    @PostMapping("/addWeek")
    public void addWeek(@RequestBody Week week){
        System.out.println("1");
        Date time = GraduationUtil.changeTime(week.getTime());
        week.setTime(time);
        weekService.addWeek(week);
        System.out.println("end");
    }

    @ResponseBody
    @RequestMapping("/getAllWeek")
    public List<Week> getAllWeek(){
        return weekService.getAllWeek();
    }
}
