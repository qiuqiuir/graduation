package com.cslg.graduation.dao;

import com.cslg.graduation.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {


    User selectByUsername(String username);

    User selectByName(String name);


    int insertUser(User user);

    int updateStatus(int id, int status);

    int updateHeader(int id, String headerUrl);

    int updatePassword(int id, String password);

    List<String> selectIsScoreUsers();



}
