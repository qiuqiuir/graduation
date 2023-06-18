package com.cslg.graduation.service;

import com.cslg.graduation.dao.AwardMapper;
import com.cslg.graduation.entity.Award;
import com.cslg.graduation.entity.Contest;
import com.cslg.graduation.entity.FirstAward;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @auther xurou
 * @date 2023/4/13
 */

@Service
public class AwardService {

    @Autowired
    private AwardMapper awardMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private ContestService contestService;

    /**
     * 获取所有获奖信息
     *
     * @return
     */
    public List<Award> getAllAward() {
        return awardMapper.selectAllAward();
    }

    /**
     * 根据比赛id获取所有获奖信息
     *
     * @param id
     * @return
     */
    public List<Award> getAwardById(int id) {
        return awardMapper.selectAwardById(id);
    }

    /**
     * 根据比赛id和比赛类型type获取获奖人员姓名
     *
     * @param id
     * @param type
     * @return
     */
    public String getAwardByIdAndType(int id, String type) {
        List<Award> awardList = awardMapper.selectAwardByIdAndType(id, type);
        for (int i = 0; i < awardList.size(); i++) {
            String username = awardList.get(i).getUsername();
            String name = userService.findUserByUsername(username).getName();
            awardList.get(i).setUsername(name);
        }
        Collections.sort(awardList, new Comparator<Award>() {
            @Override
            public int compare(Award o1, Award o2) {
                return Integer.compare(o2.getNumber(), o1.getNumber());
            }
        });
        String winner = "";
        boolean isFirst = true;
        for (int i = 0; i < awardList.size(); i++) {
            if (i != awardList.size() - 1 && awardList.get(i).getNumber() == awardList.get(i + 1).getNumber()) {
                if (isFirst) winner += "【";
                winner += awardList.get(i).getUsername();
                winner += "、";
                isFirst = false;
            } else if (i > 0 && awardList.get(i).getNumber() == awardList.get(i - 1).getNumber()) {
                winner += awardList.get(i).getUsername();
                winner += "】";
                isFirst = true;
                if (i != awardList.size() - 1) winner += "、";
            } else if (i != awardList.size() - 1) {
                winner += awardList.get(i).getUsername();
                winner += "、";
            } else {
                winner += awardList.get(i).getUsername();
            }
        }
        return winner;
    }

    /**
     * 根据比赛id和比赛类型查找该比赛已经有几个人获奖
     *
     * @param id
     * @return
     */
    public int getNumberById(int id) {
        return awardMapper.selectNumberById(id);
    }

    public int getNumberById(int id, String type) {
        return awardMapper.selectNumberByIdAndType(id, type);
    }

    /**
     * 插入一条获奖记录
     *
     * @param award
     */
    public void insertAward(Award award) {
        awardMapper.insertAward(award);
    }

    /**
     * 获取比赛总获奖次数
     *
     * @return
     */
    public int getCountAward() {
        List<Award> awardList = getAllAward();
        Set<String> set = new HashSet<>();
        int count = 0;
        for (Award award : awardList) {
            Contest contest = contestService.getContestById(award.getContestId());
            if (contest.getNumber() == 1) {
                count++;
                continue;
            }
            String name = award.getContestId() + " " + award.getNumber();
            set.add(name);
        }
        return count + set.size();
    }

    /**
     * 获得比赛获奖人次
     *
     * @return
     */
    public int getCountPersonTimes() {
        return awardMapper.selectCountRenci();
    }

    /**
     * 根据学号username获取该用户所有获奖记录
     *
     * @param username
     * @return
     */
    public List<Award> getAwardByUsername(String username) {
        return awardMapper.selectAwardByUsername(username);
    }

    /**
     * 根据比赛id和第几个获奖number获取获奖人员的学号
     *
     * @param id
     * @param number
     * @return List<String>
     */
    public List<String> getAwardsByIdAndNumberAndtype(int id, int number, String type) {
        return awardMapper.selectAwardsByIdAndNumber(id, number, type);
    }

    /**
     * 根据比赛名查找该奖项第一次获奖情况
     *
     * @param name
     * @param contestList
     * @return
     */
    public Map<String, List<FirstAward>> getFirstAwardByName(String name, List<Contest> contestList) {
        if (name.equals("天梯赛")) {
            return getFirstAwardByNameCCCC(contestList);
        }
        if (name.equals("CCPC女生赛")) {
            return getFirstAwardByNameCCPCGirls(contestList);
        }
        Map<String, List<FirstAward>> map = new HashMap<>();
        for (Contest contest : contestList) {
            String contestName = contest.getName();
            if (contestName.contains(name) && contestName.indexOf(name) + name.length() == contestName.length()) {
                if (contestName.contains("CCPC") && contest.getRemark().equals("女生赛")) continue;
                List<Award> awardList = getAwardById(contest.getId());
                Map<String, List<FirstAward>> huojiang = new HashMap<>();
                huojiang.put("省级一等奖", new ArrayList<>());
                huojiang.put("省级二等奖", new ArrayList<>());
                huojiang.put("省级三等奖", new ArrayList<>());
                huojiang.put("国家级一等奖", new ArrayList<>());
                huojiang.put("国家级二等奖", new ArrayList<>());
                huojiang.put("国家级三等奖", new ArrayList<>());
                for (Award award : awardList) {
                    String awardLevel = contest.getLevel() + award.getType();
                    boolean is = map.containsKey(awardLevel);
                    if (!is) {
                        FirstAward people = new FirstAward()
                                .setUsername(award.getUsername())
                                .setTime(contest.getTime());
                        String peopleName = userService.findUserByUsername(people.getUsername()).getName();
                        people = people.setName(peopleName);
                        huojiang.get(awardLevel).add(people);
                    }
                }
                for (Map.Entry<String, List<FirstAward>> entry : huojiang.entrySet()) {
                    if (entry.getValue().size() > 0) {
                        map.put(entry.getKey(), entry.getValue());
                    }
                }
            }
        }
        return map;
    }

    public Map<String, List<FirstAward>> getFirstAwardByNameCCCC(List<Contest> contestList) {
        Map<String, List<FirstAward>> map = new HashMap<>();
        String name = "天梯赛";
        for (Contest contest : contestList) {
            String contestName = contest.getName();
            if (contestName.contains(name) && contestName.indexOf(name) + name.length() == contestName.length()) {
                List<Award> awardList = getAwardById(contest.getId());
                Map<String, List<FirstAward>> huojiang = new HashMap<>();
                List<String> prefix = new ArrayList<>();
                prefix.add("高校");
                prefix.add("团队");
                prefix.add("个人");
                for (String s : prefix) {
                    huojiang.put(s + "省级一等奖", new ArrayList<>());
                    huojiang.put(s + "省级二等奖", new ArrayList<>());
                    huojiang.put(s + "省级三等奖", new ArrayList<>());
                    huojiang.put(s + "国家级一等奖", new ArrayList<>());
                    huojiang.put(s + "国家级二等奖", new ArrayList<>());
                    huojiang.put(s + "国家级三等奖", new ArrayList<>());
                }

                for (Award award : awardList) {
                    String awardLevel = contest.getRemark() + contest.getLevel() + award.getType();
                    boolean is = map.containsKey(awardLevel);
                    if (!is) {
                        FirstAward people = new FirstAward()
                                .setUsername(award.getUsername())
                                .setTime(contest.getTime());
                        String peopleName = userService.findUserByUsername(people.getUsername()).getName();
                        people = people.setName(peopleName);
                        huojiang.get(awardLevel).add(people);
                    }
                }
                for (Map.Entry<String, List<FirstAward>> entry : huojiang.entrySet()) {
                    if (entry.getValue().size() > 0) {
                        map.put(entry.getKey(), entry.getValue());
                    }
                }
            }
        }
        return map;
    }

    /**
     * 根据传入的比赛信息获取CCPC女生赛第一个获奖的
     *
     * @param contestList
     * @return
     */
    public Map<String, List<FirstAward>> getFirstAwardByNameCCPCGirls(List<Contest> contestList) {
        String name = "CCPC";
        Map<String, List<FirstAward>> map = new HashMap<>();
        for (Contest contest : contestList) {
            String contestName = contest.getName();
            if (contestName.contains(name) && contestName.indexOf(name) + name.length() == contestName.length() && contest.getRemark().equals("女生赛")) {
                List<Award> awardList = getAwardById(contest.getId());
                Map<String, List<FirstAward>> huojiang = new HashMap<>();
                huojiang.put("国家级一等奖", new ArrayList<>());
                huojiang.put("国家级二等奖", new ArrayList<>());
                huojiang.put("国家级三等奖", new ArrayList<>());
                for (Award award : awardList) {
                    String awardLevel = contest.getLevel() + award.getType();
                    boolean is = map.containsKey(awardLevel);
                    if (!is) {
                        FirstAward people = new FirstAward()
                                .setUsername(award.getUsername())
                                .setTime(contest.getTime());
                        String peopleName = userService.findUserByUsername(people.getUsername()).getName();
                        people = people.setName(peopleName);
                        huojiang.get(awardLevel).add(people);
                    }
                }
                for (Map.Entry<String, List<FirstAward>> entry : huojiang.entrySet()) {
                    if (entry.getValue().size() > 0) {
                        map.put(entry.getKey(), entry.getValue());
                    }
                }
            }
        }
        return map;
    }

    public List<String> getAllTeacher() {
        return awardMapper.selectAllTeacher();
    }


}
