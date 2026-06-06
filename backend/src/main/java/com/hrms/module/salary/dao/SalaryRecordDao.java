package com.hrms.module.salary.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hrms.module.salary.entity.SalaryRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SalaryRecordDao extends BaseMapper<SalaryRecord> {
}
