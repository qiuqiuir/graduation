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

    public List<String> getTeam() {
        return team;
    }

    public void setTeam(List<String> team) {
        this.team = team;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "AwardTeam{" +
                "team=" + team +
                ", id=" + id +
                ", type='" + type + '\'' +
                '}';
    }
}
