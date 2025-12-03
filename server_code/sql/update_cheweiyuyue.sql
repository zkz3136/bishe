-- ===================================================
-- 车位预约模块数据库升级脚本
-- 功能说明：添加定金、支付状态、核销状态字段
-- 执行时间：在重启后端服务前执行
-- ===================================================

-- 1. 添加定金字段
ALTER TABLE `cheweiyuyue` ADD COLUMN `dingjin` double NULL DEFAULT 50.00 COMMENT '定金金额' AFTER `ispay`;

-- 2. 添加支付状态字段
ALTER TABLE `cheweiyuyue` ADD COLUMN `zhifuzhuangtai` varchar(200) NULL DEFAULT '未支付' COMMENT '支付状态' AFTER `dingjin`;

-- 3. 添加核销状态字段
ALTER TABLE `cheweiyuyue` ADD COLUMN `hexiaozhuangtai` varchar(200) NULL DEFAULT '未核销' COMMENT '核销状态' AFTER `zhifuzhuangtai`;

-- 4. 更新现有数据：将已支付的旧数据同步到新字段
UPDATE `cheweiyuyue` SET `zhifuzhuangtai` = '已支付', `dingjin` = 50.0, `hexiaozhuangtai` = '已核销' WHERE `ispay` = '已支付';
UPDATE `cheweiyuyue` SET `zhifuzhuangtai` = '未支付', `dingjin` = 50.0, `hexiaozhuangtai` = '未核销' WHERE `ispay` = '未支付' OR `ispay` IS NULL;

-- 执行完成！
-- 提示：
-- 1. 备份数据库后再执行此脚本
-- 2. 执行完毕后重启后端服务（server_code）
-- 3. 清除浏览器缓存或强制刷新前端页面（Ctrl+F5）

