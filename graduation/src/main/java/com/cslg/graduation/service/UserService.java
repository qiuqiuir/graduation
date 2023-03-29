package com.cslg.graduation.service;

import com.cslg.graduation.dao.UserMapper;
import com.cslg.graduation.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @auther xurou
 * @date 2022/6/7
 */

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;


    public User findUserByUsername(String username){
        return userMapper.selectByUsername(username);
    }

    public User findUserByName(String name){
        return userMapper.selectByName(name);
    }

//    public User (){
//
//    }
}
