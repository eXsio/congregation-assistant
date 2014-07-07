/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.exsio.ca.model.repository.provider;

import pl.exsio.ca.model.dao.PreacherAssignmentDao;
import pl.exsio.ca.model.dao.PreacherDao;
import pl.exsio.ca.model.dao.PreacherPriviledgeDao;
import pl.exsio.ca.model.dao.ServiceGroupDao;
import pl.exsio.ca.model.dao.TerrainAssignmentDao;
import pl.exsio.ca.model.dao.TerrainDao;
import pl.exsio.ca.model.dao.TerrainFileDao;
import pl.exsio.ca.model.dao.TerrainNotificationDao;

/**
 *
 * @author exsio
 */
public interface CaRepositoryProvider {
    
    PreacherDao getPreacherRepository();
    
    PreacherAssignmentDao getPreacherAssignmentRepository();
    
    PreacherPriviledgeDao getPreacherPriviledgeRepository();
    
    ServiceGroupDao getServiceGroupRepository();
    
    TerrainDao getTerrainRepository();
    
    TerrainAssignmentDao getTerrainAssignmentRepository();
    
    TerrainFileDao getTerrainFileRepository();
    
    TerrainNotificationDao getTerrainNotificationRepository();
}
