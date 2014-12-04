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
package pl.exsio.ca.model.entity.factory;

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
import pl.exsio.ca.model.entity.EventImpl;
import pl.exsio.ca.model.entity.OverseerAssignmentImpl;
import pl.exsio.ca.model.entity.PreacherAssignmentImpl;
import pl.exsio.ca.model.entity.PreacherImpl;
import pl.exsio.ca.model.entity.PreacherPriviledgeImpl;
import pl.exsio.ca.model.entity.ServiceGroupImpl;
import pl.exsio.ca.model.entity.TerrainAssignmentImpl;
import pl.exsio.ca.model.entity.TerrainFileImpl;
import pl.exsio.ca.model.entity.TerrainImpl;
import pl.exsio.ca.model.entity.TerrainNoteImpl;
import pl.exsio.ca.model.entity.TerrainNotificationImpl;

public class CaEntityFactoryImpl implements CaEntityFactory {

    @Override
    public <T extends Preacher> T newPreacher() {
        return (T) new PreacherImpl();
    }

    @Override
    public <T extends PreacherAssignment> T newPreacherAssignment() {
        return (T) new PreacherAssignmentImpl();
    }

    @Override
    public <T extends PreacherPriviledge> T newPreacherPriviledge() {
        return (T) new PreacherPriviledgeImpl();
    }

    @Override
    public <T extends ServiceGroup> T newServiceGroup() {
        return (T) new ServiceGroupImpl();
    }

    @Override
    public <T extends Terrain> T newTerrain() {
        return (T) new TerrainImpl();
    }

    @Override
    public <T extends TerrainAssignment> T newTerrainAssignment() {
        return (T) new TerrainAssignmentImpl();
    }

    @Override
    public <T extends TerrainNotification> T newTerrainNotification() {
        return (T) new TerrainNotificationImpl();
    }

    @Override
    public <T extends Preacher> Class<T> getPreacherClass() {
         return (Class<T>) PreacherImpl.class;
    }

    @Override
    public <T extends PreacherAssignment> Class<T> getPreacherAssignmentClass() {
        return (Class<T>) PreacherAssignmentImpl.class;
    }

    @Override
    public <T extends PreacherPriviledge> Class<T> getPreacherPriviledgeClass() {
        return (Class<T>) PreacherPriviledgeImpl.class;
    }

    @Override
    public <T extends ServiceGroup> Class<T> getServiceGroupClass() {
        return (Class<T>) ServiceGroupImpl.class;
    }

    @Override
    public <T extends Terrain> Class<T> getTerrainClass() {
        return (Class<T>) TerrainImpl.class;
    }

    @Override
    public <T extends TerrainAssignment> Class<T> getTerrainAssignmentClass() {
        return (Class<T>) TerrainAssignmentImpl.class;
    }

    @Override
    public <T extends TerrainNotification> Class<T> getTerrainNotificationClass() {
        return (Class<T>) TerrainNotificationImpl.class;
    }

    @Override
    public <T extends TerrainNote> T newTerrainNote() {
        return (T) new TerrainNoteImpl();
    }

    @Override
    public <T extends TerrainNote> Class<T> getTerrainNoteClass() {
        return (Class<T>) TerrainNoteImpl.class;
    }

    @Override
    public <T extends TerrainFile> T newTerrainFile() {
        return (T) new TerrainFileImpl();
    }

    @Override
    public <T extends TerrainFile> Class<T> getTerrainFileClass() {
        return (Class<T>) TerrainFileImpl.class;
    }

    @Override
    public <T extends OverseerAssignment> T newOverseerAssignment() {
        return (T) new OverseerAssignmentImpl();
    }

    @Override
    public <T extends OverseerAssignment> Class<T> getOverseerAssignmentClass() {
        return (Class<T>) OverseerAssignmentImpl.class;
    }

    @Override
    public <T extends Event> T newEvent() {
        return (T) new EventImpl();
    }

    @Override
    public <T extends Event> Class<T> getEventClass() {
        return (Class<T>) EventImpl.class;
    }

}
