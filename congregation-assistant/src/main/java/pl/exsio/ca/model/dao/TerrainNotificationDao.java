/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.model.dao;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import pl.exsio.ca.model.Event;
import pl.exsio.ca.model.ServiceGroup;
import pl.exsio.ca.model.Terrain;
import pl.exsio.ca.model.TerrainNotification;
import pl.exsio.ca.model.TerrainType;
import pl.exsio.frameset.core.dao.GenericDao;

/**
 *
 * @author exsio
 */
public interface TerrainNotificationDao<T extends TerrainNotification> extends GenericDao<T, Long> {

    Iterable<TerrainNotification> findByTerrain(Terrain terrain);

    LinkedHashSet<TerrainNotification> findForTerrainCard(Terrain terrain, Date date);

    LinkedHashSet<TerrainNotification> findForTerrainCard(Terrain terrain);
    
    Set<TerrainNotification> findByEvent(Event event);
    
    Set<TerrainNotification> findByEventAndGroup(Event event, ServiceGroup group );
    
    Set<TerrainNotification> findByEventAndTerrainType(Event event, TerrainType type);
    
    Set<TerrainNotification> findByEventAndGroupAndTerrainType(Event event, ServiceGroup group, TerrainType type);
}
