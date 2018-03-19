package org.lhpsn.seckill.exception;

/**
 * 重复秒杀异常
 *
 * @author lh
 * @since 1.0.0
 */
public class SeckillRepeatException extends SeckillException {

    public SeckillRepeatException(String message) {
        super(message);
    }

    public SeckillRepeatException(String message, Throwable cause) {
        super(message, cause);
    }
}
