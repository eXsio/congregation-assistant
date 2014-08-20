/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.model.repository.provider;

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
public class CaRepositoryProviderImpl implements CaRepositoryProvider {

    protected PreacherDao preacherRepository;

    protected PreacherAssignmentDao preacherAssignmentRepository;

    protected OverseerAssignmentDao overseerAssignmentRepository;

    protected PreacherPriviledgeDao preacherPriviledgeRepository;

    protected ServiceGroupDao serviceGroupRepository;

    protected TerrainDao terrainRepository;

    protected TerrainAssignmentDao terrainAssignmentRepository;

    protected TerrainFileDao terrainFileRepository;

    protected TerrainNotificationDao terrainNotificationRepository;

    protected TerrainNoteDao terrainNoteRepository;

    protected EventDao eventRepository;

    @Override
    public PreacherDao getPreacherRepository() {
        return preacherRepository;
    }

    public void setPreacherRepository(PreacherDao preacherRepository) {
        this.preacherRepository = preacherRepository;
    }

    @Override
    public PreacherAssignmentDao getPreacherAssignmentRepository() {
        return preacherAssignmentRepository;
    }

    public void setPreacherAssignmentRepository(PreacherAssignmentDao preacherAssignmentRepository) {
        this.preacherAssignmentRepository = preacherAssignmentRepository;
    }

    @Override
    public PreacherPriviledgeDao getPreacherPriviledgeRepository() {
        return preacherPriviledgeRepository;
    }

    public void setPreacherPriviledgeRepository(PreacherPriviledgeDao preacherPriviledgeRepository) {
        this.preacherPriviledgeRepository = preacherPriviledgeRepository;
    }

    @Override
    public ServiceGroupDao getServiceGroupRepository() {
        return serviceGroupRepository;
    }

    public void setServiceGroupRepository(ServiceGroupDao serviceGroupRepository) {
        this.serviceGroupRepository = serviceGroupRepository;
    }

    @Override
    public TerrainDao getTerrainRepository() {
        return terrainRepository;
    }

    public void setTerrainRepository(TerrainDao terrainRepository) {
        this.terrainRepository = terrainRepository;
    }

    @Override
    public TerrainAssignmentDao getTerrainAssignmentRepository() {
        return terrainAssignmentRepository;
    }

    public void setTerrainAssignmentRepository(TerrainAssignmentDao terrainAssignmentRepository) {
        this.terrainAssignmentRepository = terrainAssignmentRepository;
    }

    @Override
    public TerrainFileDao getTerrainFileRepository() {
        return terrainFileRepository;
    }

    public void setTerrainFileRepository(TerrainFileDao terrainFileRepository) {
        this.terrainFileRepository = terrainFileRepository;
    }

    @Override
    public TerrainNotificationDao getTerrainNotificationRepository() {
        return terrainNotificationRepository;
    }

    public void setTerrainNotificationRepository(TerrainNotificationDao terrainNotificationRepository) {
        this.terrainNotificationRepository = terrainNotificationRepository;
    }

    @Override
    public TerrainNoteDao getTerrainNoteRepository() {
        return terrainNoteRepository;
    }

    public void setTerrainNoteRepository(TerrainNoteDao terrainNoteRepository) {
        this.terrainNoteRepository = terrainNoteRepository;
    }

    @Override
    public OverseerAssignmentDao getOverseerAssignmentRepository() {
        return overseerAssignmentRepository;
    }

    public void setOverseerAssignmentRepository(OverseerAssignmentDao overseerAssignmentRepository) {
        this.overseerAssignmentRepository = overseerAssignmentRepository;
    }

    @Override
    public EventDao getEventRepository() {
        return eventRepository;
    }

    public void setEventRepository(EventDao eventRepository) {
        this.eventRepository = eventRepository;
    }

}
