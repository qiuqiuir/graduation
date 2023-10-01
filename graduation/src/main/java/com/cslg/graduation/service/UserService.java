package com.cslg.graduation.service;

import com.cslg.graduation.expection.MallException;
import com.cslg.graduation.dao.UserMapper;
import com.cslg.graduation.entity.User;
import com.cslg.graduation.util.GraduationUtil;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * 根据学号找人
     *
     * @param username 学号
     * @return User
     */
    public User findUserByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    /**
     * 根据姓名找人
     *
     * @param name 姓名
     * @return User
     */
    public User findUserByName(String name) {
        return userMapper.selectByName(name);
    }

    /**
     * 返回数据库所有用户
     *
     * @return List<User>
     */
    public List<User> getAllUsers() {
        return userMapper.selectAllUsers();
    }

    /**
     * 用户登录
     *
     * @param user 用户
     * @return User
     */
    public User login(User user) {
        User u = userMapper.selectByUsername(user.getUsername());
        if (u == null) {
            throw new MallException(400, "该学号未注册");
        }
        String password = GraduationUtil.md5(user.getPassword());
        assert password != null;
        if (!password.equals(u.getPassword())) {
            throw new MallException(400, "密码错误");
        }
        return u;
    }


    /**
     * 用户注册
     *
     * @param user 用户
     */
    public void register(User user) {
        User u = userMapper.selectByUsername(user.getUsername());
        if (u != null) {
            throw new MallException(400, "该学号已注册");
        }
        user.setPassword(GraduationUtil.md5(user.getPassword()));
        user.setStatus(0);
        user.setIsScore(0);
        user.setCreateDate(new Date());
        userMapper.insertUser(user);
    }

    /**
     * 根据学号更新管理信息
     *
     * @param username 学号
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
     *
     * @param username 学号
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
     * @return List<User>
     */
    public List<User> getAllUserMessage() {
        List<User> userList = getACMer();
        List<User> shuju = new ArrayList<>();
        int year = GraduationUtil.getNowYear();
        for (User user : userList) {
            if (user.getSession() >= year % 100 - 4) {
                shuju.add(user);
            }
        }
        return shuju;
    }

    /**
     * 根据User更新用户
     *
     * @param user 用户
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
     * @param session 第几届
     * @return List<User>
     */
    public List<User> getUsersBySession(int session) {
        return userMapper.selectUsersBySession(session);
    }

    /**
     * 获取第session届所有专业
     *
     * @param session 第几届
     * @return List<String>
     */
    public List<String> getMajorsBySession(int session) {
        return userMapper.selectMajorBySession(session);
    }

    /**
     * 获取所有集训队队员
     *
     * @return List<User>
     */
    public List<User> getACMer() {
        return userMapper.selectIsScoreUsers();
    }


}
