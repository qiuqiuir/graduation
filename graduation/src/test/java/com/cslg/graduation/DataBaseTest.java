package com.cslg.graduation;

import com.cslg.graduation.dao.*;
import com.cslg.graduation.entity.*;
import com.cslg.graduation.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @auther xurou
 * @date 2023/3/29
 */

@SpringBootTest
public class DataBaseTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ScoreMapper scoreMapper;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private ContestMapper contestMapper;

    @Autowired
    private ContestService contestService;

    @Autowired
    private WeekMapper weekMapper;

    @Autowired
    private WeekService weekService;

    @Autowired
    private OjMapper ojMapper;

    @Autowired
    private SpiderService spiderService;

    @Test
    public void testUser() {

//        User user = userMapper.selectByUsername("test");
//        System.out.println(user);

//        System.out.println("--------------");
//        List<String> userList = userMapper.selectIsScoreUsers();
//
//        for (String user1 : userList) {
//            System.out.println(user1);
//        }
//
//        System.out.println("--------------");

        User user = new User()
                .setUsername("Z09422125")
                .setName("杨斌")
                .setPassword("e10adc3949ba59abbe56e057f20f883e");

        userMapper.insertUser(user);

    }

    @Test
    public void testScore() {

//        double val = scoreMapper.findDailyScoreByUsername("test",new Date(2023-1900,2,29));
//        System.out.println(val);

//        scoreService.addScore("test",new Date(2023-1900,2,30), 52.1);

//        scoreMapper.updateDailyScore("test",new Date(2023-1900,2,29), 3.2);
//        scoreMapper.updateTotalScore("test",new Date(2023-1900,2,29), 9.2);

//        Score score = scoreMapper.selectScoreByUsernameTime("test", new Date(2023-1900,2,8));
//        System.out.println(score);
//        scoreMapper.updateTotalScore("test",new Date(), 9.2);

//        scoreMapper.updateTotalScore("test",new Date(2023-1900,2,12),1.1);

//        List<Date> timeList = weekMapper.selectAllTime();
//
//        for (Date date : timeList) {
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(date);
//            System.out.print(calendar.get(Calendar.YEAR) + " ");
//            System.out.print(calendar.get(Calendar.MONTH) + 1 + " ");
//            System.out.println(calendar.get(Calendar.DATE) + " ");
//        }


        double score = spiderService.getNowcoderScore("679852081","53378");
        scoreService.addScore("020619203",new Date(2023-1900,2,19),score);
//        System.out.println(scoreService.getNumByTime(new Date(2022-1900,2,19)));
    }

    @Test
    public void testContest() {

        List<Contest> contestList = contestMapper.selectAllContest();
        for (Contest contest : contestList) {
            System.out.println(contest);
        }

        Contest contest = new Contest().setUsername("test2")
                .setName("ICPC")
                .setLevel("国家级")
                .setYear(2022)
                .setType("三等奖");

        contestService.addContest(contest);
    }

    @Test
    public void testWeek(){
        Week week = new Week().setTime(new Date(2022-1900,2,19));
        weekService.addWeek(week);

//        weekMapper.updateCount(new Date(2023-1900, 2,8),6);
//        weekMapper.updateSum(new Date(2023-1900, 2,8),18.6);
//        weekMapper.updateAvg(new Date(2023-1900, 2,8));

    }


    @Test
    public void testOj(){
//        Oj oj = new Oj()
//                .setOjId("qiuqiur")
//                .setUsername("093119134")
//                .setPlatform("codeforces");
//
//        ojMapper.insertOj(oj);

//        List<String> list = ojMapper.selectListOjId("093119134","codeforces");
//        for(String s:list){
//            System.out.println(s);
//        }

    }

}
