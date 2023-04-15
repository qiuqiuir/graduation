package com.cslg.graduation.entity;

/**
 * @auther xurou
 * @date 2023/4/13
 */
public class Award {
    private int id;
    private String username;
    private int contestId;
    private int number;
    private String type;

    public int getId() {
        return id;
    }

    public Award setId(int id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public Award setUsername(String username) {
        this.username = username;
        return this;
    }

    public int getContestId() {
        return contestId;
    }

    public Award setContestId(int contestId) {
        this.contestId = contestId;
        return this;
    }

    public int getNumber() {
        return number;
    }

    public Award setNumber(int number) {
        this.number = number;
        return this;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Award{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", contestId=" + contestId +
                ", number=" + number +
                ", type='" + type + '\'' +
                '}';
    }

    public Award setType(String type) {
        this.type = type;
        return this;


    }
}
