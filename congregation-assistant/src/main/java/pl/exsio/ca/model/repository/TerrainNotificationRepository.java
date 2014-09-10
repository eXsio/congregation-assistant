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
    @Query("select n from caTerrainNotificationImpl n join n.assignment a where a.terrain = ?1 order by date desc")
    Iterable<TerrainNotification> findByTerrain(Terrain terrain);

    @Override
    @Query("select n from caTerrainNotificationImpl n join n.assignment a join a.terrain t join a.group g  where a.terrain =?1 and n.date >=?2 order by n.date asc")
    LinkedHashSet<TerrainNotification> findForTerrainCard(Terrain terrain, Date date);

    @Override
    @Query("select n from caTerrainNotificationImpl n join n.assignment a join a.terrain t join a.group g where a.terrain =?1 order by n.date asc")
    LinkedHashSet<TerrainNotification> findForTerrainCard(Terrain terrain);

    @Override
    @Query("select n from caTerrainNotificationImpl n join n.assignment a where n.event =?1 and a.group = ?2 order by n.date asc")
    Set<TerrainNotification> findByEventAndGroup(Event event, ServiceGroup group);

    @Override
    @Query("select n from caTerrainNotificationImpl n join n.assignment a join a.terrain t where n.event =?1 and t.type = ?2 order by n.date asc")
    Set<TerrainNotification> findByEventAndTerrainType(Event event, TerrainType type);

    @Override
    @Query("select n from caTerrainNotificationImpl n join n.assignment a join a.terrain t where n.event =?1 and a.group = ?2 and t.type=?3 order by n.date asc")
    Set<TerrainNotification> findByEventAndGroupAndTerrainType(Event event, ServiceGroup group, TerrainType type);

    @Override
    @Query("select n from caTerrainNotificationImpl n where n.date>= ?1 and n.date <= ?2 order by n.date asc")
    Set<TerrainNotification> findByDateRange(Date start, Date end);

    @Override
    @Query("select n from caTerrainNotificationImpl n join n.assignment a join a.terrain t where n.date>= ?1 and n.date <= ?2 and (a.group = ?3 or ((a.endDate is not null) and (select g2 from caTerrainAssignmentImpl a2 join a2.group g2 where a2.startDate = (select max(a3.startDate) from caTerrainAssignmentImpl a3 where a3.terrain = t and ((a3.startDate >= ?1 and a3.startDate <=?2) or (a3.endDate >= ?1 and a3.endDate <=?2) or (a3.startDate <= ?1 and a3.endDate >=?2))  ) and a2.terrain = t ) = ?3)) order by n.date asc")
    Set<TerrainNotification> findByDateRangeAndGroup(Date start, Date end, ServiceGroup group);

    @Override
    @Query("select n from caTerrainNotificationImpl n join n.assignment a join a.terrain t where n.date>= ?1 and n.date <= ?2 and t.type=?3 order by n.date asc")
    Set<TerrainNotification> findByDateRangeAndTerrainType(Date start, Date end, TerrainType type);

    @Override
    @Query("select n from caTerrainNotificationImpl n join n.assignment a join a.terrain t where n.date>= ?1 and n.date <= ?2 and (a.group = ?3 or ((a.endDate is not null) and (select g2 from caTerrainAssignmentImpl a2 join a2.group g2 where a2.startDate = (select max(a3.startDate) from caTerrainAssignmentImpl a3 where a3.terrain = t and ((a3.startDate >= ?1 and a3.startDate <=?2) or (a3.endDate >= ?1 and a3.endDate <=?2) or (a3.startDate <= ?1 and a3.endDate >=?2))) and a2.terrain = t ) = ?3)) and t.type = ?4 order by n.date asc")
    Set<TerrainNotification> findByDateRangeAndGroupAndTerrainType(Date start, Date end, ServiceGroup group, TerrainType type);
}
