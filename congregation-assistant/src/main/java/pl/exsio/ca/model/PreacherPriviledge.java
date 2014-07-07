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
public interface PreacherPriviledge extends Serializable {

    Long getId();
    
    Preacher getPreacher();

    void setPreacher(Preacher preacher);

    Priviledge getPriviledge();

    void setPriviledge(Priviledge priviledge);

    Date getCreatedAt();

    String getCreatedBy();
}
