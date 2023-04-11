package com.cslg.graduation.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @auther xurou
 * @date 2022/6/7
 */
public class User {

    private String username;
    private String password;
    private int status;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date createDate;
    private String major;
    private String identityCard;
    private String phone;
    private String email;
    private String name;
    private String headerUrl;
    private String clothingSize;
    private String gender;
    private int isScore;

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public User setStatus(int status) {
        this.status = status;
        return this;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public User setCreateDate(Date createDate) {
        this.createDate = createDate;
        return this;
    }

    public String getMajor() {
        return major;
    }

    public User setMajor(String major) {
        this.major = major;
        return this;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public User setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public User setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getHeaderUrl() {
        return headerUrl;
    }

    public User setHeaderUrl(String headerUrl) {
        this.headerUrl = headerUrl;
        return this;
    }

    public String getClothingSize() {
        return clothingSize;
    }

    public User setClothingSize(String clothingSize) {
        this.clothingSize = clothingSize;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public User setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public int getIsScore() {
        return isScore;
    }

    public User setIsScore(int isScore) {
        this.isScore = isScore;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", status=" + status +
                ", createDate=" + createDate +
                ", major='" + major + '\'' +
                ", identityCard='" + identityCard + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", headerUrl='" + headerUrl + '\'' +
                ", clothingSize='" + clothingSize + '\'' +
                ", gender='" + gender + '\'' +
                ", isScore=" + isScore +
                '}';
    }
}
