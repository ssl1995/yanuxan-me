-- MySQL dump 10.13  Distrib 5.7.36, for Linux (x86_64)
--
-- Host: localhost    Database: user_center
-- ------------------------------------------------------
-- Server version	5.7.36-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `department`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT,
    `name`        varchar(255) NOT NULL COMMENT '部门名称',
    `chain_id`    varchar(255)          DEFAULT NULL COMMENT '部门链',
    `parent_id`   bigint(20)            DEFAULT '0' COMMENT '上级部门ID',
    `level`       tinyint(2)   NOT NULL DEFAULT '1' COMMENT '1:一级 2：二级 3：三级 4：四级',
    `is_enable`   tinyint(2)   NOT NULL DEFAULT '0' COMMENT '0:可用 1：删除',
    `create_time` datetime     NOT NULL COMMENT '创建时间',
    `update_time` datetime              DEFAULT NULL COMMENT '修改时间',
    `create_user` bigint(20)            DEFAULT NULL COMMENT '创建用户',
    `update_user` bigint(20)            DEFAULT NULL COMMENT '修改用户',
    `is_deleted`  tinyint(1)   NOT NULL COMMENT '逻辑删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 8
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC COMMENT ='部门表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `department`
--

LOCK TABLES `department` WRITE;
/*!40000 ALTER TABLE `department`
    DISABLE KEYS */;
INSERT INTO `department`
VALUES (2, '马士兵教育', '2', 0, 1, 1, '2022-05-12 11:33:57', '2022-05-12 11:40:29', 8, 8, 0),
       (3, '研发部', '2-3', 2, 1, 1, '2022-05-12 11:42:13', '2022-05-13 17:11:58', 8, 15, 0);
/*!40000 ALTER TABLE `department`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `department_role`
--

DROP TABLE IF EXISTS `department_role`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `department_role`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT,
    `role_id`       bigint(20) NOT NULL COMMENT '角色ID',
    `department_id` bigint(20) NOT NULL COMMENT '部门ID',
    `system_id`     bigint(20) NOT NULL COMMENT '系统ID',
    `create_time`   datetime   NOT NULL COMMENT '创建时间',
    `update_time`   datetime   DEFAULT NULL COMMENT '修改时间',
    `create_user`   bigint(20) DEFAULT NULL COMMENT '创建用户',
    `update_user`   bigint(20) DEFAULT NULL COMMENT '修改用户',
    `is_deleted`    tinyint(1) NOT NULL COMMENT '逻辑删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 9
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC COMMENT ='部门角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `department_role`
--

LOCK TABLES `department_role` WRITE;
/*!40000 ALTER TABLE `department_role`
    DISABLE KEYS */;
INSERT INTO `department_role`
VALUES (1, 1, 2, 2, '2022-05-12 17:23:33', NULL, 8, NULL, 0),
       (2, 2, 2, 2, '2022-05-12 17:51:54', NULL, 8, NULL, 0),
       (3, 3, 2, 1, '2022-06-10 17:29:07', NULL, 11, NULL, 0),
       (4, 4, 2, 1, '2022-06-20 22:42:57', NULL, 10, NULL, 0),
       (8, 5, 2, 1, '2022-06-21 14:08:24', NULL, 6, NULL, 0);
/*!40000 ALTER TABLE `department_role`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `employee`
(
    `id`            bigint(20)                             NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `user_id`       bigint(20)                             NOT NULL COMMENT '用户id',
    `user_name`     varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
    `phone`         varchar(13) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '手机号',
    `employee_name` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '员工姓名',
    `employee_type` tinyint(1)                             NOT NULL COMMENT '员工类型',
    `password`      varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '密码',
    `email`         varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '邮箱',
    `remark`        varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
    `is_enable`     tinyint(1)                             NOT NULL COMMENT '是否启用 1，启用 0 禁用',
    `create_time`   datetime                               NOT NULL,
    `update_time`   datetime                                DEFAULT NULL,
    `create_user`   bigint(20)                             NOT NULL,
    `update_user`   bigint(20)                              DEFAULT NULL,
    `is_deleted`    tinyint(1)                             NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 14
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='员工表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee`
    DISABLE KEYS */;
INSERT INTO `employee`
VALUES (1, 1, 'admin', 'admin', 'admin', 1, '123456', '123', '123', 1, '2022-05-05 12:00:00', '2022-06-30 20:48:12', 0,
        1, 0);
/*!40000 ALTER TABLE `employee`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_department`
--

DROP TABLE IF EXISTS `employee_department`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `employee_department`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT,
    `employee_id`   bigint(20) NOT NULL COMMENT '员工Id',
    `department_id` bigint(20) NOT NULL COMMENT '部门id',
    `create_time`   datetime   NOT NULL,
    `update_time`   datetime   DEFAULT NULL,
    `create_user`   bigint(20) NOT NULL,
    `update_user`   bigint(20) DEFAULT NULL,
    `is_deleted`    tinyint(1) NOT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 10
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC COMMENT ='员工部门表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_department`
--

LOCK TABLES `employee_department` WRITE;
/*!40000 ALTER TABLE `employee_department`
    DISABLE KEYS */;
INSERT INTO `employee_department`
VALUES (3, 8, 2, '2022-05-21 20:07:45', NULL, 1, NULL, 0),
       (4, 1, 4, '2022-05-27 18:01:13', NULL, 1, NULL, 0),
       (5, 9, 2, '2022-05-27 18:06:08', NULL, 1, NULL, 0),
       (6, 10, 2, '2022-05-27 18:06:21', NULL, 1, NULL, 0),
       (7, 11, 2, '2022-05-27 18:07:37', NULL, 1, NULL, 0),
       (8, 12, 3, '2022-06-14 18:01:35', NULL, 1, NULL, 0),
       (9, 13, 3, '2022-06-14 18:02:15', NULL, 1, NULL, 0);
/*!40000 ALTER TABLE `employee_department`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_role`
--

DROP TABLE IF EXISTS `employee_role`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `employee_role`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT,
    `employee_id` bigint(20) NOT NULL COMMENT '员工Id',
    `role_id`     bigint(20) NOT NULL COMMENT '角色Id',
    `create_time` datetime   NOT NULL,
    `update_time` datetime   DEFAULT NULL,
    `create_user` bigint(20) NOT NULL,
    `update_user` bigint(20) DEFAULT NULL,
    `is_deleted`  tinyint(1) NOT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 92
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC COMMENT ='系统角色权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_role`
--

LOCK TABLES `employee_role` WRITE;
/*!40000 ALTER TABLE `employee_role`
    DISABLE KEYS */;
INSERT INTO `employee_role`
VALUES (8, 3, 1, '2022-05-25 15:10:43', NULL, 10, NULL, 0),
       (9, 5, 1, '2022-05-25 15:42:43', NULL, 1, NULL, 0),
       (12, 9, 1, '2022-05-27 18:06:08', NULL, 1, NULL, 0),
       (15, 10, 1, '2022-05-27 18:06:22', NULL, 1, NULL, 0),
       (18, 11, 1, '2022-05-27 18:07:37', NULL, 1, NULL, 0),
       (19, 6, 1, '2022-05-27 18:10:44', NULL, 1, NULL, 0),
       (21, 9, 3, '2022-06-10 17:29:07', NULL, 11, NULL, 0),
       (22, 10, 3, '2022-06-10 17:29:07', NULL, 11, NULL, 0),
       (23, 11, 3, '2022-06-10 17:29:07', NULL, 11, NULL, 0),
       (25, 9, 3, '2022-06-10 17:29:07', NULL, 11, NULL, 0),
       (26, 10, 3, '2022-06-10 17:29:07', NULL, 11, NULL, 0),
       (27, 11, 3, '2022-06-10 17:29:07', NULL, 11, NULL, 0),
       (37, 13, 1, '2022-06-14 18:28:38', NULL, 19, NULL, 0),
       (38, 13, 2, '2022-06-14 18:28:38', NULL, 19, NULL, 0),
       (39, 13, 3, '2022-06-14 18:28:38', NULL, 19, NULL, 0),
       (41, 9, 4, '2022-06-20 22:42:57', NULL, 10, NULL, 0),
       (42, 10, 4, '2022-06-20 22:42:57', NULL, 10, NULL, 0),
       (43, 11, 4, '2022-06-20 22:42:57', NULL, 10, NULL, 0),
       (45, 9, 4, '2022-06-20 22:42:57', NULL, 10, NULL, 0),
       (46, 10, 4, '2022-06-20 22:42:57', NULL, 10, NULL, 0),
       (47, 11, 4, '2022-06-20 22:42:57', NULL, 10, NULL, 0),
       (67, 9, 5, '2022-06-21 14:08:24', NULL, 6, NULL, 0),
       (68, 10, 5, '2022-06-21 14:08:24', NULL, 6, NULL, 0),
       (69, 11, 5, '2022-06-21 14:08:24', NULL, 6, NULL, 0),
       (74, 8, 1, '2022-06-21 14:21:45', NULL, 6, NULL, 0),
       (75, 8, 2, '2022-06-21 14:21:45', NULL, 6, NULL, 0),
       (76, 8, 3, '2022-06-21 14:21:45', NULL, 6, NULL, 0),
       (77, 8, 5, '2022-06-21 14:21:45', NULL, 6, NULL, 0),
       (78, 4, 1, '2022-06-21 14:22:12', NULL, 6, NULL, 0),
       (85, 1, 1, '2022-06-30 20:48:12', NULL, 1, NULL, 0),
       (86, 1, 2, '2022-06-30 20:48:12', NULL, 1, NULL, 0),
       (87, 2, 1, '2022-06-30 20:51:36', NULL, 1, NULL, 0),
       (88, 2, 3, '2022-06-30 20:51:36', NULL, 1, NULL, 0),
       (89, 12, 1, '2022-06-30 20:51:51', NULL, 1, NULL, 0),
       (90, 12, 2, '2022-06-30 20:51:51', NULL, 1, NULL, 0),
       (91, 12, 3, '2022-06-30 20:51:51', NULL, 1, NULL, 0);
/*!40000 ALTER TABLE `employee_role`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu`
--

DROP TABLE IF EXISTS `menu`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `menu`
(
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `system_id`   bigint(20)  NOT NULL COMMENT '系统id',
    `parent_id`   bigint(20)  NOT NULL COMMENT '父级菜单',
    `title`       varchar(20) NOT NULL COMMENT '菜单标题',
    `permission`  varchar(50) NOT NULL COMMENT '菜单权限标识',
    `type`        tinyint(1)  NOT NULL COMMENT '菜单类型',
    `page`        varchar(50) NOT NULL COMMENT '前端页面',
    `path`        varchar(50) NOT NULL COMMENT '前端路由',
    `icon`        varchar(50) NOT NULL COMMENT '菜单图标',
    `sort`        int(4)      NOT NULL COMMENT '排序',
    `active_menu` varchar(50) DEFAULT NULL,
    `is_enable`   tinyint(1)  NOT NULL COMMENT '是否开启',
    `create_time` datetime    NOT NULL,
    `update_time` datetime    DEFAULT NULL,
    `create_user` bigint(20)  NOT NULL,
    `update_user` bigint(20)  DEFAULT NULL,
    `is_deleted`  tinyint(1)  NOT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 68
  DEFAULT CHARSET = utf8mb4
  AVG_ROW_LENGTH = 564
  ROW_FORMAT = DYNAMIC COMMENT ='菜单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu`
--

LOCK TABLES `menu` WRITE;
/*!40000 ALTER TABLE `menu`
    DISABLE KEYS */;
INSERT INTO `menu`
VALUES (1, 2, 0, '员工管理', 'EmployeeManagement', 1, '/system/employee/index.vue', 'time/:id', 'Avatar', 0, NULL, 1,
        '2022-05-12 11:14:51', NULL, 8, NULL, 0),
       (2, 1, 0, '设置', 'ChatManagement', 0, 'Layout', '/chat', 'settings-3-fill', 60, '', 1, '2022-05-18 10:56:32',
        '2022-05-21 14:27:14', 2, 15, 0),
       (3, 1, 2, '会话聊天', 'ChatSession', 0, 'chat', 'session', '', 0, NULL, 1, '2022-05-18 11:00:44',
        '2022-05-18 11:22:05', 2, 2, 1),
       (4, 1, 50, '聊天会话', 'ChatSession', 1, 'chat', 'session', 'wechat-2-fill', 10, '', 1, '2022-05-20 09:54:10',
        '2022-06-14 18:17:26', 2, 527, 0),
       (5, 1, 2, '客服管理', 'CustomerServiceManagement', 0, 'chat/management', 'management', 'chat-smile-2-fill', 20, NULL,
        1, '2022-05-20 09:55:16', NULL, 2, NULL, 1),
       (6, 1, 0, '促销', 'OperationCenter', 0, 'Layout', '/operation', 'computer-fill', 40, '', 1, '2022-05-20 09:56:00',
        '2022-05-21 14:23:52', 2, 15, 0),
       (7, 1, 6, '秒杀活动', 'LimitActivity', 1, 'operation/limit', 'limit', 'auction', 10, '', 1, '2022-05-20 09:56:30',
        '2022-05-21 14:13:08', 2, 15, 0),
       (8, 1, 7, '设置时段', 'UpdateLimitTime', 2, 'operation/limit/time', 'time/:id', 'fire-fill', 10, NULL, 1,
        '2022-05-20 09:59:11', NULL, 2, NULL, 0),
       (9, 1, 7, '设置商品', 'UpdateLimitProduct', 2, 'operation/limit/product', 'product/:id', 'fire-fill', 20, NULL, 1,
        '2022-05-20 09:59:37', NULL, 2, NULL, 0),
       (10, 1, 6, '推荐专区', 'RecommandActivity', 1, 'operation/recommend', 'recommend', 'fire', 20, '', 1,
        '2022-05-20 10:00:12', '2022-05-21 14:24:06', 2, 15, 0),
       (11, 1, 0, '权限中台', 'UserCenter', 0, 'Layout', '/uc', 'admin-fill', 80, '', 1, '2022-05-20 10:09:22',
        '2022-05-21 14:28:43', 2, 15, 0),
       (12, 1, 11, '组织架构', 'DeptManagement', 1, 'permission/dept', 'dept', 'organization-chart', 10, NULL, 1,
        '2022-05-20 10:09:49', NULL, 2, NULL, 0),
       (13, 1, 11, '菜单管理', 'MenuManagement', 0, 'permission/menu', 'menu', 'menu-2', 20, NULL, 1, '2022-05-20 10:10:17',
        NULL, 2, NULL, 0),
       (14, 1, 11, '角色管理', 'RoleManagement', 0, 'permission/role', 'role', 'account-box-fill', 30, NULL, 1,
        '2022-05-20 10:10:39', NULL, 2, NULL, 0),
       (15, 1, 11, '员工管理', 'EmployeeManagement', 0, 'permission/employee', 'employee', 'account-box', 40, '', 1,
        '2022-05-20 10:11:07', '2022-05-21 14:10:21', 2, 15, 0),
       (16, 1, 15, '创建员工', 'CreateEmployee', 2, 'permission/employee/form', 'create', 'Avatar', 10, NULL, 1,
        '2022-05-20 10:11:30', '2022-05-20 15:30:21', 2, 15, 0),
       (17, 1, 15, '编辑员工', 'UpdateEmployee', 2, 'permission/employee/form', 'update/:id', 'Avatar', 20, NULL, 1,
        '2022-05-20 10:11:59', '2022-05-20 15:30:24', 2, 15, 0),
       (18, 1, 0, '订单', 'SalesCenter', 0, 'Layout', '/sales', 'money-dollar-circle-fill', 20, '', 1,
        '2022-05-20 10:14:26', '2022-05-21 14:21:43', 2, 15, 0),
       (19, 1, 18, '订单列表', 'OrderManagement', 1, 'sales/order', 'order', 'Checked', 10, '', 1, '2022-05-20 10:15:58',
        '2022-05-21 14:20:52', 2, 15, 0),
       (20, 1, 19, '订单详情', 'OrderDetail', 2, 'sales/order/detail', 'detail/:id', 'Checked', 10, NULL, 1,
        '2022-05-20 10:16:31', NULL, 2, NULL, 0),
       (21, 1, 18, '售后申请处理', 'ServiceApplication', 0, 'sales/service', 'service', 'barcode-box-fill', 20, '', 1,
        '2022-05-20 10:16:58', '2022-05-21 14:21:01', 2, 15, 0),
       (22, 1, 21, '售后详情', 'ServiceDetail', 2, 'sales/service/detail', 'detail/:id', 'barcode-box-fill', 10, NULL, 1,
        '2022-05-20 10:17:28', NULL, 2, NULL, 0),
       (23, 1, 32, '商品分类', 'CategoryManagement', 1, 'sales/category', 'category', 'TakeawayBox', 20, '', 1,
        '2022-05-20 10:17:53', '2022-05-21 14:20:12', 2, 15, 0),
       (24, 1, 23, '创建分类', 'CreateCategory', 2, 'sales/category/form', 'create', 'TakeawayBox', 10, NULL, 1,
        '2022-05-20 10:18:19', '2022-05-20 10:18:45', 2, 2, 0),
       (25, 1, 23, '编辑分类', 'UpdateCategory', 2, 'sales/category/form', 'update/:id', 'TakeawayBox', 20, NULL, 1,
        '2022-05-20 10:18:41', NULL, 2, NULL, 0),
       (26, 1, 32, '商品列表', 'ProductManagement', 1, 'sales/product', 'management', 'MessageBox', 10, '', 1,
        '2022-05-20 10:19:13', '2022-05-21 14:19:50', 2, 15, 0),
       (27, 1, 26, '创建商品', 'CreateProduct', 2, 'sales/product/form', 'create', 'MessageBox', 10, NULL, 1,
        '2022-05-20 10:19:50', NULL, 2, NULL, 0),
       (28, 1, 26, '编辑商品', 'UpdateProduct', 2, 'sales/product/form', 'update/:id/:step?', 'MessageBox', 20, NULL, 1,
        '2022-05-20 10:20:13', NULL, 2, NULL, 0),
       (29, 1, 0, '运营', 'SystemManagement', 0, 'Layout', '/system', 'base-station', 50, '', 1, '2022-05-20 11:00:22',
        '2022-05-21 14:28:09', 2, 15, 0),
       (30, 1, 34, '用户列表', 'CustomerManagement', 1, 'system/customer', 'customer', 'user-1', 10, '', 1,
        '2022-05-20 11:00:45', '2022-05-21 14:23:24', 2, 15, 0),
       (31, 1, 29, '消息管理', 'NotifyManagement', 1, 'system/notify', 'notify', 'notification-2', 20, '', 1,
        '2022-05-20 11:01:07', '2022-05-25 16:13:35', 2, 14, 0),
       (32, 1, 0, '商品', 'Product', 0, 'Layout', 'product', 'product-hunt-fill', 10, '', 1, '2022-05-21 14:16:27',
        '2022-06-10 18:06:12', 15, 14, 0),
       (33, 1, 32, '商品列表', 'ProductManagement', 1, 'sales/product', 'management', 'MessageBox', 10, '', 1,
        '2022-05-21 14:18:45', NULL, 15, NULL, 1),
       (34, 1, 0, '用户', 'Customer', 0, 'Layout', '/customer', 'user-5-fill', 30, '', 1, '2022-05-21 14:22:51',
        '2022-05-21 14:44:14', 15, 15, 0),
       (35, 1, 0, 'IM中台', 'IM', 0, 'Layout', 'im', 'wechat-2-fill', 70, '', 1, '2022-05-21 14:36:08', NULL, 15, NULL,
        0),
       (36, 1, 29, '广告管理', 'AdvertiseManagement', 1, 'operation/advertise', 'advertise', 'advertisement', 20, '', 1,
        '2022-05-25 15:46:04', '2022-05-25 15:46:50', 14, 14, 0),
       (37, 1, 36, '创建广告', 'CreateAdvertise', 2, 'operation/advertise/form', 'create', 'advertisement', 10, '', 1,
        '2022-05-25 16:28:15', NULL, 14, NULL, 0),
       (38, 1, 36, '编辑广告', 'UpdateAdvertise', 2, 'operation/advertise/form', 'update/:id', 'advertisement', 20, '', 1,
        '2022-05-25 16:29:01', NULL, 14, NULL, 0),
       (40, 1, 0, '首页', 'App', 0, 'Layout', '/', 'home-fill', 0, '', 1, '2022-05-31 17:45:44', NULL, 15, NULL, 0),
       (41, 1, 40, '数据看板', 'Home', 1, 'home', 'home', 'home-fill', 10, '', 1, '2022-05-31 17:46:09',
        '2022-05-31 17:49:12', 15, 15, 0),
       (42, 1, 35, '系统管理', 'ImSystem', 1, 'im/system', 'system', 'apps-2', 30, '', 1, '2022-06-07 10:09:18',
        '2022-06-13 09:56:29', 15, 15, 0),
       (43, 1, 35, '店铺管理', 'ImStore', 1, 'im/store', 'store', 'building', 40, '', 1, '2022-06-07 10:12:03',
        '2022-06-13 09:56:34', 15, 15, 0),
       (44, 1, 35, '首页', 'ImHome', 1, 'im/home', 'home', 'home-heart', 20, '', 1, '2022-06-07 10:12:51',
        '2022-06-13 09:56:20', 15, 15, 0),
       (45, 1, 35, '客服管理', 'ImWaiter', 1, 'im/waiter', 'waiter', 'customer-service-2', 50, '', 1, '2022-06-07 15:24:20',
        '2022-06-13 09:56:39', 15, 15, 0),
       (46, 1, 2, '订单设置', 'OrderConfig', 1, 'config/order', 'order', 'file-settings', 20, '', 1, '2022-06-09 16:25:47',
        '2022-06-09 16:30:50', 15, 15, 0),
       (48, 1, 47, '系统配置', 'SearchSystem', 1, 'search/system', 'system', 'apps-2', 10, '', 1, '2022-06-11 15:24:49',
        '2022-06-11 15:30:39', 15, 15, 0),
       (50, 1, 0, '客服', 'CustomerService', 0, 'Layout', 'cs', 'wechat-2-fill', 60, '', 1, '2022-06-13 09:55:32',
        '2022-06-13 10:01:02', 15, 15, 0),
       (51, 1, 50, '客服管理', 'WaiterManagement', 1, 'chat/waiter', 'waiter', 'customer-service-2', 20, '', 1,
        '2022-06-13 09:57:26', '2022-06-14 18:17:42', 15, 527, 0),
       (52, 1, 32, '商品评价', 'CommentManagement', 1, 'goods/comment/list', 'comment', 'Comment', 0, '', 1,
        '2022-06-15 17:37:48', '2022-06-15 17:42:33', 6, 6, 0),
       (53, 1, 52, '评价详情', 'CommentDetail', 2, 'goods/comment/detail', 'detail/:id', 'Comment', 0, '', 1,
        '2022-06-15 17:45:14', '2022-06-15 17:46:25', 6, 6, 0),
       (54, 1, 0, '搜索中台', 'SearchCenter', 0, 'Layout', '/search', 'search-eye-fill', 220, '', 1, '2022-06-21 14:54:46',
        NULL, 14, NULL, 0),
       (55, 1, 54, '系统配置', 'SearchSystem', 1, 'search/system', 'system', 'apps-2', 10, '', 1, '2022-06-21 14:56:19',
        NULL, 14, NULL, 0),
       (56, 1, 54, '搜索配置', 'SearchConfig', 1, 'search/config', 'config', 'file-settings', 20, '', 1,
        '2022-06-21 14:56:38', NULL, 14, NULL, 0),
       (57, 1, 0, '支付中台', 'PayCenter', 0, 'Layout', '/pay', 'Wallet', 230, '', 1, '2022-07-04 14:14:13',
        '2022-07-04 14:26:16', 8, 8, 0),
       (58, 1, 57, '商户管理', 'PayMerchant', 1, 'pay/merchant', 'merchant', 'Shop', 10, '', 1, '2022-07-04 14:16:44',
        '2022-07-04 16:46:01', 8, 8, 0),
       (59, 1, 57, '应用管理', 'PayApplication', 1, 'pay/application', 'application', 'Cellphone', 20, '', 1,
        '2022-07-04 14:33:31', NULL, 8, NULL, 0),
       (60, 1, 58, '新增商户', 'CreateMerchant', 2, 'pay/merchant/form', 'create', 'Shop', 0, '', 1, '2022-07-04 16:41:47',
        '2022-07-05 16:24:45', 8, 8, 0),
       (61, 1, 58, '编辑商户', 'UpdateMerchant', 2, 'pay/merchant/form', 'update/:id', 'Shop', 0, '', 1,
        '2022-07-05 16:23:06', '2022-07-05 16:29:29', 8, 8, 0),
       (62, 1, 59, '新增应用', 'CreateApplication', 2, 'pay/application/form', 'create', 'Cellphone', 0, '', 1,
        '2022-07-06 15:00:21', '2022-07-06 15:00:31', 15, 15, 0),
       (63, 1, 59, '编辑应用', 'UpdateApplication', 2, 'pay/application/form', 'update/:id', 'Cellphone', 0, '', 1,
        '2022-07-06 15:01:35', NULL, 15, NULL, 0),
       (64, 1, 57, '支付订单', 'PayPayOrder', 0, 'pay/payOrder/list', 'payOrder', 'DocumentChecked', 30, '', 1,
        '2022-07-07 10:18:51', '2022-07-07 10:21:42', 15, 15, 0),
       (65, 1, 57, '退款订单', 'RefundOrder', 0, 'pay/refundOrder/list', 'refundOrder', 'DocumentDelete', 40, '', 1,
        '2022-07-07 10:20:32', '2022-07-07 14:40:44', 15, 15, 0),
       (66, 1, 64, '支付订单详情', 'PayOrderDetail', 2, 'pay/payOrder/detail', 'detail/:id', 'DocumentChecked', 0, '', 1,
        '2022-07-07 14:35:29', '2022-07-07 14:42:06', 15, 15, 0),
       (67, 1, 65, '退款订单详情', 'RefundOrderDetail', 2, 'pay/refundOrder/detail', 'detail/:id', 'DocumentDelete', 0, '', 1,
        '2022-07-07 14:41:25', '2022-07-07 15:29:24', 15, 15, 0);
/*!40000 ALTER TABLE `menu`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permission`
--

DROP TABLE IF EXISTS `permission`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permission`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `system_id`   bigint(20)   NOT NULL COMMENT '系统id',
    `menu_id`     bigint(20)   NOT NULL COMMENT '所属菜单id',
    `type`        tinyint(2)  DEFAULT NULL COMMENT '权限类型，1，需要分配，2，登录既有的',
    `service`     varchar(50) DEFAULT NULL COMMENT '所属服务',
    `name`        varchar(50)  NOT NULL COMMENT '接口名称',
    `uri`         varchar(100) NOT NULL COMMENT '接口路径',
    `method`      varchar(10)  NOT NULL COMMENT 'http请求类型 GET,POST,PUT,DELETE 等',
    `is_enable`   tinyint(1)   NOT NULL COMMENT '是否开启',
    `create_time` datetime     NOT NULL,
    `update_time` datetime    DEFAULT NULL,
    `create_user` bigint(20)   NOT NULL,
    `update_user` bigint(20)  DEFAULT NULL,
    `is_deleted`  tinyint(1)   NOT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 271
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC COMMENT ='权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permission`
--

LOCK TABLES `permission` WRITE;
/*!40000 ALTER TABLE `permission`
    DISABLE KEYS */;
INSERT INTO `permission`
VALUES (28, 1, 8, 1, 'mall-marketing-service', '保存活动时间段', '/mall/marketing/activityTime', 'post', 1,
        '2022-05-11 20:51:24', '2022-05-20 14:42:09', 0, 8, 0),
       (29, 1, 8, 1, 'mall-marketing-service', '根据活动id 获取活动时间段', '/mall/marketing/activityTime', 'get', 1,
        '2022-05-11 20:51:25', '2022-05-20 14:42:09', 0, 8, 0),
       (30, 1, 8, 1, 'mall-marketing-service', '删除活动时间段', '/mall/marketing/activityTime', 'delete', 1,
        '2022-05-11 20:51:25', '2022-05-20 14:42:09', 0, 8, 0),
       (32, 1, 10, 1, 'mall-marketing-service', '增加推荐商品', '/mall/marketing/productRecommended', 'post', 1,
        '2022-05-11 20:51:25', '2022-05-20 14:42:09', 0, 8, 0),
       (33, 1, 10, 1, 'mall-marketing-service', '商品推荐分页查询', '/mall/marketing/productRecommended', 'get', 1,
        '2022-05-11 20:51:25', '2022-05-20 14:42:09', 0, 8, 0),
       (34, 1, 10, 1, 'mall-marketing-service', '移除商品推荐', '/mall/marketing/productRecommended', 'delete', 1,
        '2022-05-11 20:51:25', '2022-05-20 14:42:09', 0, 8, 0),
       (35, 1, 10, 1, 'mall-marketing-service', '开启 或 关闭是否推荐', '/mall/marketing/productRecommended', 'put', 1,
        '2022-05-11 20:51:25', '2022-05-20 14:42:09', 0, 8, 0),
       (36, 1, 9, 1, 'mall-marketing-service', '保存秒杀产品sku信息', '/mall/marketing/activityProductSku', 'post', 1,
        '2022-05-11 20:51:25', '2022-05-20 14:42:09', 0, 8, 0),
       (37, 1, 9, 1, 'mall-marketing-service', '根据活动商品id 查询现有秒杀sku信息', '/mall/marketing/activityProductSku', 'get', 1,
        '2022-05-11 20:51:25', '2022-05-20 14:42:09', 0, 8, 0),
       (39, 1, 8, 1, 'mall-marketing-service', '修改活动时间段', '/mall/marketing/activityTime/{id}', 'put', 1,
        '2022-05-11 20:51:25', '2022-05-20 14:42:09', 0, 8, 0),
       (40, 1, 7, 1, 'mall-marketing-service', '活动保存新增', '/mall/marketing/activity', 'post', 1, '2022-05-11 20:51:25',
        '2022-05-20 14:42:10', 0, 8, 0),
       (41, 1, 7, 1, 'mall-marketing-service', '分页查询活动列表（不带时间段信息）', '/mall/marketing/activity', 'get', 1,
        '2022-05-11 20:51:25', '2022-05-20 14:42:10', 0, 8, 0),
       (42, 1, 7, 1, 'mall-marketing-service', '活动根据id删除', '/mall/marketing/activity', 'delete', 1,
        '2022-05-11 20:51:25', '2022-05-20 14:42:10', 0, 8, 0),
       (43, 1, 8, 1, 'mall-marketing-service', '时间段开关', '/mall/marketing/activityTime/enable/{id}', 'put', 1,
        '2022-05-11 20:51:25', '2022-05-20 14:42:10', 0, 8, 0),
       (44, 1, 7, 1, 'mall-marketing-service', '活动上下线', '/mall/marketing/activity/online/{id}', 'put', 1,
        '2022-05-11 20:51:25', '2022-05-20 14:42:10', 0, 8, 0),
       (46, 1, 31, 1, 'mall-marketing-service', '根据id查询', '/mall/marketing/appMessagePush/{id}', 'get', 1,
        '2022-05-11 20:51:25', '2022-05-20 14:42:10', 0, 8, 0),
       (47, 1, 9, 1, 'mall-marketing-service', '添加活动商品', '/mall/marketing/activityProduct', 'post', 1,
        '2022-05-11 20:51:25', '2022-05-20 14:42:10', 0, 8, 0),
       (48, 1, 9, 1, 'mall-marketing-service', '查询某个时间段内的秒杀商品（不带sku信息）', '/mall/marketing/activityProduct', 'get', 1,
        '2022-05-11 20:51:25', '2022-05-20 14:42:10', 0, 8, 0),
       (49, 1, 9, 1, 'mall-marketing-service', '根据id 移除活动时间段的商品', '/mall/marketing/activityProduct', 'delete', 1,
        '2022-05-11 20:51:25', '2022-05-20 14:42:10', 0, 8, 0),
       (50, 1, 7, 1, 'mall-marketing-service', '根据活动id 获取活动信息，附带活动时间段列表', '/mall/marketing/activity/{id}', 'get', 1,
        '2022-05-11 20:51:25', '2022-05-20 14:42:11', 0, 8, 0),
       (51, 1, 7, 1, 'mall-marketing-service', '活动根据id 修改', '/mall/marketing/activity/{id}', 'put', 1,
        '2022-05-11 20:51:25', '2022-05-20 14:42:11', 0, 8, 0),
       (52, 1, 31, 1, 'mall-marketing-service', '保存app（发布）', '/mall/marketing/appMessagePush', 'post', 1,
        '2022-05-11 20:51:25', '2022-05-20 14:42:11', 0, 8, 0),
       (53, 1, 31, 1, 'mall-marketing-service', '分页查询', '/mall/marketing/appMessagePush', 'get', 1,
        '2022-05-11 20:51:25', '2022-05-20 14:42:11', 0, 8, 0),
       (54, 1, 31, 1, 'mall-marketing-service', '删除', '/mall/marketing/appMessagePush', 'delete', 1,
        '2022-05-11 20:51:25', '2022-05-20 14:42:11', 0, 8, 0),
       (109, 2, 14, 1, 'horse-user-service', '保存角色信息', '/uc/role', 'post', 1, '2022-05-12 10:42:17',
        '2022-05-20 14:43:04', 0, 8, 0),
       (110, 2, 14, 1, 'horse-user-service', '分页查询角色', '/uc/role', 'get', 1, '2022-05-12 10:42:18',
        '2022-05-20 14:43:04', 0, 8, 0),
       (111, 2, 14, 1, 'horse-user-service', '删除角色信息', '/uc/role', 'delete', 1, '2022-05-12 10:42:18',
        '2022-05-20 14:43:04', 0, 8, 0),
       (112, 2, 12, 1, 'horse-user-service', '获取部门下的角色', '/uc/department/role', 'get', 1, '2022-05-12 10:42:18', NULL,
        0, NULL, 0),
       (113, 2, 13, 1, 'horse-user-service', '修改菜单', '/uc/menu/{id}', 'put', 1, '2022-05-12 10:42:18',
        '2022-05-20 14:43:05', 0, 8, 0),
       (114, 2, 13, 1, 'horse-user-service', '保存权限', '/uc/permission', 'post', 1, '2022-05-12 10:42:18',
        '2022-05-20 14:43:05', 0, 8, 0),
       (115, 2, 13, 1, 'horse-user-service', '根据菜单获取权限列表', '/uc/permission', 'get', 1, '2022-05-12 10:42:18',
        '2022-05-20 14:43:05', 0, 8, 0),
       (116, 2, 13, 1, 'horse-user-service', '删除', '/uc/permission', 'delete', 1, '2022-05-12 10:42:18',
        '2022-05-20 14:43:05', 0, 8, 0),
       (117, 2, 0, 1, 'horse-user-service', '新增登录限制', '/uc/admin/user/login/limit', 'post', 1, '2022-05-12 10:42:18',
        '2022-05-20 14:43:06', 0, 8, 0),
       (118, 2, 0, 1, 'horse-user-service', '查询登录限制', '/uc/admin/user/login/limit', 'get', 1, '2022-05-12 10:42:18',
        '2022-05-20 14:43:06', 0, 8, 0),
       (119, 2, 0, 1, 'horse-user-service', '删除登录限制', '/uc/admin/user/login/limit', 'delete', 1, '2022-05-12 10:42:18',
        '2022-05-20 14:43:06', 0, 8, 0),
       (120, 2, 15, 1, 'horse-user-service', '获取员工权限', '/uc/employee/role/{employeeId}', 'get', 1,
        '2022-05-12 10:42:18', '2022-05-20 14:43:06', 0, 8, 0),
       (121, 2, 15, 1, 'horse-user-service', '设置员工角色', '/uc/employee/role/{employeeId}', 'put', 1,
        '2022-05-12 10:42:18', '2022-05-20 14:43:06', 0, 8, 0),
       (122, 2, 14, 1, 'horse-user-service', '获取角色的菜单和接口 权限属性结构', '/uc/role/{id}', 'get', 1, '2022-05-12 10:42:18',
        '2022-05-20 14:43:06', 0, 8, 0),
       (123, 2, 14, 1, 'horse-user-service', '更新角色信息', '/uc/role/{id}', 'put', 1, '2022-05-12 10:42:18',
        '2022-05-20 14:43:06', 0, 8, 0),
       (124, 2, 12, 1, 'horse-user-service', '获取部门树形结构', '/uc/department/tree', 'get', 1, '2022-05-12 10:42:18',
        '2022-05-20 14:43:07', 0, 8, 0),
       (125, 2, 12, 1, 'horse-user-service', '获取部门下的子部门', '/uc/department/child', 'get', 1, '2022-05-12 10:42:18',
        '2022-05-20 14:43:07', 0, 8, 0),
       (126, 2, 12, 1, 'horse-user-service', '获取部门下的员工', '/uc/department/employee', 'get', 1, '2022-05-12 10:42:18',
        NULL, 0, NULL, 0),
       (127, 2, 13, 1, 'horse-user-service', '修改', '/uc/permission/{id}', 'put', 1, '2022-05-12 10:42:18',
        '2022-05-20 14:43:07', 0, 8, 0),
       (128, 2, 30, 1, 'horse-user-service', '分页查询C端用户信息', '/uc/admin/user/page', 'get', 1, '2022-05-12 10:42:18',
        '2022-05-20 14:43:07', 0, 8, 0),
       (129, 2, 15, 1, 'horse-user-service', '根据员工id 获取员工信息', '/uc/employee/{id}', 'get', 1, '2022-05-12 10:42:18',
        '2022-05-20 14:43:07', 0, 8, 0),
       (130, 2, 15, 1, 'horse-user-service', '员工启用，禁用', '/uc/employee/enable', 'put', 1, '2022-05-12 10:42:18',
        '2022-05-20 14:43:07', 0, 8, 0),
       (131, 2, 30, 1, 'horse-user-service', '开启 禁用用户', '/uc/admin/user/enable', 'put', 1, '2022-05-12 10:42:18',
        '2022-05-20 14:43:07', 0, 8, 0),
       (132, 2, 15, 1, 'horse-user-service', '获取当前登录的员工信息', '/uc/employee/current', 'get', 1, '2022-05-12 10:42:18',
        '2022-05-20 14:43:08', 0, 8, 0),
       (133, 2, 13, 1, 'horse-user-service', '增加菜单', '/uc/menu', 'post', 1, '2022-05-12 10:42:18',
        '2022-05-20 14:43:08', 0, 8, 0),
       (134, 2, 13, 1, 'horse-user-service', '删除菜单', '/uc/menu', 'delete', 1, '2022-05-12 10:42:18',
        '2022-05-20 14:43:08', 0, 8, 0),
       (135, 2, 0, 1, 'horse-user-service', '修改登录限制', '/uc/admin/user/login/limit/{id}', 'post', 1,
        '2022-05-12 10:42:18', '2022-05-20 14:43:08', 0, 8, 0),
       (136, 2, 13, 1, 'horse-user-service', '获取菜单树形结构', '/uc/menu/tree', 'get', 1, '2022-05-12 10:42:18',
        '2022-05-20 14:43:08', 0, 8, 0),
       (137, 2, 12, 1, 'horse-user-service', '保存部门信息', '/uc/department', 'post', 1, '2022-05-12 10:42:18',
        '2022-05-20 14:43:08', 0, 8, 0),
       (138, 2, 12, 1, 'horse-user-service', 'page', '/uc/department', 'get', 1, '2022-05-12 10:42:18', NULL, 0, NULL,
        0),
       (139, 2, 12, 1, 'horse-user-service', '删除部门信息', '/uc/department', 'delete', 1, '2022-05-12 10:42:18',
        '2022-05-20 14:43:09', 0, 8, 0),
       (140, 2, 15, 1, 'horse-user-service', '新增员工信息（后管用户新增）', '/uc/employee', 'post', 1, '2022-05-12 10:42:18',
        '2022-05-20 14:43:09', 0, 8, 0),
       (141, 2, 15, 1, 'horse-user-service', '分页查询员工列表', '/uc/employee', 'get', 1, '2022-05-12 10:42:18',
        '2022-05-20 14:43:09', 0, 8, 0),
       (142, 2, 15, 1, 'horse-user-service', '删除员工信息', '/uc/employee', 'delete', 1, '2022-05-12 10:42:18',
        '2022-05-20 14:43:09', 0, 8, 0),
       (143, 2, 15, 1, 'horse-user-service', '修改员工信息', '/uc/employee', 'put', 1, '2022-05-12 10:42:18',
        '2022-05-20 14:43:09', 0, 8, 0),
       (144, 2, 12, 1, 'horse-user-service', '根据id 获取部门信息', '/uc/department/{id}', 'get', 1, '2022-05-12 10:42:18',
        '2022-05-20 14:43:09', 0, 8, 0),
       (145, 2, 12, 1, 'horse-user-service', '修改部门信息', '/uc/department/{id}', 'put', 1, '2022-05-12 10:42:18',
        '2022-05-20 14:43:09', 0, 8, 0),
       (146, 2, 12, 1, 'horse-user-service', '删除角色到部门', '/uc/department/role', 'delete', 1, '2022-05-13 11:45:10',
        '2022-05-20 14:43:04', 8, 8, 0),
       (147, 2, 12, 1, 'horse-user-service', '设置角色到部门', '/uc/department/role', 'put', 1, '2022-05-13 11:45:10',
        '2022-05-20 14:43:04', 8, 8, 0),
       (148, 2, 15, 1, 'horse-user-service', '设置员工的部门', '/uc/employee/employee', 'put', 1, '2022-05-13 11:45:10',
        '2022-05-20 14:43:05', 8, 8, 0),
       (149, 2, 12, 1, 'horse-user-service', '查询部门下的角色 分组', '/uc/role/department', 'get', 1, '2022-05-13 11:45:10',
        '2022-05-20 14:43:05', 8, 8, 0),
       (150, 2, 15, 1, 'horse-user-service', '获取部门下的员工', '/uc/employee/department', 'get', 1, '2022-05-13 11:45:10',
        '2022-05-20 14:43:05', 8, 8, 0),
       (153, 1, 20, 1, 'mall-trade-service', '订单详情信息', '/mall/trade/admin/tradeOrder/{orderId}', 'get', 1,
        '2022-05-20 14:36:13', '2022-05-20 14:36:40', 8, 8, 0),
       (154, 1, 19, 1, 'mall-trade-service', '订单统计', '/mall/trade/admin/tradeOrder/statistics', 'get', 1,
        '2022-05-20 14:36:13', '2022-05-20 14:36:40', 8, 8, 0),
       (155, 1, 22, 1, 'mall-trade-service', '（仅退款）同意退款', '/mall/trade/admin/refundOrder/agreeRefund', 'put', 1,
        '2022-05-20 14:36:13', '2022-05-20 14:36:40', 8, 8, 0),
       (156, 1, 20, 1, 'mall-trade-service', '修改费用', '/mall/trade/admin/tradeOrder/updateAmount', 'put', 1,
        '2022-05-20 14:36:13', '2022-05-20 14:36:41', 8, 8, 0),
       (157, 1, 20, 1, 'mall-trade-service', '修改收货人信息', '/mall/trade/admin/tradeOrder/updateRecipient', 'put', 1,
        '2022-05-20 14:36:13', '2022-05-20 14:36:41', 8, 8, 0),
       (158, 1, 22, 1, 'mall-trade-service', '（退货退款）拒绝退货', '/mall/trade/admin/refundOrder/disagreeReturn', 'put', 1,
        '2022-05-20 14:36:13', '2022-05-20 14:36:41', 8, 8, 0),
       (159, 1, 21, 1, 'mall-trade-service', '退款单统计', '/mall/trade/admin/refundOrder/statistics', 'get', 1,
        '2022-05-20 14:36:13', '2022-05-20 14:36:41', 8, 8, 0),
       (160, 1, 19, 1, 'mall-trade-service', '关闭订单', '/mall/trade/admin/tradeOrder/close', 'put', 1,
        '2022-05-20 14:36:13', '2022-05-20 14:36:41', 8, 8, 0),
       (161, 1, 20, 1, 'mall-trade-service', '查看物流', '/mall/trade/admin/tradeOrder/logistics/{orderId}', 'get', 1,
        '2022-05-20 14:36:13', '2022-05-20 14:36:41', 8, 8, 0),
       (162, 1, 19, 1, 'mall-trade-service', '单笔订单发货', '/mall/trade/admin/tradeOrder/delivery', 'put', 1,
        '2022-05-20 14:36:13', '2022-05-20 14:36:41', 8, 8, 0),
       (163, 1, 22, 1, 'mall-trade-service', '（退货退款）拒绝收货', '/mall/trade/admin/refundOrder/disagreeReceiving', 'put', 1,
        '2022-05-20 14:36:13', '2022-05-20 14:36:41', 8, 8, 0),
       (164, 1, 22, 1, 'mall-trade-service', '（退货退款）确认收货', '/mall/trade/admin/refundOrder/agreeReceiving', 'put', 1,
        '2022-05-20 14:36:13', '2022-05-20 14:36:41', 8, 8, 0),
       (165, 1, 22, 1, 'mall-trade-service', '（退货退款）同意退货', '/mall/trade/admin/refundOrder/agreeReturn', 'put', 1,
        '2022-05-20 14:36:13', '2022-05-20 14:36:42', 8, 8, 0),
       (166, 1, 19, 1, 'mall-trade-service', '批量订单发货', '/mall/trade/admin/tradeOrder/batchDelivery', 'put', 1,
        '2022-05-20 14:36:13', '2022-05-20 14:36:42', 8, 8, 0),
       (167, 1, 19, 1, 'mall-trade-service', '查询物流公司列表', '/mall/trade/admin/tradeOrder/logisticsCompany', 'get', 1,
        '2022-05-20 14:36:13', '2022-05-20 14:36:42', 8, 8, 0),
       (168, 1, 19, 1, 'mall-trade-service', '订单分页列表', '/mall/trade/admin/tradeOrder/page', 'get', 1,
        '2022-05-20 14:36:13', '2022-05-20 14:36:42', 8, 8, 0),
       (169, 1, 21, 1, 'mall-trade-service', '退款单分页列表', '/mall/trade/admin/refundOrder/page', 'get', 1,
        '2022-05-20 14:36:13', '2022-05-20 14:36:42', 8, 8, 0),
       (170, 1, 22, 1, 'mall-trade-service', '查看物流', '/mall/trade/admin/refundOrder/logistics/{refundId}', 'get', 1,
        '2022-05-20 14:36:13', '2022-05-20 14:36:42', 8, 8, 0),
       (171, 1, 20, 1, 'mall-trade-service', '取消订单', '/mall/trade/admin/tradeOrder/cancel', 'put', 1,
        '2022-05-20 14:36:13', '2022-05-20 14:36:42', 8, 8, 0),
       (172, 1, 19, 1, 'mall-trade-service', '查询发货列表订单', '/mall/trade/admin/tradeOrder/listDeliveryOrder', 'get', 1,
        '2022-05-20 14:36:13', '2022-05-20 14:36:42', 8, 8, 0),
       (173, 1, 22, 1, 'mall-trade-service', '（仅退款）拒绝退款', '/mall/trade/admin/refundOrder/disagreeRefund', 'put', 1,
        '2022-05-20 14:36:13', '2022-05-20 14:36:42', 8, 8, 0),
       (174, 1, 22, 1, 'mall-trade-service', '（仅退款）退款单详情信息', '/mall/trade/admin/refundOrder/refundInfo/{refundId}',
        'get', 1, '2022-05-20 14:36:42', NULL, 8, NULL, 0),
       (175, 1, 22, 1, 'mall-trade-service', '（退货退款）退款单详情信息', '/mall/trade/admin/refundOrder/returnInfo/{refundId}',
        'get', 1, '2022-05-20 14:36:42', NULL, 8, NULL, 0),
       (176, 1, 26, 1, 'mall-product-service', '更新商品属性', '/mall/product/admin/productAttribute/{id}', 'put', 1,
        '2022-05-20 14:38:46', NULL, 8, NULL, 0),
       (177, 1, 26, 1, 'mall-product-service', '商品列表', '/mall/product/admin/product/page', 'get', 1,
        '2022-05-20 14:38:46', NULL, 8, NULL, 0),
       (178, 1, 26, 1, 'mall-product-service', '新增商品属性，返回id', '/mall/product/admin/productAttribute', 'post', 1,
        '2022-05-20 14:38:46', NULL, 8, NULL, 0),
       (179, 1, 26, 1, 'mall-product-service', '删除商品属性', '/mall/product/admin/productAttribute', 'delete', 1,
        '2022-05-20 14:38:46', NULL, 8, NULL, 0),
       (180, 1, 23, 1, 'mall-product-service', '转移商品', '/mall/product/admin/productCategory/transfer/{oldId}/{newId}',
        'put', 1, '2022-05-20 14:38:46', NULL, 8, NULL, 0),
       (181, 1, 23, 1, 'mall-product-service', '更新排序', '/mall/product/admin/productCategory/updateSort', 'put', 1,
        '2022-05-20 14:38:46', NULL, 8, NULL, 0),
       (182, 1, 26, 1, 'mall-product-service', '更新商品属性排序', '/mall/product/admin/productAttribute/updateSort', 'put', 1,
        '2022-05-20 14:38:46', NULL, 8, NULL, 0),
       (183, 1, 26, 1, 'mall-product-service', '新增商品属性组，返回id', '/mall/product/admin/productAttributeGroup', 'post', 1,
        '2022-05-20 14:38:46', NULL, 8, NULL, 0),
       (184, 1, 26, 1, 'mall-product-service', '删除商品属性组（在保存sku信息的时候才一起请求）', '/mall/product/admin/productAttributeGroup',
        'delete', 1, '2022-05-20 14:38:46', NULL, 8, NULL, 0),
       (185, 1, 28, 1, 'mall-product-service', '根据商品id查询sku列表', '/mall/product/admin/productSku/list/{productId}',
        'get', 1, '2022-05-20 14:38:46', NULL, 8, NULL, 0),
       (186, 1, 23, 1, 'mall-product-service', '添加分类', '/mall/product/admin/productCategory', 'post', 1,
        '2022-05-20 14:38:46', NULL, 8, NULL, 0),
       (187, 1, 23, 1, 'mall-product-service', '查询分类列表（树状结构）', '/mall/product/admin/productCategory', 'get', 1,
        '2022-05-20 14:38:46', NULL, 8, NULL, 0),
       (188, 1, 23, 1, 'mall-product-service', '删除分类', '/mall/product/admin/productCategory', 'delete', 1,
        '2022-05-20 14:38:46', NULL, 8, NULL, 0),
       (189, 1, 26, 1, 'mall-product-service', '更新商品属性组', '/mall/product/admin/productAttributeGroup/{id}', 'put', 1,
        '2022-05-20 14:38:46', NULL, 8, NULL, 0),
       (190, 1, 23, 1, 'mall-product-service', '更新分类', '/mall/product/admin/productCategory/{id}', 'put', 1,
        '2022-05-20 14:38:46', NULL, 8, NULL, 0),
       (191, 1, 26, 1, 'mall-product-service', '根据属性组id查询属性列表', '/mall/product/admin/productAttribute/list/{groupId}',
        'get', 1, '2022-05-20 14:38:46', NULL, 8, NULL, 0),
       (192, 1, 26, 1, 'mall-product-service', '更新商品属性组排序', '/mall/product/admin/productAttributeGroup/updateSort',
        'put', 1, '2022-05-20 14:38:46', NULL, 8, NULL, 0),
       (193, 1, 26, 1, 'mall-product-service', '查询单个商品', '/mall/product/admin/product/{id}', 'get', 1,
        '2022-05-20 14:38:46', NULL, 8, NULL, 0),
       (194, 1, 26, 1, 'mall-product-service', '修改商品', '/mall/product/admin/product/{id}', 'put', 1,
        '2022-05-20 14:38:46', NULL, 8, NULL, 0),
       (195, 1, 26, 1, 'mall-product-service', '根据商品id查询商品属性组列表',
        '/mall/product/admin/productAttributeGroup/{productId}', 'get', 1, '2022-05-20 14:38:46', NULL, 8, NULL, 0),
       (196, 1, 26, 1, 'mall-product-service', '商品上下架', '/mall/product/admin/product/enable/{id}', 'put', 1,
        '2022-05-20 14:38:46', NULL, 8, NULL, 0),
       (197, 1, 26, 1, 'mall-product-service', '新增商品并返回id（保存第一页商品信息，商品处于未上架状态）', '/mall/product/admin/product', 'post',
        1, '2022-05-20 14:38:46', NULL, 8, NULL, 0),
       (198, 1, 26, 1, 'mall-product-service', '删除商品', '/mall/product/admin/product', 'delete', 1,
        '2022-05-20 14:38:46', NULL, 8, NULL, 0),
       (199, 1, 28, 1, 'mall-product-service', '删除所有sku并新增sku（新增或删除属性组后保存sku列表时使用）',
        '/mall/product/admin/productSku/deleteAllAndAdd/{productId}', 'post', 1, '2022-05-20 14:38:46', NULL, 8, NULL,
        0),
       (200, 1, 28, 1, 'mall-product-service', '保存sku列表', '/mall/product/admin/productSku/{productId}', 'post', 1,
        '2022-05-20 14:38:46', NULL, 8, NULL, 0),
       (201, 1, 13, 1, 'horse-user-service', '通过swagger文档，更新接口权限', '/uc/permission/swagger', 'post', 1,
        '2022-05-20 14:43:09', NULL, 8, NULL, 0),
       (202, 1, 14, 1, 'horse-user-service', '设置角色的菜单与接口权限', '/uc/role/menu', 'put', 1, '2022-05-20 14:43:09', NULL, 8,
        NULL, 0),
       (203, 1, 0, 2, 'horse-user-service', '获取拥有的菜单权限', '/uc/menu', 'get', 1, '2022-05-20 14:43:09', NULL, 8, NULL, 0),
       (204, 1, 0, 2, 'horse-user-service', '系统列表', '/uc/system', 'get', 1, '2022-05-20 14:43:09', NULL, 8, NULL, 0),
       (205, 1, 5, 1, 'horse-im-service', '按时间段获取客服会话', '/im/admin/waiter/findWaiterSessions', 'get', 1,
        '2022-05-20 15:51:22', NULL, 2, NULL, 0),
       (206, 1, 4, 1, 'horse-im-service', '获取连接客服通道的ticket', '/im/waiter/getWaiterConnectTicket', 'get', 1,
        '2022-05-20 15:51:22', NULL, 2, NULL, 0),
       (207, 1, 5, 1, 'horse-im-service', '获取客服统计数据', '/im/admin/waiter/waiterStatistics', 'get', 1,
        '2022-05-20 15:51:22', NULL, 2, NULL, 0),
       (208, 1, 5, 1, 'horse-im-service', '查询会话中消息', '/im/admin/waiter/findSessionMessage', 'get', 1,
        '2022-05-20 15:51:22', NULL, 2, NULL, 0),
       (209, 1, 5, 1, 'horse-im-service', '查询所有可用客服', '/im/admin/waiter/findAllWaiter/{storeId}', 'get', 1,
        '2022-05-20 15:51:22', NULL, 2, NULL, 0),
       (211, 1, 19, 1, 'mall-trade-service', '虚拟订单发货', '/mall/trade/admin/tradeOrder/virtualDelivery{orderId}', 'put',
        1, '2022-05-27 11:39:17', '2022-05-27 11:40:06', 10, 10, 0),
       (212, 1, 19, 1, 'mall-trade-service', '批量虚拟订单发货', '/mall/trade/admin/tradeOrder/batchVirtualDelivery', 'put', 1,
        '2022-05-27 11:39:44', NULL, 10, NULL, 0),
       (213, 1, 36, 1, 'mall-marketing-service', '分页查询广告信息', '/mall/marketing/advertisement', 'get', 1,
        '2022-05-27 11:39:44', NULL, 10, NULL, 0),
       (214, 1, 36, 1, 'mall-marketing-service', '广告保存新增', '/mall/marketing/advertisement', 'post', 1,
        '2022-05-27 11:39:44', NULL, 10, NULL, 0),
       (215, 1, 36, 1, 'mall-marketing-service', '修改广告排序', '/mall/marketing/advertisement/updateSort', 'put', 1,
        '2022-05-27 11:39:44', NULL, 10, NULL, 0),
       (216, 1, 36, 1, 'mall-marketing-service', '根据广告id查询广告信息', '/mall/marketing/advertisement/{id}', 'get', 1,
        '2022-05-27 11:39:44', NULL, 10, NULL, 0),
       (217, 1, 36, 1, 'mall-marketing-service', '根据广告id修改广告信息', '/mall/marketing/advertisement/{id}', 'put', 1,
        '2022-05-27 11:39:44', NULL, 10, NULL, 0),
       (219, 1, 36, 1, 'mall-marketing-service', '删除广告', '/mall/marketing/advertisement', 'delete', 1,
        '2022-05-27 11:39:44', NULL, 10, NULL, 0),
       (220, 1, 13, 1, 'horse-user-service', '查询未分配菜单的接口', '/uc/permission/NotDistribution', 'get', 1,
        '2022-05-30 15:52:26', NULL, 10, NULL, 0),
       (222, 1, 40, 1, 'mall-base-service', '获取销售额同比增长数据', '/mall/base/frontPage/getSalesTotal', 'get', 1,
        '2022-05-31 17:00:25', NULL, 97, NULL, 0),
       (223, 1, 40, 1, 'mall-base-service', '获取今日访客统计数据', '/mall/base/frontPage/listTodayVisitorStatistics', 'get', 1,
        '2022-05-31 17:00:25', NULL, 97, NULL, 0),
       (224, 1, 40, 1, 'mall-base-service', '获取今日销售额统计数据', '/mall/base/frontPage/listTodaySalesStatistics', 'get', 1,
        '2022-05-31 17:00:25', NULL, 97, NULL, 0),
       (225, 1, 40, 1, 'mall-base-service', '获取订单数量同比增长数据', '/mall/base/frontPage/getOrderTotal', 'get', 1,
        '2022-05-31 17:00:25', NULL, 97, NULL, 0),
       (226, 1, 40, 1, 'mall-base-service', '获取销售额统计数据', '/mall/base/frontPage/listSalesStatistics', 'get', 1,
        '2022-05-31 17:00:25', NULL, 97, NULL, 0),
       (227, 1, 40, 1, 'mall-base-service', '获取头部数据 今日访客 今日订单 今日销售额 近七天销售额', '/mall/base/frontPage/getHeaderData',
        'get', 1, '2022-05-31 17:00:25', NULL, 97, NULL, 0),
       (228, 1, 40, 1, 'mall-base-service', '获取访客数量同比增长数据', '/mall/base/frontPage/getVisitorTotal', 'get', 1,
        '2022-05-31 17:00:25', NULL, 97, NULL, 0),
       (229, 1, 40, 1, 'mall-base-service', '获取用户总览数据', '/mall/base/frontPage/getUserOverviewData', 'get', 1,
        '2022-05-31 17:00:25', NULL, 97, NULL, 0),
       (230, 1, 40, 1, 'mall-base-service', '获取今日订单统计数据', '/mall/base/frontPage/listTodayOrderStatistics', 'get', 1,
        '2022-05-31 17:00:25', NULL, 97, NULL, 0),
       (231, 1, 40, 1, 'mall-base-service', '获取待处理事务数据', '/mall/base/frontPage/getWaitHandleAffairs', 'get', 1,
        '2022-05-31 17:00:25', NULL, 97, NULL, 0),
       (232, 1, 40, 1, 'mall-base-service', '获取商品总览数据', '/mall/base/frontPage/getProductOverview', 'get', 1,
        '2022-05-31 17:00:25', NULL, 97, NULL, 0),
       (233, 1, 40, 1, 'mall-base-service', '获取访客统计数据', '/mall/base/frontPage/listVisitorStatistics', 'get', 1,
        '2022-05-31 17:00:25', NULL, 97, NULL, 0),
       (234, 1, 40, 1, 'mall-base-service', '获取订单统计数据', '/mall/base/frontPage/listOrderStatistics', 'get', 1,
        '2022-05-31 17:00:25', NULL, 97, NULL, 0),
       (235, 1, 0, 1, 'horse-im-service', '指定时间段查询每日消息量', '/im/admin/count/daysMessage', 'get', 1,
        '2022-06-07 10:14:11', NULL, 2, NULL, 0),
       (236, 1, 0, 1, 'horse-im-service', '新增店铺客服', '/im/admin/waiter', 'post', 1, '2022-06-07 10:14:11', NULL, 2, NULL,
        0),
       (237, 1, 0, 1, 'horse-im-service', '查询店铺客服', '/im/admin/waiter', 'get', 1, '2022-06-07 10:14:11', NULL, 2, NULL,
        0),
       (238, 1, 0, 1, 'horse-im-service', '删除店铺客服', '/im/admin/waiter', 'delete', 1, '2022-06-07 10:14:11', NULL, 2,
        NULL, 0),
       (239, 1, 0, 1, 'horse-im-service', '更新店铺客服', '/im/admin/waiter', 'put', 1, '2022-06-07 10:14:11', NULL, 2, NULL,
        0),
       (240, 1, 0, 1, 'horse-im-service', '指定日期查询小时消息量', '/im/admin/count/hoursMessage', 'get', 1,
        '2022-06-07 10:14:11', NULL, 2, NULL, 0),
       (241, 1, 0, 1, 'horse-im-service', '查询系统在线人数', '/im/admin/count/online', 'get', 1, '2022-06-07 10:14:11', NULL,
        2, NULL, 0),
       (242, 1, 0, 1, 'horse-im-service', '新增系统', '/im/admin/thirdSystem', 'post', 1, '2022-06-07 10:14:11', NULL, 2,
        NULL, 0),
       (243, 1, 0, 1, 'horse-im-service', '查询系统', '/im/admin/thirdSystem', 'get', 1, '2022-06-07 10:14:11', NULL, 2,
        NULL, 0),
       (244, 1, 0, 1, 'horse-im-service', '删除系统', '/im/admin/thirdSystem', 'delete', 1, '2022-06-07 10:14:11', NULL, 2,
        NULL, 0),
       (245, 1, 0, 1, 'horse-im-service', '新增店铺', '/im/admin/storeConfig', 'post', 1, '2022-06-07 10:14:11', NULL, 2,
        NULL, 0),
       (246, 1, 0, 1, 'horse-im-service', '查询店铺', '/im/admin/storeConfig', 'get', 1, '2022-06-07 10:14:11', NULL, 2,
        NULL, 0),
       (247, 1, 0, 1, 'horse-im-service', '删除店铺', '/im/admin/storeConfig', 'delete', 1, '2022-06-07 10:14:11', NULL, 2,
        NULL, 0),
       (248, 1, 41, 1, 'horse-im-service', '修改店铺头像和昵称', '/im/admin/storeConfig', 'put', 1, '2022-06-07 10:14:11',
        '2022-06-09 10:51:03', 2, 15, 0),
       (249, 1, 4, 1, 'horse-im-service', '分页查询客服', '/mall/im/admin/waiter', 'get', 1, '2022-06-07 14:04:49',
        '2022-06-13 09:59:07', 2, 15, 0),
       (250, 1, 4, 1, 'horse-im-service', '转移会话', '/mall/im/admin/waiter/transfer', 'post', 1, '2022-06-07 14:04:49',
        '2022-06-13 09:59:01', 2, 15, 0),
       (251, 1, 4, 1, 'horse-im-service', '获取ticket', '/mall/im/admin/ticket', 'get', 1, '2022-06-07 14:04:49',
        '2022-06-13 09:58:55', 2, 15, 0),
       (252, 1, 35, 1, 'im', '续期token', '/mall/im/admin/token/renewal', 'get', 1, '2022-06-07 14:04:49',
        '2022-06-09 11:04:08', 2, 15, 0),
       (255, 1, 51, 1, 'horse-im-service', '新增客服', '/mall/im/admin/waiterUser', 'post', 1, '2022-06-11 17:51:13',
        '2022-06-13 09:58:24', 2, 15, 0),
       (256, 1, 51, 1, 'horse-im-service', '分页查询客服', '/mall/im/admin/waiterUser', 'get', 1, '2022-06-11 17:51:13',
        '2022-06-13 09:58:30', 2, 15, 0),
       (257, 1, 51, 1, 'horse-im-service', '删除客服', '/mall/im/admin/waiterUser', 'delete', 1, '2022-06-11 17:51:13',
        '2022-06-13 09:58:35', 2, 15, 0),
       (258, 1, 51, 1, 'horse-im-service', '修改客服', '/mall/im/admin/waiterUser', 'put', 1, '2022-06-11 17:51:13',
        '2022-06-13 09:58:40', 2, 15, 0),
       (259, 1, 51, 1, 'horse-im-service', '通过用户id查询客服', '/mall/im/admin/waiterUser/getWaiterByUserId', 'get', 1,
        '2022-06-11 17:51:13', '2022-06-13 09:58:48', 2, 15, 0),
       (260, 1, 46, 1, 'mall-base-service', '查询订单配置', '/mall/base/orderConfig/getOrderConfig', 'get', 1,
        '2022-06-17 16:49:26', '2022-06-17 17:15:15', 11, 6, 0),
       (261, 1, 46, 1, 'mall-base-service', '新增或更新订单配置', '/mall/base/orderConfig/saveOrUpdate', 'post', 1,
        '2022-06-17 16:49:26', '2022-06-17 17:15:20', 11, 6, 0),
       (262, 1, 52, 1, 'mall-comment-service', '删除', '/mall/comment/admin/comment', 'delete', 1, '2022-06-15 18:39:09',
        '2022-06-16 15:53:30', 97, 8, 0),
       (263, 1, 52, 1, 'mall-comment-service', '更新显示状态', '/mall/comment/admin/comment', 'put', 1, '2022-06-15 18:39:09',
        '2022-06-16 15:53:19', 97, 8, 0),
       (264, 1, 52, 1, 'mall-comment-service', '查询评论详情', '/mall/comment/admin/comment/getCommentDetail/{id}', 'get', 1,
        '2022-06-15 18:39:09', '2022-06-16 15:53:42', 97, 8, 0),
       (268, 1, 52, 1, 'mall-comment-service', '新增商家评论', '/mall/comment/admin/merchantComment', 'post', 1,
        '2022-06-21 09:46:27', '2022-06-21 09:47:52', 97, 97, 0),
       (269, 1, 52, 1, 'mall-comment-service', '新增评论', '/mall/comment/admin/comment', 'post', 1, '2022-06-15 18:39:09',
        '2022-06-16 15:52:45', 97, 8, 0),
       (270, 1, 52, 1, 'mall-comment-service', '分页查询评论', '/mall/comment/admin/comment', 'get', 1, '2022-06-15 18:39:09',
        '2022-06-16 15:53:05', 97, 8, 0);
/*!40000 ALTER TABLE `permission`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role`
(
    `id`          bigint(20)                     NOT NULL AUTO_INCREMENT,
    `role_name`   varchar(64) CHARACTER SET utf8 NOT NULL COMMENT '角色名称',
    `role_desc`   varchar(50) DEFAULT NULL,
    `system_id`   bigint(20)  DEFAULT NULL COMMENT '角色所属系统',
    `is_enable`   tinyint(1)                     NOT NULL COMMENT '是否开启',
    `create_time` datetime                       NOT NULL,
    `update_time` datetime    DEFAULT NULL,
    `create_user` bigint(20)                     NOT NULL,
    `update_user` bigint(20)  DEFAULT NULL,
    `is_deleted`  tinyint(1)                     NOT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 6
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC COMMENT ='系统角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role`
    DISABLE KEYS */;
INSERT INTO `role`
VALUES (1, '超级管理员', NULL, 1, 1, '2022-05-12 16:51:17', NULL, 8, NULL, 0),
       (2, '普通管理员', NULL, 1, 1, '2022-05-12 17:51:30', NULL, 8, NULL, 0),
       (3, '客服人员', '客服人员', 1, 1, '2022-05-21 20:02:32', NULL, 1, NULL, 0),
       (5, '1', '1', 1, 0, '2022-06-21 14:08:06', '2022-06-21 14:11:40', 6, 6, 0);
/*!40000 ALTER TABLE `role`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_menu`
--

DROP TABLE IF EXISTS `role_menu`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_menu`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT,
    `role_id`     bigint(20) NOT NULL COMMENT '角色Id',
    `menu_id`     bigint(20) NOT NULL COMMENT '菜单ID',
    `system_id`   bigint(20) NOT NULL COMMENT '系统id',
    `create_time` datetime   NOT NULL,
    `update_time` datetime   DEFAULT NULL,
    `create_user` bigint(20) NOT NULL,
    `update_user` bigint(20) DEFAULT NULL,
    `is_deleted`  tinyint(1) NOT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 732
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC COMMENT ='系统角色菜单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_menu`
--

LOCK TABLES `role_menu` WRITE;
/*!40000 ALTER TABLE `role_menu`
    DISABLE KEYS */;
INSERT INTO `role_menu`
VALUES (600, 1, 32, 1, '2022-05-21 15:27:21', NULL, 8, NULL, 0),
       (601, 1, 26, 1, '2022-05-21 15:27:21', NULL, 8, NULL, 0),
       (602, 1, 27, 1, '2022-05-21 15:27:21', NULL, 8, NULL, 0),
       (603, 1, 28, 1, '2022-05-21 15:27:21', NULL, 8, NULL, 0),
       (604, 1, 23, 1, '2022-05-21 15:27:21', NULL, 8, NULL, 0),
       (605, 1, 24, 1, '2022-05-21 15:27:21', NULL, 8, NULL, 0),
       (606, 1, 25, 1, '2022-05-21 15:27:21', NULL, 8, NULL, 0),
       (607, 1, 18, 1, '2022-05-21 15:27:21', NULL, 8, NULL, 0),
       (608, 1, 19, 1, '2022-05-21 15:27:21', NULL, 8, NULL, 0),
       (609, 1, 20, 1, '2022-05-21 15:27:21', NULL, 8, NULL, 0),
       (610, 1, 21, 1, '2022-05-21 15:27:21', NULL, 8, NULL, 0),
       (611, 1, 22, 1, '2022-05-21 15:27:21', NULL, 8, NULL, 0),
       (612, 1, 34, 1, '2022-05-21 15:27:21', NULL, 8, NULL, 0),
       (613, 1, 30, 1, '2022-05-21 15:27:21', NULL, 8, NULL, 0),
       (614, 1, 6, 1, '2022-05-21 15:27:21', NULL, 8, NULL, 0),
       (615, 1, 7, 1, '2022-05-21 15:27:21', NULL, 8, NULL, 0),
       (616, 1, 8, 1, '2022-05-21 15:27:21', NULL, 8, NULL, 0),
       (617, 1, 9, 1, '2022-05-21 15:27:21', NULL, 8, NULL, 0),
       (618, 1, 10, 1, '2022-05-21 15:27:21', NULL, 8, NULL, 0),
       (619, 1, 29, 1, '2022-05-21 15:27:21', NULL, 8, NULL, 0),
       (620, 1, 31, 1, '2022-05-21 15:27:21', NULL, 8, NULL, 0),
       (622, 1, 35, 1, '2022-05-21 15:27:21', NULL, 8, NULL, 0),
       (623, 1, 4, 1, '2022-05-21 15:27:21', NULL, 8, NULL, 0),
       (624, 1, 11, 1, '2022-05-21 15:27:21', NULL, 8, NULL, 0),
       (625, 1, 12, 1, '2022-05-21 15:27:21', NULL, 8, NULL, 0),
       (626, 1, 13, 1, '2022-05-21 15:27:21', NULL, 8, NULL, 0),
       (627, 1, 14, 1, '2022-05-21 15:27:21', NULL, 8, NULL, 0),
       (628, 1, 15, 1, '2022-05-21 15:27:21', NULL, 8, NULL, 0),
       (629, 1, 16, 1, '2022-05-21 15:27:21', NULL, 8, NULL, 0),
       (630, 1, 17, 1, '2022-05-21 15:27:21', NULL, 8, NULL, 0),
       (631, 1, 2, 1, '2022-05-25 15:47:54', NULL, 14, NULL, 0),
       (634, 1, 36, 1, '2022-06-01 17:57:44', NULL, 11, NULL, 0),
       (635, 1, 37, 1, '2022-06-01 17:57:44', NULL, 11, NULL, 0),
       (636, 1, 38, 1, '2022-06-01 17:57:44', NULL, 11, NULL, 0),
       (637, 1, 40, 1, '2022-06-02 10:10:18', NULL, 6, NULL, 0),
       (638, 1, 41, 1, '2022-06-02 10:10:18', NULL, 6, NULL, 0),
       (639, 2, 40, 1, '2022-06-10 17:28:55', NULL, 11, NULL, 0),
       (640, 2, 41, 1, '2022-06-10 17:28:55', NULL, 11, NULL, 0),
       (641, 3, 40, 1, '2022-06-10 17:29:07', NULL, 11, NULL, 0),
       (642, 3, 41, 1, '2022-06-10 17:29:07', NULL, 11, NULL, 0),
       (643, 3, 2, 1, '2022-06-14 18:04:05', NULL, 1, NULL, 0),
       (644, 3, 35, 1, '2022-06-14 18:04:05', NULL, 1, NULL, 0),
       (645, 3, 36, 1, '2022-06-14 18:04:05', NULL, 1, NULL, 0),
       (646, 3, 4, 1, '2022-06-14 18:04:05', NULL, 1, NULL, 0),
       (647, 3, 37, 1, '2022-06-14 18:04:05', NULL, 1, NULL, 0),
       (648, 3, 5, 1, '2022-06-14 18:04:05', NULL, 1, NULL, 0),
       (649, 3, 38, 1, '2022-06-14 18:04:05', NULL, 1, NULL, 0),
       (650, 3, 18, 1, '2022-06-14 18:04:05', NULL, 1, NULL, 0),
       (651, 3, 19, 1, '2022-06-14 18:04:05', NULL, 1, NULL, 0),
       (652, 3, 20, 1, '2022-06-14 18:04:05', NULL, 1, NULL, 0),
       (653, 3, 21, 1, '2022-06-14 18:04:05', NULL, 1, NULL, 0),
       (654, 3, 22, 1, '2022-06-14 18:04:05', NULL, 1, NULL, 0),
       (655, 3, 29, 1, '2022-06-14 18:04:05', NULL, 1, NULL, 0),
       (656, 3, 31, 1, '2022-06-14 18:04:05', NULL, 1, NULL, 0),
       (657, 1, 42, 1, '2022-06-14 18:13:20', NULL, 527, NULL, 0),
       (658, 1, 43, 1, '2022-06-14 18:13:20', NULL, 527, NULL, 0),
       (659, 1, 44, 1, '2022-06-14 18:13:20', NULL, 527, NULL, 0),
       (660, 1, 45, 1, '2022-06-14 18:13:20', NULL, 527, NULL, 0),
       (661, 1, 46, 1, '2022-06-14 18:14:27', NULL, 527, NULL, 0),
       (662, 1, 50, 1, '2022-06-14 18:14:27', NULL, 527, NULL, 0),
       (663, 1, 51, 1, '2022-06-14 18:14:27', NULL, 527, NULL, 0),
       (664, 1, 52, 1, '2022-06-15 17:41:00', NULL, 6, NULL, 0),
       (665, 1, 53, 1, '2022-06-15 17:46:39', NULL, 6, NULL, 0),
       (666, 4, 2, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (667, 4, 4, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (668, 4, 6, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (669, 4, 7, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (670, 4, 8, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (671, 4, 9, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (672, 4, 10, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (673, 4, 11, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (674, 4, 12, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (675, 4, 13, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (676, 4, 14, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (677, 4, 15, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (678, 4, 16, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (679, 4, 17, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (680, 4, 18, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (681, 4, 19, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (682, 4, 20, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (683, 4, 21, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (684, 4, 22, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (685, 4, 23, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (686, 4, 24, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (687, 4, 25, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (688, 4, 26, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (689, 4, 27, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (690, 4, 28, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (691, 4, 29, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (692, 4, 30, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (693, 4, 31, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (694, 4, 32, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (695, 4, 34, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (696, 4, 35, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (697, 4, 36, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (698, 4, 37, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (699, 4, 38, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (700, 4, 40, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (701, 4, 41, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (702, 4, 42, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (703, 4, 43, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (704, 4, 44, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (705, 4, 45, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (706, 4, 46, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (707, 4, 50, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (708, 4, 51, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (709, 4, 52, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (710, 4, 53, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (711, 5, 32, 1, '2022-06-21 14:09:41', NULL, 6, NULL, 0),
       (712, 5, 52, 1, '2022-06-21 14:09:41', NULL, 6, NULL, 0),
       (713, 5, 53, 1, '2022-06-21 14:09:41', NULL, 6, NULL, 0),
       (714, 5, 40, 1, '2022-06-21 14:09:41', NULL, 6, NULL, 0),
       (715, 5, 41, 1, '2022-06-21 14:09:41', NULL, 6, NULL, 0),
       (716, 5, 26, 1, '2022-06-21 14:09:41', NULL, 6, NULL, 0),
       (717, 5, 28, 1, '2022-06-21 14:09:41', NULL, 6, NULL, 0),
       (718, 1, 54, 1, '2022-06-21 14:56:54', NULL, 14, NULL, 0),
       (719, 1, 55, 1, '2022-06-21 14:56:54', NULL, 14, NULL, 0),
       (720, 1, 56, 1, '2022-06-21 14:56:54', NULL, 14, NULL, 0),
       (721, 1, 57, 1, '2022-07-11 18:17:10', NULL, 8, NULL, 0),
       (722, 1, 58, 1, '2022-07-11 18:17:10', NULL, 8, NULL, 0),
       (723, 1, 59, 1, '2022-07-11 18:17:10', NULL, 8, NULL, 0),
       (724, 1, 60, 1, '2022-07-11 18:17:10', NULL, 8, NULL, 0),
       (725, 1, 61, 1, '2022-07-11 18:17:10', NULL, 8, NULL, 0),
       (726, 1, 62, 1, '2022-07-11 18:17:10', NULL, 8, NULL, 0),
       (727, 1, 63, 1, '2022-07-11 18:17:10', NULL, 8, NULL, 0),
       (728, 1, 64, 1, '2022-07-11 18:17:10', NULL, 8, NULL, 0),
       (729, 1, 65, 1, '2022-07-11 18:17:10', NULL, 8, NULL, 0),
       (730, 1, 66, 1, '2022-07-11 18:17:10', NULL, 8, NULL, 0),
       (731, 1, 67, 1, '2022-07-11 18:17:10', NULL, 8, NULL, 0);
/*!40000 ALTER TABLE `role_menu`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_permission`
--

DROP TABLE IF EXISTS `role_permission`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_permission`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT,
    `role_id`       bigint(20) NOT NULL COMMENT '角色Id',
    `permission_id` bigint(20) NOT NULL COMMENT '权限ID',
    `system_id`     bigint(20) NOT NULL COMMENT '系统id',
    `create_time`   datetime   NOT NULL,
    `update_time`   datetime   DEFAULT NULL,
    `create_user`   bigint(20) NOT NULL,
    `update_user`   bigint(20) DEFAULT NULL,
    `is_deleted`    tinyint(1) NOT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2468
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC COMMENT ='系统角色权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_permission`
--

LOCK TABLES `role_permission` WRITE;
/*!40000 ALTER TABLE `role_permission`
    DISABLE KEYS */;
INSERT INTO `role_permission`
VALUES (2060, 1, 176, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 1),
       (2062, 1, 178, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2063, 1, 179, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2064, 1, 182, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2065, 1, 183, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2066, 1, 184, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2067, 1, 189, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2068, 1, 191, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2069, 1, 192, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2070, 1, 193, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2071, 1, 194, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2072, 1, 195, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2073, 1, 196, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2074, 1, 197, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2075, 1, 198, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2076, 1, 185, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2077, 1, 199, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2078, 1, 200, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2079, 1, 180, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2080, 1, 181, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2081, 1, 186, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2082, 1, 187, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2083, 1, 188, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2084, 1, 190, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2085, 1, 154, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2086, 1, 160, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2087, 1, 162, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2088, 1, 166, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2089, 1, 167, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2091, 1, 172, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2092, 1, 153, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2093, 1, 156, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2094, 1, 157, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2095, 1, 161, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2096, 1, 171, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2097, 1, 159, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2098, 1, 169, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2099, 1, 155, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2100, 1, 158, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2101, 1, 163, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2102, 1, 164, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2103, 1, 165, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2104, 1, 170, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2105, 1, 173, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2106, 1, 174, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2107, 1, 175, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2108, 1, 128, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2109, 1, 131, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2110, 1, 40, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2112, 1, 42, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2113, 1, 44, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2114, 1, 50, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2115, 1, 51, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2116, 1, 28, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2117, 1, 29, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2118, 1, 30, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2119, 1, 39, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2120, 1, 43, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2121, 1, 36, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2122, 1, 37, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2123, 1, 47, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2124, 1, 48, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2125, 1, 49, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2126, 1, 32, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2127, 1, 33, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2128, 1, 34, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2129, 1, 35, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2130, 1, 46, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2131, 1, 52, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2132, 1, 53, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2133, 1, 54, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2138, 1, 206, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2139, 1, 112, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2140, 1, 124, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2141, 1, 125, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2142, 1, 126, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2143, 1, 137, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2144, 1, 138, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2145, 1, 139, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2146, 1, 144, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2147, 1, 145, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2148, 1, 146, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2149, 1, 147, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2150, 1, 149, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2151, 1, 113, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2152, 1, 114, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2153, 1, 115, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2154, 1, 116, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2155, 1, 127, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2156, 1, 133, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2157, 1, 134, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2158, 1, 136, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2159, 1, 201, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2160, 1, 109, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2161, 1, 110, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2162, 1, 111, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2163, 1, 122, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2164, 1, 123, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2165, 1, 202, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2166, 1, 120, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2167, 1, 121, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2168, 1, 129, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2169, 1, 132, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2170, 1, 140, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2171, 1, 141, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2172, 1, 142, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2173, 1, 143, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2174, 1, 148, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2175, 1, 150, 1, '2022-05-21 15:27:22', NULL, 8, NULL, 0),
       (2390, 1, 177, 1, '2022-05-23 14:21:10', NULL, 1, NULL, 0),
       (2391, 1, 41, 1, '2022-05-23 14:22:20', NULL, 1, NULL, 0),
       (2392, 1, 168, 1, '2022-05-23 16:02:49', NULL, 1, NULL, 0),
       (2393, 1, 211, 1, '2022-05-27 14:54:29', NULL, 1, NULL, 0),
       (2394, 1, 212, 1, '2022-05-27 14:54:29', NULL, 1, NULL, 0),
       (2395, 1, 213, 1, '2022-06-01 17:57:44', NULL, 11, NULL, 0),
       (2396, 1, 214, 1, '2022-06-01 17:57:44', NULL, 11, NULL, 0),
       (2397, 1, 215, 1, '2022-06-01 17:57:44', NULL, 11, NULL, 0),
       (2398, 1, 216, 1, '2022-06-01 17:57:44', NULL, 11, NULL, 0),
       (2399, 1, 217, 1, '2022-06-01 17:57:44', NULL, 11, NULL, 0),
       (2400, 1, 219, 1, '2022-06-01 17:57:44', NULL, 11, NULL, 0),
       (2401, 1, 222, 1, '2022-06-10 17:27:00', NULL, 11, NULL, 0),
       (2402, 1, 223, 1, '2022-06-10 17:27:00', NULL, 11, NULL, 0),
       (2403, 1, 224, 1, '2022-06-10 17:27:00', NULL, 11, NULL, 0),
       (2404, 1, 225, 1, '2022-06-10 17:27:00', NULL, 11, NULL, 0),
       (2405, 1, 226, 1, '2022-06-10 17:27:00', NULL, 11, NULL, 0),
       (2406, 1, 227, 1, '2022-06-10 17:27:00', NULL, 11, NULL, 0),
       (2407, 1, 228, 1, '2022-06-10 17:27:00', NULL, 11, NULL, 0),
       (2408, 1, 229, 1, '2022-06-10 17:27:00', NULL, 11, NULL, 0),
       (2409, 1, 230, 1, '2022-06-10 17:27:00', NULL, 11, NULL, 0),
       (2410, 1, 231, 1, '2022-06-10 17:27:00', NULL, 11, NULL, 0),
       (2411, 1, 232, 1, '2022-06-10 17:27:00', NULL, 11, NULL, 0),
       (2412, 1, 233, 1, '2022-06-10 17:27:00', NULL, 11, NULL, 0),
       (2413, 1, 234, 1, '2022-06-10 17:27:00', NULL, 11, NULL, 0),
       (2414, 2, 224, 1, '2022-06-10 17:28:55', NULL, 11, NULL, 0),
       (2415, 2, 225, 1, '2022-06-10 17:28:55', NULL, 11, NULL, 0),
       (2416, 2, 226, 1, '2022-06-10 17:28:55', NULL, 11, NULL, 0),
       (2417, 2, 227, 1, '2022-06-10 17:28:55', NULL, 11, NULL, 0),
       (2418, 2, 228, 1, '2022-06-10 17:28:55', NULL, 11, NULL, 0),
       (2419, 2, 229, 1, '2022-06-10 17:28:55', NULL, 11, NULL, 0),
       (2420, 2, 230, 1, '2022-06-10 17:28:55', NULL, 11, NULL, 0),
       (2421, 2, 231, 1, '2022-06-10 17:28:55', NULL, 11, NULL, 0),
       (2422, 2, 232, 1, '2022-06-10 17:28:55', NULL, 11, NULL, 0),
       (2423, 2, 233, 1, '2022-06-10 17:28:55', NULL, 11, NULL, 0),
       (2424, 2, 234, 1, '2022-06-10 17:28:55', NULL, 11, NULL, 0),
       (2425, 2, 222, 1, '2022-06-10 17:28:55', NULL, 11, NULL, 0),
       (2426, 2, 223, 1, '2022-06-10 17:28:55', NULL, 11, NULL, 0),
       (2427, 3, 224, 1, '2022-06-10 17:29:07', NULL, 11, NULL, 0),
       (2428, 3, 225, 1, '2022-06-10 17:29:07', NULL, 11, NULL, 0),
       (2429, 3, 226, 1, '2022-06-10 17:29:07', NULL, 11, NULL, 0),
       (2430, 3, 227, 1, '2022-06-10 17:29:07', NULL, 11, NULL, 0),
       (2431, 3, 228, 1, '2022-06-10 17:29:07', NULL, 11, NULL, 0),
       (2432, 3, 229, 1, '2022-06-10 17:29:07', NULL, 11, NULL, 0),
       (2433, 3, 230, 1, '2022-06-10 17:29:07', NULL, 11, NULL, 0),
       (2434, 3, 231, 1, '2022-06-10 17:29:07', NULL, 11, NULL, 0),
       (2435, 3, 232, 1, '2022-06-10 17:29:07', NULL, 11, NULL, 0),
       (2436, 3, 233, 1, '2022-06-10 17:29:07', NULL, 11, NULL, 0),
       (2437, 3, 234, 1, '2022-06-10 17:29:07', NULL, 11, NULL, 0),
       (2438, 3, 222, 1, '2022-06-10 17:29:07', NULL, 11, NULL, 0),
       (2439, 3, 223, 1, '2022-06-10 17:29:07', NULL, 11, NULL, 0),
       (2440, 1, 249, 1, '2022-06-14 18:13:20', NULL, 527, NULL, 0),
       (2441, 1, 250, 1, '2022-06-14 18:13:20', NULL, 527, NULL, 0),
       (2442, 1, 251, 1, '2022-06-14 18:13:20', NULL, 527, NULL, 0),
       (2443, 1, 252, 1, '2022-06-14 18:13:20', NULL, 527, NULL, 0),
       (2444, 1, 130, 1, '2022-06-17 17:10:27', NULL, 1, NULL, 0),
       (2445, 1, 220, 1, '2022-06-17 17:14:41', NULL, 1, NULL, 0),
       (2446, 1, 260, 1, '2022-06-17 17:15:29', NULL, 6, NULL, 0),
       (2447, 1, 261, 1, '2022-06-17 17:15:29', NULL, 6, NULL, 0),
       (2448, 4, 224, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (2449, 4, 225, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (2450, 4, 226, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (2451, 4, 227, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (2452, 4, 228, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (2453, 4, 229, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (2454, 4, 230, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (2455, 4, 231, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (2456, 4, 232, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (2457, 4, 233, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (2458, 4, 234, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (2459, 4, 222, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (2460, 4, 223, 1, '2022-06-20 22:43:27', NULL, 10, NULL, 0),
       (2461, 5, 200, 1, '2022-06-21 14:09:41', NULL, 6, NULL, 0),
       (2462, 1, 262, 1, '2022-07-01 14:27:16', NULL, 6, NULL, 0),
       (2463, 1, 263, 1, '2022-07-01 14:27:16', NULL, 6, NULL, 0),
       (2464, 1, 264, 1, '2022-07-01 14:27:16', NULL, 6, NULL, 0),
       (2465, 1, 268, 1, '2022-07-01 14:27:16', NULL, 6, NULL, 0),
       (2466, 1, 269, 1, '2022-07-01 14:27:16', NULL, 6, NULL, 0),
       (2467, 1, 270, 1, '2022-07-01 14:27:16', NULL, 6, NULL, 0);
/*!40000 ALTER TABLE `role_permission`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system`
--

DROP TABLE IF EXISTS `system`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `system`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '系统id',
    `system_code` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '系统标识',
    `system_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '系统名称',
    `create_time` datetime   NOT NULL COMMENT '创建时间',
    `update_time` datetime                               DEFAULT NULL COMMENT '修改时间',
    `create_user` bigint(20)                             DEFAULT NULL COMMENT '创建用户',
    `update_user` bigint(20)                             DEFAULT NULL COMMENT '修改用户',
    `is_deleted`  tinyint(1) NOT NULL COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='系统表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system`
--

LOCK TABLES `system` WRITE;
/*!40000 ALTER TABLE `system`
    DISABLE KEYS */;
INSERT INTO `system`
VALUES (1, 'yuanxuan', '严选商城', '2022-05-21 12:00:00', NULL, 0, NULL, 0);
/*!40000 ALTER TABLE `system`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_client`
--

DROP TABLE IF EXISTS `system_client`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `system_client`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '客户端id',
    `system_id`   bigint(20) NOT NULL COMMENT '系统id',
    `client_code` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '客户端标识',
    `client_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '客户端名称',
    `is_enable`   tinyint(1)                             DEFAULT NULL COMMENT '0 不可用  1可用',
    `update_time` datetime                               DEFAULT NULL COMMENT '修改时间',
    `create_user` bigint(20)                             DEFAULT NULL COMMENT '创建用户',
    `update_user` bigint(20)                             DEFAULT NULL COMMENT '修改用户',
    `is_deleted`  tinyint(1) NOT NULL COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='系统客户端表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_client`
--

LOCK TABLES `system_client` WRITE;
/*!40000 ALTER TABLE `system_client`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `system_client`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user`
(
    `id`              bigint(20)                             NOT NULL AUTO_INCREMENT COMMENT '用户id',
    `account`         varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '账号',
    `nickname`        varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '昵称',
    `avatar`          varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像',
    `gender`          tinyint(1)                              DEFAULT NULL COMMENT '性别 1：男 2 女   3，未知',
    `phone`           varchar(13) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '手机号',
    `password`        varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
    `id_card`         varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '证件号码',
    `source`          tinyint(1)                              DEFAULT NULL COMMENT '用户注册来源 待定义',
    `system_id`       int(11)                                NOT NULL,
    `client_id`       int(11)                                NOT NULL,
    `union_id`        varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'unionid',
    `is_enable`       tinyint(1)                              DEFAULT NULL COMMENT '0 不可用  1可用',
    `last_login_time` datetime                               NOT NULL COMMENT '最后一次登录时间',
    `create_time`     datetime                               NOT NULL COMMENT '创建时间',
    `update_time`     datetime                                DEFAULT NULL COMMENT '修改时间',
    `create_user`     bigint(20)                              DEFAULT NULL COMMENT '创建用户',
    `update_user`     bigint(20)                              DEFAULT NULL COMMENT '修改用户',
    `is_deleted`      tinyint(1)                             NOT NULL COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1216
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='用户';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user`
    DISABLE KEYS */;
INSERT INTO `user`
VALUES (1, '', '内部测试账号', 'https://msb-edu-dev.oss-cn-beijing.aliyuncs.com/uc/account-avatar/120x120.png', 2,
        '15338807445', '', '', NULL, 1, 1, '', 1, '2022-07-14 16:46:10', '2022-05-05 16:12:40', '2022-07-14 16:46:10',
        1, 8, 0),
       (2, '', 'LOUIE',
        'https://msb-edu-prod.oss-cn-beijing.aliyuncs.com/uc/account-avatar/e653faed0e654081916f3f1ead4a90ba.gif', 1,
        '13347335633', '', '', NULL, 1, 1, '', 1, '2022-06-17 20:58:32', '2022-05-05 16:57:37', '2022-07-12 15:30:52',
        2, 2, 0);
/*!40000 ALTER TABLE `user`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_login_limit`
--

DROP TABLE IF EXISTS `user_login_limit`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_login_limit`
(
    `id`               bigint(20)       NOT NULL AUTO_INCREMENT COMMENT '用户id',
    `system_id`        int(10) unsigned NOT NULL COMMENT '系统id',
    `system_client_id` int(10) unsigned NOT NULL COMMENT '系统客户端id',
    `login_limit_num`  int(10) unsigned NOT NULL COMMENT '登录端限制个数',
    `beyond_mode`      tinyint(4)       NOT NULL COMMENT '超出限制后，1，拒绝登录，2，挤掉最老的一个会话',
    `create_time`      datetime         NOT NULL COMMENT '创建时间',
    `update_time`      datetime   DEFAULT NULL COMMENT '修改时间',
    `create_user`      bigint(20) DEFAULT NULL COMMENT '创建用户',
    `update_user`      bigint(20) DEFAULT NULL COMMENT '修改用户',
    `is_deleted`       tinyint(1)       NOT NULL COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='用户登录限制';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_login_limit`
--

LOCK TABLES `user_login_limit` WRITE;
/*!40000 ALTER TABLE `user_login_limit`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `user_login_limit`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_login_record`
--

DROP TABLE IF EXISTS `user_login_record`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_login_record`
(
    `id`          bigint(20)                               NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `user_id`     bigint(20)                               NOT NULL COMMENT '用户id',
    `token`       varchar(1000) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '登录下放token',
    `system_id`   int(11)                                  NOT NULL,
    `client_id`   int(4)                                   NOT NULL COMMENT '登录来源 待定义 1学习平台2严选商城3某某后管  登录哪个系统',
    `ip`          varchar(50) COLLATE utf8mb4_unicode_ci   NOT NULL COMMENT 'ip',
    `create_time` datetime                                 NOT NULL COMMENT '创建时间',
    `update_time` datetime   DEFAULT NULL COMMENT '修改时间',
    `create_user` bigint(20) DEFAULT NULL COMMENT '创建用户',
    `update_user` bigint(20) DEFAULT NULL COMMENT '修改用户',
    `is_deleted`  tinyint(1)                               NOT NULL COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3613
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='用户登录记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_system_relation`
--

DROP TABLE IF EXISTS `user_system_relation`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_system_relation`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `user_id`     bigint(20) NOT NULL COMMENT '用户id',
    `system_id`   bigint(20) NOT NULL COMMENT '系统id',
    `create_time` datetime   NOT NULL,
    `update_time` datetime   DEFAULT NULL,
    `create_user` bigint(20) NOT NULL,
    `update_user` bigint(20) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1212
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='用户系统关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `visitor_pv_statistics`
--

DROP TABLE IF EXISTS `visitor_pv_statistics`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `visitor_pv_statistics`
(
    `id`          int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '访客统计id',
    `record_date` date             NOT NULL COMMENT '记录日期',
    `count`       int(10) unsigned NOT NULL COMMENT '访客数量',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 194
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='访客pv统计表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `visitor_uv_statistics`
--

DROP TABLE IF EXISTS `visitor_uv_statistics`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `visitor_uv_statistics`
(
    `id`          int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '访客统计id',
    `record_date` date             NOT NULL COMMENT '记录日期',
    `count`       int(10) unsigned NOT NULL COMMENT '访客数量',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 216
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='访客uv统计表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `visitor_uv_statistics`
--
-- Dump completed on 2022-07-19 14:29:43