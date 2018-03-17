package org.lhpsn.seckill.dto;

import org.lhpsn.seckill.domain.SuccessKilled;
import org.lhpsn.seckill.enums.SeckillStateEnum;

/**
 * 秒杀执行结果DTO
 *
 * @author lh
 * @since 1.0.0
 */
public class SeckillExecutionDTO {

    // 秒杀库存id
    private Long seckillId;

    // 状态：0失败 1成功
    private Integer state;

    // 状态消息
    private String stateInfo;

    // 秒杀成功对象
    private SuccessKilled successKilled;

    public SeckillExecutionDTO(Long seckillId, SeckillStateEnum seckillStateEnum, SuccessKilled successKilled) {
        this.seckillId = seckillId;
        this.state = seckillStateEnum.getState();
        this.stateInfo = seckillStateEnum.getStateInfo();
        this.successKilled = successKilled;
    }

    public Long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(Long seckillId) {
        this.seckillId = seckillId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public SuccessKilled getSuccessKilled() {
        return successKilled;
    }

    public void setSuccessKilled(SuccessKilled successKilled) {
        this.successKilled = successKilled;
    }
}
