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

 Date: 09/11/2025 15:31:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for wodeyuyue
-- ----------------------------
DROP TABLE IF EXISTS `wodeyuyue`;
CREATE TABLE `wodeyuyue`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `orderid` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'orderid',
  `goodname` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '菜品名称',
  `buynumber` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '数量',
  `total` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '价格',
  `zhanghao` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '账号',
  `mingcheng` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `dingdanzhuangtai` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '订单状态',
  `shangjiazhanghao` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商家账号',
  `shangjiamingcheng` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商家名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '我的预约' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of wodeyuyue
-- ----------------------------
INSERT INTO `wodeyuyue` VALUES (1, '2025-10-25 17:01:10', 'orderid1', '菜品名称1', '数量1', '价格1', '账号1', '名称1', '备料', '商家账号1', '商家名称1');
INSERT INTO `wodeyuyue` VALUES (2, '2025-10-25 17:01:10', 'orderid2', '菜品名称2', '数量2', '价格2', '账号2', '名称2', '备料', '商家账号2', '商家名称2');
INSERT INTO `wodeyuyue` VALUES (3, '2025-10-25 17:01:10', 'orderid3', '菜品名称3', '数量3', '价格3', '账号3', '名称3', '备料', '商家账号3', '商家名称3');
INSERT INTO `wodeyuyue` VALUES (4, '2025-10-25 17:01:10', 'orderid4', '菜品名称4', '数量4', '价格4', '账号4', '名称4', '备料', '商家账号4', '商家名称4');
INSERT INTO `wodeyuyue` VALUES (5, '2025-10-25 17:01:10', 'orderid5', '菜品名称5', '数量5', '价格5', '账号5', '名称5', '备料', '商家账号5', '商家名称5');
INSERT INTO `wodeyuyue` VALUES (6, '2025-10-25 17:01:10', 'orderid6', '菜品名称6', '数量6', '价格6', '账号6', '名称6', '备料', '商家账号6', '商家名称6');
INSERT INTO `wodeyuyue` VALUES (7, '2025-10-25 17:01:10', 'orderid7', '菜品名称7', '数量7', '价格7', '账号7', '名称7', '备料', '商家账号7', '商家名称7');
INSERT INTO `wodeyuyue` VALUES (8, '2025-10-25 17:01:10', 'orderid8', '菜品名称8', '数量8', '价格8', '账号8', '名称8', '备料', '商家账号8', '商家名称8');
INSERT INTO `wodeyuyue` VALUES (9, '2025-10-25 17:01:10', 'orderid9', '菜品名称9', '数量9', '价格9', '账号9', '名称9', '备料', '商家账号9', '商家名称9');
INSERT INTO `wodeyuyue` VALUES (10, '2025-10-25 17:01:10', 'orderid10', '菜品名称10', '数量10', '价格10', '账号10', '名称10', '备料', '商家账号10', '商家名称10');
INSERT INTO `wodeyuyue` VALUES (11, '2025-10-25 17:01:10', '20250325170612297987', '菜品名称10', '1', '99.9', '账号1', '名称1', '备料', '商家账号10', '商家名称10');

SET FOREIGN_KEY_CHECKS = 1;
