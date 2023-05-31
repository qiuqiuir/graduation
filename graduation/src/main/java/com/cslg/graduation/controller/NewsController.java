package com.cslg.graduation.controller;

import com.cslg.graduation.common.ResponseService;
import com.cslg.graduation.entity.News;
import com.cslg.graduation.entity.Week;
import com.cslg.graduation.service.NewsService;
import com.cslg.graduation.util.GraduationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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

    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping("/addNews")
    public ResponseService addNews(@RequestBody News news){
        Date time = news.getTime();
        time = GraduationUtil.changeTime(time);
        news.setTime(time);
        newsService.addNews(news);
        return ResponseService.createBySuccess();
    }

}
