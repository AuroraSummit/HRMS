package com.hrms.module.performance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrms.module.performance.dao.PerformanceReviewDao;
import com.hrms.module.performance.entity.PerformanceReview;
import com.hrms.module.performance.service.PerformanceReviewService;
import org.springframework.stereotype.Service;

@Service
public class PerformanceReviewServiceImpl extends ServiceImpl<PerformanceReviewDao, PerformanceReview> implements PerformanceReviewService {
}
