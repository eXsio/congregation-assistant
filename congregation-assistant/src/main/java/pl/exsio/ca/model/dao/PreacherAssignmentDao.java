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
import pl.exsio.ca.model.Preacher;
import pl.exsio.ca.model.PreacherAssignment;
import pl.exsio.frameset.core.dao.GenericDao;

/**
 *
 * @author exsio
 */
public interface PreacherAssignmentDao<T extends PreacherAssignment> extends GenericDao<T, Long> {
    
    Iterable<PreacherAssignment> findByPreacherAndActive(Preacher preacher, boolean active);
    
    Iterable<PreacherAssignment> findAfter(Preacher preacher, Date date);
    
    Iterable<PreacherAssignment> findBefore(Preacher preacher, Date date);
    
    Iterable<PreacherAssignment> findAfterOrEqual(Preacher preacher, Date date);
    
    Iterable<PreacherAssignment> findBeforeOrEqual(Preacher preacher, Date date);
    
    int deactivateAll(Preacher preacher);
    
    PreacherAssignment findLatest(Preacher preacher);
    
    PreacherAssignment findActive(Preacher preacher);
    
    int setActive(Long id);
}
