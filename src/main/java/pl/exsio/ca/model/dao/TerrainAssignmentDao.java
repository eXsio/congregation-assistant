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
package pl.exsio.ca.model.dao;

import java.util.Date;
import java.util.LinkedHashSet;
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
    
    Iterable<TerrainAssignment> findByTerrain(Terrain terrain);

}
