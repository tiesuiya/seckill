package org.lhpsn.seckill.service;

import org.lhpsn.seckill.domain.Seckill;
import org.lhpsn.seckill.domain.SuccessKilled;
import org.lhpsn.seckill.dto.ExposerDTO;
import org.lhpsn.seckill.exception.SeckillException;

import java.util.List;

/**
 * 秒杀业务接口
 *
 * @author lh
 * @since 1.0.0
 */
public interface SeckillService {

    /**
     * 通过库存id查找
     *
     * @param id 库存id
     * @return 库存对象
     */
    Seckill getSeckillById(Long id);

    /**
     * 通过偏移量查找
     *
     * @param offset 偏移量
     * @param limit  条数
     * @return 库存对象集合
     */
    List<Seckill> listSeckillByOffsetAndLimit(Integer offset, Integer limit);

    /**
     * 输出秒杀接口地址
     *
     * @param seckillId 库存id
     */
    ExposerDTO exportSeckillUrl(Long seckillId);

    /**
     *
     * 执行秒杀
     *
     * @param seckillId 库存id
     * @param userPhone 用户手机号
     * @param md5       md5加密措施
     * @return 秒杀成功明细实体
     * @throws SeckillException 秒杀异常
     */
    SuccessKilled excuteSeckill(Long seckillId, Long userPhone, String md5) throws SeckillException ;
}
