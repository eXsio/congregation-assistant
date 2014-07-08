/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
