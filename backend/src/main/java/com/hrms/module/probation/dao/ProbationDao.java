package com.hrms.module.probation.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hrms.module.probation.entity.Probation;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProbationDao extends BaseMapper<Probation> {
}
