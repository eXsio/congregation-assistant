/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

}
