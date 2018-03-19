package org.lhpsn.seckill.enums;

/**
 * 秒杀状态枚举
 * TODO 包名enums是不是有点问题哦
 *
 * @author lh
 * @since 1.0.0
 */
public enum SeckillStateEnum {

    INNER_ERROR(-3, "系统异常"),
    DATA_REWRITE(-2, "数据篡改"),
    REPEAT(-1, "重复秒杀"),
    CLOSE(0, "秒杀结束"),
    SUCCESS(1, "秒杀成功");

    private Integer state;
    private String stateInfo;

    SeckillStateEnum(Integer state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public static SeckillStateEnum stateOf(Integer state) {
        for (SeckillStateEnum seckillStateEnum : values()) {
            if (seckillStateEnum.getState() == state) {
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
