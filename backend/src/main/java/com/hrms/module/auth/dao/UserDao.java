package com.hrms.module.auth.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hrms.module.auth.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao extends BaseMapper<User> {
}
