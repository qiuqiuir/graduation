package com.cslg.graduation.entity;

/**
 * @auther xurou
 * @date 2023/3/30
 */
public class Contest {

    private int id;
    private String username;
    private String level;
    private String name;
    private int year;
    private String type;

    public int getId() {
        return id;
    }

    public Contest setId(int id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public Contest setUsername(String username) {
        this.username = username;
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

    public int getYear() {
        return year;
    }

    public Contest setYear(int year) {
        this.year = year;
        return this;
    }

    public String getType() {
        return type;
    }

    public Contest setType(String type) {
        this.type = type;
        return this;
    }

    @Override
    public String toString() {
        return "Contest{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", level='" + level + '\'' +
                ", name='" + name + '\'' +
                ", year=" + year +
                ", type='" + type + '\'' +
                '}';
    }
}
