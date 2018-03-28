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
INSERT INTO seckill(product_id, product_name, quantity, start_time, end_time, gmt_create, gmt_modified) values(10001, '1000元秒杀三星S7 edge',100,'2017-03-16 11:30:00','2017-03-17 23:00:00','2017-01-10 11:30:00','2017-01-10 11:30:00');
INSERT INTO seckill(product_id, product_name, quantity, start_time, end_time, gmt_create, gmt_modified) values(10002, '1500元秒杀ipad2 mini',150,'2018-03-16 11:30:00','2018-04-16 12:00:00','2017-01-10 11:30:00','2017-01-10 11:30:00');
INSERT INTO seckill(product_id, product_name, quantity, start_time, end_time, gmt_create, gmt_modified) values(10003, '2500元秒杀小米6',200,'2019-03-16 11:30:00','2019-03-17 12:00:00','2017-01-10 11:30:00','2017-01-10 11:30:00');

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

-- -------------------------------------------------存储过程-------------------------------------------------
-- 秒杀操作（插入记录+更新库存）存储过程

-- 关于存储过程
-- 1：存储过程优化：事务行级锁持有时间
-- 2：不要过度依赖存储过程，这里只是学习怎么写，不真正使用
-- 3：QPS可以达到一个秒杀单6000+

-- 使用数据库
use seckill;

-- 告诉mysql解释器，该段命令是否已经结束了，mysql是否可以执行了。
-- 默认情况下，delimiter是分号';'。在命令行客户端中，如果有一行命令以分号结束，那么回车后，mysql将会执行该命令，这显然是不行的。
-- 返回值（-3：系统错误 -1：重复秒杀 0：秒杀关闭 1：秒杀成功）
DELIMITER $$
CREATE PROCEDURE `seckill`.`execute_seckill`
  (IN p_seckill_id bigint,
  IN p_user_phone bigint,
  IN p_now_time datetime,
  OUT r_result int)
  BEGIN
   DECLARE update_count int DEFAULT 0;
   START TRANSACTION;
   INSERT IGNORE INTO success_killed
    (seckill_id, user_phone, state, gmt_create, gmt_modified)
    VALUES
    (p_seckill_id, p_user_phone, 0, NOW(), NOW());
    SELECT ROW_COUNT() INTO update_count;
    IF (update_count = 0) THEN
      ROLLBACK;
      -- 重复秒杀
      SET r_result = -1;
    ELSEIF (update_count < 0) THEN
      ROLLBACK;
      -- 系统错误
      SET r_result = -3;
    ELSE
      UPDATE
            seckill
        SET
            quantity = quantity - 1
        WHERE id = p_seckill_id
        AND start_time <= p_now_time
        AND end_time >= p_now_time
        AND quantity > 0;
      SELECT ROW_COUNT() INTO update_count;
      IF (update_count = 0) THEN
        ROLLBACK;
        -- 秒杀关闭
        SET r_result = 0;
      ELSEIF (update_count < 0) THEN
        ROLLBACK;
        -- 系统错误
        SET r_result = -3;
      ELSE
        COMMIT;
        SET r_result = 1;
      END IF;
    END IF;
  END
$$
DELIMITER ;

-- 简单测试
-- set @r_result=-3;
-- call execute_seckill(1001, 18988887778, NOW(), @r_result);
-- SELECT @r_result;
