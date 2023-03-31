package com.cslg.graduation.service;

import com.cslg.graduation.dao.UserMapper;
import com.cslg.graduation.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
     * @param username
     * @return
     */
    public User findUserByUsername(String username){
        return userMapper.selectByUsername(username);
    }

    /**
     * 根据姓名找人
     * @param name
     * @return
     */
    public User findUserByName(String name){
        return userMapper.selectByName(name);
    }

    /**
     * 返回所有在积分表显示的同学
     */
    public List<String> IsScoreUsers(){
        return userMapper.selectIsScoreUsers();
    }



}
