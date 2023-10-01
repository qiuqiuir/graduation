package com.cslg.graduation.dao;

import com.cslg.graduation.entity.Award;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @auther xurou
 * @date 2023/4/13
 */
@Mapper
public interface AwardMapper {
    List<Award> selectAllAward();

    List<Award> selectAwardByUsername(String username);

    void insertAward(Award award);

    List<Award> selectAwardById(int id);

    List<Award> selectAwardByIdAndType(int id, String type);

    int selectNumberById(int id);

    int selectNumberByIdAndType(int id,String type);

    int selectCountAward();

    int selectCountRenci();

    int selectCountPeople();

    List<String> selectAwardsByIdAndNumber(int id, int number, String type);

    List<String> selectAllTeacher();


}
