package org.lhpsn.seckill.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lhpsn.seckill.dao.SeckillDao;
import org.lhpsn.seckill.dao.SeckillDaoTest;
import org.lhpsn.seckill.domain.Seckill;
import org.lhpsn.seckill.domain.SuccessKilled;
import org.lhpsn.seckill.dto.ExposerDTO;
import org.lhpsn.seckill.dto.SeckillExecutionDTO;
import org.lhpsn.seckill.enums.SeckillStateEnum;
import org.lhpsn.seckill.exception.RepeatKillException;
import org.lhpsn.seckill.exception.SeckillCloseException;
import org.lhpsn.seckill.exception.SeckillException;
import org.lhpsn.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.DigestUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * @author lh
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})
@Transactional
public class SeckillServiceImplTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private SeckillService seckillService;

    @Autowired
    private SeckillDao seckillDao;

    @Test
    public void getSeckillById() throws Exception {
        Seckill seckill = SeckillDaoTest.generateTestSeckillDate();
        seckillDao.insertSeckill(seckill);

        long id = seckill.getId();
        seckill = seckillService.getSeckillById(id);
        Assert.notNull(seckill, "错误，空对象");
    }

    @Test
    public void listSeckillByOffsetAndLimit() throws Exception {
        Seckill seckill = SeckillDaoTest.generateTestSeckillDate();
        seckillDao.insertSeckill(seckill);

        List<Seckill> list = seckillService.listSeckillByOffsetAndLimit(0, 100);
        Assert.notNull(list, "错误，空集合对象");
    }

    @Test
    public void exportSeckillUrl() throws Exception {
        Seckill seckill = SeckillDaoTest.generateTestSeckillDate();
        seckillDao.insertSeckill(seckill);

        long id = seckill.getId();
        ExposerDTO exposerDTO = seckillService.exportSeckillUrl(id);
        // 预计能获取到秒杀url
        Assert.isTrue(exposerDTO.getExposed(), "错误，获取url失败");
        // 预计能获取到md5
        Assert.notNull(exposerDTO.getMd5(), "错误，空md5");
        // 预计成功
        Assert.isTrue(exposerDTO.getExposed(), "错误，获取url失败");
    }

    @Test
    public void excuteSeckill() throws Exception {
        // 测试1：错误的md5
        Seckill seckill = SeckillDaoTest.generateTestSeckillDate();
        seckillDao.insertSeckill(seckill);
        long userPhone = 1L;
        long seckillId = seckill.getId();
        String md5 = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        SeckillExecutionDTO seckillExecutionDTO = null;
        try {
            // 预计会抛出异常
            seckillExecutionDTO = seckillService.excuteSeckill(seckillId, userPhone, md5);
        } catch (SeckillException e) {
            // 不进行任何操作，只是为了保证程序往下执行
        }
        Assert.isNull(seckillExecutionDTO, "错误，md5判断有问题");

        // 测试2：订单关闭测试
        seckill = SeckillDaoTest.generateTestSeckillDate();
        seckill.setEndTime(simpleDateFormat.parse("2018-01-01 00:00:00"));
        seckillDao.insertSeckill(seckill);
        seckillId = seckill.getId();
        userPhone = 2L;
        md5 = getMD5(seckillId);
        try {
            // 预计会抛出异常
            seckillExecutionDTO = seckillService.excuteSeckill(seckillId, userPhone, md5);
        } catch (SeckillException e) {
            // 不进行任何操作，只是为了保证程序往下执行
        }
        Assert.isNull(seckillExecutionDTO, "错误，订单关闭判断有问题");

        // 测试3：订单重复测试
        seckillExecutionDTO = null;
        SeckillExecutionDTO seckillExecutionDTORepeat = null;
        seckill = SeckillDaoTest.generateTestSeckillDate();
        seckillDao.insertSeckill(seckill);
        seckillId = seckill.getId();
        userPhone = 3L;
        md5 = getMD5(seckillId);
        try {
            // 预计会抛出异常
            seckillService.excuteSeckill(seckillId, userPhone, md5);
            seckillExecutionDTORepeat = seckillService.excuteSeckill(seckillId, userPhone, md5);
        } catch (SeckillException e) {
            // 不进行任何操作，只是为了保证程序往下执行
        }
        Assert.isNull(seckillExecutionDTORepeat, "错误，订单重复判断有问题");

        // 测试4：正常流程测试
        seckillExecutionDTO = null;
        seckill = SeckillDaoTest.generateTestSeckillDate();
        seckillDao.insertSeckill(seckill);
        seckillId = seckill.getId();
        userPhone = 4L;
        md5 = getMD5(seckillId);
        try {
            // 预计会抛出异常
            seckillExecutionDTO = seckillService.excuteSeckill(seckillId, userPhone, md5);
        } catch (SeckillException e) {
            // 不进行任何操作，只是为了保证程序往下执行
        }
        Assert.isTrue(SeckillStateEnum.SUCCESS.getState().equals(seckillExecutionDTO.getState()), "错误，秒杀失败");
    }

    private String getMD5(Long seckillId) {
        // 此处应该和Service类中定义保持一致
        String slat = "weB(o8fy95f4ew*&O(yfdsi5546*%%^%(*&G3F";
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
}