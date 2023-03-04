/*
SQLyog Enterprise v12.09 (64 bit)
MySQL - 5.7.34 : Database - mall_base
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`mall_base` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `mall_base`;

/*Table structure for table `dictionary` */

DROP TABLE IF EXISTS `dictionary`;

CREATE TABLE `dictionary` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字典分类，使用大写驼峰命名',
  `desc` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '描述',
  `code` tinyint(3) unsigned NOT NULL COMMENT '编码值，编码值应无任何业务意义，使用1，2，3...设值',
  `text` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文本',
  `is_enable` tinyint(1) DEFAULT '1' COMMENT '状态（0-停用，1-开启）',
  `create_time` datetime DEFAULT NULL,
  `create_user` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '填写创建人的名字',
  `update_time` datetime DEFAULT NULL,
  `update_user` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `edu_dictionary_type_code_uindex` (`type`,`code`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据字典表（任何有可能修改的以及有业务含义的数据字典或者前端需要用来展示下拉框）';

/*Table structure for table `order_config` */

DROP TABLE IF EXISTS `order_config`;

CREATE TABLE `order_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单配置id',
  `order_pay_expire` int(10) NOT NULL COMMENT '订单支付失效时长(分钟)',
  `automatic_receipt` int(10) NOT NULL COMMENT '自动收货时长(天)',
  `after_sales_expire` int(10) NOT NULL COMMENT '售后过期时长(天)',
  `after_sales_application_expire` int(10) NOT NULL COMMENT '售后申请过期时长(天)',
  `return_goods_expire` int(10) NOT NULL COMMENT '退货到期时长(天)',
  `merchant_receipt_expire` int(10) NOT NULL COMMENT '商家收货到期时长(天)',
  `automatic_praise` int(10) NOT NULL COMMENT '自动好评时长(天)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单配置表';

/*Table structure for table `receive_address` */

DROP TABLE IF EXISTS `receive_address`;

CREATE TABLE `receive_address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `name` varchar(20) NOT NULL COMMENT '收货人姓名',
  `phone` varchar(13) NOT NULL COMMENT '收货人手机',
  `province_code` varchar(20) NOT NULL COMMENT '省区域代码',
  `province` varchar(20) NOT NULL COMMENT '省',
  `city_code` varchar(20) NOT NULL COMMENT '市区域代码',
  `city` varchar(20) NOT NULL COMMENT '市',
  `area_code` varchar(20) NOT NULL COMMENT '县区域代码',
  `area` varchar(20) NOT NULL COMMENT '县',
  `detail_address` varchar(50) NOT NULL COMMENT '详细地址',
  `is_default` tinyint(1) NOT NULL COMMENT '是否默认',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_user` bigint(20) NOT NULL,
  `update_user` bigint(20) DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=211 DEFAULT CHARSET=utf8mb4 COMMENT='收货地址';

/*Table structure for table `undo_log` */

DROP TABLE IF EXISTS `undo_log`;

CREATE TABLE `undo_log` (
  `branch_id` bigint(20) NOT NULL COMMENT 'branch transaction id',
  `xid` varchar(128) NOT NULL COMMENT 'global transaction id',
  `context` varchar(128) NOT NULL COMMENT 'undo_log context,such as serialization',
  `rollback_info` longblob NOT NULL COMMENT 'rollback info',
  `log_status` int(11) NOT NULL COMMENT '0:normal status,1:defense status',
  `log_created` datetime(6) NOT NULL COMMENT 'create datetime',
  `log_modified` datetime(6) NOT NULL COMMENT 'modify datetime',
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='AT transaction mode undo table';

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
