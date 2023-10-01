package com.cslg.graduation.service;

import com.cslg.graduation.dao.AcnumberMapper;
import com.cslg.graduation.entity.Acnumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /**
     * 获得学号为username的过题数
     * @param username 学号
     * @return int
     */
    public int getAllCount(String username) {
        return acnumberMapper.allCountByUsername(username);
    }

    /**
     * 获得学号为username在时间为time的过题数
     * @param username 学号
     * @param time 时间
     * @return int
     */
    public int getAllCount(String username, Date time) {
        return acnumberMapper.allCountByUsernameAndTime(username, time);
    }

    /**
     * 获得学号为username在platform平台的过题数
     * @param username 学号
     * @param platform 平台
     * @return 过题数
     */
    public int getCount(String username, String platform) {
        return acnumberMapper.countByUsernameAndPlatform(username, platform);
    }

    /**
     * 获得学号为username在platform平台时间为time的过题数
     * @param username 学号
     * @param platform 平台
     * @param time 时间
     * @return 过题数
     */
    public int getCount(String username, String platform, Date time) {
        return acnumberMapper.countByUsernameAndPlatformAndTime(username, platform, time);
    }

    /**
     * 新增一条数据
     * @param acnumber 过题数数据
     */
    public void addAcnumber(Acnumber acnumber) {
        acnumberMapper.insertAcnumber(acnumber);
    }

    /**
     * 更新数据
     * @param username 学号
     * @param platform 平台
     * @param count 过题数
     * @param time 时间
     */
    public void updateAcnumber(String username, String platform, int count, Date time) {
        acnumberMapper.updateAcnumber(username, platform, count, time);
    }

    /**
     * 选择一个username的所有过题数据
     * @param username 学号
     * @return List<Acnumber>
     */
    public List<Acnumber> selectAcNumberByUsername(String username){
        List<Acnumber> awardList = acnumberMapper.selectAcNumberByUsername(username);
        awardList.sort(Comparator.comparing(Acnumber::getTime));
        return awardList;
    }

}
