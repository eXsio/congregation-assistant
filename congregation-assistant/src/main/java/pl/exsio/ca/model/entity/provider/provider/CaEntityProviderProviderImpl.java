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
package pl.exsio.ca.model.entity.provider.provider;

import com.vaadin.addon.jpacontainer.EntityProvider;

/**
 *
 * @author exsio
 */
public class CaEntityProviderProviderImpl implements CaEntityProviderProvider {

    private EntityProvider preacherAssignmentEntityProvider;

    private EntityProvider overseerAssignmentEntityProvider;

    private EntityProvider preacherEntityProvider;

    private EntityProvider preacherPriviledgeEntityProvider;

    private EntityProvider serviceGroupEntityProvider;

    private EntityProvider terrainAssignmentEntityProvider;

    private EntityProvider terrainEntityProvider;

    private EntityProvider terrainFileEntityProvider;

    private EntityProvider terrainNoteEntityProvider;

    private EntityProvider terrainNotificationEntityProvider;

    private EntityProvider eventEntityProvider;

    @Override
    public EntityProvider getPreacherAssignmentEntityProvider() {
        return preacherAssignmentEntityProvider;
    }

    public void setPreacherAssignmentEntityProvider(EntityProvider preacherAssignmentEntityProvider) {
        this.preacherAssignmentEntityProvider = preacherAssignmentEntityProvider;
    }

    @Override
    public EntityProvider getPreacherEntityProvider() {
        return preacherEntityProvider;
    }

    public void setPreacherEntityProvider(EntityProvider preacherEntityProvider) {
        this.preacherEntityProvider = preacherEntityProvider;
    }

    @Override
    public EntityProvider getPreacherPriviledgeEntityProvider() {
        return preacherPriviledgeEntityProvider;
    }

    public void setPreacherPriviledgeEntityProvider(EntityProvider preacherPriviledgeEntityProvider) {
        this.preacherPriviledgeEntityProvider = preacherPriviledgeEntityProvider;
    }

    @Override
    public EntityProvider getServiceGroupEntityProvider() {
        return serviceGroupEntityProvider;
    }

    public void setServiceGroupEntityProvider(EntityProvider serviceGroupEntityProvider) {
        this.serviceGroupEntityProvider = serviceGroupEntityProvider;
    }

    @Override
    public EntityProvider getTerrainAssignmentEntityProvider() {
        return terrainAssignmentEntityProvider;
    }

    public void setTerrainAssignmentEntityProvider(EntityProvider terrainAssignmentEntityProvider) {
        this.terrainAssignmentEntityProvider = terrainAssignmentEntityProvider;
    }

    @Override
    public EntityProvider getTerrainEntityProvider() {
        return terrainEntityProvider;
    }

    public void setTerrainEntityProvider(EntityProvider terrainEntityProvider) {
        this.terrainEntityProvider = terrainEntityProvider;
    }

    @Override
    public EntityProvider getTerrainFileEntityProvider() {
        return terrainFileEntityProvider;
    }

    public void setTerrainFileEntityProvider(EntityProvider terrainFileEntityProvider) {
        this.terrainFileEntityProvider = terrainFileEntityProvider;
    }

    @Override
    public EntityProvider getTerrainNoteEntityProvider() {
        return terrainNoteEntityProvider;
    }

    public void setTerrainNoteEntityProvider(EntityProvider terrainNoteEntityProvider) {
        this.terrainNoteEntityProvider = terrainNoteEntityProvider;
    }

    @Override
    public EntityProvider getTerrainNotificationEntityProvider() {
        return terrainNotificationEntityProvider;
    }

    public void setTerrainNotificationEntityProvider(EntityProvider terrainNotificationEntityProvider) {
        this.terrainNotificationEntityProvider = terrainNotificationEntityProvider;
    }

    @Override
    public EntityProvider getOverseerAssignmentEntityProvider() {
        return overseerAssignmentEntityProvider;
    }

    public void setOverseerAssignmentEntityProvider(EntityProvider overseerAssignmentEntityProvider) {
        this.overseerAssignmentEntityProvider = overseerAssignmentEntityProvider;
    }

    @Override
    public EntityProvider getEventEntityProvider() {
        return eventEntityProvider;
    }

    public void setEventEntityProvider(EntityProvider eventEntityProvider) {
        this.eventEntityProvider = eventEntityProvider;
    }

}
