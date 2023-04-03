package com.cslg.graduation.controller;

import com.cslg.graduation.entity.Oj;
import com.cslg.graduation.service.OjService;
import com.cslg.graduation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @auther xurou
 * @date 2023/4/3
 */

@Controller
@RequestMapping("/oj")
@CrossOrigin(origins = "http://localhost:5173")
public class OjController {

    @Autowired
    private OjService ojService;

    @Autowired
    private UserService userService;

    @PostMapping("/addOj")
    public void addOj(@RequestBody  Oj oj) {
        ojService.addOj(oj);
    }

    @ResponseBody
    @RequestMapping("/getAllOj")
    public List<Oj> getAllOj(){
        List<Oj> ojList = ojService.getAllOj();
        for(int i=0;i<ojList.size();i++){
            String username = ojList.get(i).getUsername();
            String name = userService.findUserByUsername(username).getName();
            ojList.get(i).setUsername(name);
        }
        return ojList;
    }

}
