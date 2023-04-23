package com.cslg.graduation;

import com.cslg.graduation.dao.*;
import com.cslg.graduation.entity.*;
import com.cslg.graduation.service.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    @Autowired
    private NewsMapper newsMapper;

    @Autowired
    private TrainingMapper trainingMapper;

    @Autowired
    private AwardService awardService;

    @Autowired
    private AcnumberService acnumberService;

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
                .setUsername("093119134")
                .setName("徐柔")
                .setMajor("数据科学与大数据技术")
                .setEmail("1143853098@qq.com")
                .setGender("女")
                .setIdentityCard("350102200111151624")
                .setClothingSize("M");
        userService.updateUser(user);
//
//        userMapper.insertUser(user);
//        System.out.println(userService.findUserByUsername("093119134"));

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


//        System.out.println(scoreService.getNumByTime(new Date(2022-1900,2,19)));
        System.out.println(scoreService.getDailyScoreByUsernameAndTime("040722109", new Date(2023 - 1900, 1, 24)));
    }

    @Test
    public void testContest() {

        List<Contest> contestList = contestService.getAllContest();
        for (Contest contest : contestList) {
            System.out.println(contest);
            System.out.println(contest.getRemark());
            System.out.println(contest.getRemark() == null);
            System.out.println(StringUtils.isBlank(contest.getRemark()));
        }
//
//        Contest contest = new Contest().setUsername("test2")
//                .setName("ICPC")
//                .setLevel("国家级")
//                .setYear(2022)
//                .setType("三等奖");
//
//        contestService.addContest(contest);
    }

    @Test
    public void testWeek() {
//        Week week = new Week().setTime(new Date(2022-1900,2,19));
//        weekService.addWeek(week);
        List<Week> weekList = weekMapper.selectAllWeek();
        for (Week week : weekList) {
            System.out.println(week);
        }

//        weekMapper.updateCount(new Date(2023-1900, 2,8),6);
//        weekMapper.updateSum(new Date(2023-1900, 2,8),18.6);
//        weekMapper.updateAvg(new Date(2023-1900, 2,8));

    }


    @Test
    public void testOj() {
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
        List<Oj> list = ojMapper.selectListOj("093119134");
        for (Oj oj : list) {
            System.out.println(oj);
        }

    }

    @Test
    public void testNews() {
//        News news = new News()
//                .setTime(new Date(2022-1900,10,14))
//                .setTitle("集训队学子在第47届国际大学生程序设计竞赛亚洲区域赛（西安）获铜奖")
//                .setUrl("http://acm.cse.cslg.cn/archives/2653");
//
//        newsMapper.insertNews(news);
        List<News> newsList = newsMapper.selectAllNews();
        for (News news : newsList) {
            System.out.println(news);
        }

    }


    @Test
    public void testTraining() {
//        Training training = new Training()
//                .setPlatform("牛客")
//                .setTitle("数学")
//                .setUrl("https://ac.nowcoder.com/courses/cover/live/731");
//
//        trainingMapper.insertTraining(training);
//        List<Training> newsList= trainingMapper.selectAllTraining();
//        for(Training news:newsList) {
//            System.out.println(news);
//        }
        List<User> userList = userService.getAllUsers();
        for (User user : userList) {
            Acnumber acnumber = new Acnumber()
                    .setUsername(user.getUsername())
                    .setCount(0);
            acnumber.setPlatform("nowcoder");
            acnumberService.addAcnumber(acnumber);
            acnumber.setPlatform("codeforces");
            acnumberService.addAcnumber(acnumber);
            acnumber.setPlatform("luogu");
            acnumberService.addAcnumber(acnumber);
            acnumber.setPlatform("atcoder");
            acnumberService.addAcnumber(acnumber);
            acnumber.setPlatform("vjudge");
            acnumberService.addAcnumber(acnumber);
        }

    }

    @Test
    public void award() {
        awardService.getAwardByIdAndType(120, "三等奖");
    }
}
