-- ============================================================
-- HRMS 人力资源管理系统 - 数据库初始化脚本
-- 数据库: MySQL 8.0+
-- ============================================================

CREATE DATABASE IF NOT EXISTS hrms DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE hrms;

-- ============================================================
-- 1. 系统管理模块
-- ============================================================

-- 角色表
CREATE TABLE IF NOT EXISTS `sys_role` (
    `id` BIGINT AUTO_INCREMENT COMMENT '角色ID',
    `name` VARCHAR(64) NOT NULL COMMENT '角色名称',
    `code` VARCHAR(64) NOT NULL COMMENT '角色编码',
    `description` VARCHAR(255) DEFAULT '' COMMENT '角色描述',
    `status` TINYINT DEFAULT 0 COMMENT '状态(0正常 1停用)',
    `sort` INT DEFAULT 0 COMMENT '排序',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 用户-角色关联表
CREATE TABLE IF NOT EXISTS `sys_user_role` (
    `id` BIGINT AUTO_INCREMENT COMMENT '关联ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_role` (`user_id`, `role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- ============================================================
-- 2. 认证模块
-- ============================================================

-- 用户表
CREATE TABLE IF NOT EXISTS `auth_user` (
    `id` BIGINT AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(64) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码',
    `real_name` VARCHAR(64) DEFAULT NULL COMMENT '真实姓名',
    `email` VARCHAR(128) DEFAULT NULL COMMENT '邮箱',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `status` TINYINT DEFAULT 0 COMMENT '状态(0正常 1停用)',
    `type` TINYINT DEFAULT 1 COMMENT '用户类型(1系统用户 2管理员)',
    `emp_id` BIGINT DEFAULT NULL COMMENT '关联员工ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_emp_id` (`emp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- ============================================================
-- 3. 组织架构模块
-- ============================================================

-- 部门表
CREATE TABLE IF NOT EXISTS `org_department` (
    `id` BIGINT AUTO_INCREMENT COMMENT '部门ID',
    `name` VARCHAR(64) NOT NULL COMMENT '部门名称',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父部门ID',
    `path` VARCHAR(200) DEFAULT '' COMMENT '部门路径',
    `sort` INT DEFAULT 0 COMMENT '排序',
    `leader` VARCHAR(32) DEFAULT NULL COMMENT '负责人',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    `email` VARCHAR(128) DEFAULT NULL COMMENT '邮箱',
    `status` TINYINT DEFAULT 0 COMMENT '状态(0正常 1停用)',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- 岗位表
CREATE TABLE IF NOT EXISTS `org_position` (
    `id` BIGINT AUTO_INCREMENT COMMENT '岗位ID',
    `name` VARCHAR(64) NOT NULL COMMENT '岗位名称',
    `code` VARCHAR(64) DEFAULT NULL COMMENT '岗位编码',
    `dept_id` BIGINT DEFAULT NULL COMMENT '所属部门ID',
    `sort` INT DEFAULT 0 COMMENT '排序',
    `status` TINYINT DEFAULT 0 COMMENT '状态(0正常 1停用)',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`),
    KEY `idx_dept_id` (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='岗位表';

-- ============================================================
-- 4. 员工管理模块
-- ============================================================

-- 员工表
CREATE TABLE IF NOT EXISTS `emp_employee` (
    `id` BIGINT AUTO_INCREMENT COMMENT '员工ID',
    `emp_no` VARCHAR(32) NOT NULL COMMENT '工号',
    `name` VARCHAR(64) NOT NULL COMMENT '姓名',
    `gender` TINYINT DEFAULT NULL COMMENT '性别(0男 1女 2未知)',
    `birthday` DATE DEFAULT NULL COMMENT '出生日期',
    `id_card` VARCHAR(18) DEFAULT NULL COMMENT '身份证号',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `email` VARCHAR(128) DEFAULT NULL COMMENT '邮箱',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `dept_id` BIGINT DEFAULT NULL COMMENT '部门ID',
    `position_id` BIGINT DEFAULT NULL COMMENT '岗位ID',
    `hire_date` DATE DEFAULT NULL COMMENT '入职日期',
    `status` VARCHAR(20) DEFAULT 'active' COMMENT '状态(active在职 resigned离职 probation试用期)',
    `employment_type` TINYINT DEFAULT NULL COMMENT '聘用类型(1正式 2实习 3劳务 4兼职)',
    `source` TINYINT DEFAULT NULL COMMENT '来源(1社会招聘 2校园招聘 3内部推荐 4其他)',
    `emergency_contact` VARCHAR(32) DEFAULT NULL COMMENT '紧急联系人',
    `emergency_phone` VARCHAR(20) DEFAULT NULL COMMENT '紧急联系电话',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_emp_no` (`emp_no`),
    KEY `idx_dept_id` (`dept_id`),
    KEY `idx_position_id` (`position_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工表';

-- 教育经历表
CREATE TABLE IF NOT EXISTS `emp_education` (
    `id` BIGINT AUTO_INCREMENT COMMENT '记录ID',
    `emp_id` BIGINT NOT NULL COMMENT '员工ID',
    `school` VARCHAR(128) NOT NULL COMMENT '学校名称',
    `major` VARCHAR(128) DEFAULT NULL COMMENT '专业',
    `degree` TINYINT DEFAULT NULL COMMENT '学历(1高中 2大专 3本科 4硕士 5博士)',
    `start_date` DATE DEFAULT NULL COMMENT '开始日期',
    `end_date` DATE DEFAULT NULL COMMENT '结束日期',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`),
    KEY `idx_emp_id` (`emp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教育经历表';

-- 工作经历表
CREATE TABLE IF NOT EXISTS `emp_work_experience` (
    `id` BIGINT AUTO_INCREMENT COMMENT '记录ID',
    `emp_id` BIGINT NOT NULL COMMENT '员工ID',
    `company` VARCHAR(128) NOT NULL COMMENT '公司名称',
    `position` VARCHAR(64) DEFAULT NULL COMMENT '职位',
    `start_date` DATE DEFAULT NULL COMMENT '开始日期',
    `end_date` DATE DEFAULT NULL COMMENT '结束日期',
    `description` TEXT DEFAULT NULL COMMENT '工作描述',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`),
    KEY `idx_emp_id` (`emp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工作经历表';

-- ============================================================
-- 5. 考勤管理模块
-- ============================================================

-- 考勤记录表
CREATE TABLE IF NOT EXISTS `att_attendance` (
    `id` BIGINT AUTO_INCREMENT COMMENT '记录ID',
    `emp_id` BIGINT NOT NULL COMMENT '员工ID',
    `attendance_date` DATE NOT NULL COMMENT '考勤日期',
    `clock_in_time` DATETIME DEFAULT NULL COMMENT '签到时间',
    `clock_out_time` DATETIME DEFAULT NULL COMMENT '签退时间',
    `status` TINYINT DEFAULT 0 COMMENT '状态(0正常 1迟到 2早退 3缺勤 4异常)',
    `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`),
    KEY `idx_emp_id` (`emp_id`),
    KEY `idx_date` (`attendance_date`),
    UNIQUE KEY `uk_emp_date` (`emp_id`, `attendance_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='考勤记录表';

-- 请假申请表
CREATE TABLE IF NOT EXISTS `att_leave` (
    `id` BIGINT AUTO_INCREMENT COMMENT '申请ID',
    `emp_id` BIGINT NOT NULL COMMENT '员工ID',
    `leave_type` TINYINT NOT NULL COMMENT '请假类型(1年假 2事假 3病假 4婚假 5产假 6丧假 7其他)',
    `start_date` DATE NOT NULL COMMENT '开始日期',
    `end_date` DATE NOT NULL COMMENT '结束日期',
    `duration` DECIMAL(5,1) NOT NULL COMMENT '时长(天)',
    `reason` VARCHAR(500) DEFAULT NULL COMMENT '请假原因',
    `status` TINYINT DEFAULT 0 COMMENT '状态(0待审批 1已通过 2已驳回 3已取消)',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`),
    KEY `idx_emp_id` (`emp_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='请假申请表';

-- ============================================================
-- 6. 薪酬管理模块
-- ============================================================

-- 薪资配置表
CREATE TABLE IF NOT EXISTS `sal_config` (
    `id` BIGINT AUTO_INCREMENT COMMENT '配置ID',
    `name` VARCHAR(64) NOT NULL COMMENT '配置名称',
    `basic_salary` DECIMAL(12,2) NOT NULL COMMENT '基本工资',
    `housing_fund_rate` DECIMAL(5,2) DEFAULT 0 COMMENT '公积金比例(%)',
    `pension_rate` DECIMAL(5,2) DEFAULT 0 COMMENT '养老保险比例(%)',
    `medical_rate` DECIMAL(5,2) DEFAULT 0 COMMENT '医疗保险比例(%)',
    `unemployment_rate` DECIMAL(5,2) DEFAULT 0 COMMENT '失业保险比例(%)',
    `injury_rate` DECIMAL(5,2) DEFAULT 0 COMMENT '工伤保险比例(%)',
    `maternity_rate` DECIMAL(5,2) DEFAULT 0 COMMENT '生育保险比例(%)',
    `tax_exemption` DECIMAL(12,2) DEFAULT 5000 COMMENT '个税起征点',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='薪资配置表';

-- 薪资记录表
CREATE TABLE IF NOT EXISTS `sal_record` (
    `id` BIGINT AUTO_INCREMENT COMMENT '记录ID',
    `emp_id` BIGINT NOT NULL COMMENT '员工ID',
    `month` VARCHAR(7) NOT NULL COMMENT '薪资月份(YYYY-MM)',
    `basic_salary` DECIMAL(12,2) DEFAULT 0 COMMENT '基本工资',
    `allowance` DECIMAL(12,2) DEFAULT 0 COMMENT '津贴',
    `overtime_pay` DECIMAL(12,2) DEFAULT 0 COMMENT '加班费',
    `bonus` DECIMAL(12,2) DEFAULT 0 COMMENT '奖金',
    `deduction` DECIMAL(12,2) DEFAULT 0 COMMENT '扣款',
    `housing_fund` DECIMAL(12,2) DEFAULT 0 COMMENT '公积金',
    `pension` DECIMAL(12,2) DEFAULT 0 COMMENT '养老险',
    `medical` DECIMAL(12,2) DEFAULT 0 COMMENT '医疗险',
    `unemployment` DECIMAL(12,2) DEFAULT 0 COMMENT '失业险',
    `tax` DECIMAL(12,2) DEFAULT 0 COMMENT '个税',
    `actual_salary` DECIMAL(12,2) DEFAULT 0 COMMENT '实发工资',
    `status` TINYINT DEFAULT 0 COMMENT '状态(0待发放 1已发放 2已撤回)',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`),
    KEY `idx_emp_id` (`emp_id`),
    KEY `idx_month` (`month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='薪资记录表';

-- ============================================================
-- 7. 绩效管理模块
-- ============================================================

-- 绩效模板表
CREATE TABLE IF NOT EXISTS `perf_template` (
    `id` BIGINT AUTO_INCREMENT COMMENT '模板ID',
    `name` VARCHAR(64) NOT NULL COMMENT '模板名称',
    `description` TEXT DEFAULT NULL COMMENT '模板描述',
    `status` TINYINT DEFAULT 0 COMMENT '状态(0启用 1停用)',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='绩效模板表';

-- 绩效计划表
CREATE TABLE IF NOT EXISTS `perf_plan` (
    `id` BIGINT AUTO_INCREMENT COMMENT '计划ID',
    `template_id` BIGINT DEFAULT NULL COMMENT '模板ID',
    `name` VARCHAR(64) NOT NULL COMMENT '计划名称',
    `start_date` DATE DEFAULT NULL COMMENT '开始日期',
    `end_date` DATE DEFAULT NULL COMMENT '结束日期',
    `status` TINYINT DEFAULT 0 COMMENT '状态(0待开始 1进行中 2已完成 3已取消)',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`),
    KEY `idx_template_id` (`template_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='绩效计划表';

-- 绩效评审表
CREATE TABLE IF NOT EXISTS `perf_review` (
    `id` BIGINT AUTO_INCREMENT COMMENT '评审ID',
    `plan_id` BIGINT NOT NULL COMMENT '计划ID',
    `emp_id` BIGINT NOT NULL COMMENT '员工ID',
    `self_score` DECIMAL(5,2) DEFAULT NULL COMMENT '自评分',
    `self_comment` TEXT DEFAULT NULL COMMENT '自评意见',
    `manager_score` DECIMAL(5,2) DEFAULT NULL COMMENT '主管评分',
    `manager_comment` TEXT DEFAULT NULL COMMENT '主管意见',
    `hr_score` DECIMAL(5,2) DEFAULT NULL COMMENT 'HR评分',
    `hr_comment` TEXT DEFAULT NULL COMMENT 'HR意见',
    `final_score` DECIMAL(5,2) DEFAULT NULL COMMENT '最终得分',
    `result` TINYINT DEFAULT NULL COMMENT '结果(1优秀 2良好 3合格 4待改进 5不合格)',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`),
    KEY `idx_plan_id` (`plan_id`),
    KEY `idx_emp_id` (`emp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='绩效评审表';

-- ============================================================
-- 8. 招聘管理模块
-- ============================================================

-- 招聘需求表
CREATE TABLE IF NOT EXISTS `rec_demand` (
    `id` BIGINT AUTO_INCREMENT COMMENT '需求ID',
    `title` VARCHAR(128) NOT NULL COMMENT '招聘职位',
    `dept_id` BIGINT DEFAULT NULL COMMENT '需求部门ID',
    `position_id` BIGINT DEFAULT NULL COMMENT '岗位ID',
    `headcount` INT DEFAULT 1 COMMENT '招聘人数',
    `current_count` INT DEFAULT 0 COMMENT '已招人数',
    `description` TEXT DEFAULT NULL COMMENT '职位描述',
    `requirement` TEXT DEFAULT NULL COMMENT '任职要求',
    `salary_min` DECIMAL(12,2) DEFAULT NULL COMMENT '最低薪资',
    `salary_max` DECIMAL(12,2) DEFAULT NULL COMMENT '最高薪资',
    `status` TINYINT DEFAULT 0 COMMENT '状态(0待审批 1招聘中 2已关闭 3已完成)',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`),
    KEY `idx_dept_id` (`dept_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='招聘需求表';

-- 面试记录表
CREATE TABLE IF NOT EXISTS `rec_interview` (
    `id` BIGINT AUTO_INCREMENT COMMENT '记录ID',
    `demand_id` BIGINT DEFAULT NULL COMMENT '需求ID',
    `candidate_name` VARCHAR(64) NOT NULL COMMENT '候选人姓名',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '电话',
    `email` VARCHAR(128) DEFAULT NULL COMMENT '邮箱',
    `position` VARCHAR(64) DEFAULT NULL COMMENT '应聘职位',
    `resume_url` VARCHAR(255) DEFAULT NULL COMMENT '简历URL',
    `interview_date` DATETIME DEFAULT NULL COMMENT '面试时间',
    `interviewer` VARCHAR(32) DEFAULT NULL COMMENT '面试官',
    `round` TINYINT DEFAULT 1 COMMENT '面试轮次',
    `result` TINYINT DEFAULT NULL COMMENT '结果(0未开始 1通过 2未通过 3待定)',
    `comment` TEXT DEFAULT NULL COMMENT '面试评价',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`),
    KEY `idx_demand_id` (`demand_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='面试记录表';

-- Offer记录表
CREATE TABLE IF NOT EXISTS `rec_offer` (
    `id` BIGINT AUTO_INCREMENT COMMENT 'OfferID',
    `demand_id` BIGINT DEFAULT NULL COMMENT '需求ID',
    `candidate_name` VARCHAR(64) NOT NULL COMMENT '候选人姓名',
    `offer_salary` DECIMAL(12,2) DEFAULT NULL COMMENT 'Offer薪资',
    `offer_date` DATE DEFAULT NULL COMMENT 'Offer发放日期',
    `expiry_date` DATE DEFAULT NULL COMMENT '有效期',
    `status` TINYINT DEFAULT 0 COMMENT '状态(0待发送 1已发送 2已接受 3已拒绝 4已撤回)',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`),
    KEY `idx_demand_id` (`demand_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Offer记录表';

-- ============================================================
-- 9. 培训管理模块
-- ============================================================

-- 培训计划表
CREATE TABLE IF NOT EXISTS `train_plan` (
    `id` BIGINT AUTO_INCREMENT COMMENT '计划ID',
    `name` VARCHAR(128) NOT NULL COMMENT '培训名称',
    `description` TEXT DEFAULT NULL COMMENT '培训描述',
    `start_date` DATE DEFAULT NULL COMMENT '开始日期',
    `end_date` DATE DEFAULT NULL COMMENT '结束日期',
    `location` VARCHAR(128) DEFAULT NULL COMMENT '培训地点',
    `instructor` VARCHAR(64) DEFAULT NULL COMMENT '讲师',
    `max_attendees` INT DEFAULT NULL COMMENT '最大人数',
    `status` TINYINT DEFAULT 0 COMMENT '状态(0待开始 1进行中 2已完成 3已取消)',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='培训计划表';

-- 培训记录表
CREATE TABLE IF NOT EXISTS `train_record` (
    `id` BIGINT AUTO_INCREMENT COMMENT '记录ID',
    `plan_id` BIGINT NOT NULL COMMENT '计划ID',
    `emp_id` BIGINT NOT NULL COMMENT '员工ID',
    `attendance` TINYINT DEFAULT NULL COMMENT '出勤(0缺席 1出勤 2请假)',
    `score` DECIMAL(5,2) DEFAULT NULL COMMENT '考核分数',
    `evaluation` TEXT DEFAULT NULL COMMENT '培训评价',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`),
    KEY `idx_plan_id` (`plan_id`),
    KEY `idx_emp_id` (`emp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='培训记录表';

-- ============================================================
-- 10. 合同管理模块
-- ============================================================

-- 合同表
CREATE TABLE IF NOT EXISTS `ctr_contract` (
    `id` BIGINT AUTO_INCREMENT COMMENT '合同ID',
    `emp_id` BIGINT NOT NULL COMMENT '员工ID',
    `contract_type` TINYINT DEFAULT NULL COMMENT '合同类型(1正式 2试用 3实习 4劳务 5兼职)',
    `start_date` DATE DEFAULT NULL COMMENT '合同开始日期',
    `end_date` DATE DEFAULT NULL COMMENT '合同结束日期',
    `sign_date` DATE DEFAULT NULL COMMENT '签订日期',
    `file_url` VARCHAR(255) DEFAULT NULL COMMENT '合同文件URL',
    `status` TINYINT DEFAULT 0 COMMENT '状态(0待签 1生效 2到期 3解约)',
    `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`),
    KEY `idx_emp_id` (`emp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='合同表';

-- ============================================================
-- HRMS 初始数据 (Seed Data)
-- 管理员账号: admin / admin123  (密码经过BCrypt加密)
-- ============================================================

USE hrms;

-- ============================================================
-- 1. 默认管理员
-- ============================================================
INSERT INTO `auth_user` (`username`, `password`, `real_name`, `email`, `phone`, `status`, `type`)
VALUES ('admin', '$2a$10$UtsHlU5Wi3UKuyAUlcr4deo/9l.nj1ra47y8E5F013SVDZwDbakgq', '系统管理员', 'admin@hrms.com', '13800000000', 0, 2)
ON DUPLICATE KEY UPDATE `real_name` = VALUES(`real_name`);

-- ============================================================
-- 2. 角色与用户关联数据
-- ============================================================
-- 默认角色
INSERT INTO `sys_role` (`id`, `name`, `code`, `description`, `sort`) VALUES
(1, '超级管理员', 'admin', '系统管理员，拥有全部权限', 1),
(2, '普通员工', 'employee', '普通员工，可访问员工门户', 2)
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`);

-- 给 admin 用户(uid=1)分配 admin 角色
INSERT IGNORE INTO `sys_user_role` (`user_id`, `role_id`) VALUES (1, 1);

-- 演示员工账号: employee / employee123 (BCrypt加密)
INSERT INTO `auth_user` (`username`, `password`, `real_name`, `email`, `phone`, `status`, `type`, `emp_id`)
VALUES ('employee', '$2a$10$s/3FOPTDpnjnY7WNFQNtAOMqt./eVhnEMb1p3NQMAE5Kzc4Rovo/C', '张三', 'zhangsan@hrms.com', '13900001111', 0, 1, 1)
ON DUPLICATE KEY UPDATE `real_name` = VALUES(`real_name`);

-- 给 employee 用户(uid=2)分配 employee 角色
INSERT IGNORE INTO `sys_user_role` (`user_id`, `role_id`) VALUES (2, 2);

-- 默认员工数据
INSERT INTO `emp_employee` (`id`, `emp_no`, `name`, `gender`, `phone`, `email`, `dept_id`, `position_id`, `hire_date`, `status`, `employment_type`)
VALUES (1, 'EMP2023001', '张三', 0, '13900001111', 'zhangsan@hrms.com', 2, 3, '2025-09-01', 'active', 1)
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`);

-- ============================================================
-- 3. 默认部门数据
-- ============================================================
INSERT INTO `org_department` (`id`, `name`, `parent_id`, `sort`, `status`) VALUES
(1, '总公司', 0, 1, 0),
(2, '技术部', 1, 1, 0),
(3, '市场部', 1, 2, 0),
(4, '人事部', 1, 3, 0),
(5, '财务部', 1, 4, 0),
(6, '运营部', 1, 5, 0),
(7, '行政部', 1, 6, 0)
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`);

-- ============================================================
-- ... existing code ...
-- ============================================================
-- 5. 默认岗位数据
-- ============================================================
INSERT INTO `org_position` (`id`, `name`, `code`, `dept_id`, `sort`, `status`) VALUES
                                                                                   (1, '技术总监', 'CTO', 2, 1, 0),
                                                                                   (2, '前端工程师', 'FE', 2, 2, 0),
                                                                                   (3, '后端工程师', 'BE', 2, 3, 0),
                                                                                   (4, '测试工程师', 'QA', 2, 4, 0),
                                                                                   (5, '市场总监', 'MKT_DIR', 3, 1, 0),
                                                                                   (6, '市场专员', 'MKT', 3, 2, 0),
                                                                                   (7, 'HR总监', 'HR_DIR', 4, 1, 0),
                                                                                   (8, 'HR专员', 'HR', 4, 2, 0),
                                                                                   (9, '财务总监', 'CFO', 5, 1, 0),
                                                                                   (10, '会计', 'ACC', 5, 2, 0),
                                                                                   (11, '运营总监', 'COO', 6, 1, 0),
                                                                                   (12, '运营专员', 'OPS', 6, 2, 0),
                                                                                   (13, '行政主管', 'ADMIN', 7, 1, 0),
                                                                                   (14, '行政专员', 'ADMIN_ASST', 7, 2, 0)
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`);

-- ============================================================
-- 6. 通知公告示例数据
-- ============================================================
INSERT INTO `sys_notice` (`id`, `title`, `content`, `notice_type`, `priority`, `publisher`, `publish_date`, `expire_date`, `target_role`, `status`, `view_count`) VALUES
(1, '关于2026年端午节放假安排的通知', '根据国家法定节假日安排，2026年端午节假期为6月19日至6月21日，共3天。请各部门提前做好工作安排，并在放假前检查办公室水电安全。祝大家端午安康！', 1, 1, '人力资源部', '2026-06-10', '2026-06-22', 'all', 1, 156),
(2, '2026年第二季度绩效考核启动通知', '各位同事：2026年第二季度绩效考核将于7月1日正式启动，考核周期为4月-6月。请各位员工在7月5日前完成自评，部门负责人于7月12日前完成评分。考核模板已更新，请登录绩效模块查看。', 2, 2, '人力资源部', '2026-06-08', '2026-07-15', 'all', 1, 89),
(3, '关于新版员工手册发布的通知', '《HRMS员工手册V3.0》已于2026年6月1日正式发布。新手册更新了考勤制度、加班政策、远程办公规范等内容。请全体员工仔细阅读，如有疑问请联系人力资源部。', 2, 0, '人力资源部', '2026-06-01', '2026-12-31', 'all', 1, 234),
(4, '关于办公区域装修的温馨提示', '各位同事请注意：公司3楼办公区将于6月15日-6月30日进行装修改造。施工期间可能会有噪音，建议3楼同事临时到2楼或4楼办公。已为大家准备好了临时工位，请提前与行政部联系安排。', 1, 0, '行政部', '2026-06-12', '2026-07-01', 'all', 1, 67),
(5, '"数字化转型领导力"培训报名通知', '公司将于6月25日举办"数字化转型领导力"专题培训，邀请外部专家授课。培训对象为各部门经理及以上管理人员。请有意参加的同事于6月20日前在培训模块完成报名。', 3, 0, '培训发展部', '2026-06-11', '2026-06-26', 'admin', 1, 45),
(6, '系统升级维护通知', 'HRMS系统计划于6月15日（周日）凌晨2:00-6:00进行版本升级维护，届时系统将暂停服务。请各位同事提前安排好工作，避免在维护时段使用系统。升级后将新增员工异动管理和离职管理功能。', 4, 2, '信息技术部', '2026-06-13', '2026-06-16', 'all', 1, 312)
ON DUPLICATE KEY UPDATE `title` = VALUES(`title`);

-- ============================================================
-- 7. 员工异动示例数据
-- ============================================================
INSERT INTO `emp_transfer` (`id`, `emp_id`, `transfer_type`, `from_dept_id`, `to_dept_id`, `from_position_id`, `to_position_id`, `from_salary`, `to_salary`, `transfer_date`, `reason`, `status`) VALUES
(1, 1, 1, 2, 2, 3, 1, 15000.00, 22000.00, '2026-05-15', '年度绩效优秀，晋升为技术总监', 1),
(2, 1, 4, 2, 4, 3, 8, 15000.00, 15000.00, '2026-03-01', '根据业务需要从技术部调至HR部协助系统建设', 1)
ON DUPLICATE KEY UPDATE `reason` = VALUES(`reason`);

-- ============================================================
-- 8. 转正记录示例数据
-- ============================================================
INSERT INTO `emp_probation` (`id`, `emp_id`, `probation_start`, `probation_end`, `actual_end`, `self_evaluation`, `mentor_name`, `mentor_evaluation`, `manager_evaluation`, `score`, `result`, `status`) VALUES
(1, 1, '2025-09-01', '2026-02-28', '2026-02-28', '在试用期间，我快速熟悉了公司技术架构和业务流程，主导完成了HRMS系统的核心模块开发，并与团队成员建立了良好的协作关系。', '张伟', '该员工技术能力突出，学习能力强，能够快速适应团队节奏，在试用期内已经能够独立负责重要模块的开发工作。', '表现优异，技术能力和工作态度均超出预期，具备正式员工的各项要求，建议予以转正。', 92.50, 1, 2)
ON DUPLICATE KEY UPDATE `self_evaluation` = VALUES(`self_evaluation`);

-- ============================================================
-- 9. 合同示例数据
-- ============================================================
INSERT INTO `ctr_contract` (`id`, `emp_id`, `contract_type`, `start_date`, `end_date`, `sign_date`, `status`, `remark`) VALUES
(1, 1, 2, '2025-09-01', '2026-02-28', '2025-09-01', 1, '试用期合同，为期6个月'),
(2, 1, 1, '2026-03-01', '2029-02-28', '2026-03-01', 1, '正式劳动合同，为期3年')
ON DUPLICATE KEY UPDATE `remark` = VALUES(`remark`);
