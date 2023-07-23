package com.cslg.graduation.service;

import com.cslg.graduation.common.ResponseCode;
import com.cslg.graduation.expection.MallException;
import com.cslg.graduation.dao.UserMapper;
import com.cslg.graduation.entity.User;
import com.cslg.graduation.util.GraduationUtil;
import com.cslg.graduation.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @auther xurou
 * @date 2022/6/7
 */

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 根据学号找人
     *
     * @param username
     * @return
     */
    public User findUserByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    /**
     * 根据姓名找人
     *
     * @param name
     * @return
     */
    public User findUserByName(String name) {
        return userMapper.selectByName(name);
    }

    /**
     * 返回数据库所有用户
     */
    public List<User> getAllUsers() {
        return userMapper.selectAllUsers();
    }

    /**
     * 用户登录
     *
     * @param user
     * @return
     */
    public User login(User user) {
        User u = userMapper.selectByUsername(user.getUsername());
        if (u == null) {
            throw new MallException(400, "该学号未注册");
        }
        String password = GraduationUtil.md5(user.getPassword());
        if (!password.equals(u.getPassword())) {
            throw new MallException(400, "密码错误");
        }
        return u;
    }


    /**
     * 用户注册
     *
     * @param user
     * @return
     */
    public void register(User user) {
        User u = userMapper.selectByUsername(user.getUsername());
        if (u != null) {
            throw new MallException(400, "该学号已注册");
        }
        user.setPassword(GraduationUtil.md5(user.getPassword()));
        user.setStatus(0);
        user.setIsScore(0);
//        String nowDate = "";
//        GraduationUtil.DateToString(new Date());
        user.setCreateDate(new Date());
        userMapper.insertUser(user);
        return;
    }

    /**
     * 根据学号更新管理信息
     *
     * @param username
     */
    public void updateStatus(String username) {
        int status = userMapper.selectByUsername(username).getStatus();
        if (status == 0) {
            userMapper.updateStatus(username, 1);
        } else if (status == 1) {
            userMapper.updateStatus(username, 0);
        }
    }

    /**
     * 根据学号更新是否集训队队员
     * @param username
     */
    public void updateIsScore(String username) {
        int isScore = userMapper.selectByUsername(username).getIsScore();
        if (isScore == 0) {
            userMapper.updateIsScore(username, 1);
        } else {
            userMapper.updateIsScore(username, 0);
        }
    }

    /**
     * 获取所有在役用户信息
     *
     * @return
     */
    public List<Map<String, Object>> getAllUserMessage() {
        List<User> userList = getACMer();
        List<Map<String, Object>> shuju = new ArrayList<>();
        int year = GraduationUtil.getNowYear();
        for (User user : userList) {
            if (user.getSession() >= year % 100 - 4) {
                Map<String, Object> map = new HashMap<>();
                map.put("username", user.getUsername());
                map.put("name", user.getName());
                shuju.add(map);
            }
        }
        return shuju;
    }

    /**
     * 根据User更新用户
     *
     * @param user
     */
    public void updateUser(User user) {
        if (user.getPassword() != null) {
            user = user.setPassword(GraduationUtil.md5(user.getPassword()));
        } else {
            User u = userMapper.selectByUsername(user.getUsername());
            user = user.setPassword(u.getPassword());
        }
        userMapper.updateUser(user);
    }

    /**
     * 根据第session届获取该届所有用户
     *
     * @param session
     * @return
     */
    public List<User> getUsersBySession(int session) {
        return userMapper.selectUsersBySession(session);
    }

    /**
     * 获取第session届所有专业
     *
     * @param session
     * @return
     */
    public List<String> getMajorsBySession(int session) {
        return userMapper.selectMajorBySession(session);
    }

    /**
     * 获取所有集训队队员
     *
     * @return
     */
    public List<User> getACMer() {
        return userMapper.selectIsScoreUsers();
    }


}
