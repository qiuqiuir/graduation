package com.cslg.graduation.entity;

/**
 * @auther xurou
 * @date 2023/4/9
 */
public class Training {
    private int id;
    private String platform;
    private String title;
    private String url;

    public int getId() {
        return id;
    }

    public Training setId(int id) {
        this.id = id;
        return this;
    }

    public String getPlatform() {
        return platform;
    }

    public Training setPlatform(String platform) {
        this.platform = platform;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Training setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Training setUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public String toString() {
        return "Training{" +
                "id=" + id +
                ", platform='" + platform + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
