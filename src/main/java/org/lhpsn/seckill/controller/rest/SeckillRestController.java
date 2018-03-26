package org.lhpsn.seckill.controller.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.lhpsn.seckill.advice.RestControllerExceptionAdvice;
import org.lhpsn.seckill.domain.SuccessKilled;
import org.lhpsn.seckill.dto.ExecutionDTO;
import org.lhpsn.seckill.dto.ExposerDTO;
import org.lhpsn.seckill.dto.common.ResponseDTO;
import org.lhpsn.seckill.enums.SeckillStateEnum;
import org.lhpsn.seckill.exception.SeckillCloseException;
import org.lhpsn.seckill.exception.SeckillMd5Exception;
import org.lhpsn.seckill.exception.SeckillRepeatException;
import org.lhpsn.seckill.dto.ExecutionRequestDTO;
import org.lhpsn.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

/**
 * 秒杀业务路由Rest
 *
 * @author lh
 * @since 1.0.0
 */
@Api(value = "秒杀接口")
@RestController
public class SeckillRestController extends RestControllerExceptionAdvice {

    @Autowired
    private SeckillService seckillService;

    @ApiOperation(value = "获取系统时间")
    @GetMapping(value = "/time/now")
    public ResponseDTO<Long> getSystemTime() {
        Date now = new Date();
        return new ResponseDTO<Long>().success(now.getTime());
    }

    @ApiOperation(value = "获取秒杀地址")
    @GetMapping(value = "/seckills/{id}/exposer")
    public ResponseDTO<ExposerDTO> getExposer(@PathVariable("id") Long id) {
        ExposerDTO exposerDTO = seckillService.exportSeckillUrl(id);
        return new ResponseDTO<ExposerDTO>().success(exposerDTO);
    }

    @ApiOperation(value = "执行秒杀")
    @PostMapping(value = "/seckills/{id}/execution")
    public ResponseDTO<ExecutionDTO> doExecution(@PathVariable("id") Long id,
                                                 @RequestBody @Valid ExecutionRequestDTO param) {
        // 客户端事务控制秒杀
        ExecutionDTO executionDTO;
        SuccessKilled successKilled = null;
        // 业务异常需要自己处理，没办法，暂时想不到更优雅的方式
        try {
            successKilled = seckillService.excuteSeckill(id, Long.valueOf(param.getUserPhone()), param.getMd5());
            executionDTO = new ExecutionDTO(id, SeckillStateEnum.SUCCESS, successKilled);
        } catch (SeckillCloseException e) {
            executionDTO = new ExecutionDTO(id, SeckillStateEnum.CLOSE, successKilled);
        } catch (SeckillRepeatException e) {
            executionDTO = new ExecutionDTO(id, SeckillStateEnum.REPEAT, successKilled);
        } catch (SeckillMd5Exception e) {
            executionDTO = new ExecutionDTO(id, SeckillStateEnum.DATA_REWRITE, successKilled);
        } catch (Exception e) {
            executionDTO = new ExecutionDTO(id, SeckillStateEnum.INNER_ERROR, successKilled);
        }
        return new ResponseDTO<ExecutionDTO>().success(executionDTO);

        // 存储过程控制秒杀
        // ExecutionDTO executionDTO = seckillService.excuteSeckillProcedure(id, userPhone, md5);
        // return new WebDTO<ExecutionDTO>().success(executionDTO);
    }

}
