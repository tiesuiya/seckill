package org.lhpsn.seckill.exception;

/**
 * 重复秒杀异常
 *
 * @author lh
 * @since 1.0.0
 */
public class RepeatKillException extends SeckillException {

    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
