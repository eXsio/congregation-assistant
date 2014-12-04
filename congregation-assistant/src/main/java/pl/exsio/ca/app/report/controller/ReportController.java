/* 
 * The MIT License
 *
 * Copyright 2014 exsio.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
