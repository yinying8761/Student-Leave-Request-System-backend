-- 学生请销假系统 数据库建表脚本
-- 先创建数据库: CREATE DATABASE leave_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE leave_system;

-- 1. 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id`            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username`      VARCHAR(50)  NOT NULL COMMENT '用户名',
    `password`      VARCHAR(255) NOT NULL COMMENT '密码(BCrypt)',
    `real_name`     VARCHAR(50)  NOT NULL COMMENT '真实姓名',
    `role`          VARCHAR(20)  NOT NULL COMMENT '角色: STUDENT/ADVISOR/COUNSELOR/ADMIN',
    `phone`         VARCHAR(20)  DEFAULT NULL COMMENT '手机号',
    `email`         VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `department`    VARCHAR(100) DEFAULT NULL COMMENT '院系',
    `class_name`    VARCHAR(100) DEFAULT NULL COMMENT '班级',
    `advisor_id`    BIGINT       DEFAULT NULL COMMENT '导师ID(学生所属导师)',
    `counselor_id`  BIGINT       DEFAULT NULL COMMENT '辅导员ID(学生所属辅导员)',
    `enabled`       TINYINT      NOT NULL DEFAULT 1 COMMENT '是否启用',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_role` (`role`),
    KEY `idx_advisor_id` (`advisor_id`),
    KEY `idx_counselor_id` (`counselor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 2. 请假申请表
CREATE TABLE IF NOT EXISTS `leave_application` (
    `id`            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '申请ID',
    `student_id`    BIGINT       NOT NULL COMMENT '学生ID',
    `leave_type`    VARCHAR(20)  NOT NULL COMMENT '请假类型: PERSONAL/SICK/OFFICIAL/OTHER',
    `start_time`    DATETIME     NOT NULL COMMENT '请假开始时间',
    `end_time`      DATETIME     NOT NULL COMMENT '请假结束时间',
    `duration_days` DECIMAL(4,1) NOT NULL COMMENT '请假天数',
    `reason`                  TEXT         NOT NULL COMMENT '请假原因',
    `is_leave_campus`         TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '是否离校: 0-否 1-是',
    `destination_province`     VARCHAR(50)  DEFAULT NULL COMMENT '目的地-省',
    `destination_city`         VARCHAR(50)  DEFAULT NULL COMMENT '目的地-市',
    `destination_district`     VARCHAR(50)  DEFAULT NULL COMMENT '目的地-区/县',
    `destination_detail`       VARCHAR(200) DEFAULT NULL COMMENT '目的地-详细地址',
    `contact_phone`           VARCHAR(20)  DEFAULT NULL COMMENT '本人联系电话',
    `emergency_contact_name`  VARCHAR(50)  DEFAULT NULL COMMENT '紧急联系人姓名',
    `emergency_contact_phone` VARCHAR(20)  DEFAULT NULL COMMENT '紧急联系人电话',
    `status`        VARCHAR(20)  NOT NULL DEFAULT 'PENDING' COMMENT '状态: PENDING/APPROVED/REJECTED/CANCELLING/CANCELLED',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_student_id` (`student_id`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='请假申请表';

-- 3. 审批记录表
CREATE TABLE IF NOT EXISTS `approval_record` (
    `id`             BIGINT       NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    `application_id` BIGINT       NOT NULL COMMENT '请假申请ID',
    `approver_id`    BIGINT       NOT NULL COMMENT '审批人ID',
    `step`           TINYINT      NOT NULL COMMENT '审批步骤: 1-辅导员',
    `action`         VARCHAR(10)  NOT NULL COMMENT '审批动作: APPROVE/REJECT',
    `comment`        VARCHAR(500) DEFAULT NULL COMMENT '审批意见',
    `create_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '审批时间',
    PRIMARY KEY (`id`),
    KEY `idx_application_id` (`application_id`),
    KEY `idx_approver_id` (`approver_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审批记录表';

-- 4. 销假表
CREATE TABLE IF NOT EXISTS `leave_cancellation` (
    `id`             BIGINT       NOT NULL AUTO_INCREMENT COMMENT '销假ID',
    `application_id` BIGINT       NOT NULL COMMENT '请假申请ID',
    `student_id`     BIGINT       NOT NULL COMMENT '学生ID',
    `return_time`    DATETIME     NOT NULL COMMENT '实际返校时间',
    `status`         VARCHAR(20)  NOT NULL DEFAULT 'PENDING' COMMENT '状态: PENDING/APPROVED',
    `comment`        VARCHAR(500) DEFAULT NULL COMMENT '销假说明',
    `approver_id`    BIGINT       DEFAULT NULL COMMENT '销假审批人ID',
    `approve_time`   DATETIME     DEFAULT NULL COMMENT '审批时间',
    `create_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_application_id` (`application_id`),
    KEY `idx_student_id` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销假表';

-- 5. 请假额度表
CREATE TABLE IF NOT EXISTS `leave_quota` (
    `id`               BIGINT       NOT NULL AUTO_INCREMENT COMMENT '额度ID',
    `leave_type`       VARCHAR(20)  NOT NULL COMMENT '请假类型',
    `max_days_per_time` DECIMAL(4,1) NOT NULL COMMENT '单次最大天数',
    `max_days_per_term` DECIMAL(4,1) NOT NULL COMMENT '每学期最大天数',
    `create_time`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_leave_type` (`leave_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='请假额度表';

-- 6. 通知表
CREATE TABLE IF NOT EXISTS `notification` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '通知ID',
    `user_id`     BIGINT       NOT NULL COMMENT '接收用户ID',
    `title`       VARCHAR(200) NOT NULL COMMENT '通知标题',
    `content`     TEXT         NOT NULL COMMENT '通知内容',
    `is_read`     TINYINT      NOT NULL DEFAULT 0 COMMENT '是否已读',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_user_read` (`user_id`, `is_read`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知表';

-- 初始数据: 默认管理员
INSERT INTO `user` (`username`, `password`, `real_name`, `role`, `department`)
VALUES ('admin', '$2a$10$ZA7NpQwEnPizavjS1ounI.N5ZDqC6SAMs2QTrECoRzUbpX2CAk4Xq', '系统管理员', 'ADMIN', '信息中心');
-- 默认密码: admin123 (BCrypt 加密)
UPDATE leave_system.`user`
SET password = '$2a$10$ZA7NpQwEnPizavjS1ounI.N5ZDqC6SAMs2QTrECoRzUbpX2CAk4Xq'
WHERE username = 'admin';