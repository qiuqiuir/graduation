package com.cslg.graduation.controller;

import com.cslg.graduation.common.ResponseService;
import com.cslg.graduation.entity.*;
import com.cslg.graduation.service.AwardService;
import com.cslg.graduation.service.ContestService;
import com.cslg.graduation.service.UserService;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @auther xurou
 * @date 2023/4/13
 */

@RestController
@RequestMapping("/award")
//@CrossOrigin(origins = "http://localhost:5173")
public class AwardController {

    @Autowired
    private AwardService awardService;

    @Autowired
    private ContestService contestService;

    @Autowired
    private UserService userService;

    /**
     * 获取所有获奖记录
     *
     * @return
     */
    @RequestMapping("/getAllAward")
    public ResponseService getAll() {
        List<Contest> contestList = contestService.getAllContest();
        // 时间从近到远排序
        Collections.sort(contestList, new Comparator<Contest>() {
            @Override
            public int compare(Contest o1, Contest o2) {
                return o2.getTime().compareTo(o1.getTime());
            }
        });

        // 返回的json
        List<Map<String, Object>> result = new ArrayList<>();
        for (Contest contest : contestList) {
            Map<String, Object> map = new HashMap<>();

            // 该比赛的获奖信息
            List<Award> awardList = awardService.getAwardById(contest.getId());
            String name = contest.getLevel() + ":" + contest.getName();
            if (!StringUtils.isBlank(contest.getRemark())) {
                name = name + "(" + contest.getRemark() + ")";
            }
            map.put("label", name);
            map.put("timestamp", contest.getTime());
            map.put("level", contest.getLevel());
            List<Map<String, Object>> childrens = new ArrayList<>();
            boolean one = false, two = false, three = false;
            for (Award award : awardList) {
                if (award.getType().equals("一等奖")) one = true;
                if (award.getType().equals("二等奖")) two = true;
                if (award.getType().equals("三等奖")) three = true;
            }
            if (one) {
                Map<String, Object> firstPrize = new HashMap<>();
                int number = awardService.getNumberByIdAndType(contest.getId(), "一等奖");
                if (contest.getName().contains("ICPC") || contest.getName().contains("CCPC") || contest.getName().contains("江苏省赛")) {
                    firstPrize.put("label", "金奖(" + number + ")");
                } else firstPrize.put("label", "一等奖(" + number + ")");
                firstPrize.put("number", number);
                Map<String, Object> winners = new HashMap<>();
                winners.put("label", awardService.getAwardByIdAndType(contest.getId(), "一等奖"));
                List<Map<String, Object>> winnerList = new ArrayList<>();
                winnerList.add(winners);
                firstPrize.put("children", winnerList);
                childrens.add(firstPrize);
            }
            if (two) {
                Map<String, Object> firstPrize = new HashMap<>();
                int number = awardService.getNumberByIdAndType(contest.getId(), "二等奖");
                if (contest.getName().contains("ICPC") || contest.getName().contains("CCPC") || contest.getName().contains("江苏省赛")) {
                    firstPrize.put("label", "银奖(" + number + ")");
                } else firstPrize.put("label", "二等奖(" + number + ")");
                firstPrize.put("number", number);
                Map<String, Object> winners = new HashMap<>();
                winners.put("label", awardService.getAwardByIdAndType(contest.getId(), "二等奖"));
                List<Map<String, Object>> winnerList = new ArrayList<>();
                winnerList.add(winners);
                firstPrize.put("children", winnerList);
                childrens.add(firstPrize);
            }
            if (three) {
                Map<String, Object> firstPrize = new HashMap<>();
                int number = awardService.getNumberByIdAndType(contest.getId(), "三等奖");
                if (contest.getName().contains("ICPC") || contest.getName().contains("CCPC") || contest.getName().contains("江苏省赛")) {
                    firstPrize.put("label", "铜奖(" + number + ")");
                } else firstPrize.put("label", "三等奖(" + number + ")");
                firstPrize.put("number", number);
                Map<String, Object> winners = new HashMap<>();
                winners.put("label", awardService.getAwardByIdAndType(contest.getId(), "三等奖"));
                List<Map<String, Object>> winnerList = new ArrayList<>();
                winnerList.add(winners);
                firstPrize.put("children", winnerList);
                childrens.add(firstPrize);
            }
            map.put("children", childrens);
            List<Map<String, Object>> shuju = new ArrayList<>();
            shuju.add(map);
            Map<String, Object> xinxi = new HashMap<>();
            xinxi.put("xinxi", shuju);
            result.add(xinxi);
        }

        return ResponseService.createBySuccess(result);
    }

    /**
     * 新增一场获奖信息
     *
     * @param awardTeam
     * @return
     */
    @PostMapping("/addAward")
    public ResponseService addContest(@RequestBody AwardTeam awardTeam) {
        int number = awardService.getNumberByIdAndType(awardTeam.getId(), awardTeam.getType());
        Award award = new Award()
                .setContestId(awardTeam.getId())
                .setType(awardTeam.getType())
                .setNumber(number + 1);
        boolean individual = contestService.getNumberById(awardTeam.getId()) == 1;
        for (String username : awardTeam.getTeam()) {
            award = award.setUsername(username);
            awardService.insertAward(award);
            if (individual) award.setNumber(award.getNumber() + 1);
        }
        return ResponseService.createBySuccess();
    }

    /**
     * 获取总获奖次数
     *
     * @return
     */
    @RequestMapping("/getCount")
    public ResponseService getCountAward() {
        int count = awardService.getCountAward();
        return ResponseService.createBySuccess(count);
    }

    /**
     * 获取总获奖人次
     *
     * @return
     */
    @RequestMapping("/getCountPersonTimes")
    public ResponseService getCountPersonTimes() {
        int count = awardService.getCountPersonTimes();
        return ResponseService.createBySuccess(count);
    }

    /**
     * 查询学号为username的所有获奖信息
     *
     * @param username
     * @return
     */
    @RequestMapping("/getAward/{username}")
    public ResponseService getAwardByUsername(@PathVariable("username") String username) {
        List<Award> awardList = awardService.getAwardByUsername(username);
        List<Map<String, Object>> list = new ArrayList<>();
        for (Award award : awardList) {
            Map<String, Object> map = new HashMap<>();
            Contest contest = contestService.getContestById(award.getContestId());
            String name = contest.getName();
            if (!StringUtils.isBlank(contest.getRemark())) {
                name = name + "(" + contest.getRemark() + ")";
            }
            map.put("name", name);
            map.put("time", contest.getTime());
            map.put("level", contest.getLevel() + award.getType());
            list.add(map);
        }
        Collections.sort(list, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                Date time1 = (Date) o1.get("time");
                Date time2 = (Date) o2.get("time");
                return time1.compareTo(time2);
            }
        });
        return ResponseService.createBySuccess(list);
    }

    /**
     * 所有获奖队伍按照成绩计算分数后排序
     *
     * @return
     */
    @RequestMapping("/getAwardTeamScore")
    public ResponseService getAwardTeammScore() {
        List<Award> awardList = awardService.getAllAward();
        Map<List<String>, Integer> map = new HashMap<>();
        for (Award award : awardList) {
            Contest contest = contestService.getContestById(award.getContestId());
            int people = contest.getNumber();
            if (people == 3) {
                List<String> usernames = awardService.getAwardsByIdAndNumber(award.getContestId(), award.getNumber());
                List<String> names = new ArrayList<>();
                for (String s : usernames) {
                    User user = userService.findUserByUsername(s);
                    names.add(user.getName());
                }
                if (usernames.size() == 3) {
                    int coefficient = 0;
                    if (award.getType().equals("一等奖")) coefficient = 3;
                    if (award.getType().equals("二等奖")) coefficient = 2;
                    if (award.getType().equals("三等奖")) coefficient = 1;
                    int score = 0;
                    if (contest.getLevel().equals("国家级")) coefficient *= 2;
                    if (contest.getName().contains("邀请赛")) {
                        score += coefficient * 300;
                    } else if (contest.getName().contains("CPC")) {
                        score += coefficient * 500;
                    } else if (contest.getName().contains("省赛")) {
                        score += coefficient * 200;
                    }
                    if (map.containsKey(names)) {
                        int last = map.get(names);
                        map.put(names, last + score);
                    } else map.put(names, score);
                }
            }
        }
        List<Map.Entry<List<String>, Integer>> list = new ArrayList<Map.Entry<List<String>, Integer>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<List<String>, Integer>>() {
            @Override
            public int compare(Map.Entry<List<String>, Integer> o1, Map.Entry<List<String>, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        return ResponseService.createBySuccess(list);
    }

    /**
     * 所有组队获奖记录
     *
     * @return
     */
    @RequestMapping("/getAwardTeam")
    public ResponseService getAwardTeam() {
        List<Award> awardList = awardService.getAllAward();
        List<AwardTeam> awardTeams = new ArrayList<>();
        Map<List<String>, Integer> map = new HashMap<>();
        for (Award award : awardList) {

            Contest contest = contestService.getContestById(award.getContestId());
            int people = contest.getNumber();
            if (people == 3) {
                List<String> usernames = awardService.getAwardsByIdAndNumber(award.getContestId(), award.getNumber());
                Collections.sort(usernames, new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return o2.compareTo(o1);
                    }
                });
                List<String> names = new ArrayList<>();
                for (String s : usernames) {
                    User user = userService.findUserByUsername(s);
                    names.add(user.getName());
                }
                if(map.containsKey(names)) continue;
                map.put(names,1);
                if (usernames.size() == 3) {
                    String contestName = contest.getLevel() + ":" + contest.getName();
                    if (contest.getRemark().length() > 0) contestName += "(" + contest.getRemark() + ")";
                    if(names.get(0).equals("徐柔")&&names.get(1).equals("张雷")&&names.get(2).equals("詹智航")){
                        contestName+=",国家级:第7届CCPC(威海),省级:第6届江苏省赛";
                    }
                    AwardTeam awardTeam = new AwardTeam()
                            .setTeam(names)
                            .setType(contestName);
                    awardTeams.add(awardTeam);
                }
            }
        }
        List<User> userList = userService.getAllUsers();
        for(int j=0;j<19;j++) {
            List<String> usernames = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                int idx = new Random().nextInt(userList.size());
                while(!usernames.isEmpty()&&userList.get(idx).getUsername().equals(usernames.get(usernames.size()-1))){
                    idx = new Random().nextInt(userList.size());
                }
                usernames.add(userList.get(idx).getUsername());
            }
            Collections.sort(usernames, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o2.compareTo(o1);
                }
            });
            List<String> names = new ArrayList<>();
            for (String s : usernames) {
                User user = userService.findUserByUsername(s);
                names.add(user.getName());
            }
            AwardTeam awardTeam = new AwardTeam()
                    .setTeam(names);
            awardTeams.add(awardTeam);
        }
        Collections.sort(awardTeams, new Comparator<AwardTeam>() {
            @Override
            public int compare(AwardTeam o1, AwardTeam o2) {
                return Math.random()<0.5?1:-1;
            }
        });

        return ResponseService.createBySuccess(awardTeams);
    }

    /**
     * 获取每个奖项第一次获奖的人员信息
     *
     * @return
     */
    @RequestMapping("/AwardFist")
    public ResponseService getAwardFirst() {
        Map<String, Object> result = new HashMap<>();
        List<Contest> contestList = contestService.getAllContest();
        Collections.sort(contestList, new Comparator<Contest>() {
            @Override
            public int compare(Contest o1, Contest o2) {
                return o1.getTime().compareTo(o2.getTime());
            }
        });
        List<String> contestName = new ArrayList<>();
        contestName.add("蓝桥杯");
        contestName.add("ICPC邀请赛");
        contestName.add("CCPC邀请赛");
        contestName.add("ICPC");
        contestName.add("CCPC");
        contestName.add("江苏省赛");
        contestName.add("robocom");
        contestName.add("天梯赛");

        for (String name : contestName) {
            Map<String, List<FirstAward>> map = awardService.getFirstAwardByName(name, contestList);
            result.put(name, map);
        }
        return ResponseService.createBySuccess(result);
    }


}