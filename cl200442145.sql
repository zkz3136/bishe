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

 Date: 07/11/2025 23:00:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for caipinleixing
-- ----------------------------
DROP TABLE IF EXISTS `caipinleixing`;
CREATE TABLE `caipinleixing`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `caipinleixing` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '菜品类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '菜品类型' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of caipinleixing
-- ----------------------------
INSERT INTO `caipinleixing` VALUES (1, '2025-11-02 16:19:42', '潮汕特色菜');
INSERT INTO `caipinleixing` VALUES (2, '2025-11-02 16:19:42', '面食');
INSERT INTO `caipinleixing` VALUES (3, '2025-11-02 16:19:42', '粤菜');
INSERT INTO `caipinleixing` VALUES (4, '2025-11-02 16:19:42', '小吃');
INSERT INTO `caipinleixing` VALUES (5, '2025-11-02 16:19:42', '卤味');
INSERT INTO `caipinleixing` VALUES (6, '2025-11-02 16:19:42', '日式料理');
INSERT INTO `caipinleixing` VALUES (7, '2025-11-02 16:19:42', '西餐 ');
INSERT INTO `caipinleixing` VALUES (8, '2025-11-02 16:19:42', '丸子类');
INSERT INTO `caipinleixing` VALUES (9, '2025-11-02 16:19:42', '闽菜');
INSERT INTO `caipinleixing` VALUES (10, '2025-11-02 16:19:42', '小炒类');
INSERT INTO `caipinleixing` VALUES (11, '2025-11-02 16:19:42', '川菜');
INSERT INTO `caipinleixing` VALUES (12, '2025-11-02 17:30:20', '汤类');

-- ----------------------------
-- Table structure for cantingxinxi
-- ----------------------------
DROP TABLE IF EXISTS `cantingxinxi`;
CREATE TABLE `cantingxinxi`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `cantingmingcheng` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '餐厅名称',
  `canzhuomingcheng` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '餐桌名称',
  `fengmian` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '封面',
  `canzhuoweizhi` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '餐桌位置',
  `kezuorenshu` int NULL DEFAULT NULL COMMENT '可坐人数',
  `canzhuozhuangtai` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '餐桌状态',
  `shangjiazhanghao` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商家账号',
  `shangjiamingcheng` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商家名称',
  `storeup_number` int NULL DEFAULT 0 COMMENT '收藏数',
  `discuss_number` int NULL DEFAULT 0 COMMENT '评论数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '餐厅信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cantingxinxi
-- ----------------------------
INSERT INTO `cantingxinxi` VALUES (1, '2025-10-25 17:01:10', '餐厅名称1', '餐桌名称1', 'file/cantingxinxiFengmian1.jpg,file/cantingxinxiFengmian2.jpg,file/cantingxinxiFengmian3.jpg', '餐桌位置1', 10, '已预约', '商家账号1', '商家名称1', 1, 1);
INSERT INTO `cantingxinxi` VALUES (2, '2025-10-25 17:01:10', '餐厅名称2', '餐桌名称2', 'file/cantingxinxiFengmian2.jpg,file/cantingxinxiFengmian3.jpg,file/cantingxinxiFengmian4.jpg', '餐桌位置2', 2, '已预约', '商家账号2', '商家名称2', 3, 3);
INSERT INTO `cantingxinxi` VALUES (3, '2025-10-25 17:01:10', '餐厅名称3', '餐桌名称3', 'file/cantingxinxiFengmian3.jpg,file/cantingxinxiFengmian4.jpg,file/cantingxinxiFengmian5.jpg', '餐桌位置3', 3, '已预约', '商家账号3', '商家名称3', 3, 3);
INSERT INTO `cantingxinxi` VALUES (4, '2025-10-25 17:01:10', '餐厅名称4', '餐桌名称4', 'file/cantingxinxiFengmian4.jpg,file/cantingxinxiFengmian5.jpg,file/cantingxinxiFengmian6.jpg', '餐桌位置4', 4, '已预约', '商家账号4', '商家名称4', 4, 4);
INSERT INTO `cantingxinxi` VALUES (5, '2025-10-25 17:01:10', '餐厅名称5', '餐桌名称5', 'file/cantingxinxiFengmian5.jpg,file/cantingxinxiFengmian6.jpg,file/cantingxinxiFengmian7.jpg', '餐桌位置5', 5, '已预约', '商家账号5', '商家名称5', 5, 5);
INSERT INTO `cantingxinxi` VALUES (6, '2025-10-25 17:01:10', '餐厅名称6', '餐桌名称6', 'file/cantingxinxiFengmian6.jpg,file/cantingxinxiFengmian7.jpg,file/cantingxinxiFengmian8.jpg', '餐桌位置6', 6, '已预约', '商家账号6', '商家名称6', 6, 6);
INSERT INTO `cantingxinxi` VALUES (7, '2025-10-25 17:01:10', '餐厅名称7', '餐桌名称7', 'file/cantingxinxiFengmian7.jpg,file/cantingxinxiFengmian8.jpg,file/cantingxinxiFengmian9.jpg', '餐桌位置7', 7, '已预约', '商家账号7', '商家名称7', 7, 7);
INSERT INTO `cantingxinxi` VALUES (8, '2025-10-25 17:01:10', '餐厅名称8', '餐桌名称8', 'file/cantingxinxiFengmian8.jpg,file/cantingxinxiFengmian9.jpg,file/cantingxinxiFengmian10.jpg', '餐桌位置8', 8, '已预约', '商家账号8', '商家名称8', 8, 8);
INSERT INTO `cantingxinxi` VALUES (9, '2025-10-25 17:01:10', '餐厅名称9', '餐桌名称9', 'file/cantingxinxiFengmian9.jpg,file/cantingxinxiFengmian10.jpg,file/cantingxinxiFengmian11.jpg', '餐桌位置9', 9, '已预约', '商家账号9', '商家名称9', 9, 9);
INSERT INTO `cantingxinxi` VALUES (10, '2025-10-25 17:01:10', '餐厅名称10', '餐桌名称10', 'file/cantingxinxiFengmian10.jpg,file/cantingxinxiFengmian11.jpg,file/cantingxinxiFengmian12.jpg', '餐桌位置10', 10, '已预约', '商家账号10', '商家名称10', 10, 11);

-- ----------------------------
-- Table structure for cantingyuyue
-- ----------------------------
DROP TABLE IF EXISTS `cantingyuyue`;
CREATE TABLE `cantingyuyue`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `cantingmingcheng` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '餐厅名称',
  `canzhuomingcheng` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '餐桌名称',
  `fengmian` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '封面',
  `canzhuoweizhi` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '餐桌位置',
  `kezuorenshu` int NULL DEFAULT NULL COMMENT '可坐人数',
  `shangjiazhanghao` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商家账号',
  `shangjiamingcheng` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商家名称',
  `storeup_number` int NULL DEFAULT NULL COMMENT '收藏数',
  `zhanghao` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '账号',
  `mingcheng` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `yuyueshijian` datetime NULL DEFAULT NULL COMMENT '预约时间',
  `sfsh` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '待审核' COMMENT '是否审核',
  `shhf` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '回复内容',
  `dingjin` double NULL DEFAULT 50 COMMENT '定金金额',
  `zhifuzhuangtai` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '未支付' COMMENT '支付状态',
  `hexiaozhuangtai` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '未核销' COMMENT '核销状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '餐厅预约' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cantingyuyue
-- ----------------------------
INSERT INTO `cantingyuyue` VALUES (1, '2025-03-25 17:01:11', '餐厅名称1', '餐桌名称1', 'file/cantingyuyueFengmian1.jpg,file/cantingyuyueFengmian2.jpg,file/cantingyuyueFengmian3.jpg', '餐桌位置1', 1, '商家账号1', '商家名称1', 1, '账号1', '名称1', '2025-10-25 17:01:11', '是', '', 50, '未支付', '未核销');
INSERT INTO `cantingyuyue` VALUES (2, '2025-03-25 17:01:11', '餐厅名称2', '餐桌名称2', 'file/cantingyuyueFengmian2.jpg,file/cantingyuyueFengmian3.jpg,file/cantingyuyueFengmian4.jpg', '餐桌位置2', 2, '商家账号2', '商家名称2', 2, '账号2', '名称2', '2025-10-25 17:01:11', '是', '', 50, '未支付', '未核销');
INSERT INTO `cantingyuyue` VALUES (3, '2025-03-25 17:01:11', '餐厅名称3', '餐桌名称3', 'file/cantingyuyueFengmian3.jpg,file/cantingyuyueFengmian4.jpg,file/cantingyuyueFengmian5.jpg', '餐桌位置3', 3, '商家账号3', '商家名称3', 3, '账号3', '名称3', '2025-10-25 17:01:11', '是', '', 50, '未支付', '未核销');
INSERT INTO `cantingyuyue` VALUES (4, '2025-03-25 17:01:11', '餐厅名称4', '餐桌名称4', 'file/cantingyuyueFengmian4.jpg,file/cantingyuyueFengmian5.jpg,file/cantingyuyueFengmian6.jpg', '餐桌位置4', 4, '商家账号4', '商家名称4', 4, '账号4', '名称4', '2025-10-25 17:01:11', '是', '', 50, '未支付', '未核销');
INSERT INTO `cantingyuyue` VALUES (5, '2025-03-25 17:01:11', '餐厅名称5', '餐桌名称5', 'file/cantingyuyueFengmian5.jpg,file/cantingyuyueFengmian6.jpg,file/cantingyuyueFengmian7.jpg', '餐桌位置5', 5, '商家账号5', '商家名称5', 5, '账号5', '名称5', '2025-10-25 17:01:11', '是', '', 50, '未支付', '未核销');
INSERT INTO `cantingyuyue` VALUES (6, '2025-03-25 17:01:11', '餐厅名称6', '餐桌名称6', 'file/cantingyuyueFengmian6.jpg,file/cantingyuyueFengmian7.jpg,file/cantingyuyueFengmian8.jpg', '餐桌位置6', 6, '商家账号6', '商家名称6', 6, '账号6', '名称6', '2025-10-25 17:01:11', '是', '', 50, '未支付', '未核销');
INSERT INTO `cantingyuyue` VALUES (7, '2025-03-25 17:01:11', '餐厅名称7', '餐桌名称7', 'file/cantingyuyueFengmian7.jpg,file/cantingyuyueFengmian8.jpg,file/cantingyuyueFengmian9.jpg', '餐桌位置7', 7, '商家账号7', '商家名称7', 7, '账号7', '名称7', '2025-10-25 17:01:11', '是', '', 50, '未支付', '未核销');
INSERT INTO `cantingyuyue` VALUES (8, '2025-03-25 17:01:11', '餐厅名称8', '餐桌名称8', 'file/cantingyuyueFengmian8.jpg,file/cantingyuyueFengmian9.jpg,file/cantingyuyueFengmian10.jpg', '餐桌位置8', 8, '商家账号8', '商家名称8', 8, '账号8', '名称8', '2025-10-25 17:01:11', '是', '', 50, '未支付', '未核销');
INSERT INTO `cantingyuyue` VALUES (9, '2025-03-25 17:01:11', '餐厅名称9', '餐桌名称9', 'file/cantingyuyueFengmian9.jpg,file/cantingyuyueFengmian10.jpg,file/cantingyuyueFengmian11.jpg', '餐桌位置9', 9, '商家账号9', '商家名称9', 9, '账号9', '名称9', '2025-10-25 17:01:11', '是', '', 50, '未支付', '未核销');
INSERT INTO `cantingyuyue` VALUES (10, '2025-03-25 17:01:11', '餐厅名称10', '餐桌名称10', 'file/cantingyuyueFengmian10.jpg,file/cantingyuyueFengmian11.jpg,file/cantingyuyueFengmian12.jpg', '餐桌位置10', 10, '商家账号10', '商家名称10', 10, '账号10', '名称10', '2025-10-25 17:01:11', '是', '', 50, '未支付', '未核销');
INSERT INTO `cantingyuyue` VALUES (11, '2025-03-25 17:08:18', '餐厅名称10', '餐桌名称10', 'file/cantingxinxiFengmian10.jpg,file/cantingxinxiFengmian11.jpg,file/cantingxinxiFengmian12.jpg', '餐桌位置10', 10, '商家账号10', '商家名称10', 10, '账号1', '名称1', '2025-10-26 00:00:00', '是', '好的', 50, '未支付', '未核销');
INSERT INTO `cantingyuyue` VALUES (12, '2025-03-25 17:14:10', '餐厅名称1', '餐桌名称1', 'file/cantingxinxiFengmian1.jpg,file/cantingxinxiFengmian2.jpg,file/cantingxinxiFengmian3.jpg', '餐桌位置1', 1, '商家账号1', '商家名称1', 1, '账号1', '名称1', '2025-10-26 00:00:00', '待审核', '', 50, '未支付', '未核销');
INSERT INTO `cantingyuyue` VALUES (13, '2025-11-02 18:16:30', '餐厅名称10', '餐桌名称10', 'file/cantingxinxiFengmian10.jpg,file/cantingxinxiFengmian11.jpg,file/cantingxinxiFengmian12.jpg', '餐桌位置10', 10, '商家账号10', '商家名称10', 10, '账号1', '名称1', '2025-11-19 00:00:00', '待审核', '', 50, '未支付', '未核销');
INSERT INTO `cantingyuyue` VALUES (14, '2025-11-03 15:49:47', '餐厅名称1', '餐桌名称1', 'file/cantingxinxiFengmian1.jpg,file/cantingxinxiFengmian2.jpg,file/cantingxinxiFengmian3.jpg', '餐桌位置1', 1, '商家账号1', '商家名称1', 1, '账号1', '名称1', '2025-11-03 10:00:00', '是', '预约成功，已扣除定金50元', 50, '已支付', '未核销');
INSERT INTO `cantingyuyue` VALUES (15, '2025-11-03 15:50:11', '', '', '', '', 0, '', '', 0, '账号1', '名称1', '2025-11-03 10:00:00', '是', '预约成功，已扣除定金50元', 50, '已支付', '未核销');
INSERT INTO `cantingyuyue` VALUES (16, '2025-11-03 15:50:27', '餐厅名称1', '餐桌名称1', 'file/cantingxinxiFengmian1.jpg,file/cantingxinxiFengmian2.jpg,file/cantingxinxiFengmian3.jpg', '餐桌位置1', 1, '商家账号1', '商家名称1', 1, '账号1', '名称1', '2025-11-03 11:00:00', '是', '预约成功，已扣除定金50元', 50, '已支付', '未核销');
INSERT INTO `cantingyuyue` VALUES (17, '2025-11-07 22:38:05', '餐厅名称1', '餐桌名称1', 'file/cantingxinxiFengmian1.jpg,file/cantingxinxiFengmian2.jpg,file/cantingxinxiFengmian3.jpg', '餐桌位置1', 10, '商家账号1', '商家名称1', 1, '账号1', '名称1', '2025-11-07 11:00:00', '是', '预约成功，已扣除定金50元', 50, '已支付', '未核销');

-- ----------------------------
-- Table structure for cart
-- ----------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `tablename` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'caipinxinxi' COMMENT '商品表名',
  `goodid` bigint NOT NULL COMMENT '商品id',
  `goodname` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商品名称',
  `picture` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图片',
  `buynumber` int NULL DEFAULT NULL COMMENT '购买数量',
  `price` double NULL DEFAULT NULL COMMENT '单价',
  `discountprice` double NULL DEFAULT NULL COMMENT '折扣价',
  `userid` bigint NOT NULL COMMENT '用户id',
  `shangjiazhanghao` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商户名称',
  `goodtype` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商品类型',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `goodid`(`goodid` ASC, `price` ASC) USING BTREE,
  CONSTRAINT `cart_ibfk_1` FOREIGN KEY (`goodid`, `price`) REFERENCES `meishixinxi` (`id`, `price`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '购物车' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cart
-- ----------------------------

-- ----------------------------
-- Table structure for cheweixinxi
-- ----------------------------
DROP TABLE IF EXISTS `cheweixinxi`;
CREATE TABLE `cheweixinxi`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `cheweibianhao` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '车位编号',
  `cheweitupian` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '车位图片',
  `quyu` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '区域',
  `cheweiweizhi` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '车位位置',
  `xiaoshijiage` double NULL DEFAULT NULL COMMENT '小时价格',
  `cheweizhuangtai` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '车位状态',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `cheweibianhao`(`cheweibianhao` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '车位信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cheweixinxi
-- ----------------------------
INSERT INTO `cheweixinxi` VALUES (1, '2025-10-25 17:01:11', '车位编号1', 'file/cheweixinxiCheweitupian1.jpg,file/cheweixinxiCheweitupian2.jpg,file/cheweixinxiCheweitupian3.jpg', 'A', '车位位置1', 10, '已使用');
INSERT INTO `cheweixinxi` VALUES (2, '2025-10-25 17:01:11', '车位编号2', 'file/cheweixinxiCheweitupian2.jpg,file/cheweixinxiCheweitupian3.jpg,file/cheweixinxiCheweitupian4.jpg', 'A', '车位位置2', 10, '已使用');
INSERT INTO `cheweixinxi` VALUES (3, '2025-10-25 17:01:11', '车位编号3', 'file/cheweixinxiCheweitupian3.jpg,file/cheweixinxiCheweitupian4.jpg,file/cheweixinxiCheweitupian5.jpg', 'A', '车位位置3', 10, '已使用');
INSERT INTO `cheweixinxi` VALUES (4, '2025-10-25 17:01:11', '车位编号4', 'file/cheweixinxiCheweitupian4.jpg,file/cheweixinxiCheweitupian5.jpg,file/cheweixinxiCheweitupian6.jpg', 'A', '车位位置4', 10, '已使用');
INSERT INTO `cheweixinxi` VALUES (5, '2025-10-25 17:01:11', '车位编号5', 'file/cheweixinxiCheweitupian5.jpg,file/cheweixinxiCheweitupian6.jpg,file/cheweixinxiCheweitupian7.jpg', 'A', '车位位置5', 10, '已使用');
INSERT INTO `cheweixinxi` VALUES (6, '2025-10-25 17:01:11', '车位编号6', 'file/cheweixinxiCheweitupian6.jpg,file/cheweixinxiCheweitupian7.jpg,file/cheweixinxiCheweitupian8.jpg', 'A', '车位位置6', 10, '已使用');
INSERT INTO `cheweixinxi` VALUES (7, '2025-10-25 17:01:11', '车位编号7', 'file/cheweixinxiCheweitupian7.jpg,file/cheweixinxiCheweitupian8.jpg,file/cheweixinxiCheweitupian9.jpg', 'A', '车位位置7', 10, '已使用');
INSERT INTO `cheweixinxi` VALUES (8, '2025-10-25 17:01:11', '车位编号8', 'file/cheweixinxiCheweitupian8.jpg,file/cheweixinxiCheweitupian9.jpg,file/cheweixinxiCheweitupian10.jpg', 'A', '车位位置8', 10, '已使用');
INSERT INTO `cheweixinxi` VALUES (9, '2025-10-25 17:01:11', '车位编号9', 'file/cheweixinxiCheweitupian9.jpg,file/cheweixinxiCheweitupian10.jpg,file/cheweixinxiCheweitupian11.jpg', 'A', '车位位置9', 10, '已使用');
INSERT INTO `cheweixinxi` VALUES (10, '2025-10-25 17:01:11', '车位编号10', 'file/cheweixinxiCheweitupian10.jpg,file/cheweixinxiCheweitupian11.jpg,file/cheweixinxiCheweitupian12.jpg', 'A', '车位位置10', 10, '已使用');
INSERT INTO `cheweixinxi` VALUES (11, '2025-10-25 17:09:17', '车位编号11', 'file/1742893746842.jpg', 'B', '车位位置11', 10, '空闲');

-- ----------------------------
-- Table structure for cheweiyuyue
-- ----------------------------
DROP TABLE IF EXISTS `cheweiyuyue`;
CREATE TABLE `cheweiyuyue`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `cheweibianhao` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '车位编号',
  `cheweiweizhi` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '车位位置',
  `yuyueshijian` datetime NULL DEFAULT NULL COMMENT '预约时间',
  `xiaoshijiage` double NULL DEFAULT NULL COMMENT '小时价格',
  `ruchangzhuangtai` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '入场状态',
  `zhanghao` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '账号',
  `mingcheng` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `chepaihao` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '车牌号',
  `ispay` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '未支付' COMMENT '是否支付',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '车位预约' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cheweiyuyue
-- ----------------------------
INSERT INTO `cheweiyuyue` VALUES (1, '2025-10-25 17:01:11', '车位编号1', '车位位置1', '2025-10-25 17:01:11', 1, '已入场', '账号1', '名称1', '车牌号1', '未支付');
INSERT INTO `cheweiyuyue` VALUES (2, '2025-10-25 17:01:11', '车位编号2', '车位位置2', '2025-10-25 17:01:11', 2, '已入场', '账号2', '名称2', '车牌号2', '未支付');
INSERT INTO `cheweiyuyue` VALUES (3, '2025-10-25 17:01:11', '车位编号3', '车位位置3', '2025-10-25 17:01:11', 3, '已入场', '账号3', '名称3', '车牌号3', '未支付');
INSERT INTO `cheweiyuyue` VALUES (4, '2025-10-25 17:01:11', '车位编号4', '车位位置4', '2025-10-25 17:01:11', 4, '已入场', '账号4', '名称4', '车牌号4', '未支付');
INSERT INTO `cheweiyuyue` VALUES (5, '2025-10-25 17:01:11', '车位编号5', '车位位置5', '2025-10-25 17:01:11', 5, '已入场', '账号5', '名称5', '车牌号5', '未支付');
INSERT INTO `cheweiyuyue` VALUES (6, '2025-10-25 17:01:11', '车位编号6', '车位位置6', '2025-10-25 17:01:11', 6, '已入场', '账号6', '名称6', '车牌号6', '未支付');
INSERT INTO `cheweiyuyue` VALUES (7, '2025-10-25 17:01:11', '车位编号7', '车位位置7', '2025-10-25 17:01:11', 7, '已入场', '账号7', '名称7', '车牌号7', '未支付');
INSERT INTO `cheweiyuyue` VALUES (8, '2025-10-25 17:01:11', '车位编号8', '车位位置8', '2025-10-25 17:01:11', 8, '已入场', '账号8', '名称8', '车牌号8', '未支付');
INSERT INTO `cheweiyuyue` VALUES (9, '2025-10-25 17:01:11', '车位编号9', '车位位置9', '2025-10-25 17:01:11', 9, '已入场', '账号9', '名称9', '车牌号9', '未支付');
INSERT INTO `cheweiyuyue` VALUES (10, '2025-10-25 17:01:11', '车位编号10', '车位位置10', '2025-10-25 17:01:11', 10, '已入场', '账号10', '名称10', '车牌号10', '已支付');
INSERT INTO `cheweiyuyue` VALUES (11, '2025-10-25 17:07:17', '车位编号10', '车位位置10', '2025-10-26 00:00:00', 10, '未入场', '账号1', '名称1', '车牌号1', '已支付');
INSERT INTO `cheweiyuyue` VALUES (12, '2025-11-03 15:53:21', '车位编号10', '车位位置10', '2025-11-04 00:00:00', 10, '未入场', '账号1', '名称1', '车牌号1', '未支付');

-- ----------------------------
-- Table structure for config
-- ----------------------------
DROP TABLE IF EXISTS `config`;
CREATE TABLE `config`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '名称',
  `value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '值',
  `url` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '链接',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '轮播图' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config
-- ----------------------------
INSERT INTO `config` VALUES (1, '2025-10-25 17:01:11', 'swiper1', 'file/swiperPicture1.jpg', NULL);
INSERT INTO `config` VALUES (2, '2025-10-25 17:01:11', 'swiper2', 'file/swiperPicture2.jpg', NULL);
INSERT INTO `config` VALUES (3, '2025-10-25 17:01:11', 'swiper3', 'file/1742893928445.jpg', NULL);

-- ----------------------------
-- Table structure for dianpuhuodong
-- ----------------------------
DROP TABLE IF EXISTS `dianpuhuodong`;
CREATE TABLE `dianpuhuodong`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `huodongmingcheng` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '活动名称',
  `huodongtupian` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '活动图片',
  `kaishishijian` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `huodongdidian` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '活动地点',
  `huodongneirong` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '活动内容',
  `fabushijian` datetime NULL DEFAULT NULL COMMENT '发布时间',
  `reversetime` datetime NULL DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '店铺活动' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dianpuhuodong
-- ----------------------------
INSERT INTO `dianpuhuodong` VALUES (1, '2025-11-06 21:00:00', '活动名称1', 'file/dianpuhuodongHuodongtupian1.jpg,file/dianpuhuodongHuodongtupian2.jpg,file/dianpuhuodongHuodongtupian3.jpg', '2025-11-07 21:00:00', '活动地点1', '活动内容1', '2025-11-06 21:00:00', '2025-11-08 21:00:00');
INSERT INTO `dianpuhuodong` VALUES (2, '2025-11-06 21:00:00', '活动名称2', 'file/dianpuhuodongHuodongtupian2.jpg,file/dianpuhuodongHuodongtupian3.jpg,file/dianpuhuodongHuodongtupian4.jpg', '2025-11-07 21:00:00', '活动地点2', '活动内容2', '2025-11-06 21:00:00', '2025-11-08 21:00:00');
INSERT INTO `dianpuhuodong` VALUES (3, '2025-11-06 21:00:00', '活动名称3', 'file/dianpuhuodongHuodongtupian3.jpg,file/dianpuhuodongHuodongtupian4.jpg,file/dianpuhuodongHuodongtupian5.jpg', '2025-11-07 21:00:00', '活动地点3', '活动内容3', '2025-11-06 21:00:00', '2025-11-08 21:00:00');
INSERT INTO `dianpuhuodong` VALUES (4, '2025-11-06 21:00:00', '活动名称4', 'file/dianpuhuodongHuodongtupian4.jpg,file/dianpuhuodongHuodongtupian5.jpg,file/dianpuhuodongHuodongtupian6.jpg', '2025-11-07 21:00:00', '活动地点4', '活动内容4', '2025-11-06 21:00:00', '2025-11-08 21:00:00');
INSERT INTO `dianpuhuodong` VALUES (5, '2025-11-06 21:00:00', '活动名称5', 'file/dianpuhuodongHuodongtupian5.jpg,file/dianpuhuodongHuodongtupian6.jpg,file/dianpuhuodongHuodongtupian7.jpg', '2025-11-07 21:00:00', '活动地点5', '活动内容5', '2025-11-06 21:00:00', '2025-11-08 21:00:00');
INSERT INTO `dianpuhuodong` VALUES (6, '2025-11-06 21:00:00', '活动名称6', 'file/dianpuhuodongHuodongtupian6.jpg,file/dianpuhuodongHuodongtupian7.jpg,file/dianpuhuodongHuodongtupian8.jpg', '2025-11-07 21:00:00', '活动地点6', '活动内容6', '2025-11-06 21:00:00', '2025-11-08 21:00:00');
INSERT INTO `dianpuhuodong` VALUES (7, '2025-11-06 21:00:00', '活动名称7', 'file/dianpuhuodongHuodongtupian7.jpg,file/dianpuhuodongHuodongtupian8.jpg,file/dianpuhuodongHuodongtupian9.jpg', '2025-11-07 21:00:00', '活动地点7', '活动内容7', '2025-11-06 21:00:00', '2025-11-08 21:00:00');
INSERT INTO `dianpuhuodong` VALUES (8, '2025-11-06 21:00:00', '活动名称8', 'file/dianpuhuodongHuodongtupian8.jpg,file/dianpuhuodongHuodongtupian9.jpg,file/dianpuhuodongHuodongtupian10.jpg', '2025-11-07 21:00:00', '活动地点8', '活动内容8', '2025-11-06 21:00:00', '2025-11-08 21:00:00');
INSERT INTO `dianpuhuodong` VALUES (9, '2025-11-06 21:00:00', '活动名称9', 'file/dianpuhuodongHuodongtupian9.jpg,file/dianpuhuodongHuodongtupian10.jpg,file/dianpuhuodongHuodongtupian11.jpg', '2025-11-07 21:00:00', '活动地点9', '活动内容9', '2025-11-06 21:00:00', '2025-11-08 21:00:00');
INSERT INTO `dianpuhuodong` VALUES (10, '2025-11-06 21:00:00', '活动名称10', 'file/dianpuhuodongHuodongtupian10.jpg,file/dianpuhuodongHuodongtupian11.jpg,file/dianpuhuodongHuodongtupian12.jpg', '2025-11-07 21:00:00', '活动地点10', '活动内容10', '2025-11-06 21:00:00', '2025-11-08 21:00:00');

-- ----------------------------
-- Table structure for discusscantingxinxi
-- ----------------------------
DROP TABLE IF EXISTS `discusscantingxinxi`;
CREATE TABLE `discusscantingxinxi`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `refid` bigint NOT NULL COMMENT '关联表id',
  `userid` bigint NOT NULL COMMENT '用户id',
  `avatarurl` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '头像',
  `nickname` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名',
  `score` double NULL DEFAULT NULL COMMENT '评分',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '评论内容',
  `reply` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '回复内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '餐厅信息评论表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of discusscantingxinxi
-- ----------------------------
INSERT INTO `discusscantingxinxi` VALUES (1, '2025-10-25 17:06:48', 2, 11, 'file/yonghuTouxiang1.jpg', '账号1', 4, '<p>123</p>', NULL);
INSERT INTO `discusscantingxinxi` VALUES (2, '2025-10-25 17:08:22', 10, 11, 'file/yonghuTouxiang1.jpg', '账号1', 4, '<p>456</p>', NULL);

-- ----------------------------
-- Table structure for discussmeishixinxi
-- ----------------------------
DROP TABLE IF EXISTS `discussmeishixinxi`;
CREATE TABLE `discussmeishixinxi`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `refid` bigint NOT NULL COMMENT '关联表id',
  `userid` bigint NOT NULL COMMENT '用户id',
  `avatarurl` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '头像',
  `nickname` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '评论内容',
  `reply` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '回复内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '美食信息评论表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of discussmeishixinxi
-- ----------------------------

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
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '活动报名' ROW_FORMAT = DYNAMIC;

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

-- ----------------------------
-- Table structure for meishixinxi
-- ----------------------------
DROP TABLE IF EXISTS `meishixinxi`;
CREATE TABLE `meishixinxi`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `caipinmingcheng` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜品名称',
  `caipintupian` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜品图片',
  `caipinleixing` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜品类型',
  `caipinxiangqing` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '菜品详情',
  `kouwei` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '口味',
  `tuijianzhishu` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '推荐指数',
  `clicktime` datetime NULL DEFAULT NULL COMMENT '最近点击时间',
  `onelimittimes` int NULL DEFAULT -1 COMMENT '单限',
  `alllimittimes` int NULL DEFAULT -1 COMMENT '库存',
  `price` double NULL DEFAULT 0 COMMENT '价格',
  `storeup_number` int NULL DEFAULT 0 COMMENT '收藏数',
  `discuss_number` int NULL DEFAULT 0 COMMENT '评论数',
  `shangjiazhanghao` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商家账号',
  `shangjiamingcheng` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商家名称',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `meishixinxi_price`(`id` ASC, `price` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '美食信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of meishixinxi
-- ----------------------------
INSERT INTO `meishixinxi` VALUES (1, '2025-10-25 17:01:10', '生腌虾', 'file/meishixinxiCaipintupian1.jpg,file/meishixinxiCaipintupian2.jpg,file/meishixinxiCaipintupian3.jpg', '潮汕特色菜', '菜品详情1', '微辣', '★★', '2025-11-07 21:53:10', 1, 94, 99.9, 1, 1, '商家账号1', '商家名称1');
INSERT INTO `meishixinxi` VALUES (2, '2025-10-25 17:01:10', '海鲜面', 'file/meishixinxiCaipintupian2.jpg,file/meishixinxiCaipintupian3.jpg,file/meishixinxiCaipintupian4.jpg', '面食', '菜品详情2', '微辣', '★★', '2025-11-03 15:52:13', 2, 97, 99.9, 2, 2, '商家账号2', '商家名称2');
INSERT INTO `meishixinxi` VALUES (3, '2025-10-25 17:01:10', '梅菜扣肉', 'file/meishixinxiCaipintupian3.jpg,file/meishixinxiCaipintupian4.jpg,file/meishixinxiCaipintupian5.jpg', '粤菜', '菜品详情3', '微辣', '★★', '2025-11-02 17:40:31', 3, 99, 99.9, 3, 3, '商家账号3', '商家名称3');
INSERT INTO `meishixinxi` VALUES (4, '2025-10-25 17:01:10', '山药卷', 'file/meishixinxiCaipintupian4.jpg,file/meishixinxiCaipintupian5.jpg,file/meishixinxiCaipintupian6.jpg', '小吃', '菜品详情4', '甜', '★★', '2025-11-02 13:23:50', 4, 98, 99.9, 4, 4, '商家账号4', '商家名称4');
INSERT INTO `meishixinxi` VALUES (5, '2025-10-25 17:01:10', '卤味拼盘', 'file/meishixinxiCaipintupian5.jpg,file/meishixinxiCaipintupian6.jpg,file/meishixinxiCaipintupian7.jpg', '卤味', '菜品详情5', '微辣', '★★', '2025-11-03 17:01:10', 5, 99, 99.9, 5, 5, '商家账号5', '商家名称5');
INSERT INTO `meishixinxi` VALUES (6, '2025-10-25 17:01:10', '日式鳗鱼饭', 'file/meishixinxiCaipintupian6.jpg,file/meishixinxiCaipintupian7.jpg,file/meishixinxiCaipintupian8.jpg', '日式料理', '菜品详情6', '微辣', '★★★', '2025-11-02 17:49:28', 6, 99, 99.9, 6, 6, '商家账号6', '商家名称6');
INSERT INTO `meishixinxi` VALUES (7, '2025-10-25 17:01:10', '煎牛排', 'file/meishixinxiCaipintupian7.jpg,file/meishixinxiCaipintupian8.jpg,file/meishixinxiCaipintupian9.jpg', '西餐 ', '菜品详情7', '微辣', '★★', '2025-11-03 17:01:10', 7, 99, 99.9, 7, 7, '商家账号7', '商家名称7');
INSERT INTO `meishixinxi` VALUES (8, '2025-10-25 17:01:10', '牛肉丸', 'file/meishixinxiCaipintupian8.jpg,file/meishixinxiCaipintupian9.jpg,file/meishixinxiCaipintupian10.jpg', '丸子类', '菜品详情8', '鲜', '★★', '2025-11-03 17:01:10', 8, 99, 99.9, 8, 8, '商家账号8', '商家名称8');
INSERT INTO `meishixinxi` VALUES (9, '2025-10-25 17:01:10', '鱼卷', 'file/meishixinxiCaipintupian9.jpg,file/meishixinxiCaipintupian10.jpg,file/meishixinxiCaipintupian11.jpg', '闽菜', '菜品详情9', '微辣', '★★', '2025-11-03 17:01:10', 9, 99, 99.9, 9, 9, '商家账号9', '商家名称9');
INSERT INTO `meishixinxi` VALUES (10, '2025-10-25 17:01:10', '香辣小河虾', 'file/meishixinxiCaipintupian10.jpg,file/meishixinxiCaipintupian11.jpg,file/meishixinxiCaipintupian12.jpg', '小炒类', '菜品详情10', '中辣', '★★', '2025-11-02 18:40:16', 10, 98, 99.9, 11, 10, '商家账号10', '商家名称10');
INSERT INTO `meishixinxi` VALUES (11, '2025-10-25 17:01:10', '藤椒牛杂锅', 'file/meishixinxiCaipintupian11.jpg', '川菜', '菜品详情11', '超辣', '★★★★★', '2025-11-02 17:48:10', 20, 20, 20, 0, 0, '商家账号1', '商家名称1');
INSERT INTO `meishixinxi` VALUES (12, '2025-10-25 17:01:10', '排骨冬瓜汤', 'file/meishixinxiCaipintupian12.jpg', '汤类', '<p>菜品详情12</p>', '清淡', '★★★★', '2025-11-07 22:56:08', 1, 44, 66, 0, 0, NULL, NULL);

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `menujson` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '菜单' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES (1, '2025-03-25 17:01:11', '[{\"backMenu\":[{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"预约\"],\"appFrontIcon\":\"cuIcon-goods\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"classname\":\"cheweixinxi\",\"menu\":\"车位信息\",\"menuJump\":\"列表\",\"tableName\":\"cheweixinxi\"},{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"支付\",\"首页总数\"],\"appFrontIcon\":\"cuIcon-time\",\"buttons\":[\"查看\",\"删除\",\"支付\",\"首页总数\"],\"classname\":\"cheweiyuyue\",\"menu\":\"车位预约\",\"menuJump\":\"列表\",\"tableName\":\"cheweiyuyue\"}],\"fontClass\":\"icon-common40\",\"menu\":\"车位信息管理\",\"unicode\":\"&#xeebb;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"查看评论\",\"预约\"],\"appFrontIcon\":\"cuIcon-present\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"查看评论\"],\"classname\":\"cantingxinxi\",\"menu\":\"餐厅信息\",\"menuJump\":\"列表\",\"tableName\":\"cantingxinxi\"},{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"审核\"],\"appFrontIcon\":\"cuIcon-time\",\"buttons\":[\"查看\",\"删除\",\"审核\"],\"classname\":\"cantingyuyue\",\"menu\":\"餐厅预约\",\"menuJump\":\"列表\",\"tableName\":\"cantingyuyue\"}],\"fontClass\":\"icon-common23\",\"menu\":\"餐厅信息管理\",\"unicode\":\"&#xee05;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"appFrontIcon\":\"cuIcon-newshot\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"classname\":\"caipinleixing\",\"menu\":\"菜品类型\",\"menuJump\":\"列表\",\"tableName\":\"caipinleixing\"},{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"菜品类型\",\"查看评论\",\"首页统计\"],\"appFrontIcon\":\"cuIcon-taxi\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"菜品类型\",\"查看评论\",\"首页统计\"],\"classname\":\"meishixinxi\",\"menu\":\"美食信息\",\"menuJump\":\"列表\",\"tableName\":\"meishixinxi\"}],\"fontClass\":\"icon-common38\",\"menu\":\"菜品信息管理\",\"unicode\":\"&#xeeb2;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"月销额\",\"年销额\",\"品销量\",\"导出\",\"首页总数\",\"首页统计\"],\"appFrontIcon\":\"cuIcon-skin\",\"buttons\":[\"查看\",\"删除\",\"月销额\",\"年销额\",\"品销量\",\"导出\",\"首页总数\",\"首页统计\"],\"menu\":\"订单\",\"tableName\":\"orders\"},{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"月销额\",\"年销额\",\"品销量\",\"导出\",\"首页总数\",\"首页统计\"],\"appFrontIcon\":\"cuIcon-skin\",\"buttons\":[\"查看\",\"删除\",\"月销额\",\"年销额\",\"品销量\",\"导出\",\"首页总数\",\"首页统计\"],\"menu\":\"未支付订单\",\"menuJump\":\"未支付\",\"tableName\":\"orders\"},{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"月销额\",\"年销额\",\"品销量\",\"导出\",\"首页总数\",\"首页统计\",\"发货\",\"物流\",\"核销\"],\"appFrontIcon\":\"cuIcon-skin\",\"buttons\":[\"查看\",\"删除\",\"月销额\",\"年销额\",\"品销量\",\"导出\",\"首页总数\",\"首页统计\",\"核销\"],\"menu\":\"已支付订单\",\"menuJump\":\"已支付\",\"tableName\":\"orders\"},{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"月销额\",\"年销额\",\"品销量\",\"导出\",\"首页总数\",\"首页统计\",\"物流\"],\"appFrontIcon\":\"cuIcon-skin\",\"buttons\":[\"查看\",\"删除\",\"月销额\",\"年销额\",\"品销量\",\"导出\",\"首页总数\",\"首页统计\"],\"menu\":\"已完成订单\",\"menuJump\":\"已完成\",\"tableName\":\"orders\"},{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"月销额\",\"年销额\",\"品销量\",\"导出\",\"首页总数\",\"首页统计\"],\"appFrontIcon\":\"cuIcon-skin\",\"buttons\":[\"查看\",\"删除\",\"月销额\",\"年销额\",\"品销量\",\"导出\",\"首页总数\",\"首页统计\"],\"menu\":\"已取消订单\",\"menuJump\":\"已取消\",\"tableName\":\"orders\"},{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"月销额\",\"年销额\",\"品销量\",\"导出\",\"首页总数\",\"首页统计\",\"物流\"],\"appFrontIcon\":\"cuIcon-skin\",\"buttons\":[\"查看\",\"删除\",\"月销额\",\"年销额\",\"品销量\",\"导出\",\"首页总数\",\"首页统计\"],\"menu\":\"已退款订单\",\"menuJump\":\"已退款\",\"tableName\":\"orders\"}],\"fontClass\":\"icon-common33\",\"menu\":\"订单管理\",\"unicode\":\"&#xee6a;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"appFrontIcon\":\"cuIcon-addressbook\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"classname\":\"wodeyuyue\",\"menu\":\"我的预约\",\"menuJump\":\"列表\",\"tableName\":\"wodeyuyue\"}],\"fontClass\":\"icon-common41\",\"menu\":\"我的预约管理\",\"unicode\":\"&#xeede;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"appFrontIcon\":\"cuIcon-newshot\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"classname\":\"users\",\"menu\":\"管理员\",\"menuJump\":\"列表\",\"tableName\":\"users\"},{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"appFrontIcon\":\"cuIcon-paint\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"classname\":\"yonghu\",\"menu\":\"用户\",\"menuJump\":\"列表\",\"tableName\":\"yonghu\"}],\"fontClass\":\"icon-user2\",\"menu\":\"用户管理\",\"unicode\":\"&#xef98;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"appFrontIcon\":\"cuIcon-addressbook\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"classname\":\"config\",\"menu\":\"轮播图\",\"menuJump\":\"列表\",\"tableName\":\"config\"},{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"appFrontIcon\":\"cuIcon-pay\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"classname\":\"news\",\"menu\":\"新闻资讯\",\"menuJump\":\"列表\",\"tableName\":\"news\"},{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"appFrontIcon\":\"cuIcon-flashlightopen\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"classname\":\"systemNotice\",\"menu\":\"系统公告\",\"menuJump\":\"列表\",\"tableName\":\"system_notice\"}],\"fontClass\":\"icon-common12\",\"menu\":\"系统管理\",\"unicode\":\"&#xedf4;\"}],\"frontMenu\":[{\"child\":[{\"appFrontIcon\":\"cuIcon-send\",\"buttons\":[\"查看\",\"查看评论\"],\"classname\":\"meishixinxi\",\"menu\":\"美食信息\",\"menuJump\":\"列表\",\"tableName\":\"meishixinxi\"}],\"menu\":\"信息管理\"},{\"child\":[{\"appFrontIcon\":\"cuIcon-explore\",\"buttons\":[\"查看\"],\"classname\":\"dianpuhuodong\",\"menu\":\"店铺活动\",\"menuJump\":\"列表\",\"tableName\":\"dianpuhuodong\"}],\"menu\":\"店铺活动管理\"},{\"child\":[{\"appFrontIcon\":\"cuIcon-goods\",\"buttons\":[\"查看\"],\"classname\":\"cart\",\"menu\":\"购物车\",\"menuJump\":\"列表\",\"tableName\":\"cart\"}],\"menu\":\"购物车管理\"},{\"child\":[{\"appFrontIcon\":\"cuIcon-circle\",\"buttons\":[\"查看\",\"预约\"],\"classname\":\"cheweixinxi\",\"menu\":\"车位信息\",\"menuJump\":\"列表\",\"tableName\":\"cheweixinxi\"}],\"menu\":\"车位信息管理\"},{\"child\":[{\"appFrontIcon\":\"cuIcon-discover\",\"buttons\":[\"查看\",\"查看评论\",\"预约\"],\"classname\":\"cantingxinxi\",\"menu\":\"餐厅信息\",\"menuJump\":\"列表\",\"tableName\":\"cantingxinxi\"}],\"menu\":\"餐厅信息管理\"}],\"hasBackLogin\":\"是\",\"hasBackRegister\":\"否\",\"hasFrontLogin\":\"否\",\"hasFrontRegister\":\"否\",\"pathName\":\"users\",\"roleName\":\"管理员\",\"tableName\":\"users\"},{\"backMenu\":[{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"支付\",\"首页总数\"],\"appFrontIcon\":\"cuIcon-time\",\"buttons\":[\"查看\",\"支付\"],\"classname\":\"cheweiyuyue\",\"menu\":\"车位预约\",\"menuJump\":\"列表\",\"tableName\":\"cheweiyuyue\"}],\"fontClass\":\"icon-common40\",\"menu\":\"车位信息管理\",\"unicode\":\"&#xeebb;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"审核\"],\"appFrontIcon\":\"cuIcon-time\",\"buttons\":[\"查看\"],\"classname\":\"cantingyuyue\",\"menu\":\"餐厅预约\",\"menuJump\":\"列表\",\"tableName\":\"cantingyuyue\"}],\"fontClass\":\"icon-common23\",\"menu\":\"餐厅信息管理\",\"unicode\":\"&#xee05;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"月销额\",\"年销额\",\"品销量\",\"导出\",\"首页总数\",\"首页统计\",\"发货\",\"物流\",\"核销\"],\"appFrontIcon\":\"cuIcon-skin\",\"buttons\":[\"查看\"],\"menu\":\"已支付订单\",\"menuJump\":\"已支付\",\"tableName\":\"orders\"},{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"月销额\",\"年销额\",\"品销量\",\"导出\",\"首页总数\",\"首页统计\",\"物流\"],\"appFrontIcon\":\"cuIcon-skin\",\"buttons\":[\"查看\"],\"menu\":\"已完成订单\",\"menuJump\":\"已完成\",\"tableName\":\"orders\"},{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"月销额\",\"年销额\",\"品销量\",\"导出\",\"首页总数\",\"首页统计\",\"物流\"],\"appFrontIcon\":\"cuIcon-skin\",\"buttons\":[\"查看\"],\"menu\":\"已退款订单\",\"menuJump\":\"已退款\",\"tableName\":\"orders\"}],\"fontClass\":\"icon-common33\",\"menu\":\"订单管理\",\"unicode\":\"&#xee6a;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"appFrontIcon\":\"cuIcon-addressbook\",\"buttons\":[\"查看\"],\"classname\":\"wodeyuyue\",\"menu\":\"我的预约\",\"menuJump\":\"列表\",\"tableName\":\"wodeyuyue\"}],\"fontClass\":\"icon-common41\",\"menu\":\"我的预约管理\",\"unicode\":\"&#xeede;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"appFrontIcon\":\"cuIcon-cardboard\",\"buttons\":[\"查看\"],\"classname\":\"storeup\",\"menu\":\"我的收藏\",\"menuJump\":\"1\",\"tableName\":\"storeup\"}],\"fontClass\":\"icon-common2\",\"menu\":\"我的收藏管理\",\"unicode\":\"&#xeda4;\"}],\"frontMenu\":[{\"child\":[{\"appFrontIcon\":\"cuIcon-send\",\"buttons\":[\"查看\",\"查看评论\"],\"classname\":\"meishixinxi\",\"menu\":\"美食信息\",\"menuJump\":\"列表\",\"tableName\":\"meishixinxi\"}],\"menu\":\"信息管理\"},{\"child\":[{\"appFrontIcon\":\"cuIcon-explore\",\"buttons\":[\"查看\"],\"classname\":\"dianpuhuodong\",\"menu\":\"店铺活动\",\"menuJump\":\"列表\",\"tableName\":\"dianpuhuodong\"}],\"menu\":\"店铺活动管理\"},{\"child\":[{\"appFrontIcon\":\"cuIcon-goods\",\"buttons\":[\"查看\"],\"classname\":\"cart\",\"menu\":\"购物车\",\"menuJump\":\"列表\",\"tableName\":\"cart\"}],\"menu\":\"购物车管理\"},{\"child\":[{\"appFrontIcon\":\"cuIcon-circle\",\"buttons\":[\"查看\",\"预约\"],\"classname\":\"cheweixinxi\",\"menu\":\"车位信息\",\"menuJump\":\"列表\",\"tableName\":\"cheweixinxi\"}],\"menu\":\"车位信息管理\"},{\"child\":[{\"appFrontIcon\":\"cuIcon-discover\",\"buttons\":[\"查看\",\"查看评论\",\"预约\"],\"classname\":\"cantingxinxi\",\"menu\":\"餐厅信息\",\"menuJump\":\"列表\",\"tableName\":\"cantingxinxi\"}],\"menu\":\"餐厅信息管理\"}],\"hasBackLogin\":\"否\",\"hasBackRegister\":\"否\",\"hasFrontLogin\":\"是\",\"hasFrontRegister\":\"是\",\"pathName\":\"yonghu\",\"roleName\":\"用户\",\"tableName\":\"yonghu\"}]');

-- ----------------------------
-- Table structure for news
-- ----------------------------
DROP TABLE IF EXISTS `news`;
CREATE TABLE `news`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '内容',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标题',
  `introduction` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '简介',
  `picture` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图片',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '新闻资讯' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of news
-- ----------------------------
INSERT INTO `news` VALUES (1, '2025-03-25 17:01:09', '可是，这样小心翼翼的你，一定很累吧。你以为照顾了所有人的想法，就能得到他们的喜欢;你以为逼着自己合群，就能真的合群。直到后来才发现，在日复一日对自己的为难中，你逐渐弄丢了真实的自己.\n殊不知，人生在世，最不值得的事情，就是在别人的眼光中，迷失自己。其实，正如世界上没有十全十美的人，你也同样无法做到让每个人满意。\n世上没有不快乐的人，只有不肯让自己快乐的心。有一句话说:\n如果一段关系让你变得卑微，你可以选择抽身离去;如果一个人总是让你悲伤哭泣，你要学会放手。\n始终相信，每个人最初的样子，都是最美好的。真正喜欢你的人，会喜欢你原来的样子:真正爱你的从来不需要你的委曲求全。\n后半生，请把身上的枷锁，全部解除。去做喜欢的事，去听自己内心的声音。你会发现，这世上没有什么事情比活出自己更加幸福。\n要相信，真实的自己亦是一道风景线。你若盛开，清风自来。愿你此生尽兴，不负韶华，', '余生，愿你活出自己的精彩', '曾几何时，为了让自己显得合群，选择去做自己不喜欢的事;为了讨好喜欢的人，一次次地委屈自己;甚至，为了陌生人的一个眼神，就陷入了深深的自我怀疑。', 'file/newsPicture1.jpg');
INSERT INTO `news` VALUES (2, '2025-03-25 17:01:09', '然而，世上有很多事情没有再来一次的机会，一旦错失就意味着无法弥补，正如人与人之间，有的只是一世的缘分，一旦转身，就意味着再无可能。\n这一生只活一次，有些人也只爱一回。\n所以，在拥有时就好好把握，对于来世，可以适当憧憬，但不要一昧去幻想，活着，唯有珍惜身边做好眼前事，才能让明天少一些遗憾。人，\n既然有幸来到这个世上，那就要努力把这辈子活好。岁月其实远没有想象中的那么漫长，若是能爱就好好爱，别让说好相伴一生的人，中途散走;若是当下能相知相守，就别去等待虚无缥缈的未来。\n就像一位网友留言说的:我从来不去期待下辈子，我要的只有今生，世事无常，来世是怎样的光景，谁都无法预知。对我来说，在有限的时光里，善待爱自己及自己爱的人，用心珍惜相聚的每一时刻，努力过好当下就足够了。\n时光匆匆，韶华似水，每个人都只有这短暂的一生。\n有些事，如果喜欢就坚定努力的去做，有些人，如果爱就真诚用心地去爱，别幻想着重来，别总是寄希望于下辈子。\n下辈子，彼此也许再也不会遇见，就算遇见了，谁还会记得谁，今生相互承诺的两个人，来生也许相见不相识。\n想起之前看过的一句话:人生没有假如，我们没有来世，下辈子，你我也许只是大千世界里互不相识的两个陌生人。\n的确，生命只有一次，今生有幸相遇的人，下辈子不一定能遇见，就算能遇见，谁能把谁陪伴，谁又是谁的谁?\n余生不长，趁着岁月未老，好好珍惜这辈子的缘分吧，别等到失去了才悔不该当初。', '岁月无常，好好珍惜', '常听到这样的话，如果一切可以重新开始，我一定改掉任性，学会坚持，如果有下辈子，我一定好好珍惜，不会再弄丢原本属于自己的幸福。', 'file/newsPicture2.jpg');
INSERT INTO `news` VALUES (3, '2025-03-25 17:01:09', '人生短短几十年，为什么要蹉跎浪费在无可挽回的失去上，为什么不酒脱一点，看开一点，活得轻松快乐一点。\n这世间，没有谁的人生是十全十美的，总要面对一些离别，总要经历一些失去，如果总对身边的一切执念太深，只会越活越累。\n时间不停向前，人也不可避免的要向前看。面对一些不如意、不顺心的事，该放的就放，该忘的就忘，别总把自己困在失败的悲伤中。\n生而为人，放不是无能，而是一种洒脱，忘更不是懦弱，而是一种智慧。\n在来去匆匆，聚散不定的现实里，放下不属于自己的感情，才能有新的开始，忘记心中的烦恼，才能收获愉快的心情。\n所以，有些离开的人，该放就放下吧，一直念念不忘，伤害的是自己，有些烦心事，该忘就忘了吧一直耿耿于怀，痛苦的是自己。\n-辈子本就不长，何必让自己活得那么累，过去的情就让它过去，别再留恋，想不通的事就算了，别再纠结。\n人生的旅途，总有些事让人烦，总有些情让人伤。只有学会忘记过往的不如意，忘记一些不必要的烦恼，以轻盈的身心重新出发，才不会被旧人旧事所累。\n记得，每个人心灵的空间都是有限的，当装多了纠结与伤感，就装不下自在与欢快了。要想日子过得简单轻松一点，就要懂得放下执念，忘掉痛苦，清除伤。', '与其背着烦恼活，不如带着美好过', '一直觉得，感情里，无疾而终并不可怕，可怕的是结束后，你还抱着回忆不肯放;岁月中，失去并不可怕，可怕的是失去后，你还揪着曾经不肯忘。\n有些事，明知道不应该继续，却始终没有转身的勇气;有些人，明知道已经成为过去，却还是舍不得放手;有时候，明知道纠缠于往事旧人只会徒增伤悲，却仍然执着的去回忆，去留恋。', 'file/newsPicture3.jpg');
INSERT INTO `news` VALUES (4, '2025-03-25 17:01:09', '年轻人，可那毕竟不是现实，理想很丰满，现实很骨感这不是玩笑，你不走出来，怎么会知\n你真的想等到某天喜欢上一个姑娘时，才去纠结咖啡钱电影钱你要如何与爸妈张口，才去苦恼为她买礼物你要怎样省吃俭用节衣缩食?\n你没有挥金如土的.魄力，没有殷实的家境，没有既定的光芒锦绣的前程，二十出头的年纪，你不努力，你想拿什么报答那个在你身边用最好的青春陪伴你的姑娘?你又拿什么报答含辛茹苦养育你这么多年的父母?\n生活不是游戏，不会给你那么多反复再来的机会，青春年少风华正茂，年轻人，你不努力，你想指望什么?\n年轻人，走出来奋斗吧!累也好苦也罢。如果你现在对自己各种放纵，你指望以后你用什么条件来放松?\n年轻人，你要努力赚钱，为了自己，也为了你爸妈。为了他们在以后逛商场的时候，可以给自己买东西像给你买东西一样毫不犹豫。为了他们可以在同老友谈起你时可以一脸安详，而不是想着，这个月，儿子交了房租还有钱吃饭么。我们这个年龄的人，已经像吸血鬼一样巴着父母活了二十多年了，还真的要继续吸着他们的血活下去么?\n年轻人，停止期望凡事简单容易。如果总是挑简单的事情做，那你永远成不了大器，生活总会在始料不及时考验你，所以你必须挑战困难，保持坚强，世上任何值得拥有的东西都需要付诸努力，勤奋与自律，倘若自甘平庸，那你就会变得庸碌无为。\n年轻人，与我们一起，挑战一下自己，拥抱一下梦想。你想要的生活，跳起来，一定够得到!', '理想很丰满，现实很骨感', '游戏里的你帅气无比，乘风御剑，通关杀敌无所不能，你满足于那其中的成就感，你满足于通关的畅快淋漓。', 'file/newsPicture4.jpg');
INSERT INTO `news` VALUES (5, '2025-03-25 17:01:09', '是否会撕裂黑暗的结界?给理想一点时间，是否会完成理想的目标?太多时候，黑给黑暗一缕曙光，暗限制了理想的脚步，时间成了撕裂黑暗的曙光。社会的压抑，未知的迷茫，甚至是事实的打击，都成了所谓的黑暗。\n理想应该是一种精神上的追求，一种我们内心深处的渴望。也许理想的种类太多，不一样的人也有不一样的理想。但唯一的共同点就是我们在追求，追求自己精神上亦或是心中的目标。但总是不尽人意，一来存在太多不确定因素，二来时间还不够。理想并不是一而就的空话。\n想过放弃，因为理想与现实的差距。但又不想放弃，因为理想是精神上的追求心中的渴望。纠结了太多，却是否想过在纠结的时候，已经选择了错过。被刻意选择的错过是什么?是路过。我们在不经意间路过了多少理想，只是因为纠结。如果坚定自己的理想，用时间去行动，是否会少几次刻意的错过:-次，与一位朋友谈到如何乐观，然后追求理想。朋友想了一会儿，给我的回答是正能量与时间。他说，想要乐观就要不断给自己正能量。事物有阴阳两面，如果阳代表乐观，那在我们看待一件事时要看阳面千万别看阴面。这样自己不会消极，某种程度上来说就是一种正能量。想要追求理想，务必要花费时间。正如没有风平浪静的大海，想要追求理想，要时间。给理想一点时间，让自己去追求，不顾切的追求。\n点点滴滴的小事构成了我们的生活，点点滴滴的努力给理想铺平了路。每一次小成功都为理想迈进了一步。每一次失败，都告诉我们一些教训，反方向来说为下一次成功提供了经验。不要觉得失败是可怕的，长远来看，正是失败才有了成功。\n时间在流逝。如果安于现状，就会乐不思。理想需要时间不代表我们可以挥霍，我们能做的是珍惜时间，去为理想拼搏。\n间接意义上讲，理想的时间是我们的努力。\n如果正在路上，一直任性向前走，忘记时间不回头。', '理想是一种精神上的追求', '如果前方是一片黑暗，理想是否会黯淡?黑暗终究不能限制理想的步伐，只是需要时间。', 'file/newsPicture5.jpg');
INSERT INTO `news` VALUES (6, '2025-03-25 17:01:09', '我依然清楚的记得:\n当甲午战争战败，日寇无礼踏破中国的门户;当八国联军侵入北京，无情掠夺中国的财产:当七七事变发生，中国的老人、妇孺被残忍杀害的时候，我在想那时中国的梦是怎样的!\n我虽不曾亲眼看到，但那却是铁一般的事实。因为从老人们那深邃的眼神中可以感到无尽的愤懑;从他们干瘪的脸颊可以看到深情的泪水，从他们嘹亮的军歌中可以想到那奋勇杀敌时的豪迈;从他们激昂话语中听到那誓要捍卫家园振兴中华的誓言。作为新一代青年的我们难道不应该树立远大的理想，付之以踏实的行动，去继承先辈们的使命。去实现中华民族的伟大崛起和复兴吗?\n有梦才能使中国繁荣!\n在改革开放以来中国取得了一系列的可以载入中国史册的成就。香港、澳门的回归，经济特区的建立，使中国成为发展国家中的经济大国，科技先进国和军事强国。当中国成功举办奥运的时候，当神九飞天的时候，当蛟龙入海的时候，当航母下水的时候，当莫言荣获诺贝尔文学奖的时候。我相信每个人都感觉到了无比的自豪。但是现在的中国与其他发达国家还有很大差距。作为新一代的我们，难道不应该志存高远吗?\n我想有的人会说，我们的力量是有限的。的确个人的力量很渺小，但是中国梦就是因一个个微不足道的个人的梦一直汇集、汇集，然后凝聚成的一个巨大的梦。冯至在《十四行诗》中写道，我们准备着，深深领受，那些意想不到的奇迹，在漫长的岁月里，忽然有彗星的出现，狂风乍起。\n梦想是美丽的，它是美的期望;梦想是阳光的，它使人由浮躁走向踏实;梦想是充满力量的，它可以激发人身体里无限的潜能。我们期盼的是国泰民安、经济发展、政治清明、文化繁荣、社会和谐、生态良\n好、公平正义。这才是中国人伟大的梦', '人生如梦，梦想是帆', '人生如梦，梦想是帆，每个人都有一个只属于自己的梦，但我们同属一个国家，所以每个人的梦又与国家的兴衰荣辱紧密相连。先哲顾炎武曾说:天下兴亡，匹夫有责。只有国家好，大家才能好。', 'file/newsPicture6.jpg');
INSERT INTO `news` VALUES (7, '2025-03-25 17:01:09', '风吹过山 船靠了岸  风光呀 一点点看  我走向北 你去往南\n故事呀 一篇篇翻  好烦 又加班到很晚  你搭上空荡的 地铁已是末班\n好烦 很爱却要分开  恋爱谈不明白  好烦 接近理想好难 却又还很不甘\n如何拥抱平淡  如果 最难得到圆满  不如选择坦然 若是月亮还没来\n路灯也可照窗台  照着白色的山茶花微微开  若是晨风还没来\n晚风也可吹入怀  吹着那一地树影温柔摇摆  你总以为你不够好\n不够苗条和美貌  可是完美谁能达到  做你自己就很好\n烦恼烦恼拥有太少  没有房车没有钞票  可爱你的人永远会 \n把你当做心尖珍宝  我也懂大多数的时候  你只想逃离这世界\n我也懂太多的情绪在  一个人失眠的深夜 你可以是悲伤或者埋怨\n但请不要放弃明天  这一直灰暗的世界  我想看见你的笑脸\n若是月亮还没来  路灯也可照窗台  照着白色的山茶花微微开\n若是晨风还没来  晚风也可吹入怀  吹着那一地树影温柔摇摆\n若是月亮还没来  路灯也可照窗台  照着白色的山茶花微微开\n若是晨风还没来  晚风也可吹入怀  吹着那一地树影温柔摇摆\n李哲今年1月份创作了《若月亮没来》这首歌。随着歌曲的热度不断上升，约他写歌的人越来越多，他的工作变得更加繁忙。\n近年来，李哲凭借对音乐的热爱和不懈的努力，创作了几百首音乐作品，其中不乏广为人知的佳作，例如王栎鑫的《一个人》、何赛飞与叶炫清在央视演唱的《木兰诗》等歌曲，都是由李哲作曲。\n如今，李哲的音乐才华和努力得到了业界的广泛认可，他的音乐作品多次登上各大音乐平台的热歌榜，成为备受瞩目的新生代音乐人。现在，走在街头、坐在车里，经常会听到自己的作品。看到自己的音乐作品受到如此广泛的喜爱和认可，我感到非常有成就感。李哲笑着说，他会努力创作更多优秀音乐作品，为家乡争光。', '若月亮没来-王宇宙Leto，乔浚丞', '《若月亮没来(若是月亮还没来)》是王宇宙Leto，乔浚丞演唱的歌曲，于2024年1月22日上线发行。', 'file/newsPicture7.jpg');
INSERT INTO `news` VALUES (8, '2025-03-25 17:01:09', '放进去了的青春，走不出来的悲伤却总是给年少时的我们留下太多无法割舍的情怀。记录岁月的足迹，聆听时光的故事。然轻吟浅唱出，青春嘉年华。\n故事里总会有，那些太多感慨于时间的无情，太多对往事的追忆，太多的太多，而今刻在了心里也停泻在了流年里。一段无法抹去的回忆，也只不过是一场烟火燃放的瞬间，却也一辈子在脑海里兜兜转转。很是怀念那些相遇的日子，邂逅了幸福也留在了时间轨迹里。时而想起，那些曾肩并肩奋斗的日子，那些写满感动与快乐的岁月。\n如今，我们似乎渐行渐远，彼此奋斗着青春的轮回。渐渐的，我们都长大了，时间改变了我们时刻分享彼此快乐与不快乐的习惯，青春的渐逝，岁月的更迭让我们变得都不再联系，身边的朋友也越来越少;那些欢笑，那些幸福的流年也随着我们的陌生而变得更加奢侈。也许多年以后，我们都有了自己的朋友，有了自己关心的人。那时，我们再细数那些癫狂在你生命里一辈子的记忆故事，蓦然发现一切都停留在了回忆里。', '-米阳光传递一份温暖', '黑暗中跌倒，坚强里苦笑。只是因为青春还在，所以梦想一直在路上。当年华未曾老去，青春没有散场，留给幽暗的岁月，还有几分值得珍藏?孤独无助时、伤心难过时总爱恣意挥霍愤怒。伤了、痛了原本以为就都结束了;可是心里还不甘愿放弃，就只为一个理由，有梦想的人永不孤单。', 'file/newsPicture8.jpg');
INSERT INTO `news` VALUES (9, '2025-03-25 17:01:09', '你开始不动声色的扮演着一个大人，经历着生活日复一日的打。\n每天醒在天还没亮的五六点，挤过早高峰的地下铁，面对成山的工作，加班到深夜倒下就睡更成为了日常，还要应付各色的人际关系，逐渐开始接受努力也不一定有回报的道理。\n这段日子也许很长，但你没有放弃对生活的信心，始终相信人生总会变得一帆风顺，更坚信每件事的最后都会有一个好的结局，如果不是，说明还没到最后。\n于是你树立新的目标，让这一年的奔波更有方向。\n虽然永远无法预料明天是晴是雨，也无法预知你在乎的人是否还在身边，你一直以来的坚持究竟能换来什么。\n但你能决定的是，今天有没有备好雨伞，有没有好好爱人以及是否为自己追求的理想拼尽全力。\n永远不要只看见前方路途遥远而忘了从前的自己坚持了多久才走到了这里，\n今天拼尽全力虽然艰辛万分，在未来都将成为落在你身上的礼物。', '自己的路自己走，自己的心自己懂', '岛上书店》中有句话:每个人的生命里，都有最艰难的那一年，将人生变得美好而辽阔。\n你有没有经历过那么一年，在走过的岁月中跋涉得异常艰难困苦，却仍无法灭前行的勇气。哪怕所有人都极力劝阻，也要不甘心地试一试;哪怕前方已没有道路，却还在坚定前行;哪怕再看不到任何希望，也要竭尽全力地为自己争取一个机会。', 'file/newsPicture9.jpg');
INSERT INTO `news` VALUES (10, '2025-03-25 17:01:09', '<p>这世界上的人们都活的匆匆忙忙，每个人看起来都很焦虑，每个人都有自己的苦楚，却往往找不到人可以诉说。也许你的倾诉，对别人来说是一种打扰，而且别人没经历过你的经历，所以无法体会你的心情。我们总是在心情不好的时候拿着手机，翻翻通讯录，然后再默默放下手机，压住心中那股想要找人说说话的想法。</p><p>等到低落的情绪慢慢有些好转，才开始明白，人这一生，无论是什么样的路，都是自己的选择，冲动也好，深思熟虑也罢，自己选的路，不管多难，都要自己走下去。心中的感受，不管是复杂还是简单，都只能自己来懂，别人帮不了你，也安慰不了你，顶多给你一些无济于事的劝告，最后还得靠自己想开。你选的幸福，你自己享受;你选的眼泪，你自己来流，生活就是如此，自己的路，自己走;自己的心，自己懂。</p>', '有过往可回头', '此去经年，用一张离家的车票，换一段有故事可说的人生。\n大概就是那时候怀揣着梦想，背上了行囊，一腔热血决定远行的你。独自拎着一只行李箱，背着双肩包在深夜抵达一个陌生的城市，站在人潮涌动的车站出口，望着宽敞公路上车水马龙的喧嚣，城市里亮如白昼的繁华。', 'file/newsPicture10.jpg');

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `orderid` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单编号',
  `tablename` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'caipinxinxi' COMMENT '商品表名',
  `goodid` bigint NOT NULL COMMENT '商品id',
  `goodname` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商品名称',
  `picture` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图片',
  `buynumber` int NULL DEFAULT NULL COMMENT '购买数量',
  `price` double NULL DEFAULT NULL COMMENT '单价',
  `discountprice` double NULL DEFAULT NULL COMMENT '折扣价',
  `total` double NULL DEFAULT NULL COMMENT '总价',
  `discounttotal` double NULL DEFAULT NULL COMMENT '折扣总价格',
  `type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '支付类型',
  `status` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '订单状态',
  `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '地址',
  `tel` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电话',
  `consignee` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '收货人',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `logistics` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '物流',
  `role` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户角色',
  `userid` bigint NOT NULL COMMENT '用户id',
  `shangjiazhanghao` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商户名称',
  `goodtype` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商品类型',
  `canzhuomingcheng` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '餐桌名称',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `orderid`(`orderid` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '订单' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (1, '2025-03-25 17:06:12', '20250325170612297987', 'meishixinxi', 10, '菜品名称10', 'file/meishixinxiCaipintupian10.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已完成', NULL, NULL, NULL, '', NULL, 'yonghu', 11, '商家账号10', '菜品类型10');
INSERT INTO `orders` VALUES (2, '2025-11-02 03:08:14', '20251102030814535592', 'meishixinxi', 1, '菜品名称1', 'file/meishixinxiCaipintupian1.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已支付', NULL, NULL, NULL, '', NULL, 'yonghu', 11, '商家账号1', '菜品类型1');
INSERT INTO `orders` VALUES (3, '2025-11-02 13:23:35', '20251102132335451607', 'meishixinxi', 4, '菜品名称4', 'file/meishixinxiCaipintupian4.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已支付', NULL, NULL, NULL, '', NULL, 'yonghu', 11, '商家账号4', '菜品类型4');
INSERT INTO `orders` VALUES (4, '2025-11-02 13:23:49', '20251102132349820663', 'meishixinxi', 4, '菜品名称4', 'file/meishixinxiCaipintupian4.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '未支付', NULL, NULL, NULL, '', NULL, 'yonghu', 11, '商家账号4', '菜品类型4');
INSERT INTO `orders` VALUES (5, '2025-11-02 17:48:53', '20251102174853680425', 'meishixinxi', 2, '海鲜面', 'file/meishixinxiCaipintupian2.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '未支付', NULL, NULL, NULL, '', NULL, 'yonghu', 11, '商家账号2', '面食');
INSERT INTO `orders` VALUES (6, '2025-11-02 17:49:12', '20251102174912127554', 'meishixinxi', 1, '生腌虾', 'file/meishixinxiCaipintupian1.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已支付', NULL, NULL, NULL, '', NULL, 'yonghu', 11, '商家账号1', '潮汕特色菜');
INSERT INTO `orders` VALUES (7, '2025-11-02 18:12:21', '20251102181221199879', 'meishixinxi', 2, '海鲜面', 'file/meishixinxiCaipintupian2.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已支付', NULL, NULL, NULL, '', NULL, 'yonghu', 11, '商家账号2', '面食');
INSERT INTO `orders` VALUES (8, '2025-11-02 18:38:22', '20251102183822242515', 'meishixinxi', 1, '生腌虾', 'file/meishixinxiCaipintupian1.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已支付', NULL, NULL, NULL, '', NULL, 'yonghu', 11, '商家账号1', '潮汕特色菜');
INSERT INTO `orders` VALUES (9, '2025-11-02 18:38:44', '20251102183844249292', 'meishixinxi', 1, '生腌虾', 'file/meishixinxiCaipintupian1.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已支付', NULL, NULL, NULL, '', NULL, 'yonghu', 11, '商家账号1', '潮汕特色菜');
INSERT INTO `orders` VALUES (10, '2025-11-03 15:52:13', '20251103155213184209', 'meishixinxi', 2, '海鲜面', 'file/meishixinxiCaipintupian2.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已支付', NULL, NULL, NULL, '', NULL, 'yonghu', 11, '商家账号2', '面食');
INSERT INTO `orders` VALUES (11, '2025-11-03 17:07:15', '20251103170715904821', 'meishixinxi', 1, '生腌虾', 'file/meishixinxiCaipintupian1.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已支付', NULL, NULL, NULL, '', NULL, 'yonghu', 11, '商家账号1', '潮汕特色菜');

-- ----------------------------
-- Table structure for shangjia
-- ----------------------------
DROP TABLE IF EXISTS `shangjia`;
CREATE TABLE `shangjia`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `shangjiazhanghao` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商家账号',
  `mima` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `shangjiamingcheng` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商家名称',
  `touxiang` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '头像',
  `fuzeren` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '负责人',
  `dianhua` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电话',
  `shangjiadizhi` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商家地址',
  `sfsh` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '是否审核',
  `shhf` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '回复内容',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `shangjiazhanghao`(`shangjiazhanghao` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 120 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '商家' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of shangjia
-- ----------------------------
INSERT INTO `shangjia` VALUES (111, '2025-03-25 17:01:10', '商家账号1', '123456', '商家名称1', 'file/shangjiaTouxiang1.jpg', '负责人1', '19819881111', '商家地址1', '是', '');
INSERT INTO `shangjia` VALUES (112, '2025-03-25 17:01:10', '商家账号2', '123456', '商家名称2', 'file/shangjiaTouxiang2.jpg', '负责人2', '19819881112', '商家地址2', '是', '');
INSERT INTO `shangjia` VALUES (113, '2025-03-25 17:01:10', '商家账号3', '123456', '商家名称3', 'file/shangjiaTouxiang3.jpg', '负责人3', '19819881113', '商家地址3', '是', '');
INSERT INTO `shangjia` VALUES (114, '2025-03-25 17:01:10', '商家账号4', '123456', '商家名称4', 'file/shangjiaTouxiang4.jpg', '负责人4', '19819881114', '商家地址4', '是', '');
INSERT INTO `shangjia` VALUES (115, '2025-03-25 17:01:10', '商家账号5', '123456', '商家名称5', 'file/shangjiaTouxiang5.jpg', '负责人5', '19819881115', '商家地址5', '是', '');
INSERT INTO `shangjia` VALUES (116, '2025-03-25 17:01:10', '商家账号6', '123456', '商家名称6', 'file/shangjiaTouxiang6.jpg', '负责人6', '19819881116', '商家地址6', '是', '');
INSERT INTO `shangjia` VALUES (117, '2025-03-25 17:01:10', '商家账号7', '123456', '商家名称7', 'file/shangjiaTouxiang7.jpg', '负责人7', '19819881117', '商家地址7', '是', '');
INSERT INTO `shangjia` VALUES (118, '2025-03-25 17:01:10', '商家账号8', '123456', '商家名称8', 'file/shangjiaTouxiang8.jpg', '负责人8', '19819881118', '商家地址8', '是', '');
INSERT INTO `shangjia` VALUES (119, '2025-03-25 17:01:10', '商家账号9', '123456', '商家名称9', 'file/shangjiaTouxiang9.jpg', '负责人9', '19819881119', '商家地址9', '是', '');
INSERT INTO `shangjia` VALUES (120, '2025-03-25 17:01:10', '商家账号10', '123456', '商家名称10', 'file/shangjiaTouxiang10.jpg', '负责人10', '198198811110', '商家地址10', '是', '');

-- ----------------------------
-- Table structure for storeup
-- ----------------------------
DROP TABLE IF EXISTS `storeup`;
CREATE TABLE `storeup`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `refid` bigint NULL DEFAULT NULL COMMENT 'refid',
  `tablename` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '表名',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '名称',
  `picture` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '图片',
  `type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '1' COMMENT '类型(1:收藏,21:赞,22:踩,31:竞拍参与,41:关注)',
  `inteltype` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '推荐类型',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `userid` bigint NOT NULL COMMENT '用户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '我的收藏' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of storeup
-- ----------------------------
INSERT INTO `storeup` VALUES (1, '2025-10-25 17:05:46', 10, 'meishixinxi', '菜品名称10', 'file/meishixinxiCaipintupian10.jpg', '1', '菜品类型10', NULL, 11);
INSERT INTO `storeup` VALUES (2, '2025-10-25 17:06:27', 2, 'cantingxinxi', '餐桌名称2', 'file/cantingxinxiFengmian2.jpg', '1', NULL, NULL, 11);

-- ----------------------------
-- Table structure for system_notice
-- ----------------------------
DROP TABLE IF EXISTS `system_notice`;
CREATE TABLE `system_notice`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '公告内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统公告' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_notice
-- ----------------------------
INSERT INTO `system_notice` VALUES (1, '2025-11-25 17:01:10', '<p>公告</p>');

-- ----------------------------
-- Table structure for token
-- ----------------------------
DROP TABLE IF EXISTS `token`;
CREATE TABLE `token`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` bigint NOT NULL COMMENT '用户id',
  `username` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户名',
  `tablename` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '表名',
  `role` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '角色',
  `token` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '密码',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `expiratedtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '过期时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = 'token表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of token
-- ----------------------------
INSERT INTO `token` VALUES (1, 11, '账号1', 'yonghu', '用户', 'e8fau3lu3zgbtls78iwuvk76j32rg1h9', '2025-03-25 17:05:40', '2025-11-07 23:37:48');
INSERT INTO `token` VALUES (2, 1, 'admin', 'users', '管理员', 'fdzkicrqc7n507lmlg1nvcvuzqjjfpx7', '2025-03-25 17:06:55', '2025-11-07 23:36:43');
INSERT INTO `token` VALUES (3, 111, '商家账号1', 'shangjia', '商家', '51ny60yai98q0yrllfbbsgn0fdncs9l5', '2025-03-25 17:12:56', '2025-03-25 18:14:27');
INSERT INTO `token` VALUES (4, 120, '商家账号10', 'shangjia', '商家', 's38knhieonf4eirhdvb1qdsv9jjcj9ep', '2025-03-25 17:15:53', '2025-03-25 18:15:54');
INSERT INTO `token` VALUES (5, 1742894247741, '11', 'yonghu', '用户', '6rqn1u4qxzox90p69kiuu7yormas1b4y', '2025-03-25 17:17:36', '2025-03-25 18:17:36');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `username` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `role` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '管理员' COMMENT '角色',
  `tupian` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '图片',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '管理员' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, '2025-10-25 17:01:10', 'admin', 'admin', '管理员', 'file/1742893901879.jpg');

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
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '我的预约' ROW_FORMAT = DYNAMIC;

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

-- ----------------------------
-- Table structure for yonghu
-- ----------------------------
DROP TABLE IF EXISTS `yonghu`;
CREATE TABLE `yonghu`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `zhanghao` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '账号',
  `touxiang` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '头像',
  `mima` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `mingcheng` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `xingbie` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '性别',
  `shoujihaoma` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号码',
  `chepaihao` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '车牌号',
  `pquestion` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密保问题',
  `panswer` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密保答案',
  `max_password_wrong` int NOT NULL DEFAULT 5 COMMENT '最大密码输错次数',
  `is_locked` int NOT NULL DEFAULT 0 COMMENT '用户锁定状态',
  `money` double NULL DEFAULT 0 COMMENT '余额',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `zhanghao`(`zhanghao` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1742894247741 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of yonghu
-- ----------------------------
INSERT INTO `yonghu` VALUES (11, '2025-03-25 17:01:09', '账号1', 'file/yonghuTouxiang1.jpg', '123456', '名称1', '男', '19819881111', '车牌号1', '密保问题1', '密保答案1', 1, 0, 311.9);
INSERT INTO `yonghu` VALUES (12, '2025-03-25 17:01:09', '账号2', 'file/yonghuTouxiang2.jpg', '123456', '名称2', '男', '19819881112', '车牌号2', '密保问题2', '密保答案2', 2, 0, 200);
INSERT INTO `yonghu` VALUES (13, '2025-03-25 17:01:09', '账号3', 'file/yonghuTouxiang3.jpg', '123456', '名称3', '男', '19819881113', '车牌号3', '密保问题3', '密保答案3', 3, 0, 200);
INSERT INTO `yonghu` VALUES (14, '2025-03-25 17:01:09', '账号4', 'file/yonghuTouxiang4.jpg', '123456', '名称4', '男', '19819881114', '车牌号4', '密保问题4', '密保答案4', 4, 0, 200);
INSERT INTO `yonghu` VALUES (15, '2025-03-25 17:01:09', '账号5', 'file/yonghuTouxiang5.jpg', '123456', '名称5', '男', '19819881115', '车牌号5', '密保问题5', '密保答案5', 5, 0, 200);
INSERT INTO `yonghu` VALUES (16, '2025-03-25 17:01:09', '账号6', 'file/yonghuTouxiang6.jpg', '123456', '名称6', '男', '19819881116', '车牌号6', '密保问题6', '密保答案6', 6, 0, 200);
INSERT INTO `yonghu` VALUES (17, '2025-03-25 17:01:09', '账号7', 'file/yonghuTouxiang7.jpg', '123456', '名称7', '男', '19819881117', '车牌号7', '密保问题7', '密保答案7', 7, 0, 200);
INSERT INTO `yonghu` VALUES (18, '2025-03-25 17:01:09', '账号8', 'file/yonghuTouxiang8.jpg', '123456', '名称8', '男', '19819881118', '车牌号8', '密保问题8', '密保答案8', 8, 0, 200);
INSERT INTO `yonghu` VALUES (19, '2025-03-25 17:01:09', '账号9', 'file/yonghuTouxiang9.jpg', '123456', '名称9', '男', '19819881119', '车牌号9', '密保问题9', '密保答案9', 9, 0, 200);
INSERT INTO `yonghu` VALUES (20, '2025-03-25 17:01:09', '账号10', 'file/yonghuTouxiang10.jpg', '123456', '名称10', '男', '198198811110', '车牌号10', '密保问题10', '密保答案10', 10, 0, 200);
INSERT INTO `yonghu` VALUES (1742894247741, '2025-03-25 17:17:27', '11', NULL, '11', '11', '男', '16666666666', '闽A88888', '2', '3', 5, 0, 0);

SET FOREIGN_KEY_CHECKS = 1;
