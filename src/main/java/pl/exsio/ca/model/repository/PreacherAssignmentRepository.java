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
