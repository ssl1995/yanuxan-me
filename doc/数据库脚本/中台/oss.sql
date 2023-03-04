/*
SQLyog Enterprise v12.09 (64 bit)
MySQL - 5.7.34 : Database - oss
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`oss` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `oss`;

/*Table structure for table `oss_file_record` */

DROP TABLE IF EXISTS `oss_file_record`;

CREATE TABLE `oss_file_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_type` varchar(64) NOT NULL COMMENT '文件类型',
  `service` varchar(64) DEFAULT NULL COMMENT '所属服务',
  `directory` varchar(255) DEFAULT NULL COMMENT '存放目录',
  `file_md5` varchar(128) NOT NULL COMMENT '文件内容(MD5加密)',
  `file_url` varchar(225) NOT NULL COMMENT '文件地址',
  `file_name` varchar(255) DEFAULT NULL COMMENT '文件名称',
  `used` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否有用到',
  `file_size` int(11) DEFAULT NULL COMMENT '文件大小',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_user` bigint(20) NOT NULL,
  `update_user` bigint(20) DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
