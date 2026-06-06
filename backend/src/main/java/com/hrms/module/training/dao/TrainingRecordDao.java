package com.hrms.module.training.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hrms.module.training.entity.TrainingRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TrainingRecordDao extends BaseMapper<TrainingRecord> {
}
