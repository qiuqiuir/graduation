package com.cslg.graduation;

import com.cslg.graduation.dao.ContestMapper;
import com.cslg.graduation.dao.ScoreMapper;
import com.cslg.graduation.dao.UserMapper;
import com.cslg.graduation.dao.WeekMapper;
import com.cslg.graduation.entity.Contest;
import com.cslg.graduation.entity.Score;
import com.cslg.graduation.entity.User;
import com.cslg.graduation.entity.Week;
import com.cslg.graduation.service.ContestService;
import com.cslg.graduation.service.ScoreService;
import com.cslg.graduation.service.UserService;
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

    @Test
    public void testUserFind() {

        User user = userMapper.selectByUsername("test");
        System.out.println(user);

        System.out.println("--------------");
        List<String> userList = userMapper.selectIsScoreUsers();

        for (String user1 : userList) {
            System.out.println(user1);
        }

        System.out.println("--------------");

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

        List<Date> timeList = weekMapper.selectAllTime();

        for (Date date : timeList) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            System.out.print(calendar.get(Calendar.YEAR) + " ");
            System.out.print(calendar.get(Calendar.MONTH) + 1 + " ");
            System.out.println(calendar.get(Calendar.DATE) + " ");
        }
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
//        Week week = new Week().setTime(new Date());
//        weekMapper.insertWeek(week);

        weekMapper.updateCount(new Date(2023-1900, 2,8),6);
        weekMapper.updateSum(new Date(2023-1900, 2,8),18.6);
        weekMapper.updateAvg(new Date(2023-1900, 2,8));

    }

}
