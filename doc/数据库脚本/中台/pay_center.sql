/*
SQLyog Enterprise v12.09 (64 bit)
MySQL - 5.7.34 : Database - pay_center
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE = ''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;
CREATE
    DATABASE /*!32312 IF NOT EXISTS */`pay_center` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;

USE
    `pay_center`;

drop table if exists mch_info;

/*==============================================================*/
/* Table: mch_info                                              */
/*==============================================================*/
create table mch_info
(
    id          bigint      not null auto_increment comment '主键ID',
    mch_type    tinyint comment '商户类型（1：普通商户，3：服务商）',
    mch_code    varchar(64) comment '商户代号（wxpay：微信，alipay：支付宝）',
    mch_name    varchar(64) not null comment '商户名称',
    mch_id      varchar(64) not null comment '商户ID',
    mch_data    text comment '商户资料',
    is_disabled tinyint     not null default 0 comment '是否禁用（0：启用，1：禁用）',
    create_time datetime             default CURRENT_TIMESTAMP comment '创建时间',
    create_user bigint comment '创建人',
    update_time datetime             default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
    update_user bigint comment '更新人',
    primary key (id)
);

alter table mch_info
    comment '商户信息表';

drop table if exists app_info;

/*==============================================================*/
/* Table: app_info                                              */
/*==============================================================*/
create table app_info
(
    id             bigint       not null auto_increment comment '主键ID',
    mch_primary_id bigint       not null comment '商户主键ID',
    app_code       varchar(64)  not null comment '应用代码',
    pay_codes      varchar(255) not null comment '支持的支付方式',
    sign_key       varchar(64)  not null comment '签名秘钥',
    app_name       varchar(64)  not null comment '应用名称',
    app_id         varchar(64)  not null comment '应用ID',
    app_data       text comment '应用资料',
    is_disabled    tinyint      not null default 0 comment '是否禁用（0：启用，1：禁用）',
    create_time    datetime              default CURRENT_TIMESTAMP comment '创建时间',
    create_user    bigint comment '创建人',
    update_time    datetime              default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
    update_user    bigint comment '更新人',
    primary key (id),
    unique key AK_app_code_key (app_code)
);

alter table app_info
    comment '应用信息表';


drop table if exists pay_order;

/*==============================================================*/
/* Table: pay_order                                             */
/*==============================================================*/
create table pay_order
(
    id                   bigint         not null auto_increment comment '支付订单ID',
    mch_primary_id       bigint comment '商户主键ID',
    app_primary_id       bigint comment '应用主键ID',
    sys_tag              varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '业务系统tag',
    pay_code             varchar(64) comment '支付方式',
    pay_order_no         varchar(64)    not null comment '支付订单号',
    subject              varchar(64)    not null comment '商品标题',
    body                 varchar(255)   not null comment '商品描述',
    amount               decimal(11, 2) not null comment '金额',
    refund_amount        decimal(11, 2) default 0 comment '已退款金额',
    refund_times         int            default 0 comment '退款次数',
    pay_status           tinyint        not null comment '支付状态（1：支付中，2：已关闭，3：支付成功，4：支付失败，5：部分退款，6：全额退款）',
    notify_status        tinyint        not null comment '通知状态（1：未通知，2：已通知，3：已响应）',
    notify_url           varchar(255) comment '通知地址',
    return_url           varchar(255) comment '回调页面',
    channel_pay_order_no varchar(64) comment '支付渠道订单号',
    channel_user_id      varchar(64) comment '支付渠道用户ID',
    channel_request      varchar(2048) comment '支付渠道发起参数',
    channel_response     varchar(2048) comment '支付渠道响应参数',
    channel_notify       varchar(2048) comment '支付渠道回调参数',
    expired_time         datetime comment '失效时间',
    success_time         datetime comment '支付时间',
    create_time          datetime       default CURRENT_TIMESTAMP comment '创建时间',
    create_user          bigint comment '创建人',
    update_time          datetime       default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
    update_user          bigint comment '更新人',
    primary key (id),
    unique key AK_pay_order_no_key (pay_order_no)
);

alter table pay_order
    comment '支付订单表';


drop table if exists refund_order;

/*==============================================================*/
/* Table: refund_order                                          */
/*==============================================================*/
create table refund_order
(
    id                      bigint         not null auto_increment comment '退款订单ID',
    mch_primary_id          bigint         not null comment '商户主键ID',
    app_primary_id          bigint         not null comment '应用主键ID',
    pay_order_id            bigint         not null comment '支付订单ID',
    refund_order_no         varchar(64)    not null comment '退款订单号',
    refund_amount           decimal(11, 2) not null comment '退款金额',
    refund_reason           varchar(255)   not null comment '退款原因',
    refund_status           tinyint        not null comment '退款状态（1：退款中，2：退款关闭，3：退款成功，4：退款失败）',
    notify_status           tinyint        not null comment '通知状态（1：未通知，2：已通知，3：已响应）',
    notify_url              varchar(255) comment '通知地址',
    channel_refund_order_no varchar(64) comment '渠道退款单号',
    channel_request         varchar(2048) comment '退款渠道发起参数',
    channel_response        varchar(2048) comment '退款渠道响应参数',
    channel_notify          varchar(2048) comment '支付渠道回调参数',
    expired_time            datetime comment '失效时间',
    success_time            datetime comment '退款时间',
    create_time             datetime default CURRENT_TIMESTAMP comment '创建时间',
    update_time             datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
    create_user             bigint comment '创建人',
    update_user             bigint comment '更新人',
    primary key (id),
    unique key AK_refund_order_no_key (refund_order_no)
);

alter table refund_order
    comment '退款订单表';


