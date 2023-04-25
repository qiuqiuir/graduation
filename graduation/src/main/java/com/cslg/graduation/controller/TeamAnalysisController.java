package com.cslg.graduation.controller;

import com.cslg.graduation.service.AwardService;
import com.cslg.graduation.service.ContestService;
import com.cslg.graduation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther xurou
 * @date 2023/4/24
 */

@RestController
@RequestMapping("/team")
public class TeamAnalysisController {

    @Autowired
    private ContestService contestService;

    @Autowired
    private AwardService awardService;

    @Autowired
    private UserService userService;



}
