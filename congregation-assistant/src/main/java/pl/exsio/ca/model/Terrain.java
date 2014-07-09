/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.SortedSet;

/**
 *
 * @author exsio
 */
public interface Terrain extends Serializable {

    Long getId();

    TerrainType getType();

    void setType(TerrainType type);

    Long getNo();

    void setNo(Long no);
    
    String getName();
    
    void setName(String name);

    SortedSet<TerrainFile> getFiles();

    SortedSet<TerrainAssignment> getAssignments();
    
    Set<TerrainNote> getNotes();

    Date getCreatedAt();

    String getCreatedBy();
    
    boolean isArchival();
    
    void setArchival(boolean archival);
    
    Date getLastNotificationDate();
    
    void setLastNotificationDate(Date lastNotificationDate);
}
