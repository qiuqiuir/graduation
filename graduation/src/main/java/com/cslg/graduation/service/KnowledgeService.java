package com.cslg.graduation.service;

import com.cslg.graduation.dao.KnowledgeMapper;
import com.cslg.graduation.entity.Knowledge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auther xurou
 * @date 2023/4/4
 */
@Service
public class KnowledgeService {

    @Autowired
    private KnowledgeMapper knowledgeMapper;

    @Autowired
    private SpiderService spiderService;

    /**
     * 新增一条知识点
     *
     * @param knowledge
     */
    public void insertKnowledge(Knowledge knowledge) {
        knowledgeMapper.insertknowledge(knowledge);
    }

    /**
     * 将username的knowledgeName知识点新增count个
     *
     * @param username
     * @param knowledgeName
     * @param count
     */
    public void addKnowledgeCount(String username, String knowledgeName, int count) {
        int number = knowledgeMapper.selectKnowledgeByUsernameAndKnowledgeName(username, knowledgeName);
        if (number == 0) {
            Knowledge knowledge = new Knowledge()
                    .setUsername(username)
                    .setKnowledgeName(knowledgeName)
                    .setCount(count);
            insertKnowledge(knowledge);
        }
        number += count;
        knowledgeMapper.updateKnowledgeCount(username, knowledgeName, number);
    }

    /**
     * 修改username的knowledgeName知识点的数量为count
     *
     * @param username
     * @param knowledgeName
     * @param count
     */
    public void updateKnowledgeCount(String username, String knowledgeName, int count) {
        int number = knowledgeMapper.selectKnowledgeByUsernameAndKnowledgeName(username, knowledgeName);
        if(number>0) {
            knowledgeMapper.updateKnowledgeCount(username, knowledgeName, count);
        }else{
            Knowledge knowledge = new Knowledge()
                    .setUsername(username)
                    .setKnowledgeName(knowledgeName)
                    .setCount(count);
            insertKnowledge(knowledge);
        }
    }

    /**
     * 一次性新增username学号，cf对应id的全部知识点
     * @param username
     * @param id
     */
    public void addCodeforcesKnowledge(String username, String id) {
        Map<String, Integer> knowledges = spiderService.getCfSubmission(id);
        for (Map.Entry<String, Integer> entry : knowledges.entrySet()) {
            updateKnowledgeCount(username, entry.getKey(), entry.getValue());
        }
    }

    /**
     * 获取username的所有知识点
     * @param username
     * @return
     */
    public List<Knowledge> getAllKnowledgeByUsername(String username){
        return knowledgeMapper.selectKnowledgeByUsername(username);
    }

}
