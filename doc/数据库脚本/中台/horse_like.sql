/*
SQLyog Enterprise v12.09 (64 bit)
MySQL - 5.7.34 : Database - horse_like
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`horse_like` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;

USE `horse_like`;

/*Table structure for table `likes` */

DROP TABLE IF EXISTS `likes`;

CREATE TABLE `likes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '点赞id',
  `system_id` bigint(20) NOT NULL COMMENT '系统id',
  `scenes_id` bigint(20) NOT NULL COMMENT '场景id',
  `business_id` bigint(20) NOT NULL COMMENT '业务id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `create_time` datetime NOT NULL,
  `create_user` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_user` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='点赞表';

/*Table structure for table `scenes` */

DROP TABLE IF EXISTS `scenes`;

CREATE TABLE `scenes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '场景id',
  `scenes_code` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '场景标识',
  `scenes_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '场景名称',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_user` bigint(20) DEFAULT NULL COMMENT '创建用户',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '更新用户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='场景表';

/*Table structure for table `system` */

DROP TABLE IF EXISTS `system`;

CREATE TABLE `system` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '系统id',
  `system_code` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '系统标识',
  `system_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '系统名称',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_user` bigint(20) DEFAULT NULL COMMENT '创建用户',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '更新用户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统表';

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
