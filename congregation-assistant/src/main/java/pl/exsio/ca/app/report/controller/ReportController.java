/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.app.report.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import pl.exsio.jin.ex.TranslationInitializationException;
import pl.exsio.jin.translator.Translator;
import pl.exsio.frameset.vaadin.ex.FramesetInitializationException;

/**
 *
 * @author exsio
 */
@Controller
@RequestMapping("/")
public class ReportController {
 
    private Translator translator;

    @RequestMapping(value = "/pdf/", method = RequestMethod.GET)
    public ModelAndView pdf(
            @RequestParam(value = "report-name", defaultValue = "", required = true) String reportName,
            @RequestParam Map<String, String> params,
            ModelMap model) {
        this.init();
        if (reportName.equals("")) {
            throw new RuntimeException("Invalid report name: " + reportName);
        }
        return new ModelAndView(reportName, params);
    }

    public void init() {
        try {
            this.translator.registerTranslationFile("pl", "classpath:translations/ca.pl.yml");
            this.translator.init();
        } catch (TranslationInitializationException ex) {
            throw new FramesetInitializationException("There were problems during initialization of Reports Controller", ex);
        }
    }

    @Autowired
    public void setTranslator(Translator translator) {
        this.translator = translator;
    }

}
