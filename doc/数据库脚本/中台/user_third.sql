/*
SQLyog Enterprise v12.09 (64 bit)
MySQL - 5.7.34 : Database - user_third
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`user_third` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;

USE `user_third`;

/*Table structure for table `third_info` */

DROP TABLE IF EXISTS `third_info`;

CREATE TABLE `third_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `platform_type` tinyint(1) NOT NULL COMMENT '平台类型（1：微信公众号）',
  `app_id` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '平台应用ID',
  `app_user_id` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '平台用户ID',
  `app_union_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '平台用户唯一标识',
  `is_subscribe` tinyint(1) DEFAULT NULL COMMENT '是否关注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `AK_app_user_key` (`app_user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='第三方授权表';

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
