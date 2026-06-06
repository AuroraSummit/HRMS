package com.hrms.module.training.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrms.module.training.dao.TrainingRecordDao;
import com.hrms.module.training.entity.TrainingRecord;
import com.hrms.module.training.service.TrainingRecordService;
import org.springframework.stereotype.Service;

@Service
public class TrainingRecordServiceImpl extends ServiceImpl<TrainingRecordDao, TrainingRecord> implements TrainingRecordService {
}
