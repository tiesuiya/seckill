-- 数据库初始化脚本

-- 删除数据库
DROP DATABASE IF EXISTS seckill;

-- 创建数据库
CREATE DATABASE seckill;
-- 使用数据库
use seckill;
-- 创建秒杀库存表
CREATE TABLE seckill(
    `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
    `product_id` bigint unsigned NOT NULL COMMENT '商品id',
    `product_name` varchar(120) NOT NULL COMMENT '商品名称',
    `quantity` int unsigned NOT NULL COMMENT '库存数量',
    `start_time` datetime NOT NULL COMMENT '秒杀开启时间',
    `end_time` datetime NOT NULL COMMENT '秒杀结束时间',
    `gmt_create` datetime NOT NULL COMMENT '创建时间',
    `gmt_modified` datetime NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY idx_start_time(`start_time`),
    KEY idx_end_time(`end_time`),
    KEY idx_gmt_create(`gmt_create`),
    KEY idx_gmt_modified(`gmt_modified`)
) ENGINE=InnoDB AUTO_INCREMENT = 1000 DEFAULT CHARSET = utf8 COMMENT='秒杀库存表';

-- 初始化数据
INSERT INTO seckill(product_id, product_name, quantity, start_time, end_time, gmt_create, gmt_modified) values(10001, '1000元秒杀三星S7 edge',100,'2018-03-16 11:30:00','2018-03-16 12:00:00','2018-03-16 11:30:00','2018-03-16 11:30:00');
INSERT INTO seckill(product_id, product_name, quantity, start_time, end_time, gmt_create, gmt_modified) values(10002, '1500元秒杀ipad2 mini',150,'2018-03-16 11:30:00','2018-03-16 12:00:00','2018-03-16 11:30:00','2018-03-16 11:30:00');
INSERT INTO seckill(product_id, product_name, quantity, start_time, end_time, gmt_create, gmt_modified) values(10003, '2500元秒杀小米6',200,'2018-03-16 11:30:00','2018-03-16 12:00:00','2018-03-16 11:30:00','2018-03-16 11:30:00');

-- 秒杀成功明细表
CREATE TABLE success_killed(
    `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
    `seckill_id` bigint unsigned NOT NULL COMMENT '秒杀库存id',
    `user_phone` bigint unsigned NOT NULL COMMENT '用户手机号',
    `state` tinyint NOT NULL DEFAULT -1 COMMENT '状态：-1无效 0成功 1已付款 2已发货',
    `gmt_create` datetime NOT NULL COMMENT '创建时间',
    `gmt_modified` datetime NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY uk_seckill_id_user_phone(`seckill_id`, `user_phone`),/*联合唯一约束*/
    KEY idx_gmt_create(`gmt_create`)
) ENGINE=InnoDB AUTO_INCREMENT = 1000 DEFAULT CHARSET = utf8 COMMENT='秒杀成功明细表';

