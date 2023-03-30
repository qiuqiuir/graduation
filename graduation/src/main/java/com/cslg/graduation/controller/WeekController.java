package com.cslg.graduation.controller;

import com.cslg.graduation.entity.Week;
import com.cslg.graduation.service.WeekService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        weekService.addWeek(week);
    }
}
