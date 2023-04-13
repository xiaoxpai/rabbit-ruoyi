package com.ruoyi.web.controller.report;

import com.ruoyi.common.core.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 图形报表
 * @author xiaoxpai
 */
@Controller
@RequestMapping("/report")
public class GrapReportController extends BaseController {
    private String prefix = "report";


    @GetMapping()
    public String index()
    {
        return prefix +"/grapDemo";
    }
}
