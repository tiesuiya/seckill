package org.lhpsn.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lhpsn.seckill.domain.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;

/**
 * 测试秒杀dao
 *
 * @author lh
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})
public class SeckillDaoTest {

    @Autowired
    private SeckillDao seckillDao;

    @Transactional
    @Test
    public void testReduceNumber() throws Exception {

        // 测试1：秒杀时间测试，虚拟一个错误的时间，预计失败
        Seckill seckill = getPrettySeckill();
        Long seckillId = seckill.getId();
        Date killTime = new Date(0);
        int updateCount = seckillDao.reduceNumber(seckillId, killTime);
        Assert.isTrue(updateCount == 0, "错误，更新成功");

        // 测试2：秒杀时间测试，虚拟一个正确的时间，预计成功
        seckillId = seckill.getId();
        killTime = seckill.getStartTime();
        updateCount = seckillDao.reduceNumber(seckillId, killTime);
        Assert.isTrue(updateCount == 1, "错误，更新失败");
    }

    @Test
    public void testGetSeckillById() throws Exception {

        Seckill seckill = getPrettySeckill();
        long id = seckill.getId();
        Seckill seckillGet = seckillDao.getSeckillById(id);
        Assert.notNull(seckillGet, "错误，空对象");
    }

    @Test
    public void testListSeckillByOffsetAndLimit() throws Exception {

        List<Seckill> list = seckillDao.listSeckillByOffsetAndLimit(0, 100);
        Assert.notNull(list, "错误，空集合对象");
    }

    /**
     * 获取秒杀对象，用于测试
     *
     * @return 已存在的正确秒杀对象
     */
    private Seckill getPrettySeckill() {

        // 获取一个秒杀对象
        List<Seckill> seckills = seckillDao.listSeckillByOffsetAndLimit(0, 1);
        Assert.notEmpty(seckills, "错误，没有找到秒杀对象");
        Seckill seckill = seckills.get(0);
        return seckill;
    }
}