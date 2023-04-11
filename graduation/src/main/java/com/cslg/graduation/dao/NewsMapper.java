package com.cslg.graduation.dao;

import com.cslg.graduation.entity.News;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @auther xurou
 * @date 2023/4/9
 */
@Mapper
public interface NewsMapper {

    public void insertNews(News news);

    public List<News> selectAllNews();
}
