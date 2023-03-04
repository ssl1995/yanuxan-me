/*
SQLyog Enterprise v12.09 (64 bit)
MySQL - 5.7.34 : Database - xxl_job
*********************************************************************
*/
CREATE
database if NOT EXISTS `xxl_job` default character set utf8mb4 collate utf8mb4_unicode_ci;
use
`xxl_job`;

SET NAMES utf8mb4;

CREATE TABLE IF NOT EXISTS `xxl_job_info`
(
    `id` int
(
    11
) NOT NULL AUTO_INCREMENT,
    `job_group` int
(
    11
) NOT NULL COMMENT '执行器主键ID',
    `job_desc` varchar
(
    255
) NOT NULL,
    `add_time` datetime DEFAULT NULL,
    `update_time` datetime DEFAULT NULL,
    `author` varchar
(
    64
) DEFAULT NULL COMMENT '作者',
    `alarm_email` varchar
(
    255
) DEFAULT NULL COMMENT '报警邮件',
    `schedule_type` varchar
(
    50
) NOT NULL DEFAULT 'NONE' COMMENT '调度类型',
    `schedule_conf` varchar
(
    128
) DEFAULT NULL COMMENT '调度配置，值含义取决于调度类型',
    `misfire_strategy` varchar
(
    50
) NOT NULL DEFAULT 'DO_NOTHING' COMMENT '调度过期策略',
    `executor_route_strategy` varchar
(
    50
) DEFAULT NULL COMMENT '执行器路由策略',
    `executor_handler` varchar
(
    255
) DEFAULT NULL COMMENT '执行器任务handler',
    `executor_param` varchar
(
    512
) DEFAULT NULL COMMENT '执行器任务参数',
    `executor_block_strategy` varchar
(
    50
) DEFAULT NULL COMMENT '阻塞处理策略',
    `executor_timeout` int
(
    11
) NOT NULL DEFAULT '0' COMMENT '任务执行超时时间，单位秒',
    `executor_fail_retry_count` int
(
    11
) NOT NULL DEFAULT '0' COMMENT '失败重试次数',
    `glue_type` varchar
(
    50
) NOT NULL COMMENT 'GLUE类型',
    `glue_source` mediumtext COMMENT 'GLUE源代码',
    `glue_remark` varchar
(
    128
) DEFAULT NULL COMMENT 'GLUE备注',
    `glue_updatetime` datetime DEFAULT NULL COMMENT 'GLUE更新时间',
    `child_jobid` varchar
(
    255
) DEFAULT NULL COMMENT '子任务ID，多个逗号分隔',
    `trigger_status` tinyint
(
    4
) NOT NULL DEFAULT '0' COMMENT '调度状态：0-停止，1-运行',
    `trigger_last_time` bigint
(
    13
) NOT NULL DEFAULT '0' COMMENT '上次调度时间',
    `trigger_next_time` bigint
(
    13
) NOT NULL DEFAULT '0' COMMENT '下次调度时间',
    PRIMARY KEY
(
    `id`
)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `xxl_job_log`
(
    `id` bigint
(
    20
) NOT NULL AUTO_INCREMENT,
    `job_group` int
(
    11
) NOT NULL COMMENT '执行器主键ID',
    `job_id` int
(
    11
) NOT NULL COMMENT '任务，主键ID',
    `executor_address` varchar
(
    255
) DEFAULT NULL COMMENT '执行器地址，本次执行的地址',
    `executor_handler` varchar
(
    255
) DEFAULT NULL COMMENT '执行器任务handler',
    `executor_param` varchar
(
    512
) DEFAULT NULL COMMENT '执行器任务参数',
    `executor_sharding_param` varchar
(
    20
) DEFAULT NULL COMMENT '执行器任务分片参数，格式如 1/2',
    `executor_fail_retry_count` int
(
    11
) NOT NULL DEFAULT '0' COMMENT '失败重试次数',
    `trigger_time` datetime DEFAULT NULL COMMENT '调度-时间',
    `trigger_code` int
(
    11
) NOT NULL COMMENT '调度-结果',
    `trigger_msg` text COMMENT '调度-日志',
    `handle_time` datetime DEFAULT NULL COMMENT '执行-时间',
    `handle_code` int
(
    11
) NOT NULL COMMENT '执行-状态',
    `handle_msg` text COMMENT '执行-日志',
    `alarm_status` tinyint
(
    4
) NOT NULL DEFAULT '0' COMMENT '告警状态：0-默认、1-无需告警、2-告警成功、3-告警失败',
    PRIMARY KEY
(
    `id`
),
    KEY `I_trigger_time`
(
    `trigger_time`
),
    KEY `I_handle_code`
(
    `handle_code`
)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `xxl_job_log_report`
(
    `id` int
(
    11
) NOT NULL AUTO_INCREMENT,
    `trigger_day` datetime DEFAULT NULL COMMENT '调度-时间',
    `running_count` int
(
    11
) NOT NULL DEFAULT '0' COMMENT '运行中-日志数量',
    `suc_count` int
(
    11
) NOT NULL DEFAULT '0' COMMENT '执行成功-日志数量',
    `fail_count` int
(
    11
) NOT NULL DEFAULT '0' COMMENT '执行失败-日志数量',
    `update_time` datetime DEFAULT NULL,
    PRIMARY KEY
(
    `id`
),
    UNIQUE KEY `i_trigger_day`
(
    `trigger_day`
) USING BTREE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `xxl_job_logglue`
(
    `id` int
(
    11
) NOT NULL AUTO_INCREMENT,
    `job_id` int
(
    11
) NOT NULL COMMENT '任务，主键ID',
    `glue_type` varchar
(
    50
) DEFAULT NULL COMMENT 'GLUE类型',
    `glue_source` mediumtext COMMENT 'GLUE源代码',
    `glue_remark` varchar
(
    128
) NOT NULL COMMENT 'GLUE备注',
    `add_time` datetime DEFAULT NULL,
    `update_time` datetime DEFAULT NULL,
    PRIMARY KEY
(
    `id`
)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `xxl_job_registry`
(
    `id` int
(
    11
) NOT NULL AUTO_INCREMENT,
    `registry_group` varchar
(
    50
) NOT NULL,
    `registry_key` varchar
(
    255
) NOT NULL,
    `registry_value` varchar
(
    255
) NOT NULL,
    `update_time` datetime DEFAULT NULL,
    PRIMARY KEY
(
    `id`
),
    KEY `i_g_k_v`
(
    `registry_group`,
    `registry_key`,
    `registry_value`
)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `xxl_job_group`
(
    `id` int
(
    11
) NOT NULL AUTO_INCREMENT,
    `app_name` varchar
(
    64
) NOT NULL COMMENT '执行器AppName',
    `title` varchar
(
    12
) NOT NULL COMMENT '执行器名称',
    `address_type` tinyint
(
    4
) NOT NULL DEFAULT '0' COMMENT '执行器地址类型：0=自动注册、1=手动录入',
    `address_list` text COMMENT '执行器地址列表，多地址逗号分隔',
    `update_time` datetime DEFAULT NULL,
    PRIMARY KEY
(
    `id`
)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `xxl_job_user`
(
    `id` int
(
    11
) NOT NULL AUTO_INCREMENT,
    `username` varchar
(
    50
) NOT NULL COMMENT '账号',
    `password` varchar
(
    50
) NOT NULL COMMENT '密码',
    `role` tinyint
(
    4
) NOT NULL COMMENT '角色：0-普通用户、1-管理员',
    `permission` varchar
(
    255
) DEFAULT NULL COMMENT '权限：执行器ID列表，多个逗号分割',
    PRIMARY KEY
(
    `id`
),
    UNIQUE KEY `i_username`
(
    `username`
) USING BTREE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `xxl_job_lock`
(
    `lock_name` varchar
(
    50
) NOT NULL COMMENT '锁名称',
    PRIMARY KEY
(
    `lock_name`
)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


INSERT INTO `xxl_job_user`(`id`, `username`, `password`, `role`, `permission`)
VALUES (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', 1, NULL);
INSERT INTO `xxl_job_lock` (`lock_name`)
VALUES ('schedule_lock');

INSERT INTO `xxl_job_group` (`id`, `app_name`, `title`, `address_type`, `address_list`, `update_time`)
VALUES (1, 'mall-trade-service', '严选订单', 0, '', '2022-07-15 17:36:54');
INSERT INTO `xxl_job_group` (`id`, `app_name`, `title`, `address_type`, `address_list`, `update_time`)
VALUES (2, 'horse-user-service', '用户中心', 0, '', '2022-07-15 17:36:54');

INSERT INTO `xxl_job_info` (`id`, `job_group`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`,
                            `schedule_type`, `schedule_conf`, `misfire_strategy`, `executor_route_strategy`,
                            `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`,
                            `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`,
                            `child_jobid`, `trigger_status`, `trigger_last_time`, `trigger_next_time`)
VALUES (1, 1, '订单超时自动关闭任务', '2022-05-05 16:55:22', '2022-05-05 16:55:26', 'admin', '', 'CRON', '*/30 * * * * ?',
        'DO_NOTHING', 'LAST', 'orderPayExpireTask', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化',
        '2022-05-05 16:55:22', '', 1, 1657877910000, 1657877940000);
INSERT INTO `xxl_job_info` (`id`, `job_group`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`,
                            `schedule_type`, `schedule_conf`, `misfire_strategy`, `executor_route_strategy`,
                            `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`,
                            `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`,
                            `child_jobid`, `trigger_status`, `trigger_last_time`, `trigger_next_time`)
VALUES (2, 1, '订单收货到期自动收货任务', '2022-05-05 16:56:02', '2022-05-05 16:56:05', 'admin', '', 'CRON', '*/30 * * * * ?',
        'DO_NOTHING', 'LAST', 'orderReceiveExpireTask', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化',
        '2022-05-05 16:56:02', '', 1, 1657877910000, 1657877940000);
INSERT INTO `xxl_job_info` (`id`, `job_group`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`,
                            `schedule_type`, `schedule_conf`, `misfire_strategy`, `executor_route_strategy`,
                            `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`,
                            `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`,
                            `child_jobid`, `trigger_status`, `trigger_last_time`, `trigger_next_time`)
VALUES (3, 1, '定时更新订单物流任务', '2022-05-05 16:56:21', '2022-05-05 16:56:31', 'admin', '', 'CRON', '0 0 4 * * ?',
        'DO_NOTHING', 'LAST', 'orderRefreshLogisticsTask', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化',
        '2022-05-05 16:56:21', '', 1, 1657828800000, 1657915200000);
INSERT INTO `xxl_job_info` (`id`, `job_group`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`,
                            `schedule_type`, `schedule_conf`, `misfire_strategy`, `executor_route_strategy`,
                            `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`,
                            `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`,
                            `child_jobid`, `trigger_status`, `trigger_last_time`, `trigger_next_time`)
VALUES (4, 1, '售后申请超时自动处理任务', '2022-05-05 16:56:49', '2022-05-05 16:56:52', 'admin', '', 'CRON', '*/30 * * * * ?',
        'DO_NOTHING', 'LAST', 'refundHandleExpireTask', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化',
        '2022-05-05 16:56:49', '', 1, 1657877910000, 1657877940000);
INSERT INTO `xxl_job_info` (`id`, `job_group`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`,
                            `schedule_type`, `schedule_conf`, `misfire_strategy`, `executor_route_strategy`,
                            `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`,
                            `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`,
                            `child_jobid`, `trigger_status`, `trigger_last_time`, `trigger_next_time`)
VALUES (5, 1, '用户退货超时定时任务', '2022-05-05 16:57:10', '2022-05-05 16:57:13', 'admin', '', 'CRON', '*/30 * * * * ?',
        'DO_NOTHING', 'LAST', 'refundReturnExpireTask', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化',
        '2022-05-05 16:57:10', '', 1, 1657877910000, 1657877940000);
INSERT INTO `xxl_job_info` (`id`, `job_group`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`,
                            `schedule_type`, `schedule_conf`, `misfire_strategy`, `executor_route_strategy`,
                            `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`,
                            `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`,
                            `child_jobid`, `trigger_status`, `trigger_last_time`, `trigger_next_time`)
VALUES (6, 1, '商家收货超时定时任务', '2022-05-05 16:57:30', '2022-05-05 16:57:33', 'admin', '', 'CRON', '*/30 * * * * ?',
        'DO_NOTHING', 'LAST', 'refundReceivingExpireTask', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化',
        '2022-05-05 16:57:30', '', 1, 1657877910000, 1657877940000);
INSERT INTO `xxl_job_info` (`id`, `job_group`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`,
                            `schedule_type`, `schedule_conf`, `misfire_strategy`, `executor_route_strategy`,
                            `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`,
                            `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`,
                            `child_jobid`, `trigger_status`, `trigger_last_time`, `trigger_next_time`)
VALUES (7, 1, '定时更新退款单物流任务', '2022-05-05 16:57:49', '2022-05-05 16:57:52', 'admin', '', 'CRON', '0 0 4 * * ?',
        'DO_NOTHING', 'LAST', 'refundRefreshLogisticsTask', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化',
        '2022-05-05 16:57:49', '', 1, 1657828800000, 1657915200000);
INSERT INTO `xxl_job_info` (`id`, `job_group`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`,
                            `schedule_type`, `schedule_conf`, `misfire_strategy`, `executor_route_strategy`,
                            `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`,
                            `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`,
                            `child_jobid`, `trigger_status`, `trigger_last_time`, `trigger_next_time`)
VALUES (8, 2, '统计uv访客记录', '2022-06-10 17:07:58', '2022-06-10 17:09:05', 'admin', '', 'CRON', '0 0 1 * * ?',
        'DO_NOTHING', 'FIRST', 'statisticsVisitorUvCount', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化',
        '2022-06-10 17:07:58', '', 1, 1657818000000, 1657904400000);
INSERT INTO `xxl_job_info` (`id`, `job_group`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`,
                            `schedule_type`, `schedule_conf`, `misfire_strategy`, `executor_route_strategy`,
                            `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`,
                            `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`,
                            `child_jobid`, `trigger_status`, `trigger_last_time`, `trigger_next_time`)
VALUES (9, 2, '统计pv访客记录', '2022-06-10 17:08:53', '2022-06-10 17:09:01', 'admin', '', 'CRON', '0 0 1 * * ?',
        'DO_NOTHING', 'FIRST', 'statisticsVisitorPvCount', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化',
        '2022-06-10 17:08:53', '', 1, 1657818000000, 1657904400000);
INSERT INTO `xxl_job_info` (`id`, `job_group`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`,
                            `schedule_type`, `schedule_conf`, `misfire_strategy`, `executor_route_strategy`,
                            `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`,
                            `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`,
                            `child_jobid`, `trigger_status`, `trigger_last_time`, `trigger_next_time`)
VALUES (10, 1, '统计订单数量', '2022-06-10 17:55:56', '2022-06-10 17:56:39', 'admin', '', 'CRON', '0 0 1 * * ?', 'DO_NOTHING',
        'FIRST', 'statisticsOrderCount', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2022-06-10 17:55:56',
        '', 1, 1657818000000, 1657904400000);
INSERT INTO `xxl_job_info` (`id`, `job_group`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`,
                            `schedule_type`, `schedule_conf`, `misfire_strategy`, `executor_route_strategy`,
                            `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`,
                            `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`,
                            `child_jobid`, `trigger_status`, `trigger_last_time`, `trigger_next_time`)
VALUES (11, 1, '统计销售额', '2022-06-10 17:56:25', '2022-06-10 17:56:35', 'admin', '', 'CRON', '0 0 1 * * ?', 'DO_NOTHING',
        'FIRST', 'statisticsSales', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2022-06-10 17:56:25', '', 1,
        1657818000000, 1657904400000);

commit;


