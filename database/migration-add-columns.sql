-- ============================================================
-- HRMS 数据库 Schema 迁移 (Schema Migration)
-- 为已有数据库添加缺失的列，并回填 emp_id
-- ============================================================

USE hrms;

-- 为岗位表添加描述字段
ALTER TABLE `org_position`
    ADD COLUMN IF NOT EXISTS `description` VARCHAR(500) DEFAULT NULL COMMENT '岗位描述'
    AFTER `sort`;

-- 为用户表添加关联员工字段
ALTER TABLE `auth_user`
    ADD COLUMN IF NOT EXISTS `emp_id` BIGINT DEFAULT NULL COMMENT '关联员工ID'
    AFTER `type`;

-- 为请假表添加审批人字段
ALTER TABLE `att_leave`
    ADD COLUMN IF NOT EXISTS `approver_id` BIGINT DEFAULT NULL COMMENT '审批人ID'
    AFTER `status`;

-- 为培训记录添加完成时间字段
ALTER TABLE `train_record`
    ADD COLUMN IF NOT EXISTS `completed_time` DATETIME DEFAULT NULL COMMENT '完成时间'
    AFTER `evaluation`;

-- ============================================================
-- 回填 emp_id：根据 real_name 匹配 emp_employee.name
-- ============================================================
UPDATE `auth_user` u
    INNER JOIN `emp_employee` e ON u.`real_name` = e.`name`
SET u.`emp_id` = e.`id`
WHERE u.`emp_id` IS NULL;
