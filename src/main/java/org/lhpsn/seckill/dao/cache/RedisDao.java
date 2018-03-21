package org.lhpsn.seckill.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.lhpsn.seckill.domain.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * RedisDao
 * 用户缓存秒杀数据
 *
 * @author lh
 * @since 1.0.0
 */
public class RedisDao {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JedisPool jedisPool;

    /**
     * protostuff序列化schema
     */
    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

    public RedisDao(String ip, Integer port) {
        jedisPool = new JedisPool(ip, port);
    }

    /**
     * 获取秒杀对象
     *
     * @param seckillId 秒杀对象id
     * @return 秒杀对象
     */
    public Seckill getSeckill(long seckillId) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String key = "seckill:" + seckillId;
            // 采用第三方高效序列化方式，减少对象序列化后的size
            byte[] bytes = jedis.get(key.getBytes());
            if (bytes != null) {
                // 空对象
                Seckill seckill = schema.newMessage();
                // 调用api实现反序列化
                ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);
                return seckill;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    /**
     * 添加秒杀对象缓存
     *
     * @param seckill 秒杀对象
     * @return "ok"添加成功
     */
    public String putSeckill(Seckill seckill) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String key = "seckill:" + seckill.getId();
            // 调用api实现序列化
            byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
            // 超时缓存1小时
            int timeout = 60 * 60;
            result = jedis.setex(key.getBytes(), timeout, bytes);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result;
    }

}
