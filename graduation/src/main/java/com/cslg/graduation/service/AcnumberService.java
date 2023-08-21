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

    /**
     * 获得学号为usernam的过题数
     * @param username
     * @return
     */
    public int getAllCount(String username) {
        return acnumberMapper.allCountByUsername(username);
    }

    /**
     * 获得学号为usernam在时间为time的过题数
     * @param username
     * @param time
     * @return
     */
    public int getAllCount(String username, Date time) {
        return acnumberMapper.allCountByUsernameAndTime(username, time);
    }

    /**
     * 获得学号为usernam在platform平台的过题数
     * @param username
     * @param platform
     * @return
     */
    public int getCount(String username, String platform) {
        return acnumberMapper.countByUsernameAndPlatform(username, platform);
    }

    /**
     * 获得学号为usernam在platform平台时间为time的过题数
     * @param username
     * @param platform
     * @param time
     * @return
     */
    public int getCount(String username, String platform, Date time) {
        return acnumberMapper.countByUsernameAndPlatformAndTime(username, platform, time);
    }

    /**
     * 新增一条数据
     * @param acnumber
     */
    public void addAcnumber(Acnumber acnumber) {
        acnumberMapper.insertAcnumber(acnumber);
    }

    /**
     * 更新数据
     * @param username
     * @param platform
     * @param count
     * @param time
     */
    public void updateAcnumber(String username, String platform, int count, Date time) {
        acnumberMapper.updateAcnumber(username, platform, count, time);
    }

    /**
     * 选择一个username的所有过题数据
     * @param username
     * @return
     */
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
