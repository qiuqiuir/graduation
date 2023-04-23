package com.cslg.graduation.service;

import com.cslg.graduation.dao.AcnumberMapper;
import com.cslg.graduation.entity.Acnumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * @auther xurou
 * @date 2023/4/18
 */
@Service
public class AcnumberService {

    @Autowired
    private AcnumberMapper acnumberMapper;

    public int getAllCount(String username) {
        return acnumberMapper.allCountByUsername(username);
    }

    public int getAllCount(String username, Date time) {
        return acnumberMapper.allCountByUsernameAndTime(username, time);
    }

    public int getCount(String username, String platform) {
        return acnumberMapper.countByUsernameAndPlatform(username, platform);
    }

    public int getCount(String username, String platform, Date time) {
        return acnumberMapper.countByUsernameAndPlatformAndTime(username, platform, time);
    }

    public void addAcnumber(Acnumber acnumber) {
        acnumberMapper.insertAcnumber(acnumber);
    }

    public void updateAcnumber(String username, String platform, int count, Date time) {
        acnumberMapper.updateAcnumber(username, platform, count, time);
    }

    public void insert(Acnumber acnumber){
        acnumberMapper.insertAcnumber(acnumber);
    }

    public List<Acnumber> selectAcNumberByUsername(String username){
        List<Acnumber> awardList = acnumberMapper.selectAcNumberByUsername(username);
        Collections.sort(awardList, new Comparator<Acnumber>() {
            @Override
            public int compare(Acnumber o1, Acnumber o2) {
                return o1.getTime().compareTo(o2.getTime());
            }
        });
        return awardList;
    }

}
