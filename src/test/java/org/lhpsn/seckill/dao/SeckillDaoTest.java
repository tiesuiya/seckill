package org.lhpsn.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lhpsn.seckill.domain.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
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
@Transactional
public class SeckillDaoTest {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private SeckillDao seckillDao;

    @Test
    public void testInsertSeckill() throws Exception {
        Seckill seckill = generateTestSeckillDate();
        int insertCount = seckillDao.insertSeckill(seckill);
        Assert.isTrue(insertCount == 1, "错误，插入失败");
    }

    @Test
    public void testReduceQuantity() throws Exception {
        Seckill seckill = generateTestSeckillDate();
        seckillDao.insertSeckill(seckill);

        // 测试1：秒杀时间测试，虚拟一个错误的时间，预计失败
        long seckillId = seckill.getId();
        Date killTime = simpleDateFormat.parse("2017-01-01 00:00:00");
        int updateCount = seckillDao.reduceQuantity(seckillId, killTime);
        Assert.isTrue(updateCount == 0, "错误，更新成功");

        // 测试2：秒杀时间测试，虚拟一个正确的时间，预计成功
        seckillId = seckill.getId();
        killTime = simpleDateFormat.parse("2019-01-01 00:00:00");
        updateCount = seckillDao.reduceQuantity(seckillId, killTime);
        Assert.isTrue(updateCount == 1, "错误，更新失败");
    }

    @Test
    public void testGetSeckillById() throws Exception {
        Seckill seckill = generateTestSeckillDate();
        seckillDao.insertSeckill(seckill);

        long seckillId = seckill.getId();
        Seckill seckillGet = seckillDao.getSeckillById(seckillId);
        Assert.notNull(seckillGet, "错误，空对象");
    }

    @Test
    public void testListSeckillByOffsetAndLimit() throws Exception {
        Seckill seckill = generateTestSeckillDate();
        seckillDao.insertSeckill(seckill);

        List<Seckill> list = seckillDao.listSeckillByOffsetAndLimit(0, 100);
        Assert.notNull(list, "错误，空集合对象");
    }

    /**
     * 构造测试对象
     *
     * @return 测试秒杀库存对象，有效期18年-9999年
     */
    public static Seckill generateTestSeckillDate() throws Exception {
        // 插入测试数据
        Seckill seckill = new Seckill();
        seckill.setProductId(9090950L);
        seckill.setProductName("2元秒杀玉溪（软）");
        seckill.setQuantity(100);
        seckill.setStartTime(simpleDateFormat.parse("2018-01-01 00:00:00"));
        seckill.setEndTime(simpleDateFormat.parse("9999-01-01 00:00:00"));
        return seckill;
    }

}