package com.cslg.graduation.controller;

import com.cslg.graduation.common.ResponseService;
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

@RestController
@RequestMapping("/oj")
//@CrossOrigin(origins = "http://localhost:5173")
public class OjController {

    @Autowired
    private OjService ojService;

    @Autowired
    private UserService userService;

    @PostMapping("/addOj")
    public ResponseService addOj(@RequestBody Oj oj) {
        ojService.addOj(oj);
        return ResponseService.createBySuccess();
    }

//    @ResponseBody
    @RequestMapping("/getAll/{username}")
    public ResponseService getAllOj(@PathVariable("username")String username){
        List<Oj> ojList = ojService.getAllOjId(username);
        return ResponseService.createBySuccess(ojList);
    }

}
