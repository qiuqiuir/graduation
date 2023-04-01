package com.cslg.graduation.entity;

/**
 * @auther xurou
 * @date 2023/3/31
 */
public class Oj {

    private int id;
    private String platform;
    private String ojId;
    private String username;

    public int getId() {
        return id;
    }

    public Oj setId(int id) {
        this.id = id;
        return this;
    }

    public String getPlatform() {
        return platform;
    }

    public Oj setPlatform(String platform) {
        this.platform = platform;
        return this;
    }

    public String getOjId() {
        return ojId;
    }

    public Oj setOjId(String ojId) {
        this.ojId = ojId;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public Oj setUsername(String username) {
        this.username = username;
        return this;
    }

    @Override
    public String toString() {
        return "Oj{" +
                "id=" + id +
                ", platform='" + platform + '\'' +
                ", ojId='" + ojId + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
