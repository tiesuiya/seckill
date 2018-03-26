package org.lhpsn.seckill.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.lhpsn.seckill.domain.SuccessKilled;
import org.lhpsn.seckill.enums.SeckillStateEnum;

/**
 * 秒杀执行结果DTO
 *
 * @author lh
 * @since 1.0.0
 */
@ApiModel(value = "秒杀响应DTO")
public class ExecutionDTO {

    @ApiModelProperty(value = "秒杀库存id")
    private Long seckillId;

    @ApiModelProperty(value = "状态，SUCCESS(1, \"秒杀成功\"),\n" +
            "    CLOSE(0, \"秒杀关闭\"),\n" +
            "    REPEAT(-1, \"重复秒杀\"),\n" +
            "    DATA_REWRITE(-2, \"数据篡改\"),\n" +
            "    INNER_ERROR(-3, \"系统异常\");")
    private Integer state;

    @ApiModelProperty(value = "状态消息")
    private String stateInfo;

    @ApiModelProperty(value = "秒杀成功对象")
    private SuccessKilled successKilled;

    public ExecutionDTO(Long seckillId, SeckillStateEnum seckillStateEnum, SuccessKilled successKilled) {
        this.seckillId = seckillId;
        this.state = seckillStateEnum.getState();
        this.stateInfo = seckillStateEnum.getStateInfo();
        this.successKilled = successKilled;
    }

    public ExecutionDTO(Long seckillId, SeckillStateEnum seckillStateEnum) {
        this.seckillId = seckillId;
        this.state = seckillStateEnum.getState();
        this.stateInfo = seckillStateEnum.getStateInfo();
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

    @Override
    public String toString() {
        return "ExecutionDTO{" +
                "seckillId=" + seckillId +
                ", state=" + state +
                ", stateInfo='" + stateInfo + '\'' +
                ", successKilled=" + successKilled +
                '}';
    }
}
