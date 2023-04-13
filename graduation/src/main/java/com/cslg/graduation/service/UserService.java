package com.cslg.graduation.service;

import com.cslg.graduation.common.ResponseCode;
import com.cslg.graduation.expection.MallException;
import com.cslg.graduation.dao.UserMapper;
import com.cslg.graduation.entity.User;
import com.cslg.graduation.util.GraduationUtil;
import com.cslg.graduation.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
     * 返回所有在积分表显示的同学
     */
    public List<String> IsScoreUsers() {
        return userMapper.selectIsScoreUsers();
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
        user.setIsScore(1);
//        String nowDate = "";
//        GraduationUtil.DateToString(new Date());
        user.setCreateDate(new Date());
        userMapper.insertUser(user);
        return;
    }

    public void updateHeaderUrl(String username, String headUrl) {
        userMapper.updateHeader(username, headUrl);
    }

    public List<Map<String, Object>> getAllUserMessage() {
        List<User> userList = userMapper.selectAllUsers();
        List<Map<String, Object>> shuju = new ArrayList<>();
        for (User user : userList) {
            Map<String, Object> map = new HashMap<>();
            map.put("username", user.getUsername());
            map.put("name", user.getName());
            shuju.add(map);
        }
        return shuju;
    }

    public void updateUser(User user) {
        userMapper.updateUser(user);
    }


}
