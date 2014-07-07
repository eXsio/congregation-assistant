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
public interface TerrainNotification extends Serializable {
    
    Long getId();
    
    TerrainAssignment getAssignment();
    
    void setAssignment(TerrainAssignment assignment);
    
    Date getDate();
    
    void setDate(Date date);
    
    Date getCreatedAt();
    
    String getCreatedBy();
    
    String getComment();
    
    void setComment(String comment);
}
