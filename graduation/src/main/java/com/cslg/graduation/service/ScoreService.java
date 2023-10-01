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
    private WeekService weekService;


    /**
     * 获取username总积分
     *
     * @param username 学号
     * @return 返回总积分
     */
    public double getTotalScoreByUsername(String username) {
        return scoreMapper.findTotalScoreByUsername(username);
    }

    /**
     * 新增username在某天的分数
     *
     * @param score 积分
     */
    public void addScore(Score score) {
        scoreMapper.insertScore(score);
    }

    /**
     * 更改username某天积分
     *
     * @param username   学号
     * @param time       时间
     * @param dailyScore 当场周赛的分数
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
     * @param week 某场周赛的信息
     */
    public void addWeekScore(Week week) {
        // 爬虫获取本场比赛所有用户的分数
        Map<String, Double> rankData = new HashMap<>();
        if (week.getPlatform().equals("nowcoder")) {
            rankData = spiderService.getNowcoderScore(week.getContestId());
        } else if (week.getPlatform().equals("atcoder")) {
            rankData = spiderService.getAtcoderScore(week.getContestId());
        }
        System.out.println("本场周赛所有数据计算完毕");
        // 所有用户本周总积分，用于计算排名
        for (Map.Entry<String, Double> entry : rankData.entrySet()) {
            String username = entry.getKey();
            Double dailyScore = entry.getValue();
            double totalScore = getTotalScoreByUsername(username);
            Score score = new Score()
                    .setUsername(username)
                    .setTime(week.getTime())
                    .setDailyScore(dailyScore)
                    .setTotalScore(dailyScore + totalScore);
            addScore(score);
        }
//        List<Score> scoreList = new ArrayList<>();
//        for (User user : usernameList) {
//            String username = user.getUsername();
//            // 该用户的积分
//            double scorevalue = 0.0;
//            // 该用户在该平台的所有id
//            List<String> ojIdlist = ojService.getAllOjId(username, week.getPlatform());
//            for (String ojId : ojIdlist) {
//                for (Map.Entry<String, Double> entry : rankData.entrySet()) {
//                    if (entry.getKey().equals(ojId)) {
//                        double nowScore = entry.getValue();
//                        if (nowScore > scorevalue) scorevalue = nowScore;
//                    }
//                }
//            }
//            // 添加积分到list
//            double totalScore = getTotalScoreByUsername(username);
//            Score score = new Score()
//                    .setUsername(username)
//                    .setTime(week.getTime())
//                    .setDailyScore(scorevalue)
//                    .setTotalScore(scorevalue + totalScore);
//            addScore(score);
//        }

    }

    /**
     * 返回time时间参加周赛的人数
     *
     * @param time 时间
     * @return 参加该时间周赛的人数
     */
    public int getNumByTime(Date time) {
        return scoreMapper.selectCountByTime(time);
    }

    /**
     * 返回time时间周赛的总积分
     *
     * @param time 时间
     * @return 该时间周赛的总积分
     */
    public double getSumByTime(Date time) {
        return scoreMapper.selectSumByTime(time);
    }

    /**
     * 获取学号为username的队员在time的积分
     *
     * @param username 学号
     * @param time     时间
     * @return 当场周赛的积分
     */
    public double getDailyScoreByUsernameAndTime(String username, Date time) {
        Score score = getScore(username, time);
        if (score == null) return 0;
        return score.getDailyScore();
    }

    /**
     * 获取学号为username的队员在time的排名
     *
     * @param username 学号
     * @param time     时间
     * @return 当场周赛的排名
     */
    public int getRankByUsernameAndTime(String username, Date time) {
        Score score = getScore(username, time);
        return score.getRank();
    }

    /**
     * 根据学号和时间获取某个人某场周赛情况
     *
     * @param username 学号
     * @param time     时间
     * @return 返回Score
     */
    public Score getScore(String username, Date time) {
        return scoreMapper.selectScoreByUsernameAndTime(username, time);
    }

    /**
     * 更新周赛排名
     *
     * @param username 学号
     * @param time     时间
     * @param rank     排名
     */
    public void updateRank(String username, Date time, int rank) {
        scoreMapper.updateRank(username, time, rank);
    }

    /**
     * 获取某个时间所有积分
     *
     * @param time 时间
     * @return 该场周赛所有积分
     */
    public List<Score> getScoresByTime(Date time) {
        return scoreMapper.selectScoresByTime(time);
    }

    /**
     * 根据时间更新周赛排名
     *
     * @param time 时间
     */
    public void updateWeekRank(Date time) {
        List<Score> scoreList = getScoresByTime(time);
        scoreList.sort(new Comparator<Score>() {
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

    /**
     * 获取时间为time的周赛的最高积分
     *
     * @param time 时间
     * @return 该场周赛最高积分
     */
    public Double getMaxDailyScore(Date time) {
        List<Score> scoreList = getScoresByTime(time);
        double maxScore = 0;
        for (Score score : scoreList) {
            maxScore = Math.max(maxScore, score.getDailyScore());
        }
        return maxScore;
    }

    public List<Score> getScoresByUsername(String username) {
        return scoreMapper.selectScoresByUsername(username);
    }


}
