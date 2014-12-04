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

    Set<TerrainNotification> findByEventAndGroup(Event event, ServiceGroup group);

    Set<TerrainNotification> findByEventAndTerrainType(Event event, TerrainType type);

    Set<TerrainNotification> findByEventAndGroupAndTerrainType(Event event, ServiceGroup group, TerrainType type);

    Set<TerrainNotification> findByDateRange(Date start, Date end);

    Set<TerrainNotification> findByDateRangeAndGroup(Date start, Date end, ServiceGroup group);

    Set<TerrainNotification> findByDateRangeAndTerrainType(Date start, Date end, TerrainType type);

    Set<TerrainNotification> findByDateRangeAndGroupAndTerrainType(Date start, Date end, ServiceGroup group, TerrainType type);
}
