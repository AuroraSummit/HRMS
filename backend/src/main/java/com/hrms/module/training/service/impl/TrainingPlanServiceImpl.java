package com.hrms.module.training.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrms.module.training.dao.TrainingPlanDao;
import com.hrms.module.training.entity.TrainingPlan;
import com.hrms.module.training.service.TrainingPlanService;
import org.springframework.stereotype.Service;

@Service
public class TrainingPlanServiceImpl extends ServiceImpl<TrainingPlanDao, TrainingPlan> implements TrainingPlanService {
}
