package com.cslg.graduation.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @auther xurou
 * @date 2023/3/29
 */
public class Score {

    private int id;
    private String username;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date time;
    private double dailyScore;
    private double totalScore;

    public int getId() {
        return id;
    }

    public Score setId(int id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public Score setUsername(String username) {
        this.username = username;
        return this;
    }

    public Date getTime() {
        return time;
    }

    public Score setTime(Date time) {
        this.time = time;
        return this;
    }

    public double getDailyScore() {
        return dailyScore;
    }

    public Score setDailyScore(double dailyScore) {
        this.dailyScore = dailyScore;
        return this;
    }

    public double getTotalScore() {
        return totalScore;
    }

    public Score setTotalScore(double totalScore) {
        this.totalScore = totalScore;
        return this;
    }

    @Override
    public String toString() {
        return "Score{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", time=" + time +
                ", dailyScore=" + dailyScore +
                ", totalScore=" + totalScore +
                '}';
    }
}
