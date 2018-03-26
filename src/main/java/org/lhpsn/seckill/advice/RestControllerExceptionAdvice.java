package org.lhpsn.seckill.advice;

import org.lhpsn.seckill.dto.common.ResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Set;

/**
 * Rest类型Controller统一异常处理增强
 * <p>
 * 由于使用了mvc+rest形式的架构方式，故处理异常时不便使用@ControllerAdvice方式
 *
 * @author lh
 * @since 1.0.0
 */
public class RestControllerExceptionAdvice {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseDTO handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        logger.error("缺少请求参数", e);
        return new ResponseDTO().failure("缺少请求参数");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseDTO handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        logger.error("参数解析失败", e);
        return new ResponseDTO().failure("参数解析失败");
    }

    @ExceptionHandler(BindException.class)
    public ResponseDTO handleBindException(BindException e) {
        logger.error("参数绑定失败", e);
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        String field = error.getField();
        String code = error.getDefaultMessage();
        String message = String.format("参数绑定失败 %s:%s", field, code);
        return new ResponseDTO().failure(message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseDTO handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.error("参数验证失败", e);
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        String field = error.getField();
        String code = error.getDefaultMessage();
        String message = String.format("参数验证失败 %s:%s", field, code);
        return new ResponseDTO().failure(message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseDTO handleServiceException(ConstraintViolationException e) {
        logger.error("参数验证失败", e);
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        ConstraintViolation<?> violation = violations.iterator().next();
        String message = violation.getMessage();
        return new ResponseDTO().failure("参数验证失败 " + message);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseDTO handleValidationException(ValidationException e) {
        logger.error("参数验证失败", e);
        return new ResponseDTO().failure("参数验证失败");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseDTO handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        logger.error("不支持当前请求方法", e);
        return new ResponseDTO().failure("不支持当前请求方法");
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseDTO handleHttpMediaTypeNotSupportedException(Exception e) {
        logger.error("不支持当前媒体类型", e);
        return new ResponseDTO().failure("不支持当前媒体类型");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseDTO handleArgumentException(IllegalArgumentException e) {
        logger.error("业务处理失败", e);
        return new ResponseDTO().failure("业务处理失败 " + e.getLocalizedMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseDTO handleException(Exception e) {
        logger.error("服务器异常", e);
        return new ResponseDTO().failure("服务器异常 " + e.getLocalizedMessage());
    }
}
