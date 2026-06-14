package com.hrms.module.resignation.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hrms.module.resignation.entity.Resignation;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ResignationDao extends BaseMapper<Resignation> {
}
