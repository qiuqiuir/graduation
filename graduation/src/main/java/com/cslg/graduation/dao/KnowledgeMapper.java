package com.cslg.graduation.dao;

import com.cslg.graduation.entity.Knowledge;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @auther xurou
 * @date 2023/4/4
 */
@Mapper
public interface KnowledgeMapper {

    // 新增知识点
    public void insertknowledge(Knowledge knowledge);

    // 修改知识点过题数
    public void updateKnowledgeCount(String username,String knowledgeName,int count);

    // 选择某个用户通过的所有知识点
    public List<Knowledge> selectKnowledgeByUsername(String username);

    public int selectKnowledgeByUsernameAndKnowledgeName(String username, String knowledgeName);


}
