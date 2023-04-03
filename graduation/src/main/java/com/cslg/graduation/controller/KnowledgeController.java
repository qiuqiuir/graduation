package com.cslg.graduation.controller;

import com.cslg.graduation.entity.Knowledge;
import com.cslg.graduation.service.KnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @auther xurou
 * @date 2023/4/4
 */

@Controller
@RequestMapping("/knowledge")
@CrossOrigin(origins = "http://localhost:5173")
public class KnowledgeController {

    @Autowired
    private KnowledgeService knowledgeService;

    @RequestMapping("/getAll/{username}")
    @ResponseBody
    public List<Knowledge> getAll(@PathVariable("username")String username){
        return knowledgeService.getAllKnowledgeByUsername(username);
    }

}
