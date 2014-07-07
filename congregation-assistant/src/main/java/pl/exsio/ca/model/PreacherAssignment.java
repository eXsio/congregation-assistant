/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.exsio.ca.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author exsio
 */
public interface PreacherAssignment extends Serializable {
    
    Long getId();
    
    Preacher getPreacher();
    
    void setPreacher(Preacher preacher);
    
    ServiceGroup getGroup();
    
    void setGroup(ServiceGroup group);
    
    Date getDate();
    
    void setDate(Date date);
    
    Date getCreatedAt();
    
    String getCreatedBy();
    
    boolean isActive();
    
    void setActive(boolean active);
}
