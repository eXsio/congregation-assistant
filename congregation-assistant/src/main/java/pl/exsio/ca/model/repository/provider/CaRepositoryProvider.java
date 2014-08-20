/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.model.repository.provider;

import pl.exsio.ca.model.Event;
import pl.exsio.ca.model.OverseerAssignment;
import pl.exsio.ca.model.Preacher;
import pl.exsio.ca.model.PreacherAssignment;
import pl.exsio.ca.model.PreacherPriviledge;
import pl.exsio.ca.model.ServiceGroup;
import pl.exsio.ca.model.Terrain;
import pl.exsio.ca.model.TerrainAssignment;
import pl.exsio.ca.model.TerrainFile;
import pl.exsio.ca.model.TerrainNote;
import pl.exsio.ca.model.TerrainNotification;
import pl.exsio.ca.model.dao.EventDao;
import pl.exsio.ca.model.dao.OverseerAssignmentDao;
import pl.exsio.ca.model.dao.PreacherAssignmentDao;
import pl.exsio.ca.model.dao.PreacherDao;
import pl.exsio.ca.model.dao.PreacherPriviledgeDao;
import pl.exsio.ca.model.dao.ServiceGroupDao;
import pl.exsio.ca.model.dao.TerrainAssignmentDao;
import pl.exsio.ca.model.dao.TerrainDao;
import pl.exsio.ca.model.dao.TerrainFileDao;
import pl.exsio.ca.model.dao.TerrainNoteDao;
import pl.exsio.ca.model.dao.TerrainNotificationDao;

/**
 *
 * @author exsio
 */
public interface CaRepositoryProvider {

    PreacherDao<Preacher> getPreacherRepository();

    PreacherAssignmentDao<PreacherAssignment> getPreacherAssignmentRepository();
    
    OverseerAssignmentDao<OverseerAssignment> getOverseerAssignmentRepository();

    PreacherPriviledgeDao<PreacherPriviledge> getPreacherPriviledgeRepository();

    ServiceGroupDao<ServiceGroup> getServiceGroupRepository();

    TerrainDao<Terrain> getTerrainRepository();

    TerrainAssignmentDao<TerrainAssignment> getTerrainAssignmentRepository();

    TerrainFileDao<TerrainFile> getTerrainFileRepository();

    TerrainNotificationDao<TerrainNotification> getTerrainNotificationRepository();

    TerrainNoteDao<TerrainNote> getTerrainNoteRepository();
    
    EventDao<Event> getEventRepository();
}
