package com.cslg.graduation.controller;

import com.cslg.graduation.common.ResponseService;
import com.cslg.graduation.entity.User;
import com.cslg.graduation.service.UserService;
import com.cslg.graduation.util.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @auther xurou
 * @date 2023/4/9
 */
@RestController
public class TestController {

    @Autowired
    private UserService userService;


    @PostMapping("/test")
    public ResponseService test(@RequestBody User loginVO){
        User user = userService.login(loginVO);
        String token= JwtTokenUtils.createToken(loginVO.getUsername(),"user", true);
        Map<String,String> mp=new HashMap<>();
        if(user == null){
            return ResponseService.createByError();
        }
        mp.put("token",token);
        mp.put("name",user.getName());
        mp.put("username",user.getUsername());
        mp.put("avatar",user.getHeaderUrl());
        mp.put("admin",user.getStatus()+"");
        return ResponseService.createBySuccess(mp);
    }

}
