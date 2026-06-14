package com.hrms.module.notice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hrms.module.notice.entity.Notice;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoticeDao extends BaseMapper<Notice> {
}
