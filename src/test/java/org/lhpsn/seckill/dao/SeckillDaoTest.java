package org.lhpsn.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lhpsn.seckill.domain.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;

/**
 * @author lh
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {

    @Autowired
    private SeckillDao seckillDao;

    @Test
    public void testReduceNumber() throws Exception {

        Date killTime = new Date(0);
        int updateCount = seckillDao.reduceNumber(1000L, killTime);
        Assert.isTrue(updateCount == 0, "错误，更新成功");
    }

    @Test
    public void testGetSeckillById() throws Exception {

        long id = 1000L;
        Seckill seckill = seckillDao.getSeckillById(id);
        Assert.notNull(seckill, "错误，空对象");
    }

    @Test
    public void testListSeckillByOffsetAndLimit() throws Exception {

        List<Seckill> list = seckillDao.listSeckillByOffsetAndLimit(0, 100);
        Assert.notNull(list, "错误，空集合对象");
    }

}