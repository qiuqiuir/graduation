package com.cslg.graduation.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @auther xurou
 * @date 2023/3/30
 */
public class Week {

    private int id;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date time;
    private String platform;
    private String contestId;
    private int count;
    private double sum;
    private double avg;

    public int getId() {
        return id;
    }

    public Week setId(int id) {
        this.id = id;
        return this;
    }

    public Date getTime() {
        return time;
    }

    public Week setTime(Date time) {
        this.time = time;
        return this;
    }

    public String getPlatform() {
        return platform;
    }

    public Week setPlatform(String platform) {
        this.platform = platform;
        return this;
    }

    public String getContestId() {
        return contestId;
    }

    public Week setContestId(String contestId) {
        this.contestId = contestId;
        return this;
    }

    public int getCount() {
        return count;
    }

    public Week setCount(int count) {
        this.count = count;
        return this;
    }

    public double getSum() {
        return sum;
    }

    public Week setSum(double sum) {
        this.sum = sum;
        return this;
    }

    public double getAvg() {
        return avg;
    }

    public Week setAvg(double avg) {
        this.avg = avg;
        return this;
    }

    @Override
    public String toString() {
        return "Week{" +
                "id=" + id +
                ", time=" + time +
                ", platform='" + platform + '\'' +
                ", contestId=" + contestId +
                ", count=" + count +
                ", sum=" + sum +
                ", avg=" + avg +
                '}';
    }
}
