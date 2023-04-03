package com.cslg.graduation.entity;

/**
 * @auther xurou
 * @date 2023/4/4
 */
public class Knowledge {

    private int id;
    private String username;
    private String knowledgeName;
    private int count;

    public int getId() {
        return id;
    }

    public Knowledge setId(int id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public Knowledge setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getKnowledgeName() {
        return knowledgeName;
    }

    public Knowledge setKnowledgeName(String knowledgeName) {
        this.knowledgeName = knowledgeName;
        return this;
    }

    public int getCount() {
        return count;
    }

    public Knowledge setCount(int count) {
        this.count = count;
        return this;
    }

    @Override
    public String toString() {
        return "Knowledge{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", knowledgeName='" + knowledgeName + '\'' +
                ", count=" + count +
                '}';
    }
}
