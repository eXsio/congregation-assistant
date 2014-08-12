/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.exsio.ca.model.dao;

import java.util.ArrayList;
import java.util.Date;
import pl.exsio.ca.model.Preacher;
import pl.exsio.ca.model.ServiceGroup;
import pl.exsio.frameset.core.dao.GenericDao;

/**
 *
 * @author exsio
 */
public interface ServiceGroupDao<T extends ServiceGroup> extends GenericDao<T, Long> {
    
    ArrayList<Preacher> getOverseerByDate(ServiceGroup group, Date date);
}
