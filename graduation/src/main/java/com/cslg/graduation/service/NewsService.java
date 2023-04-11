package com.cslg.graduation.service;

import com.cslg.graduation.dao.NewsMapper;
import com.cslg.graduation.entity.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @auther xurou
 * @date 2023/4/9
 */
@Service
public class NewsService {

    @Autowired
    private NewsMapper newsMapper;

    public void addNews(News news){
        newsMapper.insertNews(news);
    }

    public List<News> getAllNews(){
        List<News> newsList = newsMapper.selectAllNews();
        Collections.sort(newsList, new Comparator<News>() {
            @Override
            public int compare(News o1, News o2) {
                return -o1.getTime().compareTo(o2.getTime());
            }
        });
        return newsList;
    }
}
