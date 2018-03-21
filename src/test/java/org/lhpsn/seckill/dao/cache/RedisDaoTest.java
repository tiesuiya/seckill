package org.lhpsn.seckill.dao.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lhpsn.seckill.dao.SeckillDao;
import org.lhpsn.seckill.dao.SeckillDaoTest;
import org.lhpsn.seckill.domain.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.lhpsn.seckill.dao.SeckillDaoTest.generateTestSeckillDate;

/**
 * 测试秒杀RedisDao
 *
 * @author lh
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})
@Transactional
public class RedisDaoTest {

    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private RedisDao redisDao;

    @Test
    public void testRedisDao() throws Exception {
        Seckill seckill = SeckillDaoTest.generateTestSeckillDate();
        seckillDao.insertSeckill(seckill);

        Seckill seckillCache = redisDao.getSeckill(seckill.getId());
        if (seckillCache == null) {
            seckillCache = seckillDao.getSeckillById(seckill.getId());
            if (seckillCache != null) {
                redisDao.putSeckill(seckillCache);
                // 重新取回
                seckillCache = redisDao.getSeckill(seckill.getId());
                Assert.notNull(seckillCache, "Redis缓存对象失败");
                // 规避返回空值对象
                Assert.notNull(seckillCache.getProductName(), "Redis缓存对象失败");
            }
        }
    }

}