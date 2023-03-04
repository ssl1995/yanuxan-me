/*
SQLyog Enterprise v12.09 (64 bit)
MySQL - 5.7.34 : Database - im_center
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`im_center` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `im_center`;

/*Table structure for table `invalid_ticket` */

DROP TABLE IF EXISTS `invalid_ticket`;

CREATE TABLE `invalid_ticket` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `last_timestamp` bigint(20) unsigned DEFAULT NULL COMMENT '最后操作时间戳',
  `sys_id` int(10) unsigned DEFAULT NULL COMMENT '系统id',
  `from_id` varchar(64) DEFAULT NULL COMMENT '操作发起人',
  `type` varchar(30) DEFAULT NULL COMMENT '操作类型',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `ticket_idx` (`last_timestamp`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*Table structure for table `message` */

DROP TABLE IF EXISTS `message`;

CREATE TABLE `message` (
  `id` int(10) unsigned NOT NULL,
  `from_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '消息发送人',
  `message_index` int(10) unsigned NOT NULL COMMENT '消息在会话中的id 在当前会话中是连续的',
  `session_id` int(10) unsigned NOT NULL COMMENT '会话id',
  `type` tinyint(3) unsigned NOT NULL COMMENT '消息类型 1-文本 2-语音 3-图片 4-视频 5-撤回消息',
  `payload` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发送的消息体',
  `create_time_stamp` bigint(20) NOT NULL COMMENT '消息创建的时间戳',
  `is_deleted` tinyint(1) DEFAULT NULL COMMENT '是否删除 1-是 0-否',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `session_idx` (`session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

/*Table structure for table `session` */

DROP TABLE IF EXISTS `session`;

CREATE TABLE `session` (
  `id` int(11) NOT NULL,
  `sys_id` tinyint(3) unsigned NOT NULL COMMENT '1-严选商城',
  `type` tinyint(3) unsigned NOT NULL COMMENT '1-单聊会话 2-群聊会话',
  `group_avatar` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '群头像',
  `group_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '群名称',
  `create_user` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `update_user` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT NULL COMMENT '是否删除 1-是 0-否',
  `update_time_stamp` bigint(20) DEFAULT NULL COMMENT '更新时间戳',
  `payload` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '会话自带信息',
  `create_time_stamp` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

/*Table structure for table `session_user` */

DROP TABLE IF EXISTS `session_user`;

CREATE TABLE `session_user` (
  `id` int(10) unsigned NOT NULL,
  `session_id` int(10) unsigned NOT NULL COMMENT '会话id',
  `user_id` varchar(10) NOT NULL COMMENT '用户id',
  `un_read_count` int(10) unsigned DEFAULT NULL COMMENT '未读消息数量',
  `is_group_owner` tinyint(1) DEFAULT NULL COMMENT '是否群主 1-是 0-否',
  `is_deleted` tinyint(1) DEFAULT NULL COMMENT '是否删除 1-是 0-否',
  `create_user` varchar(64) NOT NULL,
  `update_user` varchar(64) NOT NULL,
  `update_time_stamp` bigint(20) DEFAULT NULL COMMENT '更新时间戳',
  `user_nickname` varchar(255) DEFAULT NULL COMMENT '用户名称',
  `user_avatar` varchar(255) DEFAULT NULL COMMENT '用户头像',
  `relation_user_id` varchar(11) DEFAULT NULL COMMENT '单人会话关联用户id',
  `create_time_stamp` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `session_user_idx` (`session_id`,`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*Table structure for table `store_config` */

DROP TABLE IF EXISTS `store_config`;

CREATE TABLE `store_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sys_id` int(11) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `create_user` varchar(64) DEFAULT NULL,
  `update_user` varchar(64) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT NULL COMMENT '是否删除 1-是 0-否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*Table structure for table `store_waiter` */

DROP TABLE IF EXISTS `store_waiter`;

CREATE TABLE `store_waiter` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `store_id` int(11) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `create_user` varchar(64) DEFAULT NULL,
  `update_user` varchar(64) DEFAULT NULL,
  `waiter_nickname` varchar(255) DEFAULT NULL COMMENT '客服昵称',
  `waiter_avatar` varchar(255) DEFAULT NULL COMMENT '客服头像',
  `type` varchar(30) DEFAULT NULL COMMENT '客服类型 接入系统提供',
  `waiter_id` varchar(64) DEFAULT NULL COMMENT '接入系统提供的客服id',
  `is_deleted` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='店铺下的客服';

/*Table structure for table `third_system_config` */

DROP TABLE IF EXISTS `third_system_config`;

CREATE TABLE `third_system_config` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `create_user` varchar(64) NOT NULL,
  `update_user` varchar(64) NOT NULL,
  `is_deleted` tinyint(1) DEFAULT NULL COMMENT '是否删除 1-是 0-否',
  `secret` varchar(255) DEFAULT NULL COMMENT '第三方连接参数ticket，加解密时使用',
  `client` varchar(255) DEFAULT NULL COMMENT '外部系统的标识',
  `name` varchar(20) DEFAULT NULL COMMENT '外部系统名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4;

/*Table structure for table `user_waiter` */

DROP TABLE IF EXISTS `user_waiter`;

CREATE TABLE `user_waiter` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` varchar(64) DEFAULT NULL COMMENT '用户id',
  `store_id` varchar(64) DEFAULT NULL COMMENT '店铺id',
  `waiter_id` varchar(64) DEFAULT NULL COMMENT '客服id',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `create_user` varchar(64) NOT NULL,
  `update_user` varchar(64) NOT NULL,
  `session_id` int(10) unsigned DEFAULT NULL COMMENT '会话id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `user_store_idx` (`user_id`,`store_id`) USING BTREE,
  UNIQUE KEY `session_idx` (`session_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户和客服实时绑定关系';

/*Table structure for table `user_waiter_history_log` */

DROP TABLE IF EXISTS `user_waiter_history_log`;

CREATE TABLE `user_waiter_history_log` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` varchar(64) DEFAULT NULL COMMENT '用户id',
  `store_id` int(64) unsigned DEFAULT NULL COMMENT '店铺id',
  `waiter_id` varchar(64) DEFAULT NULL COMMENT '客服id',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `create_user` varchar(64) NOT NULL,
  `update_user` varchar(64) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=202 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
