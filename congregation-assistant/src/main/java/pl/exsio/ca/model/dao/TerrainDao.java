/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.model.dao;

import java.util.Date;
import java.util.LinkedHashSet;
import pl.exsio.ca.model.ServiceGroup;
import pl.exsio.ca.model.Terrain;
import pl.exsio.ca.model.TerrainType;
import pl.exsio.frameset.core.dao.GenericDao;

/**
 *
 * @author exsio
 */
public interface TerrainDao<T extends Terrain> extends GenericDao<T, Long> {

    LinkedHashSet<Terrain> findByType(TerrainType type);

    LinkedHashSet<Terrain> findByGroup(ServiceGroup group);

    LinkedHashSet<Terrain> findByTypeAndGroup(TerrainType type, ServiceGroup group);

    LinkedHashSet<Terrain> findByDate(Date date);

    LinkedHashSet<Terrain> findByTypeAndDate(TerrainType type, Date date);

    LinkedHashSet<Terrain> findByGroupAndDate(ServiceGroup group, Date date);

    LinkedHashSet<Terrain> findByTypeAndGroupAndDate(TerrainType type, ServiceGroup group, Date date);

    LinkedHashSet<Terrain> findAllTerrains();
}
