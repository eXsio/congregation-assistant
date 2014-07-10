/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
    @Query("from TerrainAssignmentImpl where terrain =?1 and startDate > ?2 order by startDate desc")
    Iterable<TerrainAssignment> findAfter(Terrain terrain, Date date);

    @Override
    @Query("from TerrainAssignmentImpl where terrain =?1 and startDate < ?2 order by startDate desc")
    Iterable<TerrainAssignment> findBefore(Terrain terrain, Date date);

    @Override
    @Query("from TerrainAssignmentImpl where terrain =?1 and startDate >= ?2 order by startDate desc")
    Iterable<TerrainAssignment> findAfterOrEqual(Terrain terrain, Date date);

    @Override
    @Query("from TerrainAssignmentImpl where terrain =?1 and startDate <= ?2 order by startDate desc")
    Iterable<TerrainAssignment> findBeforeOrEqual(Terrain terrain, Date date);

    @Override
    @Modifying
    @Transactional(propagation = Propagation.REQUIRED)
    @Query("update TerrainAssignmentImpl set active = false where terrain = ?1")
    int deactivateAll(Terrain terrain);

    @Override
    @Query("from TerrainAssignmentImpl where terrain=?1 and startDate = (select max(startDate) from TerrainAssignmentImpl where terrain = ?1)")
    TerrainAssignment findLatest(Terrain terrain);

    @Override
    @Query("from TerrainAssignmentImpl where active = true and terrain = ?1")
    TerrainAssignment findActive(Terrain terrain);
    
    @Override
    @Modifying
    @Transactional(propagation = Propagation.REQUIRED)
    @Query("update TerrainAssignmentImpl set active = true where id = ?1")
    int setActive(Long id);
    
    @Override
    @Query("from TerrainAssignmentImpl where terrain = ?1 and startDate <= ?2 and (endDate is null or endDate >= ?2) order by startDate desc")
    Iterable<TerrainAssignment> findByTerrainAndDate(Terrain terrain, Date date);
    
    @Override
    @Query("from TerrainAssignmentImpl where terrain =?1 and startDate >= ?2 order by startDate asc")
    LinkedHashSet<TerrainAssignment> findForTerrainCard(Terrain terrain, Date date);
    
    @Override
    @Query("from TerrainAssignmentImpl where terrain =?1 order by startDate asc")
    LinkedHashSet<TerrainAssignment> findForTerrainCard(Terrain terrain);
}
