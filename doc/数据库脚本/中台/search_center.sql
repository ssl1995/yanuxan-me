/*
SQLyog Enterprise v12.09 (64 bit)
MySQL - 5.7.34 : Database - search_center
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`search_center` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `search_center`;

/*Table structure for table `search_config` */

DROP TABLE IF EXISTS `search_config`;

CREATE TABLE `search_config` (
  `id` int(10) unsigned DEFAULT NULL,
  `system_id` int(10) unsigned DEFAULT NULL,
  `search_code` varchar(20) DEFAULT NULL,
  `search_name` varchar(50) DEFAULT NULL,
  `search_filed` varchar(200) DEFAULT NULL,
  `database_name` varchar(15) DEFAULT NULL,
  `document_id_exp` varchar(50) DEFAULT NULL,
  `sync_cron` varchar(25) DEFAULT NULL,
  `author` varchar(5) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Table structure for table `system_config` */

DROP TABLE IF EXISTS `system_config`;

CREATE TABLE `system_config` (
  `id` int(10) unsigned DEFAULT NULL,
  `system_code` varchar(20) DEFAULT NULL,
  `system_name` varchar(20) DEFAULT NULL,
  `database_type` int(10) unsigned DEFAULT NULL,
  `datasource` varchar(255) DEFAULT NULL,
  `is_deleted` tinyint(4) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
