package com.hrms.module.notice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrms.module.notice.dao.NoticeDao;
import com.hrms.module.notice.entity.Notice;
import com.hrms.module.notice.service.NoticeService;
import org.springframework.stereotype.Service;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeDao, Notice> implements NoticeService {
}
