package com.cslg.graduation;

import com.cslg.graduation.entity.Week;
import com.cslg.graduation.service.ScoreService;
import com.cslg.graduation.service.WeekService;
import com.cslg.graduation.util.GraduationUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

/**
 * @auther xurou
 * @date 2023/3/31
 */
@SpringBootTest
public class ScoreTest {

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private WeekService weekService;

    @Test
    public void testaddScore() {

//        Week week = new Week()
//                .setTime(new Date(2022 - 1900, 2, 19))
//                .setPlatform("nowcoder")
//                .setContestId("53378");
//        weekService.addWeek(week);
//        System.out.println(GraduationUtil.DateToString(new Date()));

    }

}
