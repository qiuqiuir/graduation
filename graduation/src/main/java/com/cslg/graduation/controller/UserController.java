package com.cslg.graduation.controller;

import com.cslg.graduation.common.ResponseService;
import com.cslg.graduation.entity.Award;
import com.cslg.graduation.entity.AwardTeam;
import com.cslg.graduation.entity.Contest;
import com.cslg.graduation.entity.User;
import com.cslg.graduation.expection.MallException;
import com.cslg.graduation.service.*;
import com.cslg.graduation.util.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @auther xurou
 * @date 2023/4/4
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AwardService awardService;

    @Autowired
    private AcnumberService acnumberService;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private ContestService contestService;

    /**
     * 登录,传入User,包含用户名username和密码password
     *
     * @param loginVO 用户
     * @return Map<String, String> mp,"token"验证码,"name"姓名,"username"学号,"admin"是否管理员
     */
    @PostMapping(value = "/login")
    public ResponseService login(@RequestBody User loginVO) {
        User user = userService.login(loginVO);
        String token = null;
        if (user.getStatus() == 0) {
            token = JwtTokenUtils.createToken(loginVO.getUsername(), "user", true);
        } else if (user.getStatus() == 1) {
            token = JwtTokenUtils.createToken(loginVO.getUsername(), "admin,user", true);
        }
        Map<String, String> mp = new HashMap<>();
        if (user == null) {
            return ResponseService.createByError();
        }
        mp.put("token", token);
        mp.put("name", user.getName());
        mp.put("username", user.getUsername());
        mp.put("admin", user.getStatus() + "");
        return ResponseService.createBySuccess(mp);
    }

    /**
     * 注册,传入User,包含用户名username和密码password和姓名name
     *
     * @param user 用户
     * @return 是否成功
     */
    @PostMapping("/register")
    public ResponseService register(@RequestBody User user) {
        userService.register(user);
        return ResponseService.createBySuccess();
    }

    /**
     * 通过传入的User里的username信息查找用户
     *
     * @param user 用户
     * @return User
     */
    @PostMapping("/getUser")
    public ResponseService getUserByUsername(@RequestBody User user) {
        User u = userService.findUserByUsername(user.getUsername());
        if (u == null) {
            throw new MallException(400, "该学号未注册");
        }
        return ResponseService.createBySuccess(u);
    }

    /**
     * 更新user用户信息，仅管理员
     *
     * @param user 用户
     * @return 是否成功
     */

    @PreAuthorize("hasAuthority('user')")
    @PostMapping("/update")
    public ResponseService updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return ResponseService.createBySuccess();
    }

    /**
     * 更新user权限，仅管理员
     *
     * @param username 学号
     * @return 是否成功
     */
    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/updateUserStatus")
    public ResponseService updateUserStatus(@RequestParam String username) {
        userService.updateStatus(username);
        return ResponseService.createBySuccess();
    }

    /**
     * 更新user打星，仅管理员
     *
     * @param username 学号
     * @return 是否成功
     */
    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/updateUserIsScore")
    public ResponseService updateUserIsScore(@RequestParam String username) {
        userService.updateIsScore(username);
        return ResponseService.createBySuccess();
    }


    /**
     * 获取所有用户，仅管理员
     *
     * @return List<User>
     */
    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/getAllUser")
    public ResponseService getAllUser() {
        List<User> userList = userService.getAllUsers();
        for (User user : userList) {
            user.setPassword("*");
        }
        return ResponseService.createBySuccess(userList);
    }

    /**
     * 获取所有在役队员信息，仅管理员
     *
     * @return List<User>
     */
    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/getAllUserMessage")
    public ResponseService getAllUserMessage() {
        List<User> userList = userService.getAllUserMessage();
        for (User user : userList) {
            user.setPassword("*");
            user.setIdentityCard("*");
            user.setPhone("*");
            user.setEmail("*");
            user.setClothingSize("*");
        }
        return ResponseService.createBySuccess(userList);
    }

    /**
     * 主页人员信息
     *
     * @return Map<String, Object>
     */
    @GetMapping("/getPeople")
    public ResponseService getPeople() {
        Date time = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(time);
        int year = calendar.get(Calendar.YEAR);
        year %= 100;
        Map<String, Object> fan = new HashMap<>();
        List<Map<String, Object>> result = new ArrayList<>();
        List<Integer> sessionNumber = new ArrayList<>();
        int cnt = 0;
        for (int jie = year; jie >= 17; jie--) {
            List<User> userList = userService.getUsersBySession(jie);
            if (userList.size() > 0) {
                sessionNumber.add(cnt);
                List<String> majors = userService.getMajorsBySession(jie);

                for (String major : majors) {
                    Map<String, Object> majorMap = new HashMap<>();
                    majorMap.put("major", major);
                    majorMap.put("session", "" + jie + "级(" + userList.size() + ")人");
                    List<User> majorUser = new ArrayList<>();
                    for (User u : userList) {
                        if (u.getMajor().equals(major)) {
                            majorUser.add(u);
                        }
                    }
                    majorMap.put("user", majorUser);
                    majorMap.put("number", majorUser.size());
                    result.add(majorMap);
                }
                cnt += majors.size();
            }
        }
        sessionNumber.add(cnt);
        fan.put("message", result);
        fan.put("number", sessionNumber);
        return ResponseService.createBySuccess(fan);
    }

    /**
     * 获取第session届所有队员的情况，并计分后降序返回
     *
     * @param session 第几届
     * @return List<Map < String, Object>>
     */
    @GetMapping("/getPeople/{session}")
    public ResponseService getPeopleBySession(@PathVariable("session") int session) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<User> userList = userService.getUsersBySession(session);
        for (User user : userList) {
            Map<String, Object> map = new HashMap<>();
            map.put("username", user.getUsername());
            map.put("name", user.getName());
            List<Award> awardList = awardService.getAwardByUsername(user.getUsername());
            int awardNumber = awardList.size();
            map.put("awardNumber", awardNumber);
            int acNumber = acnumberService.getAllCount(user.getUsername());
            map.put("acNumber", acNumber);
            int score = acNumber;
            score += scoreService.getTotalScoreByUsername(user.getUsername()) / 5;
            for (Award award : awardList) {
                int coefficient = 0;
                if (award.getType().equals("一等奖")) coefficient = 3;
                if (award.getType().equals("二等奖")) coefficient = 2;
                if (award.getType().equals("三等奖")) coefficient = 1;
                Contest contest = contestService.getContestById(award.getContestId());
                if (contest.getLevel().equals("国家级")) coefficient *= 2;
                if (contest.getName().contains("邀请赛")) {
                    score += coefficient * 300;
                } else if (contest.getName().contains("CPC")) {
                    score += coefficient * 500;
                } else if (contest.getName().contains("蓝桥杯")) {
                    score += coefficient * 50;
                } else if (contest.getName().contains("天梯赛")) {
                    score += coefficient * 100;
                } else if (contest.getName().contains("省赛")) {
                    score += coefficient * 200;
                } else if (contest.getName().contains("robocom")) {
                    score += coefficient * 50;
                }
            }
            map.put("score", score);
            result.add(map);
        }
        result.sort((o1, o2) -> {
            Integer y = (Integer) o2.get("score");
            Integer x = (Integer) o1.get("score");
            return y.compareTo(x);
        });
        return ResponseService.createBySuccess(result);
    }

    @GetMapping("/getTeam")
    public ResponseService getTeam() {
        List<Map<String, Object>> result = new ArrayList<>();
        List<User> userList = userService.getACMer();
        Calendar date = Calendar.getInstance();
        int year = date.get(Calendar.YEAR) % 100 - 4;
        for (User user : userList) {
            if (user.getSession() <= year) continue;
            Map<String, Object> map = new HashMap<>();
            map.put("username", user.getUsername());
            map.put("name", user.getName());
            List<Award> awardList = awardService.getAwardByUsername(user.getUsername());
            int awardNumber = awardList.size();
            map.put("awardNumber", awardNumber);
            int acNumber = acnumberService.getAllCount(user.getUsername());
            map.put("acNumber", acNumber);
            int score = acNumber;
            score += scoreService.getTotalScoreByUsername(user.getUsername()) / 5;
            for (Award award : awardList) {
                int coefficient = 0;
                if (award.getType().equals("一等奖")) coefficient = 3;
                if (award.getType().equals("二等奖")) coefficient = 2;
                if (award.getType().equals("三等奖")) coefficient = 1;
                Contest contest = contestService.getContestById(award.getContestId());
                if (contest.getLevel().equals("国家级")) coefficient *= 2;
                if (contest.getName().contains("邀请赛")) {
                    score += coefficient * 300;
                } else if (contest.getName().contains("CPC")) {
                    score += coefficient * 500;
                } else if (contest.getName().contains("蓝桥杯")) {
                    score += coefficient * 50;
                } else if (contest.getName().contains("天梯赛")) {
                    score += coefficient * 100;
                } else if (contest.getName().contains("省赛")) {
                    score += coefficient * 200;
                } else if (contest.getName().contains("robocom")) {
                    score += coefficient * 50;
                }
            }
            map.put("score", score);
            result.add(map);
        }
        result.sort((o1, o2) -> {
            Integer y = (Integer) o2.get("score");
            Integer x = (Integer) o1.get("score");
            return y.compareTo(x);
        });
        List<AwardTeam> awardTeams = new ArrayList<>();
        for (int i = 0; i < result.size() / 3 * 3; i += 3) {
            List<String> names = new ArrayList<>();
            int awardNumber = 0;
            int acnumberNumber = 0;
            for (int j = i; j < i + 3; j++) {
                names.add((String) result.get(j).get("name"));
                String username = (String) result.get(j).get("username");
                awardNumber += awardService.getAwardByUsername(username).size();
                acnumberNumber += acnumberService.getAllCount(username);
            }
            AwardTeam awardTeam = new AwardTeam()
                    .setTeam(names)
                    .setType("共计获奖" + awardNumber + "次,总过题数" + acnumberNumber + "道");
            awardTeams.add(awardTeam);
        }

        return ResponseService.createBySuccess(awardTeams);
    }
}
