package com.cslg.graduation.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @auther xurou
 * @date 2023/4/18
 */
public class Acnumber {

    private int id;
    private String username;
    private String platform;
    private int count;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date time;

    public int getId() {
        return id;
    }

    public Acnumber setId(int id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public Acnumber setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPlatform() {
        return platform;
    }

    public Acnumber setPlatform(String platform) {
        this.platform = platform;
        return this;
    }

    public int getCount() {
        return count;
    }

    public Acnumber setCount(int count) {
        this.count = count;
        return this;
    }

    public Date getTime() {
        return time;
    }

    public Acnumber setTime(Date time) {
        this.time = time;
        return this;
    }

    @Override
    public String toString() {
        return "Acnumber{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", platform='" + platform + '\'' +
                ", count=" + count +
                ", time=" + time +
                '}';
    }
}
