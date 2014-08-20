/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.model.repository;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.data.jpa.repository.Query;
import pl.exsio.ca.model.Event;
import pl.exsio.ca.model.ServiceGroup;
import pl.exsio.ca.model.Terrain;
import pl.exsio.ca.model.TerrainNotification;
import pl.exsio.ca.model.TerrainType;
import pl.exsio.ca.model.dao.TerrainNotificationDao;
import pl.exsio.ca.model.entity.TerrainNotificationImpl;
import pl.exsio.frameset.core.repository.GenericJpaRepository;

/**
 *
 * @author exsio
 */
public interface TerrainNotificationRepository extends GenericJpaRepository<TerrainNotificationImpl, Long>, TerrainNotificationDao<TerrainNotificationImpl> {

    @Override
    @Query("select n from TerrainNotificationImpl n join n.assignment a where a.terrain = ?1 order by date desc")
    Iterable<TerrainNotification> findByTerrain(Terrain terrain);

    @Override
    @Query("select n from TerrainNotificationImpl n join n.assignment a join a.terrain t join a.group g  where a.terrain =?1 and n.date >=?2 order by n.date asc")
    LinkedHashSet<TerrainNotification> findForTerrainCard(Terrain terrain, Date date);

    @Override
    @Query("select n from TerrainNotificationImpl n join n.assignment a join a.terrain t join a.group g where a.terrain =?1 order by n.date asc")
    LinkedHashSet<TerrainNotification> findForTerrainCard(Terrain terrain);

    @Override
    @Query("select n from TerrainNotificationImpl n join n.assignment a where n.event =?1 and a.group = ?2 order by n.date asc")
    Set<TerrainNotification> findByEventAndGroup(Event event, ServiceGroup group);

    @Override
    @Query("select n from TerrainNotificationImpl n join n.assignment a join a.terrain t where n.event =?1 and t.type = ?2 order by n.date asc")
    Set<TerrainNotification> findByEventAndTerrainType(Event event, TerrainType type);

    @Override
    @Query("select n from TerrainNotificationImpl n join n.assignment a join a.terrain t where n.event =?1 and a.group = ?2 and t.type=?3 order by n.date asc")
    Set<TerrainNotification> findByEventAndGroupAndTerrainType(Event event, ServiceGroup group, TerrainType type);
}
