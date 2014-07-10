/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.model.repository;

import java.util.Date;
import java.util.LinkedHashSet;
import org.springframework.data.jpa.repository.Query;
import pl.exsio.ca.model.ServiceGroup;
import pl.exsio.ca.model.Terrain;
import pl.exsio.ca.model.TerrainType;
import pl.exsio.ca.model.dao.TerrainDao;
import pl.exsio.ca.model.entity.TerrainImpl;
import pl.exsio.frameset.core.repository.GenericJpaRepository;

/**
 *
 * @author exsio
 */
public interface TerrainRepository extends GenericJpaRepository<TerrainImpl, Long>, TerrainDao<TerrainImpl> {

    @Override
    @Query("from TerrainImpl where type = ?1 order by type, no")
    LinkedHashSet<Terrain> findByType(TerrainType type);

    @Override
    @Query("select t from TerrainImpl t join t.assignments a where a.group = ?1 and a.active = true order by t.type, t.no")
    LinkedHashSet<Terrain> findByGroup(ServiceGroup group);

    @Override
    @Query("select t from TerrainImpl t join t.assignments a where a.group = ?2 and a.active = true and t.type = ?1 order by t.type, t.no")
    LinkedHashSet<Terrain> findByTypeAndGroup(TerrainType type, ServiceGroup group);

    @Override
    @Query("from TerrainImpl where lastNotificationDate >= ?1 order by type, no")
    LinkedHashSet<Terrain> findByDate(Date date);

    @Override
    @Query("from TerrainImpl where type=?1 and lastNotificationDate >= ?2 order by type, no")
    LinkedHashSet<Terrain> findByTypeAndDate(TerrainType type, Date date);

    @Override
    @Query("select t from TerrainImpl t join t.assignments a where a.group = ?1 and a.active = true and t.lastNotificationDate >= ?2 order by t.type, t.no")
    LinkedHashSet<Terrain> findByGroupAndDate(ServiceGroup group, Date date);

    @Override
    @Query("select t from TerrainImpl t join t.assignments a where a.group = ?2 and a.active = true and t.lastNotificationDate >= ?3 and t.type = ?1 order by t.type, t.no")
    LinkedHashSet<Terrain> findByTypeAndGroupAndDate(TerrainType type, ServiceGroup group, Date date);

    @Override
    @Query("from TerrainImpl order by type, no")
    LinkedHashSet<Terrain> findAllTerrains();
}
