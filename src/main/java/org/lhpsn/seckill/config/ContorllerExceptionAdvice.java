package org.lhpsn.seckill.config;

import org.lhpsn.seckill.dto.WebDTO;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Set;

/**
 * 全局异常适配
 *
 * @author lh
 * @since 1.0.0
 */
@ControllerAdvice
public class ContorllerExceptionAdvice {


    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public WebDTO handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
//        logger.error("参数验证失败", e);
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        String field = error.getField();
        String code = error.getDefaultMessage();
        String message = String.format("%s:%s", field, code);
        return new WebDTO().failure(message);
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public WebDTO handleBindException(BindException e) {
//        logger.error("参数绑定失败", e);
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        String field = error.getField();
        String code = error.getDefaultMessage();
        String message = String.format("%s:%s", field, code);
        return new WebDTO().failure(message);
    }

    /**
     * 400 - Bad Request
     * 捕获使用由hibernate实现的验证器产生的异常；
     * 注意：Hibernate Validator与Hibernate没有任何依赖关系，这里使用的只是其实现的验证器
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public WebDTO handleServiceException(ConstraintViolationException e) {
//        logger.error("参数验证失败", e);
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        ConstraintViolation<?> violation = violations.iterator().next();
        String message = violation.getMessage();
        return new WebDTO().failure("parameter:" + message);
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    public WebDTO handleValidationException(ValidationException e) {
//        logger.error("参数验证失败", e);
        return new WebDTO().failure("validation_exception");
    }

    /**
     * 全局异常捕捉处理
     *
     * @param ex 所有异常
     * @return 统一包装类型
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public WebDTO errorHandler(Exception ex) {
        return new WebDTO().failure("全局异常捕获：" + ex.getMessage());
    }
}
