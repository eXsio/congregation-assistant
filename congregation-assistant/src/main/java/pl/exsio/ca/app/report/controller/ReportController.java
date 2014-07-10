/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.exsio.ca.app.report.controller;

import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author exsio
 */
@Controller
@RequestMapping("/")
public class ReportController {
    
    @RequestMapping(value = "/pdf/", method = RequestMethod.GET)
    public  ModelAndView pdf(
            @RequestParam(value = "report-name", defaultValue = "", required = true)String reportName,
            @RequestParam Map<String,String> params,
            ModelMap model)
    {
        if(reportName.equals("")) {
            throw new RuntimeException("Invalid report name: " + reportName);
        }
        return new ModelAndView(reportName, params);
    }
}
