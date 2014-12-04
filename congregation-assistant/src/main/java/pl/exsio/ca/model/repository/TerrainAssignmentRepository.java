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

import java.util.Date;
import java.util.LinkedHashSet;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.exsio.ca.model.Terrain;
import pl.exsio.ca.model.TerrainAssignment;
import pl.exsio.ca.model.dao.TerrainAssignmentDao;
import pl.exsio.ca.model.entity.TerrainAssignmentImpl;
import pl.exsio.frameset.core.repository.GenericJpaRepository;

/**
 *
 * @author exsio
 */
public interface TerrainAssignmentRepository extends GenericJpaRepository<TerrainAssignmentImpl, Long>, TerrainAssignmentDao<TerrainAssignmentImpl> {
    
    @Override
    @Query("from caTerrainAssignmentImpl where terrain =?1 and startDate > ?2 order by startDate desc")
    Iterable<TerrainAssignment> findAfter(Terrain terrain, Date date);

    @Override
    @Query("from caTerrainAssignmentImpl where terrain =?1 and startDate < ?2 order by startDate desc")
    Iterable<TerrainAssignment> findBefore(Terrain terrain, Date date);

    @Override
    @Query("from caTerrainAssignmentImpl where terrain =?1 and startDate >= ?2 order by startDate desc")
    Iterable<TerrainAssignment> findAfterOrEqual(Terrain terrain, Date date);

    @Override
    @Query("from caTerrainAssignmentImpl where terrain =?1 and startDate <= ?2 order by startDate desc")
    Iterable<TerrainAssignment> findBeforeOrEqual(Terrain terrain, Date date);

    @Override
    @Modifying
    @Transactional(propagation = Propagation.REQUIRED)
    @Query("update caTerrainAssignmentImpl set active = false where terrain = ?1")
    int deactivateAll(Terrain terrain);

    @Override
    @Query("from caTerrainAssignmentImpl where terrain=?1 and startDate = (select max(startDate) from caTerrainAssignmentImpl where terrain = ?1)")
    TerrainAssignment findLatest(Terrain terrain);

    @Override
    @Query("from caTerrainAssignmentImpl where active = true and terrain = ?1")
    TerrainAssignment findActive(Terrain terrain);
    
    @Override
    @Modifying
    @Transactional(propagation = Propagation.REQUIRED)
    @Query("update caTerrainAssignmentImpl set active = true where id = ?1")
    int setActive(Long id);
    
    @Override
    @Query("from caTerrainAssignmentImpl where terrain = ?1 and startDate <= ?2 and (endDate is null or endDate >= ?2) order by startDate desc")
    Iterable<TerrainAssignment> findByTerrainAndDate(Terrain terrain, Date date);
}
