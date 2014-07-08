/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 *
 * @author exsio
 */
public interface ServiceGroup extends Serializable {

    Long getId();

    Long getNo();

    void setNo(Long no);

    Preacher getOverseer();

    void setOverseer(Preacher overseer);

    Set<PreacherAssignment> getPreacherAssignments();

    Set<TerrainAssignment> getTerrainAssignments();

    Date getCreatedAt();

    String getCreatedBy();
    
    boolean isArchival();
    
    void setArchival(boolean archival);
    
    String getCaption();
}
