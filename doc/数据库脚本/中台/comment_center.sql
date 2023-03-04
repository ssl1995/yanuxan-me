/*
SQLyog Enterprise v12.09 (64 bit)
MySQL - 5.7.34 : Database - comment_center
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`comment_center` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;

USE `comment_center`;

/*Table structure for table `comment` */

DROP TABLE IF EXISTS `comment`;

CREATE TABLE `comment` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `main_id` bigint(20) DEFAULT NULL COMMENT '主体id',
  `comment_content` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '评论内容',
  `is_default_comment` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否默认评价（0-否 1-是）',
  `is_show` tinyint(1) NOT NULL COMMENT '是否显示（0-否 1-是）',
  `is_deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除（0-否 1-是）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=86 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论表';

/*Table structure for table `comment_expand` */

DROP TABLE IF EXISTS `comment_expand`;

CREATE TABLE `comment_expand` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `comment_id` int(10) unsigned NOT NULL COMMENT '评论id',
  `type` tinyint(1) NOT NULL COMMENT '类型（1-图片 2-视频）',
  `url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '地址',
  `is_deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除（0-否 1-是）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=88 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论扩展表';

/*Table structure for table `comment_reply` */

DROP TABLE IF EXISTS `comment_reply`;

CREATE TABLE `comment_reply` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `comment_id` bigint(20) DEFAULT NULL COMMENT '评论id',
  `parent_id` bigint(20) DEFAULT NULL COMMENT 'parent id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `replied_user_id` bigint(20) NOT NULL COMMENT '被回复用户id',
  `reply_content` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '回复内容',
  `is_show` tinyint(1) NOT NULL COMMENT '是否显示（0-否 1-是）',
  `is_deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除（0-否 1-是）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=86 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论回复表';

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
