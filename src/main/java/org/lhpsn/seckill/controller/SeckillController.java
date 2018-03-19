package org.lhpsn.seckill.controller;

import org.lhpsn.seckill.domain.Seckill;
import org.lhpsn.seckill.domain.SuccessKilled;
import org.lhpsn.seckill.dto.ExecutionDTO;
import org.lhpsn.seckill.dto.ExposerDTO;
import org.lhpsn.seckill.dto.WebDTO;
import org.lhpsn.seckill.enums.SeckillStateEnum;
import org.lhpsn.seckill.exception.SeckillMd5Exception;
import org.lhpsn.seckill.exception.SeckillRepeatException;
import org.lhpsn.seckill.exception.SeckillCloseException;
import org.lhpsn.seckill.param.SeckillParam;
import org.lhpsn.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * 秒杀业务路由
 *
 * @author lh
 * @since 1.0.0
 */
@Controller
public class SeckillController {

    @Autowired
    private SeckillService seckillService;

    @GetMapping(value = "/seckills")
    public String listSeckills(Model model) {
        // TODO 分页返回
        List<Seckill> seckills = seckillService.listSeckillByOffsetAndLimit(0, 100);
        model.addAttribute("list", seckills);
        return "seckill/list";
    }

    @GetMapping(value = "/seckills/{id}")
    public String getSeckillDetail(@PathVariable("id") Long id, Model model) {
        Seckill seckills = seckillService.getSeckillById(id);
        model.addAttribute("seckill", seckills);
        return "/seckill/detail";
    }

    @GetMapping(value = "/time/now")
    @ResponseBody
    public WebDTO<Long> getSystemTime() {
        Date now = new Date();
        return new WebDTO<Long>().success(now.getTime());
    }

    @GetMapping(value = "/seckills/{id}/exposer")
    @ResponseBody
    public WebDTO<ExposerDTO> getExposer(@PathVariable("id") Long id) {
        ExposerDTO exposerDTO = seckillService.exportSeckillUrl(id);
        return new WebDTO<ExposerDTO>().success(exposerDTO);
    }

    @PostMapping(value = "/seckills/{id}/execution")
    @ResponseBody
    public WebDTO<ExecutionDTO> doExecution(@PathVariable("id") Long id, @Valid SeckillParam seckillParam) {
        ExecutionDTO executionDTO;
        SuccessKilled successKilled = null;
        // 业务异常需要自己处理，没办法，暂时想不到更优雅的方式
        try {
            successKilled = seckillService.excuteSeckill(id, seckillParam.getUserPhone(), seckillParam.getMd5());
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
        return new WebDTO<ExecutionDTO>().success(executionDTO);
    }

}