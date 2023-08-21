package com.cslg.graduation.controller;

import com.cslg.graduation.common.ResponseService;
import com.cslg.graduation.entity.User;
import com.cslg.graduation.entity.Week;
import com.cslg.graduation.service.WeekService;
import com.cslg.graduation.util.GraduationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @RequestMapping("/getAllWeek")
    public ResponseService getAllWeek(){
        List<Week> weekList = weekService.getAllWeek();
        return ResponseService.createBySuccess(weekList);
    }

    @RequestMapping("/getLegalWeek")
    public ResponseService getLegalWeek(){
        List<Week> weekList = weekService.getLegalAllWeek();
        return ResponseService.createBySuccess(weekList);
    }

    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping("/updateLegalWeek")
    public ResponseService updateLegalWeek(){
        weekService.updateLegalWeek();
        return ResponseService.createBySuccess();
    }

    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping("/reloadScore")
    public ResponseService deleteLegalWeek(){
        weekService.reloadScore();
        return ResponseService.createBySuccess();
    }

    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping("/gongjia")
    public ResponseService gongjia() throws ParseException {
        List<Week> weekList = weekService.getLegalAllWeek();
        Collections.sort(weekList, new Comparator<Week>() {
            @Override
            public int compare(Week o1, Week o2) {
                return o2.getTime().compareTo(o1.getTime());
            }
        });

        List<Map<String, String>> users = weekService.getNote(weekList.get(0).getTime());

        return ResponseService.createBySuccess(users);
    }

}
