# 马士兵严选

## 技术选型

核心：
* SpringCloud
* SpringCloud Alibaba  Nacos  服务注册中心 / 配置中心
* Spring Cloud GateWay 网关
* Rocket MQ 消息队列
* Seata 分布式事务
* Spring Cloud Open Feign 服务调用（负载均衡）
* SpringCloud Alibaba Sentinel 流量防护（轻量级、高性能，实时监控和控制面板）
* SkyWalking 链路追踪
* Elastic Search 近实时搜索引擎（业务搜索，日志搜索）
  * Canal 数据同步（MySql->es）
* XXL-Job 分布式任务调度
* Redis 缓存
  * Redisson 基于redis的分布式锁
* Mysql 数据库

自研中台：

- 技术中台：
  * im通讯中台
  * 分布式id生成器
  * oss文件中台
  * 消息推送中台（待完善）
  * 搜索中台（待完善）

- 业务中台
  - 用户中台
  - 权限中台
  - 点赞中台
  - 评论中台
  - 敏感词中台
  - 第三方服务中台
  - 支付中台

监控告警维护：

* 钉钉机器人 告警
* Jenkins 持续集成
* Docker K8s + kubesphere容器管理
* prometheus + grafana 服务/中间件 监控平台
* ELK（ES Logstash Kibana）日志搜集和日志查看平台

## 项目说明

Horse——飞马

分为horse脚手架和horse-mall商城包，其实是两个项目，为了方便大家浏览源码，放到一起

horse作为脚手架框架设计开发，包含分布式组件，服务治理，权限认证等基础功能，可以拿来快速做二次开发

horse-mall则是我们官方使用horse脚手架开发的一个线上运行的真实项目

目录结构：

```
horse
 |-- horse-bussiness 业务模块
     |-- horse-mall-xx
     ...
 |-- horse--framework 框架
     |-- horse-framework-common      公共模块 
     |-- horse-framework-dependency  依赖管理
     |-- horse-framework-starters    组件starters（包含各种中间件的封装）
         |-- horse-framework-starter-job    分布式任务（xxl-job）
         |-- horse-framework-starter-rocketmq     消息队列（rocketMQ组件增强）
         |-- horse-framework-starter-mysql  mysql、mybatis-plus封装
         |-- horse-framework-starter-nacos  nacos（注册中心、配置中心）
         |-- horse-framework-starter-redis  redis（分布式缓存，分布式锁，spring-cache增强，redis工具，序列化配置）
         |-- horse-framework-starter-seata  seata（分布式事务）
         |-- horse-framework-starter-web    web服务（统一异常处理、统一返回格式、统一日期格式、接口文档增强、安全功能、对象转换、字段翻译、本地缓存）
 |-- horse--support 支撑模块（包含技术中台和业务中台）
     |-- horse-gateway 网关
     |-- horse-id 分布式id
     |-- horse-im 即时通讯中台
     |-- horse-like 点赞中台
     |-- horse-oss 文件中台
     |-- horse-pay 支付中台
     |-- horse-push 消息推送中台
     |-- horse-search 搜索中台
     |-- horse-sensitive 敏感词中台
     |-- horse-third 第三方服务对接中台
     |-- horse-user 用户中台（暂时囊括了权限功能，近期会迁移到权限中台）
     |-- horse-authority 权限中台（todo）


```

## 相关资料

- 商城网站：[you.mashibing.com](https://you.mashibing.com)
- 项目白皮书：[购买链接](https://you.mashibing.com/goods/detail/81)
- 严选项目直播分享课（每周五或周六）：[直达链接](https://www.mashibing.com/live/1530)
- 项目上手及搭建指南：[直达链接](https://www.mashibing.com/subject/1?courseNo=1817)




