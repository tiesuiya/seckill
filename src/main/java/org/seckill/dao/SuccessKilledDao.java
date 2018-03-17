package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.domain.SuccessKilled;

/**
 * 秒杀成功明细dao接口
 *
 * @author lh
 * @since 1.0.0
 */
public interface SuccessKilledDao {

    /**
     * 插入秒杀成功明细数据
     *
     * @param productId 商品id
     * @param userPhone 用户手机号
     * @return 受影响行数
     */
    Integer insertSuccessKilled(@Param("seckillId") Long seckillId, @Param("productId") Long productId, @Param("userPhone") Long userPhone);

    /**
     * 通过商品id查找
     *
     * @param productId 商品id
     * @param userPhone 用户手机号
     * @return 秒杀成功明细对象
     */
    SuccessKilled getSuccessKilledBySeckillId(@Param("productId") Long productId, @Param("userPhone") Long userPhone);
}
