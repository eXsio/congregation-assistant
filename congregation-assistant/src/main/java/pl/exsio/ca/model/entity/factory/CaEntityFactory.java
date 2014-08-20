/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

/**
 *
 * @author exsio
 */
public interface CaEntityFactory {
    
    <T extends Preacher> T newPreacher();
    
    <T extends PreacherAssignment> T newPreacherAssignment();
    
    <T extends OverseerAssignment> T newOverseerAssignment();
    
    <T extends PreacherPriviledge> T newPreacherPriviledge();
    
    <T extends ServiceGroup> T newServiceGroup();
    
    <T extends Terrain> T newTerrain();
    
    <T extends TerrainFile> T newTerrainFile();
    
    <T extends TerrainAssignment> T newTerrainAssignment();
    
    <T extends TerrainNotification> T newTerrainNotification();
    
    <T extends TerrainNote> T newTerrainNote();
    
    <T extends Event> T newEvent();
    
    <T extends Preacher> Class<T> getPreacherClass();
    
    <T extends PreacherAssignment> Class<T> getPreacherAssignmentClass();
    
    <T extends OverseerAssignment> Class<T> getOverseerAssignmentClass();
    
    <T extends PreacherPriviledge> Class<T> getPreacherPriviledgeClass();
    
    <T extends ServiceGroup> Class<T> getServiceGroupClass();
    
    <T extends Terrain> Class<T> getTerrainClass();
    
    <T extends TerrainAssignment> Class<T> getTerrainAssignmentClass();
    
    <T extends TerrainNotification> Class<T> getTerrainNotificationClass();
    
    <T extends TerrainNote> Class<T> getTerrainNoteClass();
    
    <T extends TerrainFile> Class<T> getTerrainFileClass();
    
    <T extends Event> Class<T> getEventClass();
}
