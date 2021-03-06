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

import java.util.ArrayList;
import java.util.Date;
import org.springframework.data.jpa.repository.Query;
import pl.exsio.ca.model.OverseerAssignment;
import pl.exsio.ca.model.ServiceGroup;
import pl.exsio.ca.model.dao.ServiceGroupDao;
import pl.exsio.ca.model.entity.ServiceGroupImpl;
import pl.exsio.frameset.core.repository.GenericJpaRepository;

/**
 *
 * @author exsio
 */
public interface ServiceGroupRepository extends GenericJpaRepository<ServiceGroupImpl, Long>, ServiceGroupDao<ServiceGroupImpl> {
    
    @Override
    @Query("select a from caServiceGroupImpl g join g.overseerAssignments a where g=?1 and a.date <= ?2 order by a.date desc")
    ArrayList<OverseerAssignment> getOverseerAssignmentByDate(ServiceGroup group, Date date);
}
