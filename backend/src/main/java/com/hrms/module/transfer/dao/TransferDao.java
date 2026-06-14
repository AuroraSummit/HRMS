package com.hrms.module.transfer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hrms.module.transfer.entity.Transfer;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TransferDao extends BaseMapper<Transfer> {
}
