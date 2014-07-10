/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.exsio.ca.model.dao;

import java.util.Date;
import pl.exsio.ca.model.Terrain;
import pl.exsio.ca.model.TerrainAssignment;
import pl.exsio.frameset.core.dao.GenericDao;

/**
 *
 * @author exsio
 */
public interface TerrainAssignmentDao<T extends TerrainAssignment> extends GenericDao<T, Long> {
    
    Iterable<TerrainAssignment> findByTerrainAndActive(Terrain terrain, boolean active);
    
    Iterable<TerrainAssignment> findAfter(Terrain terrain, Date date);
    
    Iterable<TerrainAssignment> findBefore(Terrain terrain, Date date);
    
    Iterable<TerrainAssignment> findAfterOrEqual(Terrain terrain, Date date);
    
    Iterable<TerrainAssignment> findBeforeOrEqual(Terrain terrain, Date date);
    
    int deactivateAll(Terrain terrain);
    
    TerrainAssignment findLatest(Terrain terrain);
    
    TerrainAssignment findActive(Terrain terrain);
    
    int setActive(Long id);
    
    Iterable<TerrainAssignment> findByTerrainAndDate(Terrain terrain, Date date);
}
