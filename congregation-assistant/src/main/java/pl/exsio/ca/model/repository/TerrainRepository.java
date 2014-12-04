/* 
 * The MIT License
 *
 * Copyright 2014 exsio.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package pl.exsio.ca.model.repository;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import org.springframework.data.jpa.repository.Query;
import pl.exsio.ca.model.Event;
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
    @Query("from caTerrainImpl where type = ?1 order by type, no")
    LinkedHashSet<Terrain> findByType(TerrainType type);

    @Override
    @Query("select distinct t from caTerrainImpl t join t.assignments a where a.group = ?1 and a.active = true order by t.type, t.no")
    LinkedHashSet<Terrain> findByGroup(ServiceGroup group);

    @Override
    @Query("select distinct t from caTerrainImpl t join t.assignments a where a.group = ?2 and a.active = true and t.type = ?1 order by t.type, t.no")
    LinkedHashSet<Terrain> findByTypeAndGroup(TerrainType type, ServiceGroup group);

    @Override
    @Query("select t from caTerrainImpl t where lastNotificationDate >= ?1 order by t.type, t.no")
    LinkedHashSet<Terrain> findByLastNotificationDate(Date date);

    @Override
    @Query("select t from caTerrainImpl t where type=?1 and lastNotificationDate >= ?2 order by t.type, t.no")
    LinkedHashSet<Terrain> findByTypeAndLastNotificationDate(TerrainType type, Date date);

    @Override
    @Query("select distinct t from caTerrainImpl t join t.assignments a where a.group = ?1 and a.active = true and t.lastNotificationDate >= ?2 order by t.type, t.no")
    LinkedHashSet<Terrain> findByGroupAndLastNotificationDate(ServiceGroup group, Date date);

    @Override
    @Query("select distinct t from caTerrainImpl t join t.assignments a where a.group = ?2 and a.active = true and t.lastNotificationDate >= ?3 and t.type = ?1 order by t.type, t.no")
    LinkedHashSet<Terrain> findByTypeAndGroupAndLastNotificationDate(TerrainType type, ServiceGroup group, Date date);

    @Override
    @Query("select t from caTerrainImpl t order by t.type, t.no")
    LinkedHashSet<Terrain> findAllTerrains();

    @Override
    @Query("select distinct t from caTerrainImpl t join t.assignments a join a.notifications n where n.event = ?1 order by t.type, t.no")
    LinkedHashSet<Terrain> findByEvent(Event event);

    @Override
    @Query("select distinct t from caTerrainImpl t join t.assignments a join a.notifications n where t.type = ?1 and n.event = ?2 order by t.type, t.no")
    LinkedHashSet<Terrain> findByTypeAndEvent(TerrainType type, Event event);

    @Override
    @Query("select distinct t from caTerrainImpl t join t.assignments a join a.notifications n where a.group = ?1 and n.event = ?2 order by t.type, t.no")
    LinkedHashSet<Terrain> findByGroupAndEvent(ServiceGroup group, Event event);

    @Override
    @Query("select distinct t from caTerrainImpl t join t.assignments a join a.notifications n where t.type = ?1 and a.group = ?2 and n.event = ?3 order by t.type, t.no")
    LinkedHashSet<Terrain> findByTypeAndGroupAndEvent(TerrainType type, ServiceGroup group, Event event);

    @Override
    @Query("select distinct t from caTerrainImpl t join t.assignments a where a.startDate <= ?1 order by t.type, t.no")
    LinkedHashSet<Terrain> findByAssignmentDate(Date date);

    @Override
    @Query("select distinct t from caTerrainImpl t join t.assignments a where a.group = ?1 and a.startDate <= ?2 and (a.endDate >= ?2 or a.endDate is null) order by t.type, t.no")
    LinkedHashSet<Terrain> findByGroupAndAssignmentDate(ServiceGroup group, Date date);

    @Override
    @Query("select distinct t from caTerrainImpl t join t.assignments a where t.type=?1 and a.group = ?2 and a.startDate <= ?3 and (a.endDate >= ?3 or a.endDate is null) order by t.type, t.no")
    LinkedHashSet<Terrain> findByTypeAndGroupAndAssignmentDate(TerrainType type, ServiceGroup group, Date date);

    @Override
    @Query("select distinct t from caTerrainImpl t join t.assignments a where t.type=?1 and a.startDate <= ?2 and (a.endDate >= ?2 or a.endDate is null) order by t.type, t.no")
    LinkedHashSet<Terrain> findByTypeAndAssignmentDate(TerrainType type, Date date);

    @Override
    @Query("select distinct t from caTerrainNotificationImpl n join n.assignment a join a.terrain t where n.date>= ?1 and n.date <= ?2 order by n.date asc")
    LinkedHashSet<Terrain> findByNotificationDateRange(Date start, Date end);

    @Override
    @Query("select distinct t from caTerrainNotificationImpl n join n.assignment a join a.terrain t where n.date>= ?2 and n.date <= ?3 and (a.group = ?1 or ((a.endDate is not null) and (select g2 from caTerrainAssignmentImpl a2 join a2.group g2 where a2.startDate = (select max(a3.startDate) from caTerrainAssignmentImpl a3 where a3.terrain = t  and ((a3.startDate >= ?2 and a3.startDate <=?3) or (a3.endDate >= ?2 and a3.endDate <=?3) or (a3.startDate <= ?2 and a3.endDate >=?3)) ) and a2.terrain = t ) = ?1)) order by n.date asc")
    LinkedHashSet<Terrain> findByGroupAndNotificationDateRange(ServiceGroup group, Date start, Date end);

    @Override
    @Query("select distinct t from caTerrainNotificationImpl n join n.assignment a join a.terrain t where n.date>= ?3 and n.date <= ?4 and (a.group = ?2 or ((a.endDate is not null) and (select g2 from caTerrainAssignmentImpl a2 join a2.group g2 where a2.startDate = (select max(a3.startDate) from caTerrainAssignmentImpl a3 where a3.terrain = t  and ((a3.startDate >= ?3 and a3.startDate <=?4) or (a3.endDate >= ?3 and a3.endDate <=?4) or (a3.startDate <= ?3 and a3.endDate >=?4)) ) and a2.terrain = t ) = ?2)) and t.type = ?1 order by n.date asc")
    LinkedHashSet<Terrain> findByTypeAndGroupAndNotificationDateRange(TerrainType type, ServiceGroup group, Date start, Date end);

    @Override
    @Query("select distinct t from caTerrainNotificationImpl n join n.assignment a join a.terrain t where n.date>= ?2 and n.date <= ?3 and t.type=?1 order by n.date asc")
    LinkedHashSet<Terrain> findByTypeAndNotificationDateRange(TerrainType type, Date start, Date end);

    @Override
    @Query("from caTerrainImpl where id in ?1")
    LinkedHashSet<Terrain> findByIds(Collection ids);

    @Override
    @Query("from caTerrainImpl where id not in ?1")
    LinkedHashSet<Terrain> findExcludingIds(Collection ids);
    
    @Override
    @Query("select distinct t from caTerrainImpl t join t.assignments a where a.startDate <= ?1 and t.id not in ?2 order by t.type, t.no")
    LinkedHashSet<Terrain> findByAssignmentDateExcludingIds(Date date, Collection ids);
    
    @Override
    @Query("select distinct t from caTerrainImpl t join t.assignments a where a.group = ?1 and a.startDate <= ?2 and (a.endDate >= ?2 or a.endDate is null) and t.id not in ?3 order by t.type, t.no")
    LinkedHashSet<Terrain> findByGroupAndAssignmentDateExcludingIds(ServiceGroup group, Date date, Collection ids);

    @Override
    @Query("select distinct t from caTerrainImpl t join t.assignments a where t.type=?1 and a.group = ?2 and a.startDate <= ?3 and (a.endDate >= ?3 or a.endDate is null) and t.id not in ?4 order by t.type, t.no")
    LinkedHashSet<Terrain> findByTypeAndGroupAndAssignmentDateExcludingIds(TerrainType type, ServiceGroup group, Date date, Collection ids);

    @Override
    @Query("select distinct t from caTerrainImpl t join t.assignments a where t.type=?1 and a.startDate <= ?2 and (a.endDate >= ?2 or a.endDate is null) and t.id not in ?3 order by t.type, t.no")
    LinkedHashSet<Terrain> findByTypeAndAssignmentDateExcludingIds(TerrainType type, Date date, Collection ids);
}
