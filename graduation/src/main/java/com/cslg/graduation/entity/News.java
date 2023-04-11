package com.cslg.graduation.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Date;

/**
 * @auther xurou
 * @date 2023/4/9
 */
public class News {
    private int id;
    private String url;
    private String title;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date time;

    public int getId() {
        return id;
    }

    public News setId(int id) {
        this.id = id;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public News setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public News setTitle(String title) {
        this.title = title;
        return this;
    }

    public Date getTime() {
        return time;
    }

    public News setTime(Date time) {
        this.time = time;
        return this;
    }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", time=" + time +
                '}';
    }
}
