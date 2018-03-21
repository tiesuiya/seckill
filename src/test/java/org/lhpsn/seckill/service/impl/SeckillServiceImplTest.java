package org.lhpsn.seckill.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lhpsn.seckill.dao.SeckillDao;
import org.lhpsn.seckill.dao.SeckillDaoTest;
import org.lhpsn.seckill.domain.Seckill;
import org.lhpsn.seckill.domain.SuccessKilled;
import org.lhpsn.seckill.dto.ExecutionDTO;
import org.lhpsn.seckill.dto.ExposerDTO;
import org.lhpsn.seckill.enums.SeckillStateEnum;
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
import java.util.List;

/**
 * 秒杀服务测试
 *
 * @author lh
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})
// 在测试中发现，当测试方法被@Transactional标记时，事务所涉及的数据库资源只有在RunAfterTestMethodCallbacks类的evaluate方法调用后才被释放。
@Transactional
public class SeckillServiceImplTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private SeckillService seckillService;

    @Autowired
    private SeckillDao seckillDao;

    @Test
    public void testGetSeckillById() throws Exception {
        Seckill seckill = SeckillDaoTest.generateTestSeckillDate();
        seckillDao.insertSeckill(seckill);

        long id = seckill.getId();
        seckill = seckillService.getSeckillById(id);
        Assert.notNull(seckill, "错误，空对象");
    }

    @Test
    public void testListSeckillByOffsetAndLimit() throws Exception {
        Seckill seckill = SeckillDaoTest.generateTestSeckillDate();
        seckillDao.insertSeckill(seckill);

        List<Seckill> list = seckillService.listSeckillByOffsetAndLimit(0, 100);
        Assert.notNull(list, "错误，空集合对象");
    }

    @Test
    public void testExportSeckillUrl() throws Exception {
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
    public void testExcuteSeckill() throws Exception {
        // 测试1：错误的md5
        SuccessKilled seckillExecutionDTO = null;
        Seckill seckill = SeckillDaoTest.generateTestSeckillDate();
        seckillDao.insertSeckill(seckill);
        long userPhone = 1L;
        long seckillId = seckill.getId();
        String md5 = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        try {
            // 预计会抛出异常
            seckillExecutionDTO = seckillService.excuteSeckill(seckillId, userPhone, md5);
        } catch (SeckillException e) {
            // 不进行任何操作，只是为了保证程序往下执行
        }
        Assert.isNull(seckillExecutionDTO, "错误，md5判断有问题");

        // 测试2：订单关闭测试
        seckillExecutionDTO = null;
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
        SuccessKilled seckillExecutionDTORepeat = null;
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
            // 预计不会会抛出异常
            seckillExecutionDTO = seckillService.excuteSeckill(seckillId, userPhone, md5);
        } catch (SeckillException e) {
            // 不进行任何操作，只是为了保证程序往下执行
        }
        Assert.notNull(seckillExecutionDTO, "错误，秒杀失败");
    }

    @Test
    public void testExcuteSeckillProcedure() throws Exception {
        // 测试1：错误的md5
        ExecutionDTO executionDTO = null;
        Seckill seckill = SeckillDaoTest.generateTestSeckillDate();
        seckillDao.insertSeckill(seckill);
        long userPhone = 1L;
        long seckillId = seckill.getId();
        String md5 = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        // 预计会抛出异常
        executionDTO = seckillService.excuteSeckillProcedure(seckillId, userPhone, md5);
        Assert.isTrue(SeckillStateEnum.DATA_REWRITE.getState().equals(executionDTO.getState()), "错误，md5判断有问题");

        // 测试2：订单关闭测试
        executionDTO = null;
        seckill = SeckillDaoTest.generateTestSeckillDate();
        seckill.setEndTime(simpleDateFormat.parse("2018-01-01 00:00:00"));
        seckillDao.insertSeckill(seckill);
        seckillId = seckill.getId();
        userPhone = 2L;
        md5 = getMD5(seckillId);
        // 预计会抛出异常
        executionDTO = seckillService.excuteSeckillProcedure(seckillId, userPhone, md5);
        Assert.isTrue(SeckillStateEnum.CLOSE.getState().equals(executionDTO.getState()), "错误，订单关闭判断有问题");

        // 测试3：订单重复测试
        executionDTO = null;
        ExecutionDTO executionDTORepeat = null;
        seckill = SeckillDaoTest.generateTestSeckillDate();
        seckillDao.insertSeckill(seckill);
        seckillId = seckill.getId();
        userPhone = 3L;
        md5 = getMD5(seckillId);
        // 预计会抛出异常
        executionDTO = seckillService.excuteSeckillProcedure(seckillId, userPhone, md5);
        executionDTORepeat = seckillService.excuteSeckillProcedure(seckillId, userPhone, md5);
        Assert.isTrue(SeckillStateEnum.REPEAT.getState().equals(executionDTORepeat.getState()), "错误，订单重复判断有问题");

        // 测试4：正常流程测试
        executionDTO = null;
        seckill = SeckillDaoTest.generateTestSeckillDate();
        seckillDao.insertSeckill(seckill);
        seckillId = seckill.getId();
        userPhone = 4L;
        md5 = getMD5(seckillId);
        executionDTO = seckillService.excuteSeckillProcedure(seckillId, userPhone, md5);
        Assert.isTrue(SeckillStateEnum.SUCCESS.getState().equals(executionDTO.getState()), "错误，秒杀失败");
    }

    private String getMD5(Long seckillId) {
        // 此处应该和Service类中定义保持一致
        String slat = "weB(o8fy95f4ew*&O(yfdsi5546*%%^%(*&G3F";
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
}