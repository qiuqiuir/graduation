package com.cslg.graduation.service;

import com.cslg.graduation.dao.ScoreMapper;
import com.cslg.graduation.entity.Score;
import com.cslg.graduation.entity.User;
import com.cslg.graduation.entity.Week;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @auther xurou
 * @date 2023/3/29
 */

@Service
public class ScoreService {

    @Autowired
    private ScoreMapper scoreMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private SpiderService spiderService;

    @Autowired
    private OjService ojService;

    @Autowired
    private WeekService weekService;


    /**
     * 获取username总积分
     *
     * @param username
     * @return
     */
    public double getTotalScoreByUsername(String username) {
        return scoreMapper.findTotalScoreByUsername(username);
    }

    /**
     * 新增username在某天的分数
     *
     * @param score
     */
    public void addScore(Score score) {
        scoreMapper.insertScore(score);
    }

    /**
     * 更改username某天积分
     *
     * @param username
     * @param time
     * @param dailyScore
     */
    public void updateScore(String username, Date time, double dailyScore) {
        //计算差值
        double difference = dailyScore - scoreMapper.findDailyScoreByUsername(username, time);
        // 更新当天积分
        scoreMapper.updateDailyScore(username, time, dailyScore);
        // 查找time之后的比赛日
        List<Week> allWeek = weekService.getLegalWeek(time);
        List<Date> timeList = new ArrayList<>();
        for (Week week : allWeek) {
            timeList.add(week.getTime());
        }
        // 更新所有比赛日
        for (Date date : timeList) {
            double totalScore = getScore(username, date).getTotalScore() + difference;
            // 更新该用户的总积分
            scoreMapper.updateTotalScore(username, date, totalScore);
            // 更新该周积分情况
            weekService.updateNumSumAvgByTime(date);
        }
    }

    /**
     * 根据周赛信息计算所有队员的积分
     *
     * @param week
     */
    public void addWeekScore(Week week) {
        // 所有计算积分的用户
        List<User> usernameList = userService.getAllUsers();
        // 爬虫获取本场比赛所有用户的分数
        Map<String, Double> rankData = new HashMap<>();
        if (week.getPlatform().equals("nowcoder")) {
            rankData = spiderService.getNowcoderScore(week.getContestId());
        } else if (week.getPlatform().equals("atcoder")) {
            rankData = spiderService.getAtcoderScore(week.getContestId());
        }
        System.out.println("本场周赛所有数据计算完毕");
        // 所有用户本周总积分，用于计算排名
        List<Score> scoreList = new ArrayList<>();
        for (User user : usernameList) {
            String username = user.getUsername();
            // 该用户的积分
            double scorevalue = 0.0;
            // 该用户在该平台的所有id
            List<String> ojIdlist = ojService.getAllOjId(username, week.getPlatform());
            for (String ojId : ojIdlist) {
                for (Map.Entry<String, Double> entry : rankData.entrySet()) {
                    if (entry.getKey().equals(ojId)) {
                        double nowScore = entry.getValue();
                        if (nowScore > scorevalue) scorevalue = nowScore;
                    }
                }
            }
            // 添加积分到list
            double totalScore = getTotalScoreByUsername(username);
            Score score = new Score()
                    .setUsername(username)
                    .setTime(week.getTime())
                    .setDailyScore(scorevalue)
                    .setTotalScore(scorevalue + totalScore);
            addScore(score);
        }

    }

    /**
     * 返回time时间参加周赛的人数
     *
     * @param time
     * @return
     */
    public int getNumByTime(Date time) {
        return scoreMapper.selectCountByTime(time);
    }

    /**
     * 返回time时间周赛的总积分
     *
     * @param time
     * @return
     */
    public double getSumByTime(Date time) {
        return scoreMapper.selectSumByTime(time);
    }

    /**
     * 获取学号为username的队员在time的积分
     *
     * @param username
     * @param time
     * @return
     */
    public double getDailyScoreByUsernameAndTime(String username, Date time) {
        Score score = getScore(username, time);
        if (score == null) return 0;
        return score.getDailyScore();
    }

    /**
     * 获取学号为username的队员在time的排名
     *
     * @param username
     * @param time
     * @return
     */
    public int getRankByUsernameAndTime(String username, Date time) {
        Score score = getScore(username, time);
        return score.getRank();
    }

    /**
     * 根据学号和时间获取某个人某场周赛情况
     * @param username
     * @param time
     * @return
     */
    public Score getScore(String username, Date time) {
        return scoreMapper.selectScoreByUsernameAndTime(username, time);
    }

    /**
     * 更新周赛排名
     * @param username
     * @param time
     * @param rank
     */
    public void updateRank(String username, Date time, int rank) {
        scoreMapper.updateRank(username, time, rank);
    }

    /**
     * 获取某个时间所有积分
     * @param time
     * @return
     */
    public List<Score> getScoresByTime(Date time) {
        return scoreMapper.selectScoresByTime(time);
    }

    /**
     * 根据时间更新周赛排名
     * @param time
     */
    public void updateWeekRank(Date time) {
        List<Score> scoreList = getScoresByTime(time);
        Collections.sort(scoreList, new Comparator<Score>() {
            @Override
            public int compare(Score o1, Score o2) {
                return Double.compare(o2.getTotalScore(), o1.getTotalScore());
            }
        });
        int last = 1;
        for (int i = 0; i < scoreList.size(); i++) {
            Score nowScore = scoreList.get(i);
            if (i != 0 && nowScore.getTotalScore() == scoreList.get(i - 1).getTotalScore()) {
                updateRank(nowScore.getUsername(), time, last);
                //nowScore = nowScore.setRank(last);
            } else {
                updateRank(nowScore.getUsername(), time, i + 1);
                last = i + 1;
            }
            // addScore(nowScore);
        }
    }




}
