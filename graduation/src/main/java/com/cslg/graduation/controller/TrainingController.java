package com.cslg.graduation.controller;

import com.cslg.graduation.common.ResponseService;
import com.cslg.graduation.entity.Training;
import com.cslg.graduation.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @auther xurou
 * @date 2023/4/10
 */
@RestController
@RequestMapping("/training")
public class TrainingController {

    @Autowired
    private TrainingService trainingService;

    @ResponseBody
    @GetMapping("/getAllTraining")
    public ResponseService getAllTraining(){
        List<Training> trainingList = trainingService.getAllTraining();
        return ResponseService.createBySuccess(trainingList);
    }

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/addTraining")
    public ResponseService addTraining(@RequestBody Training training){
        trainingService.addTraining(training);
        return ResponseService.createBySuccess();
    }


}
