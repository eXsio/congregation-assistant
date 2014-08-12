/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.exsio.ca.model.repository;

import java.util.ArrayList;
import java.util.Date;
import org.springframework.data.jpa.repository.Query;
import pl.exsio.ca.model.Preacher;
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
    @Query("select p from ServiceGroupImpl g join g.overseerAssignments a join a.preacher p where g=?1 and a.date <= ?2 order by a.date desc")
    ArrayList<Preacher> getOverseerByDate(ServiceGroup group, Date date);
}
