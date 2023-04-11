package com.cslg.graduation.controller;

import com.cslg.graduation.common.ResponseService;
import com.cslg.graduation.entity.User;
import com.cslg.graduation.expection.MallException;
import com.cslg.graduation.service.UserService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @PostMapping(value = "/login")
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

    @PostMapping("/register")
    public ResponseService register(@RequestBody User user) {
        userService.register(user);
        return ResponseService.createBySuccess();
    }

    @PostMapping("/getUser")
    public ResponseService getUserByusername(@RequestBody User user){
        User u = userService.findUserByUsername(user.getUsername());
        if(u == null){
            throw new MallException(400, "该学号未注册");
        }
        return ResponseService.createBySuccess(u);
    }

    @PostMapping("/update")
    public ResponseService updateUser(@RequestBody User user){
        userService.updateUser(user);
        return ResponseService.createBySuccess();
    }

//    @PostMapping("/updateHeader")
//    public ResponseService uploadHeader(MultipartFile headerImage) {
//        if (headerImage == null) {
//            ResponseService.createByCodeErrorMessage(400,"您还没有选择图片!");
//        }
//        String fileName = headerImage.getOriginalFilename();
//        String suffix = fileName.substring(fileName.lastIndexOf('.'));
//        if (StringUtils.isBlank(suffix)) {
//            ResponseService.createByCodeErrorMessage(400,"文件的格式不正确!");
//        }
//        // 生成随机文件名
//        fileName = GraduationUtil.generateUUID() + suffix;
//        // 确定文件存放的路径
//        File dest = new File(uploadPath + "/" + fileName);
//        try {
//            // 存储文件
//            headerImage.transferTo(dest);
//        } catch (IOException e) {
//            throw new RuntimeException("上传文件失败,服务器发生异常!", e);
//        }
//
//        // 更新当前用户的头像的路径(web访问路径)
//        //http://localhost:8080/community/user/header/xxx.png
//        User user = hostHolder.getUser();
//        String headUrl = domain + contextPath + "/user/header/" + fileName;
//        userService.updateHeader(user.getId(), headUrl);
//
//        return "redirect:/index";
//    }
}
