package com.cslg.graduation.service;

import com.cslg.graduation.dao.TrainingMapper;
import com.cslg.graduation.entity.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @auther xurou
 * @date 2023/4/10
 */
@Service
public class TrainingService {

    @Autowired
    private TrainingMapper trainingMapper;

    public void addTraining(Training training){
        trainingMapper.insertTraining(training);
    }

    public List<Training> getAllTraining(){
        return trainingMapper.selectAllTraining();
    }

}
