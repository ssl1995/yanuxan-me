/*
SQLyog Enterprise v12.09 (64 bit)
MySQL - 5.7.34 : Database - mall_im
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`mall_im` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `mall_im`;

/*Table structure for table `waiter_user` */

DROP TABLE IF EXISTS `waiter_user`;

CREATE TABLE `waiter_user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `waiter_id` varchar(64) DEFAULT NULL,
  `waiter_avatar` varchar(255) DEFAULT NULL,
  `waiter_nickname` varchar(255) DEFAULT NULL,
  `user_id` int(10) unsigned DEFAULT NULL COMMENT '员工id',
  `create_user` int(10) unsigned DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_user` int(10) unsigned DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id_idx` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
