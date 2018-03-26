package org.lhpsn.seckill.controller;

import org.lhpsn.seckill.domain.Seckill;
import org.lhpsn.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
}
