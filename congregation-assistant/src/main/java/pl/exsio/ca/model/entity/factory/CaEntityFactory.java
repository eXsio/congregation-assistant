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
