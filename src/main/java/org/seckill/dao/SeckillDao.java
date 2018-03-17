package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.domain.Seckill;

import java.util.Date;
import java.util.List;

/**
 * 秒杀库存dao接口
 *
 * @author lh
 * @since 1.0.0
 */
public interface SeckillDao {

    /**
     * 减库存操作
     *
     * @param id       库存id
     * @param killTime 秒杀时间
     * @return 受影响行数
     */
    Integer reduceNumber(@Param("id") Long id, @Param("killTime") Date killTime);

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
     * @return 秒杀库存对象集合
     */
    List<Seckill> listSeckillByOffsetAndLimit(@Param("offset") Integer offset, @Param("limit") Integer limit);
}
