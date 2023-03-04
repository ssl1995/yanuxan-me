/*
SQLyog Enterprise v12.09 (64 bit)
MySQL - 5.7.34 : Database - horse_sensitive
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`horse_sensitive` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;

USE `horse_sensitive`;

/*Table structure for table `sensitive_words` */

DROP TABLE IF EXISTS `sensitive_words`;

CREATE TABLE `sensitive_words` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '敏感词ID',
  `content` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '内容',
  `words_type` tinyint(1) NOT NULL DEFAULT '4' COMMENT '类型（1-反动词库,2-广告,3-政治类,4-敏感词,5-暴恐词,6-民生词库,7-涉枪涉爆违法信息关键词,8-色情词,9-other,10-广告高危词）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `update_user` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '修改人',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '删除状态（0未删除，1已删除）',
  `enabled` tinyint(1) DEFAULT '1' COMMENT '使用状态（0禁用，1启用）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=19490 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='敏感词库';

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
