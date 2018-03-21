package org.lhpsn.seckill.dto;

/**
 * webDTO，json数据类型接口的包装对象
 *
 * @author lh
 * @since 1.0.0
 */
public class WebDTO<T> {

    /**
     * 执行结果，如果为false，说明客户端调用错误
     */
    private Boolean success;

    /**
     * 业务数据
     */
    private T data;

    /**
     * 错误信息
     */
    private String error;

    public WebDTO<T> success(T data) {
        this.success = true;
        this.data = data;
        return this;
    }

    public WebDTO<T> failure(String error) {
        this.success = false;
        this.error = error;
        return this;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "WebDTO{" +
                "success=" + success +
                ", data=" + data +
                ", error='" + error + '\'' +
                '}';
    }
}
