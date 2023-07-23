package com.cslg.graduation.controller;

import com.cslg.graduation.common.ResponseService;
import com.cslg.graduation.entity.*;
import com.cslg.graduation.service.AwardService;
import com.cslg.graduation.service.ContestService;
import com.cslg.graduation.service.UserService;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
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

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


    @RequestMapping("/getAward")
    public ResponseService getAward() {
        List<Award> awardList = awardService.getAllAward();
        List<Map<String, Object>> result = new ArrayList<>();
        Set<String> exists = new HashSet<>();
        for (Award award : awardList) {
            Contest contest = contestService.getContestById(award.getContestId());
            Map<String, Object> map = new HashMap<>();
            String name = contest.getName();
            if (!StringUtils.isBlank(contest.getRemark())) {
                name = name + "(" + contest.getRemark() + ")";
            }

            String nowNumber = award.getContestId() + " " + award.getNumber();
            if (exists.contains(nowNumber)) continue;
            exists.add(nowNumber);
            List<String> usernames = awardService.getAwardsByIdAndNumberAndtype(award.getContestId(), award.getNumber(), award.getType());
            String awardName = userService.findUserByUsername(usernames.get(0)).getName();
            for (int i = 1; i < usernames.size(); i++) {
                awardName += ",";
                String s = userService.findUserByUsername(usernames.get(i)).getName();
                awardName += s;
            }
            map.put("name", name);
            map.put("time", sdf.format(contest.getTime()));
            map.put("teacher",award.getTeacher());
            map.put("people", awardName);
            map.put("level",contest.getLevel());
            map.put("type",award.getType());
            result.add(map);
        }
        Collections.sort(result, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                String time1 = (String) o1.get("time");
                String time2 = (String) o2.get("time");
                return time2.compareTo(time1);
            }
        });
        return ResponseService.createBySuccess(result);
    }

    /**
     * 获取所有获奖记录（用于时间线的j'son）
     *
     * @return
     */
    @RequestMapping("/getAllAward")
    public ResponseService getAllAward() {
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
            // 该比赛的一等奖
            if (one) {
                Map<String, Object> firstPrize = new HashMap<>();
                int number = awardService.getNumberById(contest.getId(), "一等奖");
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
            // 该比赛的二等奖
            if (two) {
                Map<String, Object> firstPrize = new HashMap<>();
                int number = awardService.getNumberById(contest.getId(), "二等奖");
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
            // 该比赛的三等奖
            if (three) {
                Map<String, Object> firstPrize = new HashMap<>();
                int number = awardService.getNumberById(contest.getId(), "三等奖");
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
    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/addAward")
    public ResponseService addAward(@RequestBody AwardTeam awardTeam) {
        int number = awardService.getNumberById(awardTeam.getId());
        Award award = new Award()
                .setContestId(awardTeam.getId())
                .setType(awardTeam.getType())
                .setNumber(number + 1)
                .setTeacher(awardTeam.getTeacher())
                .setUrl(awardTeam.getUrl());

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
                List<String> usernames = awardService.getAwardsByIdAndNumberAndtype(award.getContestId(), award.getNumber(), award.getType());
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
        Random random = new Random(20010409);
        List<Award> awardList = awardService.getAllAward();
        List<AwardTeam> awardTeams = new ArrayList<>();
        Map<List<String>, Integer> map = new HashMap<>();
        for (Award award : awardList) {

            Contest contest = contestService.getContestById(award.getContestId());
            int people = contest.getNumber();
            if (people == 3) {
                List<String> usernames = awardService.getAwardsByIdAndNumberAndtype(award.getContestId(), award.getNumber(), award.getType());
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
                if (map.containsKey(names)) continue;
                map.put(names, 1);
                if (usernames.size() == 3) {
                    String contestName = contest.getLevel() + ":" + contest.getName();
                    if (contest.getRemark().length() > 0) contestName += "(" + contest.getRemark() + ")";
                    if (names.get(0).equals("徐柔") && names.get(1).equals("张雷") && names.get(2).equals("詹智航")) {
                        contestName += ",国家级:第7届CCPC(威海),省级:第6届江苏省赛";
                    }
                    AwardTeam awardTeam = new AwardTeam()
                            .setTeam(names)
                            .setType(contestName);
                    awardTeams.add(awardTeam);
                }
            }
        }
        List<User> userList = userService.getACMer();
        for (int j = 0; j < 19; j++) {
            List<String> usernames = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                int idx = random.nextInt(userList.size());
                while (!usernames.isEmpty() && userList.get(idx).getUsername().equals(usernames.get(usernames.size() - 1))) {
                    idx = random.nextInt(userList.size());
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
                return random.nextDouble() < 0.5 ? 1 : -1;
            }
        });

        return ResponseService.createBySuccess(awardTeams);
    }

    /**
     * 获取每个奖项第一次获奖的人员信息
     *
     * @return
     */
    @RequestMapping("/AwardFirst")
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
        contestName.add("CCPC女生赛");
        for (String name : contestName) {
            Map<String, List<FirstAward>> map = awardService.getFirstAwardByName(name, contestList);
            result.put(name, map);
        }
        return ResponseService.createBySuccess(result);
    }

    @RequestMapping("/getAwardCCPC")
    public ResponseService getAwardCCPC() {
        List<Award> awardList = awardService.getAllAward();
        List<Map<String, Object>> result = new ArrayList<>();
        Set<String> exists = new HashSet<>();
        for (Award award : awardList) {
            Contest contest = contestService.getContestById(award.getContestId());
            int idx = contest.getName().indexOf("CCPC");
            if (idx + 4 == contest.getName().length()) {
                Map<String, Object> map = new HashMap<>();
                String name = contest.getName();
                if (!StringUtils.isBlank(contest.getRemark())) {
                    name = name + "(" + contest.getRemark() + ")";
                }
                String nowNumber = award.getContestId() + " " + award.getNumber();
                if (exists.contains(nowNumber)) continue;
                exists.add(nowNumber);
                List<String> usernames = awardService.getAwardsByIdAndNumberAndtype(award.getContestId(), award.getNumber(), award.getType());
                String awardName = userService.findUserByUsername(usernames.get(0)).getName();
                for (int i = 1; i < usernames.size(); i++) {
                    awardName += ",";
                    String s = userService.findUserByUsername(usernames.get(i)).getName();
                    awardName += s;
                }
                map.put("name", name);
                String s = sdf.format(contest.getTime());
                map.put("time", sdf.format(contest.getTime()));
                if (award.getType().equals("一等奖")) {
                    map.put("type", "金奖");
                }
                if (award.getType().equals("二等奖")) {
                    map.put("type", "银奖");
                }
                if (award.getType().equals("三等奖")) {
                    map.put("type", "铜奖");
                }
                map.put("people", awardName);
                result.add(map);
            }
        }
        Collections.sort(result, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                String time1 = (String) o1.get("time");
                String time2 = (String) o2.get("time");
                return time2.compareTo(time1);
            }
        });
        return ResponseService.createBySuccess(result);
    }

    @RequestMapping("/getAwardICPC")
    public ResponseService getAwardICPC() {
        List<Award> awardList = awardService.getAllAward();
        List<Map<String, Object>> result = new ArrayList<>();
        Set<String> exists = new HashSet<>();
        for (Award award : awardList) {
            Contest contest = contestService.getContestById(award.getContestId());
            int idx = contest.getName().indexOf("ICPC");
            if (idx + 4 == contest.getName().length()) {
                Map<String, Object> map = new HashMap<>();
                String name = contest.getName();
                if (!StringUtils.isBlank(contest.getRemark())) {
                    name = name + "(" + contest.getRemark() + ")";
                }
                String nowNumber = award.getContestId() + " " + award.getNumber();
                if (exists.contains(nowNumber)) continue;
                exists.add(nowNumber);
                List<String> usernames = awardService.getAwardsByIdAndNumberAndtype(award.getContestId(), award.getNumber(), award.getType());
                String awardName = userService.findUserByUsername(usernames.get(0)).getName();
                for (int i = 1; i < usernames.size(); i++) {
                    awardName += ",";
                    String s = userService.findUserByUsername(usernames.get(i)).getName();
                    awardName += s;
                }
                map.put("name", name);
                String s = sdf.format(contest.getTime());
                map.put("time", sdf.format(contest.getTime()));
                if (award.getType().equals("一等奖")) {
                    map.put("type", "金奖");
                }
                if (award.getType().equals("二等奖")) {
                    map.put("type", "银奖");
                }
                if (award.getType().equals("三等奖")) {
                    map.put("type", "铜奖");
                }
                map.put("people", awardName);
                result.add(map);
            }
        }
        Collections.sort(result, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                String time1 = (String) o1.get("time");
                String time2 = (String) o2.get("time");
                return time2.compareTo(time1);
            }
        });
        return ResponseService.createBySuccess(result);
    }

    @RequestMapping("/getAwardProvincial")
    public ResponseService getAwardProvincial() {
        List<Award> awardList = awardService.getAllAward();
        List<Map<String, Object>> result = new ArrayList<>();
        Set<String> exists = new HashSet<>();
        for (Award award : awardList) {
            Contest contest = contestService.getContestById(award.getContestId());
            if (contest.getName().contains("江苏省赛")) {
                Map<String, Object> map = new HashMap<>();
                String name = contest.getName();
                if (!StringUtils.isBlank(contest.getRemark())) {
                    name = name + "(" + contest.getRemark() + ")";
                }
                String nowNumber = award.getContestId() + " " + award.getNumber();
                if (exists.contains(nowNumber)) continue;
                exists.add(nowNumber);
                List<String> usernames = awardService.getAwardsByIdAndNumberAndtype(award.getContestId(), award.getNumber(), award.getType());
                String awardName = userService.findUserByUsername(usernames.get(0)).getName();
                for (int i = 1; i < usernames.size(); i++) {
                    awardName += ",";
                    String s = userService.findUserByUsername(usernames.get(i)).getName();
                    awardName += s;
                }
                map.put("name", name);
                String s = sdf.format(contest.getTime());
                map.put("time", sdf.format(contest.getTime()));
                if (award.getType().equals("一等奖")) {
                    map.put("type", "金奖");
                }
                if (award.getType().equals("二等奖")) {
                    map.put("type", "银奖");
                }
                if (award.getType().equals("三等奖")) {
                    map.put("type", "铜奖");
                }
                map.put("people", awardName);
                result.add(map);
            }
        }
        Collections.sort(result, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                String time1 = (String) o1.get("time");
                String time2 = (String) o2.get("time");
                return time2.compareTo(time1);
            }
        });
        return ResponseService.createBySuccess(result);
    }

    @RequestMapping("/getAwardICPCInvite")
    public ResponseService getAwardICPCInvite() {
        List<Award> awardList = awardService.getAllAward();
        List<Map<String, Object>> result = new ArrayList<>();
        Set<String> exists = new HashSet<>();
        for (Award award : awardList) {
            Contest contest = contestService.getContestById(award.getContestId());
            if (contest.getName().contains("ICPC邀请赛")) {
                Map<String, Object> map = new HashMap<>();
                String name = contest.getName();
                if (!StringUtils.isBlank(contest.getRemark())) {
                    name = name + "(" + contest.getRemark() + ")";
                }
                String nowNumber = award.getContestId() + " " + award.getNumber();
                if (exists.contains(nowNumber)) continue;
                exists.add(nowNumber);
                List<String> usernames = awardService.getAwardsByIdAndNumberAndtype(award.getContestId(), award.getNumber(), award.getType());
                String awardName = userService.findUserByUsername(usernames.get(0)).getName();
                for (int i = 1; i < usernames.size(); i++) {
                    awardName += ",";
                    String s = userService.findUserByUsername(usernames.get(i)).getName();
                    awardName += s;
                }
                map.put("name", name);
                String s = sdf.format(contest.getTime());
                map.put("time", sdf.format(contest.getTime()));
                if (award.getType().equals("一等奖")) {
                    map.put("type", "金奖");
                }
                if (award.getType().equals("二等奖")) {
                    map.put("type", "银奖");
                }
                if (award.getType().equals("三等奖")) {
                    map.put("type", "铜奖");
                }
                map.put("people", awardName);
                result.add(map);
            }
        }
        Collections.sort(result, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                String time1 = (String) o1.get("time");
                String time2 = (String) o2.get("time");
                return time2.compareTo(time1);
            }
        });
        return ResponseService.createBySuccess(result);
    }

    @RequestMapping("/getAwardCCPCInvite")
    public ResponseService getAwardCCPCInvite() {
        List<Award> awardList = awardService.getAllAward();
        List<Map<String, Object>> result = new ArrayList<>();
        Set<String> exists = new HashSet<>();
        for (Award award : awardList) {
            Contest contest = contestService.getContestById(award.getContestId());
            if (contest.getName().contains("CCPC邀请赛")) {
                Map<String, Object> map = new HashMap<>();
                String name = contest.getName();
                if (!StringUtils.isBlank(contest.getRemark())) {
                    name = name + "(" + contest.getRemark() + ")";
                }
                String nowNumber = award.getContestId() + " " + award.getNumber();
                if (exists.contains(nowNumber)) continue;
                exists.add(nowNumber);
                List<String> usernames = awardService.getAwardsByIdAndNumberAndtype(award.getContestId(), award.getNumber(), award.getType());
                String awardName = userService.findUserByUsername(usernames.get(0)).getName();
                for (int i = 1; i < usernames.size(); i++) {
                    awardName += ",";
                    String s = userService.findUserByUsername(usernames.get(i)).getName();
                    awardName += s;
                }
                map.put("name", name);
                String s = sdf.format(contest.getTime());
                map.put("time", sdf.format(contest.getTime()));
                if (award.getType().equals("一等奖")) {
                    map.put("type", "金奖");
                }
                if (award.getType().equals("二等奖")) {
                    map.put("type", "银奖");
                }
                if (award.getType().equals("三等奖")) {
                    map.put("type", "铜奖");
                }
                map.put("people", awardName);
                result.add(map);
            }
        }
        Collections.sort(result, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                String time1 = (String) o1.get("time");
                String time2 = (String) o2.get("time");
                return time2.compareTo(time1);
            }
        });
        return ResponseService.createBySuccess(result);
    }

    @RequestMapping("/getAwardlqb")
    public ResponseService getAwardlqb() {
        List<Award> awardList = awardService.getAllAward();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Award award : awardList) {
            Contest contest = contestService.getContestById(award.getContestId());
            if (contest.getName().contains("蓝桥杯")) {
                Map<String, Object> map = new HashMap<>();
                String name = contest.getName();
                if (!StringUtils.isBlank(contest.getRemark())) {
                    name = name + "(" + contest.getRemark() + ")";
                }
                map.put("name", name);
                map.put("time", sdf.format(contest.getTime()));
                map.put("type", award.getType());
                map.put("level", contest.getLevel());
                String awardName = userService.findUserByUsername(award.getUsername()).getName();
                map.put("people", awardName);
                result.add(map);
            }
        }
        Collections.sort(result, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                String time1 = (String) o1.get("time");
                String time2 = (String) o2.get("time");
                return time2.compareTo(time1);
            }
        });
        return ResponseService.createBySuccess(result);
    }

    @RequestMapping("/getAwardrobocom")
    public ResponseService getAwardrobocom() {
        List<Award> awardList = awardService.getAllAward();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Award award : awardList) {
            Contest contest = contestService.getContestById(award.getContestId());
            if (contest.getName().contains("robocom")) {
                Map<String, Object> map = new HashMap<>();
                String name = (1900 + contest.getTime().getYear()) + "年" + contest.getName();
                if (!StringUtils.isBlank(contest.getRemark())) {
                    name = name + "(" + contest.getRemark() + ")";
                }
                map.put("name", name);
                map.put("time", sdf.format(contest.getTime()));
                map.put("type", award.getType());
                map.put("level", contest.getLevel());
                String awardName = userService.findUserByUsername(award.getUsername()).getName();
                map.put("people", awardName);
                result.add(map);
            }
        }
        Collections.sort(result, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                String time1 = (String) o1.get("time");
                String time2 = (String) o2.get("time");
                return time2.compareTo(time1);
            }
        });
        return ResponseService.createBySuccess(result);
    }

    @RequestMapping("/getAwardgplt")
    public ResponseService getAwardgplt() {
        List<Award> awardList = awardService.getAllAward();
        List<Map<String, Object>> result = new ArrayList<>();
        Set<String> exists = new HashSet<>();
        for (Award award : awardList) {
            Contest contest = contestService.getContestById(award.getContestId());
            if (contest.getName().contains("天梯赛")) {
                Map<String, Object> map = new HashMap<>();
                String name = (1900 + contest.getTime().getYear()) + "年" + contest.getName();
                if (!StringUtils.isBlank(contest.getRemark())) {
                    name = name + "(" + contest.getRemark() + ")";
                }
                String nowNumber = award.getContestId() + " " + award.getNumber();
                if (exists.contains(nowNumber)) continue;
                exists.add(nowNumber);
                List<String> usernames = awardService.getAwardsByIdAndNumberAndtype(award.getContestId(), award.getNumber(), award.getType());
                String awardName = userService.findUserByUsername(usernames.get(0)).getName();
                for (int i = 1; i < usernames.size(); i++) {
                    awardName += ",";
                    String s = userService.findUserByUsername(usernames.get(i)).getName();
                    awardName += s;
                }
                map.put("people", awardName);
                map.put("name", name);
                map.put("time", sdf.format(contest.getTime()));
                map.put("type", award.getType());
                map.put("level", contest.getLevel());
                result.add(map);
            }
        }
        Collections.sort(result, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                String time1 = (String) o1.get("time");
                String time2 = (String) o2.get("time");
                return time2.compareTo(time1);
            }
        });
        return ResponseService.createBySuccess(result);
    }

    @RequestMapping("/getTeacher")
    public ResponseService getTeacher() {
        return ResponseService.createBySuccess(awardService.getAllTeacher());
    }
}
