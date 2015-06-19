/* 
 * The MIT License
 *
 * Copyright 2015 exsio.
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
    @Query("select n from caTerrainNotificationImpl n join n.assignment a join a.terrain t left join a.group g left join a.preacher p  where a.terrain =?1 and n.date >=?2 order by n.date asc")
    LinkedHashSet<TerrainNotification> findForTerrainCard(Terrain terrain, Date date);

    @Override
    @Query("select n from caTerrainNotificationImpl n join n.assignment a join a.terrain t left join a.group g left join a.preacher p where a.terrain =?1 order by n.date asc")
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
