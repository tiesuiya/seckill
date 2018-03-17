package org.lhpsn.seckill.service.impl;

import org.lhpsn.seckill.domain.Seckill;
import org.lhpsn.seckill.domain.SuccessKilled;
import org.lhpsn.seckill.enums.SeckillStateEnum;
import org.lhpsn.seckill.dao.SeckillDao;
import org.lhpsn.seckill.dao.SuccessKilledDao;
import org.lhpsn.seckill.dto.ExposerDTO;
import org.lhpsn.seckill.dto.SeckillExecutionDTO;
import org.lhpsn.seckill.exception.RepeatKillException;
import org.lhpsn.seckill.exception.SeckillCloseException;
import org.lhpsn.seckill.exception.SeckillException;
import org.lhpsn.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * 秒杀业务接口实现
 *
 * @author lh
 * @since 1.0.0
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    // md5盐值字段，用于混淆md5
    private final static String slat = "weB(o8fy95f4ew*&O(yfdsi5546*%%^%(*&G3F";


    public Seckill getSeckillById(Long id) {
        return seckillDao.getSeckillById(id);
    }

    public List<Seckill> listSeckillByOffsetAndLimit(Integer offset, Integer limit) {
        return seckillDao.listSeckillByOffsetAndLimit(offset, limit);
    }

    public ExposerDTO exportSeckillUrl(Long seckillId) {

        Seckill seckill = seckillDao.getSeckillById(seckillId);
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();

        if (seckill == null) {
            return new ExposerDTO(false, null, seckillId, null, null, null);
        }

        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
            return new ExposerDTO(false, null, seckillId, null, null, null);
        }

        String md5 = getMD5(seckillId);
        return new ExposerDTO(true, md5, seckillId, nowTime, startTime, endTime);
    }

    /**
     * 使用注解的声明式事物的优点：
     * 1.团队达成约定，形成统一编程风格
     * 2.灵活，不是所有操作都需要事物
     */
    @Transactional
    public SeckillExecutionDTO excuteSeckill(Long seckillId, Long userPhone, String md5) throws SeckillException, SeckillCloseException, RepeatKillException {

        Date nowTime = new Date();

        // 手机验证...省略

        // md5判断
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data rewrite");
        }

        try {
            // 执行库存更新
            int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
            if (updateCount <= 0) {
                throw new SeckillCloseException("seckill is close");
            }

            // 秒杀成功记录
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            if (insertCount <= 0) {
                throw new RepeatKillException("seckill is repeat");
            }

            SuccessKilled successKilled = successKilledDao.getSuccessKilledBySeckillId(seckillId, userPhone);
            return new SeckillExecutionDTO(seckillId, SeckillStateEnum.SUCCESS, successKilled);
        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e3) {
            logger.error(e3.getMessage(), e3);
            throw new SeckillException("seckill inner error:" + e3.getMessage());
        }
    }

    private String getMD5(Long seckillId) {
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
}
