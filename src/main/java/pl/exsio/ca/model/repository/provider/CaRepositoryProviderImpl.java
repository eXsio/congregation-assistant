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
