package com.cslg.graduation.dao;

import com.cslg.graduation.entity.Training;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @auther xurou
 * @date 2023/4/9
 */
@Mapper
public interface TrainingMapper {

    public void insertTraining(Training training);

    public List<Training> selectAllTraining();
}
