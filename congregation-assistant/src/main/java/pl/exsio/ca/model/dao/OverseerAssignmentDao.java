/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.exsio.ca.model.dao;

import java.util.Date;
import pl.exsio.ca.model.OverseerAssignment;
import pl.exsio.ca.model.Preacher;
import pl.exsio.ca.model.ServiceGroup;
import pl.exsio.frameset.core.dao.GenericDao;

/**
 *
 * @author exsio
 */
public interface OverseerAssignmentDao<T extends OverseerAssignment> extends GenericDao<T, Long> {
    
    Iterable<OverseerAssignment> findByGroupAndActive(ServiceGroup group, boolean active);
    
    Iterable<OverseerAssignment> findAfter(ServiceGroup group, Date date);
    
    Iterable<OverseerAssignment> findBefore(ServiceGroup group, Date date);
    
    Iterable<OverseerAssignment> findAfterOrEqual(ServiceGroup group, Date date);
    
    Iterable<OverseerAssignment> findBeforeOrEqual(ServiceGroup group, Date date);
    
    int deactivateAll(ServiceGroup group);
    
    OverseerAssignment findLatest(ServiceGroup group);
    
    OverseerAssignment findActive(ServiceGroup group);
    
    int setActive(Long id);
}
