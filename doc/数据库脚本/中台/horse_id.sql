/*
SQLyog Enterprise v12.09 (64 bit)
MySQL - 5.7.34 : Database - horse_id
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`horse_id` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin */;

USE `horse_id`;

/*Table structure for table `cosid_segment` */

DROP TABLE IF EXISTS `cosid_segment`;

CREATE TABLE `cosid_segment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `business_id` varchar(64) NOT NULL COMMENT '业务标识',
  `auto_increment` bigint(20) NOT NULL COMMENT 'id自增',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_user` bigint(20) DEFAULT NULL COMMENT '创建用户',
  `update_user` bigint(20) DEFAULT NULL COMMENT '修改用户',
  `is_deleted` tinyint(1) NOT NULL COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1534837403477942274 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='分布式id 生成表';

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
