package com.cslg.graduation.controller;

import com.cslg.graduation.common.ResponseService;
import com.cslg.graduation.entity.Award;
import com.cslg.graduation.entity.Contest;
import com.cslg.graduation.entity.User;
import com.cslg.graduation.expection.MallException;
import com.cslg.graduation.service.*;
import com.cslg.graduation.util.GraduationUtil;
import com.cslg.graduation.util.JwtTokenUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @auther xurou
 * @date 2023/4/4
 */
@RestController
@RequestMapping("/user")
//@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired
    private UserService userService;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${graduation.path.upload}")
    private String uploadPath;

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
     * @param loginVO
     * @return
     */
    @PostMapping(value = "/login")
    public ResponseService test(@RequestBody User loginVO) {
        User user = userService.login(loginVO);
        String token = JwtTokenUtils.createToken(loginVO.getUsername(), "user", true);
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
     * @param user
     * @return
     */
    @PostMapping("/register")
    public ResponseService register(@RequestBody User user) {
        userService.register(user);
        return ResponseService.createBySuccess();
    }

    /**
     * 通过传入的User里的username信息查找用户
     * @param user
     * @return
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
     * 更新user用户信息
     * @param user
     * @return
     */
    @PostMapping("/update")
    public ResponseService updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return ResponseService.createBySuccess();
    }

    /**
     * 更新user权限
     * @param username
     * @return
     */
    @GetMapping("/updateUserStatus")
    public ResponseService updateUserStatus(@RequestParam String username) {
        userService.updateStatus(username);
        return ResponseService.createBySuccess();
    }

    @GetMapping("/getAllUser")
    public ResponseService getAllUser() {
        List<User> userList = userService.getAllUsers();
        return ResponseService.createBySuccess(userList);
    }


    @GetMapping("/getAllUserMessage")
    public ResponseService getAllUserMessage() {
        List<Map<String, Object>> userList = userService.getAllUserMessage();
        return ResponseService.createBySuccess(userList);
    }

    /**
     * 主页人员信息
     *
     * @return
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
     * @param session
     * @return
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
            score += scoreService.getTotalScoreByUsername(user.getUsername())/5;
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
        Collections.sort(result, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                Integer y = (Integer) o2.get("score");
                if (y == null) {
                    int df = 5;
                }
                Integer x = (Integer) o1.get("score");
                return y.compareTo(x);
            }
        });
        return ResponseService.createBySuccess(result);
    }
}
