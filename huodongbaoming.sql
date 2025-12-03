/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80043
 Source Host           : localhost:3306
 Source Schema         : cl200442145

 Target Server Type    : MySQL
 Target Server Version : 80043
 File Encoding         : 65001

 Date: 09/11/2025 14:40:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for huodongbaoming
-- ----------------------------
DROP TABLE IF EXISTS `huodongbaoming`;
CREATE TABLE `huodongbaoming`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `huodongmingcheng` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '活动名称',
  `huodongtupian` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '活动图片',
  `huodongdidian` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '活动地点',
  `baomingshijian` datetime NULL DEFAULT NULL COMMENT '报名时间',
  `zhanghao` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '账号',
  `mingcheng` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `sfsh` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '待审核' COMMENT '是否审核',
  `shhf` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '回复内容',
  `crossuserid` bigint NULL DEFAULT NULL COMMENT '跨表用户id',
  `crossrefid` bigint NULL DEFAULT NULL COMMENT '跨表主键id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '活动报名' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of huodongbaoming
-- ----------------------------
INSERT INTO `huodongbaoming` VALUES (1, '2025-03-25 17:01:11', '活动名称1', 'file/huodongbaomingHuodongtupian1.jpg,file/huodongbaomingHuodongtupian2.jpg,file/huodongbaomingHuodongtupian3.jpg', '活动地点1', '2025-03-25 17:01:11', '账号1', '名称1', '是', '', 1, 1);
INSERT INTO `huodongbaoming` VALUES (2, '2025-03-25 17:01:11', '活动名称2', 'file/huodongbaomingHuodongtupian2.jpg,file/huodongbaomingHuodongtupian3.jpg,file/huodongbaomingHuodongtupian4.jpg', '活动地点2', '2025-03-25 17:01:11', '账号2', '名称2', '是', '', 2, 2);
INSERT INTO `huodongbaoming` VALUES (3, '2025-03-25 17:01:11', '活动名称3', 'file/huodongbaomingHuodongtupian3.jpg,file/huodongbaomingHuodongtupian4.jpg,file/huodongbaomingHuodongtupian5.jpg', '活动地点3', '2025-03-25 17:01:11', '账号3', '名称3', '是', '', 3, 3);
INSERT INTO `huodongbaoming` VALUES (4, '2025-03-25 17:01:11', '活动名称4', 'file/huodongbaomingHuodongtupian4.jpg,file/huodongbaomingHuodongtupian5.jpg,file/huodongbaomingHuodongtupian6.jpg', '活动地点4', '2025-03-25 17:01:11', '账号4', '名称4', '是', '', 4, 4);
INSERT INTO `huodongbaoming` VALUES (5, '2025-03-25 17:01:11', '活动名称5', 'file/huodongbaomingHuodongtupian5.jpg,file/huodongbaomingHuodongtupian6.jpg,file/huodongbaomingHuodongtupian7.jpg', '活动地点5', '2025-03-25 17:01:11', '账号5', '名称5', '是', '', 5, 5);
INSERT INTO `huodongbaoming` VALUES (6, '2025-03-25 17:01:11', '活动名称6', 'file/huodongbaomingHuodongtupian6.jpg,file/huodongbaomingHuodongtupian7.jpg,file/huodongbaomingHuodongtupian8.jpg', '活动地点6', '2025-03-25 17:01:11', '账号6', '名称6', '是', '', 6, 6);
INSERT INTO `huodongbaoming` VALUES (7, '2025-03-25 17:01:11', '活动名称7', 'file/huodongbaomingHuodongtupian7.jpg,file/huodongbaomingHuodongtupian8.jpg,file/huodongbaomingHuodongtupian9.jpg', '活动地点7', '2025-03-25 17:01:11', '账号7', '名称7', '是', '', 7, 7);
INSERT INTO `huodongbaoming` VALUES (8, '2025-03-25 17:01:11', '活动名称8', 'file/huodongbaomingHuodongtupian8.jpg,file/huodongbaomingHuodongtupian9.jpg,file/huodongbaomingHuodongtupian10.jpg', '活动地点8', '2025-03-25 17:01:11', '账号8', '名称8', '是', '', 8, 8);
INSERT INTO `huodongbaoming` VALUES (9, '2025-03-25 17:01:11', '活动名称9', 'file/huodongbaomingHuodongtupian9.jpg,file/huodongbaomingHuodongtupian10.jpg,file/huodongbaomingHuodongtupian11.jpg', '活动地点9', '2025-03-25 17:01:11', '账号9', '名称9', '是', '', 9, 9);
INSERT INTO `huodongbaoming` VALUES (10, '2025-03-25 17:01:11', '活动名称10', 'file/huodongbaomingHuodongtupian10.jpg,file/huodongbaomingHuodongtupian11.jpg,file/huodongbaomingHuodongtupian12.jpg', '活动地点10', '2025-03-25 17:01:11', '账号10', '名称10', '是', '', 10, 10);
INSERT INTO `huodongbaoming` VALUES (12, '2025-11-07 22:39:32', '活动名称1', 'file/dianpuhuodongHuodongtupian1.jpg,file/dianpuhuodongHuodongtupian2.jpg,file/dianpuhuodongHuodongtupian3.jpg', '活动地点1', '2025-11-07 22:39:30', '账号1', '名称1', '待审核', '', 11, 1);

SET FOREIGN_KEY_CHECKS = 1;
