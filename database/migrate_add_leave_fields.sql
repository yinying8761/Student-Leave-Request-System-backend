-- 为已有 leave_application 表添加新字段（在 schema.sql 更新前创建的表需要用此脚本）
ALTER TABLE leave_application
    ADD COLUMN is_leave_campus         TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '是否离校: 0-否 1-是' AFTER reason,
    ADD COLUMN destination_province     VARCHAR(50)  DEFAULT NULL COMMENT '目的地-省' AFTER is_leave_campus,
    ADD COLUMN destination_city         VARCHAR(50)  DEFAULT NULL COMMENT '目的地-市' AFTER destination_province,
    ADD COLUMN destination_district     VARCHAR(50)  DEFAULT NULL COMMENT '目的地-区/县' AFTER destination_city,
    ADD COLUMN destination_detail       VARCHAR(200) DEFAULT NULL COMMENT '目的地-详细地址' AFTER destination_district,
    ADD COLUMN contact_phone           VARCHAR(20)  DEFAULT NULL COMMENT '本人联系电话' AFTER destination_detail,
    ADD COLUMN emergency_contact_name  VARCHAR(50)  DEFAULT NULL COMMENT '紧急联系人姓名' AFTER contact_phone,
    ADD COLUMN emergency_contact_phone VARCHAR(20)  DEFAULT NULL COMMENT '紧急联系人电话' AFTER emergency_contact_name;