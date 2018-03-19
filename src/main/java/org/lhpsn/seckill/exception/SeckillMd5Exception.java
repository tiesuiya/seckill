package org.lhpsn.seckill.exception;

/**
 * 秒杀数据篡改异常
 *
 * @author lh
 * @since 1.0.0
 */
public class SeckillMd5Exception extends SeckillException {

    public SeckillMd5Exception(String message) {
        super(message);
    }

    public SeckillMd5Exception(String message, Throwable cause) {
        super(message, cause);
    }
}
