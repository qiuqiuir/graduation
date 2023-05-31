package com.cslg.graduation.controller;

import com.cslg.graduation.common.ResponseService;
import com.cslg.graduation.entity.Week;
import com.cslg.graduation.service.WeekService;
import com.cslg.graduation.util.GraduationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/addWeek")
    public ResponseService addWeek(@RequestBody Week week){
        System.out.println("开始新增周赛");
        Date time = GraduationUtil.changeTime(week.getTime());
        week.setTime(time);
        weekService.addWeek(week);
        System.out.println("周赛数据统计完毕");
        return ResponseService.createBySuccess();
    }

    @ResponseBody
    @RequestMapping("/getAllWeek")
    public ResponseService getAllWeek(){
        List<Week> weekList = weekService.getAllWeek();
        return ResponseService.createBySuccess(weekList);
    }
}
