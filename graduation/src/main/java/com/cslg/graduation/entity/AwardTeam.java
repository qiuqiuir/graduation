package com.cslg.graduation.entity;

import java.util.List;

/**
 * @auther xurou
 * @date 2023/4/13
 */
public class AwardTeam {
    private List<String> team;
    private int id;
    private String type;

    private String teacher;

    private String url;

    public List<String> getTeam() {
        return team;
    }

    public AwardTeam setTeam(List<String> team) {
        this.team = team;
        return this;
    }

    public int getId() {
        return id;
    }

    public AwardTeam setId(int id) {
        this.id = id;
        return this;
    }

    public String getType() {
        return type;
    }

    public AwardTeam setType(String type) {
        this.type = type;
        return this;
    }

    public String getTeacher() {
        return teacher;
    }

    public AwardTeam setTeacher(String teacher) {
        this.teacher = teacher;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public AwardTeam setUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public String toString() {
        return "AwardTeam{" +
                "team=" + team +
                ", id=" + id +
                ", type='" + type + '\'' +
                ", teacher='" + teacher + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
