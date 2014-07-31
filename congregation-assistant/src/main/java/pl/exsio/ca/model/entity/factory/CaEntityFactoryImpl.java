/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.model.entity.factory;

import pl.exsio.ca.model.Preacher;
import pl.exsio.ca.model.PreacherAssignment;
import pl.exsio.ca.model.PreacherPriviledge;
import pl.exsio.ca.model.ServiceGroup;
import pl.exsio.ca.model.Terrain;
import pl.exsio.ca.model.TerrainAssignment;
import pl.exsio.ca.model.TerrainFile;
import pl.exsio.ca.model.TerrainNote;
import pl.exsio.ca.model.TerrainNotification;
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

}
