package com.cslg.graduation.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @auther xurou
 * @date 2023/3/30
 */
public class Contest {

    private int id;
    private String level;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date time;
    private String remark;
    private int number;

    public int getId() {
        return id;
    }

    public Contest setId(int id) {
        this.id = id;
        return this;
    }

    public String getLevel() {
        return level;
    }

    public Contest setLevel(String level) {
        this.level = level;
        return this;
    }

    public String getName() {
        return name;
    }

    public Contest setName(String name) {
        this.name = name;
        return this;
    }

    public Date getTime() {
        return time;
    }

    public Contest setTime(Date time) {
        this.time = time;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public Contest setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public int getNumber() {
        return number;
    }

    public Contest setNumber(int number) {
        this.number = number;
        return this;
    }

    @Override
    public String toString() {
        return "Contest{" +
                "id=" + id +
                ", level='" + level + '\'' +
                ", name='" + name + '\'' +
                ", time=" + time +
                ", remark='" + remark + '\'' +
                ", number=" + number +
                '}';
    }
}
