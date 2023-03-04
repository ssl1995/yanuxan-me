/*
SQLyog Enterprise v12.09 (64 bit)
MySQL - 5.7.34 : Database - mall_comment
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`mall_comment` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;

USE `mall_comment`;

/*Table structure for table `comment` */

DROP TABLE IF EXISTS `comment`;

CREATE TABLE `comment` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `origin_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '来源id',
  `parent_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '父id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `product_id` bigint(20) DEFAULT NULL COMMENT '商品id',
  `order_product_id` bigint(20) DEFAULT NULL COMMENT '订单详情id',
  `comment_content` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '评论内容',
  `comment_score` tinyint(1) DEFAULT NULL COMMENT '评价分值（1-5星）',
  `picture_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '图片地址',
  `label` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '评论标签（1-晒图 2-追评）',
  `useful_count` int(10) NOT NULL DEFAULT '0' COMMENT '有用数',
  `comment_type` tinyint(1) NOT NULL COMMENT '评论类型（1-评论 2-追评 3-回复）',
  `is_show` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '是否显示（0-否 1-是）',
  `is_default_comment` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否默认评价（0-否 1-是）',
  `is_deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除（0-否 1-是）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  KEY `index_order_product_id` (`order_product_id`) USING BTREE,
  KEY `index_comment_type` (`comment_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=157 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `merchant_comment` */

DROP TABLE IF EXISTS `merchant_comment`;

CREATE TABLE `merchant_comment` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `origin_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '来源id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `comment_content` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '评论内容',
  `is_show` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '是否显示（0-否 1-是）',
  `is_deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除（0-否 1-是）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商家评论';

/*Table structure for table `product_comment` */

DROP TABLE IF EXISTS `product_comment`;

CREATE TABLE `product_comment` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `product_id` bigint(20) DEFAULT NULL COMMENT '商品id',
  `comment_id` bigint(20) DEFAULT NULL COMMENT '评论id',
  `follow_comment_id` bigint(20) DEFAULT NULL COMMENT '追评id',
  `comment_score` tinyint(1) DEFAULT NULL COMMENT '评价分值（1-5星）',
  `is_show` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '是否显示（0-否 1-是）',
  `is_show_picture` tinyint(1) unsigned DEFAULT '0' COMMENT '是否晒图（0-否 1-是）',
  `is_follow` tinyint(1) unsigned DEFAULT '0' COMMENT '是否追评（0-否 1-是）',
  `is_deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除（0-否 1-是）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  KEY `index_product_id` (`product_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品评论表';

/*Table structure for table `store_comment` */

DROP TABLE IF EXISTS `store_comment`;

CREATE TABLE `store_comment` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `comment_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '评论id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `comment_content` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '评论内容',
  `is_show` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '是否显示（0-否 1-是）',
  `is_deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除（0-否 1-是）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商家评论';

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
