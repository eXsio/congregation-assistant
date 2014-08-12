/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.model.repository;

import java.util.Date;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.exsio.ca.model.OverseerAssignment;
import pl.exsio.ca.model.Preacher;
import pl.exsio.ca.model.ServiceGroup;
import pl.exsio.ca.model.dao.OverseerAssignmentDao;
import pl.exsio.ca.model.entity.OverseerAssignmentImpl;
import pl.exsio.frameset.core.repository.GenericJpaRepository;

/**
 *
 * @author exsio
 */
public interface OverseerAssignmentRepository extends GenericJpaRepository<OverseerAssignmentImpl, Long>, OverseerAssignmentDao<OverseerAssignmentImpl> {

    @Override
    @Query("from OverseerAssignmentImpl where group =?1 and date > ?2 order by date desc")
    Iterable<OverseerAssignment> findAfter(ServiceGroup group, Date date);

    @Override
    @Query("from OverseerAssignmentImpl where group =?1 and date < ?2 order by date desc")
    Iterable<OverseerAssignment> findBefore(ServiceGroup group, Date date);

    @Override
    @Query("from OverseerAssignmentImpl where group =?1 and date >= ?2 order by date desc")
    Iterable<OverseerAssignment> findAfterOrEqual(ServiceGroup group, Date date);

    @Override
    @Query("from OverseerAssignmentImpl where group =?1 and date <= ?2 order by date desc")
    Iterable<OverseerAssignment> findBeforeOrEqual(ServiceGroup group, Date date);

    @Override
    @Modifying
    @Transactional(propagation = Propagation.REQUIRED)
    @Query("update OverseerAssignmentImpl set active = false where group = ?1")
    int deactivateAll(ServiceGroup group);

    @Override
    @Query("from OverseerAssignmentImpl where group=?1 and date = (select max(date) from OverseerAssignmentImpl where group = ?1)")
    OverseerAssignment findLatest(ServiceGroup group);

    @Override
    @Query("from OverseerAssignmentImpl where active = true and group = ?1")
    OverseerAssignment findActive(ServiceGroup group);
    
    @Override
    @Modifying
    @Transactional(propagation = Propagation.REQUIRED)
    @Query("update OverseerAssignmentImpl set active = true where id = ?1")
    int setActive(Long id);
}
