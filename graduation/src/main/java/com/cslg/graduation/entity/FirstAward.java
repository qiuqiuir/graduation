package com.cslg.graduation.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @auther xurou
 * @date 2023/4/23
 */
public class FirstAward {

    private String username;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date time;

    public String getUsername() {
        return username;
    }

    public FirstAward setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getName() {
        return name;
    }

    public FirstAward setName(String name) {
        this.name = name;
        return this;
    }

    public Date getTime() {
        return time;
    }

    public FirstAward setTime(Date time) {
        this.time = time;
        return this;
    }

    @Override
    public String toString() {
        return "FirstAward{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", time=" + time +
                '}';
    }
}
