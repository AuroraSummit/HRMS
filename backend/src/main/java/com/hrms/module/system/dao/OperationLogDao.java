package com.hrms.module.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hrms.module.system.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OperationLogDao extends BaseMapper<OperationLog> {
}
