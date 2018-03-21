package org.lhpsn.seckill.dto;

import java.util.Date;

/**
 * 暴露秒杀地址DTO
 *
 * @author lh
 * @since 1.0.0
 */
public class ExposerDTO {

    /**
     * 是否开启秒杀
     */
    private Boolean exposed;

    /**
     * 一种加密措施
     */
    private String md5;

    /**
     * 库存id
     */
    private Long seckillId;

    /**
     * 系统当前时间（ms）
     */
    private Date now;

    /**
     * 开启时间
     */
    private Date start;

    /**
     * 结束时间
     */
    private Date end;

    public ExposerDTO(Boolean exposed, Long seckillId) {
        this.exposed = exposed;
        this.seckillId = seckillId;
    }

    public ExposerDTO(Boolean exposed, String md5, Long seckillId, Date now, Date start, Date end) {
        this.exposed = exposed;
        this.md5 = md5;
        this.seckillId = seckillId;
        this.now = now;
        this.start = start;
        this.end = end;
    }

    public Boolean getExposed() {
        return exposed;
    }

    public void setExposed(Boolean exposed) {
        this.exposed = exposed;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(Long seckillId) {
        this.seckillId = seckillId;
    }

    public Date getNow() {
        return now;
    }

    public void setNow(Date now) {
        this.now = now;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "ExposerDTO{" +
                "exposed=" + exposed +
                ", md5='" + md5 + '\'' +
                ", seckillId=" + seckillId +
                ", now=" + now +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
