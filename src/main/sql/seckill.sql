-- 秒杀操作（插入记录+更新库存）存储过程

-- 关于存储过程
-- 1：存储过程优化：事务行级锁持有时间
-- 2：不要过度依赖存储过程，这里只是学习怎么写，不真正使用
-- 3：QPS可以达到一个秒杀单6000+

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