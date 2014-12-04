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
package pl.exsio.ca.model.dao;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import pl.exsio.ca.model.Event;
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

    LinkedHashSet<Terrain> findByLastNotificationDate(Date date);

    LinkedHashSet<Terrain> findByTypeAndLastNotificationDate(TerrainType type, Date date);

    LinkedHashSet<Terrain> findByGroupAndLastNotificationDate(ServiceGroup group, Date date);

    LinkedHashSet<Terrain> findByTypeAndGroupAndLastNotificationDate(TerrainType type, ServiceGroup group, Date date);

    LinkedHashSet<Terrain> findAllTerrains();

    LinkedHashSet<Terrain> findByEvent(Event event);

    LinkedHashSet<Terrain> findByTypeAndEvent(TerrainType type, Event event);

    LinkedHashSet<Terrain> findByGroupAndEvent(ServiceGroup group, Event event);

    LinkedHashSet<Terrain> findByTypeAndGroupAndEvent(TerrainType type, ServiceGroup group, Event event);

    LinkedHashSet<Terrain> findByAssignmentDate(Date date);

    LinkedHashSet<Terrain> findByGroupAndAssignmentDate(ServiceGroup group, Date date);

    LinkedHashSet<Terrain> findByTypeAndGroupAndAssignmentDate(TerrainType type, ServiceGroup group, Date date);

    LinkedHashSet<Terrain> findByTypeAndAssignmentDate(TerrainType type, Date date);

    LinkedHashSet<Terrain> findByNotificationDateRange(Date start, Date end);

    LinkedHashSet<Terrain> findByGroupAndNotificationDateRange(ServiceGroup group, Date start, Date end);

    LinkedHashSet<Terrain> findByTypeAndGroupAndNotificationDateRange(TerrainType type, ServiceGroup group, Date start, Date end);

    LinkedHashSet<Terrain> findByTypeAndNotificationDateRange(TerrainType type, Date start, Date end);

    LinkedHashSet<Terrain> findByIds(Collection ids);

    LinkedHashSet<Terrain> findExcludingIds(Collection ids);

    LinkedHashSet<Terrain> findByAssignmentDateExcludingIds(Date date, Collection ids);

    LinkedHashSet<Terrain> findByGroupAndAssignmentDateExcludingIds(ServiceGroup group, Date date, Collection ids);

    LinkedHashSet<Terrain> findByTypeAndGroupAndAssignmentDateExcludingIds(TerrainType type, ServiceGroup group, Date date, Collection ids);

    LinkedHashSet<Terrain> findByTypeAndAssignmentDateExcludingIds(TerrainType type, Date date, Collection ids);
}
