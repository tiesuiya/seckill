package org.lhpsn.seckill.dto.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 全局JSON响应数据包装对象
 *
 * @author lh
 * @since 1.0.0
 */
@ApiModel(value = "全局JSON响应数据包装对象", description = "所有JSON响应信息都将被该对象包装后返回")
public class ResponseDTO<T> {

    @ApiModelProperty(value = "操作结果", notes = "若返回false，说明客户端调用错误而非业务错误，客户端可通过判断该值来判断是否是自身调用错误")
    private Boolean success;

    @ApiModelProperty(value = "业务数据", notes = "根据不同业务场景，将会返回不同类型对象，详见对应接口文档")
    private T data;

    @ApiModelProperty(value = "开发人员用错误信息", notes = "此值仅供开发人员参考，不能直接展示给用户！具体业务的错误消息将在data中定义")
    private String error;

    public ResponseDTO<T> success(T data) {
        this.success = true;
        this.data = data;
        return this;
    }

    public ResponseDTO<T> failure(String error) {
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
