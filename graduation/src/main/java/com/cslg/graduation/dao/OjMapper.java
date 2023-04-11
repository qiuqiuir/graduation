package com.cslg.graduation.dao;

import com.cslg.graduation.entity.Oj;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @auther xurou
 * @date 2023/3/31
 */
@Mapper
public interface OjMapper {

    public void insertOj(Oj oj);

    // 获取用户在某个平台上的所有账号
    public List<String> selectListOjId(String username, String platform);

    public List<Oj> selectAllOj();

    public List<Oj> selectListOj(String username);


}
