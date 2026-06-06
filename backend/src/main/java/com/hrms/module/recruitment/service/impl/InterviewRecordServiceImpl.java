package com.hrms.module.recruitment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrms.module.recruitment.dao.InterviewRecordDao;
import com.hrms.module.recruitment.entity.InterviewRecord;
import com.hrms.module.recruitment.service.InterviewRecordService;
import org.springframework.stereotype.Service;

@Service
public class InterviewRecordServiceImpl extends ServiceImpl<InterviewRecordDao, InterviewRecord> implements InterviewRecordService {
}
