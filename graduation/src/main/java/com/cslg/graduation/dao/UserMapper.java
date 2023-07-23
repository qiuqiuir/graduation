package com.cslg.graduation.dao;

import com.cslg.graduation.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {


    User selectByUsername(String username);

    User selectByName(String name);


    int insertUser(User user);

    void updateStatus(String username, int status);
    void updateIsScore(String username, int isScore);

    void updatePassword(String username, String password);

    List<User> selectAllUsers();

    void updateUser(User user);

    List<User> selectUsersBySession(int session);

    List<String> selectMajorBySession(int session);

    List<User> selectIsScoreUsers();


}
