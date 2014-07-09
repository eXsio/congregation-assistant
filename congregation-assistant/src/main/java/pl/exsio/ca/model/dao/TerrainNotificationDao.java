/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.exsio.ca.model.dao;

import pl.exsio.ca.model.Terrain;
import pl.exsio.ca.model.TerrainNotification;
import pl.exsio.frameset.core.dao.GenericDao;

/**
 *
 * @author exsio
 */
public interface TerrainNotificationDao<T extends TerrainNotification> extends GenericDao<T, Long> {
    
    Iterable<TerrainNotification> findByTerrain(Terrain terrain);
}
