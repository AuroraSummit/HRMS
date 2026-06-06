-- ========== 角色体系表 ==========

-- 角色表
CREATE TABLE IF NOT EXISTS sys_role (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(64) NOT NULL COMMENT '角色名称',
  code VARCHAR(64) NOT NULL UNIQUE COMMENT '角色编码',
  description VARCHAR(255) DEFAULT '' COMMENT '角色描述',
  status TINYINT DEFAULT 0 COMMENT '状态 0正常 1停用',
  sort INT DEFAULT 0 COMMENT '排序',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  is_deleted TINYINT DEFAULT 0
) COMMENT '角色表';

-- 用户-角色关联表
CREATE TABLE IF NOT EXISTS sys_user_role (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL COMMENT '用户ID',
  role_id BIGINT NOT NULL COMMENT '角色ID',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uk_user_role (user_id, role_id)
) COMMENT '用户角色关联表';

-- 角色-菜单关联表
CREATE TABLE IF NOT EXISTS sys_role_menu (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  role_id BIGINT NOT NULL COMMENT '角色ID',
  menu_id BIGINT NOT NULL COMMENT '菜单ID',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uk_role_menu (role_id, menu_id)
) COMMENT '角色菜单关联表';

-- ========== 默认数据 ==========

-- 插入默认角色
INSERT INTO sys_role (name, code, description, sort) VALUES
('超级管理员', 'admin', '系统管理员，拥有全部权限', 1),
('普通员工', 'employee', '普通员工，可查看个人信息、考勤、工资条等', 2);

-- 给 admin 用户分配 admin 角色
INSERT INTO sys_user_role (user_id, role_id) VALUES (1, 1);

-- 给 admin 角色分配所有菜单权限
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, id FROM sys_menu WHERE is_deleted = 0;

-- 如果 sys_menu 已有用户端菜单（id >= 29），也加入
INSERT IGNORE INTO sys_role_menu (role_id, menu_id)
SELECT 1, id FROM sys_menu WHERE is_deleted = 0 AND id >= 29;

-- 给 employee 角色分配用户端菜单
-- 先插入员工门户父菜单（如果存在）
INSERT IGNORE INTO sys_role_menu (role_id, menu_id)
SELECT 2, id FROM sys_menu WHERE name = '员工门户' AND parent_id = 0;
-- 再插入用户端子菜单
INSERT IGNORE INTO sys_role_menu (role_id, menu_id)
SELECT 2, id FROM sys_menu WHERE parent_id = (SELECT id FROM sys_menu WHERE name = '员工门户' AND parent_id = 0);

-- 注意：如果用户端菜单还没创建，先执行以下 SQL 创建：
-- INSERT INTO sys_menu (name, parent_id, path, component, icon, sort, type, status) VALUES
-- ('员工门户', 0, '/user', 'Layout', 'UserFilled', 99, 1, 0);
-- SET @portal_id = LAST_INSERT_ID();
-- INSERT INTO sys_menu (name, parent_id, path, component, icon, sort, type, status) VALUES
-- ('我的首页', @portal_id, '/user/dashboard', 'user/Dashboard', 'HomeFilled', 1, 2, 0),
-- ('个人信息', @portal_id, '/user/profile', 'user/Profile', 'User', 2, 2, 0),
-- ('我的考勤', @portal_id, '/user/attendance', 'user/Attendance', 'DataBoard', 3, 2, 0),
-- ('我的请假', @portal_id, '/user/leave', 'user/Leave', 'EditPen', 4, 2, 0),
-- ('我的工资', @portal_id, '/user/salary', 'user/Salary', 'Coin', 5, 2, 0),
-- ('我的绩效', @portal_id, '/user/performance', 'user/Performance', 'DataAnalysis', 6, 2, 0),
-- ('我的培训', @portal_id, '/user/training', 'user/Training', 'Reading', 7, 2, 0),
-- ('我的合同', @portal_id, '/user/contract', 'user/Contract', 'Document', 8, 2, 0);

