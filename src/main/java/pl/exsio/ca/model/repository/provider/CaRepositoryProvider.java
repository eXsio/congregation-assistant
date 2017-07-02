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
