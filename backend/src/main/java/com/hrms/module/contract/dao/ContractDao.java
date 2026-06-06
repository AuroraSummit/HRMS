package com.hrms.module.contract.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hrms.module.contract.entity.Contract;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ContractDao extends BaseMapper<Contract> {
}
