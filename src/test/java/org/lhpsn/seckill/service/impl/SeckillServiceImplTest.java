package org.lhpsn.seckill.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lhpsn.seckill.dao.SeckillDao;
import org.lhpsn.seckill.domain.Seckill;
import org.lhpsn.seckill.dto.ExposerDTO;
import org.lhpsn.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author lh
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})
public class SeckillServiceImplTest {

    @Autowired
    private SeckillService seckillService;


    @Test
    public void getSeckillById() {

        long id = 1000L;
        Seckill seckill = seckillService.getSeckillById(id);
        Assert.notNull(seckill, "错误，空对象");
    }

    @Test
    public void listSeckillByOffsetAndLimit() {

        List<Seckill> list = seckillService.listSeckillByOffsetAndLimit(0, 100);
        Assert.notNull(list, "错误，空集合对象");
    }

    @Test
    public void exportSeckillUrl() {

        long id = 1000L;
        ExposerDTO exposerDTO = seckillService.exportSeckillUrl(id);
        // 预计能获取到秒杀url
        Assert.isTrue(exposerDTO.getExposed(), "错误，获取url失败");
        // 预计能获取到md5
        Assert.notNull(exposerDTO.getMd5(), "错误，空md5");

        // TODO 业务未完全覆盖
    }

    @Test
    public void excuteSeckill() {

        long id = 1000L;
        ExposerDTO exposerDTO = seckillService.exportSeckillUrl(id);

        // TODO 未完成
    }
}