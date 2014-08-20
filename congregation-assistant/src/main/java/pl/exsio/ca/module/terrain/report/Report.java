/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.exsio.ca.module.terrain.report;

import com.vaadin.ui.Component;
import pl.exsio.frameset.security.acl.AclSubject;

/**
 *
 * @author exsio
 */
public interface Report extends Component, AclSubject {
    
    String getName();
    
    Report load();
}
