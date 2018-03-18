package org.lhpsn.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lhpsn.seckill.domain.Seckill;
import org.lhpsn.seckill.domain.SuccessKilled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


/**
 * 测试秒杀明细dao
 *
 * @author lh
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})
@Transactional
public class SuccessKilledDaoTest {

    @Autowired
    private SuccessKilledDao successKilledDao;

    @Autowired
    private SeckillDao seckillDao;

    @Test
    public void testInsertSuccessKilled() throws Exception {
        Seckill seckill = SeckillDaoTest.generateTestSeckillDate();
        seckillDao.insertSeckill(seckill);

        // 测试1：插入测试
        long seckillId = seckill.getId();
        long userPhone = 13888888888L;
        // 第一次插入，预计成功
        int updateCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
        Assert.isTrue(updateCount > 0, "错误，插入失败");

        // 测试2：唯一性测试
        // 第二次插入，因为业务需要，seckillId和userPhone不能重复，预计失败
        updateCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
        Assert.isTrue(updateCount == 0, "错误，插入成功");
    }

    @Test
    public void testGetSuccessKilledBySeckillIdAndUserPhone() throws Exception {
        Seckill seckill = SeckillDaoTest.generateTestSeckillDate();
        seckillDao.insertSeckill(seckill);

        // 初始化测试数据
        long seckillId = seckill.getId();
        long userPhone = 13888888888L;
        successKilledDao.insertSuccessKilled(seckillId, userPhone);

        // 测试方法获取
        SuccessKilled successKilled = successKilledDao.getSuccessKilledBySeckillIdAndUserPhone(seckillId, userPhone);
        Assert.notNull(successKilled, "错误，查询失败");
    }

}