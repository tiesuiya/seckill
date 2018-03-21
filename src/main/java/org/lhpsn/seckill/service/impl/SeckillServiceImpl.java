package org.lhpsn.seckill.service.impl;

import org.apache.commons.collections.MapUtils;
import org.lhpsn.seckill.dao.SeckillDao;
import org.lhpsn.seckill.dao.SuccessKilledDao;
import org.lhpsn.seckill.dao.cache.RedisDao;
import org.lhpsn.seckill.domain.Seckill;
import org.lhpsn.seckill.domain.SuccessKilled;
import org.lhpsn.seckill.dto.ExecutionDTO;
import org.lhpsn.seckill.dto.ExposerDTO;
import org.lhpsn.seckill.enums.SeckillStateEnum;
import org.lhpsn.seckill.exception.SeckillCloseException;
import org.lhpsn.seckill.exception.SeckillException;
import org.lhpsn.seckill.exception.SeckillMd5Exception;
import org.lhpsn.seckill.exception.SeckillRepeatException;
import org.lhpsn.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private RedisDao redisDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    /**
     * md5盐值字段，用于混淆md5
     */
    public final static String MD5_SALT = "weB(o8fy95f4ew*&O(yfdsi5546*%%^%(*&G3F";


    @Override
    public Seckill getSeckillById(Long id) {
        return seckillDao.getSeckillById(id);
    }

    @Override
    public List<Seckill> listSeckillByOffsetAndLimit(Integer offset, Integer limit) {
        return seckillDao.listSeckillByOffsetAndLimit(offset, limit);
    }

    @Override
    public ExposerDTO exportSeckillUrl(Long seckillId) {
        // 通过redis缓存获取，此处可以封装优化
        Seckill seckill = redisDao.getSeckill(seckillId);
        if (seckill == null) {
            seckill = seckillDao.getSeckillById(seckillId);
            if (seckill != null) {
                // 缓存起来
                redisDao.putSeckill(seckill);
            }
        }

        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();

        if (seckill == null) {
            return new ExposerDTO(false, seckillId);
        }

        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
            return new ExposerDTO(false, seckillId);
        }

        String md5 = getMD5(seckillId);
        return new ExposerDTO(true, md5, seckillId, nowTime, startTime, endTime);
    }

    /**
     * 使用注解的声明式事物的优点：
     * 1.团队达成约定，形成统一编程风格
     * 2.灵活，不是所有操作都需要事物
     */
    @Override
    @Transactional
    public SuccessKilled excuteSeckill(Long seckillId, Long userPhone, String md5) throws SeckillException {
        Date nowTime = new Date();

        // md5判断
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillMd5Exception("seckill data rewrite");
        }

        /*
         * insert放在前面执行，此微小的操作可提高并发量
         * 当业务场景为用户重复秒杀时，insert失败，程序将在执行update前进入rollback，直接减少了一个步骤的网络延迟和可能出现的GC，从而提高程序性能
         * 代码执行顺序：
         * 当insert在前面：--网络延迟+GC--> update --网络延迟+GC--> insert --网络延迟+GC--> rollback
         * 当update在前面：--网络延迟+GC--> insert --网络延迟+GC--> rollback
         */
        // 秒杀成功记录
        int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
        if (insertCount <= 0) {
            throw new SeckillRepeatException("seckill is repeat");
        }

        // 执行库存更新
        int updateCount = seckillDao.reduceQuantity(seckillId, nowTime);
        if (updateCount <= 0) {
            throw new SeckillCloseException("seckill is close");
        }

        return successKilledDao.getSuccessKilledBySeckillIdAndUserPhone(seckillId, userPhone);
    }

    @Override
    public ExecutionDTO excuteSeckillProcedure(Long seckillId, Long userPhone, String md5) {
        Date nowTime = new Date();

        // md5判断
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            return new ExecutionDTO(seckillId, SeckillStateEnum.DATA_REWRITE);
        }

        Map<String, Object> params = new HashMap<String, Object>(4);
        params.put("seckillId", seckillId);
        params.put("userPhone", userPhone);
        params.put("killTime", nowTime);
        params.put("result", null);
        try {
            // 执行存储过程
            seckillDao.killByProcedure(params);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        int result = MapUtils.getInteger(params, "result", -3);
        if (result == 1) {
            SuccessKilled successKilled = successKilledDao.getSuccessKilledBySeckillIdAndUserPhone(seckillId, userPhone);
            return new ExecutionDTO(seckillId, SeckillStateEnum.SUCCESS, successKilled);
        } else if (result == 0) {
            return new ExecutionDTO(seckillId, SeckillStateEnum.CLOSE);
        } else if (result == -1) {
            return new ExecutionDTO(seckillId, SeckillStateEnum.REPEAT);
        }
        return new ExecutionDTO(seckillId, SeckillStateEnum.INNER_ERROR);
    }

    private String getMD5(Long seckillId) {
        String base = seckillId + "/" + MD5_SALT;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }
}
