/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.app.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.ErrorHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification;
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
         this.setErrorHandler(new ErrorHandler() {

             @Override
             public void error(com.vaadin.server.ErrorEvent event) {
                 Notification.show(event.getThrowable().getMessage(), Notification.Type.TRAY_NOTIFICATION);
             }
         });
         this.getTranslator().registerTranslationFile("pl", "classpath:translations/ca.pl.yml");
         super.init(request);
     }

}