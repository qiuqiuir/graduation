package com.cslg.graduation.controller;

import com.cslg.graduation.common.ResponseService;
import com.cslg.graduation.entity.News;
import com.cslg.graduation.entity.Week;
import com.cslg.graduation.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @auther xurou
 * @date 2023/4/9
 */
@RestController
@RequestMapping("/news")
@CrossOrigin(origins = "http://localhost:5173")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @ResponseBody
    @RequestMapping("/getAllNews")
    public ResponseService getAllNews(){
        List<News> newsList = newsService.getAllNews();
        return ResponseService.createBySuccess(newsList);
    }

}
