-- ============================================================
-- HRMS 人力资源管理系统 - 数据库初始化脚本
-- 数据库: MySQL 8.0+
-- ============================================================

CREATE DATABASE IF NOT EXISTS hrms DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE hrms;

-- ============================================================
-- 1. 系统管理模块
-- ============================================================

-- 系统菜单表
CREATE TABLE IF NOT EXISTS `sys_menu` (
    `id` BIGINT AUTO_INCREMENT COMMENT '菜单ID',
    `name` VARCHAR(64) NOT NULL COMMENT '菜单名称',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父菜单ID',
    `path` VARCHAR(200) DEFAULT '' COMMENT '路由路径',
    `component` VARCHAR(200) DEFAULT NULL COMMENT '组件路径',
    `icon` VARCHAR(64) DEFAULT NULL COMMENT '图标',
    `sort` INT DEFAULT 0 COMMENT '排序',
    `type` TINYINT DEFAULT 1 COMMENT '类型(1目录 2菜单 3按钮)',
    `permission` VARCHAR(100) DEFAULT NULL COMMENT '权限标识',
    `status` TINYINT DEFAULT 0 COMMENT '状态(0正常 1停用)',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统菜单表';

-- 字典类型表
CREATE TABLE IF NOT EXISTS `sys_dict_type` (
    `id` BIGINT AUTO_INCREMENT COMMENT '字典类型ID',
    `name` VARCHAR(64) NOT NULL COMMENT '字典名称',
    `code` VARCHAR(64) NOT NULL COMMENT '字典编码',
    `status` TINYINT DEFAULT 0 COMMENT '状态(0正常 1停用)',
    `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典类型表';

-- 字典数据表
CREATE TABLE IF NOT EXISTS `sys_dict_data` (
    `id` BIGINT AUTO_INCREMENT COMMENT '字典数据ID',
    `dict_type_id` BIGINT NOT NULL COMMENT '字典类型ID',
    `label` VARCHAR(64) NOT NULL COMMENT '字典标签',
    `value` VARCHAR(64) NOT NULL COMMENT '字典键值',
    `sort` INT DEFAULT 0 COMMENT '排序',
    `status` TINYINT DEFAULT 0 COMMENT '状态(0正常 1停用)',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`),
    KEY `idx_dict_type_id` (`dict_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典数据表';

-- 系统操作日志表
CREATE TABLE IF NOT EXISTS `sys_operation_log` (
    `id` BIGINT AUTO_INCREMENT COMMENT '日志ID',
    `module` VARCHAR(32) DEFAULT NULL COMMENT '模块名称',
    `operation` VARCHAR(64) DEFAULT NULL COMMENT '操作描述',
    `request_url` VARCHAR(255) DEFAULT NULL COMMENT '请求URL',
    `request_method` VARCHAR(10) DEFAULT NULL COMMENT '请求方式',
    `request_param` TEXT DEFAULT NULL COMMENT '请求参数',
    `response_data` TEXT DEFAULT NULL COMMENT '返回数据',
    `ip` VARCHAR(64) DEFAULT NULL COMMENT '操作IP',
    `duration` BIGINT DEFAULT NULL COMMENT '耗时(ms)',
    `operator` VARCHAR(32) DEFAULT NULL COMMENT '操作人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`),
    KEY `idx_module` (`module`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统操作日志表';

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
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
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
-- 2. 默认菜单 (按路由层级)
-- ============================================================
INSERT INTO `sys_menu` (`id`, `name`, `parent_id`, `path`, `component`, `icon`, `sort`, `type`, `permission`, `status`) VALUES
-- 一级菜单
(1, '首页面板', 0, '/dashboard', 'dashboard/index', 'HomeFilled', 1, 1, NULL, 0),
(2, '组织架构', 0, '/org', 'Layout', 'OfficeBuilding', 2, 1, NULL, 0),
(3, '员工管理', 0, '/employee', 'Layout', 'UserFilled', 3, 1, NULL, 0),
(4, '招聘管理', 0, '/recruitment', 'Layout', 'Briefcase', 4, 1, NULL, 0),
(5, '考勤管理', 0, '/attendance', 'Layout', 'Clock', 5, 1, NULL, 0),
(6, '薪酬管理', 0, '/salary', 'Layout', 'Money', 6, 1, NULL, 0),
(7, '绩效管理', 0, '/performance', 'Layout', 'DataAnalysis', 7, 1, NULL, 0),
(8, '培训管理', 0, '/training', 'Layout', 'Reading', 8, 1, NULL, 0),
(9, '合同管理', 0, '/contract', 'Layout', 'Document', 9, 1, NULL, 0),
(10, '系统管理', 0, '/system', 'Layout', 'Tools', 10, 1, NULL, 0),
-- 二级菜单
(11, '首页面板', 1, '/dashboard', 'dashboard/index', 'HomeFilled', 1, 2, NULL, 0),
(12, '部门管理', 2, '/org/dept', 'org/Dept', 'List', 1, 2, NULL, 0),
(13, '岗位管理', 2, '/org/position', 'org/Position', 'Avatar', 2, 2, NULL, 0),
(14, '员工管理', 3, '/employee', 'employee/index', 'UserFilled', 1, 2, NULL, 0),
(15, '招聘需求', 4, '/recruitment/demand', 'recruitment/Demand', 'DocumentAdd', 1, 2, NULL, 0),
(16, '面试记录', 4, '/recruitment/interview', 'recruitment/Interview', 'ChatDotSquare', 2, 2, NULL, 0),
(17, 'Offer管理', 4, '/recruitment/offer', 'recruitment/Offer', 'Tickets', 3, 2, NULL, 0),
(18, '考勤明细', 5, '/attendance', 'attendance/index', 'DataBoard', 1, 2, NULL, 0),
(19, '请假管理', 5, '/attendance/leave', 'attendance/Leave', 'EditPen', 2, 2, NULL, 0),
(20, '薪酬管理', 6, '/salary', 'salary/index', 'Coin', 1, 2, NULL, 0),
(21, '薪资配置', 6, '/salary/config', 'salary/Config', 'Setting', 2, 2, NULL, 0),
(22, '绩效考核', 7, '/performance', 'performance/index', 'DataBoard', 1, 2, NULL, 0),
(23, '绩效模板', 7, '/performance/template', 'performance/Template', 'CopyDocument', 2, 2, NULL, 0),
(24, '培训管理', 8, '/training', 'training/index', 'Reading', 1, 2, NULL, 0),
(25, '合同管理', 9, '/contract', 'contract/index', 'Document', 1, 2, NULL, 0),
(26, '菜单管理', 10, '/system/menu', 'system/Menu', 'Menu', 1, 2, NULL, 0),
(27, '字典管理', 10, '/system/dict', 'system/Dict', 'Collection', 2, 2, NULL, 0),
(28, '操作日志', 10, '/system/log', 'system/Log', 'DocumentCopy', 3, 2, NULL, 0)
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`);

-- ============================================================
-- 3. 字典数据
-- ============================================================

-- 员工状态
INSERT INTO `sys_dict_type` (`id`, `name`, `code`, `status`, `remark`) VALUES
(1, '员工状态', 'emp_status', 0, '员工在职状态'),
(2, '性别', 'gender', 0, '性别'),
(3, '请假类型', 'leave_type', 0, '请假类型'),
(4, '聘用类型', 'employment_type', 0, '员工聘用类型'),
(5, '学历', 'degree', 0, '学历等级'),
(6, '合同类型', 'contract_type', 0, '合同类型')
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`);

INSERT INTO `sys_dict_data` (`dict_type_id`, `label`, `value`, `sort`, `status`) VALUES
-- 员工状态
(1, '在职', 'active', 1, 0),
(1, '离职', 'resigned', 2, 0),
(1, '试用期', 'probation', 3, 0),
-- 性别
(2, '男', '0', 1, 0),
(2, '女', '1', 2, 0),
(2, '未知', '2', 3, 0),
-- 请假类型
(3, '年假', '1', 1, 0),
(3, '事假', '2', 2, 0),
(3, '病假', '3', 3, 0),
(3, '婚假', '4', 4, 0),
(3, '产假', '5', 5, 0),
(3, '丧假', '6', 6, 0),
(3, '其他', '7', 7, 0),
-- 聘用类型
(4, '正式', '1', 1, 0),
(4, '实习', '2', 2, 0),
(4, '劳务', '3', 3, 0),
(4, '兼职', '4', 4, 0),
-- 学历
(5, '高中', '1', 1, 0),
(5, '大专', '2', 2, 0),
(5, '本科', '3', 3, 0),
(5, '硕士', '4', 4, 0),
(5, '博士', '5', 5, 0),
-- 合同类型
(6, '正式', '1', 1, 0),
(6, '试用', '2', 2, 0),
(6, '实习', '3', 3, 0),
(6, '劳务', '4', 4, 0),
(6, '兼职', '5', 5, 0);

-- ============================================================
-- 4. 默认部门数据
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
