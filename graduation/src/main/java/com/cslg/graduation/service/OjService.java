package com.cslg.graduation.service;

import com.cslg.graduation.dao.OjMapper;
import com.cslg.graduation.entity.Oj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @auther xurou
 * @date 2023/3/31
 */
@Service
public class OjService {

    @Autowired
    private OjMapper ojMapper;

    /**
     * 新增一条oj记录
     * @param oj
     */
    public void addOj(Oj oj){
        ojMapper.insertOj(oj);
    }

    /**
     * 根据学号和平台获取该用户的所有第三方id
     * @param username
     * @param platform
     * @return
     */
    public List<String> getAllOjId(String username,String platform){
        return ojMapper.selectListOjId(username, platform);
    }

}
