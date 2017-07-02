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
import pl.exsio.ca.model.OverseerAssignment;
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
    @Query("from caOverseerAssignmentImpl where group =?1 and date > ?2 order by date desc")
    Iterable<OverseerAssignment> findAfter(ServiceGroup group, Date date);

    @Override
    @Query("from caOverseerAssignmentImpl where group =?1 and date < ?2 order by date desc")
    Iterable<OverseerAssignment> findBefore(ServiceGroup group, Date date);

    @Override
    @Query("from caOverseerAssignmentImpl where group =?1 and date >= ?2 order by date desc")
    Iterable<OverseerAssignment> findAfterOrEqual(ServiceGroup group, Date date);

    @Override
    @Query("from caOverseerAssignmentImpl where group =?1 and date <= ?2 order by date desc")
    Iterable<OverseerAssignment> findBeforeOrEqual(ServiceGroup group, Date date);

    @Override
    @Modifying
    @Transactional(propagation = Propagation.REQUIRED)
    @Query("update caOverseerAssignmentImpl set active = false where group = ?1")
    int deactivateAll(ServiceGroup group);

    @Override
    @Query("from caOverseerAssignmentImpl where group=?1 and date = (select max(date) from caOverseerAssignmentImpl where group = ?1)")
    OverseerAssignment findLatest(ServiceGroup group);

    @Override
    @Query("from caOverseerAssignmentImpl where active = true and group = ?1")
    OverseerAssignment findActive(ServiceGroup group);
    
    @Override
    @Modifying
    @Transactional(propagation = Propagation.REQUIRED)
    @Query("update caOverseerAssignmentImpl set active = true where id = ?1")
    int setActive(Long id);
}
