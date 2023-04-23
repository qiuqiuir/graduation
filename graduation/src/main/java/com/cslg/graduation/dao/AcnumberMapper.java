package com.cslg.graduation.dao;

import com.cslg.graduation.entity.Acnumber;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface AcnumberMapper {

    int allCountByUsername(String username);

    int allCountByUsernameAndTime(String username, Date time);

    int countByUsernameAndPlatform(String username, String platform);

    int countByUsernameAndPlatformAndTime(String username, String platform, Date time);

    void insertAcnumber(Acnumber acnumber);

    void updateAcnumber(String username, String platform,int count,Date time);

    List<Acnumber> selectAcNumberByUsername(String username);

}
