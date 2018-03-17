package org.lhpsn.seckill.exception;

/**
 * 秒杀异常
 *
 * @author lh
 * @since 1.0.0
 */
public class SeckillException extends RuntimeException {

    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
