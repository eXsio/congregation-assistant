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
public interface TerrainAssignment extends Serializable {

    Long getId();

    ServiceGroup getGroup();

    void setGroup(ServiceGroup group);

    Terrain getTerrain();

    void setTerrain(Terrain terrain);

    Date getStartDate();

    void setStartDate(Date start);

    Date getEndDate();

    void setEndDate(Date end);

    String getComment();

    void setComment(String comment);

    boolean isActive();

    void setActive(boolean active);

    Set<TerrainNotification> getNotifications();

    Date getCreatedAt();

    String getCreatedBy();
}
