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
import pl.exsio.ca.model.Preacher;
import pl.exsio.ca.model.PreacherAssignment;
import pl.exsio.ca.model.dao.PreacherAssignmentDao;
import pl.exsio.ca.model.entity.PreacherAssignmentImpl;
import pl.exsio.frameset.core.repository.GenericJpaRepository;

/**
 *
 * @author exsio
 */
public interface PreacherAssignmentRepository extends GenericJpaRepository<PreacherAssignmentImpl, Long>, PreacherAssignmentDao<PreacherAssignmentImpl> {

    @Override
    @Query("from caPreacherAssignmentImpl where preacher =?1 and date > ?2 order by date desc")
    Iterable<PreacherAssignment> findAfter(Preacher preacher, Date date);

    @Override
    @Query("from caPreacherAssignmentImpl where preacher =?1 and date < ?2 order by date desc")
    Iterable<PreacherAssignment> findBefore(Preacher preacher, Date date);

    @Override
    @Query("from caPreacherAssignmentImpl where preacher =?1 and date >= ?2 order by date desc")
    Iterable<PreacherAssignment> findAfterOrEqual(Preacher preacher, Date date);

    @Override
    @Query("from caPreacherAssignmentImpl where preacher =?1 and date <= ?2 order by date desc")
    Iterable<PreacherAssignment> findBeforeOrEqual(Preacher preacher, Date date);

    @Override
    @Modifying
    @Transactional(propagation = Propagation.REQUIRED)
    @Query("update caPreacherAssignmentImpl set active = false where preacher = ?1")
    int deactivateAll(Preacher preacher);

    @Override
    @Query("from caPreacherAssignmentImpl where preacher=?1 and date = (select max(date) from caPreacherAssignmentImpl where preacher = ?1)")
    PreacherAssignment findLatest(Preacher preacher);

    @Override
    @Query("from caPreacherAssignmentImpl where active = true and preacher = ?1")
    PreacherAssignment findActive(Preacher preacher);
    
    @Override
    @Modifying
    @Transactional(propagation = Propagation.REQUIRED)
    @Query("update caPreacherAssignmentImpl set active = true where id = ?1")
    int setActive(Long id);
}
