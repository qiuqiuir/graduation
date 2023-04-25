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

    @Override
    public String toString() {
        return "AwardTeam{" +
                "team=" + team +
                ", id=" + id +
                ", type='" + type + '\'' +
                '}';
    }
}
