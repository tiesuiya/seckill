package org.lhpsn.seckill.enums;

/**
 * 秒杀状态枚举
 *
 * @author lh
 * @since 1.0.0
 */
public enum SeckillStateEnum {

    SUCCESS(1, "秒杀成功"),
    CLOSE(0, "秒杀结束"),
    REPEAT(-1, "重复秒杀"),
    DATA_REWRITE(-2, "数据篡改"),
    INNER_ERROR(-3, "系统异常");

    private Integer state;
    private String stateInfo;

    SeckillStateEnum(Integer state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public static SeckillStateEnum stateOf(Integer state) {
        for (SeckillStateEnum seckillStateEnum : values()) {
            if (seckillStateEnum.getState().equals(state)) {
                return seckillStateEnum;
            }
        }
        return null;
    }

    public Integer getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

}
