package com.cslg.graduation.controller;

import com.cslg.graduation.common.ResponseService;
import com.cslg.graduation.entity.Knowledge;
import com.cslg.graduation.service.KnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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
    public ResponseService getAll(@PathVariable("username") String username) {
        List<Knowledge> knowledgeList = knowledgeService.getAllKnowledgeByUsername(username);
        Collections.sort(knowledgeList, new Comparator<Knowledge>() {
            @Override
            public int compare(Knowledge o1, Knowledge o2) {
                return -Integer.compare(o1.getCount(),o2.getCount());
            }
        });
        return ResponseService.createBySuccess(knowledgeList);
    }

}
