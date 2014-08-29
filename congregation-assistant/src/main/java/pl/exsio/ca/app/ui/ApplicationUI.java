/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.app.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import pl.exsio.frameset.vaadin.ui.FramesetUI;

/**
 *
 * @author exsio
 */
@Theme("ca")
@Title("Asystent Zboru")
public class ApplicationUI extends FramesetUI {

    @Override
    protected String getPathSegmentDelimiter() {
        return ".";
    }
    
    /**
     *
     */
    @Override
     protected void init(VaadinRequest request) { 
         this.getTranslator().registerTranslationFile("pl", "classpath:translations/ca.pl.yml");
         super.init(request);
     }

}