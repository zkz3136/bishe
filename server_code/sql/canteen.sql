/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80043
 Source Host           : localhost:3306
 Source Schema         : canteen

 Target Server Type    : MySQL
 Target Server Version : 80043
 File Encoding         : 65001

 Date: 20/01/2026 01:04:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cart
-- ----------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `goodid` bigint NOT NULL COMMENT '商品id',
  `goodname` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商品名称',
  `picture` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图片',
  `buynumber` int NULL DEFAULT NULL COMMENT '购买数量',
  `price` double NULL DEFAULT NULL COMMENT '单价',
  `discountprice` double NULL DEFAULT NULL COMMENT '折扣价',
  `userid` bigint NOT NULL COMMENT '用户id',
  `goodtype` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '菜品类型',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `goodid`(`goodid` ASC, `price` ASC) USING BTREE,
  CONSTRAINT `cart_ibfk_1` FOREIGN KEY (`goodid`, `price`) REFERENCES `dish_info` (`id`, `price`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '购物车' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cart
-- ----------------------------
INSERT INTO `cart` VALUES (8, '2026-01-20 00:54:45', 3, '梅菜扣肉', 'file/meishixinxiCaipintupian3.jpg', 1, 99.9, 99.9, 11, '粤菜');

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
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '轮播图' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config
-- ----------------------------
INSERT INTO `config` VALUES (1, '2025-10-25 17:01:11', 'swiper1', 'file/swiperPicture1.jpg', NULL);
INSERT INTO `config` VALUES (2, '2025-10-25 17:01:11', 'swiper2', 'file/swiperPicture2.jpg', NULL);
INSERT INTO `config` VALUES (3, '2025-10-25 17:01:11', 'swiper3', 'file/1742893928445.jpg', NULL);
INSERT INTO `config` VALUES (4, '2026-01-13 13:22:14', 'swiper', 'file/1768281732509.jpg', '');

-- ----------------------------
-- Table structure for dish_info
-- ----------------------------
DROP TABLE IF EXISTS `dish_info`;
CREATE TABLE `dish_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `dish_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜品名称',
  `dish_image` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜品图片',
  `dish_category` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜品类型',
  `dish_description` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '菜品详情',
  `flavor` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '口味',
  `rating` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '推荐指数',
  `click_time` datetime NULL DEFAULT NULL COMMENT '最近点击时间',
  `onelimittimes` int NULL DEFAULT -1 COMMENT '单限',
  `alllimittimes` int NULL DEFAULT -1 COMMENT '库存',
  `price` double NULL DEFAULT 0 COMMENT '价格',
  `discountprice` double NULL DEFAULT NULL COMMENT '折扣价',
  `storeup_number` int NULL DEFAULT 0 COMMENT '收藏数',
  `discuss_number` int NULL DEFAULT 0 COMMENT '评论数',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `meishixinxi_price`(`id` ASC, `price` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '美食信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dish_info
-- ----------------------------
INSERT INTO `dish_info` VALUES (1, '2025-10-25 17:01:10', '生腌虾', 'file/meishixinxiCaipintupian1.jpg,file/meishixinxiCaipintupian2.jpg,file/meishixinxiCaipintupian3.jpg', '潮汕特色', '菜品详情1', '微辣', '★★', '2026-01-20 00:54:55', 1, 71, 99.9, 80, 1, 1);
INSERT INTO `dish_info` VALUES (2, '2025-10-25 17:01:10', '海鲜面', 'file/meishixinxiCaipintupian2.jpg,file/meishixinxiCaipintupian3.jpg,file/meishixinxiCaipintupian4.jpg', '面食', '菜品详情2', '微辣', '★★', '2025-12-03 17:27:13', 2, 95, 99.9, 99.9, 2, 2);
INSERT INTO `dish_info` VALUES (3, '2025-10-25 17:01:10', '梅菜扣肉', 'file/meishixinxiCaipintupian3.jpg,file/meishixinxiCaipintupian4.jpg,file/meishixinxiCaipintupian5.jpg', '粤菜', '菜品详情3', '微辣', '★★', '2025-11-09 21:46:30', 3, 95, 99.9, 99.9, 4, 3);
INSERT INTO `dish_info` VALUES (4, '2025-10-25 17:01:10', '山药卷', 'file/meishixinxiCaipintupian4.jpg,file/meishixinxiCaipintupian5.jpg,file/meishixinxiCaipintupian6.jpg', '小吃', '菜品详情4', '微辣', '★★', '2026-01-20 00:54:55', 4, 94, 99.9, 99.9, 5, 4);
INSERT INTO `dish_info` VALUES (5, '2025-10-25 17:01:10', '卤味拼盘', 'file/meishixinxiCaipintupian5.jpg,file/meishixinxiCaipintupian6.jpg,file/meishixinxiCaipintupian7.jpg', '卤味', '菜品详情5', '微辣', '★★', '2025-11-03 17:01:10', 5, 99, 99.9, 99.9, 5, 5);
INSERT INTO `dish_info` VALUES (6, '2025-10-25 17:01:10', '日式鳗鱼', 'file/meishixinxiCaipintupian6.jpg,file/meishixinxiCaipintupian7.jpg,file/meishixinxiCaipintupian8.jpg', '日式料理', '菜品详情6', '微辣', '★★', '2025-11-09 16:02:51', 6, 99, 99.9, 99.9, 6, 6);
INSERT INTO `dish_info` VALUES (7, '2025-10-25 17:01:10', '煎牛排', 'file/meishixinxiCaipintupian7.jpg,file/meishixinxiCaipintupian8.jpg,file/meishixinxiCaipintupian9.jpg', '西餐 ', '菜品详情7', '微辣', '★★', '2025-11-03 17:01:10', 7, 99, 99.9, 99.9, 7, 7);
INSERT INTO `dish_info` VALUES (8, '2025-10-25 17:01:10', '牛肉丸', 'file/meishixinxiCaipintupian8.jpg,file/meishixinxiCaipintupian9.jpg,file/meishixinxiCaipintupian10.jpg', '丸子', '菜品详情8', '微辣', '★★', '2025-11-03 17:01:10', 8, 99, 99.9, 99.9, 8, 8);
INSERT INTO `dish_info` VALUES (9, '2025-10-25 17:01:10', '鱼卷', 'file/meishixinxiCaipintupian9.jpg,file/meishixinxiCaipintupian10.jpg,file/meishixinxiCaipintupian11.jpg', '闽菜', '菜品详情9', '微辣', '★★', '2025-11-03 17:01:10', 9, 99, 99.9, 99.9, 9, 9);
INSERT INTO `dish_info` VALUES (10, '2025-10-25 17:01:10', '香辣小河虾', 'file/meishixinxiCaipintupian10.jpg,file/meishixinxiCaipintupian11.jpg,file/meishixinxiCaipintupian12.jpg', '小炒', '菜品详情10', '中辣', '★★', '2025-11-02 18:40:16', 10, 98, 99.9, 99.9, 11, 10);
INSERT INTO `dish_info` VALUES (11, '2025-10-25 17:01:10', '藤椒牛杂', 'file/meishixinxiCaipintupian11.jpg', '川菜', '菜品详情11', '超辣', '★★★★', '2025-11-02 17:48:10', 20, 20, 20, 20, 0, 0);
INSERT INTO `dish_info` VALUES (12, '2025-10-25 17:01:10', '排骨冬瓜汤', 'file/meishixinxiCaipintupian12.jpg', '汤类', '菜品详情12', '清淡', '★★★★', '2026-01-20 00:53:52', 1, 44, 66, 66, 0, 0);

-- ----------------------------
-- Table structure for dish_review
-- ----------------------------
DROP TABLE IF EXISTS `dish_review`;
CREATE TABLE `dish_review`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `refid` bigint NOT NULL COMMENT '关联表id',
  `userid` bigint NOT NULL COMMENT '用户id',
  `avatarurl` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '头像',
  `nickname` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '评论内容',
  `score` int NULL DEFAULT 0 COMMENT '评分',
  `reply` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '回复内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '美食信息评论' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dish_review
-- ----------------------------
INSERT INTO `dish_review` VALUES (1, '2026-01-19 00:42:56', 4, 11, 'file/userTouxiang1.jpg', '账号1', '好吃', 5, NULL);

-- ----------------------------
-- Table structure for faq
-- ----------------------------
DROP TABLE IF EXISTS `faq`;
CREATE TABLE `faq`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `question` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '问题',
  `keywords` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '关键词',
  `answer` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '答案',
  `category` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '分类',
  `enabled` int NOT NULL DEFAULT 1 COMMENT '是否启用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '智能客服知识库' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of faq
-- ----------------------------
INSERT INTO `faq` VALUES (1, '2026-01-18 23:23:26', '如何预约餐桌？', '预约,餐桌,定金', '进入“餐厅信息/餐厅预约”，选择餐桌与预约时间提交预约，并按提示支付定金，支付成功后预约生效。', '预约', 1);
INSERT INTO `faq` VALUES (2, '2026-01-18 23:23:26', '停车怎么计费？', '停车,计费,收费,价格', '车辆离场时系统会按计费规则自动计算停车费用，并在完成结算后更新车位状态。', '停车', 1);
INSERT INTO `faq` VALUES (3, '2026-01-18 23:23:26', '如何查看订单？', '订单,查询,记录', '进入个人中心或订单管理页面，可查看订单列表与订单状态，并支持取消、退款等操作。', '订单', 1);

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `menujson` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '菜单' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES (1, '2025-10-25 17:01:11', '[{\"backMenu\": [{\"menu\": \"车位信息管理\", \"child\": [{\"menu\": \"车位信息\", \"buttons\": [\"新增\", \"查看\", \"修改\", \"删除\"], \"menuJump\": \"列表\", \"classname\": \"cheweixinxi\", \"tableName\": \"cheweixinxi\", \"allButtons\": [\"新增\", \"查看\", \"修改\", \"删除\"], \"appFrontIcon\": \"cuIcon-goods\"}], \"unicode\": \"&#xeebb;\", \"fontClass\": \"icon-common40\"}, {\"menu\": \"餐厅信息管理\", \"child\": [{\"menu\": \"餐厅信息\", \"buttons\": [\"新增\", \"查看\", \"修改\", \"删除\", \"查看评论\"], \"menuJump\": \"列表\", \"classname\": \"cantingxinxi\", \"tableName\": \"cantingxinxi\", \"allButtons\": [\"新增\", \"查看\", \"修改\", \"删除\", \"查看评论\", \"预约\"], \"appFrontIcon\": \"cuIcon-present\"}, {\"menu\": \"餐厅预约\", \"buttons\": [\"查看\", \"删除\", \"审核\"], \"menuJump\": \"列表\", \"classname\": \"cantingyuyue\", \"tableName\": \"cantingyuyue\", \"allButtons\": [\"新增\", \"查看\", \"修改\", \"删除\", \"审核\"], \"appFrontIcon\": \"cuIcon-time\"}], \"unicode\": \"&#xee05;\", \"fontClass\": \"icon-common23\"}, {\"menu\": \"活动公告\", \"child\": [{\"menu\": \"餐厅活动\", \"buttons\": [\"新增\", \"查看\", \"修改\", \"删除\"], \"menuJump\": \"列表\", \"classname\": \"dianpuhuodong\", \"tableName\": \"dianpuhuodong\", \"allButtons\": [\"新增\", \"查看\", \"修改\", \"删除\"], \"appFrontIcon\": \"cuIcon-medal\"}, {\"menu\": \"系统公告\", \"buttons\": [\"新增\", \"查看\", \"修改\", \"删除\"], \"menuJump\": \"列表\", \"classname\": \"systemNotice\", \"tableName\": \"system_notice\", \"allButtons\": [\"新增\", \"查看\", \"修改\", \"删除\"], \"appFrontIcon\": \"cuIcon-flashlightopen\"}], \"unicode\": \"&#xee30;\", \"fontClass\": \"icon-common30\"}, {\"menu\": \"菜品信息管理\", \"child\": [{\"menu\": \"菜品类型\", \"buttons\": [\"新增\", \"查看\", \"修改\", \"删除\"], \"menuJump\": \"列表\", \"classname\": \"caipinleixing\", \"tableName\": \"caipinleixing\", \"allButtons\": [\"新增\", \"查看\", \"修改\", \"删除\"], \"appFrontIcon\": \"cuIcon-newshot\"}, {\"menu\": \"美食信息\", \"buttons\": [\"新增\", \"查看\", \"修改\", \"删除\", \"菜品类型\", \"查看评论\", \"首页统计\"], \"menuJump\": \"列表\", \"classname\": \"meishixinxi\", \"tableName\": \"meishixinxi\", \"allButtons\": [\"新增\", \"查看\", \"修改\", \"删除\", \"菜品类型\", \"查看评论\", \"首页统计\"], \"appFrontIcon\": \"cuIcon-taxi\"}], \"unicode\": \"&#xeeb2;\", \"fontClass\": \"icon-common38\"}, {\"menu\": \"订单管理\", \"child\": [{\"menu\": \"订单\", \"buttons\": [\"查看\", \"删除\", \"月销额\", \"年销额\", \"品销量\", \"导出\", \"首页总数\", \"首页统计\"], \"tableName\": \"orders\", \"allButtons\": [\"新增\", \"查看\", \"修改\", \"删除\", \"月销额\", \"年销额\", \"品销量\", \"导出\", \"首页总数\", \"首页统计\"], \"appFrontIcon\": \"cuIcon-skin\", \"menuJump\": \"\"}], \"unicode\": \"&#xee6a;\", \"fontClass\": \"icon-common33\"}, {\"menu\": \"用户管理\", \"child\": [{\"menu\": \"管理员\", \"buttons\": [\"新增\", \"查看\", \"修改\", \"删除\"], \"menuJump\": \"列表\", \"classname\": \"users\", \"tableName\": \"users\", \"allButtons\": [\"新增\", \"查看\", \"修改\", \"删除\"], \"appFrontIcon\": \"cuIcon-newshot\"}, {\"menu\": \"用户\", \"buttons\": [\"新增\", \"查看\", \"修改\", \"删除\"], \"menuJump\": \"列表\", \"classname\": \"user\", \"tableName\": \"user\", \"allButtons\": [\"新增\", \"查看\", \"修改\", \"删除\"], \"appFrontIcon\": \"cuIcon-paint\"}, {\"menu\": \"员工\", \"buttons\": [\"新增\", \"查看\", \"修改\", \"删除\"], \"menuJump\": \"列表\", \"classname\": \"yuangong\", \"tableName\": \"yuangong\", \"allButtons\": [\"新增\", \"查看\", \"修改\", \"删除\"], \"appFrontIcon\": \"cuIcon-service\"}], \"unicode\": \"&#xef98;\", \"fontClass\": \"icon-user2\"}, {\"menu\": \"轮播图管理\", \"child\": [{\"menu\": \"轮播图\", \"buttons\": [\"新增\", \"查看\", \"修改\", \"删除\"], \"menuJump\": \"列表\", \"classname\": \"config\", \"tableName\": \"config\", \"allButtons\": [\"新增\", \"查看\", \"修改\", \"删除\"], \"appFrontIcon\": \"cuIcon-addressbook\"}], \"unicode\": \"&#xedf4;\", \"fontClass\": \"icon-common12\"}], \"pathName\": \"users\", \"roleName\": \"管理员\", \"frontMenu\": [{\"menu\": \"信息管理\", \"child\": [{\"menu\": \"美食信息\", \"buttons\": [\"查看\", \"查看评论\"], \"menuJump\": \"列表\", \"classname\": \"meishixinxi\", \"tableName\": \"meishixinxi\", \"appFrontIcon\": \"cuIcon-send\"}]}, {\"menu\": \"活动管理\", \"child\": [{\"menu\": \"餐厅活动\", \"buttons\": [\"查看\"], \"menuJump\": \"列表\", \"classname\": \"dianpuhuodong\", \"tableName\": \"dianpuhuodong\", \"appFrontIcon\": \"cuIcon-explore\"}]}, {\"menu\": \"购物车管理\", \"child\": [{\"menu\": \"购物车\", \"buttons\": [\"查看\"], \"menuJump\": \"列表\", \"classname\": \"cart\", \"tableName\": \"cart\", \"appFrontIcon\": \"cuIcon-goods\"}]}, {\"menu\": \"车位信息管理\", \"child\": [{\"menu\": \"车位信息\", \"buttons\": [\"查看\"], \"menuJump\": \"列表\", \"classname\": \"cheweixinxi\", \"tableName\": \"cheweixinxi\", \"appFrontIcon\": \"cuIcon-circle\"}]}, {\"menu\": \"餐厅信息管理\", \"child\": [{\"menu\": \"餐厅信息\", \"buttons\": [\"查看\", \"查看评论\", \"预约\"], \"menuJump\": \"列表\", \"classname\": \"cantingxinxi\", \"tableName\": \"cantingxinxi\", \"appFrontIcon\": \"cuIcon-discover\"}]}], \"tableName\": \"users\", \"hasBackLogin\": \"是\", \"hasFrontLogin\": \"否\", \"hasBackRegister\": \"否\", \"hasFrontRegister\": \"否\"}, {\"backMenu\": [{\"menu\": \"餐厅信息管理\", \"child\": [{\"menu\": \"餐厅预约\", \"buttons\": [\"查看\"], \"menuJump\": \"列表\", \"classname\": \"cantingyuyue\", \"tableName\": \"cantingyuyue\", \"allButtons\": [\"新增\", \"查看\", \"修改\", \"删除\", \"审核\"], \"appFrontIcon\": \"cuIcon-time\"}], \"unicode\": \"&#xee05;\", \"fontClass\": \"icon-common23\"}, {\"menu\": \"订单管理\", \"child\": [{\"menu\": \"已支付订单\", \"buttons\": [\"查看\"], \"menuJump\": \"\", \"tableName\": \"orders\", \"allButtons\": [\"新增\", \"查看\", \"修改\", \"删除\", \"月销额\", \"年销额\", \"品销量\", \"导出\", \"首页总数\", \"首页统计\", \"发货\", \"物流\", \"核销\"], \"appFrontIcon\": \"cuIcon-skin\"}], \"unicode\": \"&#xee6a;\", \"fontClass\": \"icon-common33\"}, {\"menu\": \"我的收藏管理\", \"child\": [{\"menu\": \"我的收藏\", \"buttons\": [\"查看\"], \"menuJump\": \"1\", \"classname\": \"storeup\", \"tableName\": \"storeup\", \"allButtons\": [\"新增\", \"查看\", \"修改\", \"删除\"], \"appFrontIcon\": \"cuIcon-cardboard\"}], \"unicode\": \"&#xeda4;\", \"fontClass\": \"icon-common2\"}], \"pathName\": \"user\", \"roleName\": \"用户\", \"frontMenu\": [{\"menu\": \"信息管理\", \"child\": [{\"menu\": \"美食信息\", \"buttons\": [\"查看\", \"查看评论\"], \"menuJump\": \"列表\", \"classname\": \"meishixinxi\", \"tableName\": \"meishixinxi\", \"appFrontIcon\": \"cuIcon-send\"}]}, {\"menu\": \"活动管理\", \"child\": [{\"menu\": \"餐厅活动\", \"buttons\": [\"查看\"], \"menuJump\": \"列表\", \"classname\": \"dianpuhuodong\", \"tableName\": \"dianpuhuodong\", \"appFrontIcon\": \"cuIcon-explore\"}]}, {\"menu\": \"购物车管理\", \"child\": [{\"menu\": \"购物车\", \"buttons\": [\"查看\"], \"menuJump\": \"列表\", \"classname\": \"cart\", \"tableName\": \"cart\", \"appFrontIcon\": \"cuIcon-goods\"}]}, {\"menu\": \"车位信息管理\", \"child\": [{\"menu\": \"车位信息\", \"buttons\": [\"查看\", \"预约\"], \"menuJump\": \"列表\", \"classname\": \"cheweixinxi\", \"tableName\": \"cheweixinxi\", \"appFrontIcon\": \"cuIcon-circle\"}]}, {\"menu\": \"餐厅信息管理\", \"child\": [{\"menu\": \"餐厅信息\", \"buttons\": [\"查看\", \"查看评论\", \"预约\"], \"menuJump\": \"列表\", \"classname\": \"cantingxinxi\", \"tableName\": \"cantingxinxi\", \"appFrontIcon\": \"cuIcon-discover\"}]}], \"tableName\": \"user\", \"hasBackLogin\": \"否\", \"hasFrontLogin\": \"是\", \"hasBackRegister\": \"否\", \"hasFrontRegister\": \"是\"}, {\"backMenu\": [{\"menu\": \"订单管理\", \"child\": [{\"menu\": \"订单\", \"buttons\": [\"查看\"], \"tableName\": \"orders\", \"allButtons\": [\"新增\", \"查看\", \"修改\"], \"appFrontIcon\": \"cuIcon-skin\", \"menuJump\": \"\"}], \"unicode\": \"&#xee6a;\", \"fontClass\": \"icon-common33\"}, {\"menu\": \"预约管理\", \"child\": [{\"menu\": \"餐厅预约\", \"buttons\": [\"查看\", \"审核\"], \"menuJump\": \"列表\", \"classname\": \"cantingyuyue\", \"tableName\": \"cantingyuyue\", \"allButtons\": [\"新增\", \"查看\", \"修改\", \"删除\", \"审核\"], \"appFrontIcon\": \"cuIcon-time\"}], \"unicode\": \"&#xeebb;\", \"fontClass\": \"icon-common40\"}, {\"menu\": \"信息查看\", \"child\": [{\"menu\": \"餐厅信息\", \"buttons\": [\"查看\"], \"menuJump\": \"列表\", \"classname\": \"cantingxinxi\", \"tableName\": \"cantingxinxi\", \"allButtons\": [\"新增\", \"查看\", \"修改\", \"删除\", \"查看评论\"], \"appFrontIcon\": \"cuIcon-present\"}, {\"menu\": \"美食信息\", \"buttons\": [\"查看\"], \"menuJump\": \"列表\", \"classname\": \"meishixinxi\", \"tableName\": \"meishixinxi\", \"allButtons\": [\"新增\", \"查看\", \"修改\", \"删除\", \"查看评论\"], \"appFrontIcon\": \"cuIcon-goods\"}], \"unicode\": \"&#xee14;\", \"fontClass\": \"icon-common14\"}, {\"menu\": \"活动公告\", \"child\": [{\"menu\": \"店铺活动\", \"buttons\": [\"查看\"], \"menuJump\": \"列表\", \"classname\": \"dianpuhuodong\", \"tableName\": \"dianpuhuodong\", \"allButtons\": [\"新增\", \"查看\", \"修改\", \"删除\"], \"appFrontIcon\": \"cuIcon-medal\"}, {\"menu\": \"系统公告\", \"buttons\": [\"查看\"], \"menuJump\": \"列表\", \"classname\": \"systemNotice\", \"tableName\": \"systemNotice\", \"allButtons\": [\"新增\", \"查看\", \"修改\", \"删除\"], \"appFrontIcon\": \"cuIcon-notice\"}], \"unicode\": \"&#xee32;\", \"fontClass\": \"icon-common32\"}], \"pathName\": \"yuangong\", \"roleName\": \"员工\", \"tableName\": \"yuangong\", \"hasBackLogin\": \"是\"}]');

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `messages` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '消息JSON',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '消息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of message
-- ----------------------------

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
  `discounttotal` double NULL DEFAULT NULL COMMENT '折扣总价',
  `type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '支付类型',
  `status` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '订单状态',
  `tel` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电话',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `role` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户角色',
  `userid` bigint NOT NULL COMMENT '用户id',
  `goodtype` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商品类型',
  `seat_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '餐桌名称',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `orderid`(`orderid` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '订单' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (5, '2025-11-02 17:48:53', '20251102174853680425', 'dish_info', 2, '海鲜面', 'file/meishixinxiCaipintupian2.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '未支付', NULL, '', 'user', 11, '面食', NULL);
INSERT INTO `orders` VALUES (6, '2025-11-02 17:49:12', '20251102174912127554', 'dish_info', 1, '生腌虾', 'file/meishixinxiCaipintupian1.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已支付', NULL, '', 'user', 11, '潮汕特色', NULL);
INSERT INTO `orders` VALUES (7, '2025-11-02 18:12:21', '20251102181221199879', 'dish_info', 2, '海鲜面', 'file/meishixinxiCaipintupian2.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已支付', NULL, '', 'user', 11, '面食', NULL);
INSERT INTO `orders` VALUES (8, '2025-11-02 18:38:22', '20251102183822242515', 'dish_info', 1, '生腌虾', 'file/meishixinxiCaipintupian1.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已支付', NULL, '', 'user', 11, '潮汕特色', NULL);
INSERT INTO `orders` VALUES (9, '2025-11-02 18:38:44', '20251102183844249292', 'dish_info', 1, '生腌虾', 'file/meishixinxiCaipintupian1.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已支付', NULL, '', 'user', 11, '潮汕特色', NULL);
INSERT INTO `orders` VALUES (10, '2025-11-03 15:52:13', '20251103155213184209', 'dish_info', 2, '海鲜面', 'file/meishixinxiCaipintupian2.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已支付', NULL, '', 'user', 11, '面食', NULL);
INSERT INTO `orders` VALUES (11, '2025-11-03 17:07:15', '20251103170715904821', 'dish_info', 1, '生腌虾', 'file/meishixinxiCaipintupian1.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已支付', NULL, '', 'user', 11, '潮汕特色', NULL);
INSERT INTO `orders` VALUES (12, '2025-11-09 13:39:18', '20251109133918228235', 'dish_info', 3, '梅菜扣肉', 'file/meishixinxiCaipintupian3.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已支付', NULL, '', 'user', 11, '粤菜', NULL);
INSERT INTO `orders` VALUES (13, '2025-11-09 13:39:34', '20251109133934249274', 'dish_info', 3, '梅菜扣肉', 'file/meishixinxiCaipintupian3.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已支付', NULL, '', 'user', 11, '粤菜', NULL);
INSERT INTO `orders` VALUES (14, '2025-11-09 13:39:48', '20251109133948871652', 'dish_info', 3, '梅菜扣肉', 'file/meishixinxiCaipintupian3.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已支付', NULL, '', 'user', 11, '粤菜', NULL);
INSERT INTO `orders` VALUES (15, '2025-11-09 13:40:04', '20251109134004229720', 'dish_info', 1, '生腌虾', 'file/meishixinxiCaipintupian1.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已支付', NULL, '', 'user', 11, '潮汕特色', NULL);
INSERT INTO `orders` VALUES (16, '2025-11-09 15:21:30', '20251109152130885050', 'dish_info', 1, '生腌虾', 'file/meishixinxiCaipintupian1.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已支付', NULL, '', 'user', 11, '潮汕特色', NULL);
INSERT INTO `orders` VALUES (17, '2025-11-09 15:21:30', '20251109152130903273', 'dish_info', 3, '梅菜扣肉', 'file/meishixinxiCaipintupian3.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已完成', NULL, '', 'user', 11, '粤菜', NULL);
INSERT INTO `orders` VALUES (18, '2025-11-09 16:11:58', '20251109161158014500', 'dish_info', 1, '生腌虾', 'file/meishixinxiCaipintupian1.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已完成', NULL, '', 'user', 11, '潮汕特色', NULL);
INSERT INTO `orders` VALUES (19, '2025-11-09 18:58:16', '20251109185816593607', 'dish_info', 1, '生腌虾', 'file/meishixinxiCaipintupian1.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已支付', NULL, '', 'user', 11, '潮汕特色', NULL);
INSERT INTO `orders` VALUES (20, '2025-11-09 21:46:36', '20251109214636101837', 'dish_info', 2, '海鲜虾', 'file/meishixinxiCaipintupian2.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已支付', NULL, '', 'user', 11, '面食', NULL);
INSERT INTO `orders` VALUES (21, '2025-11-09 22:04:03', '20251109220403230322', 'dish_info', 2, '海鲜面', 'file/meishixinxiCaipintupian2.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已完成', NULL, '', 'user', 11, '面食', NULL);
INSERT INTO `orders` VALUES (22, '2025-11-09 22:12:57', '20251109221257515377', 'dish_info', 1, '生腌虾', 'file/meishixinxiCaipintupian1.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已支付', NULL, '', 'user', 11, '潮汕特色', NULL);
INSERT INTO `orders` VALUES (23, '2025-11-09 22:16:29', '20251109221629636614', 'dish_info', 1, '生腌虾', 'file/meishixinxiCaipintupian1.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '未支付', NULL, '', 'user', 11, '潮汕特色', '餐桌名称1');
INSERT INTO `orders` VALUES (24, '2025-11-09 22:16:49', '20251109221649508555', 'dish_info', 1, '生腌虾', 'file/meishixinxiCaipintupian1.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已完成', NULL, '', 'user', 11, '潮汕特色', '餐桌名称1');
INSERT INTO `orders` VALUES (25, '2025-11-09 22:28:56', '20251109222856384830', 'dish_info', 1, '生腌虾', 'file/meishixinxiCaipintupian1.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已支付', NULL, '不要辣', 'user', 11, '潮汕特色', '餐桌名称10');
INSERT INTO `orders` VALUES (26, '2025-11-09 22:31:01', '20251109223101668030', 'dish_info', 1, '生腌虾', 'file/meishixinxiCaipintupian1.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已支付', NULL, '', 'user', 11, '潮汕特色', '餐桌名称2');
INSERT INTO `orders` VALUES (27, '2025-11-09 22:39:58', '20251109223958498813', 'dish_info', 1, '生腌虾', 'file/meishixinxiCaipintupian1.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已支付', NULL, '', 'user', 11, '潮汕特色', '餐桌名称1');
INSERT INTO `orders` VALUES (28, '2025-11-09 22:40:57', '20251109224057504129', 'dish_info', 1, '生腌虾', 'file/meishixinxiCaipintupian1.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已完成', NULL, '', 'user', 11, '潮汕特色', '餐桌名称1');
INSERT INTO `orders` VALUES (29, '2025-12-03 17:11:24', '20251203171124720754', 'dish_info', 4, '山药卷', 'file/meishixinxiCaipintupian4.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已支付', NULL, '', 'user', 11, '小吃', '餐桌名称2');
INSERT INTO `orders` VALUES (30, '2025-12-03 17:19:19', '20251203171919284425', 'dish_info', 1, '生腌虾', 'file/meishixinxiCaipintupian1.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已支付', NULL, '', 'user', 11, '潮汕特色', '餐桌名称2');
INSERT INTO `orders` VALUES (31, '2025-12-03 19:06:23', '20251203190623550065', 'dish_info', 1, '生腌虾', 'file/meishixinxiCaipintupian1.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已支付', NULL, '', 'user', 11, '潮汕特色', '餐桌名称2');
INSERT INTO `orders` VALUES (32, '2025-12-03 19:08:55', '20251203190855075957', 'dish_info', 1, '生腌虾', 'file/meishixinxiCaipintupian1.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已完成', NULL, '', 'user', 11, '潮汕特色', '餐桌名称1');
INSERT INTO `orders` VALUES (33, '2025-12-03 21:02:23', '20251203210223704698', 'dish_info', 4, '山药卷', 'file/meishixinxiCaipintupian4.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已完成', NULL, '', 'user', 11, '小吃', '餐桌名称2');
INSERT INTO `orders` VALUES (34, '2026-01-13 13:42:02', '20260113134202033250', 'dish_info', 1, '生腌虾', 'file/meishixinxiCaipintupian1.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已支付', NULL, '1', 'user', 11, '潮汕特色', '餐桌名称1');
INSERT INTO `orders` VALUES (35, '2026-01-13 14:03:07', '20260113140307831655', 'dish_info', 1, '生腌虾', 'file/meishixinxiCaipintupian1.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已支付', NULL, '', 'user', 11, '潮汕特色', '餐桌名称2');
INSERT INTO `orders` VALUES (36, '2026-01-13 14:06:06', '20260113140606055662', 'dish_info', 1, '生腌虾', 'file/meishixinxiCaipintupian1.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已支付', NULL, '', 'user', 11, '潮汕特色', '餐桌名称1');
INSERT INTO `orders` VALUES (37, '2026-01-13 14:11:17', '20260113141117690267', 'dish_info', 1, '生腌虾', 'file/meishixinxiCaipintupian1.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '未支付', NULL, '', 'user', 11, '潮汕特色', '餐桌名称2');
INSERT INTO `orders` VALUES (38, '2026-01-13 14:22:15', '20260113142215753657', 'dish_info', 4, '山药卷', 'file/meishixinxiCaipintupian4.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已支付', '19819881111', '', 'user', 11, '小吃', '餐桌名称2');
INSERT INTO `orders` VALUES (39, '2026-01-19 00:41:57', '20260119004157698770', 'dish_info', 1, '生腌虾', 'file/meishixinxiCaipintupian1.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已支付', '19819881111', '', 'user', 11, '潮汕特色菜', NULL);
INSERT INTO `orders` VALUES (40, '2026-01-19 00:50:39', '20260119005039074130', 'dish_info', 1, '生腌虾', 'file/meishixinxiCaipintupian1.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已支付', '19819881111', '', 'user', 11, '潮汕特色菜', NULL);
INSERT INTO `orders` VALUES (41, '2026-01-19 23:37:36', '20260119233736161557', 'dish_info', 1, '生腌虾', 'file/meishixinxiCaipintupian1.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已支付', '19819881111', '', 'user', 11, '潮汕特色', NULL);
INSERT INTO `orders` VALUES (42, '2026-01-20 00:53:19', '20260120005319674519', 'dish_info', 1, '生腌虾', 'file/meishixinxiCaipintupian1.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已支付', '19819881111', '', 'user', 11, '潮汕特色', '餐桌名称2');
INSERT INTO `orders` VALUES (43, '2026-01-20 00:54:21', '20260120005421374991', 'dish_info', 1, '生腌虾', 'file/meishixinxiCaipintupian1.jpg', 1, 99.9, 80, 99.9, 80, '1', '已支付', '19819881111', '', 'user', 11, '潮汕特色', '餐桌名称2');
INSERT INTO `orders` VALUES (44, '2026-01-20 00:54:54', '20260120005454759975', 'dish_info', 1, '生腌虾', 'file/meishixinxiCaipintupian1.jpg', 1, 99.9, 80, 99.9, 80, '1', '已支付', '19819881111', '', 'user', 11, '潮汕特色', '餐桌名称4');
INSERT INTO `orders` VALUES (45, '2026-01-20 00:54:54', '20260120005454740559', 'dish_info', 4, '山药卷', 'file/meishixinxiCaipintupian4.jpg', 1, 99.9, 99.9, 99.9, 99.9, '1', '已支付', '19819881111', '', 'user', 11, '小吃', '餐桌名称4');

-- ----------------------------
-- Table structure for parking_info
-- ----------------------------
DROP TABLE IF EXISTS `parking_info`;
CREATE TABLE `parking_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `spot_number` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '车位编号',
  `spot_image` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '车位图片',
  `area` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '区域',
  `spot_location` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '车位位置',
  `hourly_price` double NULL DEFAULT NULL COMMENT '小时价格',
  `spot_status` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '车位状态',
  `plate_number` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '车牌号',
  `entry_time` datetime NULL DEFAULT NULL COMMENT '入场时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `spot_number`(`spot_number` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '车位信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of parking_info
-- ----------------------------
INSERT INTO `parking_info` VALUES (1, '2025-10-25 17:01:11', '车位编号1', 'file/cheweixinxiCheweitupian1.jpg,file/cheweixinxiCheweitupian2.jpg,file/cheweixinxiCheweitupian3.jpg', 'A', '车位位置1', 10, '空闲', NULL, NULL);
INSERT INTO `parking_info` VALUES (2, '2025-10-25 17:01:11', '车位编号2', 'file/cheweixinxiCheweitupian2.jpg,file/cheweixinxiCheweitupian3.jpg,file/cheweixinxiCheweitupian4.jpg', 'A', '车位位置2', 10, '空闲', NULL, NULL);
INSERT INTO `parking_info` VALUES (3, '2025-10-25 17:01:11', '车位编号3', 'file/cheweixinxiCheweitupian3.jpg,file/cheweixinxiCheweitupian4.jpg,file/cheweixinxiCheweitupian5.jpg', 'A', '车位位置3', 10, '空闲', NULL, NULL);
INSERT INTO `parking_info` VALUES (4, '2025-10-25 17:01:11', '车位编号4', 'file/cheweixinxiCheweitupian4.jpg,file/cheweixinxiCheweitupian5.jpg,file/cheweixinxiCheweitupian6.jpg', 'A', '车位位置4', 10, '空闲', NULL, NULL);
INSERT INTO `parking_info` VALUES (5, '2025-10-25 17:01:11', '车位编号5', 'file/cheweixinxiCheweitupian5.jpg,file/cheweixinxiCheweitupian6.jpg,file/cheweixinxiCheweitupian7.jpg', 'A', '车位位置5', 10, '空闲', NULL, NULL);
INSERT INTO `parking_info` VALUES (6, '2025-10-25 17:01:11', '车位编号6', 'file/cheweixinxiCheweitupian6.jpg,file/cheweixinxiCheweitupian7.jpg,file/cheweixinxiCheweitupian8.jpg', 'A', '车位位置6', 10, '空闲', NULL, NULL);
INSERT INTO `parking_info` VALUES (7, '2025-10-25 17:01:11', '车位编号7', 'file/cheweixinxiCheweitupian7.jpg,file/cheweixinxiCheweitupian8.jpg,file/cheweixinxiCheweitupian9.jpg', 'A', '车位位置7', 10, '空闲', NULL, NULL);
INSERT INTO `parking_info` VALUES (8, '2025-10-25 17:01:11', '车位编号8', 'file/cheweixinxiCheweitupian8.jpg,file/cheweixinxiCheweitupian9.jpg,file/cheweixinxiCheweitupian10.jpg', 'A', '车位位置8', 10, '空闲', NULL, NULL);
INSERT INTO `parking_info` VALUES (9, '2025-10-25 17:01:11', '车位编号9', 'file/cheweixinxiCheweitupian9.jpg,file/cheweixinxiCheweitupian10.jpg,file/cheweixinxiCheweitupian11.jpg', 'A', '车位位置9', 10, '空闲', NULL, NULL);
INSERT INTO `parking_info` VALUES (10, '2025-10-25 17:01:11', '车位编号10', 'file/cheweixinxiCheweitupian10.jpg,file/cheweixinxiCheweitupian11.jpg,file/cheweixinxiCheweitupian12.jpg', 'A', '车位位置10', 10, '空闲', NULL, NULL);
INSERT INTO `parking_info` VALUES (11, '2025-10-25 17:09:17', '车位编号11', 'file/1742893746842.jpg', 'B', '车位位置11', 10, '空闲', NULL, NULL);

-- ----------------------------
-- Table structure for restaurant_event
-- ----------------------------
DROP TABLE IF EXISTS `restaurant_event`;
CREATE TABLE `restaurant_event`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `event_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '活动名称',
  `event_image` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '活动图片',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `event_location` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '活动地点',
  `event_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '活动内容',
  `publish_time` datetime NULL DEFAULT NULL COMMENT '发布时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '餐厅活动' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of restaurant_event
-- ----------------------------
INSERT INTO `restaurant_event` VALUES (1, '2025-11-06 21:00:00', '活动名称1', 'file/dianpuhuodongHuodongtupian1.jpg,file/dianpuhuodongHuodongtupian2.jpg,file/dianpuhuodongHuodongtupian3.jpg', '2025-11-07 21:00:00', '活动地点1', '<p>活动内容1</p>', '2025-11-06 21:00:00', '2025-11-09 21:00:00');
INSERT INTO `restaurant_event` VALUES (2, '2025-11-06 21:00:00', '活动名称2', 'file/dianpuhuodongHuodongtupian2.jpg,file/dianpuhuodongHuodongtupian3.jpg,file/dianpuhuodongHuodongtupian4.jpg', '2025-11-07 21:00:00', '活动地点2', '活动内容2', '2025-11-06 21:00:00', '2025-11-08 21:00:00');
INSERT INTO `restaurant_event` VALUES (3, '2025-11-06 21:00:00', '活动名称3', 'file/dianpuhuodongHuodongtupian3.jpg,file/dianpuhuodongHuodongtupian4.jpg,file/dianpuhuodongHuodongtupian5.jpg', '2025-11-07 21:00:00', '活动地点3', '活动内容3', '2025-11-06 21:00:00', '2025-11-08 21:00:00');
INSERT INTO `restaurant_event` VALUES (4, '2025-11-06 21:00:00', '活动名称4', 'file/dianpuhuodongHuodongtupian4.jpg,file/dianpuhuodongHuodongtupian5.jpg,file/dianpuhuodongHuodongtupian6.jpg', '2025-11-07 21:00:00', '活动地点4', '活动内容4', '2025-11-06 21:00:00', '2025-11-08 21:00:00');
INSERT INTO `restaurant_event` VALUES (5, '2025-11-06 21:00:00', '活动名称5', 'file/dianpuhuodongHuodongtupian5.jpg,file/dianpuhuodongHuodongtupian6.jpg,file/dianpuhuodongHuodongtupian7.jpg', '2025-11-07 21:00:00', '活动地点5', '活动内容5', '2025-11-06 21:00:00', '2025-11-08 21:00:00');
INSERT INTO `restaurant_event` VALUES (6, '2025-11-06 21:00:00', '活动名称6', 'file/dianpuhuodongHuodongtupian6.jpg,file/dianpuhuodongHuodongtupian7.jpg,file/dianpuhuodongHuodongtupian8.jpg', '2025-11-07 21:00:00', '活动地点6', '活动内容6', '2025-11-06 21:00:00', '2025-11-08 21:00:00');
INSERT INTO `restaurant_event` VALUES (7, '2025-11-06 21:00:00', '活动名称7', 'file/dianpuhuodongHuodongtupian7.jpg,file/dianpuhuodongHuodongtupian8.jpg,file/dianpuhuodongHuodongtupian9.jpg', '2025-11-07 21:00:00', '活动地点7', '活动内容7', '2025-11-06 21:00:00', '2025-11-08 21:00:00');
INSERT INTO `restaurant_event` VALUES (8, '2025-11-06 21:00:00', '活动名称8', 'file/dianpuhuodongHuodongtupian8.jpg,file/dianpuhuodongHuodongtupian9.jpg,file/dianpuhuodongHuodongtupian10.jpg', '2025-11-07 21:00:00', '活动地点8', '活动内容8', '2025-11-06 21:00:00', '2025-11-08 21:00:00');
INSERT INTO `restaurant_event` VALUES (9, '2025-11-06 21:00:00', '活动名称9', 'file/dianpuhuodongHuodongtupian9.jpg,file/dianpuhuodongHuodongtupian10.jpg,file/dianpuhuodongHuodongtupian11.jpg', '2025-11-07 21:00:00', '活动地点9', '活动内容9', '2025-11-06 21:00:00', '2025-11-08 21:00:00');
INSERT INTO `restaurant_event` VALUES (10, '2025-11-06 21:00:00', '活动名称10', 'file/dianpuhuodongHuodongtupian10.jpg,file/dianpuhuodongHuodongtupian11.jpg,file/dianpuhuodongHuodongtupian12.jpg', '2025-11-07 21:00:00', '活动地点10', '活动内容10', '2025-11-06 21:00:00', '2025-11-08 21:00:00');

-- ----------------------------
-- Table structure for restaurant_info
-- ----------------------------
DROP TABLE IF EXISTS `restaurant_info`;
CREATE TABLE `restaurant_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `seat_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '餐桌名称',
  `cover_image` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '封面',
  `table_location` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '餐桌位置',
  `capacity` int NULL DEFAULT NULL COMMENT '可坐人数',
  `table_status` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '餐桌状态',
  `storeup_number` int NULL DEFAULT 0 COMMENT '收藏数',
  `discuss_number` int NULL DEFAULT 0 COMMENT '评论数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '餐厅信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of restaurant_info
-- ----------------------------
INSERT INTO `restaurant_info` VALUES (1, '2025-10-25 17:01:10', '餐桌名称1', 'file/cantingxinxiFengmian1.jpg', '餐桌位置1', 10, '允许预约', 1, 1);
INSERT INTO `restaurant_info` VALUES (2, '2025-10-25 17:01:10', '餐桌名称2', 'file/cantingxinxiFengmian2.jpg,file/cantingxinxiFengmian3.jpg,file/cantingxinxiFengmian4.jpg', '餐桌位置2', 2, '允许预约', 3, 3);
INSERT INTO `restaurant_info` VALUES (3, '2025-10-25 17:01:10', '餐桌名称3', 'file/cantingxinxiFengmian3.jpg,file/cantingxinxiFengmian4.jpg,file/cantingxinxiFengmian5.jpg', '餐桌位置3', 3, '允许预约', 3, 3);
INSERT INTO `restaurant_info` VALUES (4, '2025-10-25 17:01:10', '餐桌名称4', 'file/cantingxinxiFengmian4.jpg,file/cantingxinxiFengmian5.jpg,file/cantingxinxiFengmian6.jpg', '餐桌位置4', 4, '允许预约', 4, 4);
INSERT INTO `restaurant_info` VALUES (5, '2025-10-25 17:01:10', '餐桌名称5', 'file/cantingxinxiFengmian5.jpg,file/cantingxinxiFengmian6.jpg,file/cantingxinxiFengmian7.jpg', '餐桌位置5', 5, '允许预约', 5, 5);
INSERT INTO `restaurant_info` VALUES (6, '2025-10-25 17:01:10', '餐桌名称6', 'file/cantingxinxiFengmian6.jpg,file/cantingxinxiFengmian7.jpg,file/cantingxinxiFengmian8.jpg', '餐桌位置6', 6, '允许预约', 6, 6);
INSERT INTO `restaurant_info` VALUES (7, '2025-10-25 17:01:10', '餐桌名称7', 'file/cantingxinxiFengmian7.jpg,file/cantingxinxiFengmian8.jpg,file/cantingxinxiFengmian9.jpg', '餐桌位置7', 7, '允许预约', 7, 7);
INSERT INTO `restaurant_info` VALUES (8, '2025-10-25 17:01:10', '餐桌名称8', 'file/cantingxinxiFengmian8.jpg,file/cantingxinxiFengmian9.jpg,file/cantingxinxiFengmian10.jpg', '餐桌位置8', 8, '允许预约', 8, 8);
INSERT INTO `restaurant_info` VALUES (9, '2025-10-25 17:01:10', '餐桌名称9', 'file/cantingxinxiFengmian9.jpg,file/cantingxinxiFengmian10.jpg,file/cantingxinxiFengmian11.jpg', '餐桌位置9', 9, '允许预约', 9, 9);
INSERT INTO `restaurant_info` VALUES (10, '2025-10-25 17:01:10', '餐桌名称10', 'file/cantingxinxiFengmian10.jpg,file/cantingxinxiFengmian11.jpg,file/cantingxinxiFengmian12.jpg', '餐桌位置10', 10, '允许预约', 10, 11);

-- ----------------------------
-- Table structure for restaurant_reservation
-- ----------------------------
DROP TABLE IF EXISTS `restaurant_reservation`;
CREATE TABLE `restaurant_reservation`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `seat_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '餐桌名称',
  `cover_image` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '封面',
  `table_location` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '餐桌位置',
  `capacity` int NULL DEFAULT NULL COMMENT '可坐人数',
  `storeup_number` int NULL DEFAULT NULL COMMENT '收藏数',
  `account` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '账号',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `reservation_time` datetime NULL DEFAULT NULL COMMENT '预约时间',
  `deposit` double NULL DEFAULT 50 COMMENT '定金金额',
  `payment_status` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '未支付' COMMENT '支付状态',
  `verification_status` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '未核销' COMMENT '核销状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '餐厅预约' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of restaurant_reservation
-- ----------------------------
INSERT INTO `restaurant_reservation` VALUES (1, '2025-10-25 17:01:11', '餐桌名称1', 'file/cantingyuyueFengmian1.jpg', '餐桌位置1', 1, 1, '账号1', '名称1', '2025-10-25 17:01:11', 50, '未支付', '未核销');
INSERT INTO `restaurant_reservation` VALUES (2, '2025-10-25 17:01:11', '餐桌名称2', 'file/cantingyuyueFengmian2.jpg', '餐桌位置2', 2, 2, '账号2', '名称2', '2025-10-25 17:01:11', 50, '未支付', '未核销');
INSERT INTO `restaurant_reservation` VALUES (3, '2025-10-25 17:01:11', '餐桌名称3', 'file/cantingyuyueFengmian3.jpg,file/cantingyuyueFengmian4.jpg,file/cantingyuyueFengmian5.jpg', '餐桌位置3', 3, 3, '账号3', '名称3', '2025-10-25 17:01:11', 50, '未支付', '未核销');
INSERT INTO `restaurant_reservation` VALUES (4, '2025-10-25 17:01:11', '餐桌名称4', 'file/cantingyuyueFengmian4.jpg,file/cantingyuyueFengmian5.jpg,file/cantingyuyueFengmian6.jpg', '餐桌位置4', 4, 4, '账号4', '名称4', '2025-10-25 17:01:11', 50, '未支付', '未核销');
INSERT INTO `restaurant_reservation` VALUES (5, '2025-10-25 17:01:11', '餐桌名称5', 'file/cantingyuyueFengmian5.jpg,file/cantingyuyueFengmian6.jpg,file/cantingyuyueFengmian7.jpg', '餐桌位置5', 5, 5, '账号5', '名称5', '2025-10-25 17:01:11', 50, '未支付', '未核销');
INSERT INTO `restaurant_reservation` VALUES (6, '2025-10-25 17:01:11', '餐桌名称6', 'file/cantingyuyueFengmian6.jpg,file/cantingyuyueFengmian7.jpg,file/cantingyuyueFengmian8.jpg', '餐桌位置6', 6, 6, '账号6', '名称6', '2025-10-25 17:01:11', 50, '未支付', '未核销');
INSERT INTO `restaurant_reservation` VALUES (7, '2025-10-25 17:01:11', '餐桌名称7', 'file/cantingyuyueFengmian7.jpg,file/cantingyuyueFengmian8.jpg,file/cantingyuyueFengmian9.jpg', '餐桌位置7', 7, 7, '账号7', '名称7', '2025-10-25 17:01:11', 50, '未支付', '未核销');
INSERT INTO `restaurant_reservation` VALUES (8, '2025-10-25 17:01:11', '餐桌名称8', 'file/cantingyuyueFengmian8.jpg,file/cantingyuyueFengmian9.jpg,file/cantingyuyueFengmian10.jpg', '餐桌位置8', 8, 8, '账号8', '名称8', '2025-10-25 17:01:11', 50, '未支付', '未核销');
INSERT INTO `restaurant_reservation` VALUES (9, '2025-10-25 17:01:11', '餐桌名称9', 'file/cantingyuyueFengmian9.jpg,file/cantingyuyueFengmian10.jpg,file/cantingyuyueFengmian11.jpg', '餐桌位置9', 9, 9, '账号9', '名称9', '2025-10-25 17:01:11', 50, '未支付', '未核销');
INSERT INTO `restaurant_reservation` VALUES (10, '2025-10-25 17:01:11', '餐桌名称10', 'file/cantingyuyueFengmian10.jpg,file/cantingyuyueFengmian11.jpg,file/cantingyuyueFengmian12.jpg', '餐桌位置10', 10, 10, '账号10', '名称10', '2025-10-25 17:01:11', 50, '未支付', '未核销');
INSERT INTO `restaurant_reservation` VALUES (11, '2025-10-25 17:08:18', '餐桌名称10', 'file/cantingxinxiFengmian10.jpg,file/cantingxinxiFengmian11.jpg,file/cantingxinxiFengmian12.jpg', '餐桌位置10', 10, 10, '账号1', '名称1', '2025-10-26 00:00:00', 50, '未支付', '未核销');
INSERT INTO `restaurant_reservation` VALUES (13, '2025-11-02 18:16:30', '餐桌名称10', 'file/cantingxinxiFengmian10.jpg,file/cantingxinxiFengmian11.jpg,file/cantingxinxiFengmian12.jpg', '餐桌位置10', 10, 10, '账号1', '名称1', '2025-11-19 00:00:00', 50, '未支付', '未核销');
INSERT INTO `restaurant_reservation` VALUES (20, '2025-11-09 22:48:06', '餐桌名称2', 'file/cantingxinxiFengmian2.jpg,file/cantingxinxiFengmian3.jpg,file/cantingxinxiFengmian4.jpg', '餐桌位置2', 2, 3, '账号1', '名称1', '2025-11-09 10:00:00', 50, '已支付', '未核销');
INSERT INTO `restaurant_reservation` VALUES (21, '2025-12-03 19:57:19', '餐桌名称1', 'file/cantingxinxiFengmian1.jpg', '餐桌位置1', 10, 1, '账号1', '名称1', '2025-12-03 10:00:00', 50, '已支付', '未核销');
INSERT INTO `restaurant_reservation` VALUES (22, '2025-12-03 21:01:14', '餐桌名称1', 'file/cantingxinxiFengmian1.jpg', '餐桌位置1', 10, 1, '账号1', '名称1', '2025-12-04 10:00:00', 50, '已退款', '已核销');
INSERT INTO `restaurant_reservation` VALUES (23, '2025-12-03 21:04:59', '餐桌名称1', 'file/cantingxinxiFengmian1.jpg', '餐桌位置1', 10, 1, '账号1', '名称1', '2025-12-04 11:00:00', 50, '已支付', '未核销');

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
  `userid` bigint NOT NULL COMMENT '用户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '我的收藏' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of storeup
-- ----------------------------
INSERT INTO `storeup` VALUES (1, '2025-10-25 17:05:46', 10, 'dish_info', '菜品名称10', 'file/meishixinxiCaipintupian10.jpg', 11);
INSERT INTO `storeup` VALUES (2, '2025-10-25 17:06:27', 2, 'restaurant_info', '餐桌名称2', 'file/cantingxinxiFengmian2.jpg', 11);
INSERT INTO `storeup` VALUES (3, '2025-11-09 13:39:02', 3, 'dish_info', '梅菜扣肉', 'file/meishixinxiCaipintupian3.jpg', 11);
INSERT INTO `storeup` VALUES (4, '2025-11-09 21:46:24', 4, 'dish_info', '山药卷', 'file/meishixinxiCaipintupian4.jpg', 11);
INSERT INTO `storeup` VALUES (5, '2026-01-18 23:24:57', 1, 'restaurant_info', '餐桌名称1', 'file/cantingxinxiFengmian1.jpg', 11);

-- ----------------------------
-- Table structure for system_notice
-- ----------------------------
DROP TABLE IF EXISTS `system_notice`;
CREATE TABLE `system_notice`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '公告内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统公告' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_notice
-- ----------------------------
INSERT INTO `system_notice` VALUES (1, '2025-11-25 17:01:10', '<p>公告</p>');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `role` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色',
  `login_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '登录账号',
  `password` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `avatar` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '头像',
  `gender` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '性别',
  `mobile` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号码',
  `security_question` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密保问题',
  `security_answer` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密保答案',
  `max_password_wrong` int NOT NULL DEFAULT 5 COMMENT '最大密码输错次数',
  `is_locked` int NOT NULL DEFAULT 0 COMMENT '用户锁定状态',
  `money` double NULL DEFAULT 0 COMMENT '余额',
  `position` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '职位',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_login_name`(`login_name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1742894247742 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户/员工/管理员' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, '2025-10-25 17:01:10', '管理员', 'admin', 'admin', NULL, 'file/1742893901879.jpg', NULL, NULL, NULL, NULL, 5, 0, 0, NULL);
INSERT INTO `users` VALUES (11, '2025-10-25 17:01:09', '用户', '账号1', '123456', '名称1', 'file/userTouxiang1.jpg', '男', '19819881111', '密保问题1', '密保答案1', 0, 0, 776, NULL);
INSERT INTO `users` VALUES (12, '2025-10-25 17:01:09', '用户', '账号2', '123456', '名称2', 'file/userTouxiang2.jpg', '女', '19819881112', '密保问题2', '密保答案2', 2, 0, 200, NULL);
INSERT INTO `users` VALUES (13, '2025-10-25 17:01:09', '用户', '账号3', '123456', '名称3', 'file/userTouxiang3.jpg', '男', '19819881113', '密保问题3', '密保答案3', 3, 0, 200, NULL);
INSERT INTO `users` VALUES (14, '2025-10-25 17:01:09', '用户', '账号4', '123456', '名称4', 'file/userTouxiang4.jpg', '女', '19819881114', '密保问题4', '密保答案4', 4, 0, 200, NULL);
INSERT INTO `users` VALUES (15, '2025-10-25 17:01:09', '用户', '账号5', '123456', '名称5', 'file/userTouxiang5.jpg', '男', '19819881115', '密保问题5', '密保答案5', 5, 0, 200, NULL);
INSERT INTO `users` VALUES (111, '2025-10-25 17:01:10', '员工', '员工1', '123456', '员工姓名1', 'file/yuangongTouxiang1.jpg', NULL, '19819881111', NULL, NULL, 5, 0, 0, '主管1');
INSERT INTO `users` VALUES (112, '2025-10-25 17:01:10', '员工', '员工2', '123456', '员工姓名2', 'file/yuangongTouxiang2.jpg', NULL, '19819881112', NULL, NULL, 5, 0, 0, '主管2');
INSERT INTO `users` VALUES (113, '2025-10-25 17:01:10', '员工', '员工3', '123456', '员工姓名3', 'file/yuangongTouxiang3.jpg', NULL, '19819881113', NULL, NULL, 5, 0, 0, '主管3');
INSERT INTO `users` VALUES (114, '2025-10-25 17:01:10', '员工', '员工4', '123456', '员工姓名4', 'file/yuangongTouxiang4.jpg', NULL, '19819881114', NULL, NULL, 5, 0, 0, '主管4');
INSERT INTO `users` VALUES (115, '2025-10-25 17:01:10', '员工', '员工5', '123456', '员工姓名5', 'file/yuangongTouxiang5.jpg', NULL, '19819881115', NULL, NULL, 5, 0, 0, '主管5');
INSERT INTO `users` VALUES (116, '2025-10-25 17:01:10', '员工', '员工6', '123456', '员工姓名6', 'file/yuangongTouxiang6.jpg', NULL, '19819881116', NULL, NULL, 5, 0, 0, '主管6');
INSERT INTO `users` VALUES (117, '2025-10-25 17:01:10', '员工', '员工7', '123456', '员工姓名7', 'file/yuangongTouxiang7.jpg', NULL, '19819881117', NULL, NULL, 5, 0, 0, '主管7');
INSERT INTO `users` VALUES (118, '2025-10-25 17:01:10', '员工', '员工8', '123456', '员工姓名8', 'file/yuangongTouxiang8.jpg', NULL, '19819881118', NULL, NULL, 5, 0, 0, '主管8');
INSERT INTO `users` VALUES (119, '2025-10-25 17:01:10', '员工', '员工9', '123456', '员工姓名9', 'file/yuangongTouxiang9.jpg', NULL, '19819881119', NULL, NULL, 5, 0, 0, '主管9');
INSERT INTO `users` VALUES (120, '2025-10-25 17:01:10', '员工', '员工10', '123456', '员工姓名10', 'file/yuangongTouxiang10.jpg', NULL, '198198811110', NULL, NULL, 5, 0, 0, '主管10');

SET FOREIGN_KEY_CHECKS = 1;
