-- MySQL dump 10.13  Distrib 5.7.36, for Linux (x86_64)
--
-- Host: localhost    Database: mall_marketing
-- ------------------------------------------------------
-- Server version	5.7.36-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS */`mall_marketing` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;

USE `mall_marketing`;
--
-- Table structure for table `activity`
--

DROP TABLE IF EXISTS `activity`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activity`
(
    `id`                  int(10) unsigned                       NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `name`                varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '活动名称',
    `activity_start_time` datetime                               NOT NULL COMMENT '活动开始时间',
    `activity_end_time`   datetime                               NOT NULL COMMENT '活动结束时间',
    `is_online`           tinyint(1)       DEFAULT NULL COMMENT '是否上线',
    `create_user`         int(10) unsigned                       NOT NULL COMMENT '创建人id',
    `create_time`         datetime                               NOT NULL COMMENT '创建时间',
    `update_user`         int(10) unsigned DEFAULT NULL COMMENT '更新人id',
    `update_time`         datetime         DEFAULT NULL COMMENT '更新时间',
    `is_deleted`          tinyint(1)                             NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 16
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='活动表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity`
--

LOCK TABLES `activity` WRITE;
/*!40000 ALTER TABLE `activity`
    DISABLE KEYS */;
INSERT INTO `activity`
VALUES (1, '秒杀活动', '2022-05-20 00:00:00', '2022-05-21 23:59:59', 0, 1, '2022-05-05 16:58:13', 1, '2022-05-07 16:42:34',
        1),
       (2, '过期活动', '2022-05-01 00:00:00', '2022-05-04 23:59:59', 0, 1, '2022-05-05 16:58:30', NULL, NULL, 1),
       (3, '过期活动', '2022-05-01 00:00:00', '2022-05-05 23:59:59', 0, 14, '2022-05-06 11:23:22', NULL, NULL, 1),
       (4, '过期', '2022-05-01 00:00:00', '2022-05-02 23:59:59', 0, 14, '2022-05-06 11:24:20', 1, '2022-05-06 11:49:43',
        1),
       (5, '进行中', '2022-05-03 00:00:00', '2022-05-10 23:59:59', 0, 14, '2022-05-06 11:24:30', 11, '2022-05-07 20:47:07',
        1),
       (6, '111', '2022-05-16 00:00:00', '2022-06-20 23:59:59', 0, 1, '2022-05-06 18:57:35', NULL, NULL, 1),
       (7, '1111', '2022-06-27 00:00:00', '2022-06-28 23:59:59', 0, 1, '2022-05-07 16:18:30', 1, '2022-05-07 16:28:36',
        1),
       (8, '马士兵严选年中大促', '2022-05-11 00:00:00', '2022-05-12 23:59:59', 0, 11, '2022-05-07 17:48:24', 11,
        '2022-05-07 18:11:56', 1),
       (9, '123', '2022-05-20 00:00:00', '2022-05-21 23:59:59', 0, 14, '2022-05-07 19:00:44', NULL, NULL, 1),
       (10, 'aaa', '2022-05-08 00:00:00', '2022-05-11 23:59:59', 0, 1, '2022-05-09 10:46:08', 1, '2022-05-10 19:42:49',
        1),
       (11, '11111', '2022-05-08 00:00:00', '2022-05-12 23:59:59', 0, 1, '2022-05-09 11:01:15', 1,
        '2022-05-09 16:21:12', 1),
       (12, '32132132132142112421321', '2022-05-14 00:00:00', '2022-05-28 23:59:59', 0, 1, '2022-05-09 15:23:12', 1,
        '2022-05-09 15:23:41', 1),
       (13, '123', '2022-05-27 00:00:00', '2022-05-28 23:59:59', 0, 14, '2022-05-09 16:34:27', NULL, NULL, 1),
       (14, '213', '2022-05-04 00:00:00', '2022-05-04 23:59:59', 0, 14, '2022-05-09 16:35:30', NULL, NULL, 1),
       (15, '秒杀', '2022-05-13 00:00:00', '2022-06-30 23:59:59', 1, 1, '2022-05-11 10:29:09', 1, '2022-05-27 17:13:01',
        0);
/*!40000 ALTER TABLE `activity`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activity_product`
--

DROP TABLE IF EXISTS `activity_product`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activity_product`
(
    `id`                     int(10) unsigned                        NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `activity_time_id`       int(10) unsigned                        NOT NULL COMMENT '活动对应时间段id',
    `product_id`             int(10) unsigned                        NOT NULL COMMENT '商品id',
    `product_name`           varchar(50) COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '商品名称',
    `product_main_picture`   varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品主图',
    `product_starting_price` decimal(11, 2)   DEFAULT NULL COMMENT '商品起售价',
    `category_id`            int(10) unsigned                        NOT NULL COMMENT '分类id',
    `create_user`            int(10) unsigned                        NOT NULL COMMENT '创建人id',
    `create_time`            datetime                                NOT NULL COMMENT '创建时间',
    `update_user`            int(10) unsigned DEFAULT NULL COMMENT '更新人id',
    `update_time`            datetime         DEFAULT NULL COMMENT '更新时间',
    `is_deleted`             tinyint(1)                              NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 140
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='活动商品表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_product`
--

LOCK TABLES `activity_product` WRITE;
/*!40000 ALTER TABLE `activity_product`
    DISABLE KEYS */;
INSERT INTO `activity_product`
VALUES (1, 1, 2, '四川大凉山丑苹果新鲜当季水果冰糖心大苹果现摘现发脆甜',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product2bca55bb5c3adb4d02a7a76039d097eewww800-800.jpg',
        37.00, 38, 14, '2022-05-06 11:22:01', NULL, NULL, 0),
       (2, 4, 2, '四川大凉山丑苹果新鲜当季水果冰糖心大苹果现摘现发脆甜',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product2bca55bb5c3adb4d02a7a76039d097eewww800-800.jpg',
        37.00, 38, 1, '2022-05-06 11:37:42', NULL, NULL, 0),
       (3, 3, 2, '四川大凉山丑苹果新鲜当季水果冰糖心大苹果现摘现发脆甜',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product2bca55bb5c3adb4d02a7a76039d097eewww800-800.jpg',
        37.00, 38, 1, '2022-05-06 11:50:29', NULL, NULL, 0),
       (4, 3, 3, '云南新鲜小黄姜嫩生姜产地直销',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product4291c68c8352edee6a08255fc8ccbe6fwww755-755.jpg',
        29.00, 38, 1, '2022-05-06 11:50:29', NULL, NULL, 0),
       (5, 3, 5, '广西荔浦芋头新鲜现挖当季香芋新鲜槟榔紫藤芋头农家蔬菜',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product81a4404f4967305eb252b51c19c70669www800-800.jpg',
        37.00, 38, 1, '2022-05-06 11:50:29', NULL, NULL, 0),
       (6, 3, 6, '【海南金煌芒】当季新鲜水果芒果个大肉厚',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productv1_HyUYnSed_70878597530529303160615_30cb18c6c60f2fe869e1aac6ec1996d8_sx_361677_www800-800.jpg',
        43.00, 38, 1, '2022-05-06 11:50:29', NULL, NULL, 0),
       (7, 3, 13, '源百希广西纯水牛奶200ml*10盒学生宿舍儿童早餐营养常温纯水牛奶',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01aeBbww2KcJrsrPVe1_!!0-item_pic.jpg_430x430q90.jpg',
        69.00, 38, 1, '2022-05-06 11:50:29', NULL, NULL, 0),
       (8, 3, 14, 'C6T测体温手环 血压心率血氧睡眠运动计步健康监测 智能手环',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product13495852650_684060882.jpg', 69.00, 53, 1,
        '2022-05-06 11:50:29', NULL, NULL, 0),
       (9, 3, 15, 'SKG K3颈椎按摩仪护颈仪颈部按摩器礼品',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN013l1P561gwqxR2JVXF_!!3319944207-0-cib.jpg',
        259.00, 52, 1, '2022-05-06 11:50:29', NULL, NULL, 0),
       (10, 7, 2, '四川大凉山丑苹果新鲜当季水果冰糖心大苹果现摘现发脆甜',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product2bca55bb5c3adb4d02a7a76039d097eewww800-800.jpg',
        37.00, 38, 1, '2022-05-06 11:50:35', NULL, NULL, 1),
       (11, 7, 3, '云南新鲜小黄姜嫩生姜产地直销',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product4291c68c8352edee6a08255fc8ccbe6fwww755-755.jpg',
        29.00, 38, 1, '2022-05-06 11:50:35', NULL, NULL, 0),
       (12, 7, 5, '广西荔浦芋头新鲜现挖当季香芋新鲜槟榔紫藤芋头农家蔬菜',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product81a4404f4967305eb252b51c19c70669www800-800.jpg',
        37.00, 38, 1, '2022-05-06 11:50:35', NULL, NULL, 0),
       (13, 7, 6, '【海南金煌芒】当季新鲜水果芒果个大肉厚',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productv1_HyUYnSed_70878597530529303160615_30cb18c6c60f2fe869e1aac6ec1996d8_sx_361677_www800-800.jpg',
        43.00, 38, 1, '2022-05-06 11:50:35', NULL, NULL, 0),
       (14, 7, 13, '源百希广西纯水牛奶200ml*10盒学生宿舍儿童早餐营养常温纯水牛奶',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01aeBbww2KcJrsrPVe1_!!0-item_pic.jpg_430x430q90.jpg',
        69.00, 38, 1, '2022-05-06 11:50:35', NULL, NULL, 0),
       (15, 7, 14, 'C6T测体温手环 血压心率血氧睡眠运动计步健康监测 智能手环',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product13495852650_684060882.jpg', 69.00, 53, 1,
        '2022-05-06 11:50:35', NULL, NULL, 0),
       (16, 7, 15, 'SKG K3颈椎按摩仪护颈仪颈部按摩器礼品',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN013l1P561gwqxR2JVXF_!!3319944207-0-cib.jpg',
        259.00, 52, 1, '2022-05-06 11:50:35', NULL, NULL, 0),
       (17, 7, 16, '跨境UM68T全触屏1.69蓝牙手表计步体温检测动态心率睡眠智能手表',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01YEWlgG1DItVQnTuDR_!!2555070194-0-cib.jpg',
        129.00, 53, 1, '2022-05-06 11:50:35', NULL, NULL, 0),
       (18, 7, 17, '跨境私模 typec六合一扩展坞usb hub3.0 hdmi 读卡器笔记本拓展坞',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01N5oBIf1JmYuXN6sMB_!!2212978381071-0-cib.jpg',
        69.00, 50, 1, '2022-05-06 11:50:35', NULL, NULL, 0),
       (19, 7, 18, '柏战K20青轴机械键盘 混光机械键盘青轴电竞网吧游戏键盘电脑键盘',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN011nFEoP1DrheUNhNgU_!!3435280270-0-cib.jpg',
        79.00, 51, 1, '2022-05-06 11:50:35', NULL, NULL, 0),
       (20, 2, 2, '四川大凉山丑苹果新鲜当季水果冰糖心大苹果现摘现发脆甜',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product2bca55bb5c3adb4d02a7a76039d097eewww800-800.jpg',
        37.00, 38, 1, '2022-05-06 14:30:51', NULL, NULL, 0),
       (21, 2, 3, '云南新鲜小黄姜嫩生姜产地直销',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product4291c68c8352edee6a08255fc8ccbe6fwww755-755.jpg',
        29.00, 38, 1, '2022-05-06 14:30:51', NULL, NULL, 0),
       (22, 2, 5, '广西荔浦芋头新鲜现挖当季香芋新鲜槟榔紫藤芋头农家蔬菜',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product81a4404f4967305eb252b51c19c70669www800-800.jpg',
        37.00, 38, 1, '2022-05-06 14:30:51', NULL, NULL, 0),
       (23, 2, 6, '【海南金煌芒】当季新鲜水果芒果个大肉厚',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productv1_HyUYnSed_70878597530529303160615_30cb18c6c60f2fe869e1aac6ec1996d8_sx_361677_www800-800.jpg',
        43.00, 38, 1, '2022-05-06 14:30:51', NULL, NULL, 0),
       (24, 2, 13, '源百希广西纯水牛奶200ml*10盒学生宿舍儿童早餐营养常温纯水牛奶',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01aeBbww2KcJrsrPVe1_!!0-item_pic.jpg_430x430q90.jpg',
        69.00, 38, 1, '2022-05-06 14:30:51', NULL, NULL, 0),
       (25, 2, 14, 'C6T测体温手环 血压心率血氧睡眠运动计步健康监测 智能手环',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product13495852650_684060882.jpg', 69.00, 53, 1,
        '2022-05-06 14:30:51', NULL, NULL, 0),
       (26, 2, 15, 'SKG K3颈椎按摩仪护颈仪颈部按摩器礼品',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN013l1P561gwqxR2JVXF_!!3319944207-0-cib.jpg',
        259.00, 52, 1, '2022-05-06 14:30:51', NULL, NULL, 0),
       (27, 2, 16, '跨境UM68T全触屏1.69蓝牙手表计步体温检测动态心率睡眠智能手表',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01YEWlgG1DItVQnTuDR_!!2555070194-0-cib.jpg',
        129.00, 53, 1, '2022-05-06 14:30:51', NULL, NULL, 1),
       (28, 2, 17, '跨境私模 typec六合一扩展坞usb hub3.0 hdmi 读卡器笔记本拓展坞',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01N5oBIf1JmYuXN6sMB_!!2212978381071-0-cib.jpg',
        69.00, 50, 1, '2022-05-06 14:30:51', NULL, NULL, 0),
       (29, 2, 18, '柏战K20青轴机械键盘 混光机械键盘青轴电竞网吧游戏键盘电脑键盘',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN011nFEoP1DrheUNhNgU_!!3435280270-0-cib.jpg',
        79.00, 51, 1, '2022-05-06 14:30:51', NULL, NULL, 0),
       (30, 8, 2, '四川大凉山丑苹果新鲜当季水果冰糖心大苹果现摘现发脆甜',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product2bca55bb5c3adb4d02a7a76039d097eewww800-800.jpg',
        37.00, 38, 1, '2022-05-06 14:33:53', NULL, NULL, 0),
       (31, 8, 3, '云南新鲜小黄姜嫩生姜产地直销',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product4291c68c8352edee6a08255fc8ccbe6fwww755-755.jpg',
        29.00, 38, 1, '2022-05-06 14:33:53', NULL, NULL, 0),
       (32, 8, 5, '广西荔浦芋头新鲜现挖当季香芋新鲜槟榔紫藤芋头农家蔬菜',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product81a4404f4967305eb252b51c19c70669www800-800.jpg',
        37.00, 38, 1, '2022-05-06 14:33:53', NULL, NULL, 0),
       (33, 8, 6, '【海南金煌芒】当季新鲜水果芒果个大肉厚',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productv1_HyUYnSed_70878597530529303160615_30cb18c6c60f2fe869e1aac6ec1996d8_sx_361677_www800-800.jpg',
        43.00, 38, 1, '2022-05-06 14:33:53', NULL, NULL, 0),
       (34, 8, 13, '源百希广西纯水牛奶200ml*10盒学生宿舍儿童早餐营养常温纯水牛奶',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01aeBbww2KcJrsrPVe1_!!0-item_pic.jpg_430x430q90.jpg',
        69.00, 38, 1, '2022-05-06 14:33:53', NULL, NULL, 0),
       (35, 8, 14, 'C6T测体温手环 血压心率血氧睡眠运动计步健康监测 智能手环',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product13495852650_684060882.jpg', 69.00, 53, 1,
        '2022-05-06 14:33:53', NULL, NULL, 0),
       (36, 8, 15, 'SKG K3颈椎按摩仪护颈仪颈部按摩器礼品',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN013l1P561gwqxR2JVXF_!!3319944207-0-cib.jpg',
        259.00, 52, 1, '2022-05-06 14:33:53', NULL, NULL, 0),
       (37, 8, 16, '跨境UM68T全触屏1.69蓝牙手表计步体温检测动态心率睡眠智能手表',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01YEWlgG1DItVQnTuDR_!!2555070194-0-cib.jpg',
        129.00, 53, 1, '2022-05-06 14:33:53', NULL, NULL, 1),
       (38, 8, 17, '跨境私模 typec六合一扩展坞usb hub3.0 hdmi 读卡器笔记本拓展坞',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01N5oBIf1JmYuXN6sMB_!!2212978381071-0-cib.jpg',
        69.00, 50, 1, '2022-05-06 14:33:53', NULL, NULL, 0),
       (39, 8, 18, '柏战K20青轴机械键盘 混光机械键盘青轴电竞网吧游戏键盘电脑键盘',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN011nFEoP1DrheUNhNgU_!!3435280270-0-cib.jpg',
        79.00, 51, 1, '2022-05-06 14:33:53', NULL, NULL, 0),
       (40, 4, 3, '云南新鲜小黄姜嫩生姜产地直销',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product4291c68c8352edee6a08255fc8ccbe6fwww755-755.jpg',
        29.00, 38, 1, '2022-05-06 15:25:30', NULL, NULL, 0),
       (41, 4, 5, '广西荔浦芋头新鲜现挖当季香芋新鲜槟榔紫藤芋头农家蔬菜',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product81a4404f4967305eb252b51c19c70669www800-800.jpg',
        37.00, 38, 1, '2022-05-06 15:25:30', NULL, NULL, 0),
       (42, 4, 6, '【海南金煌芒】当季新鲜水果芒果个大肉厚',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productv1_HyUYnSed_70878597530529303160615_30cb18c6c60f2fe869e1aac6ec1996d8_sx_361677_www800-800.jpg',
        43.00, 38, 1, '2022-05-06 15:25:30', NULL, NULL, 0),
       (43, 4, 13, '源百希广西纯水牛奶200ml*10盒学生宿舍儿童早餐营养常温纯水牛奶',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01aeBbww2KcJrsrPVe1_!!0-item_pic.jpg_430x430q90.jpg',
        69.00, 38, 1, '2022-05-06 15:25:30', NULL, NULL, 0),
       (44, 4, 14, 'C6T测体温手环 血压心率血氧睡眠运动计步健康监测 智能手环',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product13495852650_684060882.jpg', 69.00, 53, 1,
        '2022-05-06 15:25:30', NULL, NULL, 0),
       (45, 4, 15, 'SKG K3颈椎按摩仪护颈仪颈部按摩器礼品',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN013l1P561gwqxR2JVXF_!!3319944207-0-cib.jpg',
        259.00, 52, 1, '2022-05-06 15:25:30', NULL, NULL, 0),
       (46, 4, 16, '跨境UM68T全触屏1.69蓝牙手表计步体温检测动态心率睡眠智能手表',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01YEWlgG1DItVQnTuDR_!!2555070194-0-cib.jpg',
        129.00, 53, 1, '2022-05-06 15:25:30', NULL, NULL, 0),
       (47, 4, 17, '跨境私模 typec六合一扩展坞usb hub3.0 hdmi 读卡器笔记本拓展坞',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01N5oBIf1JmYuXN6sMB_!!2212978381071-0-cib.jpg',
        69.00, 50, 1, '2022-05-06 15:25:30', NULL, NULL, 0),
       (48, 4, 18, '柏战K20青轴机械键盘 混光机械键盘青轴电竞网吧游戏键盘电脑键盘',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN011nFEoP1DrheUNhNgU_!!3435280270-0-cib.jpg',
        79.00, 51, 1, '2022-05-06 15:25:30', NULL, NULL, 0),
       (49, 13, 2, '四川大凉山丑苹果新鲜当季水果冰糖心大苹果现摘现发脆甜',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product2bca55bb5c3adb4d02a7a76039d097eewww800-800.jpg',
        37.00, 38, 1, '2022-05-06 16:04:03', NULL, NULL, 0),
       (50, 13, 3, '云南新鲜小黄姜嫩生姜产地直销',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product4291c68c8352edee6a08255fc8ccbe6fwww755-755.jpg',
        29.00, 38, 1, '2022-05-06 16:04:03', NULL, NULL, 0),
       (51, 13, 5, '广西荔浦芋头新鲜现挖当季香芋新鲜槟榔紫藤芋头农家蔬菜',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product81a4404f4967305eb252b51c19c70669www800-800.jpg',
        37.00, 38, 1, '2022-05-06 16:04:03', NULL, NULL, 0),
       (52, 13, 6, '【海南金煌芒】当季新鲜水果芒果个大肉厚',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productv1_HyUYnSed_70878597530529303160615_30cb18c6c60f2fe869e1aac6ec1996d8_sx_361677_www800-800.jpg',
        43.00, 38, 1, '2022-05-06 16:04:03', NULL, NULL, 0),
       (53, 13, 13, '源百希广西纯水牛奶200ml*10盒学生宿舍儿童早餐营养常温纯水牛奶',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01aeBbww2KcJrsrPVe1_!!0-item_pic.jpg_430x430q90.jpg',
        69.00, 38, 1, '2022-05-06 16:04:03', NULL, NULL, 0),
       (54, 13, 14, 'C6T测体温手环 血压心率血氧睡眠运动计步健康监测 智能手环',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product13495852650_684060882.jpg', 69.00, 53, 1,
        '2022-05-06 16:04:03', NULL, NULL, 0),
       (55, 13, 15, 'SKG K3颈椎按摩仪护颈仪颈部按摩器礼品',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN013l1P561gwqxR2JVXF_!!3319944207-0-cib.jpg',
        259.00, 52, 1, '2022-05-06 16:04:03', NULL, NULL, 0),
       (56, 13, 16, '跨境UM68T全触屏1.69蓝牙手表计步体温检测动态心率睡眠智能手表',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01YEWlgG1DItVQnTuDR_!!2555070194-0-cib.jpg',
        129.00, 53, 1, '2022-05-06 16:04:03', NULL, NULL, 0),
       (57, 13, 17, '跨境私模 typec六合一扩展坞usb hub3.0 hdmi 读卡器笔记本拓展坞',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01N5oBIf1JmYuXN6sMB_!!2212978381071-0-cib.jpg',
        69.00, 50, 1, '2022-05-06 16:04:03', NULL, NULL, 0),
       (58, 13, 18, '柏战K20青轴机械键盘 混光机械键盘青轴电竞网吧游戏键盘电脑键盘',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN011nFEoP1DrheUNhNgU_!!3435280270-0-cib.jpg',
        79.00, 51, 1, '2022-05-06 16:04:03', NULL, NULL, 0),
       (59, 15, 2, '四川大凉山丑苹果新鲜当季水果冰糖心大苹果现摘现发脆甜',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product2bca55bb5c3adb4d02a7a76039d097eewww800-800.jpg',
        37.00, 38, 11, '2022-05-07 11:13:02', NULL, NULL, 0),
       (60, 15, 3, '云南新鲜小黄姜嫩生姜产地直销',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product4291c68c8352edee6a08255fc8ccbe6fwww755-755.jpg',
        29.00, 38, 11, '2022-05-07 11:13:02', NULL, NULL, 0),
       (61, 15, 5, '广西荔浦芋头新鲜现挖当季香芋新鲜槟榔紫藤芋头农家蔬菜',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product81a4404f4967305eb252b51c19c70669www800-800.jpg',
        37.00, 38, 11, '2022-05-07 11:13:02', NULL, NULL, 0),
       (62, 15, 6, '【海南金煌芒】当季新鲜水果芒果个大肉厚',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productv1_HyUYnSed_70878597530529303160615_30cb18c6c60f2fe869e1aac6ec1996d8_sx_361677_www800-800.jpg',
        43.00, 38, 11, '2022-05-07 11:13:02', NULL, NULL, 0),
       (63, 15, 13, '源百希广西纯水牛奶200ml*10盒学生宿舍儿童早餐营养常温纯水牛奶',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01aeBbww2KcJrsrPVe1_!!0-item_pic.jpg_430x430q90.jpg',
        69.00, 38, 11, '2022-05-07 11:13:02', NULL, NULL, 0),
       (64, 15, 14, 'C6T测体温手环 血压心率血氧睡眠运动计步健康监测 智能手环',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product13495852650_684060882.jpg', 69.00, 53, 11,
        '2022-05-07 11:13:02', NULL, NULL, 0),
       (65, 15, 15, 'SKG K3颈椎按摩仪护颈仪颈部按摩器礼品',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN013l1P561gwqxR2JVXF_!!3319944207-0-cib.jpg',
        259.00, 52, 11, '2022-05-07 11:13:02', NULL, NULL, 0),
       (66, 15, 16, '跨境UM68T全触屏1.69蓝牙手表计步体温检测动态心率睡眠智能手表',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01YEWlgG1DItVQnTuDR_!!2555070194-0-cib.jpg',
        129.00, 53, 11, '2022-05-07 11:13:02', NULL, NULL, 0),
       (67, 15, 17, '跨境私模 typec六合一扩展坞usb hub3.0 hdmi 读卡器笔记本拓展坞',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01N5oBIf1JmYuXN6sMB_!!2212978381071-0-cib.jpg',
        69.00, 50, 11, '2022-05-07 11:13:02', NULL, NULL, 0),
       (68, 15, 18, '柏战K20青轴机械键盘 混光机械键盘青轴电竞网吧游戏键盘电脑键盘',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN011nFEoP1DrheUNhNgU_!!3435280270-0-cib.jpg',
        79.00, 51, 11, '2022-05-07 11:13:02', NULL, NULL, 0),
       (69, 16, 2, '四川大凉山丑苹果新鲜当季水果冰糖心大苹果现摘现发脆甜',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product2bca55bb5c3adb4d02a7a76039d097eewww800-800.jpg',
        37.00, 38, 1, '2022-05-07 16:20:15', NULL, NULL, 0),
       (70, 16, 3, '云南新鲜小黄姜嫩生姜产地直销',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product4291c68c8352edee6a08255fc8ccbe6fwww755-755.jpg',
        29.00, 38, 1, '2022-05-07 16:20:15', NULL, NULL, 0),
       (71, 16, 5, '广西荔浦芋头新鲜现挖当季香芋新鲜槟榔紫藤芋头农家蔬菜',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product81a4404f4967305eb252b51c19c70669www800-800.jpg',
        37.00, 38, 1, '2022-05-07 16:20:15', NULL, NULL, 0),
       (72, 16, 13, '源百希广西纯水牛奶200ml*10盒学生宿舍儿童早餐营养常温纯水牛奶',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01aeBbww2KcJrsrPVe1_!!0-item_pic.jpg_430x430q90.jpg',
        69.00, 38, 1, '2022-05-07 16:20:15', NULL, NULL, 0),
       (73, 22, 45, '办公室睡觉午睡午休神器多功能卡通午睡枕',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01I7QAJq27Bfmo14C4Y_!!2209446807759-0-cib.jpg',
        19.00, 41, 1, '2022-05-09 11:01:46', NULL, NULL, 0),
       (74, 22, 46, 'Spring Boot微服务实战| 精通Spring Boot 2.0',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product8288d649f0e94a73.jpg', 56.00, 31, 1,
        '2022-05-09 11:01:46', NULL, NULL, 0),
       (75, 22, 3, '云南新鲜小黄姜嫩生姜产地直销',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product4291c68c8352edee6a08255fc8ccbe6fwww755-755.jpg',
        29.00, 38, 1, '2022-05-09 14:45:58', NULL, NULL, 1),
       (76, 23, 2, '四川大凉山丑苹果新鲜当季水果冰糖心大苹果现摘现发脆甜',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product2bca55bb5c3adb4d02a7a76039d097eewww800-800.jpg',
        37.00, 38, 1, '2022-05-09 15:37:08', NULL, NULL, 0),
       (77, 23, 3, '云南新鲜小黄姜嫩生姜产地直销',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product4291c68c8352edee6a08255fc8ccbe6fwww755-755.jpg',
        29.00, 38, 1, '2022-05-09 15:37:08', NULL, NULL, 0),
       (78, 23, 5, '广西荔浦芋头新鲜现挖当季香芋新鲜槟榔紫藤芋头农家蔬菜',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product81a4404f4967305eb252b51c19c70669www800-800.jpg',
        37.00, 38, 1, '2022-05-09 15:37:08', NULL, NULL, 1),
       (79, 23, 45, '办公室睡觉午睡午休神器多功能卡通午睡枕',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01I7QAJq27Bfmo14C4Y_!!2209446807759-0-cib.jpg',
        19.00, 41, 1, '2022-05-09 15:37:18', NULL, NULL, 1),
       (80, 23, 46, 'Spring Boot微服务实战| 精通Spring Boot 2.0',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product8288d649f0e94a73.jpg', 56.00, 31, 1,
        '2022-05-09 15:37:18', NULL, NULL, 1),
       (81, 23, 47, 'Java设计模式| 高等学校设计模式课程系列教材',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product5af3f085N532313e3.jpg', 56.00, 31, 1,
        '2022-05-09 15:37:18', NULL, NULL, 1),
       (82, 23, 13, '源百希广西纯水牛奶200ml*10盒学生宿舍儿童早餐营养常温纯水牛奶',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01aeBbww2KcJrsrPVe1_!!0-item_pic.jpg_430x430q90.jpg',
        69.00, 38, 1, '2022-05-09 16:29:25', NULL, NULL, 0),
       (83, 26, 50, '机器学习| 周志华 AI人工智能书籍理论导引/公式详解/演化学习',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product945e8503207dec67.jpg', 88.00, 34, 1,
        '2022-05-09 16:50:47', NULL, NULL, 1),
       (84, 26, 2, '四川大凉山丑苹果新鲜当季水果冰糖心大苹果现摘现发脆甜',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product2bca55bb5c3adb4d02a7a76039d097eewww800-800.jpg',
        37.00, 38, 1, '2022-05-09 16:51:43', NULL, NULL, 0),
       (85, 23, 15, 'SKG K3颈椎按摩仪护颈仪颈部按摩器礼品',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN013l1P561gwqxR2JVXF_!!3319944207-0-cib.jpg',
        259.00, 52, 11, '2022-05-10 11:13:03', NULL, NULL, 0),
       (86, 23, 54, '测试商品勿拍', 'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productim.jpg', 0.01, 38, 1,
        '2022-05-10 14:14:53', NULL, NULL, 1),
       (87, 23, 70, '测试lg（勿拍）',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productemre-45ixjp1iDhk-unsplash.jpg', 0.01, 50,
        1, '2022-05-10 14:14:53', NULL, NULL, 1),
       (88, 27, 54, '测试商品勿拍', 'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productim.jpg', 0.01, 38, 1,
        '2022-05-11 15:09:10', NULL, NULL, 1),
       (89, 27, 2, '四川大凉山丑苹果新鲜当季水果冰糖心大苹果现摘现发脆甜',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product2bca55bb5c3adb4d02a7a76039d097eewww800-800.jpg',
        37.00, 38, 1, '2022-05-11 15:09:55', NULL, NULL, 1),
       (90, 27, 3, '云南新鲜小黄姜嫩生姜产地直销',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product4291c68c8352edee6a08255fc8ccbe6fwww755-755.jpg',
        29.00, 38, 1, '2022-05-11 15:09:55', NULL, NULL, 1),
       (91, 27, 5, '广西荔浦芋头新鲜现挖当季香芋新鲜槟榔紫藤芋头农家蔬菜',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product81a4404f4967305eb252b51c19c70669www800-800.jpg',
        37.00, 38, 1, '2022-05-11 15:09:55', NULL, NULL, 1),
       (92, 27, 6, '【海南金煌芒】当季新鲜水果芒果个大肉厚',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productv1_HyUYnSed_70878597530529303160615_30cb18c6c60f2fe869e1aac6ec1996d8_sx_361677_www800-800.jpg',
        43.00, 38, 1, '2022-05-11 15:09:55', NULL, NULL, 1),
       (93, 27, 13, '源百希广西纯水牛奶200ml*10盒学生宿舍儿童早餐营养常温纯水牛奶',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01aeBbww2KcJrsrPVe1_!!0-item_pic.jpg_430x430q90.jpg',
        69.00, 38, 1, '2022-05-11 15:09:55', NULL, NULL, 1),
       (94, 27, 54, '测试商品（拍了不发货嗷）', 'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productim.jpg', 0.01,
        38, 1, '2022-05-11 21:58:17', NULL, NULL, 1),
       (95, 27, 54, '测试商品（拍了不发货嗷）', 'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productim.jpg', 0.01,
        38, 1, '2022-05-12 11:22:45', NULL, NULL, 1),
       (96, 27, 20, '致钛（ZhiTai）长江存储 256GB SSD固态硬盘 SATA 3.0 接口SC001',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01irCALN1MfdkJm9IRV_!!2201483871462-0-cib.jpg',
        207.00, 53, 1, '2022-05-13 17:22:28', NULL, NULL, 1),
       (97, 27, 21, 'TWS迷你私模真无线5.0蓝牙耳机双耳入耳式运动防水降噪',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product23301949399_2075274333.jpg', 56.00, 53, 1,
        '2022-05-13 17:22:28', NULL, NULL, 1),
       (98, 27, 22, '一拖三数据线 三合一手机充电线礼品广告批发 三合一3A伸缩充电线',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01EAi0301nRtWgAXeZX_!!2691675087-0-cib.jpg',
        26.00, 50, 1, '2022-05-13 17:22:28', NULL, NULL, 1),
       (99, 27, 24, '三合一无线充电器 15W快充适用于苹果安卓手机台灯无线充',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN0168vAtO1hiU9uL1Iaz_!!2212725224311-0-cib.jpg',
        36.00, 50, 1, '2022-05-13 17:22:28', NULL, NULL, 1),
       (100, 27, 26, '适用苹果13Magsafe磁吸无线充电宝iPhone12P Max背夹电池移动电源',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01iJPlGn2AC4zqdKQ4n_!!2212219388166-0-cib.jpg',
        46.00, 50, 1, '2022-05-13 17:22:28', NULL, NULL, 1),
       (101, 27, 27, '无线充LED游戏RGB发光鼠标垫大号电竞加厚桌面mousepad',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01iKUQ5Z1rN5oByxPIC_!!2207944265618-0-cib.jpg',
        29.00, 50, 1, '2022-05-13 17:22:28', NULL, NULL, 1),
       (102, 27, 29, '超频三红海迷你mini 电脑cpu散热器 CPU风扇1155/50 AMD 静音8CM',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product13202672331_1682827655.jpg', 37.00, 50, 1,
        '2022-05-13 17:22:28', NULL, NULL, 1),
       (103, 27, 30, '迪欧人体工程学椅办公电脑座椅可躺两用员工久坐人体工学椅子',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01Ve055M1RVdDSWULDL_!!2606852117-0-cib.jpg',
        370.00, 52, 1, '2022-05-13 17:22:28', NULL, NULL, 1),
       (104, 27, 31, '富瑞峰抖音直播桌面支架懒人手机通用折叠支架快手直播手机支架',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01g9sTBP2CPwEYUl5tB_!!2200643208467-0-cib.jpg',
        36.00, 50, 1, '2022-05-13 17:22:28', NULL, NULL, 1),
       (105, 27, 32, '笔记本电脑支架 可以折叠便携铝合金支架 增高散热电脑托新品',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01sxBAAF1Bs2h9Cy1UD_!!0-0-cib.jpg',
        37.00, 52, 1, '2022-05-13 17:22:28', NULL, NULL, 1),
       (106, 27, 33, 'NeSugar小巢糖创意桌面无叶风扇usb台式风扇办公室喷雾风扇冷风机',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01nsp1Wk1FIYRXXUXL8_!!4023510464-0-cib.jpg',
        92.00, 52, 1, '2022-05-13 17:22:37', NULL, NULL, 1),
       (107, 27, 34, '金属手机保护套适用于苹果11苹果12pro金属气囊散热创意礼品',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product22887312428_1060259454.jpg', 65.00, 50, 1,
        '2022-05-13 17:22:37', NULL, NULL, 1),
       (108, 27, 36, '256G存储卡 128GTF卡 16G手机 64G游戏机 32G内存卡',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product19540989545_508406224.jpg', 15.00, 50, 1,
        '2022-05-13 17:22:37', NULL, NULL, 1),
       (109, 27, 38, 'HKC 24英寸V2412显示器高清屏幕IPS面板广视角游戏家用办公显示屏',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01SwMTxU1Mfdkc0rMpP_!!2201483871462-0-cib.jpg',
        795.00, 51, 1, '2022-05-13 17:22:37', NULL, NULL, 0),
       (110, 27, 39, '创意笔筒ins办公皮革收纳筒多功能小收纳盒 桌面礼品',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product3337556256_518870389.jpg', 26.00, 52, 1,
        '2022-05-13 17:22:37', NULL, NULL, 1),
       (111, 27, 40, '精通Spring Cloud微服务架构|Java开发和架构师必备书籍',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product90b234415cb6fbb1.jpg', 78.00, 31, 1,
        '2022-05-13 17:22:37', NULL, NULL, 1),
       (112, 27, 41, '深入理解Java高并发编程| 计算机、系统、软件多层次讲透CPU并发、内核并发、Java并发、线程池',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01ys2lk41v5vQwLg4wb_!!1614846122.jpg_430x430q90.jpg',
        97.00, 31, 1, '2022-05-13 17:22:37', NULL, NULL, 1),
       (113, 27, 42, '微服务设计模式和最佳实践| 微服务开发热门推荐书籍',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN015umQF31sfFA7aXtHW_!!0-item_pic.jpg_430x430q90.jpg',
        72.00, 35, 1, '2022-05-13 17:22:37', NULL, NULL, 1),
       (114, 27, 43, '乐扣保温杯礼品盒定刻印字水杯lock男女304不锈钢便携杯子LHC4019',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01tCaK3K1xt34lfxtDH_!!2206534906500-0-cib.jpg',
        78.00, 39, 1, '2022-05-13 17:22:37', NULL, NULL, 1),
       (115, 27, 44, '热销慢回弹靠垫记忆靠背跨境亚马逊汽车腰靠座椅靠枕2022新品',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01TUkPbt2GpMgUStsJ9_!!2599279064-0-cib.jpg',
        52.00, 40, 1, '2022-05-13 17:22:37', NULL, NULL, 1),
       (116, 27, 54, '测试商品（拍了不发货嗷）', 'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productim.jpg', 0.01,
        38, 1, '2022-05-17 22:19:07', NULL, NULL, 1),
       (117, 27, 76, 'JavaScript百炼成仙 杨逸飞 JavaScript初学入门教材书JavaScript编程入门',
        'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01edWUtV1v5vOlloNXF_!!1614846122.jpg_430x430q90.jpg',
        52.00, 65, 1, '2022-05-18 10:10:31', NULL, NULL, 1),
       (118, 27, 43, '乐扣保温杯礼品盒定刻印字水杯lock男女304不锈钢便携杯子LHC4019',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01G9K1E61xt34rdbBS3_!!2206534906500-0-cib.jpg',
        78.00, 39, 11, '2022-05-25 15:23:41', NULL, NULL, 0),
       (119, 27, 33, 'NeSugar小巢糖创意桌面无叶风扇usb台式风扇办公室喷雾风扇冷风机',
        'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/product精致办公.png', 92.00, 52, 11,
        '2022-05-25 15:24:05', NULL, NULL, 0),
       (120, 27, 34, '金属手机保护套适用于苹果11苹果12pro金属气囊散热创意礼品',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product22887759320_1060259454.jpg', 65.00, 50, 11,
        '2022-05-25 15:24:05', NULL, NULL, 0),
       (121, 27, 36, '256G存储卡 128GTF卡 16G手机 64G游戏机 32G内存卡',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product19494528886_508406224.jpg', 15.00, 50, 11,
        '2022-05-25 15:24:05', NULL, NULL, 0),
       (122, 27, 39, '创意笔筒ins办公皮革收纳筒多功能小收纳盒 桌面礼品',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product11200515170_518870389.jpg', 26.00, 52, 11,
        '2022-05-25 15:24:05', NULL, NULL, 0),
       (123, 27, 2, '四川大凉山丑苹果新鲜当季水果冰糖心大苹果现摘现发脆甜',
        'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/product生活专区2.png', 37.00, 38, 11,
        '2022-05-25 15:24:21', NULL, NULL, 0),
       (124, 27, 3, '云南新鲜小黄姜嫩生姜产地直销',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productea1d1f9c2c7f0e1074f4d8ed82948d7awww800-800.jpg',
        29.00, 38, 11, '2022-05-25 15:24:21', NULL, NULL, 0),
       (125, 27, 5, '广西荔浦芋头新鲜现挖当季香芋新鲜槟榔紫藤芋头农家蔬菜',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product0c6ece9da3183c3930e54fd2cace2e94www1080-1080.jpg',
        37.00, 38, 11, '2022-05-25 15:24:21', NULL, NULL, 0),
       (126, 27, 6, '【海南金煌芒】当季新鲜水果芒果个大肉厚',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productv1_HyUYnSed_70878596610423851920615_adb2fd9c4f7dc7d8778a3f554a8d6fb7_sx_328434_www800-800.jpg',
        43.00, 38, 11, '2022-05-25 15:24:21', NULL, NULL, 0),
       (127, 27, 13, '源百希广西纯水牛奶200ml*10盒学生宿舍儿童早餐营养常温纯水牛奶',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01v6iIYJ2KcJrJ4xSX0_!!2211629699577.jpg_430x430q90.jpg',
        69.00, 38, 11, '2022-05-25 15:24:21', NULL, NULL, 1),
       (128, 27, 14, 'C6T测体温手环 血压心率血氧睡眠运动计步健康监测 智能手环',
        'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/product数码.png', 69.00, 53, 11,
        '2022-05-25 15:24:21', NULL, NULL, 0),
       (129, 27, 15, 'SKG K3颈椎按摩仪护颈仪颈部按摩器礼品',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01pU35b41gwqxaLboWG_!!3319944207-0-cib.jpg',
        259.00, 52, 11, '2022-05-25 15:24:21', NULL, NULL, 1),
       (130, 27, 16, 'UM68T全触屏1.69蓝牙手表计步体温检测动态心率睡眠智能手表',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01YEWlgG1DItVQnTuDR_!!2555070194-0-cib.jpg',
        129.00, 53, 11, '2022-05-25 15:24:21', NULL, NULL, 0),
       (131, 27, 17, '跨境私模 typec六合一扩展坞usb hub3.0 hdmi 读卡器笔记本拓展坞',
        'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/product3c.png', 129.00, 50, 11,
        '2022-05-25 15:24:21', NULL, NULL, 0),
       (132, 27, 20, '致钛（ZhiTai）长江存储 256GB SSD固态硬盘 SATA 3.0 接口SC001',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN0136uPEv1MfdkJmCaAt_!!2201483871462-0-cib.jpg',
        256.00, 53, 11, '2022-05-25 15:24:35', NULL, NULL, 0),
       (133, 27, 21, 'TWS迷你私模真无线5.0蓝牙耳机双耳入耳式运动防水降噪',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product23220067340_2075274333.jpg', 56.00, 53, 11,
        '2022-05-25 15:24:35', NULL, NULL, 1),
       (134, 27, 22, '一拖三数据线 三合一手机充电线礼品广告批发 三合一3A伸缩充电线',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01mSMdUu1nRtROTFWre_!!2691675087-0-cib.jpg',
        26.00, 50, 11, '2022-05-25 15:24:35', NULL, NULL, 1),
       (135, 27, 24, '三合一无线充电器 15W快充适用于苹果安卓手机台灯无线充',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01MI7fdh1hiU9pNWaqJ_!!2212725224311-0-cib.jpg',
        36.00, 50, 11, '2022-05-25 15:24:35', NULL, NULL, 0),
       (136, 27, 26, '适用苹果13Magsafe磁吸无线充电宝iPhone12P Max背夹电池移动电源',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01kO4zK42AC4zhtyDQ2_!!2212219388166-0-cib.jpg',
        46.00, 50, 11, '2022-05-25 15:24:35', NULL, NULL, 0),
       (137, 27, 29, '超频三红海迷你mini 电脑cpu散热器 CPU风扇1155/50 AMD 静音8CM',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product13160104478_1682827655.jpg', 37.00, 50, 11,
        '2022-05-25 15:24:35', NULL, NULL, 0),
       (138, 27, 30, '迪欧人体工程学椅办公电脑座椅可躺两用员工久坐人体工学椅子',
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN01tD9iq51RVdDmbYhTv_!!2606852117-0-cib.jpg',
        370.00, 52, 11, '2022-05-25 15:24:35', NULL, NULL, 0),
       (139, 27, 31, '富瑞峰抖音直播桌面支架懒人手机通用折叠支架快手直播手机支架',
        'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/product3c2.png', 36.00, 50, 11,
        '2022-05-25 15:24:35', NULL, NULL, 0);
/*!40000 ALTER TABLE `activity_product`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activity_product_sku`
--

DROP TABLE IF EXISTS `activity_product_sku`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activity_product_sku`
(
    `id`                  int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `activity_product_id` int(10) unsigned NOT NULL COMMENT '活动商品id',
    `product_id`          int(10) unsigned NOT NULL COMMENT '商品id',
    `product_sku_id`      int(10) unsigned NOT NULL COMMENT '商品sku id',
    `price`               decimal(11, 2)   NOT NULL COMMENT 'sku活动秒杀价格',
    `original_price`      decimal(11, 2)   NOT NULL,
    `number`              int(10) unsigned NOT NULL COMMENT '活动秒杀数量',
    `stock`               int(10) unsigned NOT NULL COMMENT '活动库存',
    `create_user`         int(10) unsigned NOT NULL COMMENT '创建人id',
    `create_time`         datetime         NOT NULL COMMENT '创建时间',
    `update_user`         int(10) unsigned DEFAULT NULL COMMENT '更新人id',
    `update_time`         datetime         DEFAULT NULL COMMENT '更新时间',
    `is_deleted`          tinyint(1)       NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 200
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='活动商品表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_product_sku`
--

LOCK TABLES `activity_product_sku` WRITE;
/*!40000 ALTER TABLE `activity_product_sku`
    DISABLE KEYS */;
INSERT INTO `activity_product_sku`
VALUES (1, 59, 2, 10, 1.00, 37.00, 0, 0, 11, '2022-05-07 11:14:02', NULL, NULL, 0),
       (2, 59, 2, 11, 1.00, 53.00, 0, 0, 11, '2022-05-07 11:14:02', NULL, NULL, 0),
       (3, 59, 2, 12, 1.00, 40.00, 0, 0, 11, '2022-05-07 11:14:02', NULL, NULL, 0),
       (4, 59, 2, 13, 1.00, 66.00, 0, 0, 11, '2022-05-07 11:14:02', NULL, NULL, 0),
       (5, 59, 2, 14, 1.00, 88.00, 0, 0, 11, '2022-05-07 11:14:02', NULL, NULL, 0),
       (6, 73, 45, 153, 15.90, 19.00, 200, 200, 1, '2022-05-09 15:16:36', NULL, NULL, 0),
       (7, 73, 45, 154, 15.90, 19.00, 100, 100, 1, '2022-05-09 15:16:36', NULL, NULL, 0),
       (8, 73, 45, 155, 15.90, 19.00, 100, 100, 1, '2022-05-09 15:16:36', NULL, NULL, 0),
       (9, 73, 45, 156, 15.90, 19.00, 100, 100, 1, '2022-05-09 15:16:36', NULL, NULL, 0),
       (10, 73, 45, 157, 15.90, 19.00, 100, 100, 1, '2022-05-09 15:16:36', NULL, NULL, 0),
       (11, 73, 45, 158, 15.90, 19.00, 100, 100, 1, '2022-05-09 15:16:36', NULL, NULL, 0),
       (12, 76, 2, 10, 19.90, 37.00, 99, 98, 1, '2022-05-09 15:39:58', 1, '2022-05-09 15:53:44', 0),
       (13, 76, 2, 11, 19.90, 53.00, 100, 100, 1, '2022-05-09 15:39:58', 1, '2022-05-09 15:53:44', 0),
       (14, 76, 2, 12, 19.90, 40.00, 100, 100, 1, '2022-05-09 15:39:58', 1, '2022-05-09 15:53:44', 0),
       (15, 76, 2, 13, 19.90, 66.00, 100, 100, 1, '2022-05-09 15:39:58', 1, '2022-05-09 15:53:44', 0),
       (16, 76, 2, 14, 19.90, 88.00, 100, 99, 1, '2022-05-09 15:39:58', 1, '2022-05-09 15:53:44', 0),
       (17, 83, 50, 163, 0.01, 88.00, 100, 100, 1, '2022-05-09 16:50:55', 1, '2022-05-09 16:51:07', 0),
       (18, 84, 2, 10, 19.90, 37.00, 97, 97, 1, '2022-05-09 16:51:59', 1, '2022-05-10 19:25:06', 0),
       (19, 84, 2, 11, 19.90, 53.00, 97, 97, 1, '2022-05-09 16:51:59', 1, '2022-05-10 19:25:06', 0),
       (20, 84, 2, 12, 19.90, 40.00, 98, 98, 1, '2022-05-09 16:51:59', 1, '2022-05-10 19:25:06', 0),
       (21, 84, 2, 13, 19.90, 66.00, 99, 99, 1, '2022-05-09 16:51:59', 1, '2022-05-10 19:25:06', 0),
       (22, 84, 2, 14, 19.90, 88.00, 99, 99, 1, '2022-05-09 16:51:59', 1, '2022-05-10 19:25:06', 0),
       (23, 82, 13, 184, 65.00, 69.00, 5, 5, 11, '2022-05-10 10:41:46', 8, '2022-05-10 19:23:45', 0),
       (26, 77, 3, 130, 28.88, 29.00, 20, 20, 11, '2022-05-10 10:45:09', NULL, NULL, 0),
       (27, 77, 3, 131, 38.88, 39.00, 20, 20, 11, '2022-05-10 10:45:09', NULL, NULL, 0),
       (28, 77, 3, 132, 68.88, 69.00, 20, 20, 11, '2022-05-10 10:45:09', NULL, NULL, 0),
       (31, 85, 15, 29, 300.00, 259.00, 1, 1, 11, '2022-05-10 11:16:39', NULL, NULL, 0),
       (32, 85, 15, 30, 300.00, 319.00, 0, 0, 11, '2022-05-10 11:16:39', NULL, NULL, 0),
       (33, 85, 15, 31, 300.00, 359.00, 0, 0, 11, '2022-05-10 11:16:39', NULL, NULL, 0),
       (34, 86, 54, 4, 0.01, 0.01, 50000, 35773, 1, '2022-05-10 14:17:21', 1, '2022-05-10 16:43:37', 0),
       (35, 86, 54, 5, 0.01, 0.02, 50000, 36026, 1, '2022-05-10 14:17:21', 1, '2022-05-10 16:43:37', 0),
       (36, 86, 54, 6, 0.01, 0.03, 50000, 35804, 1, '2022-05-10 14:17:21', 1, '2022-05-10 16:43:37', 0),
       (37, 86, 54, 7, 0.01, 0.04, 50000, 35865, 1, '2022-05-10 14:17:21', 1, '2022-05-10 16:43:37', 0),
       (38, 86, 54, 8, 0.01, 0.05, 50000, 36016, 1, '2022-05-10 14:17:21', 1, '2022-05-10 16:43:37', 0),
       (39, 86, 54, 9, 0.01, 0.06, 50000, 35949, 1, '2022-05-10 14:17:21', 1, '2022-05-10 16:43:37', 0),
       (40, 88, 54, 4, 0.01, 0.01, 134783, 123832, 1, '2022-05-11 15:10:14', NULL, NULL, 0),
       (41, 88, 54, 5, 0.01, 0.02, 134783, 123662, 1, '2022-05-11 15:10:14', NULL, NULL, 0),
       (42, 88, 54, 6, 0.01, 0.03, 134783, 123832, 1, '2022-05-11 15:10:14', NULL, NULL, 0),
       (43, 88, 54, 7, 0.01, 0.04, 134783, 123771, 1, '2022-05-11 15:10:14', NULL, NULL, 0),
       (44, 88, 54, 8, 0.01, 0.05, 134783, 123750, 1, '2022-05-11 15:10:14', NULL, NULL, 0),
       (45, 88, 54, 9, 0.01, 0.06, 134626, 123539, 1, '2022-05-11 15:10:14', NULL, NULL, 0),
       (46, 89, 2, 10, 19.90, 37.00, 97, 97, 1, '2022-05-11 15:10:28', NULL, NULL, 0),
       (47, 89, 2, 11, 19.90, 53.00, 97, 97, 1, '2022-05-11 15:10:28', NULL, NULL, 0),
       (48, 89, 2, 12, 19.90, 40.00, 98, 98, 1, '2022-05-11 15:10:28', NULL, NULL, 0),
       (49, 89, 2, 13, 19.90, 66.00, 99, 99, 1, '2022-05-11 15:10:28', NULL, NULL, 0),
       (50, 89, 2, 14, 19.90, 88.00, 99, 99, 1, '2022-05-11 15:10:28', NULL, NULL, 0),
       (51, 90, 3, 130, 19.90, 29.00, 100, 99, 1, '2022-05-11 15:10:40', NULL, NULL, 0),
       (52, 90, 3, 131, 19.90, 39.00, 100, 100, 1, '2022-05-11 15:10:40', NULL, NULL, 0),
       (53, 90, 3, 132, 19.90, 69.00, 100, 100, 1, '2022-05-11 15:10:40', NULL, NULL, 0),
       (54, 94, 54, 4, 0.01, 0.01, 54265, 52820, 1, '2022-05-11 21:58:40', NULL, NULL, 0),
       (55, 94, 54, 5, 0.01, 0.02, 111111, 109698, 1, '2022-05-11 21:58:40', NULL, NULL, 0),
       (56, 94, 54, 6, 0.01, 0.03, 111111, 109649, 1, '2022-05-11 21:58:40', NULL, NULL, 0),
       (57, 94, 54, 7, 0.01, 0.04, 111111, 109717, 1, '2022-05-11 21:58:40', NULL, NULL, 0),
       (58, 94, 54, 8, 0.01, 0.05, 111111, 109699, 1, '2022-05-11 21:58:40', NULL, NULL, 0),
       (59, 94, 54, 9, 0.01, 0.06, 111111, 109731, 1, '2022-05-11 21:58:40', NULL, NULL, 0),
       (60, 95, 54, 4, 0.01, 0.01, 50000, 50003, 1, '2022-05-12 11:23:06', 1, '2022-05-17 20:02:33', 0),
       (61, 95, 54, 5, 0.02, 0.02, 50000, 49999, 1, '2022-05-12 11:23:06', 1, '2022-05-17 20:02:33', 0),
       (62, 95, 54, 6, 0.03, 0.03, 50000, 50000, 1, '2022-05-12 11:23:06', 1, '2022-05-17 20:02:33', 0),
       (63, 95, 54, 7, 0.04, 0.04, 50000, 50000, 1, '2022-05-12 11:23:06', 1, '2022-05-17 20:02:33', 0),
       (64, 95, 54, 8, 0.05, 0.05, 50000, 50000, 1, '2022-05-12 11:23:06', 1, '2022-05-17 20:02:33', 0),
       (65, 95, 54, 9, 0.06, 0.06, 50000, 50000, 1, '2022-05-12 11:23:06', 1, '2022-05-17 20:02:33', 0),
       (66, 91, 5, 117, 20.00, 37.00, 2, 2, 1, '2022-05-13 11:25:33', 11, '2022-05-17 19:26:42', 0),
       (67, 91, 5, 118, 25.00, 66.00, 2, 2, 1, '2022-05-13 11:25:33', 11, '2022-05-17 19:26:42', 0),
       (68, 91, 5, 119, 20.00, 41.00, 3, 3, 1, '2022-05-13 11:25:33', 11, '2022-05-17 19:26:42', 0),
       (69, 91, 5, 120, 50.00, 76.00, 2, 2, 1, '2022-05-13 11:25:33', 11, '2022-05-17 19:26:42', 0),
       (70, 93, 13, 184, 68.00, 69.00, 10, 10, 11, '2022-05-17 19:27:02', NULL, NULL, 0),
       (71, 96, 20, 40, 200.00, 207.00, 10, 10, 11, '2022-05-17 19:27:20', NULL, NULL, 0),
       (72, 100, 26, 51, 46.00, 46.00, 1, 1, 11, '2022-05-17 19:28:00', NULL, NULL, 0),
       (73, 100, 26, 52, 49.00, 49.00, 1, 1, 11, '2022-05-17 19:28:00', NULL, NULL, 0),
       (74, 100, 26, 53, 45.00, 46.00, 1, 1, 11, '2022-05-17 19:28:00', NULL, NULL, 0),
       (75, 100, 26, 54, 50.00, 51.00, 1, 1, 11, '2022-05-17 19:28:00', NULL, NULL, 0),
       (76, 99, 24, 49, 35.00, 36.00, 10, 10, 11, '2022-05-17 19:28:12', NULL, NULL, 0),
       (77, 99, 24, 50, 35.00, 36.00, 10, 10, 11, '2022-05-17 19:28:12', NULL, NULL, 0),
       (78, 98, 22, 45, 20.00, 26.00, 10, 10, 11, '2022-05-17 19:28:26', NULL, NULL, 0),
       (79, 98, 22, 46, 20.00, 26.00, 10, 10, 11, '2022-05-17 19:28:26', NULL, NULL, 0),
       (80, 98, 22, 47, 25.00, 26.00, 10, 10, 11, '2022-05-17 19:28:26', NULL, NULL, 0),
       (81, 98, 22, 48, 25.00, 26.00, 10, 10, 11, '2022-05-17 19:28:26', NULL, NULL, 0),
       (82, 97, 21, 41, 55.00, 56.00, 10, 10, 11, '2022-05-17 19:28:38', NULL, NULL, 0),
       (83, 97, 21, 42, 55.00, 56.00, 10, 10, 11, '2022-05-17 19:28:38', NULL, NULL, 0),
       (84, 97, 21, 43, 55.00, 56.00, 10, 10, 11, '2022-05-17 19:28:38', NULL, NULL, 0),
       (85, 97, 21, 44, 55.00, 56.00, 10, 10, 11, '2022-05-17 19:28:38', NULL, NULL, 0),
       (86, 115, 44, 150, 50.00, 52.00, 10, 10, 11, '2022-05-17 19:29:09', NULL, NULL, 0),
       (87, 115, 44, 151, 50.00, 52.00, 10, 10, 11, '2022-05-17 19:29:09', NULL, NULL, 0),
       (88, 115, 44, 152, 50.00, 52.00, 10, 10, 11, '2022-05-17 19:29:09', NULL, NULL, 0),
       (89, 113, 42, 143, 70.00, 72.00, 10, 10, 11, '2022-05-17 19:29:17', NULL, NULL, 0),
       (90, 92, 6, 19, 60.00, 61.00, 10, 10, 11, '2022-05-17 19:30:52', NULL, NULL, 0),
       (91, 92, 6, 20, 60.00, 65.00, 10, 10, 11, '2022-05-17 19:30:52', NULL, NULL, 0),
       (92, 92, 6, 21, 40.00, 43.00, 1, 1, 11, '2022-05-17 19:30:52', NULL, NULL, 0),
       (93, 92, 6, 22, 45.00, 46.00, 10, 10, 11, '2022-05-17 19:30:52', NULL, NULL, 0),
       (94, 92, 6, 23, 88.00, 90.00, 10, 10, 11, '2022-05-17 19:30:52', NULL, NULL, 0),
       (95, 103, 30, 60, 366.00, 370.00, 0, 0, 11, '2022-05-17 19:31:28', NULL, NULL, 0),
       (96, 103, 30, 61, 488.00, 490.00, 7, 7, 11, '2022-05-17 19:31:28', NULL, NULL, 0),
       (97, 103, 30, 62, 788.00, 789.00, 5, 5, 11, '2022-05-17 19:31:28', NULL, NULL, 0),
       (98, 104, 31, 68, 35.00, 36.00, 5, 5, 11, '2022-05-17 19:31:46', NULL, NULL, 0),
       (99, 104, 31, 69, 35.00, 36.00, 4, 4, 11, '2022-05-17 19:31:46', NULL, NULL, 0),
       (100, 105, 32, 73, 36.00, 37.00, 5, 5, 11, '2022-05-17 19:31:53', NULL, NULL, 0),
       (101, 106, 33, 76, 95.00, 96.00, 0, 0, 11, '2022-05-17 19:32:05', NULL, NULL, 0),
       (102, 106, 33, 77, 90.00, 92.00, 5, 5, 11, '2022-05-17 19:32:05', NULL, NULL, 0),
       (103, 107, 34, 78, 60.00, 65.00, 10, 10, 11, '2022-05-17 19:32:29', NULL, NULL, 0),
       (104, 107, 34, 79, 60.00, 65.00, 10, 10, 11, '2022-05-17 19:32:29', NULL, NULL, 0),
       (105, 107, 34, 80, 60.00, 65.00, 10, 10, 11, '2022-05-17 19:32:29', NULL, NULL, 0),
       (106, 107, 34, 81, 60.00, 65.00, 10, 10, 11, '2022-05-17 19:32:29', NULL, NULL, 0),
       (107, 107, 34, 82, 60.00, 65.00, 10, 10, 11, '2022-05-17 19:32:29', NULL, NULL, 0),
       (108, 111, 40, 141, 75.00, 78.00, 10, 10, 11, '2022-05-17 19:32:37', NULL, NULL, 0),
       (109, 109, 38, 133, 788.00, 795.00, 10, 10, 11, '2022-05-17 19:33:10', NULL, NULL, 0),
       (110, 109, 38, 134, 850.00, 859.00, 10, 10, 11, '2022-05-17 19:33:10', NULL, NULL, 0),
       (111, 112, 41, 142, 96.00, 97.00, 199, 199, 1, '2022-05-17 22:16:49', NULL, NULL, 0),
       (112, 116, 54, 4, 0.01, 0.01, 50000, 50000, 1, '2022-05-17 22:19:24', NULL, NULL, 0),
       (113, 116, 54, 5, 0.01, 0.02, 50000, 50000, 1, '2022-05-17 22:19:24', NULL, NULL, 0),
       (114, 116, 54, 6, 0.01, 0.03, 50000, 50000, 1, '2022-05-17 22:19:24', NULL, NULL, 0),
       (115, 116, 54, 7, 0.01, 0.04, 50000, 50000, 1, '2022-05-17 22:19:24', NULL, NULL, 0),
       (116, 116, 54, 8, 0.01, 0.05, 50000, 50000, 1, '2022-05-17 22:19:24', NULL, NULL, 0),
       (117, 116, 54, 9, 0.01, 0.06, 50000, 50000, 1, '2022-05-17 22:19:24', NULL, NULL, 0),
       (118, 117, 76, 267, 0.00, 52.00, 0, 0, 1, '2022-05-18 10:10:47', NULL, NULL, 0),
       (119, 118, 43, 144, 50.00, 78.00, 30, 30, 11, '2022-05-25 15:27:28', NULL, NULL, 0),
       (120, 118, 43, 145, 50.00, 78.00, 30, 30, 11, '2022-05-25 15:27:28', NULL, NULL, 0),
       (121, 118, 43, 146, 50.00, 78.00, 30, 30, 11, '2022-05-25 15:27:28', NULL, NULL, 0),
       (122, 118, 43, 147, 50.00, 78.00, 30, 30, 11, '2022-05-25 15:27:28', NULL, NULL, 0),
       (123, 118, 43, 148, 50.00, 78.00, 30, 30, 11, '2022-05-25 15:27:28', NULL, NULL, 0),
       (124, 118, 43, 149, 50.00, 78.00, 30, 30, 11, '2022-05-25 15:27:28', NULL, NULL, 0),
       (125, 119, 33, 76, 80.00, 96.00, 0, 0, 11, '2022-05-25 15:27:51', NULL, NULL, 0),
       (126, 119, 33, 77, 80.00, 92.00, 10, 10, 11, '2022-05-25 15:27:51', NULL, NULL, 0),
       (127, 120, 34, 78, 45.00, 65.00, 45, 45, 11, '2022-05-25 15:28:15', NULL, NULL, 0),
       (128, 120, 34, 79, 45.00, 65.00, 45, 45, 11, '2022-05-25 15:28:15', NULL, NULL, 0),
       (129, 120, 34, 80, 45.00, 65.00, 45, 45, 11, '2022-05-25 15:28:15', NULL, NULL, 0),
       (130, 120, 34, 81, 45.00, 65.00, 45, 45, 11, '2022-05-25 15:28:15', NULL, NULL, 0),
       (131, 120, 34, 82, 45.00, 65.00, 45, 45, 11, '2022-05-25 15:28:15', NULL, NULL, 0),
       (132, 121, 36, 83, 10.00, 15.00, 10, 10, 11, '2022-05-25 15:29:06', NULL, NULL, 0),
       (133, 121, 36, 84, 15.00, 18.00, 15, 15, 11, '2022-05-25 15:29:06', NULL, NULL, 0),
       (134, 121, 36, 85, 15.00, 21.00, 15, 15, 11, '2022-05-25 15:29:06', NULL, NULL, 0),
       (135, 121, 36, 86, 15.00, 23.00, 15, 15, 11, '2022-05-25 15:29:06', NULL, NULL, 0),
       (136, 121, 36, 87, 15.00, 28.00, 15, 15, 11, '2022-05-25 15:29:06', NULL, NULL, 0),
       (137, 121, 36, 88, 15.00, 18.00, 15, 15, 11, '2022-05-25 15:29:06', NULL, NULL, 0),
       (138, 121, 36, 89, 15.00, 20.00, 15, 15, 11, '2022-05-25 15:29:06', NULL, NULL, 0),
       (139, 121, 36, 90, 15.00, 23.00, 15, 15, 11, '2022-05-25 15:29:06', NULL, NULL, 0),
       (140, 121, 36, 91, 15.00, 25.00, 15, 15, 11, '2022-05-25 15:29:06', NULL, NULL, 0),
       (141, 121, 36, 92, 15.00, 32.00, 15, 15, 11, '2022-05-25 15:29:06', NULL, NULL, 0),
       (142, 121, 36, 93, 15.00, 76.00, 15, 15, 11, '2022-05-25 15:29:06', NULL, NULL, 0),
       (143, 121, 36, 94, 150.00, 165.00, 15, 15, 11, '2022-05-25 15:29:06', NULL, NULL, 0),
       (144, 121, 36, 95, 300.00, 359.00, 15, 15, 11, '2022-05-25 15:29:06', NULL, NULL, 0),
       (145, 122, 39, 135, 20.00, 26.00, 20, 20, 11, '2022-05-25 15:29:24', NULL, NULL, 0),
       (146, 122, 39, 136, 20.00, 26.00, 20, 20, 11, '2022-05-25 15:29:24', NULL, NULL, 0),
       (147, 122, 39, 137, 20.00, 26.00, 20, 20, 11, '2022-05-25 15:29:24', NULL, NULL, 0),
       (148, 122, 39, 138, 20.00, 26.00, 20, 20, 11, '2022-05-25 15:29:24', NULL, NULL, 0),
       (149, 122, 39, 139, 20.00, 26.00, 20, 20, 11, '2022-05-25 15:29:24', NULL, NULL, 0),
       (150, 122, 39, 140, 20.00, 26.00, 20, 20, 11, '2022-05-25 15:29:24', NULL, NULL, 0),
       (151, 123, 2, 10, 30.00, 37.00, 10, 10, 11, '2022-05-25 15:29:46', NULL, NULL, 0),
       (152, 123, 2, 11, 50.00, 53.00, 10, 10, 11, '2022-05-25 15:29:46', NULL, NULL, 0),
       (153, 123, 2, 12, 36.00, 40.00, 10, 10, 11, '2022-05-25 15:29:46', NULL, NULL, 0),
       (154, 123, 2, 13, 50.00, 66.00, 10, 10, 11, '2022-05-25 15:29:46', NULL, NULL, 0),
       (155, 123, 2, 14, 70.00, 88.00, 10, 10, 11, '2022-05-25 15:29:46', NULL, NULL, 0),
       (156, 124, 3, 130, 26.00, 29.00, 10, 10, 11, '2022-05-25 15:30:03', NULL, NULL, 0),
       (157, 124, 3, 131, 30.00, 39.00, 10, 10, 11, '2022-05-25 15:30:03', NULL, NULL, 0),
       (158, 124, 3, 132, 60.00, 69.00, 10, 10, 11, '2022-05-25 15:30:03', NULL, NULL, 0),
       (159, 125, 5, 117, 30.00, 37.00, 0, 0, 11, '2022-05-25 15:30:16', NULL, NULL, 0),
       (160, 125, 5, 118, 60.00, 66.00, 0, 0, 11, '2022-05-25 15:30:16', NULL, NULL, 0),
       (161, 125, 5, 119, 38.00, 41.00, 0, 0, 11, '2022-05-25 15:30:16', NULL, NULL, 0),
       (162, 125, 5, 120, 70.00, 76.00, 0, 0, 11, '2022-05-25 15:30:16', NULL, NULL, 0),
       (163, 127, 13, 184, 60.00, 69.00, 5, 5, 11, '2022-05-25 15:30:25', NULL, NULL, 0),
       (164, 126, 6, 19, 58.00, 61.00, 10, 10, 11, '2022-05-25 15:30:45', NULL, NULL, 0),
       (165, 126, 6, 20, 60.00, 65.00, 10, 10, 11, '2022-05-25 15:30:45', NULL, NULL, 0),
       (166, 126, 6, 21, 40.00, 43.00, 10, 10, 11, '2022-05-25 15:30:45', NULL, NULL, 0),
       (167, 126, 6, 22, 40.00, 46.00, 10, 10, 11, '2022-05-25 15:30:45', NULL, NULL, 0),
       (168, 126, 6, 23, 80.00, 90.00, 10, 10, 11, '2022-05-25 15:30:45', NULL, NULL, 0),
       (169, 128, 14, 25, 60.00, 69.00, 60, 60, 11, '2022-05-25 15:30:59', NULL, NULL, 0),
       (170, 128, 14, 26, 60.00, 69.00, 60, 60, 11, '2022-05-25 15:30:59', NULL, NULL, 0),
       (171, 128, 14, 27, 60.00, 69.00, 60, 60, 11, '2022-05-25 15:30:59', NULL, NULL, 0),
       (172, 128, 14, 28, 60.00, 69.00, 60, 60, 11, '2022-05-25 15:30:59', NULL, NULL, 0),
       (173, 129, 15, 29, 200.00, 259.00, 10, 10, 11, '2022-05-25 15:31:14', NULL, NULL, 0),
       (174, 129, 15, 30, 300.00, 319.00, 10, 10, 11, '2022-05-25 15:31:14', NULL, NULL, 0),
       (175, 129, 15, 31, 320.00, 359.00, 10, 10, 11, '2022-05-25 15:31:14', NULL, NULL, 0),
       (176, 130, 16, 32, 100.00, 129.00, 10, 10, 11, '2022-05-25 15:31:31', NULL, NULL, 0),
       (177, 130, 16, 33, 100.00, 129.00, 10, 10, 11, '2022-05-25 15:31:31', NULL, NULL, 0),
       (178, 131, 17, 185, 100.00, 129.00, 10, 10, 11, '2022-05-25 15:31:39', NULL, NULL, 0),
       (179, 132, 20, 289, 240.00, 256.00, 10, 10, 11, '2022-05-25 15:31:48', NULL, NULL, 0),
       (180, 133, 21, 41, 50.00, 56.00, 10, 10, 11, '2022-05-25 15:32:01', NULL, NULL, 0),
       (181, 133, 21, 42, 50.00, 56.00, 10, 10, 11, '2022-05-25 15:32:01', NULL, NULL, 0),
       (182, 133, 21, 43, 50.00, 56.00, 10, 10, 11, '2022-05-25 15:32:01', NULL, NULL, 0),
       (183, 133, 21, 44, 50.00, 56.00, 10, 10, 11, '2022-05-25 15:32:01', NULL, NULL, 0),
       (184, 134, 22, 45, 20.00, 26.00, 10, 10, 11, '2022-05-25 15:32:14', NULL, NULL, 0),
       (185, 134, 22, 46, 20.00, 26.00, 10, 10, 11, '2022-05-25 15:32:14', NULL, NULL, 0),
       (186, 134, 22, 47, 20.00, 26.00, 10, 10, 11, '2022-05-25 15:32:14', NULL, NULL, 0),
       (187, 134, 22, 48, 20.00, 26.00, 10, 10, 11, '2022-05-25 15:32:14', NULL, NULL, 0),
       (188, 139, 31, 68, 32.00, 36.00, 10, 10, 11, '2022-05-25 15:32:25', NULL, NULL, 0),
       (189, 139, 31, 69, 32.00, 36.00, 10, 10, 11, '2022-05-25 15:32:25', NULL, NULL, 0),
       (190, 138, 30, 60, 350.00, 370.00, 0, 0, 11, '2022-05-25 15:32:42', NULL, NULL, 0),
       (191, 138, 30, 61, 470.00, 490.00, 4, 4, 11, '2022-05-25 15:32:42', NULL, NULL, 0),
       (192, 138, 30, 62, 750.00, 789.00, 6, 6, 11, '2022-05-25 15:32:42', NULL, NULL, 0),
       (193, 137, 29, 59, 34.00, 37.00, 1, 1, 11, '2022-05-25 15:32:48', NULL, NULL, 0),
       (194, 136, 26, 51, 44.00, 46.00, 67, 67, 11, '2022-05-25 15:33:08', NULL, NULL, 0),
       (195, 136, 26, 52, 45.00, 49.00, 7, 7, 11, '2022-05-25 15:33:08', NULL, NULL, 0),
       (196, 136, 26, 53, 42.00, 46.00, 8, 8, 11, '2022-05-25 15:33:08', NULL, NULL, 0),
       (197, 136, 26, 54, 47.00, 51.00, 3, 3, 11, '2022-05-25 15:33:08', NULL, NULL, 0),
       (198, 135, 24, 49, 30.00, 36.00, 5, 5, 11, '2022-05-25 15:33:18', NULL, NULL, 0),
       (199, 135, 24, 50, 30.00, 36.00, 6, 6, 11, '2022-05-25 15:33:18', NULL, NULL, 0);
/*!40000 ALTER TABLE `activity_product_sku`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activity_time`
--

DROP TABLE IF EXISTS `activity_time`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activity_time`
(
    `id`          int(10) unsigned                       NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `activity_id` int(10) unsigned                       NOT NULL COMMENT '活动表id',
    `time_name`   varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '时段名称',
    `start_time`  time                                   NOT NULL COMMENT '时段开始时间',
    `end_time`    time                                   NOT NULL COMMENT '时段结束时间',
    `is_enable`   tinyint(1)       DEFAULT NULL COMMENT '是否启用',
    `create_user` int(10) unsigned                       NOT NULL COMMENT '创建人id',
    `create_time` datetime                               NOT NULL COMMENT '创建时间',
    `update_user` int(10) unsigned DEFAULT NULL COMMENT '更新人id',
    `update_time` datetime         DEFAULT NULL COMMENT '更新时间',
    `is_deleted`  tinyint(1)                             NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 28
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='活动时段表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_time`
--

LOCK TABLES `activity_time` WRITE;
/*!40000 ALTER TABLE `activity_time`
    DISABLE KEYS */;
INSERT INTO `activity_time`
VALUES (1, 2, '123', '10:22:00', '10:22:00', 1, 14, '2022-05-06 10:22:07', NULL, NULL, 0),
       (2, 5, '111', '11:25:10', '11:25:10', 0, 14, '2022-05-06 11:25:14', 1, '2022-05-07 16:43:05', 0),
       (3, 4, '123', '11:26:00', '11:26:00', 1, 14, '2022-05-06 11:26:05', NULL, NULL, 0),
       (4, 1, '1', '11:27:51', '11:27:51', 0, 14, '2022-05-06 11:27:54', 1, '2022-05-07 16:43:52', 0),
       (5, 1, '2', '11:27:55', '11:27:55', 0, 14, '2022-05-06 11:27:58', 1, '2022-05-07 16:43:53', 0),
       (6, 1, '3', '11:32:34', '11:32:34', 0, 1, '2022-05-06 11:32:39', 1, '2022-05-07 16:43:54', 0),
       (7, 4, '13213', '11:49:59', '11:49:59', 1, 1, '2022-05-06 11:50:08', NULL, NULL, 0),
       (8, 5, '12321', '14:32:00', '14:32:00', 0, 1, '2022-05-06 14:32:07', 1, '2022-05-07 16:43:02', 0),
       (9, 1, '11', '14:38:19', '17:38:19', 0, 1, '2022-05-06 14:38:30', 1, '2022-05-07 16:43:54', 0),
       (10, 1, '1', '15:33:03', '15:33:03', 0, 14, '2022-05-06 15:33:09', 1, '2022-05-07 16:43:55', 0),
       (11, 5, '11', '15:38:20', '23:38:20', 0, 1, '2022-05-06 15:38:30', 1, '2022-05-07 16:43:02', 0),
       (12, 5, '啊啊啊啊1111888', '16:01:09', '20:03:09', 0, 1, '2022-05-06 16:03:29', 1, '2022-05-07 16:43:01', 0),
       (13, 5, ' 首付多少分多少', '16:04:41', '21:03:41', 0, 1, '2022-05-06 16:03:57', 1, '2022-05-07 16:43:00', 0),
       (14, 1, '2321', '18:08:33', '18:08:33', 0, 1, '2022-05-06 18:08:44', 1, '2022-05-07 16:43:55', 0),
       (15, 5, '时间', '11:11:05', '11:11:05', 0, 11, '2022-05-07 11:11:22', 1, '2022-05-07 16:42:59', 0),
       (16, 7, '321321321', '16:19:12', '20:19:12', 0, 1, '2022-05-07 16:19:42', 1, '2022-05-07 16:44:01', 0),
       (17, 7, '321321321', '16:23:46', '18:23:46', 0, 1, '2022-05-07 16:23:53', 1, '2022-05-07 16:44:02', 0),
       (18, 5, '123123', '16:34:52', '16:34:52', 0, 1, '2022-05-07 16:35:02', 1, '2022-05-07 16:42:58', 0),
       (19, 5, '2232', '16:35:42', '16:35:42', 0, 1, '2022-05-07 16:35:49', 1, '2022-05-07 16:42:57', 0),
       (20, 10, 'cscs', '10:46:35', '19:59:35', 1, 1, '2022-05-09 10:47:00', NULL, NULL, 1),
       (21, 10, 'sdf', '12:59:32', '17:59:32', 1, 1, '2022-05-09 10:59:49', NULL, NULL, 1),
       (22, 11, '321321', '11:01:23', '11:23:23', 1, 1, '2022-05-09 11:01:38', 1, '2022-05-09 16:20:20', 0),
       (23, 10, '15点场', '11:18:10', '22:50:10', 1, 1, '2022-05-09 15:36:36', 1, '2022-05-10 19:41:57', 0),
       (24, 10, 'asd1122a', '16:00:00', '17:52:04', 1, 1, '2022-05-09 15:38:18', 1, '2022-05-09 16:48:26', 1),
       (25, 10, '12321321', '16:49:00', '18:49:00', 1, 1, '2022-05-09 16:49:06', 1, '2022-05-09 16:49:22', 1),
       (26, 10, '16点场', '16:49:27', '23:59:30', 0, 1, '2022-05-09 16:49:34', 1, '2022-05-10 19:41:58', 0),
       (27, 15, '15点秒杀专场', '06:00:19', '23:29:19', 1, 1, '2022-05-11 10:29:32', 11, '2022-05-19 00:25:47', 0);
/*!40000 ALTER TABLE `activity_time`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `advertisement`
--

DROP TABLE IF EXISTS `advertisement`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `advertisement`
(
    `id`                       int(10) unsigned                        NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name`                     varchar(50) COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '广告名称',
    `platform`                 tinyint(4)                              NOT NULL COMMENT '广告平台（1-移动端 2-PC端）',
    `location`                 tinyint(4)                              NOT NULL COMMENT '广告位置（1-首页轮播 2-首页金刚区 3-商品分类广告）',
    `product_category_id`      int(10) unsigned                                 DEFAULT NULL COMMENT '商品分类id',
    `picture_url`              varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '广告图片地址',
    `advertisement_start_time` datetime                                NOT NULL COMMENT '广告开始时间',
    `advertisement_end_time`   datetime                                NOT NULL COMMENT '广告结束时间',
    `jump_type`                tinyint(4)                              NOT NULL COMMENT '跳转类型（1-商品详情 2-分类商品列表 3-链接 4-不跳转）',
    `jump_url`                 varchar(255) COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '跳转地址',
    `is_enable`                tinyint(4)                              NOT NULL COMMENT '启用状态（0-未启用 1-已启用）',
    `sort`                     int(11)                                          DEFAULT NULL COMMENT '排序',
    `is_deleted`               tinyint(4)                              NOT NULL DEFAULT '0' COMMENT '删除状态（0-未删除 1-已删除）',
    `create_time`              datetime                                         DEFAULT NULL COMMENT '创建时间',
    `create_user`              int(10) unsigned                                 DEFAULT NULL COMMENT '创建人',
    `update_time`              datetime                                         DEFAULT NULL COMMENT '更新时间',
    `update_user`              int(10) unsigned                                 DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 114
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='广告';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `advertisement`
--

LOCK TABLES `advertisement` WRITE;
/*!40000 ALTER TABLE `advertisement`
    DISABLE KEYS */;
INSERT INTO `advertisement`
VALUES (79, 'PCcs1111', 2, 1, NULL, 'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/advertise/444.png',
        '2022-06-01 18:06:35', '9999-06-02 00:00:00', 1, '74', 0, 0, 1, '2022-06-01 18:06:35', 1, NULL, NULL),
       (80, '人体工程学椅-PC', 2, 1, NULL, 'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/advertise/1.png',
        '2022-06-01 18:16:25', '9999-06-02 00:00:00', 1, '30', 1, -1, 1, '2022-06-01 18:16:25', 1,
        '2022-06-02 10:15:29', 1),
       (81, 'SKG颈椎按摩器-PC', 2, 1, NULL,
        'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/advertise/2banner.png', '2022-06-01 18:22:32',
        '9999-06-02 00:00:00', 1, '15', 1, -2, 1, '2022-06-01 18:22:32', 1, '2022-06-02 09:47:33', 1),
       (82, '微服务架构书-PC', 2, 1, NULL,
        'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/advertise/3banner.png', '2022-06-01 18:24:28',
        '9999-06-02 00:00:00', 3, 'https://you.mashibing.com/goods/detail/40', 1, -3, 1, '2022-06-01 18:24:28', 1,
        '2022-06-02 09:44:06', 1),
       (83, '牛奶-PC', 2, 1, NULL, 'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/advertise/banner4.jpg',
        '2022-06-01 18:24:55', '9999-06-02 00:00:00', 1, '13', 1, -4, 1, '2022-06-01 18:24:55', 1,
        '2022-06-02 09:43:47', 1),
       (84, '大型架构分享课-PC', 2, 1, NULL,
        'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/advertise/banner6.jpg', '2022-06-02 10:19:42',
        '9999-06-03 00:00:00', 3, 'https://www.mashibing.com/live/1530', 1, -5, 1, '2022-06-02 10:19:42', 1, NULL,
        NULL),
       (85, '马士兵严选项目-PC', 2, 1, NULL,
        'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/advertise/banner5.jpg', '2022-06-02 10:20:23',
        '9999-06-03 00:00:00', 3, 'https://www.mashibing.com/free/1373', 1, -6, 1, '2022-06-02 10:20:23', 1, NULL,
        NULL),
       (86, '高并发-PC', 2, 1, NULL,
        'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/advertise/PC-深入理解Java高并发编程-banner(1200).jpg',
        '2022-06-02 10:29:06', '9999-06-03 00:00:00', 1, '40', 1, -7, 0, '2022-06-02 10:29:06', 1,
        '2022-06-02 18:11:23', 1),
       (87, '按摩仪-PC', 2, 1, NULL,
        'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/advertise/PC-按摩器banner(1200).jpg',
        '2022-06-02 10:30:42', '9999-06-03 00:00:00', 1, '15', 1, -8, 0, '2022-06-02 10:30:42', 1,
        '2022-06-02 18:11:39', 1),
       (88, '人体工程学椅-PC', 2, 1, NULL,
        'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/advertise/PC-人体工程椅banner(1200).jpg',
        '2022-06-02 10:32:18', '9999-06-03 00:00:00', 1, '30', 1, -9, 0, '2022-06-02 10:32:18', 1,
        '2022-06-02 18:11:50', 1),
       (89, '牛奶-PC', 2, 1, NULL,
        'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/advertise/PC-牛奶banner(1200).jpg',
        '2022-06-02 10:33:08', '9999-06-03 00:00:00', 1, '13', 1, -10, 0, '2022-06-02 10:33:08', 1,
        '2022-06-02 18:11:58', 1),
       (90, '马士兵严选商城项目', 2, 1, NULL,
        'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/advertise/严选banner（1200x360）.jpg',
        '2022-06-02 10:33:43', '9999-06-03 00:00:00', 3, 'https://www.mashibing.com/free/1373', 1, -11, 0,
        '2022-06-02 10:33:43', 1, '2022-06-02 18:12:06', 1),
       (91, '大型架构分享课-PC', 2, 1, NULL,
        'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/advertise/马士兵严选大型架构分享课(1200x360).jpg',
        '2022-06-02 10:34:50', '9999-06-03 00:00:00', 3, 'https://www.mashibing.com/live/1530', 1, -12, 0,
        '2022-06-02 10:34:50', 1, '2022-06-02 18:12:14', 1),
       (92, '高并发-APP', 1, 1, NULL,
        'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/advertise/3banner.png', '2022-06-02 10:37:22',
        '9999-06-03 00:00:00', 1, '40', 1, 0, 0, '2022-06-02 10:37:22', 1, NULL, NULL),
       (93, '按摩仪-APP', 1, 1, NULL,
        'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/advertise/2banner.png', '2022-06-02 10:41:42',
        '9999-06-03 00:00:00', 1, '15', 1, -1, 0, '2022-06-02 10:41:42', 1, '2022-06-02 11:17:53', 1),
       (94, '人体工程学椅-APP', 1, 1, NULL, 'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/advertise/1.png',
        '2022-06-02 10:42:38', '9999-06-03 00:00:00', 1, '30', 1, -2, 0, '2022-06-02 10:42:38', 1, NULL, NULL),
       (95, '牛奶-APP', 1, 1, NULL, 'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/advertise/banner4.jpg',
        '2022-06-02 10:48:41', '9999-06-03 00:00:00', 1, '13', 1, -3, 0, '2022-06-02 10:48:41', 1, NULL, NULL),
       (96, '马士兵严选商城项目-APP', 1, 1, NULL,
        'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/advertise/banner5.jpg', '2022-06-02 10:49:19',
        '9999-06-03 00:00:00', 3, 'https://m.mashibing.com/course/1373/1/', 1, -4, 0, '2022-06-02 10:49:19', 1, NULL,
        NULL),
       (97, '大型架构分享课-APP', 1, 1, NULL,
        'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/advertise/banner6.jpg', '2022-06-02 10:49:50',
        '9999-06-03 00:00:00', 3, 'https://m.mashibing.com/live/1530', 1, -5, 0, '2022-06-02 10:49:50', 1, NULL, NULL),
       (98, '网络安全', 1, 2, NULL, 'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/advertise/网络安全.png',
        '2022-06-02 10:54:26', '9999-06-03 00:00:00', 2, '36', 1, 0, 0, '2022-06-02 10:54:26', 1, '2022-06-02 14:22:48',
        1),
       (99, '入门必备', 1, 2, NULL, 'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/advertise/入门必备.png',
        '2022-06-02 10:55:00', '9999-06-03 00:00:00', 2, '35', 1, -1, 0, '2022-06-02 10:55:00', 1,
        '2022-06-02 14:23:13', 1),
       (100, 'AI书籍', 1, 2, NULL, 'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/advertise/AI书籍.png',
        '2022-06-02 10:55:34', '9999-06-03 00:00:00', 2, '34', 1, -2, 0, '2022-06-02 10:55:34', 1,
        '2022-06-02 14:23:31', 1),
       (101, 'Python书籍', 1, 2, NULL,
        'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/advertise/Python书籍.png', '2022-06-02 10:56:05',
        '9999-06-03 00:00:00', 2, '33', 1, -3, 0, '2022-06-02 10:56:05', 1, '2022-06-02 14:25:37', 1),
       (102, 'Java书籍', 1, 2, NULL, 'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/advertise/Java书籍.png',
        '2022-06-02 10:56:31', '9999-06-03 00:00:00', 2, '31', 1, -4, 0, '2022-06-02 10:56:31', 1,
        '2022-06-02 14:25:48', 1),
       (103, '生活专区', 1, 2, NULL, 'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/advertise/生活专区.png',
        '2022-06-02 10:59:00', '9999-06-03 00:00:00', 2, '5', 1, -5, 0, '2022-06-02 10:59:00', 1, '2022-06-02 14:26:24',
        1),
       (104, '精致办公', 1, 2, NULL, 'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/advertise/精致办公.png',
        '2022-06-02 10:59:26', '9999-06-03 00:00:00', 2, '4', 1, -6, 0, '2022-06-02 10:59:26', 1, '2022-06-02 14:26:17',
        1),
       (105, '程序员外设', 1, 2, NULL, 'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/advertise/程序员外设.png',
        '2022-06-02 10:59:51', '9999-06-03 00:00:00', 2, '3', 1, -7, 0, '2022-06-02 10:59:51', 1, '2022-06-02 14:26:12',
        1),
       (106, '3C配件', 1, 2, NULL, 'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/advertise/3C配件.png',
        '2022-06-02 11:00:19', '9999-06-03 00:00:00', 2, '2', 1, -8, 0, '2022-06-02 11:00:19', 1, '2022-06-02 14:26:05',
        1),
       (107, '数码周边', 1, 2, NULL, 'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/advertise/数码周边.png',
        '2022-06-02 11:00:38', '9999-06-03 00:00:00', 2, '1', 1, -9, 0, '2022-06-02 11:00:38', 1, '2022-06-02 14:25:57',
        1),
       (108, '专业书籍', 1, 3, 6, 'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/advertise/专业书籍分类.jpg',
        '2022-06-02 11:06:50', '9999-06-03 00:00:00', 4, NULL, 1, 0, 0, '2022-06-02 11:06:50', 1, NULL, NULL),
       (109, '数码周边', 1, 3, 1, 'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/advertise/数码周边分类.jpg',
        '2022-06-02 11:07:29', '9999-06-03 00:00:00', 4, NULL, 1, -1, 0, '2022-06-02 11:07:29', 1, NULL, NULL),
       (110, '3C配件', 1, 3, 2, 'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/advertise/3C配件分类.jpg',
        '2022-06-02 11:07:52', '9999-06-03 00:00:00', 4, NULL, 1, -2, 0, '2022-06-02 11:07:52', 1, NULL, NULL),
       (111, '程序员外设', 1, 3, 3, 'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/advertise/程序员外设分类.jpg',
        '2022-06-02 11:08:13', '9999-06-03 00:00:00', 4, NULL, 1, -3, 0, '2022-06-02 11:08:13', 1, NULL, NULL),
       (112, '精致办公', 1, 3, 4, 'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/advertise/精致办公分类.jpg',
        '2022-06-02 11:08:34', '9999-06-03 00:00:00', 4, NULL, 1, -4, 0, '2022-06-02 11:08:34', 1, NULL, NULL),
       (113, '生活专区', 1, 3, 5, 'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/advertise/生活专区分类.jpg',
        '2022-06-02 11:08:55', '9999-06-03 00:00:00', 4, NULL, 1, -5, 0, '2022-06-02 11:08:55', 1, NULL, NULL);
/*!40000 ALTER TABLE `advertisement`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `app_message_push`
--

DROP TABLE IF EXISTS `app_message_push`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `app_message_push`
(
    `id`           int(10) unsigned                        NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `title`        varchar(50) COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '消息标题',
    `content`      varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '消息内容',
    `link_jump`    varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '链接跳转',
    `release_time` datetime         DEFAULT NULL COMMENT '发布时间',
    `create_user`  int(10) unsigned                        NOT NULL COMMENT '创建人id',
    `create_time`  datetime                                NOT NULL COMMENT '创建时间',
    `update_user`  int(10) unsigned DEFAULT NULL COMMENT '更新人id',
    `update_time`  datetime         DEFAULT NULL COMMENT '更新时间',
    `is_deleted`   tinyint(1)                              NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='app消息推送';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `app_message_push`
--

LOCK TABLES `app_message_push` WRITE;
/*!40000 ALTER TABLE `app_message_push`
    DISABLE KEYS */;
INSERT INTO `app_message_push`
VALUES (1, '1', '1', '1', NULL, 1, '2022-05-09 17:24:56', NULL, NULL, 0),
       (2, '223', '3232', 'https://www.baidu.com/', NULL, 1, '2022-05-09 20:09:14', NULL, NULL, 0),
       (3, '321321', '312321', 'https://www.baidu.com/', NULL, 1, '2022-05-09 20:09:37', NULL, NULL, 0);
/*!40000 ALTER TABLE `app_message_push`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_recommended`
--

DROP TABLE IF EXISTS `product_recommended`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_recommended`
(
    `id`                   int(10) unsigned                        NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `product_id`           int(10) unsigned                        NOT NULL COMMENT '商品id',
    `product_main_picture` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品主图',
    `product_name`         varchar(50) COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '商品名称',
    `is_enable`            tinyint(1)                              NOT NULL COMMENT '是否开启 1开启，0未开启',
    `create_user`          int(10) unsigned                        NOT NULL COMMENT '创建人id',
    `create_time`          datetime                                NOT NULL COMMENT '创建时间',
    `update_user`          int(10) unsigned                                 DEFAULT NULL COMMENT '更新人id',
    `update_time`          datetime                                         DEFAULT NULL COMMENT '更新时间',
    `is_deleted`           tinyint(1)                              NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 19
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='商品推荐表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_recommended`
--

LOCK TABLES `product_recommended` WRITE;
/*!40000 ALTER TABLE `product_recommended`
    DISABLE KEYS */;
INSERT INTO `product_recommended`
VALUES (7, 40, 'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product90b234415cb6fbb1.jpg',
        '精通Spring Cloud微服务架构|Java开发和架构师必备书籍', 0, 0, '2022-05-06 17:41:51', 11, '2022-05-07 21:49:56', 0),
       (8, 41, 'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/product深入理解Java高并发编程-书影-1000.jpg',
        '深入理解Java高并发编程| 计算机、系统、软件多层次讲透CPU并发、内核并发、Java并发、线程池', 1, 0, '2022-05-06 19:55:04', 0, '2022-05-13 19:52:16', 0),
       (9, 42,
        'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/productO1CN015umQF31sfFA7aXtHW_!!0-item_pic.jpg_430x430q90.jpg',
        '微服务设计模式和最佳实践| 微服务开发热门推荐书籍', 0, 0, '2022-05-06 20:07:59', 11, '2022-05-07 21:49:58', 0),
       (10, 48, 'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/mall-product/product0700b461dcb5f705.jpg',
        'Python爬虫技术：深入理解原理、技术与开发/宁哥大讲堂', 0, 0, '2022-05-06 20:51:35', 11, '2022-05-07 21:49:58', 0),
       (11, 51, 'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/mall-product/product1000X1000.jpg',
        '项目驱动零起点学Java 百万程序员Java学习经验总结', 1, 0, '2022-05-06 21:07:12', 0, '2022-05-13 19:55:04', 0);
/*!40000 ALTER TABLE `product_recommended`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `undo_log`
--

DROP TABLE IF EXISTS `undo_log`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `undo_log`
(
    `branch_id`     bigint(20)   NOT NULL COMMENT 'branch transaction id',
    `xid`           varchar(128) NOT NULL COMMENT 'global transaction id',
    `context`       varchar(128) NOT NULL COMMENT 'undo_log context,such as serialization',
    `rollback_info` longblob     NOT NULL COMMENT 'rollback info',
    `log_status`    int(11)      NOT NULL COMMENT '0:normal status,1:defense status',
    `log_created`   datetime(6)  NOT NULL COMMENT 'create datetime',
    `log_modified`  datetime(6)  NOT NULL COMMENT 'modify datetime',
    UNIQUE KEY `ux_undo_log` (`xid`, `branch_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='AT transaction mode undo table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `undo_log`
--

LOCK TABLES `undo_log` WRITE;
/*!40000 ALTER TABLE `undo_log`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `undo_log`
    ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;

-- Dump completed on 2022-07-19 16:26:52
