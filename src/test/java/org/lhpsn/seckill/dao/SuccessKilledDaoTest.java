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

import java.util.List;

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
public class SuccessKilledDaoTest {

    @Autowired
    private SuccessKilledDao successKilledDao;

    @Autowired
    private SeckillDao seckillDao;

    @Test
    @Transactional
    public void testInsertSuccessKilled() throws Exception {

        // 唯一性测试
        Seckill seckill = getPrettySeckill();
        long seckillId = seckill.getId();
        long userPhone = 13888888888L;
        // 第一次插入，预计成功
        int updateCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
        Assert.isTrue(updateCount > 0, "错误，插入失败");
        // 第二次插入，因为业务需要，seckillId和userPhone不能重复，预计失败
        updateCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
        Assert.isTrue(updateCount == 0, "错误，插入成功");
    }

    @Test
    public void testGetSuccessKilledBySeckillId() throws Exception {

        // 获取一个秒杀对象
        Seckill seckill = seckillDao.listSeckillByOffsetAndLimit(0, 1).get(0);

        // 初始化测试参数
        long seckillId = seckill.getId();
        long productId = seckill.getProductId();
        long userPhone = 13888888888L;

        int updateCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
        Assert.isTrue(updateCount > 0, "错误，插入测试数据失败");

        SuccessKilled successKilled = successKilledDao.getSuccessKilledBySeckillId(productId, userPhone);
        System.out.println(successKilled);
        Assert.notNull(successKilled, "错误，查询失败");

        // TODO 添加事务后此处需要调整，目前手动删除所插入数据success_killed
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