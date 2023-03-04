/*
SQLyog Enterprise v12.09 (64 bit)
MySQL - 5.7.34 : Database - mall_trade
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`mall_trade` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;

USE `mall_trade`;

/*Table structure for table `order_statistics` */

DROP TABLE IF EXISTS `order_statistics`;

CREATE TABLE `order_statistics` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '订单统计id',
  `record_date` date NOT NULL COMMENT '记录日期',
  `count` int(10) unsigned NOT NULL COMMENT '数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=266 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单统计表';

/*Table structure for table `refund_evidence` */

DROP TABLE IF EXISTS `refund_evidence`;

CREATE TABLE `refund_evidence` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '凭证ID',
  `refund_id` bigint(20) NOT NULL COMMENT '退款单ID',
  `evidence_type` tinyint(1) NOT NULL COMMENT '凭证类型（1：申请凭证，2：物流凭证）',
  `file_type` tinyint(1) NOT NULL COMMENT '文件类型（1：图片）',
  `file_url` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件地址',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除状态（0：未删除，1：已删除）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_user` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='退款单退货凭证';

/*Table structure for table `refund_order` */

DROP TABLE IF EXISTS `refund_order`;

CREATE TABLE `refund_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '退款单ID',
  `refund_no` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '退款单号',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `order_product_id` bigint(20) NOT NULL COMMENT '订单详情ID',
  `product_amount` decimal(11,2) NOT NULL COMMENT '商品金额',
  `back_shipping_amount` decimal(11,2) NOT NULL COMMENT '退运费金额',
  `apply_amount` decimal(11,2) NOT NULL COMMENT '申请退款金额',
  `refund_amount` decimal(11,2) NOT NULL COMMENT '退款金额',
  `problem_describe` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '问题描述',
  `refund_reason` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '退款原因',
  `close_reason` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '关闭原因',
  `apply_time` datetime NOT NULL COMMENT '退款申请时间',
  `handle_time` datetime DEFAULT NULL COMMENT '商家处理时间',
  `handle_expire_time` datetime NOT NULL COMMENT '商家处理到期时间',
  `return_expire_time` datetime DEFAULT NULL COMMENT '用户退货到期时间',
  `receiving_expire_time` datetime DEFAULT NULL COMMENT '商家收货到期时间',
  `receive_status` tinyint(1) NOT NULL COMMENT '收货状态（1：未收到货，2：已收到货）',
  `refund_type` tinyint(1) NOT NULL COMMENT '退款单类型（1：仅退款，2：退货退款）',
  `refund_status` tinyint(1) NOT NULL COMMENT '退款单状态（1：已申请，2：已关闭，3：待退货，4：退货中，5：退款中，6：退款成功，7：退款失败）',
  `handle_status` tinyint(1) NOT NULL COMMENT '商家处理状态（1：待处理，2：同意退款，3：拒绝退款，4：同意退货，5：拒绝退货，6：确认收货，7：拒绝收货）',
  `is_back_shipping_amount` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否退运费（0：否，1：是）',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除状态（0：未删除，1：已删除）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_user` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`,`create_time`) USING BTREE,
  UNIQUE KEY `AK_refund_no_key` (`refund_no`)
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='退款订单表';

/*Table structure for table `refund_order_jeepay` */

DROP TABLE IF EXISTS `refund_order_jeepay`;

CREATE TABLE `refund_order_jeepay` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `refund_id` bigint(20) NOT NULL COMMENT '退款单ID',
  `mch_refund_no` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商户退款单号',
  `pay_order_id` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '支付订单号',
  `app_id` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '支付应用ID',
  `is_success` tinyint(1) NOT NULL DEFAULT '0' COMMENT '退款是否成功',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `AK_mch_refund_no_key` (`mch_refund_no`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='退款单jeepay对接';

/*Table structure for table `refund_order_log` */

DROP TABLE IF EXISTS `refund_order_log`;

CREATE TABLE `refund_order_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `refund_id` bigint(20) NOT NULL COMMENT '退款单ID',
  `operation_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '操作类型（1：用户提交申请，2：用户修改申请，3：用户撤销申请，4：超时自动处理申请，5：商家同意退款，6：商家拒绝退款，7：超时自动同意退货，8：商家同意退货，9：商家拒绝退货，10：超时取消退货，11：用户填写退货物流单，12：超时自动退款，13：商家同意收货，14：商家拒绝收货）',
  `remark` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注信息',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8061 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='退款单操作记录';

/*Table structure for table `refund_order_logistics` */

DROP TABLE IF EXISTS `refund_order_logistics`;

CREATE TABLE `refund_order_logistics` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '物流ID',
  `refund_id` bigint(20) NOT NULL COMMENT '退款单ID',
  `recipient_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '收件人姓名',
  `recipient_phone` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '收件人号码',
  `recipient_address` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '收货地址',
  `company_code` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '物流公司编号',
  `company_name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '物流公司名称',
  `tracking_no` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '物流单号',
  `logistics_api` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '物流API',
  `logistics_data` varchar(2048) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '物流详情数据',
  `is_subscribe` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否订阅（0：未订阅，1：已订阅）',
  `logistics_status` tinyint(1) NOT NULL COMMENT '物流状态（-1：无数据，0：在途，1：揽收，2：疑难，3：签收，4：退签，5：派件，8：清关，14：拒签）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `AK_order_id_key` (`refund_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='退货单物流信息';

/*Table structure for table `refund_order_pay_center` */

DROP TABLE IF EXISTS `refund_order_pay_center`;

CREATE TABLE `refund_order_pay_center` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `refund_id` bigint(20) NOT NULL COMMENT '退款单ID',
  `pay_order_id` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '支付订单ID',
  `pay_order_no` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '支付订单号',
  `refund_order_id` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '退款订单ID',
  `refund_order_no` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '退款订单号',
  `app_code` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '支付应用代号',
  `is_success` tinyint(1) NOT NULL DEFAULT '0' COMMENT '退款是否成功',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `AK_mch_refund_no_key` (`refund_order_no`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='退款单支付中台对接';

/*Table structure for table `sales_statistics` */

DROP TABLE IF EXISTS `sales_statistics`;

CREATE TABLE `sales_statistics` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `record_date` date NOT NULL COMMENT '记录日期',
  `sales` decimal(11,2) NOT NULL COMMENT '销售额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=265 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='销售额统计表';

/*Table structure for table `trade_order` */

DROP TABLE IF EXISTS `trade_order`;

CREATE TABLE `trade_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单编号',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `total_amount` decimal(11,2) NOT NULL COMMENT '总金额',
  `shipping_amount` decimal(11,2) NOT NULL COMMENT '运费',
  `discount_amount` decimal(11,2) NOT NULL COMMENT '优惠金额',
  `pay_amount` decimal(11,2) NOT NULL COMMENT '实付金额',
  `refund_amount` decimal(11,2) NOT NULL COMMENT '已退款金额',
  `submit_time` datetime NOT NULL COMMENT '下单时间',
  `expire_time` datetime NOT NULL COMMENT '失效时间',
  `auto_receive_time` datetime DEFAULT NULL COMMENT '自动收货时间',
  `receive_time` datetime DEFAULT NULL COMMENT '收货时间',
  `auto_praise` datetime DEFAULT NULL COMMENT '自动好评时间',
  `after_sale_deadline_time` datetime DEFAULT NULL COMMENT '售后截止时间',
  `user_message` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户留言',
  `cancel_reason` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '取消原因',
  `order_source` tinyint(1) NOT NULL COMMENT '订单来源（1：未知来源，2：安卓端APP，3：IOS端APP）',
  `order_type` tinyint(1) NOT NULL COMMENT '订单类型（1：普通订单，2：免费订单，3：秒杀订单）',
  `order_status` tinyint(1) NOT NULL COMMENT '订单状态（1：待支付，2：已关闭，3：已支付，4：已发货，5：已收货，6：已完成，7：已追评）',
  `pay_type` tinyint(1) NOT NULL COMMENT '支付方式（1：未支付，2：微信支付，3：支付宝支付）',
  `is_package_free` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否包邮（0：不包邮，1：包邮）',
  `is_after_sale` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否开启售后（0：未开启售后，1：已开启售后）',
  `is_disabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否禁用（0：启用，1：禁用）',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除（0：未删除，1：已删除）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `AK_order_key` (`order_no`),
  KEY `trade_order_create_time_index` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1479 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='交易订单';

/*Table structure for table `trade_order_0` */

DROP TABLE IF EXISTS `trade_order_0`;

CREATE TABLE `trade_order_0` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单编号',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `total_amount` decimal(11,2) NOT NULL COMMENT '总金额',
  `shipping_amount` decimal(11,2) NOT NULL COMMENT '运费',
  `discount_amount` decimal(11,2) NOT NULL COMMENT '优惠金额',
  `pay_amount` decimal(11,2) NOT NULL COMMENT '实付金额',
  `refund_amount` decimal(11,2) NOT NULL COMMENT '已退款金额',
  `submit_time` datetime NOT NULL COMMENT '下单时间',
  `expire_time` datetime NOT NULL COMMENT '失效时间',
  `auto_receive_time` datetime DEFAULT NULL COMMENT '自动收货时间',
  `receive_time` datetime DEFAULT NULL COMMENT '收货时间',
  `auto_praise` datetime DEFAULT NULL COMMENT '自动好评时间',
  `after_sale_deadline_time` datetime DEFAULT NULL COMMENT '售后截止时间',
  `user_message` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户留言',
  `cancel_reason` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '取消原因',
  `order_source` tinyint(1) NOT NULL COMMENT '订单来源（1：未知来源，2：安卓端APP，3：IOS端APP）',
  `order_type` tinyint(1) NOT NULL COMMENT '订单类型（1：普通订单，2：免费订单，3：秒杀订单）',
  `order_status` tinyint(1) NOT NULL COMMENT '订单状态（1：待支付，2：已关闭，3：已支付，4：已发货，5：已收货，6：已完成，7：已追评）',
  `pay_type` tinyint(1) NOT NULL COMMENT '支付方式（1：未支付，2：微信支付，3：支付宝支付）',
  `is_package_free` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否包邮（0：不包邮，1：包邮）',
  `is_after_sale` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否开启售后（0：未开启售后，1：已开启售后）',
  `is_disabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否禁用（0：启用，1：禁用）',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除（0：未删除，1：已删除）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `AK_order_key` (`order_no`),
  KEY `trade_order_create_time_index` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1477 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='交易订单';

/*Table structure for table `trade_order_1` */

DROP TABLE IF EXISTS `trade_order_1`;

CREATE TABLE `trade_order_1` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单编号',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `total_amount` decimal(11,2) NOT NULL COMMENT '总金额',
  `shipping_amount` decimal(11,2) NOT NULL COMMENT '运费',
  `discount_amount` decimal(11,2) NOT NULL COMMENT '优惠金额',
  `pay_amount` decimal(11,2) NOT NULL COMMENT '实付金额',
  `refund_amount` decimal(11,2) NOT NULL COMMENT '已退款金额',
  `submit_time` datetime NOT NULL COMMENT '下单时间',
  `expire_time` datetime NOT NULL COMMENT '失效时间',
  `auto_receive_time` datetime DEFAULT NULL COMMENT '自动收货时间',
  `receive_time` datetime DEFAULT NULL COMMENT '收货时间',
  `auto_praise` datetime DEFAULT NULL COMMENT '自动好评时间',
  `after_sale_deadline_time` datetime DEFAULT NULL COMMENT '售后截止时间',
  `user_message` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户留言',
  `cancel_reason` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '取消原因',
  `order_source` tinyint(1) NOT NULL COMMENT '订单来源（1：未知来源，2：安卓端APP，3：IOS端APP）',
  `order_type` tinyint(1) NOT NULL COMMENT '订单类型（1：普通订单，2：免费订单，3：秒杀订单）',
  `order_status` tinyint(1) NOT NULL COMMENT '订单状态（1：待支付，2：已关闭，3：已支付，4：已发货，5：已收货，6：已完成，7：已追评）',
  `pay_type` tinyint(1) NOT NULL COMMENT '支付方式（1：未支付，2：微信支付，3：支付宝支付）',
  `is_package_free` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否包邮（0：不包邮，1：包邮）',
  `is_after_sale` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否开启售后（0：未开启售后，1：已开启售后）',
  `is_disabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否禁用（0：启用，1：禁用）',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除（0：未删除，1：已删除）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `AK_order_key` (`order_no`),
  KEY `trade_order_create_time_index` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1478 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='交易订单';

/*Table structure for table `trade_order_2` */

DROP TABLE IF EXISTS `trade_order_2`;

CREATE TABLE `trade_order_2` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单编号',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `total_amount` decimal(11,2) NOT NULL COMMENT '总金额',
  `shipping_amount` decimal(11,2) NOT NULL COMMENT '运费',
  `discount_amount` decimal(11,2) NOT NULL COMMENT '优惠金额',
  `pay_amount` decimal(11,2) NOT NULL COMMENT '实付金额',
  `refund_amount` decimal(11,2) NOT NULL COMMENT '已退款金额',
  `submit_time` datetime NOT NULL COMMENT '下单时间',
  `expire_time` datetime NOT NULL COMMENT '失效时间',
  `auto_receive_time` datetime DEFAULT NULL COMMENT '自动收货时间',
  `receive_time` datetime DEFAULT NULL COMMENT '收货时间',
  `auto_praise` datetime DEFAULT NULL COMMENT '自动好评时间',
  `after_sale_deadline_time` datetime DEFAULT NULL COMMENT '售后截止时间',
  `user_message` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户留言',
  `cancel_reason` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '取消原因',
  `order_source` tinyint(1) NOT NULL COMMENT '订单来源（1：未知来源，2：安卓端APP，3：IOS端APP）',
  `order_type` tinyint(1) NOT NULL COMMENT '订单类型（1：普通订单，2：免费订单，3：秒杀订单）',
  `order_status` tinyint(1) NOT NULL COMMENT '订单状态（1：待支付，2：已关闭，3：已支付，4：已发货，5：已收货，6：已完成，7：已追评）',
  `pay_type` tinyint(1) NOT NULL COMMENT '支付方式（1：未支付，2：微信支付，3：支付宝支付）',
  `is_package_free` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否包邮（0：不包邮，1：包邮）',
  `is_after_sale` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否开启售后（0：未开启售后，1：已开启售后）',
  `is_disabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否禁用（0：启用，1：禁用）',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除（0：未删除，1：已删除）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `AK_order_key` (`order_no`),
  KEY `trade_order_create_time_index` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1479 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='交易订单';

/*Table structure for table `trade_order_jeepay` */

DROP TABLE IF EXISTS `trade_order_jeepay`;

CREATE TABLE `trade_order_jeepay` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `mch_order_no` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商户订单号',
  `pay_order_id` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '支付订单号',
  `app_id` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '支付应用ID',
  `way_code` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '支付方式',
  `pay_data_type` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '支付参数类型',
  `is_success` tinyint(1) NOT NULL DEFAULT '0' COMMENT '支付是否成功',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `AK_mch_order_no_key` (`mch_order_no`)
) ENGINE=InnoDB AUTO_INCREMENT=527 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='交易订单jeepay对接';

/*Table structure for table `trade_order_log` */

DROP TABLE IF EXISTS `trade_order_log`;

CREATE TABLE `trade_order_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `operation_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '操作类型（1：用户提交申请，2：用户修改申请，3：用户撤销申请，4：超时自动处理申请，5：商家同意退款，6：商家拒绝退款，7：超时自动同意退货，8：商家同意退货，9：商家拒绝退货，10：超时取消退货，11：用户填写退货物流单，12：超时自动退款，13：商家同意收货，14：商家拒绝收货，15：退款成功通知）',
  `remark` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注信息',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2419 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单操作记录';

/*Table structure for table `trade_order_logistics` */

DROP TABLE IF EXISTS `trade_order_logistics`;

CREATE TABLE `trade_order_logistics` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '物流ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `recipient_name` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收件人姓名',
  `recipient_phone` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收件人号码',
  `recipient_address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收货地址',
  `province` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '省',
  `province_code` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '省区域代码',
  `city` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '市',
  `city_code` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '市区域代码',
  `area` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '区/县',
  `area_code` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '区/县代码',
  `detail_address` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '详细地址',
  `company_code` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '物流公司编号',
  `company_name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '物流公司名称',
  `tracking_no` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '物流单号',
  `logistics_api` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '物流API',
  `logistics_data` varchar(2048) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '物流详情数据',
  `is_subscribe` tinyint(1) DEFAULT '0' COMMENT '是否订阅（0：未订阅，1：已订阅）',
  `logistics_status` tinyint(1) DEFAULT NULL COMMENT '物流状态（-1：无数据，0：在途，1：揽收，2：疑难，3：签收，4：退签，5：派件，8：清关，14：拒签）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `AK_order_id_key` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=921 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单物流信息';

/*Table structure for table `trade_order_pay_center` */

DROP TABLE IF EXISTS `trade_order_pay_center`;

CREATE TABLE `trade_order_pay_center` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `pay_order_id` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '支付订单ID',
  `pay_order_no` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '支付订单号',
  `app_code` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '支付应用代号',
  `pay_code` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '支付方式',
  `is_success` tinyint(1) NOT NULL DEFAULT '0' COMMENT '支付是否成功',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `AK_mch_order_no_key` (`pay_order_no`)
) ENGINE=InnoDB AUTO_INCREMENT=966 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='交易订单支付中台';

/*Table structure for table `trade_order_product` */

DROP TABLE IF EXISTS `trade_order_product`;

CREATE TABLE `trade_order_product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '详情ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `product_id` bigint(20) NOT NULL COMMENT '商品ID',
  `product_sku_id` bigint(20) NOT NULL COMMENT '商品SKU-ID',
  `product_name` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品名称',
  `product_image_url` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品图片',
  `sku_describe` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'SKU规格',
  `quantity` int(11) NOT NULL COMMENT '购买数量',
  `product_price` decimal(11,2) NOT NULL COMMENT '商品单价',
  `real_price` decimal(11,2) NOT NULL COMMENT '实际价格',
  `real_amount` decimal(11,2) NOT NULL COMMENT '实际金额',
  `activity_id` bigint(20) DEFAULT NULL COMMENT '活动ID',
  `detail_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '明细状态（1：正常状态，2：申请售后，3：退款成功，4：退款失败）',
  `activity_type` tinyint(1) NOT NULL DEFAULT '1' COMMENT '活动类型（1：正常购买，2：秒杀）',
  `comment_status` tinyint(1) DEFAULT NULL COMMENT '评价状态（1：待评价，2：已评价，3：已追评）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1165 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单商品详情';

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
