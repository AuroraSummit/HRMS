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
-- 5. 默认岗位数据
-- ============================================================-- ... existing code ...
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

